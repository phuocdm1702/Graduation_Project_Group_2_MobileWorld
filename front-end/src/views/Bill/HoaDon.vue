<template>
  <div class="mt-8 max-w-screen-xl mx-auto">
  <h4 class="text-xl font-semibold text-gray-700">📋 Danh sách Hóa Đơn</h4>

  <!-- Form lọc -->
  <div
    class="bg-white shadow-lg rounded-lg p-5 mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
    <input v-model="searchQuery" type="text" placeholder="🔍 Tìm kiếm hóa đơn..." class="input-field"/>
    <input v-model="minAmount" type="number" placeholder="💰 Giá tối thiểu" class="input-field"/>
    <input v-model="maxAmount" type="number" placeholder="💰 Giá tối đa" class="input-field"/>

    <select v-model="selectedOrderType" class="input-field">
      <option value="">📦 Tất cả loại đơn</option>
      <option value="Online">Online</option>
      <option value="Tại cửa hàng">Tại cửa hàng</option>
    </select>

    <input v-model="startDate" type="date" class="input-field"/>
    <input v-model="endDate" type="date" class="input-field"/>

    <button @click="applyFilters"
            class="col-span-full sm:col-span-2 md:col-span-3 lg:col-span-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition">
      🔍 Lọc
    </button>
  </div>

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
  </div>
</template>

<script setup>
import useHoaDonLineList from "@/views/Bill/HoaDon";

const {
  dataTable
} = useHoaDonLineList();
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
.th-cell {
  @apply px-4 py-3 text-left border-b;
}

.td-cell {
  @apply px-4 py-2 text-sm;
}

</style>
