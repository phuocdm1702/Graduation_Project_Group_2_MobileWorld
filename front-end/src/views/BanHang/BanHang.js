// src/composables/useCounterSales.js
import { ref, computed } from "vue";
import axios from "axios";

// Hàm tạo chuỗi ngẫu nhiên gồm 5 ký tự (chữ cái và số)
const generateRandomCode = () => {
  const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Tập hợp ký tự gồm chữ cái in hoa và số
  let randomCode = "";
  for (let i = 0; i < 5; i++) {
    const randomIndex = Math.floor(Math.random() * characters.length);
    randomCode += characters[randomIndex];
  }
  return `HD${randomCode}`; // Ghép với tiền tố HD
};

export function useCounterSales(toastRef) {
  // Data
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
  const customer = ref({ name: "", phone: "" });
  const receiver = ref({
    name: "",
    phone: "",
    city: "Tỉnh Phú Thọ",
    district: "Huyện Lâm Thao",
    ward: "Xã Xuân Lũng",
    address: "",
  });
  const discountCode = ref("");
  const discount = ref(0);
  const orderNotes = ref("");
  const payOnDelivery = ref(false);
  const paymentMethod = ref("");
  const activeInvoiceId = ref(null);
  const pendingInvoices = ref([]);

  // Lấy danh sách hóa đơn chưa xác nhận từ backend
  const fetchPendingInvoices = async () => {
    try {
      const response = await axios.get("http://localhost:8080/ban-hang/data");
      pendingInvoices.value = response.data.map((hd) => ({
        id: hd.id,
        code: hd.ma,
        status: hd.status,
        items: hd.items.map((item) => ({
          id: item.id || null,
          name: item.name,
          imei: item.imei,
          price: item.price,
        })),
      }));
    } catch (error) {
      console.error("Lỗi khi lấy danh sách hóa đơn:", error);
      if (toastRef.value) toastRef.value.kshowToast("error", "Không thể tải danh sách hóa đơn!");
    }
  };

  fetchPendingInvoices();

  const totalPrice = computed(() => {
    return cartItems.value.reduce((total, item) => total + item.price, 0);
  });

  const getNestedValue = (item, key) => {
    return key.split(".").reduce((obj, k) => (obj && obj[k] !== undefined ? obj[k] : null), item) || "N/A";
  };

  const editItem = (item) => {
    if (toastRef.value) toastRef.value.kshowToast("info", `Chỉnh sửa sản phẩm: ${item.name}`);
  };

  const toggleStatus = (item) => {
    if (toastRef.value) toastRef.value.kshowToast("success", `Đã thay đổi trạng thái sản phẩm: ${item.name}`);
  };

  const removeItem = (item) => {
    const index = cartItems.value.findIndex((i) => i.id === item.id);
    if (index !== -1) {
      cartItems.value.splice(index, 1);
      if (toastRef.value) toastRef.value.kshowToast("success", `Đã xóa sản phẩm: ${item.name}`);
    }
  };

  const createNewPendingInvoice = async () => {
    const newInvoice = {
      ma: generateRandomCode(), // Sử dụng hàm generateRandomCode để tạo mã ngẫu nhiên
      status: 0,
      items: [],
    };

    try {
      const response = await axios.post("http://localhost:8080/ban-hang/addHD", newInvoice);
      pendingInvoices.value.unshift({
        id: response.data.id,
        code: response.data.ma,
        status: response.data.trangThai,
        items: [],
      });
      loadPendingInvoice(pendingInvoices.value[0]);
      if (toastRef.value) toastRef.value.kshowToast("success", `Đã tạo hóa đơn chờ mới: ${response.data.ma}`);
    } catch (error) {
      console.error("Lỗi khi tạo hóa đơn mới:", error);
      if (toastRef.value) toastRef.value.kshowToast("error", "Không thể tạo hóa đơn mới!");
    }
  };

  const loadPendingInvoice = (invoice) => {
    activeInvoiceId.value = invoice.id;
    cartItems.value = [...invoice.items];
    if (toastRef.value) toastRef.value.kshowToast("info", `Đã tải hóa đơn chờ: ${invoice.code}`);
  };

  const scanQR = () => {
    if (toastRef.value) toastRef.value.kshowToast("info", "Đang quét QR...");
  };

  const addProduct = () => {
    const newProduct = {
      id: cartItems.value.length > 0 ? Math.max(...cartItems.value.map((i) => i.id || 0)) + 1 : 1,
      name: `Sản phẩm mới ${cartItems.value.length + 1}`,
      imei: `IME${Math.floor(1000 + Math.random() * 9000)}`,
      price: 1000000 + Math.floor(Math.random() * 9000000),
    };

    cartItems.value.push(newProduct);

    if (activeInvoiceId.value) {
      const invoice = pendingInvoices.value.find((i) => i.id === activeInvoiceId.value);
      if (invoice) {
        invoice.items.push(newProduct);
      }
    }

    if (toastRef.value) toastRef.value.kshowToast("success", `Đã thêm sản phẩm: ${newProduct.name}`);
  };

  const searchCustomers = () => {
    if (searchCustomer.value) {
      selectedCustomer.value = true;
      customer.value = { name: "Khách hàng tìm thấy", phone: "098xxxxxxx" };
      if (toastRef.value) toastRef.value.kshowToast("success", `Đã tìm thấy khách hàng: ${customer.value.name}`);
    } else {
      selectedCustomer.value = null;
    }
  };

  const addNewCustomer = () => {
    selectedCustomer.value = true;
    customer.value = { name: "", phone: "" };
    if (toastRef.value) toastRef.value.kshowToast("info", "Thêm mới khách hàng");
  };

  const applyDiscount = () => {
    if (discountCode.value) {
      discount.value = 2000000;
      if (toastRef.value) toastRef.value.kshowToast("success", "Đã áp dụng mã giảm giá");
    } else {
      if (toastRef.value) toastRef.value.kshowToast("error", "Vui lòng nhập mã giảm giá");
    }
  };

  const selectPayment = (method) => {
    paymentMethod.value = method;
    let methodName = "";
    switch (method) {
      case "transfer":
        methodName = "Chuyển khoản";
        break;
      case "cash":
        methodName = "Tiền mặt";
        break;
      case "both":
        methodName = "Cả hai phương thức";
        break;
    }
    if (toastRef.value) toastRef.value.kshowToast("info", `Đã chọn phương thức thanh toán: ${methodName}`);
  };

  const createOrder = () => {
    if (!paymentMethod.value && !payOnDelivery.value) {
      if (toastRef.value) toastRef.value.kshowToast("error", "Vui lòng chọn phương thức thanh toán");
      return;
    }

    if (activeInvoiceId.value) {
      pendingInvoices.value = pendingInvoices.value.filter((i) => i.id !== activeInvoiceId.value);
      activeInvoiceId.value = null;
    }

    if (toastRef.value) toastRef.value.kshowToast("success", "Thanh toán thành công!");

    cartItems.value = [];
    selectedCustomer.value = null;
    searchCustomer.value = "";
    discountCode.value = "";
    discount.value = 0;
    orderNotes.value = "";
    paymentMethod.value = "";
    payOnDelivery.value = false;
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
    totalPrice,
    fetchPendingInvoices,
    getNestedValue,
    editItem,
    toggleStatus,
    removeItem,
    createNewPendingInvoice,
    loadPendingInvoice,
    scanQR,
    addProduct,
    searchCustomers,
    addNewCustomer,
    applyDiscount,
    selectPayment,
    createOrder,
  };
}