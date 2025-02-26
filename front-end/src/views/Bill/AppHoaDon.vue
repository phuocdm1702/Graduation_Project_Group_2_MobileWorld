<template>
  <p class="mt-4 text-lg font-semibold text-red-500">{{ message }}</p>
  <div class="mt-8 max-w-screen-xl mx-auto">
    <h4 class="text-xl font-semibold text-gray-700">📋 Danh sách Hóa Đơn</h4>

    <!-- Form lọc -->
    <div class="bg-white shadow-lg rounded-lg p-5 mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
      <input v-model="searchQuery" type="text" placeholder="🔍 Tìm kiếm hóa đơn..." class="input-field" />
      <input v-model="minAmount" type="number" placeholder="💰 Giá tối thiểu" class="input-field" />
      <input v-model="maxAmount" type="number" placeholder="💰 Giá tối đa" class="input-field" />
      
      <select v-model="selectedOrderType" class="input-field">
        <option value="">📦 Tất cả loại đơn</option>
        <option value="Online">Online</option>
        <option value="Tại cửa hàng">Tại cửa hàng</option>
      </select>

      <input v-model="startDate" type="date" class="input-field" />
      <input v-model="endDate" type="date" class="input-field" />

      <button @click="applyFilters" class="col-span-full sm:col-span-2 md:col-span-3 lg:col-span-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition">🔍 Lọc</button>
    </div>

    <!-- Bảng dữ liệu -->
    <div class="overflow-x-auto mt-6 bg-white shadow-lg rounded-lg p-4">
      <table class="w-full min-w-max table-auto border-collapse">
        <thead>
          <tr class="bg-gray-100 text-gray-700 text-sm font-semibold">
            <th class="th-cell">ID Khách hàng</th>
            <th class="th-cell">ID Nhân viên</th>
            <th class="th-cell">Mã</th>
            <th class="th-cell">Tên Sản phẩm</th>
            <th class="th-cell">Loại Đơn</th>
            <th class="th-cell">Tổng tiền</th>
            <th class="th-cell">Giảm giá</th>
            <th class="th-cell">Khách hàng</th>
            <th class="th-cell">SDT</th>
            <th class="th-cell">Ngày Tạo</th>
            <th class="th-cell">Trạng thái</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="bill in bills" :key="bill.id" class="text-gray-700 border-b hover:bg-gray-50">
            <td class="td-cell">{{ bill.customerId }}</td>
            <td class="td-cell">{{ bill.staffId }}</td>
            <td class="td-cell">{{ bill.code }}</td>
            <td class="td-cell">{{ bill.productName }}</td>
            <td class="td-cell">{{ bill.orderType }}</td>
            <td class="td-cell font-semibold text-blue-600">{{ bill.totalAmount }}</td>
            <td class="td-cell text-red-500">{{ bill.discountedTotal }}</td>
            <td class="td-cell">{{ bill.customerName }}</td>
            <td class="td-cell">{{ bill.customerPhone }}</td>
            <td class="td-cell">{{ bill.createdDate }}</td>
            <td class="td-cell font-medium" :class="getStatusClass(bill.status)">{{ bill.status }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

const message = ref("");

onMounted(async () => {
  try {
    message.value = (await axios.get("http://localhost:8080/api/hello")).data;
  } catch (error) {
    console.error("Lỗi:", error);
  }
});
const bills = ref([
  {
    customerId: "KH001",
    staffId: "NV001",
    code: "HD001",
    productName: "iPhone 14",
    orderType: "Online",
    totalAmount: "15,000,000đ",
    discountedTotal: "14,500,000đ",
    customerName: "Nguyễn Văn A",
    customerPhone: "0123456789",
    createdDate: "2024-02-20",
    status: "Hoàn thành",
  },
  {
    customerId: "KH002",
    staffId: "NV002",
    code: "HD002",
    productName: "Samsung Galaxy S23",
    orderType: "Tại cửa hàng",
    totalAmount: "20,000,000đ",
    discountedTotal: "19,800,000đ",
    customerName: "Trần Thị B",
    customerPhone: "0987654321",
    createdDate: "2024-02-21",
    status: "Đang xử lý",
  },
  {
    customerId: "KH003",
    staffId: "NV003",
    code: "HD003",
    productName: "MacBook Pro 14",
    orderType: "Online",
    totalAmount: "35,000,000đ",
    discountedTotal: "34,500,000đ",
    customerName: "Phạm Văn C",
    customerPhone: "0912345678",
    createdDate: "2024-02-19",
    status: "Hoàn thành",
  },
  {
    customerId: "KH004",
    staffId: "NV004",
    code: "HD004",
    productName: "iPad Air 5",
    orderType: "Online",
    totalAmount: "16,000,000đ",
    discountedTotal: "15,500,000đ",
    customerName: "Lê Thị D",
    customerPhone: "0945678901",
    createdDate: "2024-02-18",
    status: "Đang xử lý",
  },
  {
    customerId: "KH005",
    staffId: "NV005",
    code: "HD005",
    productName: "Apple Watch Series 9",
    orderType: "Tại cửa hàng",
    totalAmount: "10,000,000đ",
    discountedTotal: "9,800,000đ",
    customerName: "Ngô Minh E",
    customerPhone: "0981234567",
    createdDate: "2024-02-22",
    status: "Hoàn thành",
  },
  {
    customerId: "KH006",
    staffId: "NV006",
    code: "HD006",
    productName: "AirPods Pro 2",
    orderType: "Online",
    totalAmount: "6,000,000đ",
    discountedTotal: "5,800,000đ",
    customerName: "Đặng Thị F",
    customerPhone: "0977654321",
    createdDate: "2024-02-23",
    status: "Đang xử lý",
  },
  {
    customerId: "KH007",
    staffId: "NV007",
    code: "HD007",
    productName: "Samsung Galaxy Z Fold 5",
    orderType: "Tại cửa hàng",
    totalAmount: "45,000,000đ",
    discountedTotal: "44,000,000đ",
    customerName: "Bùi Văn G",
    customerPhone: "0934567890",
    createdDate: "2024-02-25",
    status: "Chờ xác nhận",
  },
  {
    customerId: "KH008",
    staffId: "NV008",
    code: "HD008",
    productName: "Xiaomi 13 Pro",
    orderType: "Online",
    totalAmount: "18,000,000đ",
    discountedTotal: "17,500,000đ",
    customerName: "Trương Thị H",
    customerPhone: "0901234567",
    createdDate: "2024-02-26",
    status: "Hoàn thành",
  },
  {
    customerId: "KH009",
    staffId: "NV009",
    code: "HD009",
    productName: "Sony WH-1000XM5",
    orderType: "Online",
    totalAmount: "8,000,000đ",
    discountedTotal: "7,800,000đ",
    customerName: "Võ Minh I",
    customerPhone: "0898765432",
    createdDate: "2024-02-27",
    status: "Đang xử lý",
  },
  {
    customerId: "KH010",
    staffId: "NV010",
    code: "HD010",
    productName: "LG OLED C2 55-inch",
    orderType: "Tại cửa hàng",
    totalAmount: "30,000,000đ",
    discountedTotal: "29,500,000đ",
    customerName: "Nguyễn Hữu K",
    customerPhone: "0888888888",
    createdDate: "2024-02-28",
    status: "Hoàn thành",
  },
]);

const getStatusClass = (status) => {
  switch (status) {
    case "Hoàn thành":
      return "text-green-600";
    case "Đang xử lý":
      return "text-yellow-600";
    default:
      return "text-gray-600";
  }
};
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
