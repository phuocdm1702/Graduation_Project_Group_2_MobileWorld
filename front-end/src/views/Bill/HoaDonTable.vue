<template>
    <!-- Bảng dữ liệu -->
    <div class="overflow-x-auto mt-6 bg-white shadow-lg rounded-lg p-4">
      <table class="w-full min-w-max table-auto border-collapse">
        <thead>
        <tr class="bg-gray-100 text-gray-700 text-sm font-semibold">
          <th class="th-cell">STT</th>
          <th class="th-cell">Mã</th>
          <th class="th-cell">Nhân viên</th>
          <th class="th-cell">Khách hàng</th>
          <th class="th-cell">SDT</th>
          <th class="th-cell">Loại Đơn</th>
          <th class="th-cell">Tổng tiền</th>
          <th class="th-cell"> Sau Giảm giá</th>
          <th class="th-cell">Ngày Tạo</th>
          <th class="th-cell">Trạng thái</th>
          <th class="th-cell">Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(hoaDon,index) in dataTable" :key="hoaDon.id" class="text-gray-700 border-b hover:bg-gray-50">
          <td class="td-cell">{{ index + 1 }}</td>
          <td class="td-cell">{{ hoaDon.ma }}</td>
          <td class="td-cell">{{ hoaDon.idNhanVien.ma }}</td>
          <td class="td-cell">{{ hoaDon.idKhachHang.ten }}</td>
          <td class="td-cell">{{ hoaDon.soDienThoaiKhachHang }}</td>
          <td class="td-cell">{{ hoaDon.loaiDon }}</td>
          <td class="td-cell">{{ hoaDon.tongTien.toLocaleString() }} VND</td>
          <td class="td-cell">{{ hoaDon.tongTienSauGiam ? hoaDon.tongTienSauGiam.toLocaleString() : "0" }} VND</td>
          <td class="td-cell">{{ new Date(hoaDon.ngayTao).toLocaleDateString() }}</td>
          <td class="td-cell">
      <span :class="hoaDon.trangThai === 1 ? 'text-green-500' : 'text-red-500'">
        {{ hoaDon.trangThai === 1 ? "Hoàn thành" : "Chờ xử lý" }}
      </span>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
</template>

// export default {
//   name: "HoaDonTable"
// }
<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

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

</script>
<style scoped>
.th-cell {
  @apply px-4 py-3 text-left border-b;
}

.td-cell {
  @apply px-4 py-2 text-sm;
}


</style>