<template>
  <div class="mt-8">
    <h4 class="text-gray-600">Hóa Đơn Chi Tiết</h4>

    <!-- Bộ lọc -->
    <div class="flex space-x-4 my-4">
      <select v-model="selectedStatus" class="border p-2 rounded-md">
        <option value="">Tất cả trạng thái</option>
        <option value="Hoàn thành">Hoàn thành</option>
        <option value="Đang xử lý">Đang xử lý</option>
        <option value="Đã thanh toán">Đã thanh toán</option>
        <option value="Chờ xác nhận">Chờ xác nhận</option>
      </select>

      <select v-model="selectedBillId" class="border p-2 rounded-md">
        <option value="">Tất cả hóa đơn</option>
        <option v-for="bill in uniqueBillIds" :key="bill" :value="bill">{{ bill }}</option>
      </select>
    </div>

    <!-- Bảng dữ liệu -->
    <div class="mt-4">
      <table class="w-full bg-white rounded-md shadow-md">
        <thead>
          <tr class="bg-gray-200 text-gray-700">
            <th class="px-4 py-2">ID</th>
            <th class="px-4 py-2">ID Hóa đơn</th>
            <th class="px-4 py-2">ID Chi tiết Sản Phẩm</th>
            <th class="px-4 py-2">Mã</th>
            <th class="px-4 py-2">Tổng tiền</th>
            <th class="px-4 py-2">Trạng thái</th>
            <th class="px-4 py-2">Ghi chú</th>
          </tr>
        </thead>
        <tbody>
          <tr class="border-t text-center" v-for="(detail, index) in filteredInvoiceDetails" :key="index">
            <td class="px-4 py-2">{{ detail.id }}</td>
            <td class="px-4 py-2">{{ detail.billId }}</td>
            <td class="px-4 py-2">{{ detail.productDetailId }}</td>
            <td class="px-4 py-2">{{ detail.code }}</td>
            <td class="px-4 py-2">{{ detail.totalAmount }}</td>
            <td class="px-4 py-2">{{ detail.status }}</td>
            <td class="px-4 py-2">{{ detail.notes }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";

const invoiceDetails = ref([
  { id: 1, billId: "HD001", productDetailId: "CTSP001", code: "ORD001", totalAmount: "15,000,000đ", status: "Hoàn thành", notes: "Giao hàng nhanh" },
  { id: 2, billId: "HD001", productDetailId: "CTSP002", code: "ORD002", totalAmount: "8,500,000đ", status: "Đang xử lý", notes: "Chờ xác nhận" },
  { id: 3, billId: "HD002", productDetailId: "CTSP003", code: "ORD003", totalAmount: "20,000,000đ", status: "Đã thanh toán", notes: "Khách thanh toán bằng thẻ" },
  { id: 4, billId: "HD003", productDetailId: "CTSP004", code: "ORD004", totalAmount: "12,000,000đ", status: "Hoàn thành", notes: "Miễn phí vận chuyển" },
  { id: 5, billId: "HD004", productDetailId: "CTSP005", code: "ORD005", totalAmount: "10,500,000đ", status: "Chờ xác nhận", notes: "Khách yêu cầu kiểm tra hàng" },
  { id: 6, billId: "HD005", productDetailId: "CTSP006", code: "ORD006", totalAmount: "22,000,000đ", status: "Đang xử lý", notes: "Giao hàng trong 3 ngày" },
  { id: 7, billId: "HD006", productDetailId: "CTSP007", code: "ORD007", totalAmount: "5,000,000đ", status: "Hoàn thành", notes: "Thanh toán ví điện tử" },
  { id: 8, billId: "HD007", productDetailId: "CTSP008", code: "ORD008", totalAmount: "18,500,000đ", status: "Đã thanh toán", notes: "Đơn hàng giảm giá" },
  { id: 9, billId: "HD008", productDetailId: "CTSP009", code: "ORD009", totalAmount: "9,000,000đ", status: "Chờ xác nhận", notes: "Hỗ trợ trả góp" },
  { id: 10, billId: "HD009", productDetailId: "CTSP010", code: "ORD010", totalAmount: "7,800,000đ", status: "Đang xử lý", notes: "Giao hàng COD" },
  { id: 11, billId: "HD010", productDetailId: "CTSP011", code: "ORD011", totalAmount: "14,700,000đ", status: "Hoàn thành", notes: "Khách hài lòng" },
  { id: 12, billId: "HD011", productDetailId: "CTSP012", code: "ORD012", totalAmount: "26,000,000đ", status: "Đã thanh toán", notes: "Giao trong ngày" },
  { id: 13, billId: "HD012", productDetailId: "CTSP013", code: "ORD013", totalAmount: "32,500,000đ", status: "Đang xử lý", notes: "Khách yêu cầu bảo hành" },
]);

const selectedStatus = ref("");
const selectedBillId = ref("");

const uniqueBillIds = computed(() => {
  return [...new Set(invoiceDetails.value.map(detail => detail.billId))];
});

const filteredInvoiceDetails = computed(() => {
  return invoiceDetails.value.filter(detail => {
    return (
      (selectedStatus.value === "" || detail.status === selectedStatus.value) &&
      (selectedBillId.value === "" || detail.billId === selectedBillId.value)
    );
  });
});
</script>

<style>
.table-container {
  display: flex;
  justify-content: center;
  padding: 20px;
}
.table {
  width: 80%;
  border-collapse: collapse;
}
</style>
