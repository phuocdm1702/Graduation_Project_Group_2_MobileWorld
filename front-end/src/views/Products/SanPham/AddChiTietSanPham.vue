<template>
  <div>
    <!-- Thêm BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast" />

      <!-- Phần Thêm Sản Phẩm -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-4">
        <h3 class="text-lg font-medium text-gray-600 mb-4 text-center">THÊM SẢN PHẨM</h3>
        <div class="grid grid-cols-1 gap-6">
          <div class="flex items-center">
            <label class="w-40 text-sm font-medium text-gray-700">Sản Phẩm</label>
            <input
              v-model="productData.id"
              type="text"
              placeholder="Nhập Tên sản phẩm"
              class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <!-- Hệ Điều Hành, Màn Hình, Nhà Sản Xuất -->
          <div class="grid grid-cols-3 gap-6">
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Hệ Điều Hành</label>
              <select
                v-model="productData.idHeDieuHanh"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Hệ Điều Hành</option>
                <option v-for="he in heDieuHanhOptions" :key="he.id" :value="he.id">{{ he.heDieuHanh + " " + he.phienBan }}</option>
              </select>
              <button
                @click="openAddModal('heDieuHanh')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Màn Hình</label>
              <select
                v-model="productData.idManHinh"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Màn Hình</option>
                <option v-for="man in manHinhOptions" :key="man.id" :value="man.id">{{ man.kichThuoc + " " + man.doPhanGiai }}</option>
              </select>
              <button
                @click="openAddModal('manHinh')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Nhà Sản Xuất</label>
              <select
                v-model="productData.idNhaSanXuat"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Nhà Sản Xuất</option>
                <option v-for="nha in nhaSanXuatOptions" :key="nha.id" :value="nha.id">{{ nha.nhaSanXuat }}</option>
              </select>
              <button
                @click="openAddModal('nhaSanXuat')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
          </div>

          <!-- Camera, Sim, Thiết Kế -->
          <div class="grid grid-cols-3 gap-6">
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Cụm Camera</label>
              <select
                v-model="productData.idCumCamera"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Cụm Camera</option>
                <option v-for="camera in cumCameraOptions" :key="camera.id" :value="camera.id">
                  (Trước: {{ camera.cameraTruoc || 'N/A' }}, Sau: {{ camera.cameraSau || 'N/A' }})
                </option>
              </select>
              <button
                @click="openAddModal('cumCamera')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Sim</label>
              <select
                v-model="productData.idSim"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Sim</option>
                <option v-for="sim in simOptions" :key="sim.id" :value="sim.id">{{ sim.cacLoaiSimHoTro + " " + "(hỗ trợ " +sim.soLuongSimHoTro + " sim)" }}</option>
              </select>
              <button
                @click="openAddModal('sim')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Thiết Kế</label>
              <select
                v-model="productData.idThietKe"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Thiết Kế</option>
                <option v-for="thietKe in thietKeOptions" :key="thietKe.id" :value="thietKe.id">{{ thietKe.chatLieuKhung + " " + "(" + thietKe.chatLieuMatLung + ")"}}</option>
              </select>
              <button
                @click="openAddModal('thietKe')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
          </div>

          <!-- Pin, CPU, GPU, Công Nghệ Mạng, Công Nghệ Sạc, Hỗ Trợ Công Nghệ Sạc, Chỉ Số Kháng Bụi Nước,Tình Trạng -->
          <div class="grid grid-cols-2 gap-6">
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Pin</label>
              <select
                v-model="productData.idPin"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Pin</option>
                <option v-for="pin in pinOptions" :key="pin.id" :value="pin.id">{{ pin.loaiPin + " " + "(" + pin.dungLuongPin + ")" }}</option>
              </select>
              <button
                @click="openAddModal('pin')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="ml-24 w-36 text-sm font-medium text-gray-700">CPU</label>
              <select
                v-model="productData.idCpu"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn CPU</option>
                <option v-for="cpu in cpuOptions" :key="cpu.id" :value="cpu.id">{{ cpu.tenCpu + " " + "(" + cpu.soNhan + "lõi)  " }}</option>
              </select>
              <button
                @click="openAddModal('cpu')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">GPU</label>
              <select
                v-model="productData.idGpu"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn GPU</option>
                <option v-for="gpu in gpuOptions" :key="gpu.id" :value="gpu.id">{{ gpu.tenGpu }}</option>
              </select>
              <button
                @click="openAddModal('gpu')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="ml-24 w-36 text-sm font-medium text-gray-700">Công Nghệ Mạng</label>
              <select
                v-model="productData.idCongNgheMang"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Công Nghệ Mạng</option>
                <option v-for="congNghe in congNgheMangOptions" :key="congNghe.id" :value="congNghe.id">{{ congNghe.tenCongNgheMang }}</option>
              </select>
              <button
                @click="openAddModal('congNgheMang')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Cổng Sạc</label>
              <select
                v-model="productData.idCongSac"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Cổng Sạc</option>
                <option v-for="congSac in congSacOptions" :key="congSac.id" :value="congSac.id">{{ congSac.congSac }}</option>
              </select>
              <button
                @click="openAddModal('congSac')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="ml-24 w-36 text-sm font-medium text-gray-700">Hỗ Trợ Công Nghệ Sạc</label>
              <select
                v-model="productData.idHoTroCongNgheSac"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Hỗ Trợ Công Nghệ Sạc</option>
                <option v-for="hoTro in hoTroCongNgheSacOptions" :key="hoTro.id" :value="hoTro.id">
                  {{ hoTro.tenCongNgheSac || 'N/A' }}
                </option>
              </select>
              <button
                @click="openAddModal('hoTroCongNgheSac')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-36 text-sm font-medium text-gray-700">Kháng Bụi Nước</label>
              <select
                v-model="productData.idChiSoKhangBuiVaNuoc"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Chỉ Số Kháng Bụi Nước</option>
                <option v-for="chiSo in chiSoKhangBuiVaNuocOptions" :key="chiSo.id" :value="chiSo.id">{{ chiSo.tenChiSo }}</option>
              </select>
              <button
                @click="openAddModal('chiSoKhangBuiVaNuoc')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="ml-24 w-36 text-sm font-medium text-gray-700">Tình Trạng</label>
              <select
                v-model="productData.idLoaiTinhTrang"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Tình Trạng</option>
                <option v-for="tinhTrang in tinhTrangOptions" :key="tinhTrang.id" :value="tinhTrang.id">{{ tinhTrang.loaiTinhTrang }}</option>
              </select>
              <button
                @click="openAddModal('tinhTrang')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
          </div>

          <!-- Tiện Ích Đặc Biệt, Giá Bán -->
          <div class="grid grid-cols-1 gap-6">
            <div class="flex items-center">
              <label class="w-40 text-sm font-medium text-gray-700">Tiện Ích Đặc Biệt</label>
              <input
                v-model="productData.tienIchDacBiet"
                type="text"
                placeholder="Nhập Tiện Ích Đặc Biệt"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div class="flex items-center">
              <label class="w-40 text-sm font-medium text-gray-700">Giá Bán</label>
              <input
                v-model="productData.giaBan"
                type="text"
                placeholder="Nhập Giá Bán"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- Phần Thêm Phiên Bản -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-4">
        <h3 class="text-lg font-medium text-gray-600 mb-4 text-center">THÊM PHIÊN BẢN</h3>
        <div class="grid grid-cols-3 gap-6">
          <!-- RAM -->
          <div class="flex items-center relative">
            <label class="w-36 text-sm font-medium text-gray-700">RAM</label>
            <div class="w-3/5 relative">
              <button
                @click="toggleDropdown('ram')"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-left"
              >
                {{ currentVariant.selectedRams.length > 0 ? currentVariant.selectedRams.length + ' RAM đã chọn' : 'Chọn RAM' }}
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
              class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
            >
              +
            </button>
          </div>

          <!-- Bộ Nhớ Trong -->
          <div class="flex items-center relative">
            <label class="w-36 text-sm font-medium text-gray-700">Bộ Nhớ Trong</label>
            <div class="w-3/5 relative">
              <button
                @click="toggleDropdown('boNhoTrong')"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-left"
              >
                {{ currentVariant.selectedBoNhoTrongs.length > 0 ? currentVariant.selectedBoNhoTrongs.length + ' ROM đã chọn' : 'Chọn Bộ Nhớ Trong' }}
              </button>
              <div
                v-if="dropdownOpen.boNhoTrong"
                class="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-lg shadow-lg max-h-48 overflow-y-auto"
              >
                <label v-for="boNho in boNhoTrongOptions" :key="boNho.id" class="flex items-center p-2 hover:bg-gray-100">
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
              class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
            >
              +
            </button>
          </div>

          <!-- Màu Sắc -->
          <div class="flex items-center relative">
            <label class="w-36 text-sm font-medium text-gray-700">Màu Sắc</label>
            <div class="w-3/5 relative">
              <button
                @click="toggleDropdown('mauSac')"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-left"
              >
                {{ currentVariant.selectedMauSacs.length > 0 ? currentVariant.selectedMauSacs.length + ' Màu đã chọn' : 'Chọn Màu Sắc' }}
              </button>
              <div
                v-if="dropdownOpen.mauSac"
                class="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-lg shadow-lg max-h-48 overflow-y-auto"
              >
                <label v-for="mau in mauSacOptions" :key="mau.id" class="flex items-center p-2 hover:bg-gray-100">
                  <input
                    type="checkbox"
                    :value="mau.id"
                    v-model="currentVariant.selectedMauSacs"
                    class="mr-2"
                  />
                  {{ mau.tenMau }}
                </label>
              </div>
            </div>
            <button
              @click="openAddModal('mauSac')"
              class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
            >
              +
            </button>
          </div>
        </div>

        <!-- Nút thêm biến thể -->
        <div class="mt-4 flex justify-end">
          <button
            @click="addVariant"
            class="px-4 py-2 bg-orange-500 text-white font-semibold rounded-lg hover:bg-orange-600 transition"
          >
            Tạo biến thể sản phẩm
          </button>
        </div>

        <!-- Danh sách biến thể đã thêm (dưới dạng bảng) -->
        <div v-if="productVariants.length > 0" class="mt-6">
          <!-- Nhóm các biến thể theo RAM và Bộ Nhớ Trong -->
          <div v-for="(group, groupIndex) in groupVariantsByRamAndRom" :key="groupIndex">
            <h4 class="text-lg font-medium text-gray-600 mb-2">
              PHIÊN BẢN {{ group.ram }}/{{ group.rom }}
            </h4>
            <table class="w-full border-collapse border border-gray-300">
              <thead>
              <tr class="bg-gray-100">
                <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">STT</th>
                <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">TÊN SẢN PHẨM</th>
                <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">MÀU SẮC</th>
                <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">SỐ LƯỢNG</th>
                <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">ĐƠN GIÁ</th>
                <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">THAO TÁC</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(variant, variantIndex) in group.variants" :key="variantIndex">
                <td class="border border-gray-300 p-2 text-center">{{ variantIndex + 1 }}</td>
                <td class="border border-gray-300 p-2">{{ productData.id || 'N/A' }}</td>
                <td class="border border-gray-300 p-2">
                  <div class="flex items-center">
                      <span
                        class="w-6 h-6 mr-2 inline-block"
                        :style="{ backgroundColor: mauSacOptions.find(mau => mau.id === variant.idMauSac)?.ma || '#000' }"
                      ></span>
                    {{ mauSacOptions.find(mau => mau.id === variant.idMauSac)?.tenMau || 'N/A' }}
                  </div>
                </td>
                <td class="border border-gray-300 p-2">
                  <input
                    v-model="variant.soLuong"
                    type="number"
                    min="0"
                    class="w-20 p-1 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-center"
                  />
                </td>
                <td class="border border-gray-300 p-2">
                  <input
                    v-model="variant.donGia"
                    type="text"
                    class="w-32 p-1 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </td>
                <td class="border border-gray-300 p-2 flex justify-center gap-2">
                  <label class="flex items-center">
                    <input
                      type="file"
                      @change="handleVariantImageUpload($event, group.startIndex + variantIndex)"
                      class="hidden"
                    />
                    <span class="text-sm text-gray-500 mr-2">
                        {{ variantImages[group.startIndex + variantIndex]?.fileName || 'Choose File' }}
                      </span>
                    <button
                      class="px-2 py-1 bg-red-500 text-white rounded-lg hover:bg-red-600 transition"
                      @click="$event.target.parentElement.querySelector('input[type=file]').click()"
                    >
                      Upload
                    </button>
                  </label>
                  <button
                    @click="removeVariant(group.startIndex + variantIndex)"
                    class="px-2 py-1 bg-red-500 text-white rounded-lg hover:bg-red-600 transition"
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

      <!-- Phần Thêm Ảnh -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-4">
        <h3 class="text-lg font-medium text-gray-600 mb-4 text-center">THÊM ẢNH</h3>
        <div class="grid grid-cols-1 gap-6">
          <div class="flex items-center">
            <label class="w-40 text-sm font-medium text-gray-700">Ảnh</label>
            <input
              type="file"
              @change="handleImageUpload"
              class="w-full p-2 border border-gray-300 rounded-lg"
            />
          </div>
        </div>
        <div class="mt-4 flex justify-end">
          <button
            @click="addImage"
            class="px-4 py-2 bg-orange-500 text-white font-semibold rounded-lg hover:bg-orange-600 transition"
          >
            Tải lên ảnh
          </button>
        </div>
        <div v-for="(image, index) in productImages" :key="index" class="grid grid-cols-1 gap-6 mt-2">
          <div class="flex items-center">
            <input
              :value="image.fileName || 'No file chosen'"
              type="text"
              class="w-full p-2 border border-gray-300 rounded-lg bg-gray-100"
              readonly
            />
          </div>
        </div>
      </div>

      <!-- Nút chức năng -->
      <div class="flex justify-end gap-2 mt-4">
        <button
          @click="resetForm"
          class="px-4 py-2 bg-gray-300 text-gray-700 font-semibold rounded-lg hover:bg-gray-400 transition"
        >
          Làm mới
        </button>
        <button
          @click="handleSubmit"
          class="px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg hover:bg-orange-600 transition"
        >
          Lưu
        </button>
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
          <div v-if="currentAttribute === 'id'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">ID Sản Phẩm</label>
              <input v-model="entityData.id" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập ID sản phẩm" />
            </div>
          </div>
          <div v-if="currentAttribute === 'heDieuHanh'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Hệ Điều Hành</label>
              <input v-model="entityData.tenHeDieuHanh" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên hệ điều hành" />
            </div>
          </div>
          <div v-if="currentAttribute === 'manHinh'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Kích Thước Màn Hình</label>
              <input v-model="entityData.kichThuoc" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập kích thước màn hình" />
            </div>
          </div>
          <div v-if="currentAttribute === 'nhaSanXuat'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Nhà Sản Xuất</label>
              <input v-model="entityData.tenNhaSanXuat" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên nhà sản xuất" />
            </div>
          </div>
          <div v-if="currentAttribute === 'cumCamera'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Cụm Camera</label>
              <input v-model="entityData.tenCamera" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên cụm camera" />
            </div>
          </div>
          <div v-if="currentAttribute === 'sim'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Loại Sim</label>
              <input v-model="entityData.loaiSim" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập loại sim" />
            </div>
          </div>
          <div v-if="currentAttribute === 'thietKe'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Thiết Kế</label>
              <input v-model="entityData.tenThietKe" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên thiết kế" />
            </div>
          </div>
          <div v-if="currentAttribute === 'pin'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Dung Lượng Pin</label>
              <input v-model="entityData.dungLuong" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập dung lượng pin" />
            </div>
          </div>
          <div v-if="currentAttribute === 'cpu'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên CPU</label>
              <input v-model="entityData.tenCpu" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên CPU" />
            </div>
          </div>
          <div v-if="currentAttribute === 'gpu'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên GPU</label>
              <input v-model="entityData.tenGpu" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên GPU" />
            </div>
          </div>
          <div v-if="currentAttribute === 'congNgheMang'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Công Nghệ Mạng</label>
              <input v-model="entityData.tenCongNghe" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên công nghệ mạng" />
            </div>
          </div>
          <div v-if="currentAttribute === 'congSac'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Cổng Sạc</label>
              <input v-model="entityData.tenCongNghe" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên công nghệ sạc" />
            </div>
          </div>
          <div v-if="currentAttribute === 'hoTroCongNgheSac'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Hỗ Trợ Công Nghệ Sạc</label>
              <input v-model="entityData.ten" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên hỗ trợ công nghệ sạc" />
            </div>
          </div>
          <div v-if="currentAttribute === 'chiSoKhangBuiVaNuoc'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Mã Chỉ Số Kháng Bụi Nước</label>
              <input v-model="entityData.maChiSo" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập mã chỉ số kháng bụi nước" />
            </div>
          </div>
          <div v-if="currentAttribute === 'tinhTrang'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Tình Trạng</label>
              <input v-model="entityData.tenTinhTrang" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên tình trạng" />
            </div>
          </div>
          <div v-if="currentAttribute === 'ram'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Mã RAM</label>
              <input
                v-model="entityData.ma"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập mã RAM"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Dung Lượng RAM</label>
              <input
                v-model="entityData.dungLuong"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập mã bộ nhớ trong"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Dung Lượng Bộ Nhớ Trong</label>
              <input
                v-model="entityData.dungLuong"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập mã màu sắc"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Màu Sắc</label>
              <input
                v-model="entityData.tenMau"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập tên màu sắc (ví dụ: Đen)"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'tienIchDacBiet'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tiện Ích Đặc Biệt</label>
              <input v-model="entityData.tienIchDacBiet" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tiện ích đặc biệt" />
            </div>
          </div>
          <div v-if="currentAttribute === 'giaBan'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Giá Bán</label>
              <input v-model="entityData.giaBan" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập giá bán" />
            </div>
          </div>
        </template>
      </FormModal>
    </div>
  </div>
