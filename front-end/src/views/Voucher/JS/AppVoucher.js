import { ref, computed } from "vue";
import axios from "axios";

export default function usePhieuGiamGia() {
  const vouchers = ref([]);
  const searchQuery = ref("");
  const filterType = ref("");
  const filterStatus = ref("");
  const startDate = ref(null);
  const endDate = ref(null);
  const minOrder = ref(null);
  const valueFilter = ref(null);
  const currentPage = ref(0);
  const pageSize = ref(10);
  const totalPages = ref(0);

  const baseURL = "http://localhost:8080/phieu-giam-gia";

  // Hàm tính toán trạng thái hiển thị
  const computeDisplayStatus = (item) => {
    const currentDate = new Date();
    const startDate = new Date(item.ngayBatDau);
    const endDate = new Date(item.ngayKetThuc);

    if (startDate > currentDate) {
      return { text: "Chưa diễn ra", isActive: false };
    } else if (endDate >= currentDate) {
      return item.trangThai
        ? { text: "Không hoạt động", isActive: true }
        : { text: "Hoạt động", isActive: false };
    }
    return null; // Trả về null cho các PGG đã hết hạn để lọc bỏ sau
  };

  // Hàm cập nhật vouchers với displayStatus và điều chỉnh trangThai
  const updateVouchersWithDisplayStatus = (voucherList) => {
    return voucherList
      .map((item) => {
        const displayStatus = computeDisplayStatus(item);
        if (displayStatus === null) return null; // Bỏ qua các PGG đã hết hạn
        return {
          ...item,
          displayStatus,
          trangThai: displayStatus.isActive,
        };
      })
      .filter((item) => item !== null); // Loại bỏ các item null
  };

  const fetchDataPGG = async (page = 0) => {
    try {
      const response = await axios.get(`${baseURL}/data`, {
        params: {
          page,
          size: pageSize.value,
        },
      });
      const rawVouchers = response.data.content || [];
      vouchers.value = updateVouchersWithDisplayStatus(rawVouchers);
      totalPages.value = response.data.totalPages;
    } catch (error) {
      console.error("Error loading data:", error);
      vouchers.value = [];
    }
  };

  const searchPGG = async (page = 0) => {
    try {
      let response;
      if (!searchQuery.value || searchQuery.value.trim() === "") {
        response = await axios.get(`${baseURL}/data`, {
          params: {
            page,
            size: pageSize.value,
          },
        });
      } else {
        response = await axios.get(`${baseURL}/search`, {
          params: {
            keyword: searchQuery.value.trim(),
            page,
            size: pageSize.value,
          },
        });
      }
      const rawVouchers = response.data.content;
      vouchers.value = updateVouchersWithDisplayStatus(rawVouchers);
      totalPages.value = response.data.totalPages;
    } catch (error) {
      console.error("Lỗi search!", error);
    }
  };

  const filterPGG = async (page = 0) => {
    try {
      const params = {
        loaiPhieuGiamGia: filterType.value || null,
        trangThai: filterStatus.value || null,
        startDate: startDate.value ? startDate.value.toISOString().split("T")[0] : null,
        endDate: endDate.value ? endDate.value.toISOString().split("T")[0] : null,
        minOrder: minOrder.value ? Number(minOrder.value) : null,
        valueFilter: valueFilter.value ? Number(valueFilter.value) : null,
        page,
        size: pageSize.value,
      };

      const response = await axios.get(`${baseURL}/filter`, { params });
      const rawVouchers = response.data.content || [];
      vouchers.value = updateVouchersWithDisplayStatus(rawVouchers);
      totalPages.value = response.data.totalPages || 0;
    } catch (error) {
      console.error("Lỗi khi lọc phiếu giảm giá:", error);
      vouchers.value = [];
    }
  };

  const toggleStatusPGG = async (item) => {
    try {
      const newStatus = !item.trangThai;
      await axios.put(`${baseURL}/update-trang-thai/${item.id}`, { trangThai: newStatus });
      item.trangThai = newStatus; // Cập nhật trạng thái gốc
      // Cập nhật lại displayStatus và trangThai
      const displayStatus = computeDisplayStatus(item);
      item.displayStatus = displayStatus;
      item.trangThai = displayStatus.isActive; // Điều chỉnh lại trangThai để DynamicTable.vue sử dụng
    } catch (error) {
      console.error("Lỗi khi cập nhật trạng thái:", error);
      console.log("Response từ server:", error.response?.data);
    }
  };

  const columns = ref([
    {
      key: "stt",
      label: "#",
      formatter: (_, __, index) => index + 1 + currentPage.value * pageSize.value,
    },
    { key: "ma", label: "Mã" },
    { key: "tenPhieuGiamGia", label: "Tên Phiếu" },
    { key: "phanTramGiamGia", label: "% Giảm Giá", formatter: (value) => `${value}%` },
    {
      key: "soTienGiamToiDa",
      label: "Số Tiền Giảm TĐ",
      formatter: (value, item) => {
        if (item.loaiPhieuGiamGia === "Phần trăm") {
          const phanTramGiamGia = item.phanTramGiamGia || 0;
          const hoaDonToiThieu = item.hoaDonToiThieu || 0;
          const soTienGiamToiDa = (phanTramGiamGia / 100) * hoaDonToiThieu;
          return soTienGiamToiDa.toLocaleString("vi-VN", {
            style: "currency",
            currency: "VND",
          });
        }
        return value ? value.toLocaleString("vi-VN", { style: "currency", currency: "VND" }) : "0 ₫";
      },
    },
    { key: "soLuongDung", label: "Số lượng" },
    { key: "hoaDonToiThieu", label: "Hóa\nĐơn\nTối\nThiểu" },
    { key: "ngayBatDau", label: "Ngày\nBĐ", formatter: (value) => new Date(value).toLocaleDateString("vi-VN") },
    { key: "ngayKetThuc", label: "Ngày\nKT", formatter: (value) => new Date(value).toLocaleDateString("vi-VN") },
    { key: "displayStatus", label: "Trạng thái", cellSlot: "trangThaiSlot" }, // Thay đổi key thành "displayStatus"
    { key: "actions", label: "Hành động", cellSlot: "actionsSlot" },
  ]);

  const getNestedValue = (obj, key) => {
    if (key === "displayStatus") {
      return obj.displayStatus?.text || "N/A"; // Trả về text của displayStatus
    }
    return key.split(".").reduce((o, k) => (o && o[k] !== undefined ? o[k] : null), obj) || "N/A";
  };

  const goToPage = (page) => {
    currentPage.value = page;
    if (searchQuery.value.trim().length > 0) {
      searchPGG(page);
    } else if (
      filterType.value ||
      filterStatus.value ||
      startDate.value ||
      endDate.value ||
      minOrder.value ||
      valueFilter.value
    ) {
      filterPGG(page);
    } else {
      fetchDataPGG(page);
    }
  };

  return {
    vouchers,
    columns,
    getNestedValue,
    searchQuery,
    filterType,
    filterStatus,
    startDate,
    endDate,
    minOrder,
    valueFilter,
    currentPage,
    pageSize,
    totalPages,
    searchPGG,
    fetchDataPGG,
    filterPGG,
    toggleStatusPGG,
    goToPage,
  };
}