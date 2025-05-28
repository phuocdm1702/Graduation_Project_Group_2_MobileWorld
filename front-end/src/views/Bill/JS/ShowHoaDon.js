// import { ref, onMounted } from "vue";
// import axios from "axios";
// import { format } from "date-fns";
// import { vi } from "date-fns/locale";
//
// export default function useShowHoaDon(id) {
//   // State
//   const hoaDon = ref(null);
//   const isModalOpen = ref(false);
//   const isHistoryModalOpen = ref(false);
//   const invoiceHistory = ref([]);
//
//   // Hardcoded Product Data
//   const hardcodedProducts = ref([
//     { tenSanPham: "iPhone 14 Pro", imel: "123456789012345", gia: 25000000 },
//     { tenSanPham: "Samsung Galaxy S23", imel: "987654321098765", gia: 20000000 },
//     { tenSanPham: "Xiaomi 13", imel: "456789123456789", gia: 15000000 },
//   ]);
//
//   // Trong <script setup>
//   const getStatusText = (status) => {
//     const statusMap = {
//       0: "Chờ xác nhận",
//       1: "Chờ giao hàng",
//       2: "Đang giao",
//       3: "Hoàn thành",
//       4: "Đã hủy",
//     };
//     return statusMap[status] || "Không xác định";
//   };
//
//   // Table Columns Configuration
//   const paymentMethodColumns = [
//     { label: "STT", key: "index", formatter: (_, __, index) => index + 1 },
//     { label: "Mã thanh toán", key: "ma" },
//     {
//       label: "Phương thức thanh toán",
//       key: "hinhThucThanhToan",
//       formatter: (_, item) => {
//         const tienMat = item.tienMat || 0;
//         const tienCK = item.tienChuyenKhoan || 0;
//         if (tienMat > 0 && tienCK > 0) {
//           return `Tiền mặt (${tienMat.toLocaleString()} VND) + Chuyển khoản (${tienCK.toLocaleString()} VND)`;
//         }
//         return tienMat > 0
//           ? `Tiền mặt (${tienMat.toLocaleString()} VND)`
//           : tienCK > 0
//             ? `Chuyển khoản (${tienCK.toLocaleString()} VND)`
//             : "Không xác định";
//       },
//     },
//     {
//       label: "Số tiền",
//       key: "tongTien",
//       formatter: (_, item) => {
//         const totalAmount = (item.tienMat || 0) + (item.tienChuyenKhoan || 0);
//         return totalAmount > 0 ? `${totalAmount.toLocaleString()} VND` : "0 VND";
//       },
//     },
//     {
//       label: "Ghi chú",
//       key: "ghiChu",
//       formatter: () => hoaDon.value?.ghiChu || "N/A",
//     },
//     {
//       label: "Người xác nhận",
//       key: "idNhanVien.tenNhanVien",
//       formatter: () => hoaDon.value?.idNhanVien?.tenNhanVien || hoaDon.value?.idNhanVien?.id || "N/A",
//     },
//   ];
//
//   const productColumns = ref([
//     { label: "STT", key: "index", formatter: (_, __, index) => index + 1 },
//     {
//       label: "Sản phẩm",
//       key: "idChiTietSanPham.idSanPham.tenSanPham",
//       formatter: (value) => value || "N/A",
//     },
//     { label: "IMEI", key: "idImelDaBan.imel", formatter: (value) => value || "N/A" },
//     {
//       label: "Đơn giá",
//       key: "gia",
//       formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
//     },
//     {
//       label: "Thao tác",
//       key: "actions",
//       formatter: (_, item) => `
//         <button class="bg-blue-500 text-white px-2 py-1 rounded" data-action="scan" data-id="${item.id}">
//           <i class="fas fa-qrcode"></i> Quét QR
//         </button>
//         <button class="bg-orange-500 text-white px-2 py-1 rounded mr-2" data-action="add" data-id="${item.id}">
//           <i class="fas fa-plus"></i> Thêm
//         </button>
//         <button class="bg-red-500 text-white px-2 py-1 rounded mr-2" data-action="delete" data-id="${item.id}">
//           <i class="fas fa-trash"></i> Xóa
//         </button>
//       `,
//     },
//   ]);
//
//   const historyColumns = ref([
//     { label: "STT", key: "index", formatter: (_, __, index) => index + 1 },
//     { label: "Nhân viên", key: "idNhanVien.tenNhanVien" },
//     {
//       label: "Thời gian",
//       key: "thoiGian",
//       formatter: (value) => (value ? format(new Date(value), "HH:mm:ss dd/MM/yyyy", { locale: vi }) : "N/A"),
//     },
//     { label: "Hành động", key: "hanhDong", formatter: (value) => value || "N/A" },
//   ]);
//
//   const productModalColumns = ref([
//     { label: "STT", key: "index", formatter: (_, __, index) => index + 1 },
//     { label: "Tên sản phẩm", key: "tenSanPham", formatter: (value) => value || "N/A" },
//     { label: "IMEI", key: "imel", formatter: (value) => value || "N/A" },
//     {
//       label: "Đơn giá",
//       key: "gia",
//       formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
//     },
//   ]);
//
//   // Utility Functions
//   const getNestedValue = (obj, path) => path.split(".").reduce((acc, part) => acc?.[part], obj) || null;
//
//   // Data Fetching Functions
//   const fetchHoaDonDetail = async () => {
//     try {
//       const { data } = await axios.get(`http://localhost:8080/hoa-don/detail/${id}`);
//       hoaDon.value = data;
//     } catch (error) {
//       console.error("Lỗi khi lấy chi tiết hóa đơn:", error);
//       hoaDon.value = null;
//     }
//   };
//
//   const fetchInvoiceHistory = async (invoiceId) => {
//     try {
//       const { data } = await axios.get(`http://localhost:8080/hoa-don/detail/${invoiceId}`);
//       invoiceHistory.value = data.lichSuHoaDon || [];
//       isHistoryModalOpen.value = true;
//     } catch (error) {
//       console.error("Lỗi khi lấy lịch sử hóa đơn:", error);
//       invoiceHistory.value = [];
//     }
//   };
//
//   // Modal Handlers
//   const openModal = () => {
//     isModalOpen.value = true;
//   };
//
//   const closeModal = () => {
//     isModalOpen.value = false;
//   };
//
//   const closeHistoryModal = () => {
//     isHistoryModalOpen.value = false;
//     invoiceHistory.value = [];
//   };
//
//   // Lifecycle Hooks
//   onMounted(() => {
//     fetchHoaDonDetail();
//   });
//
//   return {
//     hoaDon,
//     getNestedValue,
//     paymentMethodColumns,
//     productColumns,
//     historyColumns,
//     productModalColumns,
//     hardcodedProducts,
//     isModalOpen,
//     openModal,
//     closeModal,
//     isHistoryModalOpen,
//     invoiceHistory,
//     fetchInvoiceHistory,
//     closeHistoryModal,
//     getStatusText,
//   };
// }


