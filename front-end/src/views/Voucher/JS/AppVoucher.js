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
  const pageSize = ref(11);
  const totalPages = ref(0);

  const baseURL = "http://localhost:8080/phieu-giam-gia";

  const computeDisplayStatus = (item) => {
    const currentDate = new Date();
    const startDate = new Date(item.ngayBatDau);
    const endDate = new Date(item.ngayKetThuc);

    if (startDate > currentDate) {
      return {
        text: "Chưa diễn ra",
        isActive: false,
        cssClass: "badge-pending",
        isToggleDisabled: true, // Vô hiệu hóa ToggleSwitch
      };
    } else if (endDate >= currentDate) {
      if (item.trangThai) {
        return {
          text: "Không hoạt động",
          isActive: true,
          cssClass: "badge-inactive",
          isToggleDisabled: false, // Cho phép bật/tắt
        };
      } else {
        return {
          text: "Đang diễn ra",
          isActive: false,
          cssClass: "badge-active",
          isToggleDisabled: false, // Cho phép bật/tắt
        };
      }
    }
    return null;
  };

  const updateVouchersWithDisplayStatus = (voucherList) => {
    return voucherList
      .map((item) => {
        const displayStatus = computeDisplayStatus(item);
        if (displayStatus === null) return null;
        return {
          ...item,
          displayStatus: displayStatus,
        };
      })
      .filter((item) => item !== null);
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
      // Helper function to format date safely
      const formatDate = (dateValue) => {
        if (!dateValue) return null; // Handle null or undefined
        const date = dateValue instanceof Date ? dateValue : new Date(dateValue);
        return isNaN(date.getTime()) ? null : date.toISOString().split("T")[0];
      };

      const params = {
        loaiPhieuGiamGia: filterType.value || null,
        trangThai: filterStatus.value || null,
        startDate: formatDate(startDate.value),
        endDate: formatDate(endDate.value),
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
      if (item.displayStatus?.isToggleDisabled) {
        console.log(`Cannot toggle ${item.ma}: ToggleSwitch is disabled`);
        return;
      }
      const newStatus = !item.trangThai;
      await axios.put(`${baseURL}/update-trang-thai/${item.id}`, { trangThai: newStatus });
      item.trangThai = newStatus; // Cập nhật trạng thái gốc
      const displayStatus = computeDisplayStatus(item);
      item.displayStatus = displayStatus;
      console.log(`Toggled ${item.ma}: trangThai = ${item.trangThai}, displayStatus = ${item.displayStatus.text}`);
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
          return soTienGiamToiDa.toLocaleString("vi-VN") + " VND";
        }
        return value ? value.toLocaleString("vi-VN") + " VND" : "0 VND";
      },
    },
    { key: "soLuongDung", label: "Số lượng" },
    { key: "hoaDonToiThieu", 
      label: "Hóa\nĐơn\nTối\nThiểu", 
      formatter: (value, item) => {
        return value.toLocaleString("vi-VN") + " VND";
      }
    },
    { key: "ngayBatDau", label: "Ngày\nBĐ", formatter: (value) => new Date(value).toLocaleDateString("vi-VN") },
    { key: "ngayKetThuc", label: "Ngày\nKT", formatter: (value) => new Date(value).toLocaleDateString("vi-VN") },
    { key: "displayStatus", label: "Trạng thái", cellSlot: "trangThaiPGG" },
    { key: "actions", label: "Hành động", cellSlot: "actionsSlot" },
  ]);

  const getNestedValue = (obj, key) => {
    if (key === "displayStatus") {
      return obj.displayStatus?.text || "N/A";
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