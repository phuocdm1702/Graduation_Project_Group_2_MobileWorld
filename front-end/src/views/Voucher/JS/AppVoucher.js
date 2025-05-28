import { ref } from "vue";
import axios from "axios";
import { debounce } from "lodash"; // Cần cài đặt: npm install lodash

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

  const computeDisplayStatus = (item) => {
    const currentDate = new Date();
    const startDate = new Date(item.ngayBatDau);
    const endDate = new Date(item.ngayKetThuc);

    if (startDate > currentDate) {
      return {
        text: "Chưa diễn ra",
        isActive: false,
        cssClass: "badge-pending",
        isToggleDisabled: true,
        isHiddenToggle: true,
      };
    } else if (endDate >= currentDate) {
      if (item.trangThai) {
        return {
          text: "Đang diễn ra",
          isActive: true,
          cssClass: "badge-active",
          isToggleDisabled: false,
          isHiddenToggle: false,
        };
      } else {
        return {
          text: "Không hoạt động",
          isActive: false,
          cssClass: "badge-inactive",
          isToggleDisabled: false,
          isHiddenToggle: false,
        };
      }
    }
    return null; // Phiếu hết hạn không hiển thị
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
        params: { page, size: pageSize.value },
      });
      const rawVouchers = response.data.content || [];
      vouchers.value = updateVouchersWithDisplayStatus(rawVouchers);
      totalPages.value = response.data.totalPages;
    } catch (error) {
      console.error("Error loading data:", error.response?.data || error);
    }
  };

  const searchPGG = async (page = 0) => {
    try {
      let response;
      if (!searchQuery.value || searchQuery.value.trim() === "") {
        response = await axios.get(`${baseURL}/data`, {
          params: { page, size: pageSize.value },
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
      const rawVouchers = response.data.content || [];
      vouchers.value = updateVouchersWithDisplayStatus(rawVouchers);
      totalPages.value = response.data.totalPages;
    } catch (error) {
      console.error("Lỗi search!", error.response?.data || error);
    }
  };

  const filterPGG = async (page = 0) => {
    try {
      const formatDate = (dateValue) => {
        if (!dateValue) return null;
        const date = dateValue instanceof Date ? dateValue : new Date(dateValue);
        return isNaN(date.getTime()) ? null : date.toISOString().split("T")[0];
      };

      let trangThaiValue = filterStatus.value || null;
      let tempStartDate = startDate.value;
      let tempEndDate = endDate.value;

      if (trangThaiValue) {
        if (["Đang diễn ra", "active"].includes(trangThaiValue)) {
          trangThaiValue = true; // trangThai = true
          tempStartDate = null;
          tempEndDate = null;
        } else if (["Không hoạt động", "inactive"].includes(trangThaiValue)) {
          trangThaiValue = false; // trangThai = false
          tempStartDate = null;
          tempEndDate = null;
        } else if (trangThaiValue === "Chưa diễn ra") {
          trangThaiValue = null;
          tempStartDate = new Date().toISOString().split("T")[0];
          tempEndDate = null;
        } else {
          trangThaiValue = null;
        }
      }

      const params = {
        loaiPhieuGiamGia: filterType.value === "" ? null : filterType.value,
        trangThai: trangThaiValue,
        startDate: formatDate(tempStartDate),
        endDate: formatDate(tempEndDate),
        minOrder: minOrder.value ? Number(minOrder.value) : null,
        valueFilter: valueFilter.value ? Number(valueFilter.value) : null,
        page,
        size: pageSize.value,
      };

      console.log("Filter params:", params);

      const response = await axios.get(`${baseURL}/filter`, { params });
      const rawVouchers = response.data.content || [];
      vouchers.value = updateVouchersWithDisplayStatus(rawVouchers);
      totalPages.value = response.data.totalPages || 0;
    } catch (error) {
      console.error("Lỗi khi lọc phiếu giảm giá:", error.response?.data || error);
    }
  };

  const toggleStatusPGG = async (item) => {
    try {
      if (item.displayStatus?.isToggleDisabled) {
        console.log(`Cannot toggle ${item.ma}: ToggleSwitch is disabled`);
        return;
      }
      const newStatus = !item.trangThai; // true -> Hoạt động, false -> Không hoạt động
      await axios.put(`${baseURL}/update-trang-thai/${item.id}`, { trangThai: newStatus });

      const index = vouchers.value.findIndex((v) => v.id === item.id);
      if (index !== -1) {
        vouchers.value[index].trangThai = newStatus;
        vouchers.value[index].displayStatus = computeDisplayStatus(vouchers.value[index]);
        vouchers.value = [...vouchers.value];
      }
    } catch (error) {
      console.error("Lỗi khi cập nhật trạng thái:", error.response?.data || error);
    }
  };

  const columns = ref([
    { key: "stt", label: "#", formatter: (_, __, index) => index + 1 + currentPage.value * pageSize.value, width: "50px" },
    { key: "ma", label: "Mã", width: "80px" },
    { key: "tenPhieuGiamGia", label: "Tên Phiếu", width: "150px" },
    {
      key: "phanTramGiamGia",
      label: "% Giảm Giá",
      formatter: (value, item) => {
        if (item.loaiPhieuGiamGia === "Phần trăm") {
          return value ? `${value}%` : "N/A";
        } else if (item.loaiPhieuGiamGia === "Tiền mặt" && item.soTienGiamToiDa && item.hoaDonToiThieu) {
          const percent = (item.soTienGiamToiDa / item.hoaDonToiThieu) * 100;
          return `${percent.toFixed(1)}%`;
        }
        return "N/A";
      },
      width: "50px",
    },
    {
      key: "soTienGiamToiDa",
      label: "Giảm Tối Đa",
      formatter: (value, item) => {
        if (item.loaiPhieuGiamGia === "Phần trăm") {
          const phanTramGiamGia = item.phanTramGiamGia || 0;
          const hoaDonToiThieu = item.hoaDonToiThieu || 0;
          const soTienGiamToiDa = (phanTramGiamGia / 100) * hoaDonToiThieu;
          return soTienGiamToiDa.toLocaleString("vi-VN") + " VND";
        }
        return value ? value.toLocaleString("vi-VN") + " VND" : "0 VND";
      },
      width: "120px",
    },
    { key: "soLuongDung", 
      label: "Số lượng", 
      formatter: (value, item) => {
        return item.soLuongDung == -1 ? "Vô hạn" : value;
      }, 
      width: "80px" },
    { key: "hoaDonToiThieu", label: "Hóa\nĐơn\nTối\nThiểu", formatter: (value) => value.toLocaleString("vi-VN") + " VND", width: "100px" },
    { key: "ngayBatDau", label: "Bắt đầu", formatter: (value) => new Date(value).toLocaleDateString("vi-VN"), width: "100px" },
    { key: "ngayKetThuc", label: "Kết thúc", formatter: (value) => new Date(value).toLocaleDateString("vi-VN"), width: "100px" },
    { key: "displayStatus", label: "Trạng thái", cellSlot: "trangThaiPGG", width: "120px" },
    { key: "actions", label: "Hành động", cellSlot: "actionsSlot", width: "100px" },
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
    } else if (filterType.value || filterStatus.value || startDate.value || endDate.value || minOrder.value || valueFilter.value) {
      filterPGG(page);
    } else {
      fetchDataPGG(page);
    }
  };

  const debouncedSearchPGG = debounce(searchPGG, 300);
  const debouncedFilterPGG = debounce(filterPGG, 300);

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
    searchPGG: debouncedSearchPGG,
    fetchDataPGG,
    filterPGG: debouncedFilterPGG,
    toggleStatusPGG,
    goToPage,
  };
}