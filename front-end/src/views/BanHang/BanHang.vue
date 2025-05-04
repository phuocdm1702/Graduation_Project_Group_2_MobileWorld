<template>
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems"/>
  <ToastNotification ref="toast"/>

  <div class="min-h-screen bg-gray-100 p-4 flex flex-col">
    <div class="flex-1">
      <!-- Hóa đơn chờ -->
      <div class="bg-white rounded-lg shadow mb-4 p-4">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-orange-500">Hóa đơn chờ</h2>
          <button @click="createNewPendingInvoice" class="p-1 bg-orange-500 text-white rounded hover:bg-orange-600">
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

      <!-- Giỏ hàng -->
      <div class="bg-white p-4 rounded-lg shadow mb-4">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-orange-500">Giỏ hàng</h2>
          <div class="flex space-x-2">
            <button @click="scanQR" class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">Quét QR
            </button>
            <button @click="openProductModal" class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">
              Thêm sản phẩm
            </button>
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
              <button @click="removeItem(item)"
                      class="bg-red-500 text-white px-3 py-1 rounded-md hover:bg-red-600 transition">
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
      <div v-if="showProductModal"
           class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg shadow-lg w-full max-w-screen-2xl p-6">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-lg font-semibold text-orange-500">Chọn sản phẩm</h2>
            <button @click="closeProductModal" class="text-gray-500 hover:text-gray-700">
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
              placeholder="Nhập tên hoặc mã sản phẩm"
            />
          </div>
          <div class="max-h-96 overflow-y-auto">
            <table class="w-full">
              <thead>
              <tr class="bg-gray-100">
                <th class="p-4 text-left">STT</th>
                <th class="p-4 text-left min-w-[200px]">Tên sản phẩm</th>
                <th class="p-4 text-left min-w-[150px]">Mã</th>
                <th class="p-4 text-left min-w-[150px]">Màu</th>
                <th class="p-4 text-left min-w-[120px]">Số lượng</th>
                <th class="p-4 text-left min-w-[150px]">Giá</th>
                <th class="p-4 text-right min-w-[120px]"></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(product, index) in filteredProducts" :key="product.id" class="border-b">
                <td class="p-4">{{ index + 1 }}</td>
                <td class="p-4">{{ product.tenSanPham }}</td>
                <td class="p-4">{{ product.ma }}</td>
                <td class="p-4">{{ product.mauSac || 'N/A' }}</td>
                <td class="p-4">{{ product.soLuong || 0 }}</td>
                <td class="p-4">{{ product.giaBan.toLocaleString() }} đ</td>
                <td class="p-4 text-right">
                  <button
                    @click="showIMEIList(product)"
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

      <!-- Modal chọn IMEI -->
      <div v-if="showIMEIModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg shadow-lg w-full max-w-screen-xl p-6">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-lg font-semibold text-orange-500">Chọn IMEI cho {{ selectedProduct.tenSanPham }}</h2>
            <button @click="closeIMEIModal" class="text-gray-500 hover:text-gray-700">
              <i class="fas fa-times"></i>
            </button>
          </div>
          <div class="max-h-96 overflow-y-auto">
            <table class="w-full">
              <thead>
              <tr class="bg-gray-100">
                <th class="p-2 text-left">IMEI</th>
                <th class="p-2"></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="imei in availableIMEIs" :key="imei" class="border-b">
                <td class="p-2">{{ imei }}</td>
                <td class="p-2 text-right">
                  <input
                    type="checkbox"
                    :value="imei"
                    v-model="selectedIMEIs"
                  />
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <!-- Hiển thị danh sách IMEI đã chọn -->
          <div v-if="selectedIMEIs.length > 0" class="mt-4">
            <h3 class="text-md font-semibold text-gray-700">IMEI đã chọn:</h3>
            <ul class="list-disc pl-5">
              <li v-for="imei in selectedIMEIs" :key="imei" class="text-gray-600">{{ imei }}</li>
            </ul>
          </div>
          <div class="mt-4 text-right">
            <button
              @click="addProductWithIMEIs"
              :disabled="selectedIMEIs.length === 0"
              class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
            >
              Thêm vào giỏ hàng
            </button>
          </div>
        </div>
      </div>

      <!-- Thông tin khách hàng -->
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
            <button @click="openCustomerModal" class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">
              Thêm mới khách hàng
            </button>
          </div>
        </div>
        <div v-if="selectedCustomer" class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">Tên khách hàng</label>
            <input v-model="customer.name" type="text" class="mt-1 p-2 w-full border rounded"
                   placeholder="Nguyễn Oanh"/>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
            <input v-model="customer.phone" type="text" class="mt-1 p-2 w-full border rounded"
                   placeholder="0985357224"/>
          </div>
        </div>
      </div>

      <!-- Thông tin đơn hàng -->
      <div class="bg-white p-4 rounded-lg shadow">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-orange-500">Thông tin đơn hàng</h2>
          <label class="flex items-center">
            <input type="checkbox" v-model="payOnDelivery" class="mr-2"/>
            <span>Bán giao hàng</span>
          </label>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <h3 class="text-md font-medium text-orange-500 mb-2">Thông tin người nhận</h3>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Tên người nhận</label>
                <input
                  v-model="receiver.name"
                  type="text"
                  class="mt-1 p-2 w-full border rounded"
                  placeholder="Nguyễn Oanh"
                  :disabled="!isReceiverEditable"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
                <input
                  v-model="receiver.phone"
                  type="text"
                  class="mt-1 p-2 w-full border rounded"
                  placeholder="0985357224"
                  :disabled="!isReceiverEditable"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Thành phố</label>
                <input
                  v-model="receiver.city"
                  type="text"
                  class="mt-1 p-2 w-full border rounded"
                  :disabled="!isReceiverEditable"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Quận, Huyện</label>
                <input
                  v-model="receiver.district"
                  type="text"
                  class="mt-1 p-2 w-full border rounded"
                  :disabled="!isReceiverEditable"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Phường, xã</label>
                <input
                  v-model="receiver.ward"
                  type="text"
                  class="mt-1 p-2 w-full border rounded"
                  :disabled="!isReceiverEditable"
                />
              </div>
            </div>
            <div class="mt-4">
              <label class="block text-sm font-medium text-gray-700">Địa chỉ cụ thể</label>
              <input
                v-model="receiver.address"
                type="text"
                class="mt-1 p-2 w-full border rounded"
                placeholder="Xóm Bình Yên"
                :disabled="!isReceiverEditable"
              />
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
                <input v-model="discountCode" type="text" class="p-2 w-full border rounded"
                       placeholder="Mừng Quốc Khánh 2/9"/>
                <button @click="applyDiscount" class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">Áp
                  dụng
                </button>
              </div>
              <div class="mt-2 text-right">
                <p>Tổng tiền hàng: {{ totalPrice.toLocaleString() }} đ</p>
                <p>Giảm giá: -{{ discount.toLocaleString() }} đ</p>
                <p class="text-lg font-semibold">Tổng tiền cần thanh toán: {{
                    (totalPrice - discount).toLocaleString()
                  }} đ</p>
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
                  <input type="checkbox" v-model="payOnDelivery" class="mr-2"/>
                  <span>Thanh toán khi nhận hàng</span>
                </label>
              </div>
            </div>
            <div class="text-right">
              <button @click="createOrder" class="px-6 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">Thanh
                toán
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- FormModal -->
  <FormModal
    :show="isCustomerModalOpen"
    entity-name="khách hàng"
    :entity-data="customer"
    @submit="(data) => { addNewCustomer(data); isCustomerModalOpen = false; }"
    @close="isCustomerModalOpen = false"
  >
    <template #default="{ entityData }">
      <div class="grid grid-cols-1 gap-4">
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
import {ref, computed, onMounted, watch} from "vue";
import {useRoute} from "vue-router";
import DynamicTable from "@/components/DynamicTable.vue";
import ToastNotification from "@/components/ToastNotification.vue";
import BreadcrumbWrapper from "@/components/BreadcrumbWrapper.vue";
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

