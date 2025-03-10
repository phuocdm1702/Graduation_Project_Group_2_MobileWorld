import { ref, onMounted } from "vue";
import axios from "axios";

export default function useHoaDonLineList() {
  const dataTable = ref([]);
  const currentPage = ref(1); // Trang hiện tại (bắt đầu từ 1)
  const pageSize = ref(10); // Kích thước trang
  const totalPages = ref(0); // Tổng số trang
  const totalElements = ref(0); // Tổng số bản ghi

  // Biến cho form lọc
  const keyword = ref(""); // Thay searchQuery bằng keyword để đồng bộ với backend
  const minAmount = ref(null);
  const maxAmount = ref(null);
  const selectedOrderType = ref("");
  const startDate = ref("");
  const endDate = ref("");

  // Định nghĩa cột động (điều chỉnh để phù hợp với HoaDonDTO)
  const columns = [
    {
      label: "#",
      key: "index",
      formatter: (_, __, index) => (currentPage.value - 1) * pageSize.value + index + 1,
    },
    { label: "Mã", key: "ma" },
    { label: "Khách hàng", key: "idKhachHang.ten" },
    { label: "Nhân viên", key: "idNhanVien.ma" },
    { label: "SDT", key: "soDienThoaiKhachHang" },
    {
      label: "Tổng giá trị",
      key: "tongTien",
      formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
    },
    {
      label: "Tiền giảm",
      key: "idPhieuGiamGia.phanTramGiamGia",
      formatter: (phanTramGiamGia, item) => {
        if (!phanTramGiamGia || !item?.tongTien) return "0 VND";
        const giamGia = (item.tongTien * (phanTramGiamGia / 100)) || 0;
        const formattedGiamGia = Math.round(giamGia / 1000) * 1000; // Làm tròn đến hàng nghìn
        return `(${phanTramGiamGia}%) ~ ${formattedGiamGia.toLocaleString()}đ`;
      },
    },
    {
      label: "Phí",
      key: "phi_van_chuyen",
      formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
    },
    {
      label: "Sau Giảm giá",
      key: "tongTienSauGiam",
      formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
    },
    {
      label: "TG tạo",
      key: "ngayTao",
      formatter: (value) => (value ? new Date(value).toLocaleDateString() : "N/A"),
    },
    {
      label: "Trạng thái",
      key: "trangThai",
      formatter: (value) =>
        `<span class="${value === 1 ? "text-green-500" : "text-red-500"}">${
          value === 1 ? "Hoàn thành" : "Chờ xử lý"
        }</span>`,
    },
    { label: "Loại Đơn", key: "loaiDon" },
    {
      label: "Thao tác",
      key: "actions",
      formatter: (value, item) => `
        <a href="/hoa-don-chi-tiet/${item.id}" class="text-blue-500 hover:text-blue-700">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-eye" viewBox="0 0 16 16">
            <path d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 0 0 1 14.828 8c-.058.087-.122.183-.195.288-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-2.457A13.134 13.134 0 0 1 1.172 8z"/>
            <path d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5zM4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0z"/>
          </svg>
        </a>
      `,
    },
  ];

  // Hàm lấy giá trị lồng nhau (nested value)
  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => acc?.[part], obj) || null;
  };

  // Hàm lấy dữ liệu từ API
  const fetchData = async () => {
    try {
      const res = await axios.get("http://localhost:8080/hoa-don/home", {
        params: {
          page: currentPage.value -1, // Đồng bộ với backend (0-based)
          pageSize: pageSize.value, // Đồng bộ với backend
        },
      });
      console.log("Dữ liệu từ API:", res.data);
      // Kiểm tra và gán dữ liệu từ HoaDonDTO
      dataTable.value = res.data.content || [];
      totalPages.value = res.data.totalPages || 0;
      totalElements.value = res.data.totalElements || 0;
    } catch (error) {
      console.error("Lỗi khi gọi API:", error);
      dataTable.value = [];
      totalPages.value = 0;
      totalElements.value = 0;
    }
  };
  

  // Hàm áp dụng bộ lọc (bao gồm tìm kiếm)
  const applyFilters = () => {
    currentPage.value = 1; // Reset về trang đầu tiên khi lọc hoặc tìm kiếm
    fetchData().catch((error) => {
      console.error("Lỗi trong applyFilters:", error);
    });
  };

  // Chuyển trang trước
  const prevPage = () => {
    if (currentPage.value > 1) {
      currentPage.value--;
      fetchData().catch((error) => {
        console.error("Lỗi trong prevPage:", error);
      });
    }
  };

  const goToFirstPage = async () => {
    if (currentPage.value > 1) {
      currentPage.value = 1;
      try {
        await fetchData();
      } catch (error) {
        console.error("Lỗi trong goToFirstPage:", error);
      }
    }
  };

  const goToLastPage = async () => {
    if (currentPage.value < totalPages.value) {
      currentPage.value = totalPages.value;
      try {
        await fetchData();
      } catch (error) {
        console.error("Lỗi trong goToLastPage:", error);
      }
    }
  };


  // Chuyển trang sau
  const nextPage = () => {
    if (currentPage.value < totalPages.value) {
      currentPage.value++;
      fetchData().catch((error) => {
        console.error("Lỗi trong nextPage:", error);
      });
    }
  };

  // Gọi API khi component được mounted
  onMounted(() => {
    fetchData().catch((error) => {
      console.error("Lỗi trong onMounted:", error);
    });
  });

  return {
    dataTable,
    currentPage,
    pageSize,
    totalPages,
    prevPage,
    nextPage,
    keyword, // Trả về keyword thay vì searchQuery
    minAmount,
    maxAmount,
    selectedOrderType,
    startDate,
    endDate,
    applyFilters,
    columns,
    getNestedValue,
    goToFirstPage,
    goToLastPage
  };
}