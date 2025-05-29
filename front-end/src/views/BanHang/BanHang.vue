<template>
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
  <ToastNotification ref="toast" />

  <div class="min-h-screen bg-gray-100 p-4 flex flex-col">
    <div class="flex-1">
      <!-- Hóa đơn chờ -->
      <div class="bg-white rounded-lg shadow mb-4 p-4">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-orange-500">Hóa đơn chờ</h2>
          <button
            @click="createNewPendingInvoice"
            class="p-2 bg-orange-500 text-white rounded hover:bg-orange-600 transition"
            title="Tạo hóa đơn mới"
            :disabled="isCreatingInvoice"
          >
            <i class="fas fa-plus"></i>
          </button>
        </div>
        <div class="flex space-x-2 overflow-x-auto pb-2">
          <div
            v-for="invoice in pendingInvoices"
            :key="invoice.id"
            @click="loadPendingInvoice(invoice)"
            :class="{
              'bg-orange-100 border-orange-400': activeInvoiceId === invoice.id,
              'border-gray-300': activeInvoiceId !== invoice.id,
            }"
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
            <button
              @click="scanQR"
              class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600 transition"
              :disabled="!activeInvoiceId"
            >
              Quét QR
            </button>
            <button
              @click="openProductModal"
              class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600 transition"
              :disabled="!activeInvoiceId"
            >
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
              <button
                @click="removeItem(item)"
                class="bg-red-500 text-white px-3 py-1 rounded-md hover:bg-red-600 transition"
                title="Xóa sản phẩm"
              >
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
      <div
        v-if="showProductModal"
        class="modal-overlay"
        @click.self="closeProductModal"
      >
        <div class="modal-content bg-white rounded-lg shadow-lg w-full max-w-fit p-6">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-lg font-semibold text-orange-500">Chọn sản phẩm</h2>
            <div class="flex space-x-2">
              <button
                @click="refreshProducts"
                class="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600 transition"
                title="Làm mới danh sách"
                :disabled="isLoadingMore"
              >
                Làm mới
              </button>
              <button
                @click="closeProductModal"
                class="text-gray-500 hover:text-gray-700 transition"
                title="Đóng"
              >
                <i class="fas fa-times"></i>
              </button>
            </div>
          </div>
          <div v-if="!isProductsLoaded" class="text-center p-4">
            <p class="text-gray-600">Đang tải sản phẩm...</p>
          </div>
          <div v-else>
            <div class="mb-4">
              <label class="block text-sm font-medium text-gray-700">Tìm kiếm sản phẩm</label>
              <input
                v-model="productSearchQuery"
                @input="searchProductsWithIMEI"
                type="text"
                class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
                placeholder="Nhập tên, mã sản phẩm hoặc IMEI"
              />
            </div>
            <div class="max-h-96 overflow-y-auto" @scroll="handleScroll">
              <table class="w-full" v-memo="[filteredProducts]">
                <thead>
                <tr class="bg-gray-100">
                  <th class="p-4 text-left">STT</th>
                  <th class="p-4 text-left min-w-[150px]">Mã</th>
                  <th class="p-4 text-left min-w-[200px]">Tên sản phẩm</th>
                  <th class="p-4 text-left min-w-[150px]">Màu</th>
                  <th class="p-4 text-left min-w-[150px]">Ram</th>
                  <th class="p-4 text-left min-w-[150px]">Bộ nhớ trong</th>
                  <th class="p-4 text-left min-w-[120px]">Số lượng</th>
                  <th class="p-4 text-left min-w-[150px]">Giá</th>
                  <th class="p-4 text-right min-w-[120px]">Chọn Imel</th>
                </tr>
                </thead>
                <tbody>
                <tr
                  v-for="(product, index) in filteredProducts"
                  :key="index"
                  class="border-b hover:bg-gray-50"
                >
                  <td class="p-4">{{ index + 1 }}</td>
                  <td class="p-4">{{ product.tenSanPham }}</td>
                  <td class="p-4">{{ product.ma }}</td>
                  <td class="p-4">{{ product.mauSac || 'N/A' }}</td>
                  <td class="p-4">{{ product.dungLuongRam || 'N/A' }}</td>
                  <td class="p-4">{{ product.dungLuongBoNhoTrong || 'N/A' }}</td>
                  <td class="p-4">{{ product.soLuong || 0 }}</td>
                  <td class="p-4">{{ product.giaBan.toLocaleString() }} đ</td>
                  <td class="p-4 text-right">
                    <button
                      @click="showIMEIList(product)"
                      class="px-3 py-1 bg-orange-500 text-white rounded hover:bg-orange-600 transition"
                    >
                      Chọn
                    </button>
                  </td>
                </tr>
                </tbody>
              </table>
              <div v-if="isLoadingMore" class="text-center p-4">
                <p class="text-gray-600">Đang tải thêm...</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Modal chọn IMEI -->
      <div
        v-if="showIMEIModal"
        class="modal-overlay"
        @click.self="closeIMEIModal"
      >
        <div class="modal-content bg-white rounded-lg shadow-lg w-full max-w-2xl p-6">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-lg font-semibold text-orange-500">
              Chọn IMEI cho {{ selectedProduct?.tenSanPham }} ({{ selectedProduct?.mauSac }}, {{ selectedProduct?.dungLuongRam }}, {{ selectedProduct?.dungLuongBoNhoTrong }})
            </h2>
            <button
              @click="closeIMEIModal"
              class="text-gray-500 hover:text-gray-700 transition"
              title="Đóng"
            >
              <i class="fas fa-times"></i>
            </button>
          </div>
          <div class="max-h-96 overflow-y-auto">
            <table class="w-full">
              <thead>
              <tr class="bg-gray-100">
                <th class="p-2 text-left">IMEI</th>
                <th class="p-2 text-right">Chọn</th>
              </tr>
              </thead>
              <tbody>
              <tr
                v-for="(imei, index) in availableIMEIs"
                :key="index"
                class="border-b hover:bg-gray-50"
              >
                <td class="p-2">{{ imei.imei }}</td>
                <td class="p-2 text-right">
                  <input
                    type="checkbox"
                    :value="imei.imei"
                    v-model="selectedIMEIs"
                    class="form-checkbox h-5 w-5 text-orange-500"
                    @change="handleIMEISelection"
                  />
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div v-if="selectedIMEIs.length > 0" class="mt-4">
            <h3 class="text-md font-semibold text-gray-700 mb-2">IMEI đã chọn:</h3>
            <div class="bg-gray-50 border border-gray-200 rounded-lg p-4 max-h-40 overflow-y-auto">
              <div class="flex flex-wrap gap-2">
                <span
                  v-for="imei in selectedIMEIs"
                  :key="imei"
                  class="inline-flex items-center px-3 py-1 bg-orange-100 text-orange-800 rounded-full text-sm font-medium transition-all hover:bg-orange-200"
                >
                  {{ imei }}
                  <button
                    @click="removeIMEI(imei)"
                    class="ml-2 text-orange-600 hover:text-orange-800 focus:outline-none"
                    title="Xóa IMEI"
                  >
                    <i class="fas fa-times text-xs"></i>
                  </button>
                </span>
              </div>
            </div>
          </div>
          <div class="mt-4 text-right">
            <button
              @click="addProductWithIMEIs"
              :disabled="selectedIMEIs.length === 0"
              class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600 disabled:bg-gray-400 transition"
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
              class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
              placeholder="Nhập tên hoặc số điện thoại"
              @input="searchCustomers"
            />
          </div>
          <div class="flex items-end">
            <button
              @click="openCustomerModal"
              class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600 transition"
            >
              Thêm mới khách hàng
            </button>
          </div>
        </div>
        <div v-if="selectedCustomer" class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">Tên khách hàng</label>
            <input
              v-model="customer.name"
              type="text"
              class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
              placeholder="Nguyễn Oanh"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
            <input
              v-model="customer.phone"
              type="text"
              class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
              placeholder="0985357224"
            />
          </div>
        </div>
      </div>

      <!-- Thông tin đơn hàng -->
      <div class="bg-white p-4 rounded-lg shadow">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-orange-500">Thông tin đơn hàng</h2>
          <label class="flex items-center">
            <label class="switch">
              <input type="checkbox" v-model="isDelivery" @change="toggleDelivery($event.target.checked)">
              <span class="slider"></span>
            </label>
            <span class="ml-3 text-gray-800 font-medium">Bán giao hàng</span>
          </label>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div v-if="isDelivery">
            <h3 class="text-md font-medium text-orange-500 mb-2">Thông tin người nhận</h3>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Tên người nhận</label>
                <input
                  v-model="receiver.name"
                  type="text"
                  class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
                  :disabled="!isReceiverEditable"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
                <input
                  v-model="receiver.phone"
                  type="text"
                  class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
                  :disabled="!isReceiverEditable"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Thành phố</label>
                <input
                  v-model="receiver.city"
                  type="text"
                  class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
                  :disabled="!isReceiverEditable"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Quận, Huyện</label>
                <input
                  v-model="receiver.district"
                  type="text"
                  class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
                  :disabled="!isReceiverEditable"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Phường, xã</label>
                <input
                  v-model="receiver.ward"
                  type="text"
                  class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
                  :disabled="!isReceiverEditable"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Địa chỉ cụ thể</label>
                <input
                  v-model="receiver.address"
                  type="text"
                  class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
                  :disabled="!isReceiverEditable"
                />
              </div>
            </div>
            <div class="mt-4">
              <label class="block text-sm font-medium text-gray-700">Ghi chú</label>
              <textarea
                v-model="orderNotes"
                class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
                rows="3"
              ></textarea>
            </div>
          </div>

          <div :class="{'md:col-start-2': !isDelivery}">
            <div class="border-t border-gray-300 mb-4"></div>
            <div class="mb-4">
              <h3 class="text-md font-medium text-orange-500">Mã giảm giá</h3>
              <div class="flex space-x-4">
                <input
                  v-model="discountCodeInput"
                  class="p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
                  list="languages"
                  placeholder="Chọn mã riêng hoặc nhập mã công khai"
                  @focus="fetchDiscountCodes"
                />
                <datalist id="languages">
                  <option v-for="code in discountCodes" :key="code.id" :value="code.ma" />
                </datalist>
                <button
                  @click="applyDiscount"
                  class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600 transition"
                >
                  Áp dụng
                </button>
              </div>
              <p v-if="discountCodes.length === 0 && idKhachHang" class="mt-2 text-sm text-gray-600">
                Không có mã giảm giá riêng. Vui lòng nhập mã công khai nếu có.
              </p>
              <div class="mt-2 text-right">
                <p>Tổng tiền hàng: {{ totalPrice.toLocaleString() }} đ</p>
                <p>Giảm giá: -{{ discount.toLocaleString() }} đ</p>
                <p class="text-lg font-semibold">
                  Tổng tiền cần thanh toán: {{ (totalPrice - discount).toLocaleString() }} đ
                </p>
              </div>
            </div>
            <div class="mb-4">
              <h3 class="text-md font-medium text-orange-500">Phương thức thanh toán</h3>
              <div class="flex flex-wrap gap-2">
                <button
                  @click="selectPayment('transfer')"
                  :class="{
                    'bg-orange-100 border-orange-500 text-orange-600': paymentMethod === 'transfer',
                    'bg-transparent border-orange-400 text-orange-500': paymentMethod !== 'transfer'}"
                  class="px-4 py-2 border rounded transition hover:bg-orange-100">
                  Chuyển khoản
                </button>

                <button
                  @click="selectPayment('cash')"
                  :class="{
                    'bg-orange-100 border-orange-500 text-orange-600': paymentMethod === 'cash',
                    'bg-transparent border-orange-400 text-orange-500': paymentMethod !== 'cash'}"
                  class="px-4 py-2 border rounded transition hover:bg-orange-100">
                  Tiền mặt
                </button>

                <button
                  @click="selectPayment('both')"
                  :class="{
                    'bg-orange-100 border-orange-500 text-orange-600': paymentMethod === 'both',
                    'bg-transparent border-orange-400 text-orange-500': paymentMethod !== 'both'}"
                  class="px-4 py-2 border rounded transition hover:bg-orange-100">
                  Cả hai
                </button>
              </div>
              <div v-if="paymentMethod === 'both'" class="mt-4 grid grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700">Tiền chuyển khoản</label>
                  <input
                    v-model.number="tienChuyenKhoan"
                    type="number"
                    class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
                    placeholder="Nhập số tiền chuyển khoản"
                    min="0"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700">Tiền mặt</label>
                  <input
                    v-model.number="tienMat"
                    type="number"
                    class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
                    placeholder="Nhập số tiền mặt"
                    min="0"
                  />
                </div>
              </div>
            </div>
            <div class="text-right">
              <button
                @click="ThanhToan"
                :disabled="!activeInvoiceId || cartItems.length === 0 || isCreatingOrder"
                class="px-6 py-2 bg-orange-500 text-white rounded hover:bg-orange-600 disabled:bg-gray-400 transition"
              >
                Thanh toán
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
            class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
            placeholder="Nhập tên khách hàng"
            required
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
          <input
            v-model="entityData.phone"
            type="text"
            class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
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
            class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
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
            class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
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
            class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
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
            class="mt-1 p-2 w-full border rounded focus:outline-none focus:ring-2 focus:ring-orange-500"
            placeholder="Nhập địa chỉ cụ thể"
            required
          />
        </div>
      </div>
    </template>
  </FormModal>

  <ConfirmModal
    :show="showConfirmModal"
    :message="'Bạn có chắc chắn muốn thanh toán chưa?'"
    @confirm="createOrder"
    @cancel="showConfirmModal = false"
  />
