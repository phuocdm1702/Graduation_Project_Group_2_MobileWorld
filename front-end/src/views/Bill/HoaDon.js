import { ref, onMounted, computed } from "vue";
import axios from "axios";

export default function useHoaDonLineList() {
  const dataTable = ref([]);
  const currentPage = ref(0);
  const pageSize = ref(7);
  const totalElements = ref(0);

  // Filter variables
  const keyword = ref("");
  const minAmount = ref(null);
  const maxAmount = ref(null);
  const selectedOrderType = ref("");
  const startDate = ref("");
  const endDate = ref("");
  const isFiltering = ref(false);

  const totalPages = computed(() => Math.ceil(totalElements.value / pageSize.value));

  const columns = [
    {
      label: "#",
      key: "index",
      formatter: (_, __, index) => currentPage.value * pageSize.value + index + 1,
    },
    { label: "Mã", key: "ma" },
    { label: "Nhân viên", key: "idNhanVien.ma" },
    { label: "Khách hàng", key: "idKhachHang.ten" },
    { label: "SDT", key: "soDienThoaiKhachHang" },
    {
      label: "Tổng giá trị",
      key: "tongTienSauGiam",
      formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
    },
    {
      label: "Tiền giảm",
      key: "idPhieuGiamGia.phanTramGiamGia",
      formatter: (phanTramGiamGia, item) => {
        if (!phanTramGiamGia || !item?.tongTien) return "0 VND";
        const giamGia = (item.tongTien * (phanTramGiamGia / 100)) || 0;
        const formattedGiamGia = Math.round(giamGia / 1000) * 1000;
        return `(${phanTramGiamGia}%) ~ ${formattedGiamGia.toLocaleString()}đ`;
      },
    },
    {
      label: "Phí",
      key: "phi_van_chuyen",
      formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
    },
    {
      label: "TG tạo",
      key: "ngayTao",
      formatter: (value) => (value ? new Date(value).toLocaleDateString() : "N/A"),
    },
    { label: "Loại Đơn", key: "loaiDon" },
    {
      label: "Trạng thái",
      key: "trangThai",
      formatter: (value) =>
        `<span class="${value === 1 ? "text-green-500" : "text-red-500"}">${
          value === 1 ? "Hoàn thành" : "Chờ xử lý"
        }</span>`,
    },
    {
      label: "Thao tác",
      key: "actions",
      formatter: (value, item) => `
        <a href="/show-hoa-don/${item.id}" class="text-blue-500 hover:text-blue-700 mr-2">
          <i class="fa-solid fa-edit" style="color: #f97316;"></i>
        </a>
        <a href="#" onclick="printInvoice(${item.id})" class="text-blue-500 hover:text-blue-700">
          <i class="fa-solid fa-print" style="color: #f97316;"></i>
        </a>
      `,
    },
  ];

  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => acc?.[part], obj) || null;
  };

  const fetchData = async () => {
    try {
      const res = await axios.get("http://localhost:8080/hoa-don/home", {
        params: {
          page: currentPage.value,
          size: pageSize.value,
        },
      });
      dataTable.value = res.data.content || [];
      totalElements.value = res.data.totalElements || 0;
    } catch (error) {
      console.error("Lỗi khi gọi API:", error);
      dataTable.value = [];
      totalElements.value = 0;
    }
  };

  const applyFilters = async () => {
    const hasFilters = keyword.value || minAmount.value || maxAmount.value ||
      selectedOrderType.value || startDate.value || endDate.value;

    isFiltering.value = hasFilters;
    currentPage.value = 0;

    try {
      const res = await axios.get("http://localhost:8080/hoa-don/home", {
        params: {
          page: currentPage.value,
          size: pageSize.value,
          keyword: keyword.value || undefined,
          minAmount: minAmount.value || undefined,
          maxAmount: maxAmount.value || undefined,
          loaiDon: selectedOrderType.value || undefined,
          startDate: startDate.value || undefined,
          endDate: endDate.value || undefined,
        },
      });
      dataTable.value = res.data.content || [];
      totalElements.value = res.data.totalElements || 0;
    } catch (error) {
      console.error("Lỗi khi lọc dữ liệu:", error);
      dataTable.value = [];
      totalElements.value = 0;
    }
  };

  const goToPage = async (page) => {
    currentPage.value = page;
    if (isFiltering.value) {
      await applyFilters();
    } else {
      await fetchData();
    }
  };

  const prevPage = async () => {
    if (currentPage.value > 0) {
      currentPage.value--;
      if (isFiltering.value) {
        await applyFilters();
      } else {
        await fetchData();
      }
    }
  };

  const nextPage = async () => {
    if (currentPage.value < totalPages.value - 1) {
      currentPage.value++;
      if (isFiltering.value) {
        await applyFilters();
      } else {
        await fetchData();
      }
    }
  };

  const goToFirstPage = async () => {
    if (currentPage.value > 0) {
      currentPage.value = 0;
      if (isFiltering.value) {
        await applyFilters();
      } else {
        await fetchData();
      }
    }
  };

  const goToLastPage = async () => {
    if (currentPage.value < totalPages.value - 1) {
      currentPage.value = totalPages.value - 1;
      if (isFiltering.value) {
        await applyFilters();
      } else {
        await fetchData();
      }
    }
  };

  onMounted(() => {
    fetchData();
  });

  return {
    dataTable,
    currentPage,
    pageSize,
    totalPages,
    totalElements,
    goToPage,
    keyword,
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