// Toast reference
const toast = ref(null);

// Handle province and district changes for customer modal
const handleProvinceChange = (entityData) => {
  const province = provinces.value.find((prov) => prov.name === entityData.city);
  districts.value = province ? province.districts : [];
  entityData.district = '';
  entityData.ward = '';
  wards.value = [];
};

const handleDistrictChange = (entityData) => {
  const district = districts.value.find((dist) => dist.name === entityData.district);
  wards.value = district ? district.wards : [];
  entityData.ward = '';
};

// Fetch province data on mount
onMounted(async () => {
  try {
    const response = await axios.get('https://provinces.open-api.vn/api/?depth=3', {
      withCredentials: false
    });
    provinces.value = response.data;
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu địa chỉ:', error);
    if (toast.value) {
      toast.value.kshowToast('error', 'Không thể tải danh sách tỉnh/thành phố: ' + (error.response?.data?.error || error.message));
    }
  }
});

// Customer modal state
const isCustomerModalOpen = ref(false);
const openCustomerModal = () => {
  isCustomerModalOpen.value = true;
};

// BanHang logic
const cartItems = ref([]);
const cartColumns = ref([
  {key: "id", label: "STT"},
  {key: "name", label: "Sản phẩm"},
  {key: "price", label: "Đơn giá", formatter: (value) => `${value.toLocaleString()} đ`},
  {key: "imei", label: "IMEI", formatter: (value) => value || "N/A"},
  {key: "actions", label: "Xóa", cellSlot: "actionsSlot"},
]);
const searchCustomer = ref("");
const selectedCustomer = ref(null);
const customer = ref({name: "", phone: "", city: "", district: "", ward: "", address: ""});
const receiver = ref({
  name: "",
  phone: "",
  city: "",
  district: "",
  ward: "",
  address: ""
});
const discountCode = ref("");
const discount = ref(0);
const orderNotes = ref("");
const payOnDelivery = ref(false);
const paymentMethod = ref("");
const activeInvoiceId = ref(null);
const pendingInvoices = ref([]);
const showProductModal = ref(false);
const showIMEIModal = ref(false);
const products = ref([]);
const filteredProducts = ref([]);
const productSearchQuery = ref("");
const selectedProduct = ref(null);
const availableIMEIs = ref([]);
const selectedIMEIs = ref([]);
const gioHangId = ref(null);
const tienChuyenKhoan = ref(0);
const tienMat = ref(0);

