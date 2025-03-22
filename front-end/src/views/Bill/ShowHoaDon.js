import { ref, onMounted } from "vue";
import axios from "axios";
import { format } from 'date-fns';
import { vi } from 'date-fns/locale';

export default function useShowHoaDon(id) {
  const hoaDon = ref(null);
  const isModalOpen = ref(false);
  const isHistoryModalOpen = ref(false);
  const invoiceHistory = ref([]);

  const fetchHoaDonDetail = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/hoa-don/detail/${id}`);
      hoaDon.value = res.data;
    } catch (error) {
      console.error("Lỗi khi lấy chi tiết hóa đơn:", error);
      hoaDon.value = null;
    }
  };

  const fetchInvoiceHistory = async (invoiceId) => {
    try {
      const res = await axios.get(`http://localhost:8080/hoa-don/detail/${invoiceId}`); // Use the same endpoint to get full details including history
      invoiceHistory.value = res.data.lichSuHoaDon || []; // Extract the history from the DTO
      isHistoryModalOpen.value = true;
    } catch (error) {
      console.error("Lỗi khi lấy lịch sử hóa đơn:", error);
      invoiceHistory.value = [];
    }
  };

  const paymentMethodColumns = [
    {
      label: "STT",
      key: "index",
      formatter: (_, __, index) => index + 1,
    },
    {
      label: "Mã thanh toán",
      key: "ma",
    },
    {
      label: "Phương thức thanh toán",
      key: "hinhThucThanhToan",
      formatter: (_, item) => {
        const tienMat = item.tienMat || 0;
        const tienCK = item.tienChuyenKhoan || 0;
        if (tienMat > 0 && tienCK > 0) {
          return `Tiền mặt (${tienMat.toLocaleString()} VND) + Chuyển khoản (${tienCK.toLocaleString()} VND)`;
        } else if (tienMat > 0) {
          return `Tiền mặt (${tienMat.toLocaleString()} VND)`;
        } else if (tienCK > 0) {
          return `Chuyển khoản (${tienCK.toLocaleString()} VND)`;
        }
        return "Không xác định";
      },
    },
    {
      label: "Số tiền",
      key: "tongTien",
      formatter: (_, item) => {
        const tienMat = item.tienMat || 0;
        const tienCK = item.tienChuyenKhoan || 0;
        const totalAmount = tienMat + tienCK;
        return totalAmount > 0 ? `${totalAmount.toLocaleString()} VND` : "0 VND";
      },
    },
    {
      label: "Ghi chú",
      key: "ghiChu",
      formatter: () => {
        return hoaDon.value?.ghiChu || "N/A";
      },
    },
    {
      label: "Người xác nhận",
      key: "idNhanVien.tenNhanVien",
      formatter: () => {
        return hoaDon.value?.idNhanVien?.tenNhanVien || hoaDon.value?.idNhanVien?.id || "N/A";
      },
    },
  ];

  const productColumns = ref([
    {
      label: "STT",
      key: "index",
      formatter: (_, __, index) => index + 1,
    },
    {
      label: "Sản phẩm",
      key: "idChiTietSanPham.idSanPham.tenSanPham",
      formatter: (value) => value || "N/A",
    },
    {
      label: "IMEI",
      key: "idImelDaBan.imel",
      formatter: (value) => value || "N/A",
    },
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

  // Define columns for the history table in the modal
  const historyColumns = ref([
    {
      label: "STT",
      key: "index",
      formatter: (_, __, index) => index + 1,
    },
    {
      label: "Mã",
      key: "ma",
      formatter: (value) => value || "N/A",
    },
    {
      label: "Hành động",
      key: "hanhDong",
      formatter: (value) => value || "N/A",
    },
    {
      label: "Thời gian",
      key: "thoiGian",
      formatter: (value) => (value ? format(new Date(value), 'HH:mm:ss dd/MM/yyyy', { locale: vi }) : "N/A"),
    },
    {
      label: "Nhân viên",
      key: "idNhanVien.tenNhanVien",
    },
  ]);

  // Define columns for the product modal table
  const productModalColumns = ref([
    {
      label: "STT",
      key: "index",
      formatter: (_, __, index) => index + 1,
    },
    {
      label: "Tên sản phẩm",
      key: "tenSanPham",
      formatter: (value) => value || "N/A",
    },
    {
      label: "IMEI",
      key: "imel",
      formatter: (value) => value || "N/A",
    },
    {
      label: "Đơn giá",
      key: "gia",
      formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
    },
  ]);

  // Hardcoded product data
  const hardcodedProducts = ref([
    {
      tenSanPham: "iPhone 14 Pro",
      imel: "123456789012345",
      gia: 25000000,
    },
    {
      tenSanPham: "Samsung Galaxy S23",
      imel: "987654321098765",
      gia: 20000000,
    },
    {
      tenSanPham: "Xiaomi 13",
      imel: "456789123456789",
      gia: 15000000,
    },
  ]);

  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => acc?.[part], obj) || null;
  };

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
  };
}