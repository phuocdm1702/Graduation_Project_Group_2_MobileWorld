import {onMounted, ref} from "vue";
import axios from "axios";

export default function useLichSuPhieuBaoHanhList(){
  const dataTablelsbh = ref([]);
  
  
  onMounted(async () => {
    try {
      const res = await axios.get("http://localhost:8080/lich-su-bao-hanh/home");
      console.log("Dữ liệu từ API:", res.data);
      dataTablelsbh.value = res.data;
    } catch (error) {
      console.error("Lỗi:", error);
    }
  })
  
  return{
    dataTablelsbh
  }
}