// Computed property to determine if receiver fields should be editable
const isReceiverEditable = computed(() => {
  return payOnDelivery.value;
});

const totalPrice = computed(() => {
  return cartItems.value.reduce((total, item) => total + item.price, 0);
});

const calculateDiscount = () => {
  const total = totalPrice.value;
  let newDiscount = 0;

  if (total > 40000000) {
    newDiscount = 1500000;
    if (toast.value && discount.value !== newDiscount) {
      toast.value.kshowToast("success", "Đã áp dụng giảm giá 1.500.000 đ cho hóa đơn trên 40 triệu!");
    }
  } else if (total > 30000000) {
    newDiscount = 1000000;
    if (toast.value && discount.value !== newDiscount) {
      toast.value.kshowToast("success", "Đã áp dụng giảm giá 1.000.000 đ cho hóa đơn trên 30 triệu!");
    }
  } else if (total > 15000000) {
    newDiscount = 500000;
    if (toast.value && discount.value !== newDiscount) {
      toast.value.kshowToast("success", "Đã áp dụng giảm giá 500.000 đ cho hóa đơn trên 15 triệu!");
    }
  } else {
    newDiscount = 0;
    if (toast.value && discount.value !== 0) {
      toast.value.kshowToast("info", "Hóa đơn dưới 15 triệu, không áp dụng giảm giá.");
    }
  }

  discount.value = newDiscount;
};

watch(totalPrice, () => {
  calculateDiscount();
});

const fetchPendingInvoices = async () => {
  try {
    const response = await axios.get("http://localhost:8080/ban-hang/data");
    pendingInvoices.value = response.data.map((hd) => ({
      id: hd.id,
      code: hd.maHoaDon,
      status: hd.trangThai === 0 ? "Chờ" : "Đã thanh toán",
      items: hd.items.map((item) => ({
        id: item.id,
        name: item.tenSanPham,
        price: item.giaBan,
        imei: item.imei,
      })),
    }));

    const storedInvoiceId = localStorage.getItem("activeInvoiceId");
    if (storedInvoiceId) {
      activeInvoiceId.value = parseInt(storedInvoiceId);
      const selectedInvoice = pendingInvoices.value.find((i) => i.id === activeInvoiceId.value);
      if (selectedInvoice) {
        await loadPendingInvoice(selectedInvoice);
      } else {
        localStorage.removeItem("activeInvoiceId");
        activeInvoiceId.value = null;
      }
    }
  } catch (error) {
    console.error("Lỗi khi lấy danh sách hóa đơn:", error);
    if (toast.value) toast.value.kshowToast("error", "Không thể tải danh sách hóa đơn!");
  }
};

