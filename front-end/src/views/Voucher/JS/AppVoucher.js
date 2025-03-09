import { ref, watch } from "vue";
import axios from "axios";

export default function usePhieuGiamGia() {
  const vouchers = ref([]);
  const searchQuery = ref("");
  const filterType = ref("");
  const filterStatus = ref("");

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
      if (!searchQuery.value || !searchQuery.value.trim()) {
        response = await axios.get(`${baseURL}/data`);
      } else {
        response = await axios.get(`${baseURL}/search`, {
          params: {
            keyword: searchQuery.value,
            loaiPhieuGiamGia: filterType.value === "Phần trăm" ? 1 : filterType.value === "Tiền mặt" ? 2 : null,
            trangThai: filterStatus.value === "Còn hạn" ? 1 : filterStatus.value === "Hết hạn" ? 0 : null
          }
        });
      }
      vouchers.value = response.data;
    } catch (error) {
      console.error("Lỗi search!", error);
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
    searchPGG,
    deletePGG,
    fetchDataPGG
  };
}
