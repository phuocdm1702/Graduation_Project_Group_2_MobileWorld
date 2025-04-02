<template>
  <!-- Breadcrumb -->
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

  <!-- Toast Notification -->
  <ToastNotification ref="toast" />

  <div class="min-h-screen bg-gray-100 p-4 flex">
    <!-- Sidebar for Pending Invoices -->
    <div class="w-64 bg-white rounded-lg shadow mr-4 p-4">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-lg font-semibold text-orange-500">Hóa đơn chờ</h2>
        <button
          @click="createNewPendingInvoice"
          class="p-1 bg-orange-500 text-white rounded hover:bg-orange-600"
        >
          <i class="fas fa-plus"></i>
        </button>
      </div>

      <div class="space-y-2 max-h-[calc(100vh-200px)] overflow-y-auto">
        <div
          v-for="invoice in pendingInvoices"
          :key="invoice.id"
          @click="loadPendingInvoice(invoice)"
          :class="{'bg-orange-100': activeInvoiceId === invoice.id}"
          class="p-2 border rounded cursor-pointer hover:bg-gray-100"
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

    <!-- Main Content -->
    <div class="flex-1">
      <!-- Header -->
      <div class="flex justify-between items-center mb-4">
        <h1 class="text-2xl font-bold text-orange-500">Bán hàng tại quầy</h1>
      </div>

      <!-- Cart Section -->
      <div class="bg-white p-4 rounded-lg shadow mb-4">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-orange-500">Giỏ hàng</h2>
          <div class="flex space-x-2">
            <button @click="scanQR" class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">Quét QR</button>
            <button @click="addProduct" class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">Thêm sản phẩm</button>
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

      <!-- Customer Info Section -->
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
            <button @click="addNewCustomer" class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">Thêm mới khách hàng</button>
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

      <!-- Order Info Section -->
      <div class="bg-white p-4 rounded-lg shadow">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-orange-500">Thông tin đơn hàng</h2>
          <label class="flex items-center">
            <input type="checkbox" class="mr-2" />
            <span>Bán giao hàng</span>
          </label>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <!-- Receiver Info -->
          <div>
            <h3 class="text-md font-medium text-orange-500 mb-2">Thông tin người nhận</h3>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Tên người nhận</label>
                <input v-model="receiver.name" type="text" class="mt-1 p-2 w-full border rounded" placeholder="Nguyễn Oanh" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
                <input v-model="receiver.phone" type="text" class="mt-1 p-2 w-full border rounded" placeholder="0985357224" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Tỉnh, Thành phố</label>
                <select v-model="receiver.city" class="mt-1 p-2 w-full border rounded">
                  <option>Tỉnh Phú Thọ</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Quận, Huyện</label>
                <select v-model="receiver.district" class="mt-1 p-2 w-full border rounded">
                  <option>Huyện Lâm Thao</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Phường, Xã</label>
                <select v-model="receiver.ward" class="mt-1 p-2 w-full border rounded">
                  <option>Xã Xuân Lũng</option>
                </select>
              </div>
            </div>
            <div class="mt-4">
              <label class="block text-sm font-medium text-gray-700">Địa chỉ cụ thể</label>
              <input v-model="receiver.address" type="text" class="mt-1 p-2 w-full border rounded" placeholder="Xóm Bình Yên" />
            </div>
            <!-- Notes -->
            <div class="mt-4">
              <label class="block text-sm font-medium text-gray-700">Ghi chú</label>
              <textarea v-model="orderNotes" class="mt-1 p-2 w-full border rounded" rows="3"></textarea>
            </div>
          </div>

          <!-- Payment and Discount Info -->
          <div>
            <!-- Divider -->
            <div class="border-t border-gray-300 mb-4"></div>

            <!-- Discount Info -->
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

            <!-- Payment Options -->
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
              <div class="mt-2">
                <label class="flex items-center">
                  <input type="checkbox" v-model="payOnDelivery" class="mr-2" />
                  <span>Thanh toán khi nhận hàng</span>
                </label>
              </div>
            </div>

            <!-- Submit Button -->
            <div class="text-right">
              <button @click="createOrder" class="px-6 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">Thanh toán</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from "vue";
