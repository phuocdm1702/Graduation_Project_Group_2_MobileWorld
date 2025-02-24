<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Phiếu Giảm Giá" />

    <div class="mt-4">
      <h4 class="text-gray-600">Quản lý Phiếu Giảm Giá</h4>

      <div class="mt-4 flex">
        <div
          class="w-full max-w-4xl overflow-hidden bg-white border rounded-md shadow-md"
        >
          <form>
            <div
              class="flex items-center justify-between px-5 py-3 text-gray-700 border-b"
            >
              <h3 class="text-sm">Thêm Phiếu Giảm Giá</h3>
              <button>
                <svg
                  class="w-4 h-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>

            <div
              class="p-5 text-gray-700 bg-gray-200 border-b grid grid-cols-3 gap-4"
            >
              <div>
                <label class="text-xs">Mã</label>
                <input
                  type="text"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Tên Phiếu</label>
                <input
                  type="text"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Loại Phiếu</label>
                <div class="mt-2 flex items-center space-x-4">
                  <label class="flex items-center">
                    <input
                      type="radio"
                      name="voucherType"
                      value="Chung"
                      v-model="voucherType"
                      class="mr-2"
                    />
                    Chung
                  </label>
                  <label class="flex items-center">
                    <input
                      type="radio"
                      name="voucherType"
                      value="Cá nhân"
                      v-model="voucherType"
                      class="mr-2"
                    />
                    Cá nhân
                  </label>
                </div>
              </div>
              <div>
                <label class="text-xs">Giá trị</label>
                <input
                  type="text"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Số tiền giảm tối đa</label>
                <input
                  type="text"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Hóa đơn tối thiểu</label>
                <input
                  type="text"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Ngày bắt đầu</label>
                <input
                  type="date"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Ngày kết thúc</label>
                <input
                  type="date"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Trạng thái</label>
                <select class="w-full px-4 py-2 mt-2 border rounded-md">
                  <option>Hoạt động</option>
                  <option>Hết hạn</option>
                </select>
              </div>
            </div>

            <div class="px-5 py-3 flex justify-between">
              <button
                class="px-3 py-1 text-sm text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300"
              >
                Hủy
              </button>
              <button
                class="px-3 py-1 text-sm text-white bg-indigo-600 rounded-md hover:bg-indigo-500"
              >
                Lưu
              </button>
            </div>
          </form>
        </div>

        <div
          v-if="voucherType === 'Cá nhân'"
          class="ml-4 w-1/3 bg-white border rounded-md shadow-md p-4"
        >
          <h4 class="text-gray-600 mb-2">Chọn Khách Hàng</h4>
          <table class="w-full bg-white rounded-md shadow-md">
            <thead>
              <tr class="bg-gray-200 text-gray-700">
                <th class="px-4 py-2">Chọn</th>
                <th class="px-4 py-2">Mã KH</th>
                <th class="px-4 py-2">Tên Khách Hàng</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="customer in customers"
                :key="customer.id"
                class="border-t text-center"
              >
                <td class="px-4 py-2">
                  <input
                    type="checkbox"
                    v-model="selectedCustomers"
                    :value="customer.id"
                  />
                </td>
                <td class="px-4 py-2">{{ customer.id }}</td>
                <td class="px-4 py-2">{{ customer.name }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div class="mt-8">
      <h4 class="text-gray-600">Danh sách Phiếu Giảm Giá</h4>
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
            <tr class="bg-gray-200 text-gray-700">
              <th class="px-4 py-2">Mã</th>
              <th class="px-4 py-2">Tên Phiếu</th>
              <th class="px-4 py-2">Loại Phiếu</th>
              <th class="px-4 py-2">Giá trị</th>
              <th class="px-4 py-2">Số tiền giảm tối đa</th>
              <th class="px-4 py-2">Hóa đơn tối thiểu</th>
              <th class="px-4 py-2">Ngày bắt đầu</th>
              <th class="px-4 py-2">Ngày kết thúc</th>
              <th class="px-4 py-2">Trạng thái</th>
              <th class="px-4 py-2">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr
              class="border-t text-center"
              v-for="voucher in vouchers"
              :key="voucher.id"
            >
              <td class="px-4 py-2">{{ voucher.id }}</td>
              <td class="px-4 py-2">{{ voucher.name }}</td>
              <td class="px-4 py-2">{{ voucher.type }}</td>
              <td class="px-4 py-2">{{ voucher.value }}</td>
              <td class="px-4 py-2">{{ voucher.maxDiscount }}</td>
              <td class="px-4 py-2">{{ voucher.minOrder }}</td>
              <td class="px-4 py-2">{{ voucher.startDate }}</td>
              <td class="px-4 py-2">{{ voucher.endDate }}</td>
              <td class="px-4 py-2">{{ voucher.status }}</td>
              <td class="px-4 py-2">
                <button class="text-blue-600 hover:underline mr-2">Sửa</button>
                <button class="text-red-600 hover:underline">Xóa</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const vouchers = ref([
  {
    id: "VC001",
    name: "Giảm 10%",
    type: "Phần trăm",
    value: "10%",
    maxDiscount: "50,000đ",
    minOrder: "200,000đ",
    startDate: "2024-02-20",
    endDate: "2024-03-20",
    status: "Hoạt động",
  },
  {
    id: "VC002",
    name: "Giảm 50k",
    type: "Tiền mặt",
    value: "50,000đ",
    maxDiscount: "50,000đ",
    minOrder: "500,000đ",
    startDate: "2024-02-25",
    endDate: "2024-03-25",
    status: "Hết hạn",
  },
]);

const voucherType = ref("Chung");
const selectedCustomers = ref([]);
const customers = ref([
  { id: "KH001", name: "Nguyễn Văn A", email: "a@example.com" },
  { id: "KH002", name: "Trần Thị B", email: "b@example.com" },
  { id: "KH003", name: "Lê Văn C", email: "c@example.com" },
]);
</script>
