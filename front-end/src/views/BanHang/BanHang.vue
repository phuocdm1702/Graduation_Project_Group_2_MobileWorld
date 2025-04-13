<template>
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
  <ToastNotification ref="toast" />

  <div class="min-h-screen bg-gray-100 p-4 flex flex-col">
    <div class="flex-1">
      <div class="flex justify-between items-center mb-4">
        <h1 class="text-2xl font-bold text-orange-500">Bán hàng tại quầy</h1>
      </div>

      <div class="bg-white rounded-lg shadow mb-4 p-4">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-orange-500">Hóa đơn chờ</h2>
          <button
            @click="createNewPendingInvoice"
            class="p-1 bg-orange-500 text-white rounded hover:bg-orange-600"
          >
            <i class="fas fa-plus"></i>
          </button>
        </div>
        <div class="flex space-x-2 overflow-x-auto pb-2">
          <div
            v-for="invoice in pendingInvoices"
            :key="invoice.id"
            @click="loadPendingInvoice(invoice)"
            :class="{'bg-orange-100 border-orange-400': activeInvoiceId === invoice.id, 'border-gray-300': activeInvoiceId !== invoice.id}"
            class="p-2 border rounded cursor-pointer hover:bg-gray-100 min-w-[150px] flex-shrink-0"
          >
            <div class="flex justify-between items-center">
              <span class="font-medium">{{ invoice.code }}</span>
              <span class="text-xs bg-gray-200 px-2 py-1 rounded">{{ invoice.status }}</span>
            </div>
            <div class="text-xs text-gray-500 mt-1">
              {{ invoice.items.length }} sản phẩm
            </div>
          </div>
        </div>
      </div>

      <div class="bg-white p-4 rounded-lg shadow mb-4">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-orange-500">Giỏ hàng</h2>
          <div class="flex space-x-2">
            <button @click="scanQR" class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">Quét QR</button>
            <button @click="openProductModal" class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">Thêm sản phẩm</button>
          </div>
        </div>

        <DynamicTable
          :data="cartItems"
          :columns="cartColumns"
          :getNestedValue="getNestedValue"
          @editItem="editItem"
          @toggleStatus="toggleStatus"
        >
          <template #actionsSlot="{ item }">
            <div class="flex items-center space-x-2">
              <button @click="removeItem(item)" class="bg-red-500 text-white px-3 py-1 rounded-md hover:bg-red-600 transition">
                <i class="fa-solid fa-trash"></i>
              </button>
            </div>
          </template>
        </DynamicTable>

        <div class="mt-2 text-right">
          <p class="text-lg font-semibold">Tổng tiền: {{ totalPrice.toLocaleString() }} đ</p>
        </div>
      </div>

      <!-- Modal chọn sản phẩm -->
      <div v-if="showProductModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg shadow-lg w-full max-w-2xl p-6">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-lg font-semibold text-orange-500">Chọn sản phẩm</h2>
            <button @click="showProductModal = false" class="text-gray-500 hover:text-gray-700">
              <i class="fas fa-times"></i>
            </button>
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700">Tìm kiếm sản phẩm</label>
            <input
              v-model="productSearchQuery"
              @input="searchProducts"
              type="text"
              class="mt-1 p-2 w-full border rounded"
              placeholder="Nhập tên, mã hoặc IMEI sản phẩm"
            />
          </div>
          <div class="max-h-96 overflow-y-auto">
            <table class="w-full">
              <thead>
              <tr class="bg-gray-100">
                <th class="p-2 text-left">Tên sản phẩm</th>
                <th class="p-2 text-left">Mã</th>
                <th class="p-2 text-left">IMEI</th>
                <th class="p-2 text-left">Giá</th>
                <th class="p-2"></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="product in filteredProducts" :key="product.id" class="border-b">
                <td class="p-2">{{ product.tenSanPham }}</td>
                <td class="p-2">{{ product.ma }}</td>
                <td class="p-2">{{ product.imel }}</td>
                <td class="p-2">{{ product.giaBan.toLocaleString() }} đ</td>
                <td class="p-2 text-right">
                  <button
                    @click="addProduct(product)"
                    class="px-3 py-1 bg-orange-500 text-white rounded hover:bg-orange-600"
                  >
                    Chọn
                  </button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div class="bg-white p-4 rounded-lg shadow mb-4">
        <h2 class="text-lg font-semibold text-orange-500 mb-2">Thông tin khách hàng</h2>
        <div class="flex space-x-4 mb-4">
          <div class="flex-1">
            <label class="block text-sm font-medium text-gray-700">Tìm kiếm khách hàng</label>
            <input
              v-model="searchCustomer"
              type="text"
              class="mt-1 p-2 w-full border rounded"
              placeholder="Nhập tên hoặc số điện thoại"
              @input="searchCustomers"
            />
          </div>
          <div class="flex items-end">
            <button @click="openCustomerModal" class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">Thêm mới khách hàng</button>
          </div>
        </div>
        <div v-if="selectedCustomer" class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">Tên khách hàng</label>
            <input v-model="customer.name" type="text" class="mt-1 p-2 w-full border rounded" placeholder="Nguyễn Oanh" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
            <input v-model="customer.phone" type="text" class="mt-1 p-2 w-full border rounded" placeholder="0985357224" />
          </div>
        </div>
      </div>

      <div class="bg-white p-4 rounded-lg shadow">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-orange-500">Thông tin đơn hàng</h2>
          <label class="flex items-center">
            <input type="checkbox" v-model="payOnDelivery" class="mr-2" />
            <span>Bán giao hàng</span>
          </label>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <h3 class="text-md font-medium text-orange-500 mb-2">Thông tin người nhận</h3>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Tên người nhận</label>
                <input v-model="customer.name" type="text" class="mt-1 p-2 w-full border rounded" placeholder="Nguyễn Oanh" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
                <input v-model="customer.phone" type="text" class="mt-1 p-2 w-full border rounded" placeholder="0985357224" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Thành phố</label>
                <input v-model="receiver.city" type="text" class="mt-1 p-2 w-full border rounded"  />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Quận, Huyện</label>
                <input v-model="receiver.district" type="text" class="mt-1 p-2 w-full border rounded"  />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Phường, xã</label>
                <input v-model="receiver.ward" type="text" class="mt-1 p-2 w-full border rounded"  />
              </div>
            </div>
            <div class="mt-4">
              <label class="block text-sm font-medium text-gray-700">Địa chỉ cụ thể</label>
              <input v-model="receiver.address" type="text" class="mt-1 p-2 w-full border rounded" placeholder="Xóm Bình Yên" />
            </div>
            <div class="mt-4">
              <label class="block text-sm font-medium text-gray-700">Ghi chú</label>
              <textarea v-model="orderNotes" class="mt-1 p-2 w-full border rounded" rows="3"></textarea>
            </div>
          </div>

          <div>
            <div class="border-t border-gray-300 mb-4"></div>
            <div class="mb-4">
              <h3 class="text-md font-medium text-orange-500">Mã giảm giá</h3>
              <div class="flex space-x-4">
                <input v-model="discountCode" type="text" class="p-2 w-full border rounded" placeholder="Mừng Quốc Khánh 2/9" />
                <button @click="applyDiscount" class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">Áp dụng</button>
              </div>
              <div class="mt-2 text-right">
                <p>Tổng tiền hàng: {{ totalPrice.toLocaleString() }} đ</p>
                <p>Giảm giá: -{{ discount.toLocaleString() }} đ</p>
                <p class="text-lg font-semibold">Tổng tiền cần thanh toán: {{ (totalPrice - discount).toLocaleString() }} đ</p>
              </div>
            </div>
            <div class="mb-4">
              <h3 class="text-md font-medium text-orange-500">Phương thức thanh toán</h3>
              <div class="flex flex-wrap gap-2">
                <button
                  @click="selectPayment('transfer')"
                  :class="{'bg-blue-600': paymentMethod === 'transfer', 'bg-blue-500': paymentMethod !== 'transfer'}"
                  class="px-4 py-2 text-white rounded hover:bg-blue-600"
                >
                  Chuyển khoản
                </button>
                <button
                  @click="selectPayment('cash')"
                  :class="{'bg-gray-600': paymentMethod === 'cash', 'bg-gray-500': paymentMethod !== 'cash'}"
                  class="px-4 py-2 text-white rounded hover:bg-gray-600"
                >
                  Tiền mặt
                </button>
                <button
                  @click="selectPayment('both')"
                  :class="{'bg-purple-600': paymentMethod === 'both', 'bg-purple-500': paymentMethod !== 'both'}"
                  class="px-4 py-2 text-white rounded hover:bg-purple-600"
                >
                  Cả hai
                </button>
              </div>
              <!-- Thêm ô nhập tiền nếu chọn "Cả hai" -->
              <div v-if="paymentMethod === 'both'" class="mt-4 grid grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700">Tiền chuyển khoản</label>
                  <input
                    v-model="tienChuyenKhoan"
                    type="number"
                    class="mt-1 p-2 w-full border rounded"
                    placeholder="Nhập số tiền chuyển khoản"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700">Tiền mặt</label>
                  <input
                    v-model="tienMat"
                    type="number"
                    class="mt-1 p-2 w-full border rounded"
                    placeholder="Nhập số tiền mặt"
                  />
                </div>
              </div>
              <div class="mt-2">
                <label class="flex items-center">
                  <input type="checkbox" v-model="payOnDelivery" class="mr-2" />
                  <span>Thanh toán khi nhận hàng</span>
                </label>
              </div>
            </div>
            <div class="text-right">
              <button @click="createOrder" class="px-6 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">Thanh toán</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <FormModal
    :show="isCustomerModalOpen"
    entity-name="khách hàng"
    :entity-data="customer"
    @submit="(data) => { addNewCustomer(data); isCustomerModalOpen = false; }"
    @close="isCustomerModalOpen = false"
  >
    <template #default="{ entityData }">
      <div class="grid grid-cols-1 gap-4">
        <!-- Tên khách hàng -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Tên khách hàng</label>
          <input
            v-model="entityData.name"
            type="text"
            class="mt-1 p-2 w-full border rounded"
            placeholder="Nhập tên khách hàng"
            required
          />
        </div>
        <!-- Số điện thoại -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
          <input
            v-model="entityData.phone"
            type="text"
            class="mt-1 p-2 w-full border rounded"
            placeholder="Nhập số điện thoại"
            required
            pattern="\d{10}"
            title="Số điện thoại phải có đúng 10 chữ số"
          />
        </div>
        <!-- Tỉnh/Thành phố -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Tỉnh/Thành phố</label>
          <select
            v-model="entityData.city"
            class="mt-1 p-2 w-full border rounded"
            required
            @change="handleProvinceChange(entityData)"
          >
            <option value="" disabled>Chọn tỉnh/thành phố</option>
            <option v-for="province in provinces" :key="province.code" :value="province.name">
              {{ province.name }}
            </option>
          </select>
        </div>
        <!-- Quận/Huyện -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Quận/Huyện</label>
          <select
            v-model="entityData.district"
            class="mt-1 p-2 w-full border rounded"
            required
            @change="handleDistrictChange(entityData)"
            :disabled="!entityData.city"
          >
            <option value="" disabled>Chọn quận/huyện</option>
            <option v-for="district in districts" :key="district.code" :value="district.name">
              {{ district.name }}
            </option>
          </select>
        </div>
        <!-- Phường/Xã -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Phường/Xã</label>
          <select
            v-model="entityData.ward"
            class="mt-1 p-2 w-full border rounded"
            required
            :disabled="!entityData.district"
          >
            <option value="" disabled>Chọn phường/xã</option>
            <option v-for="ward in wards" :key="ward.code" :value="ward.name">
              {{ ward.name }}
            </option>
          </select>
        </div>
        <!-- Địa chỉ cụ thể -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Địa chỉ cụ thể</label>
          <input
            v-model="entityData.address"
            type="text"
            class="mt-1 p-2 w-full border rounded"
            placeholder="Nhập địa chỉ cụ thể"
            required
          />
        </div>
      </div>
    </template>
  </FormModal>
