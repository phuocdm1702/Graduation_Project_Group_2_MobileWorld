import { ref, onMounted } from "vue";
import axios from "axios";

export default function useHoaDonLineList(){
  const dataTable = ref([]);

  onMounted(async () => {
    try {
      const res = await axios.get("http://localhost:8080/hoa-don/home");
      console.log("Dữ liệu từ API:", res.data);
      dataTable.value = res.data;
    } catch (error) {
      console.error("Lỗi:", error);
    }
  });
return{
  dataTable,
}
}