const fetchProducts = async () => {
  try {
    const response = await axios.get("http://localhost:8080/ban-hang/san-pham");
    products.value = await Promise.all(response.data.map(async (p) => {
      const imeiResponse = await axios.get(`http://localhost:8080/ban-hang/san-pham/${p.id}/imeis`);
      return {
        id: p.id,
        tenSanPham: p.tenSanPham,
        ma: p.ma,
        mauSac: p.mauSac || 'N/A',
        soLuong: imeiResponse.data.length,
        giaBan: p.giaBan,
      };
    }));
    filteredProducts.value = products.value;
  } catch (error) {
    console.error("Lỗi khi lấy danh sách sản phẩm:", error);
    if (toast.value) toast.value.kshowToast("error", "Không thể tải danh sách sản phẩm!");
  }
};

const fetchCartItems = async () => {
  const storedGioHangId = localStorage.getItem("gioHangId");
  if (storedGioHangId) {
    gioHangId.value = parseInt(storedGioHangId);
    try {
      const response = await axios.get(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`);
      cartItems.value = response.data.map((item) => ({
        id: item.id,
        name: item.tenSanPham,
        price: item.giaBan,
        imei: item.imei,
      }));
      calculateDiscount();
    } catch (error) {
      console.error("Lỗi khi lấy danh sách chi tiết giỏ hàng:", error);
      if (toast.value) toast.value.kshowToast("error", "Không thể tải giỏ hàng!");
    }
  }
};

fetchPendingInvoices();
fetchCartItems();

const getNestedValue = (item, key) => {
  return key === "imei" ? item.imei : (key.split(".").reduce((obj, k) => (obj && obj[k] !== undefined ? obj[k] : null), item) || "N/A");
};

const editItem = (item) => {
  if (toast.value) toast.value.kshowToast("info", `Chỉnh sửa sản phẩm: ${item.name}`);
};

const toggleStatus = (item) => {
  if (toast.value) toast.value.kshowToast("success", `Đã thay đổi trạng thái sản phẩm: ${item.name}`);
};

const removeItem = async (item) => {
  try {
    await axios.delete(`http://localhost:8080/ban-hang/gio-hang/chi-tiet/${item.id}`);
    cartItems.value = cartItems.value.filter((i) => i.id !== item.id);
    const invoice = pendingInvoices.value.find((i) => i.id === activeInvoiceId.value);
    if (invoice) {
      invoice.items = invoice.items.filter((i) => i.id !== item.id);
    }
    if (toast.value) toast.value.kshowToast("success", `Đã xóa sản phẩm: ${item.name}`);
    calculateDiscount();
  } catch (error) {
    console.error("Lỗi khi xóa sản phẩm:", error);
    if (toast.value) toast.value.kshowToast("error", "Không thể xóa sản phẩm!");
  }
};

const generateRandomCode = () => {
  const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  let randomCode = "";
  for (let i = 0; i < 5; i++) {
    const randomIndex = Math.floor(Math.random() * characters.length);
    randomCode += characters[randomIndex];
  }
  return `HD${randomCode}`;
};

const createNewPendingInvoice = async () => {
  const newInvoice = {
    maHoaDon: generateRandomCode(),
    trangThai: 0,
  };

  try {
    const gioHangResponse = await axios.post("http://localhost:8080/ban-hang/addGioHang", {
      ma: `GH_${newInvoice.maHoaDon}`,
      idKhachHang: 1,
    });
    gioHangId.value = gioHangResponse.data.id;
    localStorage.setItem("gioHangId", gioHangId.value);

    const response = await axios.post("http://localhost:8080/ban-hang/addHD", newInvoice);
    pendingInvoices.value.unshift({
      id: response.data.id,
      code: response.data.ma,
      status: response.data.trangThai === 0 ? "Chờ" : "Đã thanh toán",
      items: [],
    });
    await loadPendingInvoice(pendingInvoices.value[0]);
    if (toast.value) toast.value.kshowToast("success", `Đã tạo hóa đơn chờ mới: ${response.data.ma}`);
  } catch (error) {
    console.error("Lỗi khi tạo hóa đơn mới:", error);
    if (toast.value) toast.value.kshowToast("error", "Không thể tạo hóa đơn mới!");
  }
};

const loadPendingInvoice = async (invoice) => {
  activeInvoiceId.value = invoice.id;
  localStorage.setItem("activeInvoiceId", invoice.id);

  if (!gioHangId.value) {
    const gioHangResponse = await axios.post("http://localhost:8080/ban-hang/addGioHang", {
      ma: `GH_${invoice.code}`,
      idKhachHang: 1,
    });
    gioHangId.value = gioHangResponse.data.id;
    localStorage.setItem("gioHangId", gioHangId.value);
  }

  try {
    const response = await axios.get(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`);
    cartItems.value = response.data.map((item) => ({
      id: item.id,
      name: item.tenSanPham,
      price: item.giaBan,
      imei: item.imei,
    }));
    const invoiceToUpdate = pendingInvoices.value.find((i) => i.id === invoice.id);
    if (invoiceToUpdate) {
      invoiceToUpdate.items = [...cartItems.value];
    }
    calculateDiscount();
  } catch (error) {
    console.error("Lỗi khi tải sản phẩm của hóa đơn:", error);
    cartItems.value = [];
  }

  if (toast.value) toast.value.kshowToast("info", `Đã tải hóa đơn chờ: ${invoice.code}`);
};

const scanQR = () => {
  if (toast.value) toast.value.kshowToast("info", "Đang quét QR...");
};

const openProductModal = async () => {
  await fetchProducts();
  showProductModal.value = true;
  productSearchQuery.value = "";
};

const closeProductModal = () => {
  showProductModal.value = false;
};

const showIMEIList = async (product) => {
  selectedProduct.value = product;
  try {
    const response = await axios.get(`http://localhost:8080/ban-hang/san-pham/${product.id}/imeis`);
    availableIMEIs.value = response.data;
    selectedIMEIs.value = [];
    showIMEIModal.value = true;
  } catch (error) {
    console.error("Lỗi khi lấy danh sách IMEI:", error);
    if (toast.value) toast.value.kshowToast("error", "Không thể tải danh sách IMEI!");
  }
};

const closeIMEIModal = () => {
  showIMEIModal.value = false;
  selectedProduct.value = null;
  availableIMEIs.value = [];
  selectedIMEIs.value = [];
};

const searchProducts = () => {
  if (!productSearchQuery.value) {
    filteredProducts.value = products.value;
  } else {
    filteredProducts.value = products.value.filter((product) =>
      product.tenSanPham.toLowerCase().includes(productSearchQuery.value.toLowerCase()) ||
      product.ma.toLowerCase().includes(productSearchQuery.value.toLowerCase())
    );
  }
};

const addProductWithIMEIs = async () => {
  if (!activeInvoiceId.value) {
    if (toast.value) toast.value.kshowToast("error", "Vui lòng chọn hoặc tạo hóa đơn trước!");
    return;
  }

  if (selectedIMEIs.value.length === 0) {
    if (toast.value) toast.value.kshowToast("error", "Vui lòng chọn ít nhất một IMEI!");
    return;
  }

  try {
    if (!gioHangId.value) {
      const gioHangResponse = await axios.post("http://localhost:8080/ban-hang/addGioHang", {
        ma: generateRandomCode(),
        idKhachHang: 1,
      });
      gioHangId.value = gioHangResponse.data.id;
      localStorage.setItem("gioHangId", gioHangId.value);
    }

    for (const imei of selectedIMEIs.value) {
      const chiTietSanPhamResponse = await axios.get(`http://localhost:8080/ban-hang/san-pham?imei=${imei}`);
      const chiTietSanPham = chiTietSanPhamResponse.data.find((p) => p.imei === imei);
      if (!chiTietSanPham) throw new Error("Không tìm thấy sản phẩm với IMEI: " + imei);

      const response = await axios.post(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`, {
        idChiTietSanPham: chiTietSanPham.id,
        hoaDonId: activeInvoiceId.value,
      });

      const newItem = {
        id: response.data.id,
        name: response.data.tenSanPham,
        price: response.data.giaBan,
        imei: response.data.imei,
      };
      cartItems.value.push(newItem);
      const invoice = pendingInvoices.value.find((i) => i.id === activeInvoiceId.value);
      if (invoice) invoice.items.push(newItem);
    }

    if (toast.value) toast.value.kshowToast("success", `Đã thêm ${selectedIMEIs.value.length} sản phẩm: ${selectedProduct.value.tenSanPham}`);
    closeIMEIModal();
    closeProductModal();
    calculateDiscount();
  } catch (error) {
    console.error("Lỗi khi thêm sản phẩm:", error);
    if (toast.value) toast.value.kshowToast("error", "Không thể thêm sản phẩm: " + error.message);
  }
};

const searchCustomers = async () => {
  if (!searchCustomer.value.trim()) {
    selectedCustomer.value = null;
    customer.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
    receiver.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
    return;
  }

  try {
    const response = await axios.get(`http://localhost:8080/khach-hang/search?query=${encodeURIComponent(searchCustomer.value.trim())}`);
    if (response.data && response.data.length > 0) {
      const firstCustomer = response.data[0];
      selectedCustomer.value = true;
      customer.value = {
        name: firstCustomer.ten || "",
        phone: firstCustomer.idTaiKhoan?.soDienThoai || "",
        city: firstCustomer.idDiaChiKH?.thanhPho || "",
        district: firstCustomer.idDiaChiKH?.quan || "",
        ward: firstCustomer.idDiaChiKH?.phuong || "",
        address: firstCustomer.idDiaChiKH?.diaChiCuThe || "",
      };
      receiver.value = {
        name: customer.value.name,
        phone: customer.value.phone,
        city: customer.value.city || "Tỉnh Phú Thọ",
        district: customer.value.district || "Huyện Lâm Thao",
        ward: customer.value.ward || "Xã Xuân Lũng",
        address: customer.value.address || "",
      };
    } else {
      selectedCustomer.value = null;
      customer.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
      receiver.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
      if (toast.value) toast.value.kshowToast("info", "Không tìm thấy khách hàng phù hợp");
    }
  } catch (error) {
    console.error("Lỗi khi tìm kiếm khách hàng:", error);
    selectedCustomer.value = null;
    customer.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
    receiver.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
    if (toast.value) toast.value.kshowToast("error", "Không thể tìm kiếm khách hàng: " + (error.response?.data?.error || error.message));
  }
};

const addNewCustomer = async (newCustomer) => {
  const customerData = {
    tenKH: newCustomer.name?.trim(),
    soDienThoai: newCustomer.phone,
    thanhPho: newCustomer.city,
    quan: newCustomer.district,
    phuong: newCustomer.ward,
    diaChiCuThe: newCustomer.address,
  };

  if (!customerData.tenKH || !customerData.soDienThoai || !customerData.thanhPho ||
    !customerData.quan || !customerData.phuong || !customerData.diaChiCuThe) {
    console.error('Dữ liệu không đầy đủ:', customerData);
    if (toast.value) toast.value.kshowToast('error', 'Vui lòng điền đầy đủ thông tin khách hàng');
    return;
  }

  try {
    const response = await axios.post('http://localhost:8080/khach-hang/addBh', customerData, {
      headers: {'Content-Type': 'application/json'},
    });
    selectedCustomer.value = true;
    customer.value = {
      name: response.data.ten,
      phone: response.data.soDienThoai || response.data.idTaiKhoan?.soDienThoai || '',
      city: response.data.idDiaChiKH?.thanhPho,
      district: response.data.idDiaChiKH?.quan,
      ward: response.data.idDiaChiKH?.phuong,
      address: response.data.idDiaChiKH?.diaChiCuThe,
    };
    if (toast.value) toast.value.kshowToast("success", `Đã thêm thành công khách hàng: ${response.data.ten}`);
  } catch (error) {
    console.error('Lỗi khi thêm khách hàng:', error);
    let errorMessage = 'Không thể thêm khách hàng';
    if (error.response?.status === 400 && error.response?.data?.includes('constraint [UQ__')) {
      errorMessage = 'Số điện thoại đã tồn tại. Vui lòng sử dụng số khác.';
    } else {
      errorMessage = error.response?.data || error.message;
    }
    if (toast.value) toast.value.kshowToast('error', errorMessage);
    return;
  }
};

const applyDiscount = () => {
  if (discountCode.value === "MungQuocKhanh") {
    discount.value = 500000;
    if (toast.value) toast.value.kshowToast("success", "Áp dụng mã giảm giá thành công!");
  } else {
    if (toast.value) toast.value.kshowToast("error", "Mã giảm giá không hợp lệ!");
  }
};

const selectPayment = (method) => {
  paymentMethod.value = method;
  const finalAmount = totalPrice.value - discount.value;
  switch (method) {
    case "transfer":
      tienChuyenKhoan.value = finalAmount;
      tienMat.value = 0;
      break;
    case "cash":
      tienMat.value = finalAmount;
      tienChuyenKhoan.value = 0;
      break;
    case "both":
      tienChuyenKhoan.value = 0;
      tienMat.value = 0;
      break;
  }
  if (toast.value) toast.value.kshowToast("info", `Đã chọn phương thức thanh toán: ${method}`);
};

const createOrder = async () => {
  if (!paymentMethod.value && !payOnDelivery.value) {
    if (toast.value) toast.value.kshowToast("error", "Vui lòng chọn phương thức thanh toán hoặc thanh toán khi nhận hàng");
    return;
  }

  if (!activeInvoiceId.value) {
    if (toast.value) toast.value.kshowToast("error", "Vui lòng chọn hoặc tạo hóa đơn trước!");
    return;
  }

  if (payOnDelivery.value) {
    if (!receiver.value.name || !receiver.value.phone || !receiver.value.city ||
      !receiver.value.district || !receiver.value.ward || !receiver.value.address) {
      if (toast.value) toast.value.kshowToast("error", "Vui lòng điền đầy đủ thông tin người nhận khi chọn bán giao hàng");
      return;
    }
  }

  if (paymentMethod.value === "both") {
    const finalAmount = totalPrice.value - discount.value;
    if (tienChuyenKhoan.value + tienMat.value !== finalAmount) {
      if (toast.value) toast.value.kshowToast("error", "Tổng tiền chuyển khoản và tiền mặt phải bằng tổng tiền hóa đơn!");
      return;
    }
  }

  // Biến payload được định nghĩa tại đây
  const payload = {
    totalPrice: totalPrice.value,
    discount: discount.value,
    paymentMethod: paymentMethod.value,
    tienChuyenKhoan: tienChuyenKhoan.value,
    tienMat: tienMat.value,
    isDelivery: payOnDelivery.value,
    receiver: payOnDelivery.value ? {
      name: receiver.value.name,
      phone: receiver.value.phone,
      city: receiver.value.city,
      district: receiver.value.district,
      ward: receiver.value.ward,
      address: receiver.value.address,
      email: receiver.value.email || null
    } : null
  };

  try {
    await axios.post(`http://localhost:8080/ban-hang/thanh-toan/${activeInvoiceId.value}`, payload);
    pendingInvoices.value = pendingInvoices.value.filter((i) => i.id !== activeInvoiceId.value);
    activeInvoiceId.value = null;
    localStorage.removeItem("activeInvoiceId");
    localStorage.removeItem("gioHangId");
    gioHangId.value = null;
    cartItems.value = [];
    selectedCustomer.value = null;
    searchCustomer.value = "";
    discountCode.value = "";
    discount.value = 0;
    orderNotes.value = "";
    paymentMethod.value = "";
    payOnDelivery.value = false;
    tienChuyenKhoan.value = 0;
    tienMat.value = 0;
    receiver.value = { name: "", phone: "", city: "", district: "", ward: "", address: "", email: "" };
    if (toast.value) toast.value.kshowToast("success", "Thanh toán thành công!");
  } catch (error) {
    console.error("Lỗi khi thanh toán:", error);
    if (toast.value) toast.value.kshowToast("error", "Thanh toán thất bại: " + (error.response?.data || error.message));
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

/* Remove overflow-x-auto styles */
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

/* New styles for table */
table {
  border-collapse: collapse;
  width: 100%;
  table-layout: auto;
}

th, td {
  padding: 1.5rem;
  text-align: left;
  vertical-align: middle;
}

th {
  background-color: #f3f4f6;
  position: sticky;
  top: 0;
  z-index: 1;
}

tr {
  transition: background-color 0.2s;
}

tr:hover {
  background-color: #f9fafb;
}

td {
  border-bottom: 1px solid #e5e7eb;
}

/* Ensure the modal content fits within max-width */
.max-w-2xl {
  max-width: 100%;
  overflow-x: hidden;
}
</style>