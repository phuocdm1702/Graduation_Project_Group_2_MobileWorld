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
    { key: "imei", label: "IMEI" },
    { key: "price", label: "Đơn giá", formatter: (value) => `${value.toLocaleString()} đ` },
    { key: "actions", label: "Xóa", cellSlot: "actionsSlot" },
  ]);
  const searchCustomer = ref("");
  const selectedCustomer = ref(null);
  const customer = ref({ name: "", phone: "", city: "", district: "", ward: "", address: ""});
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
  const products = ref([]);
  const filteredProducts = ref([]);
  const productSearchQuery = ref("");
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
        code: hd.ma,
        status: hd.status,
        items: hd.items.map((item) => ({
          id: item.id,
          name: item.name,
          imei: item.imei,
          price: item.price,
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
      products.value = response.data;
      filteredProducts.value = response.data;
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
          imei: item.imel,
          price: item.tongTien,
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
    return key.split(".").reduce((obj, k) => (obj && obj[k] !== undefined ? obj[k] : null), item) || "N/A";
  };

  const editItem = (item) => {
    if (toastRef.value) toastRef.value.kshowToast("info", `Chỉnh sửa sản phẩm: ${item.name}`);
  };

  const toggleStatus = (item) => {
    if (toastRef.value) toastRef.value.kshowToast("success", `Đã thay đổi trạng thái sản phẩm: ${item.name}`);
  };

  const removeItem = async (item) => {
    try {
      // Gọi API để xóa chi tiết giỏ hàng
      await axios.delete(`http://localhost:8080/ban-hang/gio-hang/chi-tiet/${item.id}`);

      // Cập nhật danh sách cartItems
      cartItems.value = cartItems.value.filter((i) => i.id !== item.id);

      // Cập nhật danh sách sản phẩm trong hóa đơn chờ
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
      ma: generateRandomCode(),
      status: 0,
      items: [],
    };

    try {
      const gioHangResponse = await axios.post("http://localhost:8080/ban-hang/addGioHang", {
        ma: `GH_${newInvoice.ma}`,
        idKhachHang: 1,
      });
      gioHangId.value = gioHangResponse.data.id;
      localStorage.setItem("gioHangId", gioHangId.value);

      const response = await axios.post("http://localhost:8080/ban-hang/addHD", newInvoice);
      pendingInvoices.value.unshift({
        id: response.data.id,
        code: response.data.ma,
        status: response.data.trangThai,
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
        imei: item.imel,
        price: item.tongTien,
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

  const searchProducts = () => {
    if (!productSearchQuery.value) {
      filteredProducts.value = products.value;
    } else {
      filteredProducts.value = products.value.filter((product) =>
        product.tenSanPham.toLowerCase().includes(productSearchQuery.value.toLowerCase()) ||
        product.ma.toLowerCase().includes(productSearchQuery.value.toLowerCase()) ||
        product.imel.toLowerCase().includes(productSearchQuery.value.toLowerCase())
      );
    }
  };

  const addProduct = async (product) => {
    if (!activeInvoiceId.value) {
      if (toastRef.value) toastRef.value.kshowToast("error", "Vui lòng chọn hoặc tạo hóa đơn trước!");
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
        console.log("Thêm giỏ hàng thành công, gioHangId:", gioHangId.value);
      }

      console.log("product.id:", product.id);

      const response = await axios.post(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`, {
        id: product.id,
        hoaDonId: activeInvoiceId.value,
      });
      console.log("Thêm chi tiết giỏ hàng thành công:", response.data);

      const newItem = {
        id: response.data.id,
        name: response.data.tenSanPham,
        imei: product.imel,
        price: response.data.tongTien,
      };

      cartItems.value.push(newItem);
      console.log("cartItems sau khi thêm:", cartItems.value);

      const invoice = pendingInvoices.value.find((i) => i.id === activeInvoiceId.value);
      if (invoice) {
        invoice.items.push(newItem);
      }

      if (toastRef.value) toastRef.value.kshowToast("success", `Đã thêm sản phẩm: ${newItem.name}`);
      showProductModal.value = false;

      calculateDiscount();
    } catch (error) {
      console.error("Lỗi khi thêm sản phẩm:", error);
      console.log("Chi tiết lỗi:", error.response ? error.response.data : error.message);
      if (toastRef.value) toastRef.value.kshowToast("error", "Không thể thêm sản phẩm!");
    }
  };

  const searchCustomers = async () => {
    if (!searchCustomer.value.trim()) {
      selectedCustomer.value = null;
      customer.value = {
        name: "",
        phone: "",
        city: "",
        district: "",
        ward: "",
        address: "",
      };
      receiver.value = {
        name: "",
        phone: "",
        city: "",
        district: "",
        ward: "",
        address: "",
      };
      return;
    }

    try {
      const response = await axios.get(`http://localhost:8080/khach-hang/search?query=${encodeURIComponent(searchCustomer.value.trim())}`);
      if (response.data && response.data.length > 0) {
        const firstCustomer = response.data[0];
        selectedCustomer.value = true;
        customer.value = {
          name: firstCustomer.ten || "",
          phone: firstCustomer.idTaiKhoan?.soDienThoai || "",
          city: firstCustomer.idDiaChiKH?.thanhPho || "",
          district: firstCustomer.idDiaChiKH?.quan || "",
          ward: firstCustomer.idDiaChiKH?.phuong || "",
          address: firstCustomer.idDiaChiKH?.diaChiCuThe || "",
        };
        // Đồng bộ dữ liệu với receiver
        receiver.value = {
          name: customer.value.name,
          phone: customer.value.phone,
          city: customer.value.city || "Tỉnh Phú Thọ", // Giá trị mặc định nếu không có
          district: customer.value.district || "Huyện Lâm Thao",
          ward: customer.value.ward || "Xã Xuân Lũng",
          address: customer.value.address || "",
        };
      } else {
        selectedCustomer.value = null;
        customer.value = {
          name: "",
          phone: "",
          city: "",
          district: "",
          ward: "",
          address: "",
        };
        receiver.value = {
          name: "",
          phone: "",
          city: "",
          district: "",
          ward: "",
          address: "",
        };
        if (toastRef.value) {
          toastRef.value.kshowToast("info", "Không tìm thấy khách hàng phù hợp");
        }
      }
    } catch (error) {
      console.error("Lỗi khi tìm kiếm khách hàng:", error);
      selectedCustomer.value = null;
      customer.value = {
        name: "",
        phone: "",
        city: "",
        district: "",
        ward: "",
        address: "",
      };
      receiver.value = {
        name: "",
        phone: "",
        city: "",
        district: "",
        ward: "",
        address: "",
      };
      if (toastRef.value) {
        toastRef.value.kshowToast("error", "Không thể tìm kiếm khách hàng: " + (error.response?.data?.error || error.message));
      }
    }
  };

  const addNewCustomer = async (newCustomer) => {
    console.log('Dữ liệu gửi lên:', newCustomer);
    const customerData = {
      tenKH: newCustomer.name?.trim(),
      soDienThoai: newCustomer.phone,
      thanhPho: newCustomer.city,
      quan: newCustomer.district,
      phuong: newCustomer.ward,
      diaChiCuThe: newCustomer.address,
    };

    // Kiểm tra dữ liệu trước khi gửi
    if (!customerData.tenKH || !customerData.soDienThoai || !customerData.thanhPho ||
      !customerData.quan || !customerData.phuong || !customerData.diaChiCuThe) {
      console.error('Dữ liệu không đầy đủ:', customerData);
      if (toastRef.value) {
        toastRef.value.kshowToast('error', 'Vui lòng điền đầy đủ thông tin khách hàng');
      }
      return;
    }

    try {
      const response = await axios.post('http://localhost:8080/khach-hang/addBh', customerData, {
        headers: { 'Content-Type': 'application/json' },
      });
      console.log('Phản hồi từ server:', response.data);
      selectedCustomer.value = true;
      customer.value = {
        name: response.data.ten,
        phone: response.data.soDienThoai || response.data.idTaiKhoan?.soDienThoai || '',
        city: response.data.idDiaChiKH?.thanhPho,
        district: response.data.idDiaChiKH?.quan,
        ward: response.data.idDiaChiKH?.phuong,
        address: response.data.idDiaChiKH?.diaChiCuThe,
      };
      if (toastRef.value) {
        toastRef.value.kshowToast("success", `Đã thêm thành công khách hàng: ${response.data.ten}`);
      }

    } catch (error) {
      console.error('Lỗi khi thêm khách hàng:', error);
      console.log('Phản hồi lỗi từ server:', error.response?.data);
      let errorMessage = 'Không thể thêm khách hàng';
      if (error.response?.status === 400 && error.response?.data?.includes('constraint [UQ__')) {
        errorMessage = 'Số điện thoại đã tồn tại. Vui lòng sử dụng số khác.';
      } else {
        errorMessage = error.response?.data || error.message;
      }
      if (toastRef.value) {
        toastRef.value.kshowToast('error', errorMessage);
      }
      return;
    }
  };

  const selectPayment = (method) => {
    paymentMethod.value = method;
    let methodName = "";
    switch (method) {
      case "transfer":
        methodName = "Chuyển khoản";
        tienChuyenKhoan.value = totalPrice.value - discount.value;
        tienMat.value = 0;
        break;
      case "cash":
        methodName = "Tiền mặt";
        tienMat.value = totalPrice.value - discount.value;
        tienChuyenKhoan.value = 0;
        break;
      case "both":
        methodName = "Cả hai phương thức";
        // Người dùng sẽ nhập số tiền
        break;
    }
    if (toastRef.value) toastRef.value.kshowToast("info", `Đã chọn phương thức thanh toán: ${methodName}`);
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

      if (activeInvoiceId.value) {
        pendingInvoices.value = pendingInvoices.value.filter((i) => i.id !== activeInvoiceId.value);
        activeInvoiceId.value = null;
        localStorage.removeItem("activeInvoiceId");
      }

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
    products,
    filteredProducts,
    productSearchQuery,
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
    searchProducts,
    addProduct,
    searchCustomers,
    addNewCustomer,
    selectPayment,
    createOrder,
  };
}