import {ref, onMounted} from "vue";
import axios from "axios";
import {format} from 'date-fns';
import {vi} from 'date-fns/locale'; // Locale tiếng Việt
export default function useShowHoaDon(id) {
  const hoaDon = ref(null);
  const isModalOpen = ref(false);
  const fetchHoaDonDetail = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/hoa-don/detail/${id}`);
      hoaDon.value = res.data;
    } catch (error) {
      console.error("Lỗi khi lấy chi tiết hóa đơn:", error);
      hoaDon.value = null;
    }
  };

  // Columns cho Lịch sử thanh toán
  const paymentHistoryColumns = [
    {
      label: "STT",
      key: "index",
      formatter: (_, __, index) => index + 1,
    },
    {
      label: "Phương thức thanh toán",
      key: "hinhThucThanhToan", // Nếu backend bổ sung thuộc tính này
      formatter: (_, item) => {
        // Lấy danh sách hình thức thanh toán từ hoaDon
        const htttList = hoaDon.value?.hinhThucThanhToan || [];
        if (!htttList.length) return "Không xác định";

        // Giả sử mỗi LichSuHoaDon liên kết với một hoặc nhiều HinhThucThanhToan
        const paymentMethods = htttList
          .filter(httt => httt.idHoaDon === item.idHoaDon)
          .map(httt => {
            const tienMat = httt.tienMat || 0;
            const tienCK = httt.tienChuyenKhoan || 0;
            if (tienMat > 0 && tienCK > 0) {
              return `Tiền mặt (${tienMat.toLocaleString()} VND) + Chuyển khoản (${tienCK.toLocaleString()} VND)`;
            } else if (tienMat > 0) {
              return `Tiền mặt (${tienMat.toLocaleString()} VND)`;
            } else if (tienCK > 0) {
              return `Chuyển khoản (${tienCK.toLocaleString()} VND)`;
            }
            return "Không xác định";
          });
        return paymentMethods.join(", ") || "Không xác định";
      },
    },
    {
      label: "Số tiền",
      key: "tongTien",
      formatter: (_, item) => {
        const htttList = hoaDon.value?.hinhThucThanhToan || [];
        if (!htttList.length) return "0 VND";

        // Tính tổng tiền từ các hình thức thanh toán liên quan đến hóa đơn
        const totalAmount = htttList
          .filter(httt => httt.idHoaDon === item.idHoaDon)
          .reduce((sum, httt) => {
            const tienMat = httt.tienMat || 0;
            const tienCK = httt.tienChuyenKhoan || 0;
            return sum + tienMat + tienCK;
          }, 0);

        return totalAmount > 0 ? `${totalAmount.toLocaleString()} VND` : "0 VND";
      },
    },
    {
      label: "Thời gian",
      key: "thoiGian",
      formatter: (value) => (value ? format(new Date(value), 'dd/MM/yyyy HH:mm:ss', {locale: vi}) : 'N/A'),
    },
    {
      label: "Ghi chú",
      key: "",
      formatter: (value) => value || "...",
    },
    {
      label: "Người xác nhận",
      key: "idNhanVien.tenNhanVien",
      formatter: (value) => value || "N/A",
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

  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => acc?.[part], obj) || null;
  };

  const openModal = () => {
    isModalOpen.value = true;
  };

  const closeModal = () => {
    isModalOpen.value = false;
  };

  onMounted(() => {
    fetchHoaDonDetail();
  });

  return {
    hoaDon,
    getNestedValue,
    paymentHistoryColumns,
    productColumns,
    isModalOpen,
    openModal,
    closeModal,
  };
}