// import { ref, onMounted } from "vue";
// import axios from "axios";
// import { format } from "date-fns";
// import { vi } from "date-fns/locale";
//
// export default function useShowHoaDon(id) {
//   // State
//   const hoaDon = ref(null);
//   const isModalOpen = ref(false);
//   const isHistoryModalOpen = ref(false);
//   const invoiceHistory = ref([]);
//
//   // Hardcoded Product Data
//   const hardcodedProducts = ref([
//     { tenSanPham: "iPhone 14 Pro", imel: "123456789012345", gia: 25000000 },
//     { tenSanPham: "Samsung Galaxy S23", imel: "987654321098765", gia: 20000000 },
//     { tenSanPham: "Xiaomi 13", imel: "456789123456789", gia: 15000000 },
//   ]);
//
//   // Hàm ánh xạ trạng thái
//   const getStatusText = (status) => {
//     const statusMap = {
//       0: "Chờ xác nhận",
//       1: "Chờ giao hàng",
//       2: "Đang giao",
//       3: "Hoàn thành",
//       4: "Đã hủy",
//     };
//     return statusMap[status] || "Không xác định";
//   };
//
//   // Cấu hình cột bảng
//   const paymentMethodColumns = [
//     { label: "STT", key: "index", formatter: (_, __, index) => index + 1 },
//     { label: "Mã thanh toán", key: "ma" },
//     {
//       label: "Phương thức thanh toán",
//       key: "hinhThucThanhToan",
//       formatter: (_, item) => {
//         const tienMat = item.tienMat || 0;
//         const tienCK = item.tienChuyenKhoan || 0;
//         if (tienMat > 0 && tienCK > 0) {
//           return `Tiền mặt (${tienMat.toLocaleString()} VND) + Chuyển khoản (${tienCK.toLocaleString()} VND)`;
//         }
//         return tienMat > 0
//           ? `Tiền mặt (${tienMat.toLocaleString()} VND)`
//           : tienCK > 0
//             ? `Chuyển khoản (${tienCK.toLocaleString()} VND)`
//             : "Không xác định";
//       },
//     },
//     {
//       label: "Số tiền",
//       key: "tongTien",
//       formatter: (_, item) => {
//         const totalAmount = (item.tienMat || 0) + (item.tienChuyenKhoan || 0);
//         return totalAmount > 0 ? `${totalAmount.toLocaleString()} VND` : "0 VND";
//       },
//     },
//     {
//       label: "Ghi chú",
//       key: "ghiChu",
//       formatter: () => hoaDon.value?.ghiChu || "N/A",
//     },
//     {
//       label: "Người xác nhận",
//       key: "idNhanVien.tenNhanVien",
//       formatter: () => hoaDon.value?.idNhanVien?.tenNhanVien || hoaDon.value?.idNhanVien?.id || "N/A",
//     },
//   ];
//
//   const productColumns = ref([
//     { label: "STT", key: "index", formatter: (_, __, index) => index + 1 },
//     {
//       label: "Sản phẩm",
//       key: "idChiTietSanPham.idSanPham.tenSanPham",
//       formatter: (value) => value || "N/A",
//     },
//     { label: "IMEI", key: "idImelDaBan.imel", formatter: (value) => value || "N/A" },
//     {
//       label: "Đơn giá",
//       key: "gia",
//       formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
//     },
//     {
//       label: "Thao tác",
//       key: "actions",
//       formatter: (_, item) => `
//         <button class="bg-blue-500 text-white px-2 py-1 rounded" data-action="scan" data-id="${item.id}">
//           <i class="fas fa-qrcode"></i> Quét QR
//         </button>
//         <button class="bg-orange-500 text-white px-2 py-1 rounded mr-2" data-action="add" data-id="${item.id}">
//           <i class="fas fa-plus"></i> Thêm
//         </button>
//         <button class="bg-red-500 text-white px-2 py-1 rounded mr-2" data-action="delete" data-id="${item.id}">
//           <i class="fas fa-trash"></i> Xóa
//         </button>
//       `,
//     },
//   ]);
//
//   const historyColumns = ref([
//     { label: "STT", key: "index", formatter: (_, __, index) => index + 1 },
//     { label: "Nhân viên", key: "idNhanVien.tenNhanVien" },
//     {
//       label: "Thời gian",
//       key: "thoiGian",
//       formatter: (value) => (value ? format(new Date(value), "HH:mm:ss dd/MM/yyyy", { locale: vi }) : "N/A"),
//     },
//     { label: "Hành động", key: "hanhDong", formatter: (value) => value || "N/A" },
//   ]);
//
//   const productModalColumns = ref([
//     { label: "STT", key: "index", formatter: (_, __, index) => index + 1 },
//     { label: "Tên sản phẩm", key: "tenSanPham", formatter: (value) => value || "N/A" },
//     { label: "IMEI", key: "imel", formatter: (value) => value || "N/A" },
//     {
//       label: "Đơn giá",
//       key: "gia",
//       formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
//     },
//   ]);
//
//   // Utility Functions
//   const getNestedValue = (obj, path) => path.split(".").reduce((acc, part) => acc?.[part], obj) || null;
//
//   // Data Fetching Functions
//   const fetchHoaDonDetail = async () => {
//     try {
//       const { data } = await axios.get(`http://localhost:8080/hoa-don/detail/${id}`);
//       hoaDon.value = data;
//     } catch (error) {
//       console.error("Lỗi khi lấy chi tiết hóa đơn:", error);
//       hoaDon.value = null;
//     }
//   };
//
//   const fetchInvoiceHistory = async (invoiceId) => {
//     try {
//       const { data } = await axios.get(`http://localhost:8080/hoa-don/detail/${invoiceId}`);
//       invoiceHistory.value = data.lichSuHoaDon || [];
//       isHistoryModalOpen.value = true;
//     } catch (error) {
//       console.error("Lỗi khi lấy lịch sử hóa đơn:", error);
//       invoiceHistory.value = [];
//     }
//   };
//
//   // Cập nhật trạng thái hóa đơn
//   const updateOrderStatus = async (newStatus, action) => {
//     try {
//       const response = await axios.put(`http://localhost:8080/hoa-don/update-status/${id}`, {
//         trangThai: newStatus,
//         hanhDong: action,
//       });
//       hoaDon.value = response.data;
//       await fetchInvoiceHistory(id); // Cập nhật lại lịch sử hóa đơn
//       console.log(`Cập nhật trạng thái thành công: ${action}`);
//     } catch (error) {
//       console.error("Lỗi khi cập nhật trạng thái:", error);
//     }
//   };
//
//   // Xác nhận đơn hàng (Chờ xác nhận → Chờ giao hàng)
//   const confirmOrder = async () => {
//     await updateOrderStatus(1, "Đã xác nhận");
//   };
//
//   // Bắt đầu giao hàng (Chờ giao hàng → Đang giao)
//   const startShipping = async () => {
//     await updateOrderStatus(2, "Đang giao");
//   };
//
//   // Hoàn thành đơn hàng (Đang giao → Hoàn thành)
//   const completeOrder = async () => {
//     await updateOrderStatus(3, "Hoàn thành");
//   };
//
//   // Modal Handlers
//   const openModal = () => {
//     isModalOpen.value = true;
//   };
//
//   const closeModal = () => {
//     isModalOpen.value = false;
//   };
//
//   const closeHistoryModal = () => {
//     isHistoryModalOpen.value = false;
//     invoiceHistory.value = [];
//   };
//
//   // Lifecycle Hooks
//   onMounted(() => {
//     fetchHoaDonDetail();
//   });
//
//   return {
//     hoaDon,
//     getNestedValue,
//     paymentMethodColumns,
//     productColumns,
//     historyColumns,
//     productModalColumns,
//     hardcodedProducts,
//     isModalOpen,
//     openModal,
//     closeModal,
//     isHistoryModalOpen,
//     invoiceHistory,
//     fetchInvoiceHistory,
//     closeHistoryModal,
//     getStatusText,
//     confirmOrder,
//     startShipping,
//     completeOrder,
//   };
// }


