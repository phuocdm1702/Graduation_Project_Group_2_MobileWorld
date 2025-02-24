<template>
  <div class="p-6">
    <!-- Thống kê -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
      <div v-for="(stat, index) in statistics" :key="index" class="p-6 rounded-xl text-white" :class="stat.bgColor">
        <h2 class="text-xl font-semibold">{{ stat.title }}</h2>
        <p>Doanh thu đơn hàng: {{ stat.revenue }}</p>
        <p>Sản phẩm đã bán: {{ stat.sold }}</p>
        <p>Tổng số đơn hàng: {{ stat.orders }}</p>
      </div>
    </div>
    
    <!-- Bảng sản phẩm bán chạy -->
    <div class="bg-white p-4 rounded-xl shadow-md">
      <h2 class="text-lg font-semibold mb-4">Top Sản Phẩm Bán Chạy Tháng Này</h2>
      <table class="w-full border-collapse border border-gray-200">
        <thead class="bg-gray-100">
          <tr>
            <th class="border p-2">STT</th>
            <th class="border p-2">Ảnh</th>
            <th class="border p-2">Tên Sản Phẩm</th>
            <th class="border p-2">Số Lượng</th>
            <th class="border p-2">Giá Tiền</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(product, index) in products" :key="index" class="text-center">
            <td class="border p-2">{{ index + 1 }}</td>
            <td class="border p-2"><img :src="product.image" class="h-12 w-12 rounded" /></td>
            <td class="border p-2">{{ product.name }}</td>
            <td class="border p-2">{{ product.sold }}</td>
            <td class="border p-2">{{ product.price }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      statistics: [
        { title: "Hôm nay", revenue: "2.800.000 VND", sold: 15, orders: 5, bgColor: "bg-blue-500" },
        { title: "Tuần này", revenue: "59.892.500 VND", sold: 256, orders: 40, bgColor: "bg-purple-500" },
        { title: "Tháng này", revenue: "59.892.500 VND", sold: 256, orders: 40, bgColor: "bg-green-500" },
        { title: "Năm nay", revenue: "59.892.500 VND", sold: 256, orders: 40, bgColor: "bg-teal-600" },
      ],
      products: [
        { name: "Áo Phông Trẻ Em", sold: 145, price: "250.000 VND", image: "https://via.placeholder.com/50" },
        { name: "Áo Phông Luxury", sold: 52, price: "500.000 VND", image: "https://via.placeholder.com/50" },
        { name: "Áo Phông Luxury", sold: 19, price: "300.000 VND", image: "https://via.placeholder.com/50" }
      ]
    };
  }
};
</script>

<style>
body {
  background-color: #f8f9fa;
}
</style>
