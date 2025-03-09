import { ref, onMounted } from "vue";
import axios from "axios";

export default function useHoaDonChiTietLineList(){
  const dataTableHDCT = ref([]);

  onMounted(async () => {
    try {
      const res = await axios.get("http://localhost:8080/hoa-don-chi-tiet/home");
      console.log("Dữ liệu từ API:", res.data);
      dataTableHDCT.value = res.data;
    } catch (error) {
      console.error("Lỗi:", error);
    }
  });
  
  return{
    dataTableHDCT
  }
}