<template>
  <div class="mt-8">
    <h4 class="text-gray-600">Lịch sử Hóa Đơn</h4>

    <!-- Bộ lọc -->
    <div class="flex space-x-4 my-4">
      <select v-model="selectedBillId" class="border p-2 rounded-md">
        <option value="">Tất cả hóa đơn</option>
        <option v-for="bill in uniqueBillIds" :key="bill" :value="bill">{{ bill }}</option>
      </select>

      <select v-model="selectedStaffId" class="border p-2 rounded-md">
        <option value="">Tất cả nhân viên</option>
        <option v-for="staff in uniqueStaffIds" :key="staff" :value="staff">{{ staff }}</option>
      </select>
    </div>

    <!-- Bảng dữ liệu -->
    <div class="mt-4">
      <table class="w-full bg-white rounded-md shadow-md">
        <thead>
          <tr class="bg-gray-200 text-gray-700">
            <th class="px-4 py-2">ID</th>
            <th class="px-4 py-2">ID Hóa đơn</th>
            <th class="px-4 py-2">ID Nhân viên</th>
            <th class="px-4 py-2">Mã</th>
            <th class="px-4 py-2">Hành động</th>
            <th class="px-4 py-2">Thời gian ghi lại</th>
          </tr>
        </thead>
        <tbody>
          <tr class="border-t text-center" v-for="(history, index) in filteredBillHistory" :key="index">
            <td class="px-4 py-2">{{ history.id }}</td>
            <td class="px-4 py-2">{{ history.billId }}</td>
            <td class="px-4 py-2">{{ history.staffId }}</td>
            <td class="px-4 py-2">{{ history.code }}</td>
            <td class="px-4 py-2">{{ history.action }}</td>
            <td class="px-4 py-2">{{ history.timestamp }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";

const billHistory = ref([
  { id: 1, billId: "HD001", staffId: "NV001", code: "ORD001", action: "Tạo hóa đơn", timestamp: "2024-02-20 10:30:00" },
  { id: 2, billId: "HD001", staffId: "NV001", code: "ORD001", action: "Cập nhật trạng thái: Đang xử lý", timestamp: "2024-02-21 15:45:00" },
  { id: 3, billId: "HD002", staffId: "NV002", code: "ORD002", action: "Tạo hóa đơn", timestamp: "2024-02-22 09:20:00" },
  { id: 4, billId: "HD002", staffId: "NV002", code: "ORD002", action: "Hoàn thành thanh toán", timestamp: "2024-02-23 18:00:00" },
  { id: 5, billId: "HD003", staffId: "NV003", code: "ORD003", action: "Tạo hóa đơn", timestamp: "2024-02-24 14:00:00" },
  { id: 6, billId: "HD003", staffId: "NV003", code: "ORD003", action: "Xác nhận đơn hàng", timestamp: "2024-02-25 09:30:00" },
  { id: 7, billId: "HD004", staffId: "NV004", code: "ORD004", action: "Tạo hóa đơn", timestamp: "2024-02-26 11:15:00" },
  { id: 8, billId: "HD004", staffId: "NV004", code: "ORD004", action: "Cập nhật trạng thái: Hoàn thành", timestamp: "2024-02-27 17:45:00" },
  { id: 9, billId: "HD005", staffId: "NV005", code: "ORD005", action: "Tạo hóa đơn", timestamp: "2024-02-28 08:50:00" },
  { id: 10, billId: "HD005", staffId: "NV005", code: "ORD005", action: "Cập nhật trạng thái: Đang xử lý", timestamp: "2024-02-29 10:05:00" },
  { id: 11, billId: "HD006", staffId: "NV001", code: "ORD006", action: "Tạo hóa đơn", timestamp: "2024-03-01 12:10:00" },
  { id: 12, billId: "HD006", staffId: "NV001", code: "ORD006", action: "Thanh toán thành công", timestamp: "2024-03-02 14:25:00" },
  { id: 13, billId: "HD007", staffId: "NV002", code: "ORD007", action: "Tạo hóa đơn", timestamp: "2024-03-03 16:40:00" },
  { id: 14, billId: "HD007", staffId: "NV002", code: "ORD007", action: "Cập nhật trạng thái: Hoàn thành", timestamp: "2024-03-04 19:00:00" },
  { id: 15, billId: "HD008", staffId: "NV003", code: "ORD008", action: "Tạo hóa đơn", timestamp: "2024-03-05 20:30:00" },
  { id: 16, billId: "HD008", staffId: "NV003", code: "ORD008", action: "Đơn hàng bị hủy", timestamp: "2024-03-06 22:10:00" },
  { id: 17, billId: "HD009", staffId: "NV004", code: "ORD009", action: "Tạo hóa đơn", timestamp: "2024-03-07 09:50:00" },
]);

const selectedBillId = ref("");
const selectedStaffId = ref("");

const uniqueBillIds = computed(() => {
  return [...new Set(billHistory.value.map(history => history.billId))];
});

const uniqueStaffIds = computed(() => {
  return [...new Set(billHistory.value.map(history => history.staffId))];
});

const filteredBillHistory = computed(() => {
  return billHistory.value.filter(history => {
    return (
      (selectedBillId.value === "" || history.billId === selectedBillId.value) &&
      (selectedStaffId.value === "" || history.staffId === selectedStaffId.value)
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
