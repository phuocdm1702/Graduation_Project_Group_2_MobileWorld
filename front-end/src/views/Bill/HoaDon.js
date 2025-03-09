import { ref, onMounted } from "vue";
import axios from "axios";

export default function useHoaDonLineList() {
  const dataTable = ref([]);
  const currentPage = ref(1); // Trang hiện tại (bắt đầu từ 1)
  const pageSize = ref(3); // Kích thước trang
  const totalPages = ref(0); // Tổng số trang
  const totalElements = ref(0); // Tổng số bản ghi

  // Biến cho form lọc
  const searchQuery = ref("");
  const minAmount = ref(null);
  const maxAmount = ref(null);
  const selectedOrderType = ref("");
  const startDate = ref("");
  const endDate = ref("");

  // Định nghĩa cột động
  const columns = [
    {
      label: "#",
      key: "index",
      formatter: (_, __, index) => (currentPage.value - 1) * pageSize.value + index + 1,
    },
    { label: "Mã hóa đơn", key: "ma" },
    { label: "Nhân viên", key: "idNhanVien.ma" },
    { label: "Khách hàng", key: "idKhachHang.ten" },
    { label: "SDT", key: "soDienThoaiKhachHang" },
    { label: "Loại Đơn", key: "loaiDon" },
    {
      label: "Tổng tiền",
      key: "tongTien",
      formatter: (value) => `${value.toLocaleString()} VND`,
    },
    {
      label: "Sau Giảm giá",
      key: "tongTienSauGiam",
      formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
    },
    {
      label: "Ngày Tạo",
      key: "ngayTao",
      formatter: (value) => new Date(value).toLocaleDateString(),
    },
    {
      label: "Trạng thái",
      key: "trangThai",
      formatter: (value) =>
        `<span class="${value === 1 ? "text-green-500" : "text-red-500"}">${
          value === 1 ? "Hoàn thành" : "Chờ xử lý"
        }</span>`,
    },
    { label: "Thao tác", key: "actions", formatter: () => "" }, // Để trống hoặc thêm nút thao tác sau
  ];

  // Hàm lấy giá trị lồng nhau (nested value)
  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => acc && acc[part], obj);
  };

  // Hàm lấy dữ liệu từ API
  const fetchData = async () => {
    try {
      const res = await axios.get("http://localhost:8080/hoa-don/home", {
        params: {
          page: currentPage.value - 1,
          size: pageSize.value,
          search: searchQuery.value || undefined,
          minAmount: minAmount.value || undefined,
          maxAmount: maxAmount.value || undefined,
          orderType: selectedOrderType.value || undefined,
          startDate: startDate.value || undefined,
          endDate: endDate.value || undefined,
        },
      });
      console.log("Dữ liệu từ API:", res.data);
      dataTable.value = res.data.content;
      totalPages.value = res.data.totalPages;
      totalElements.value = res.data.totalElements;
    } catch (error) {
      console.error("Lỗi khi gọi API:", error);
      // Xử lý lỗi (ví dụ: đặt giá trị mặc định)
      dataTable.value = [];
      totalPages.value = 0;
      totalElements.value = 0;
    }
  };

  // Hàm áp dụng bộ lọc
  const applyFilters = () => {
    currentPage.value = 1; // Reset về trang đầu tiên khi lọc
    fetchData();
  };

  // Chuyển trang trước
  const prevPage = () => {
    if (currentPage.value > 1) {
      currentPage.value--;
      fetchData();
    }
  };

  // Chuyển trang sau
  const nextPage = () => {
    if (currentPage.value < totalPages.value) {
      currentPage.value++;
      fetchData();
    }
  };

  // Gọi API khi component được mounted
  onMounted(() => {
    fetchData();
  });

  return {
    dataTable,
    currentPage,
    pageSize,
    totalPages,
    prevPage,
    nextPage,
    searchQuery,
    minAmount,
    maxAmount,
    selectedOrderType,
    startDate,
    endDate,
    applyFilters,
    columns,
    getNestedValue,
  };
}