</template>

<style scoped>
/* Thay đổi màu hover của cursor-pointer */
.cursor-pointer:hover {
  background-color: #e0e0e0; /* Nền xám nhạt nhẹ hơn để tương phản */
}

/* Giữ màu cam cho các phần nổi bật */
.bg-orange-100 {
  background-color: #ffedd5;
  border-color: #fdba74;
}

/* Thanh cuộn */
.overflow-x-auto {
  scrollbar-width: thin;
  scrollbar-color: #333333 #000000; /* Thanh cuộn màu đen để đồng bộ */
}

.overflow-x-auto::-webkit-scrollbar {
  height: 8px;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
  background-color: #333333; /* Thanh cuộn màu xám đậm */
  border-radius: 4px;
}

.overflow-x-auto::-webkit-scrollbar-track {
  background-color: #000000; /* Nền thanh cuộn màu đen */
}

/* Bảng */
table {
  border-collapse: collapse;
  width: 100%;
  table-layout: auto;
}

table td:nth-child(2) {
  max-width: 400px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding: 0.75rem;
  transition: background-color 0.2s ease;
}

table td:nth-child(2):hover {
  background-color: #fff7ed; /* Nền cam nhạt khi hover */
  cursor: pointer;
}

/* Điều chỉnh biểu tượng và văn bản */
table td:nth-child(2) span:last-child i {
  vertical-align: middle;
}

