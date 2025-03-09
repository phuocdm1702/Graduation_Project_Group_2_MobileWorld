import { ref, onMounted } from "vue";
import axios from "axios";


export default function useLichSuHoaDonLineList(){
  const dataTableLSHD = ref([]);

  onMounted(async () => {
    try {
      const res = await axios.get("http://localhost:8080/lich-su-hoa-don/home");
      console.log("Dữ liệu từ API:", res.data);
      dataTableLSHD.value = res.data;
    } catch (error) {
      console.error("Lỗi:", error);
    }
  });
  
  return{
    dataTableLSHD
  }
}