</template>

<script setup>
import {ref, computed, onMounted} from "vue";
import { useRoute } from "vue-router";
import DynamicTable from "@/components/DynamicTable.vue";
import ToastNotification from "@/components/ToastNotification.vue";
import BreadcrumbWrapper from "@/components/BreadcrumbWrapper.vue";
import { useBanHang } from "@/views/BanHang/BanHang.js"; // Sửa đường dẫn import
import FormModal from "@/components/FormModal.vue";
import axios from "axios";

const route = useRoute();
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Bán Hàng Tại Quầy"];
});

const provinces = ref([]);
const districts = ref([]);
const wards = ref([]);

// Khởi tạo toast ref
const toast = ref(null);

// Xử lý thay đổi tỉnh/thành phố
const handleProvinceChange = (entityData) => {
  const province = provinces.value.find((prov) => prov.name === entityData.city);
  districts.value = province ? province.districts : [];
  entityData.district = ''; // Đặt lại quận
  entityData.ward = ''; // Đặt lại phường
  wards.value = []; // Đặt lại danh sách phường
};

// Xử lý thay đổi quận/huyện
const handleDistrictChange = (entityData) => {
  const district = districts.value.find((dist) => dist.name === entityData.district);
  wards.value = district ? district.wards : [];
  entityData.ward = ''; // Đặt lại phường
};

