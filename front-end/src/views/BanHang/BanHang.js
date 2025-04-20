import { ref, computed, watch } from "vue";
import axios from "axios";

const generateRandomCode = () => {
  const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  let randomCode = "";
  for (let i = 0; i < 5; i++) {
    const randomIndex = Math.floor(Math.random() * characters.length);
    randomCode += characters[randomIndex];
  }
  return `HD${randomCode}`;
};

export function useBanHang(toastRef) {
  const cartItems = ref([]);
  const cartColumns = ref([
    { key: "id", label: "STT" },
    { key: "name", label: "Sản phẩm" },
    { key: "price", label: "Đơn giá", formatter: (value) => `${value.toLocaleString()} đ` },
    { key: "imei", label: "IMEI", formatter: (value) => value || "N/A" },
    { key: "actions", label: "Xóa", cellSlot: "actionsSlot" },
  ]);
  const searchCustomer = ref("");
  const selectedCustomer = ref(null);
  const customer = ref({ name: "", phone: "", city: "", district: "", ward: "", address: "" });
  const receiver = ref({
    name: "",
    phone: "",
    city: "",
    district: "",
    ward: "",
    address: ""
  });
  const discountCode = ref("");
  const discount = ref(0);
  const orderNotes = ref("");
  const payOnDelivery = ref(false);
  const paymentMethod = ref("");
  const activeInvoiceId = ref(null);
  const pendingInvoices = ref([]);
  const showProductModal = ref(false);
  const showIMEIModal = ref(false);
  const products = ref([]);
  const filteredProducts = ref([]);
  const productSearchQuery = ref("");
  const selectedProduct = ref(null);
  const selectedQuantity = ref(1);
  const availableIMEIs = ref([]);
  const selectedIMEIs = ref([]);
  const gioHangId = ref(null);
  const tienChuyenKhoan = ref(0);
  const tienMat = ref(0);

  const totalPrice = computed(() => {
    return cartItems.value.reduce((total, item) => total + item.price, 0);
  });

  const calculateDiscount = () => {
    const total = totalPrice.value;
    let newDiscount = 0;

    if (total > 40000000) {
      newDiscount = 1500000;
      if (toastRef.value && discount.value !== newDiscount) {
        toastRef.value.kshowToast("success", "Đã áp dụng giảm giá 1.500.000 đ cho hóa đơn trên 40 triệu!");
      }
    } else if (total > 30000000) {
      newDiscount = 1000000;
      if (toastRef.value && discount.value !== newDiscount) {
        toastRef.value.kshowToast("success", "Đã áp dụng giảm giá 1.000.000 đ cho hóa đơn trên 30 triệu!");
      }
    } else if (total > 15000000) {
      newDiscount = 500000;
      if (toastRef.value && discount.value !== newDiscount) {
        toastRef.value.kshowToast("success", "Đã áp dụng giảm giá 500.000 đ cho hóa đơn trên 15 triệu!");
      }
    } else {
      newDiscount = 0;
      if (toastRef.value && discount.value !== 0) {
        toastRef.value.kshowToast("info", "Hóa đơn dưới 15 triệu, không áp dụng giảm giá.");
      }
    }

    discount.value = newDiscount;
  };

  watch(totalPrice, () => {
    calculateDiscount();
  });

  const fetchPendingInvoices = async () => {
    try {
      const response = await axios.get("http://localhost:8080/ban-hang/data");
      pendingInvoices.value = response.data.map((hd) => ({
        id: hd.id,
        code: hd.maHoaDon,
        status: hd.trangThai === 0 ? "Chờ" : "Đã thanh toán",
        items: hd.items.map((item) => ({
          id: item.id,
          name: item.tenSanPham,
          price: item.giaBan,
          imei: item.imei,
        })),
      }));

      const storedInvoiceId = localStorage.getItem("activeInvoiceId");
      if (storedInvoiceId) {
        activeInvoiceId.value = parseInt(storedInvoiceId);
        const selectedInvoice = pendingInvoices.value.find((i) => i.id === activeInvoiceId.value);
        if (selectedInvoice) {
          await loadPendingInvoice(selectedInvoice);
        } else {
          localStorage.removeItem("activeInvoiceId");
          activeInvoiceId.value = null;
        }
      }
    } catch (error) {
      console.error("Lỗi khi lấy danh sách hóa đơn:", error);
      if (toastRef.value) toastRef.value.kshowToast("error", "Không thể tải danh sách hóa đơn!");
    }
  };

  const fetchProducts = async () => {
    try {
      const response = await axios.get("http://localhost:8080/ban-hang/san-pham");
      products.value = response.data.map((p) => ({
        id: p.id,
        tenSanPham: p.tenSanPham,
        ma: p.ma,
        giaBan: p.giaBan,
      }));
      filteredProducts.value = products.value;
    } catch (error) {
      console.error("Lỗi khi lấy danh sách sản phẩm:", error);
      if (toastRef.value) toastRef.value.kshowToast("error", "Không thể tải danh sách sản phẩm!");
    }
  };

  const fetchCartItems = async () => {
    const storedGioHangId = localStorage.getItem("gioHangId");
    if (storedGioHangId) {
      gioHangId.value = parseInt(storedGioHangId);
      try {
        const response = await axios.get(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`);
        cartItems.value = response.data.map((item) => ({
          id: item.id,
          name: item.tenSanPham,
          price: item.giaBan,
          imei: item.imei,
        }));
        calculateDiscount();
      } catch (error) {
        console.error("Lỗi khi lấy danh sách chi tiết giỏ hàng:", error);
        if (toastRef.value) toastRef.value.kshowToast("error", "Không thể tải giỏ hàng!");
      }
    }
  };

  fetchPendingInvoices();
  fetchCartItems();

  const getNestedValue = (item, key) => {
    return key === "imei" ? item.imei : (key.split(".").reduce((obj, k) => (obj && obj[k] !== undefined ? obj[k] : null), item) || "N/A");
  };

  const editItem = (item) => {
    if (toastRef.value) toastRef.value.kshowToast("info", `Chỉnh sửa sản phẩm: ${item.name}`);
  };

  const toggleStatus = (item) => {
    if (toastRef.value) toastRef.value.kshowToast("success", `Đã thay đổi trạng thái sản phẩm: ${item.name}`);
  };

  const removeItem = async (item) => {
    try {
      await axios.delete(`http://localhost:8080/ban-hang/gio-hang/chi-tiet/${item.id}`);
      cartItems.value = cartItems.value.filter((i) => i.id !== item.id);
      const invoice = pendingInvoices.value.find((i) => i.id === activeInvoiceId.value);
      if (invoice) {
        invoice.items = invoice.items.filter((i) => i.id !== item.id);
      }
      if (toastRef.value) toastRef.value.kshowToast("success", `Đã xóa sản phẩm: ${item.name}`);
      calculateDiscount();
    } catch (error) {
      console.error("Lỗi khi xóa sản phẩm:", error);
      if (toastRef.value) toastRef.value.kshowToast("error", "Không thể xóa sản phẩm!");
    }
  };

  const createNewPendingInvoice = async () => {
    const newInvoice = {
      maHoaDon: generateRandomCode(),
      trangThai: 0,
    };

    try {
      const gioHangResponse = await axios.post("http://localhost:8080/ban-hang/addGioHang", {
        ma: `GH_${newInvoice.maHoaDon}`,
        idKhachHang: 1,
      });
      gioHangId.value = gioHangResponse.data.id;
      localStorage.setItem("gioHangId", gioHangId.value);

      const response = await axios.post("http://localhost:8080/ban-hang/addHD", newInvoice);
      pendingInvoices.value.unshift({
        id: response.data.id,
        code: response.data.ma,
        status: response.data.trangThai === 0 ? "Chờ" : "Đã thanh toán",
        items: [],
      });
      await loadPendingInvoice(pendingInvoices.value[0]);
      if (toastRef.value) toastRef.value.kshowToast("success", `Đã tạo hóa đơn chờ mới: ${response.data.ma}`);
    } catch (error) {
      console.error("Lỗi khi tạo hóa đơn mới:", error);
      if (toastRef.value) toastRef.value.kshowToast("error", "Không thể tạo hóa đơn mới!");
    }
  };

  const loadPendingInvoice = async (invoice) => {
    activeInvoiceId.value = invoice.id;
    localStorage.setItem("activeInvoiceId", invoice.id);

    if (!gioHangId.value) {
      const gioHangResponse = await axios.post("http://localhost:8080/ban-hang/addGioHang", {
        ma: `GH_${invoice.code}`,
        idKhachHang: 1,
      });
      gioHangId.value = gioHangResponse.data.id;
      localStorage.setItem("gioHangId", gioHangId.value);
    }

    try {
      const response = await axios.get(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`);
      cartItems.value = response.data.map((item) => ({
        id: item.id,
        name: item.tenSanPham,
        price: item.giaBan,
        imei: item.imei,
      }));
      const invoiceToUpdate = pendingInvoices.value.find((i) => i.id === invoice.id);
      if (invoiceToUpdate) {
        invoiceToUpdate.items = [...cartItems.value];
      }
      calculateDiscount();
    } catch (error) {
      console.error("Lỗi khi tải sản phẩm của hóa đơn:", error);
      cartItems.value = [];
    }

    if (toastRef.value) toastRef.value.kshowToast("info", `Đã tải hóa đơn chờ: ${invoice.code}`);
  };

  const scanQR = () => {
    if (toastRef.value) toastRef.value.kshowToast("info", "Đang quét QR...");
  };

  const openProductModal = async () => {
    await fetchProducts();
    showProductModal.value = true;
    productSearchQuery.value = "";
  };

  const closeProductModal = () => {
    showProductModal.value = false;
  };

  const showIMEIList = async (product) => {
    selectedProduct.value = product;
    try {
      const response = await axios.get(`http://localhost:8080/ban-hang/san-pham/${product.id}/imeis`);
      availableIMEIs.value = response.data;
      selectedIMEIs.value = [];
      selectedQuantity.value = 1;
      showIMEIModal.value = true;
    } catch (error) {
      console.error("Lỗi khi lấy danh sách IMEI:", error);
      if (toastRef.value) toastRef.value.kshowToast("error", "Không thể tải danh sách IMEI!");
    }
  };

  const closeIMEIModal = () => {
    showIMEIModal.value = false;
    selectedProduct.value = null;
    availableIMEIs.value = [];
    selectedIMEIs.value = [];
    selectedQuantity.value = 1;
  };

  const searchProducts = () => {
    if (!productSearchQuery.value) {
      filteredProducts.value = products.value;
    } else {
      filteredProducts.value = products.value.filter((product) =>
        product.tenSanPham.toLowerCase().includes(productSearchQuery.value.toLowerCase()) ||
        product.ma.toLowerCase().includes(productSearchQuery.value.toLowerCase())
      );
    }
  };

  const addProductWithIMEIs = async () => {
    if (!activeInvoiceId.value) {
      if (toastRef.value) toastRef.value.kshowToast("error", "Vui lòng chọn hoặc tạo hóa đơn trước!");
      return;
    }

    if (selectedIMEIs.value.length !== selectedQuantity.value) {
      if (toastRef.value) toastRef.value.kshowToast("error", "Số lượng IMEI chọn phải bằng số lượng sản phẩm!");
      return;
    }

    try {
      if (!gioHangId.value) {
        const gioHangResponse = await axios.post("http://localhost:8080/ban-hang/addGioHang", {
          ma: generateRandomCode(),
          idKhachHang: 1,
        });
        gioHangId.value = gioHangResponse.data.id;
        localStorage.setItem("gioHangId", gioHangId.value);
      }

      for (const imei of selectedIMEIs.value) {
        const chiTietSanPham = (await axios.get(`http://localhost:8080/ban-hang/san-pham?imei=${imei}`)).data.find(
          (p) => p.idImel.ma === imei
        );
        if (!chiTietSanPham) throw new Error("Không tìm thấy sản phẩm với IMEI: " + imei);

        const response = await axios.post(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`, {
          idChiTietSanPham: chiTietSanPham.id,
          hoaDonId: activeInvoiceId.value,
        });

        const newItem = {
          id: response.data.id,
          name: response.data.tenSanPham,
          price: response.data.giaBan,
          imei: response.data.imei,
        };
        cartItems.value.push(newItem);
        const invoice = pendingInvoices.value.find((i) => i.id === activeInvoiceId.value);
        if (invoice) invoice.items.push(newItem);
      }

      if (toastRef.value) toastRef.value.kshowToast("success", `Đã thêm ${selectedQuantity.value} sản phẩm: ${selectedProduct.value.tenSanPham}`);
      closeIMEIModal();
      closeProductModal();
      calculateDiscount();
    } catch (error) {
      console.error("Lỗi khi thêm sản phẩm:", error);
      if (toastRef.value) toastRef.value.kshowToast("error", "Không thể thêm sản phẩm: " + error.message);
    }
  };

  const searchCustomers = async () => {
    // Giữ nguyên logic tìm kiếm khách hàng
  };

  const addNewCustomer = async (newCustomer) => {
    // Giữ nguyên logic thêm khách hàng
  };

  const selectPayment = (method) => {
    paymentMethod.value = method;
    const finalAmount = totalPrice.value - discount.value;
    switch (method) {
      case "transfer":
        tienChuyenKhoan.value = finalAmount;
        tienMat.value = 0;
        break;
      case "cash":
        tienMat.value = finalAmount;
        tienChuyenKhoan.value = 0;
        break;
      case "both":
        tienChuyenKhoan.value = 0;
        tienMat.value = 0;
        break;
    }
    if (toastRef.value) toastRef.value.kshowToast("info", `Đã chọn phương thức thanh toán: ${method}`);
  };

  const createOrder = async () => {
    if (!paymentMethod.value && !payOnDelivery.value) {
      if (toastRef.value) toastRef.value.kshowToast("error", "Vui lòng chọn phương thức thanh toán");
      return;
    }

    if (!activeInvoiceId.value) {
      if (toastRef.value) toastRef.value.kshowToast("error", "Vui lòng chọn hoặc tạo hóa đơn trước!");
      return;
    }

    if (paymentMethod.value === "both") {
      const finalAmount = totalPrice.value - discount.value;
      if (tienChuyenKhoan.value + tienMat.value !== finalAmount) {
        if (toastRef.value) toastRef.value.kshowToast("error", "Tổng tiền chuyển khoản và tiền mặt phải bằng tổng tiền hóa đơn!");
        return;
      }
    }

    try {
      await axios.post(`http://localhost:8080/ban-hang/thanh-toan/${activeInvoiceId.value}`, {
        totalPrice: totalPrice.value,
        discount: discount.value,
        paymentMethod: paymentMethod.value,
        tienChuyenKhoan: tienChuyenKhoan.value,
        tienMat: tienMat.value,
      });

      pendingInvoices.value = pendingInvoices.value.filter((i) => i.id !== activeInvoiceId.value);
      activeInvoiceId.value = null;
      localStorage.removeItem("activeInvoiceId");
      localStorage.removeItem("gioHangId");
      gioHangId.value = null;
      cartItems.value = [];
      selectedCustomer.value = null;
      searchCustomer.value = "";
      discountCode.value = "";
      discount.value = 0;
      orderNotes.value = "";
      paymentMethod.value = "";
      payOnDelivery.value = false;
      tienChuyenKhoan.value = 0;
      tienMat.value = 0;

      if (toastRef.value) toastRef.value.kshowToast("success", "Thanh toán thành công!");
    } catch (error) {
      console.error("Lỗi khi thanh toán:", error);
      if (toastRef.value) toastRef.value.kshowToast("error", "Thanh toán thất bại: " + (error.response?.data || error.message));
    }
  };

  return {
    cartItems,
    cartColumns,
    searchCustomer,
    selectedCustomer,
    customer,
    receiver,
    discountCode,
    discount,
    orderNotes,
    payOnDelivery,
    paymentMethod,
    activeInvoiceId,
    pendingInvoices,
    showProductModal,
    showIMEIModal,
    products,
    filteredProducts,
    productSearchQuery,
    selectedProduct,
    selectedQuantity,
    availableIMEIs,
    selectedIMEIs,
    totalPrice,
    tienChuyenKhoan,
    tienMat,
    fetchPendingInvoices,
    getNestedValue,
    editItem,
    toggleStatus,
    removeItem,
    createNewPendingInvoice,
    loadPendingInvoice,
    scanQR,
    openProductModal,
    closeProductModal,
    showIMEIList,
    closeIMEIModal,
    searchProducts,
    addProductWithIMEIs,
    searchCustomers,
    addNewCustomer,
    selectPayment,
    createOrder,
  };
}