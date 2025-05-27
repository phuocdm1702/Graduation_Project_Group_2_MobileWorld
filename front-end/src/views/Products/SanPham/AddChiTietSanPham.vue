<template>
  <div>
    <BreadcrumbWrapper v-if="breadcrumbItems" :breadcrumb-items="breadcrumbItems"/>

    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast"/>

      <div v-if="isLoading" class="text-center">
        <p>Đang tải dữ liệu...</p>
      </div>

      <div class="bg-white shadow-lg rounded-lg p-5 mb-4">
        <h3 class="text-xl font-semibold text-gray-800 mb-6 text-center">THÊM SẢN PHẨM</h3>
        <div class="grid grid-cols-1 gap-6">
          <!-- Tên sản phẩm -->
          <div class="flex items-center relative">
            <label class="w-28 text-sm font-medium text-gray-700">Tên Sản Phẩm</label>
            <div class="w-full relative">
              <input
                v-model="productData.tenSanPham"
                type="text"
                placeholder="Nhập hoặc chọn tên sản phẩm"
                class="input-field p-2 border border-gray-300"
                @focus="showProductDropdown = true"
                @blur="delayHideProductDropdown"
                @input="filterProducts"
              />
              <div
                v-if="showProductDropdown && filteredProductNameOptions.length > 0"
                class="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-lg shadow-lg max-h-48 overflow-y-auto"
              >
                <div
                  v-for="product in filteredProductNameOptions"
                  :key="product.id"
                  class="p-2 hover:bg-gray-100 cursor-pointer"
                  @click="selectProduct(product)"
                >
                  {{ product.tenSanPham }}
                </div>
              </div>
            </div>
          </div>

          <!-- Hệ Điều Hành, Màn Hình, Nhà Sản Xuất -->
          <div class="grid grid-cols-3 gap-6">
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Hệ Điều Hành</label>
              <v-select
                v-model="productData.idHeDieuHanh"
                :options="heDieuHanhOptions"
                :get-option-label="option => `${option.heDieuHanh} ${option.phienBan}`"
                :reduce="option => option.id"
                placeholder="Chọn Hệ Điều Hành"
                class="input-field w-3/5"
                :disabled="isProductSelected"
              />
              <button
                @click="openAddModal('heDieuHanh')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Màn Hình</label>
              <v-select
                v-model="productData.congNgheManHinh"
                :options="congNgheManHinhOptions"
                :get-option-label="option => `${option.congNgheManHinh} ${option.chuanManHinh}`"
                :reduce="option => option.id"
                placeholder="Chọn Màn Hình"
                class="input-field w-3/5"
                :disabled="isProductSelected"
              />
              <button
                @click="openAddModal('congNgheManHinh')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Nhà Sản Xuất</label>
              <v-select
                v-model="productData.idNhaSanXuat"
                :options="nhaSanXuatOptions"
                label="nhaSanXuat"
                :reduce="option => option.id"
                placeholder="Chọn Nhà Sản Xuất"
                class="input-field w-3/5"
                :disabled="isProductSelected"
              />
              <button
                @click="openAddModal('nhaSanXuat')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
          </div>

          <!-- Camera, Sim, Thiết Kế -->
          <div class="grid grid-cols-3 gap-6">
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Cụm Camera</label>
              <v-select
                v-model="productData.idCumCamera"
                :options="cumCameraOptions"
                :get-option-label="option => `${option.cameraTruoc || 'N/A'} (${option.cameraSau ? option.cameraSau.split(',').slice(0, 2).join(', ') : 'N/A'})`"
                :reduce="option => option.id"
                placeholder="Chọn Cụm Camera"
                class="input-field w-3/5"
                :disabled="isProductSelected"
              />
              <button
                @click="openAddModal('cumCamera')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Sim</label>
              <v-select
                v-model="productData.idSim"
                :options="simOptions"
                :get-option-label="option => `${option.cacLoaiSimHoTro} (hỗ trợ ${option.soLuongSimHoTro} sim)`"
                :reduce="option => option.id"
                placeholder="Chọn Sim"
                class="input-field w-3/5"
                :disabled="isProductSelected"
              />
              <button
                @click="openAddModal('sim')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Thiết Kế</label>
              <v-select
                v-model="productData.idThietKe"
                :options="thietKeOptions"
                :get-option-label="option => `${option.chatLieuKhung} (${option.chatLieuMatLung})`"
                :reduce="option => option.id"
                placeholder="Chọn Thiết Kế"
                class="input-field w-3/5"
                :disabled="isProductSelected"
              />
              <button
                @click="openAddModal('thietKe')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
          </div>

          <!-- Pin, CPU, GPU, Công Nghệ Mạng, Công Nghệ Sạc, Hỗ Trợ Công Nghệ Sạc, Chỉ Số Kháng Bụi Nước -->
          <div class="grid grid-cols-3 gap-6">
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Pin</label>
              <v-select
                v-model="productData.idPin"
                :options="pinOptions"
                :get-option-label="option => `${option.loaiPin} (${option.dungLuongPin})`"
                :reduce="option => option.id"
                placeholder="Chọn Pin"
                class="input-field w-3/5"
                :disabled="isProductSelected"
              />
              <button
                @click="openAddModal('pin')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">CPU</label>
              <v-select
                v-model="productData.idCpu"
                :options="cpuOptions"
                :get-option-label="option => `${option.tenCpu} (${option.soNhan} lõi)`"
                :reduce="option => option.id"
                placeholder="Chọn CPU"
                class="input-field w-3/5"
                :disabled="isProductSelected"
              />
              <button
                @click="openAddModal('cpu')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">GPU</label>
              <v-select
                v-model="productData.idGpu"
                :options="gpuOptions"
                label="tenGpu"
                :reduce="option => option.id"
                placeholder="Chọn GPU"
                class="input-field w-3/5"
                :disabled="isProductSelected"
              />
              <button
                @click="openAddModal('gpu')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Công Nghệ Mạng</label>
              <v-select
                v-model="productData.idCongNgheMang"
                :options="congNgheMangOptions"
                label="tenCongNgheMang"
                :reduce="option => option.id"
                placeholder="Chọn Công Nghệ Mạng"
                class="input-field"
                :disabled="isProductSelected"
              />
              <button
                @click="openAddModal('congNgheMang')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Hỗ Trợ Công Nghệ Sạc</label>
              <v-select
                v-model="productData.hoTroCongNgheSac"
                :options="hoTroCongNgheSacOptions"
                :get-option-label="option => `${option.congSac || 'N/A'} (${option.congNgheHoTro ? option.congNgheHoTro.split(',').slice(0, 2).join(', ') : 'N/A'})`"
                :reduce="option => option.id"
                placeholder="Chọn Hỗ Trợ Công Nghệ Sạc"
                class="input-field w-3/5"
                :disabled="isProductSelected"
              />
              <button
                @click="openAddModal('hoTroCongNgheSac')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Kháng Bụi Nước</label>
              <v-select
                v-model="productData.idChiSoKhangBuiVaNuoc"
                :options="chiSoKhangBuiVaNuocOptions"
                label="tenChiSo"
                :reduce="option => option.id"
                placeholder="Chọn Kháng Bụi Nước"
                class="input-field w-3/5"
                :disabled="isProductSelected"
              />
              <button
                @click="openAddModal('chiSoKhangBuiVaNuoc')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Phần Thêm Phiên Bản -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-4">
        <h3 class="text-xl font-semibold text-gray-800 mb-6 text-center">THÊM PHIÊN BẢN</h3>
        <div class="grid grid-cols-3 gap-6">
          <!-- RAM -->
          <div class="flex items-center relative">
            <label class="w-36 text-sm font-medium text-gray-700">RAM</label>
            <div class="w-full relative">
              <button
                @click="toggleDropdown('ram')"
                class="input-field p-2 border border-gray-300 text-left"
              >
                {{
                  currentVariant.selectedRams.length > 0
                    ? currentVariant.selectedRams
                    .map((id) => ramOptions.find((r) => r.id === id)?.dungLuong)
                    .filter(Boolean)
                    .join(', ') || 'Không có RAM hợp lệ'
                    : 'Chọn RAM'
                }}
              </button>
              <div
                v-if="dropdownOpen.ram"
                class="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-lg shadow-lg max-h-48 overflow-y-auto"
              >
                <label v-for="ram in ramOptions" :key="ram.id" class="flex items-center p-2 hover:bg-gray-100">
                  <input
                    type="checkbox"
                    :value="ram.id"
                    v-model="currentVariant.selectedRams"
                    class="mr-2"
                  />
                  {{ ram.dungLuong }}
                </label>
              </div>
            </div>
            <button
              @click="openAddModal('ram')"
              class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
            >
              +
            </button>
          </div>

          <!-- Bộ Nhớ Trong -->
          <div class="flex items-center relative">
            <label class="w-36 text-sm font-medium text-gray-700">Bộ Nhớ Trong</label>
            <div class="w-full relative">
              <button
                @click="toggleDropdown('boNhoTrong')"
                class="input-field p-2 border border-gray-300 text-left"
              >
                {{
                  currentVariant.selectedBoNhoTrongs.length > 0
                    ? currentVariant.selectedBoNhoTrongs
                    .map((id) => boNhoTrongOptions.find((b) => b.id === id)?.dungLuong)
                    .filter(Boolean)
                    .join(', ') || 'Không có ROM hợp lệ'
                    : 'Chọn Bộ Nhớ Trong'
                }}
              </button>
              <div
                v-if="dropdownOpen.boNhoTrong"
                class="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-lg shadow-lg max-h-48 overflow-y-auto"
              >
                <label v-for="boNho in boNhoTrongOptions" :key="boNho.id"
                       class="flex items-center p-2 hover:bg-gray-100">
                  <input
                    type="checkbox"
                    :value="boNho.id"
                    v-model="currentVariant.selectedBoNhoTrongs"
                    class="mr-2"
                  />
                  {{ boNho.dungLuong }}
                </label>
              </div>
            </div>
            <button
              @click="openAddModal('boNhoTrong')"
              class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
            >
              +
            </button>
          </div>

          <!-- Màu Sắc -->
          <div class="flex items-center relative">
            <label class="w-36 text-sm font-medium text-gray-700">Màu Sắc</label>
            <div class="w-full relative">
              <button
                @click="openColorModal"
                class="input-field p-2 border border-gray-300 text-left"
              >
                {{
                  currentVariant.selectedMauSacs.length > 0
                    ? currentVariant.selectedMauSacs
                    .map((id) => mauSacOptions.find((m) => m.id === id)?.tenMau)
                    .filter(Boolean)
                    .join(', ') || 'Không có màu hợp lệ'
                    : 'Chọn Màu Sắc'
                }}
              </button>
            </div>
            <button
              @click="openAddModal('mauSac')"
              class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
            >
              +
            </button>
          </div>
        </div>

        <!-- Danh sách biến thể đã thêm -->
        <div v-if="productVariants.length > 0" class="mt-6">
          <div v-if="productVariants && productVariants.length > 0" class="col-span-3">
            <div v-for="(group, groupIndex) in groupVariantsByRamAndRom" :key="groupIndex">
              <div class="flex justify-between m-2 items-center">
                <div class="flex items-center">
                  <h4 class="text-lg font-medium text-gray-600">
                    PHIÊN BẢN {{ group.ram }}/{{ group.rom }}
                  </h4>
                </div>
                <div class="flex gap-2">
                  <div class="flex items-center">
                    <label class="w-36 text-sm font-medium text-gray-700">Giá Chung</label>
                    <input
                      v-model="groupCommonValues[group.groupKey].price"
                      type="text"
                      placeholder="Nhập giá chung"
                      class="input-field p-2 border border-gray-300"
                      @input="updateSelectedVariants(group)"
                    />
                  </div>
                  <!-- Nút xóa nhiều -->
                  <div class="flex items-center">
                    <button
                      v-if="selectedVariants.length > 0"
                      @click="removeMultipleVariants"
                      class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition"
                    >
                      Xóa {{ selectedVariants.length }} Đã Chọn
                    </button>
                  </div>
                </div>
              </div>
              <table class="w-full border-collapse border border-gray-300">
                <thead>
                <tr class="bg-gray-100">
                  <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">
                    <input
                      type="checkbox"
                      :checked="allSelected[group.groupKey] || false"
                      @change="toggleGroupSelection(group, $event.target.checked)"
                    />
                  </th>
                  <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">STT</th>
                  <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">TÊN SẢN PHẨM</th>
                  <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">MÀU SẮC</th>
                  <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">ĐƠN GIÁ</th>
                  <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">SỐ LƯỢNG IMEI</th>
                  <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">THAO TÁC</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(variant, variantIndex) in group.variants" :key="variantIndex">
                  <td class="border border-gray-300 p-2 text-center">
                    <input
                      type="checkbox"
                      v-model="selectedVariants"
                      :value="group.startIndex + variantIndex"
                      @change="updateSelectedCount(group)"
                    />
                  </td>
                  <td class="border border-gray-300 p-2 text-center">{{ variantIndex + 1 }}</td>
                  <td class="border border-gray-300 p-2">{{ productData.tenSanPham || 'N/A' }}</td>
                  <td class="border border-gray-300 p-2">
                    <div class="flex items-center">
              <span
                class="w-12 h-6 mr-2 inline-block"
                :style="{ backgroundColor: getColorFromName(mauSacOptions.find(mau => mau.id === variant.idMauSac)?.tenMau) || '#000' }"
              ></span>
                      {{ mauSacOptions.find(mau => mau.id === variant.idMauSac)?.tenMau || 'N/A' }}
                    </div>
                  </td>
                  <td class="border border-gray-300 p-2">
                    <input
                      v-model="variant.donGia"
                      type="text"
                      class="input-field p-1 border border-gray-300"
                    />
                  </td>
                  <td class="border border-gray-300 p-2 text-center">
                    {{
                      (variantImeis && variantImeis[group.startIndex + variantIndex]) ? variantImeis[group.startIndex + variantIndex].length : 0
                    }}
                  </td>
                  <td class="border border-gray-300 p-2 text-center">
                    <button
                      @click="openImeiModal(group.startIndex + variantIndex)"
                      class="px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition m-1"
                    >
                      Nhập IMEI
                    </button>
                    <button
                      @click="removeVariant(group.startIndex + variantIndex)"
                      class="px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition m-1"
                    >
                      Xóa
                    </button>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <!-- Phần Thêm Ảnh Tự Động -->
      <div v-if="showImageSection && productVariants && productVariants.length > 0"
           class="bg-white shadow-lg rounded-lg p-6 mb-4">
        <h3 class="text-xl font-semibold text-gray-800 mb-6 text-center">THÊM ẢNH SẢN PHẨM</h3>

        <!-- Ảnh Theo Màu Sắc -->
        <div v-if="uniqueColors.length > 0">
          <div class="grid grid-cols-3 gap-6">
            <div v-for="color in uniqueColors" :key="color.colorId"
                 class="relative flex flex-col items-center bg-gray-50 rounded-xl p-4 shadow-sm hover:shadow-md transition-shadow duration-300">
              <!-- Badge tên màu -->
              <span
                class="absolute -top-3 px-3 py-1 text-sm font-medium rounded-full shadow-sm text-white"
                :style="{ backgroundColor: getColorFromName(color.colorName) || '#000' }"
              >
          {{ color.colorName }}
        </span>
              <!-- Khung ảnh -->
              <div
                class="w-36 h-44 bg-white rounded-lg border border-gray-200 flex items-center justify-center overflow-hidden mt-4">
                <img
                  v-if="colorImages[color.colorId]?.previewUrl"
                  :src="colorImages[color.colorId].previewUrl"
                  alt="Color Image Preview"
                  class="w-full h-full object-contain p-2"
                />
                <span v-else class="text-gray-400 text-sm">Chưa có ảnh</span>
              </div>
              <!-- Button tải ảnh -->
              <label
                class="mt-4 flex items-center px-4 py-2 bg-orange-500 text-white text-sm font-medium rounded-lg cursor-pointer hover:bg-orange-600 transition duration-300">
                <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                     xmlns="http://www.w3.org/2000/svg">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"></path>
                </svg>
                Tải ảnh lên
                <input
                  type="file"
                  accept="image/*"
                  @change="handleColorImageUpload($event, color.colorId)"
                  class="hidden"
                />
              </label>
            </div>
          </div>
        </div>

        <!-- Nút chức năng -->
        <div class="flex justify-end gap-3 mt-8">
          <button
            @click="resetForm"
            class="px-5 py-2 bg-gray-200 text-gray-700 font-medium rounded-lg hover:bg-gray-300 transition duration-300"
          >
            Làm mới
          </button>
          <button
            @click="handleSubmit"
            :disabled="isSubmitting"
            :class="[
        'px-5 py-2 font-medium rounded-lg transition duration-300',
        isSubmitting ? 'bg-gray-400 cursor-not-allowed' : 'bg-orange-500 text-white hover:bg-orange-600'
      ]"
          >
            {{ isSubmitting ? 'Đang xử lý...' : 'Thêm sản phẩm' }}
          </button>
        </div>
      </div>

      <!-- Form Modal -->
      <FormModal
        v-if="showFormModal"
        :show="showFormModal"
        :entity-name="currentAttribute"
        :entity-data="{}"
        @submit="handleAddAttribute"
        @close="closeFormModal"
      >
        <template #default="{ entityData }">
          <div v-if="currentAttribute === 'heDieuHanh'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Hệ Điều Hành</label>
              <input
                v-model="entityData.heDieuHanh"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập tên hệ điều hành"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Phiên Bản</label>
              <input
                v-model="entityData.phienBan"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập phiên bản"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'congNgheManHinh'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Kích Thước Màn Hình</label>
              <input
                v-model="entityData.kichThuoc"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập kích thước màn hình"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Độ Phân Giải</label>
              <input
                v-model="entityData.doPhanGiai"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập độ phân giải"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'nhaSanXuat'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Nhà Sản Xuất</label>
              <input
                v-model="entityData.nhaSanXuat"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập tên nhà sản xuất"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'cumCamera'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Camera Trước</label>
              <input
                v-model="entityData.cameraTruoc"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập thông số camera trước"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Camera Sau</label>
              <input
                v-model="entityData.cameraSau"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập thông số camera sau"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'sim'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Các Loại Sim Hỗ Trợ</label>
              <input
                v-model="entityData.cacLoaiSimHoTro"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập loại sim"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Số Lượng Sim Hỗ Trợ</label>
              <input
                v-model="entityData.soLuongSimHoTro"
                type="number"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập số lượng sim"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'thietKe'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Chất Liệu Khung</label>
              <input
                v-model="entityData.chatLieuKhung"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập chất liệu khung"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Chất Liệu Mặt Lưng</label>
              <input
                v-model="entityData.chatLieuMatLung"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập chất liệu mặt lưng"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'pin'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Loại Pin</label>
              <input
                v-model="entityData.loaiPin"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập loại pin"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Dung Lượng Pin</label>
              <input
                v-model="entityData.dungLuongPin"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập dung lượng pin"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'cpu'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên CPU</label>
              <input
                v-model="entityData.tenCpu"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập tên CPU"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Số Nhân</label>
              <input
                v-model="entityData.soNhan"
                type="number"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập số nhân"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'gpu'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên GPU</label>
              <input
                v-model="entityData.tenGpu"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập tên GPU"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'congNgheMang'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Công Nghệ Mạng</label>
              <input
                v-model="entityData.tenCongNgheMang"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập tên công nghệ mạng"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'hoTroCongNgheSac'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Hỗ Trợ Công Nghệ Sạc</label>
              <input
                v-model="entityData.tenCongNgheSac"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập tên hỗ trợ công nghệ sạc"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'chiSoKhangBuiVaNuoc'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Chỉ Số Kháng Bụi Nước</label>
              <input
                v-model="entityData.tenChiSo"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập tên chỉ số kháng bụi nước"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'ram'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Mã RAM</label>
              <input
                v-model="entityData.ma"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập mã RAM"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Dung Lượng RAM</label>
              <input
                v-model="entityData.dungLuong"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập dung lượng RAM (ví dụ: 8GB)"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'boNhoTrong'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Mã Bộ Nhớ Trong</label>
              <input
                v-model="entityData.ma"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập mã bộ nhớ trong"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Dung Lượng Bộ Nhớ Trong</label>
              <input
                v-model="entityData.dungLuong"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập dung lượng bộ nhớ trong (ví dụ: 128GB)"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'mauSac'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Mã Màu Sắc</label>
              <input
                v-model="entityData.ma"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập mã màu sắc (ví dụ: #000000 cho Đen)"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Màu Sắc</label>
              <input
                v-model="entityData.tenMau"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập tên màu sắc (ví dụ: Đen)"
              />
            </div>
          </div>
        </template>
      </FormModal>

      <!-- Color Selection Modal -->
      <div v-if="showColorModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg shadow-lg p-6 w-3/4">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-medium text-gray-700">Chọn màu sắc</h3>
            <button @click="closeColorModal" class="text-gray-500 hover:text-gray-700">✕</button>
          </div>
          <div class="grid grid-cols-5 gap-4 mb-4">
            <label v-for="mau in mauSacOptions" :key="mau.id" class="flex items-center space-x-2">
              <input
                type="checkbox"
                :value="mau.id"
                v-model="currentVariant.selectedMauSacs"
                class="form-checkbox h-5 w-5 text-blue-600"
              />
              <span
                class="w-16 h-8 rounded border border-gray-300"
                :style="{ backgroundColor: getColorFromName(mau.tenMau) || '#FFFFFF' }"
              ></span>
              <span class="text-sm text-gray-700 whitespace-nowrap">{{ mau.tenMau }}</span>
            </label>
          </div>
          <div class="flex justify-between">
            <button
              @click="closeColorModal"
              class="px-4 py-2 bg-gray-300 text-gray-700 font-semibold rounded-lg hover:bg-gray-400 transition"
            >
              Đóng
            </button>
            <button
              @click="confirmColorSelection"
              class="px-4 py-2 bg-orange-500 text-white font-semibold rounded-lg hover:bg-orange-600 transition"
            >
              Xác nhận
            </button>
          </div>
        </div>
      </div>

      <!-- IMEI Input Modal -->
      <div v-if="showImeiModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg shadow-lg p-6 w-1/2">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-medium text-gray-700">Nhập IMEI</h3>
            <button @click="closeImeiModal" class="text-gray-500 hover:text-gray-700">✕</button>
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">Nhập IMEI (mỗi IMEI trên một dòng)</label>
            <textarea
              v-model="imeiInput"
              rows="5"
              class="input-field p-2 border border-gray-300"
              placeholder="Nhập IMEI, mỗi IMEI trên một dòng..."
            ></textarea>
            <div class="mt-2 text-sm text-gray-700">
              <p v-for="(imei, index) in filteredImeiList" :key="index">
                IMEI {{ index + 1 }}: {{ imei.length }} chữ số
                <span v-if="imei.length === 15" class="text-green-500"> (Đủ 15 số)</span>
                <span v-else class="text-red-500"> (Cần 15 số)</span>
              </p>
              <p v-if="!imeiInput.trim()">Chưa nhập IMEI</p>
            </div>
          </div>
          <div class="mb-4 flex items-center gap-4">
            <div class="w-3/4">
              <label class="block text-sm font-medium text-gray-700 mb-2">Nhập từ file Excel</label>
              <input
                type="file"
                accept=".xlsx, .xls"
                @change="handleExcelImport"
                class="input-field w-full p-2 border border-gray-300"
              />
            </div>
            <div class="w-1/4">
              <label class="block text-sm font-medium text-gray-700 mb-2">Tải mẫu Excel</label>
              <button
                @click="downloadImeiTemplate"
                class="px-4 py-2 bg-orange-500 text-white font-semibold rounded-lg hover:bg-orange-600 transition w-full"
              >
                Tải mẫu IMEI
              </button>
            </div>
          </div>
          <div class="flex justify-between">
            <button
              @click="closeImeiModal"
              class="px-4 py-2 bg-gray-300 text-gray-700 font-semibold rounded-lg hover:bg-gray-400 transition"
            >
              Đóng
            </button>
            <button
              @click="saveImei"
              class="px-4 py-2 bg-orange-500 text-white font-semibold rounded-lg hover:bg-orange-600 transition"
            >
              Lưu
            </button>
          </div>
        </div>
      </div>

      <!-- Lớp phủ tải -->
      <div
        v-if="isSubmitting"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50"
      >
        <div class="bg-white rounded-lg p-6 flex items-center gap-4">
          <svg
            class="animate-spin h-8 w-8 text-orange-500"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
          >
            <circle
              class="opacity-25"
              cx="12"
              cy="12"
              r="10"
              stroke="currentColor"
              stroke-width="4"
            ></circle>
            <path
              class="opacity-75"
              fill="currentColor"
              d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
            ></path>
          </svg>
          <span class="text-lg font-medium text-gray-700">Đang xử lý...</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {defineComponent, computed, ref, onMounted, watch} from 'vue';
import {useRoute} from 'vue-router';
import axios from 'axios';
import ToastNotification from '@/components/ToastNotification.vue';
import FormModal from '@/components/FormModal.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import VSelect from 'vue-select';
import 'vue-select/dist/vue-select.css';
import '@/assets/sanPham.css';

import {useProductData} from './composables/useProductData';
import {useProductVariants} from './composables/useProductVariants';
import {useProductImages} from './composables/useProductImages';
import {useDropdowns} from './composables/useDropdowns';
import {useModals} from './composables/useModals';
import {useFormHandling} from './composables/useFormHandling';
import {useApiRequests} from './composables/useApiRequests';

export default defineComponent({
  name: 'AddChiTietSanPham',
  components: {
    ToastNotification,
    FormModal,
    BreadcrumbWrapper,
    VSelect,
  },
  setup() {
    const route = useRoute();
    const toast = ref(null);
    const isSubmitting = ref(false);

    const {productData, resetProductData, setDefaultValues} = useProductData();
    const {
      heDieuHanhOptions,
      congNgheManHinhOptions,
      nhaSanXuatOptions,
      cumCameraOptions,
      simOptions,
      thietKeOptions,
      pinOptions,
      cpuOptions,
      gpuOptions,
      congNgheMangOptions,
      hoTroCongNgheSacOptions,
      chiSoKhangBuiVaNuocOptions,
      ramOptions,
      boNhoTrongOptions,
      mauSacOptions,
      fetchOptions,
      handleAddAttribute,
    } = useApiRequests(toast);

    const {dropdownOpen, toggleDropdown} = useDropdowns();
    const {
      showFormModal,
      currentAttribute,
      showColorModal,
      showImeiModal,
      currentVariantIndex,
      imeiInput,
      variantImeis,
      openAddModal,
      closeFormModal,
      openColorModal,
      closeColorModal,
      openImeiModal,
      closeImeiModal,
      saveImei,
      handleExcelImport,
      resetModals,
      downloadImeiTemplate,
    } = useModals(dropdownOpen, toggleDropdown, toast);

    const {
      productVariants,
      currentVariant,
      selectedVariants,
      allSelected,
      groupCommonValues,
      groupVariantsByRamAndRom,
      uniqueColors,
      addVariant,
      removeVariant,
      removeMultipleVariants,
      updateSelectedVariants,
      toggleGroupSelection,
      toggleAllVariants,
      updateSelectedCount,
      resetVariants,
      validateSelections,
    } = useProductVariants(ramOptions, boNhoTrongOptions, mauSacOptions, productData, variantImeis);

    const {
      showImageSection,
      mainImages,
      colorImages,
      getColorFromName,
      handleMainImageUpload,
      handleColorImageUpload,
      resetImages,
    } = useProductImages();

    const {handleSubmit} = useFormHandling(
      toast,
      productData,
      productVariants,
      mainImages,
      colorImages,
      variantImeis
    );

    // Ghi đè handleSubmit để thêm thông báo thành công
    const handleSubmitWithSuccess = async () => {
      if (isSubmitting.value) return;

      isSubmitting.value = true;
      try {
        await handleSubmit();
        toast.value?.kshowToast('success', 'Thêm mới sản phẩm thành công!');
      } catch (error) {
        // Lỗi đã được xử lý trong handleSubmit
      } finally {
        isSubmitting.value = false;
      }
    };

    const breadcrumbItems = computed(() => route.meta?.breadcrumb || ['Thêm Sản Phẩm']);

    const productNameOptions = ref([]);
    const filteredProductNameOptions = ref([]);
    const showProductDropdown = ref(false);
    const isProductSelected = ref(false);

    const fetchProductNames = async () => {
      try {
        let allProducts = [];
        let page = 0;
        let hasMore = true;
        const pageSize = 100;

        while (hasMore) {
          const response = await axios.get('http://localhost:8080/san-pham', {
            params: {
              page: page,
              size: pageSize
            }
          });

          const data = response.data;
          if (data.content && data.content.length > 0) {
            allProducts = [...allProducts, ...data.content];
            page += 1;
            hasMore = data.content.length === pageSize && (!data.totalPages || page < data.totalPages);
          } else {
            hasMore = false;
          }
        }

        productNameOptions.value = allProducts;
        filteredProductNameOptions.value = allProducts;
      } catch (error) {
        console.error('Lỗi khi lấy danh sách sản phẩm:', error);
        toast.value?.kshowToast('error', 'Lỗi khi tải danh sách sản phẩm!');
      }
    };

    const filterProducts = () => {
      const searchTerm = productData.value.tenSanPham.toLowerCase().trim();
      if (searchTerm) {
        filteredProductNameOptions.value = productNameOptions.value.filter(product =>
          product.tenSanPham.toLowerCase().includes(searchTerm)
        );
      } else {
        filteredProductNameOptions.value = productNameOptions.value;
      }
      isProductSelected.value = false; // Reset flag when user types a new name
    };

    const selectProduct = async (product) => {
      productData.value.tenSanPham = product.tenSanPham;
      showProductDropdown.value = false;
      isProductSelected.value = true; // Set flag to true when product is selected

      try {
        const response = await axios.get(`http://localhost:8080/san-pham/${product.id}`);
        const productDetails = response.data;

        productData.value = {
          ...productData.value,
          idHeDieuHanh: productDetails.idHeDieuHanh || '',
          congNgheManHinh: productDetails.congNgheManHinh || '',
          idNhaSanXuat: productDetails.idNhaSanXuat || '',
          idCumCamera: productDetails.idCumCamera || '',
          idSim: productDetails.idSim || '',
          idThietKe: productDetails.idThietKe || '',
          idPin: productDetails.idPin || '',
          idCpu: productDetails.idCpu || '',
          idGpu: productDetails.idGpu || '',
          idCongNgheMang: productDetails.idCongNgheMang || '',
          hoTroCongNgheSac: productDetails.hoTroCongNgheSac || '',
          idChiSoKhangBuiVaNuoc: productDetails.idChiSoKhangBuiVaNuoc || '',
        };

        toast.value?.kshowToast('success', 'Đã tải thông tin sản phẩm!');
      } catch (error) {
        console.error('Lỗi khi lấy chi tiết sản phẩm:', error);
        toast.value?.kshowToast('error', 'Lỗi khi tải chi tiết sản phẩm!');
      }
    };

    const delayHideProductDropdown = () => {
      setTimeout(() => {
        showProductDropdown.value = false;
      }, 200);
    };

    const filteredImeiList = computed(() => {
      return imeiInput.value
        .split('\n')
        .map(imei => imei.trim())
        .filter(imei => imei.length > 0);
    });

    const confirmColorSelection = () => {
      if (addVariant()) {
        showImageSection.value = true;
        closeColorModal();

        groupVariantsByRamAndRom.value.forEach((group) => {
          const groupKey = `${group.ram}/${group.rom}`;
          if (!(groupKey in allSelected.value)) {
            allSelected.value[groupKey] = false;
            groupCommonValues.value[groupKey] = {price: ''};
            toggleAllVariants(group, false);
          }
        });
      } else {
        toast.value?.kshowToast('error', 'Vui lòng chọn ít nhất một RAM, một Bộ Nhớ Trong và một Màu Sắc!');
      }
    };

    const handleAddAttributeLocal = async (data) => {
      try {
        const success = await handleAddAttribute(
          currentAttribute.value,
          data,
          productData,
          currentVariant
        );

        if (success) {
          toast.value?.kshowToast('success', 'Thêm thành công!');
          closeFormModal();
          validateSelections(); // Kiểm tra lại sau khi thêm tùy chọn mới
        } else {
          toast.value?.kshowToast('error', 'Thêm thất bại. Vui lòng thử lại.');
        }
      } catch (error) {
        console.error('Lỗi khi thêm thuộc tính:', error);
        toast.value?.kshowToast('error', 'Có lỗi xảy ra: ' + (error.response?.data?.message || error.message));
      }
    };

    // Hàm xóa tùy chọn (mô phỏng, thay bằng API thực tế)
    const deleteOption = async (type, id) => {
      try {
        // Thay bằng API thực tế, ví dụ: axios.delete(`http://localhost:8080/${type}/${id}`)
        console.log(`Xóa ${type} với id ${id}`);

        // Cập nhật danh sách tùy chọn
        if (type === 'mauSac') {
          mauSacOptions.value = mauSacOptions.value.filter((opt) => opt.id !== id);
        } else if (type === 'ram') {
          ramOptions.value = ramOptions.value.filter((opt) => opt.id !== id);
        } else if (type === 'boNhoTrong') {
          boNhoTrongOptions.value = boNhoTrongOptions.value.filter((opt) => opt.id !== id);
        }

        toast.value?.kshowToast('success', `Đã xóa ${type === 'mauSac' ? 'màu sắc' : type === 'ram' ? 'RAM' : 'Bộ Nhớ Trong'}!`);
      } catch (error) {
        console.error(`Lỗi khi xóa ${type}:`, error);
        toast.value?.kshowToast('error', `Lỗi khi xóa ${type}!`);
      }
    };

    const trimSpaces = () => {
      productData.value.tenSanPham = productData.value.tenSanPham.trim();
    };

    const resetForm = () => {
      resetProductData({
        heDieuHanhOptions: heDieuHanhOptions.value || [],
        congNgheManHinhOptions: congNgheManHinhOptions.value || [],
        nhaSanXuatOptions: nhaSanXuatOptions.value || [],
        cumCameraOptions: cumCameraOptions.value || [],
        simOptions: simOptions.value || [],
        thietKeOptions: thietKeOptions.value || [],
        pinOptions: pinOptions.value || [],
        cpuOptions: cpuOptions.value || [],
        gpuOptions: gpuOptions.value || [],
        congNgheMangOptions: congNgheMangOptions.value || [],
        hoTroCongNgheSacOptions: hoTroCongNgheSacOptions.value || [],
        chiSoKhangBuiVaNuocOptions: chiSoKhangBuiVaNuocOptions.value || [],
      });
      resetVariants();
      resetImages();
      resetModals();
      isProductSelected.value = false; // Reset the flag
    };

    const isLoading = ref(true);

    onMounted(async () => {
      try {
        await Promise.all([fetchOptions(), fetchProductNames()]);
        setDefaultValues({
          heDieuHanhOptions: heDieuHanhOptions.value || [],
          congNgheManHinhOptions: congNgheManHinhOptions.value || [],
          nhaSanXuatOptions: nhaSanXuatOptions.value || [],
          cumCameraOptions: cumCameraOptions.value || [],
          simOptions: simOptions.value || [],
          thietKeOptions: thietKeOptions.value || [],
          pinOptions: pinOptions.value || [],
          cpuOptions: cpuOptions.value || [],
          gpuOptions: gpuOptions.value || [],
          congNgheMangOptions: congNgheMangOptions.value || [],
          hoTroCongNgheSacOptions: hoTroCongNgheSacOptions.value || [],
          chiSoKhangBuiVaNuocOptions: chiSoKhangBuiVaNuocOptions.value || [],
        });
      } finally {
        isLoading.value = false;
      }
    });

    watch(
      () => productData.value,
      (newValue) => {
        console.log('productData cập nhật:', newValue);
      },
      {deep: true}
    );

    return {
      toast,
      productData,
      productVariants,
      currentVariant,
      heDieuHanhOptions,
      congNgheManHinhOptions,
      nhaSanXuatOptions,
      cumCameraOptions,
      simOptions,
      thietKeOptions,
      pinOptions,
      cpuOptions,
      gpuOptions,
      congNgheMangOptions,
      hoTroCongNgheSacOptions,
      chiSoKhangBuiVaNuocOptions,
      ramOptions,
      boNhoTrongOptions,
      mauSacOptions,
      dropdownOpen,
      breadcrumbItems,
      groupVariantsByRamAndRom,
      uniqueColors,
      selectedVariants,
      allSelected,
      groupCommonValues,
      showImageSection,
      mainImages,
      colorImages,
      showFormModal,
      currentAttribute,
      showColorModal,
      showImeiModal,
      currentVariantIndex,
      imeiInput,
      variantImeis,
      productNameOptions,
      filteredProductNameOptions,
      showProductDropdown,
      filteredImeiList,
      toggleDropdown,
      addVariant,
      removeVariant,
      removeMultipleVariants,
      updateSelectedVariants,
      toggleGroupSelection,
      toggleAllVariants,
      updateSelectedCount,
      openAddModal,
      closeFormModal,
      handleAddAttribute: handleAddAttributeLocal,
      openColorModal,
      closeColorModal,
      confirmColorSelection,
      openImeiModal,
      closeImeiModal,
      saveImei,
      handleExcelImport,
      handleMainImageUpload,
      handleColorImageUpload,
      isSubmitting,
      handleSubmit: handleSubmitWithSuccess,
      resetForm,
      getColorFromName,
      trimSpaces,
      filterProducts,
      selectProduct,
      delayHideProductDropdown,
      downloadImeiTemplate,
      deleteOption,
      isProductSelected,
    };
  },
});
</script>

<style scoped>
.input-field {
  @apply w-full h-12 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}

.input-field :deep(.v-select) {
  @apply w-full border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}

.input-field :deep(.vs__dropdown-toggle) {
  @apply border border-gray-300 rounded-lg h-14 overflow-hidden;
}

.input-field :deep(.vs__search) {
  @apply p-2;
}

/* Đảm bảo card có kích thước đồng đều */
.grid-cols-3 > div {
  @apply flex-1;
}

/* Hiệu ứng hover cho button tải ảnh */
label:hover svg {
  @apply transform scale-110 transition-transform duration-200;
}

/* Đảm bảo ảnh không bị méo */
img.object-contain {
  @apply max-w-full max-h-full;
}
</style>