import { ref, onMounted } from "vue";
import axios from "axios";
import { format } from "date-fns";
import { vi } from "date-fns/locale";

export default function useShowHoaDon(id) {
  // State
  const hoaDon = ref(null);
  const isModalOpen = ref(false);
  const isHistoryModalOpen = ref(false);
  const invoiceHistory = ref([]);

  // Hardcoded Product Data
  const hardcodedProducts = ref([
    { tenSanPham: "iPhone 14 Pro", imel: "123456789012345", gia: 25000000 },
    { tenSanPham: "Samsung Galaxy S23", imel: "987654321098765", gia: 20000000 },
    { tenSanPham: "Xiaomi 13", imel: "456789123456789", gia: 15000000 },
  ]);

  // Hàm ánh xạ trạng thái
  const getStatusText = (status) => {
    const statusMap = {
      0: "Chờ xác nhận",
      1: "Chờ giao hàng",
      2: "Đang giao",
      3: "Hoàn thành",
      4: "Đã hủy",
    };
    return statusMap[status] || "Không xác định";
  };

  // Cấu hình cột bảng
  const paymentMethodColumns = [
    { label: "STT", key: "index", formatter: (_, __, index) => index + 1 },
    { label: "Mã thanh toán", key: "ma" },
    {
      label: "Phương thức thanh toán",
      key: "hinhThucThanhToan",
      formatter: (_, item) => {
        const tienMat = item.tienMat || 0;
        const tienCK = item.tienChuyenKhoan || 0;
        if (tienMat > 0 && tienCK > 0) {
          return `Tiền mặt (${tienMat.toLocaleString()} VND) + Chuyển khoản (${tienCK.toLocaleString()} VND)`;
        }
        return tienMat > 0
          ? `Tiền mặt (${tienMat.toLocaleString()} VND)`
          : tienCK > 0
            ? `Chuyển khoản (${tienCK.toLocaleString()} VND)`
            : "Không xác định";
      },
    },
    {
      label: "Số tiền",
      key: "tongTien",
      formatter: (_, item) => {
        const totalAmount = (item.tienMat || 0) + (item.tienChuyenKhoan || 0);
        return totalAmount > 0 ? `${totalAmount.toLocaleString()} VND` : "0 VND";
      },
    },
    {
      label: "Ghi chú",
      key: "ghiChu",
      formatter: () => hoaDon.value?.ghiChu || "N/A",
    },
    {
      label: "Người xác nhận",
      key: "idNhanVien.tenNhanVien",
      formatter: () => hoaDon.value?.idNhanVien?.tenNhanVien || hoaDon.value?.idNhanVien?.id || "N/A",
    },
  ];

  const productColumns = ref([
    { label: "STT", key: "index", formatter: (_, __, index) => index + 1 },
    {
      label: "Sản phẩm",
      key: "idChiTietSanPham.idSanPham.tenSanPham",
      formatter: (value) => value || "N/A",
    },
    { label: "IMEI", key: "idImelDaBan.imel", formatter: (value) => value || "N/A" },
    {
      label: "Đơn giá",
      key: "gia",
      formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
    },
    {
      label: "Thao tác",
      key: "actions",
      formatter: (_, item) => `
        <button class="bg-blue-500 text-white px-2 py-1 rounded" data-action="scan" data-id="${item.id}">
          <i class="fas fa-qrcode"></i> Quét QR
        </button>
        <button class="bg-orange-500 text-white px-2 py-1 rounded mr-2" data-action="add" data-id="${item.id}">
          <i class="fas fa-plus"></i> Thêm
        </button>
        <button class="bg-red-500 text-white px-2 py-1 rounded mr-2" data-action="delete" data-id="${item.id}">
          <i class="fas fa-trash"></i> Xóa
        </button>
      `,
    },
  ]);

  const historyColumns = ref([
    { label: "STT", key: "index", formatter: (_, __, index) => index + 1 },
    { label: "Nhân viên", key: "idNhanVien.tenNhanVien" },
    {
      label: "Thời gian",
      key: "thoiGian",
      formatter: (value) => (value ? format(new Date(value), "HH:mm:ss dd/MM/yyyy", { locale: vi }) : "N/A"),
    },
    { label: "Hành động", key: "hanhDong", formatter: (value) => value || "N/A" },
  ]);

  const productModalColumns = ref([
    { label: "STT", key: "index", formatter: (_, __, index) => index + 1 },
    { label: "Tên sản phẩm", key: "tenSanPham", formatter: (value) => value || "N/A" },
    { label: "IMEI", key: "imel", formatter: (value) => value || "N/A" },
    {
      label: "Đơn giá",
      key: "gia",
      formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
    },
  ]);

  // Utility Functions
  const getNestedValue = (obj, path) => path.split(".").reduce((acc, part) => acc?.[part], obj) || null;

  // Data Fetching Functions
  const fetchHoaDonDetail = async () => {
    try {
      const { data } = await axios.get(`http://localhost:8080/hoa-don/detail/${id}`);
      hoaDon.value = data;
    } catch (error) {
      console.error("Lỗi khi lấy chi tiết hóa đơn:", error);
      hoaDon.value = null;
    }
  };

  const fetchInvoiceHistory = async (invoiceId) => {
    try {
      const { data } = await axios.get(`http://localhost:8080/hoa-don/detail/${invoiceId}`);
      invoiceHistory.value = data.lichSuHoaDon || [];
      isHistoryModalOpen.value = true;
    } catch (error) {
      console.error("Lỗi khi lấy lịch sử hóa đơn:", error);
      invoiceHistory.value = [];
    }
  };

  // Cập nhật trạng thái hóa đơn
  const updateOrderStatus = async (newStatus, action) => {
    try {
      const response = await axios.put(`http://localhost:8080/hoa-don/update-status/${id}`, {
        trangThai: newStatus,
        hanhDong: action,
      });
      hoaDon.value = response.data;
      await fetchInvoiceHistory(id); // Cập nhật lại lịch sử hóa đơn
      console.log(`Cập nhật trạng thái thành công: ${action}`);
    } catch (error) {
      console.error("Lỗi khi cập nhật trạng thái:", error);
    }
  };

  // Xác nhận đơn hàng (Chờ xác nhận → Chờ giao hàng)
  const confirmOrder = async () => {
    await updateOrderStatus(1, "Đã xác nhận");
  };

  // Bắt đầu giao hàng (Chờ giao hàng → Đang giao)
  const startShipping = async () => {
    await updateOrderStatus(2, "Đang giao");
  };

  // Hoàn thành đơn hàng (Đang giao → Hoàn thành)
  const completeOrder = async () => {
    await updateOrderStatus(3, "Hoàn thành");
  };

  // Hủy đơn hàng (Chuyển trạng thái thành Đã hủy)
  const cancelOrder = async () => {
    await updateOrderStatus(4, "Đã hủy");
  };

  // Modal Handlers
  const openModal = () => {
    isModalOpen.value = true;
  };

  const closeModal = () => {
    isModalOpen.value = false;
  };

  const closeHistoryModal = () => {
    isHistoryModalOpen.value = false;
    invoiceHistory.value = [];
  };

  // Lifecycle Hooks
  onMounted(() => {
    fetchHoaDonDetail();
  });

  return {
    hoaDon,
    getNestedValue,
    paymentMethodColumns,
    productColumns,
    historyColumns,
    productModalColumns,
    hardcodedProducts,
    isModalOpen,
    openModal,
    closeModal,
    isHistoryModalOpen,
    invoiceHistory,
    fetchInvoiceHistory,
    closeHistoryModal,
    getStatusText,
    confirmOrder,
    startShipping,
    completeOrder,
    cancelOrder,
  };
}