table td:nth-child(2) span:last-child {
  letter-spacing: 0.5px;
}

th,
td {
  padding: 1rem;
  text-align: left;
  vertical-align: middle;
  color: #000000; /* Chuyển màu chữ thành đen */
}

th {
  background-color: #e0e0e0; /* Nền tiêu đề bảng sáng hơn một chút */
  position: sticky;
  top: 0;
  z-index: 1;
}

tr {
  transition: background-color 0.2s;
}

tr:hover {
  background-color: #f0f0f0; /* Nền sáng hơn khi hover */
}

td {
  border-bottom: 1px solid #000000; /* Viền đen để đồng bộ */
}

/* Công tắc */
.switch {
  font-size: 17px;
  position: relative;
  display: inline-block;
  width: 3.5em;
  height: 2em;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  inset: 0;
  background: #333333; /* Đổi màu xám thành xám đậm để phù hợp với nền đen */
  border-radius: 50px;
  transition: all 0.4s cubic-bezier(0.23, 1, 0.320, 1);
}

.slider:before {
  position: absolute;
  content: "";
  height: 1.4em;
  width: 1.4em;
  left: 0.3em;
  bottom: 0.3em;
  background-color: white;
  border-radius: 50px;
  box-shadow: 0 0px 20px rgba(0, 0, 0, 0.4);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.switch input:checked + .slider {
  background: #f59e0b;
}

.switch input:focus + .slider {
  box-shadow: 0 0 1px #f59e0b;
}

.switch input:checked + .slider:before {
  transform: translateX(1.6em);
  width: 2em;
  height: 2em;
  bottom: 0;
}

/* Các phần tử khác có màu xám trong template */
.text-gray-500 {
  color: #000000 !important; /* Đổi màu xám thành đen */
}

.text-gray-600 {
  color: #000000 !important; /* Đổi màu xám thành đen */
}

.text-gray-700 {
  color: #000000 !important; /* Đổi màu xám thành đen */
}

.text-gray-800 {
  color: #000000 !important; /* Đổi màu xám thành đen */
}

.bg-gray-100 {
  background-color: #ffffff; /* Đổi nền xám nhạt thành trắng để tương phản */
}

.bg-gray-50 {
  background-color: #f5f5f5; /* Nền sáng hơn một chút */
}

.border-gray-200 {
  border-color: #000000; /* Viền đen */
}

.border-gray-300 {
  border-color: #000000; /* Viền đen */
}

/* Điều chỉnh màu nền của modal overlay */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 40;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* Overlay mờ nhỏ hơn và nhẹ hơn */
.modal-overlay::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 110%; /* Kích thước lớn hơn modal một chút */
  height: 110%;
  transform: translate(-50%, -50%);
  background-color: #000000; /* Màu đen */
  opacity: 0.2; /* Độ mờ 20% */
  z-index: 40;
  border-radius: 10px; /* Bo góc nhẹ */
}