</template>

<script>
import { defineComponent, computed } from 'vue';
import ToastNotification from '@/components/ToastNotification.vue';
import FormModal from '@/components/FormModal.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import addProductLogic from './addProductLogic.js';

export default defineComponent({
  name: 'AddProduct',
  components: {
    ToastNotification,
    FormModal,
    BreadcrumbWrapper,
  },
  setup() {
    const logic = addProductLogic();

    // Nhóm các biến thể theo RAM và Bộ Nhớ Trong
    const groupVariantsByRamAndRom = computed(() => {
      const grouped = [];
      const seen = new Set();
      let startIndex = 0;

      logic.productVariants.value.forEach((variant, index) => {
        const ram = logic.ramOptions.value.find(r => r.id === variant.idRam)?.dungLuong || 'N/A';
        const rom = logic.boNhoTrongOptions.value.find(b => b.id === variant.idBoNhoTrong)?.dungLuong || 'N/A';
        const key = `${ram}/${rom}`;

        if (!seen.has(key)) {
          seen.add(key);
          const variantsInGroup = logic.productVariants.value.filter(v => {
            const vRam = logic.ramOptions.value.find(r => r.id === v.idRam)?.dungLuong || 'N/A';
            const vRom = logic.boNhoTrongOptions.value.find(b => b.id === v.idBoNhoTrong)?.dungLuong || 'N/A';
            return `${vRam}/${vRom}` === key;
          });

          grouped.push({
            ram,
            rom,
            variants: variantsInGroup,
            startIndex,
          });

          startIndex += variantsInGroup.length;
        }
      });

      return grouped;
    });

    return {
      ...logic,
      groupVariantsByRamAndRom,
    };
  },
});
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}

input[type="color"] {
  height: 2rem;
  padding: 0;
  cursor: pointer;
}

/* Đảm bảo dropdown hiển thị đúng */
.relative {
  position: relative;
}

/* Tùy chỉnh giao diện dropdown */
.absolute {
  position: absolute;
}

/* Tùy chỉnh checkbox */
input[type="checkbox"] {
  accent-color: #3b82f6; /* Màu xanh cho checkbox */
}

/* Tùy chỉnh hover cho các mục trong dropdown */
.hover\:bg-gray-100:hover {
  background-color: #f3f4f6;
}

/* Tùy chỉnh bảng */
table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #d1d5db;
  padding: 8px;
  text-align: center;
}

th {
  background-color: #f3f4f6;
  font-weight: 500;
}

tbody tr:nth-child(odd) {
  background-color: #ffffff;
}

tbody tr:nth-child(even) {
  background-color: #f9fafb;
}

input[type="number"],
input[type="text"] {
  width: 100%;
  padding: 4px;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  text-align: center;
}

button {
  transition: all 0.2s ease-in-out;
}
</style>