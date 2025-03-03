import {onMounted, ref} from "vue";
import axios from "axios";

export default function usePhieuBaoHanhLineList() {
  const dataTablePBH = ref([]);
  const dataCBO = ref([]);
  onMounted(async () => {
    try {
      const res1 = await axios.get("http://localhost:8080/phieu-bao-hanh/home");
      const res2 = await axios.get("http://localhost:8080/phieu-bao-hanh/cbo");
      console.log("Dữ liệu từ API:", res1.data);
      console.log("Dữ liệu từ API:", res2.data);
      dataTablePBH.value = res1.data;
      dataCBO.value = res2.data;
    }catch (error){
      console.error("Lỗi:", error);
    }
  })
  
  
  return{
    dataTablePBH,
    dataCBO
  }

}