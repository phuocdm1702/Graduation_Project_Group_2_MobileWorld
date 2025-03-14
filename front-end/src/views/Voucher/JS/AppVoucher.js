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

  const baseURL = "http://localhost:8080/phieu-giam-gia";

  const fetchDataPGG = async () => {
    try {
      const response = await axios.get(`${baseURL}/data`);
      console.log("Dữ liệu nhận được từ API:", response.data);
      vouchers.value = response.data || [];
    } catch (error) {
      console.error("Error loading data:", error);
      vouchers.value = []; // Đảm bảo không bị undefined
    }
  };


  const searchPGG = async () => {
    try {
      let response;
      if(!searchQuery.value || searchQuery.value.trim() === "") {
        response = await axios.get(`${baseURL}/data`)
      } else {
        response = await axios.get(`${baseURL}/search`, {
          params: {
            keyword: searchQuery.value.trim(),
          }
        });
      }
      vouchers.value = response.data;
    } catch (error) {
      console.error("Lỗi search!", error);
    }
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

      if (!params.loaiPhieuGiamGia && !params.trangThai && !params.startDate &&
        !params.endDate && !params.minOrder && !params.valueFilter) {
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

  const deletePGG = async (id) => {
    try {
      await axios.delete(`${baseURL}/delete/${id}`);
      await fetchDataPGG();
    } catch (error) {
      console.log("Lỗi khi xóa:", error);
    }
  };

  return {
    vouchers,
    searchQuery,
    filterType,
    filterStatus,
    startDate,
    endDate,
    minOrder,
    valueFilter,
    searchPGG,
    deletePGG,
    fetchDataPGG,
    filterPGG,
  };
}