/* Modal content */
.modal-content {
  position: relative;
  z-index: 50;
}

/* Nút đóng modal */
button.text-gray-500:hover {
  color: #333333 !important; /* Đổi màu hover của nút đóng thành xám đậm */
}

/* Đảm bảo các nhãn và văn bản trong form đều là màu đen */
label.text-gray-700 {
  color: #000000 !important;
}

/* Đổi màu nền của các phần tử trong giỏ hàng và hóa đơn */
.bg-white {
  background-color: #ffffff; /* Giữ màu trắng cho nền */
}

/* Đảm bảo tổng tiền và các văn bản quan trọng nổi bật */
.text-lg.font-semibold {
  color: #000000 !important; /* Tổng tiền màu đen */
}

/* Thanh toán và các nút */
button:disabled.bg-gray-400 {
  background-color: #666666 !important; /* Đổi màu xám của nút disabled thành xám đậm */
}
</style>

<script setup>
import { ref } from 'vue';
import ConfirmModal from "@/components/ConfirmModal.vue";
import DynamicTable from '@/components/DynamicTable.vue';
import ToastNotification from '@/components/ToastNotification.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import FormModal from '@/components/FormModal.vue';
import useBanHang from '@/views/BanHang/BanHang';
import axios from "axios";

const {
  idKhachHang,
  toast,
  breadcrumbItems,
  provinces,
  districts,
  wards,
  isCustomerModalOpen,
  openCustomerModal,
  handleProvinceChange,
  handleDistrictChange,
  cartItems,
  cartColumns,
  productColumns,
  imeiColumns,
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
  showIMEIModal,
  products,
  filteredProducts,
  productSearchQuery,
  selectedProduct,
  availableIMEIs,
  selectedIMEIs,
  gioHangId,
  tienChuyenKhoan,
  tienMat,
  isReceiverEditable,
  totalPrice,
  isProductsLoaded,
  isLoadingMore,
  currentPage,
  pageSize,
  getNestedValue,
  editItem,
  toggleStatus,
  removeItem,
  createNewPendingInvoice,
  loadPendingInvoice,
  scanQR,
  openProductModal,
  closeProductModal,
  showIMEIList,
  closeIMEIModal,
  searchProductsWithIMEI,
  addProductWithIMEIs,
  searchCustomers,
  addNewCustomer,
  applyDiscount,
  selectPayment,
  createOrder,
  fetchProducts,
  refreshProducts,
  discountCodes,
  discountCodeInput,
  fetchDiscountCodes,
  selectDiscountCode,
  isDelivery,
  toggleDelivery,
} = useBanHang();

const isCreatingInvoice = ref(false);
const isCreatingOrder = ref(false);

const handleScroll = (event) => {
  const element = event.target;
  if (
    element.scrollHeight - element.scrollTop <= element.clientHeight + 100 &&
    !isLoadingMore.value
  ) {
    fetchProducts(true);
  }
};

const handleIMEISelection = () => {
  console.log('Selected IMEIs:', selectedIMEIs.value);
};

const showConfirmModal = ref(false);

const ThanhToan = () => {
  showConfirmModal.value = true;
}
</script>