import { useRoute } from "vue-router";
import DynamicTable from '@/components/DynamicTable.vue';
import ToastNotification from '@/components/ToastNotification.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';

export default {
  components: {
    DynamicTable,
    ToastNotification,
    BreadcrumbWrapper
  },
  setup() {
    const route = useRoute();

    const breadcrumbItems = computed(() => {
      if (typeof route.meta.breadcrumb === 'function') {
        return route.meta.breadcrumb(route);
      }
      return route.meta?.breadcrumb || ['Bán Hàng Tại Quầy'];
    });

    return {
      breadcrumbItems
    };
  },
  data() {
    return {
      cartItems: [],
      cartColumns: [
        { key: 'id', label: 'STT' },
        { key: 'name', label: 'Sản phẩm' },
        { key: 'imei', label: 'IMEI' },
        { key: 'price', label: 'Đơn giá', formatter: (value) => `${value.toLocaleString()} đ` },
        { key: 'actions', label: 'Xóa', cellSlot: 'actionsSlot' }
      ],
      searchCustomer: '',
      selectedCustomer: null,
      customer: { name: '', phone: '' },
      receiver: {
        name: '',
        phone: '',
        city: 'Tỉnh Phú Thọ',
        district: 'Huyện Lâm Thao',
        ward: 'Xã Xuân Lũng',
        address: ''
      },
      discountCode: '',
      discount: 0,
      orderNotes: '',
      payOnDelivery: false,
      paymentMethod: '',
      activeInvoiceId: null,
      pendingInvoices: [
        {
          id: 1,
          code: 'HD252',
          status: '0',
          items: [
            { id: 1, name: 'iPhone 13 Pro Max', imei: 'IME0000154', price: 25000000 },
            { id: 2, name: 'AirPods Pro', imei: 'IME0000155', price: 5000000 }
          ]
        },
        {
          id: 2,
          code: 'HD255',
          status: '0',
          items: [
            { id: 3, name: 'Samsung Galaxy S22 Ultra', imei: 'IME0000156', price: 22000000 }
          ]
        },
        {
          id: 3,
          code: 'HD267',
          status: '0',
          items: [
            { id: 4, name: 'iPad Pro 11"', imei: 'IME0000157', price: 18000000 },
            { id: 5, name: 'Apple Pencil 2', imei: 'IME0000158', price: 2500000 }
          ]
        }
      ]
    };
  },
  computed: {
    totalPrice() {
      return this.cartItems.reduce((total, item) => total + item.price, 0);
    }
  },
  methods: {
    getNestedValue(item, key) {
      return key.split('.').reduce((obj, k) => (obj && obj[k] !== undefined ? obj[k] : null), item) || 'N/A';
    },
    editItem(item) {
      this.$refs.toast.kshowToast('info', `Chỉnh sửa sản phẩm: ${item.name}`);
    },
    toggleStatus(item) {
      this.$refs.toast.kshowToast('success', `Đã thay đổi trạng thái sản phẩm: ${item.name}`);
    },
    removeItem(item) {
      const index = this.cartItems.findIndex(i => i.id === item.id);
      if (index !== -1) {
        this.cartItems.splice(index, 1);
        this.$refs.toast.kshowToast('success', `Đã xóa sản phẩm: ${item.name}`);
      }
    },
    createNewPendingInvoice() {
      const newInvoiceId = this.pendingInvoices.length > 0
        ? Math.max(...this.pendingInvoices.map(i => i.id)) + 1
        : 1;

      const newInvoice = {
        id: newInvoiceId,
        code: `HD${270 + newInvoiceId}`,
        status: '0',
        items: []
      };

      this.pendingInvoices.unshift(newInvoice);
      this.loadPendingInvoice(newInvoice);
      this.$refs.toast.kshowToast('success', `Đã tạo hóa đơn chờ mới: ${newInvoice.code}`);
    },
    loadPendingInvoice(invoice) {
      this.activeInvoiceId = invoice.id;
      this.cartItems = [...invoice.items];
      this.$refs.toast.kshowToast('info', `Đã tải hóa đơn chờ: ${invoice.code}`);
    },
    scanQR() {
      this.$refs.toast.kshowToast('info', 'Đang quét QR...');
    },
    addProduct() {
      const newProduct = {
        id: this.cartItems.length > 0 ? Math.max(...this.cartItems.map(i => i.id)) + 1 : 1,
        name: `Sản phẩm mới ${this.cartItems.length + 1}`,
        imei: `IME${Math.floor(1000 + Math.random() * 9000)}`,
        price: 1000000 + Math.floor(Math.random() * 9000000)
      };

      this.cartItems.push(newProduct);

      // Cập nhật vào hóa đơn chờ đang active
      if (this.activeInvoiceId) {
        const invoice = this.pendingInvoices.find(i => i.id === this.activeInvoiceId);
        if (invoice) {
          invoice.items.push(newProduct);
        }
      }

      this.$refs.toast.kshowToast('success', `Đã thêm sản phẩm: ${newProduct.name}`);
    },
    searchCustomers() {
      if (this.searchCustomer) {
        this.selectedCustomer = true;
        this.customer = { name: 'Khách hàng tìm thấy', phone: '098xxxxxxx' };
        this.$refs.toast.kshowToast('success', `Đã tìm thấy khách hàng: ${this.customer.name}`);
      } else {
        this.selectedCustomer = null;
      }
    },
    addNewCustomer() {
      this.selectedCustomer = true;
      this.customer = { name: '', phone: '' };
      this.$refs.toast.kshowToast('info', 'Thêm mới khách hàng');
    },
    applyDiscount() {
      if (this.discountCode) {
        this.discount = 2000000;
        this.$refs.toast.kshowToast('success', 'Đã áp dụng mã giảm giá');
      } else {
        this.$refs.toast.kshowToast('error', 'Vui lòng nhập mã giảm giá');
      }
    },
    selectPayment(method) {
      this.paymentMethod = method;
      let methodName = '';
      switch(method) {
        case 'transfer': methodName = 'Chuyển khoản'; break;
        case 'cash': methodName = 'Tiền mặt'; break;
        case 'both': methodName = 'Cả hai phương thức'; break;
      }
      this.$refs.toast.kshowToast('info', `Đã chọn phương thức thanh toán: ${methodName}`);
    },
    createOrder() {
      if (!this.paymentMethod && !this.payOnDelivery) {
        this.$refs.toast.kshowToast('error', 'Vui lòng chọn phương thức thanh toán');
        return;
      }

      // Xóa hóa đơn chờ nếu đang active
      if (this.activeInvoiceId) {
        this.pendingInvoices = this.pendingInvoices.filter(i => i.id !== this.activeInvoiceId);
        this.activeInvoiceId = null;
      }

      this.$refs.toast.kshowToast('success', 'Thanh toán thành công!');

      // Reset form
      this.cartItems = [];
      this.selectedCustomer = null;
      this.searchCustomer = '';
      this.discountCode = '';
      this.discount = 0;
      this.orderNotes = '';
      this.paymentMethod = '';
      this.payOnDelivery = false;
    }
  }
};
</script>

<style scoped>
.w-64 {
  width: 12rem;
  min-width: 12rem;
}

.cursor-pointer:hover {
  background-color: #f3f4f6;
}

.bg-orange-100 {
  background-color: #ffedd5;
  border-color: #fdba74;
}
</style>