// Tải dữ liệu địa chỉ khi component được mounted
onMounted(async () => {
  try {
    const response = await axios.get('https://provinces.open-api.vn/api/?depth=3', {
      withCredentials: false // Thêm dòng này để tránh lỗi CORS
    });
    provinces.value = response.data;
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu địa chỉ:', error);
    if (toast.value) {
      toast.value.kshowToast('error', 'Không thể tải danh sách tỉnh/thành phố: ' + (error.response?.data?.error || error.message));
    }
  }
})

const isCustomerModalOpen = ref(false);
const openCustomerModal = () => {
  isCustomerModalOpen.value = true;
};
const {
  cartItems,
  cartColumns,
  searchCustomer,
  selectedCustomer,
  customer,
  receiver,
  discountCode,
  discount,
  orderNotes,
  payOnDelivery,
  paymentMethod,
  activeInvoiceId,
  pendingInvoices,
  showProductModal,
  products,
  filteredProducts,
  productSearchQuery,
  totalPrice,
  tienChuyenKhoan,
  tienMat,
  fetchPendingInvoices,
  getNestedValue,
  editItem,
  toggleStatus,
  removeItem,
  createNewPendingInvoice,
  loadPendingInvoice,
  scanQR,
  openProductModal,
  searchProducts,
  addProduct,
  searchCustomers,
  addNewCustomer,
  selectPayment,
  createOrder,
} = useBanHang(toast);

// Hàm applyDiscount (logic giả lập, bạn có thể thay đổi theo yêu cầu)
const applyDiscount = () => {
  if (discountCode.value === "MungQuocKhanh") {
    discount.value = 500000; // Giả lập giảm giá 500,000
    toast.value.kshowToast("success", "Áp dụng mã giảm giá thành công!");
  } else {
    toast.value.kshowToast("error", "Mã giảm giá không hợp lệ!");
  }
};
</script>

<style scoped>
.cursor-pointer:hover {
  background-color: #f3f4f6;
}

.bg-orange-100 {
  background-color: #ffedd5;
  border-color: #fdba74;
}

.overflow-x-auto {
  scrollbar-width: thin;
  scrollbar-color: #f3f4f6 #e5e7eb;
}

.overflow-x-auto::-webkit-scrollbar {
  height: 8px;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
  background-color: #f3f4f6;
  border-radius: 4px;
}

.overflow-x-auto::-webkit-scrollbar-track {
  background-color: #e5e7eb;
}
</style>