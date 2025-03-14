import { ref, computed } from "vue";
import axios from "axios";
import ToggleSwitch from "@/components/ToggleSwitch.vue";

export default function usePhieuGiamGia() {
  const vouchers = ref([]);
  const searchQuery = ref("");
  const filterType = ref("");
  const filterStatus = ref("");
  const startDate = ref(null);
  const endDate = ref(null);
  const minOrder = ref(null);
  const valueFilter = ref(null);

  const baseURL = "http://localhost:8080/phieu-giam-gia";

  const fetchDataPGG = async () => {
    try {
      const response = await axios.get(`${baseURL}/data`);
      console.log("Dữ liệu nhận được từ API:", response.data);
      vouchers.value = response.data || [];
    } catch (error) {
      console.error("Error loading data:", error);
      vouchers.value = [];
    }
  };

  const searchPGG = async () => {
    try {
      let response;
      if (!searchQuery.value || searchQuery.value.trim() === "") {
        response = await axios.get(`${baseURL}/data`);
      } else {
        response = await axios.get(`${baseURL}/search`, {
          params: {
            keyword: searchQuery.value.trim(),
          },
        });
      }
      vouchers.value = response.data;
    } catch (error) {
      console.error("Lỗi search!", error);
    }
  };

  const toggleStatusPGG = async (item) => {
    try {
      const newStatus = !item.trangThai;
      await axios.put(`${baseURL}/update-trang-thai/${item.id}`, { trangThai: newStatus });
      item.trangThai = newStatus; // Cập nhật local state
    } catch (error) {
      console.error("Lỗi khi cập nhật trạng thái:", error);
      console.log("Response từ server:", error.response?.data); // Log chi tiết lỗi từ server
    }
  };
  

  // Gán sau khi deletePGG đã được khai báo

  const columns = ref([
    { key: "ma", label: "Mã" },
    { key: "tenPhieuGiamGia", label: "Tên Phiếu" },
    { key: "loaiPhieuGiamGia", label: "Loại Phiếu" },
    { key: "phanTramGiamGia", label: "Phần trăm giảm giá", formatter: (value) => `${value * 100}%` },
    { key: "soTienGiamToiDa", label: "Số tiền giảm tối đa" },
    { key: "soLuongDung", label: "Số lượng" },
    { key: "hoaDonToiThieu", label: "Hóa đơn tối thiểu" },
    { key: "ngayBatDau", label: "Ngày bắt đầu", formatter: (value) => new Date(value).toLocaleDateString("vi-VN") },
    { key: "ngayKetThuc", label: "Ngày kết thúc", formatter: (value) => new Date(value).toLocaleDateString("vi-VN") },
    { key: "moTa", label: "Mô tả" },
    { key: "trangThai", label: "Trạng thái", cellSlot: "trangThaiSlot" },
    {
      key: "actions",
      label: "Hành động",
      cellSlot: "actionsSlot",
    },
  ]);

  const getNestedValue = (obj, key) => {
    return key.split(".").reduce((o, k) => (o && o[k] !== undefined ? o[k] : null), obj);
  };

  const filterPGG = async () => {
    try {
      const params = {
        loaiPhieuGiamGia: filterType.value || null,
        trangThai: filterStatus.value || null,
        startDate: startDate.value || null,
        endDate: endDate.value || null,
        minOrder: minOrder.value ? Number(minOrder.value) : null,
        valueFilter: valueFilter.value ? Number(valueFilter.value) : null,
      };

      if (
        !params.loaiPhieuGiamGia &&
        !params.trangThai &&
        !params.startDate &&
        !params.endDate &&
        !params.minOrder &&
        !params.valueFilter
      ) {
        await fetchDataPGG();
        return;
      }

      const response = await axios.get(`${baseURL}/filter`, { params });
      vouchers.value = response.data || [];
    } catch (error) {
      console.error("Lỗi khi lọc phiếu giảm giá:", error);
      vouchers.value = [];
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
    searchPGG,
    fetchDataPGG,
    filterPGG,
    toggleStatusPGG,
  };
}