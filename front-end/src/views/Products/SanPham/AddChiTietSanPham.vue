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
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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
                <option v-for="sim in simOptions" :key="sim.id" :value="sim.id">{{ sim.cacLoaiSimHoTro + " " + "(hỗ trợ " + sim.soLuongSimHoTro + " sim)" }}</option>
              </select>
              <button
                @click="openAddModal('sim')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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
                <option v-for="thietKe in thietKeOptions" :key="thietKe.id" :value="thietKe.id">{{ thietKe.chatLieuKhung + " " + "(" + thietKe.chatLieuMatLung + ")" }}</option>
              </select>
              <button
                @click="openAddModal('thietKe')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
          </div>

          <!-- Pin, CPU, GPU, Công Nghệ Mạng, Công Nghệ Sạc, Hỗ Trợ Công Nghệ Sạc, Chỉ Số Kháng Bụi Nước, Tình Trạng -->
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
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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
                <option v-for="cpu in cpuOptions" :key="cpu.id" :value="cpu.id">{{ cpu.tenCpu + " " + "(" + cpu.soNhan + " lõi)" }}</option>
              </select>
              <button
                @click="openAddModal('cpu')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
          </div>

          <!-- Tiện Ích Đặc Biệt -->
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
              class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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
              class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
            >
              +
            </button>
          </div>

          <!-- Màu Sắc -->
          <div class="flex items-center relative">
            <label class="w-36 text-sm font-medium text-gray-700">Màu Sắc</label>
            <div class="w-3/5 relative">
              <button
                @click="openColorModal"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-left"
              >
                {{ currentVariant.selectedMauSacs.length > 0 ? currentVariant.selectedMauSacs.length + ' Màu đã chọn' : 'Chọn Màu Sắc' }}
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

        <!-- Danh sách biến thể đã thêm (dưới dạng bảng) -->
        <div v-if="productVariants.length > 0" class="mt-6">
          <div v-for="(group, groupIndex) in groupVariantsByRamAndRom" :key="groupIndex">
            <div class="flex justify-between m-2">
            <h4 class="text-lg font-medium text-gray-600 mb-2">
              PHIÊN BẢN {{ group.ram }}/{{ group.rom }}
            </h4>
              <div class="flex gap-2">
                <!-- Giá Chung -->
                <div class="flex items-center">
                  <label class="w-36 text-sm font-medium text-gray-700">Giá Chung</label>
                  <input
                    v-model="commonPrice"
                    type="text"
                    placeholder="Nhập giá chung"
                    class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    @input="updateSelectedVariants"
                  />
                </div>

                <!-- Số Lượng Chung -->
                <div class="flex items-center">
                  <label class="w-36 text-sm font-medium text-gray-700">Số Lượng Chung</label>
                  <input
                    v-model="commonQuantity"
                    type="number"
                    min="0"
                    placeholder="Nhập số lượng chung"
                    class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    @input="updateSelectedVariants"
                  />
                </div>
              </div>
            </div>
            <table class="w-full border-collapse border border-gray-300">
              <thead>
              <tr class="bg-gray-100">
                <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">
                  <input
                    type="checkbox"
                    @change="toggleGroupSelection(group, $event.target.checked)"
                  />
                </th>
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
                <td class="border border-gray-300 p-2 text-center">
                  <input
                    type="checkbox"
                    v-model="selectedVariants"
                    :value="group.startIndex + variantIndex"
                    @change="updateSelectedCount"
                  />
                </td>
                <td class="border border-gray-300 p-2 text-center">{{ variantIndex + 1 }}</td>
                <td class="border border-gray-300 p-2">{{ productData.id || 'N/A' }}</td>
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
                    v-model="variant.soLuong"
                    type="number"
                    min="0"
                    class="w-full p-1 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-center"
                  />
                </td>
                <td class="border border-gray-300 p-2">
                  <input
                    v-model="variant.donGia"
                    type="text"
                    class="w-full p-1 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </td>
                <td class="border border-gray-300 p-2 flex justify-center gap-2">
                  <button
                    @click="openImeiModal(group.startIndex + variantIndex)"
                    class="px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
                  >
                    Nhập
                  </button>
                  <button
                    @click="removeVariant(group.startIndex + variantIndex)"
                    class="px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
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

      <!-- Phần Thêm Ảnh Tự Động -->
      <div v-if="showImageSection && productVariants.length > 0" class="bg-white shadow-lg rounded-lg p-5 mb-4">
        <h3 class="text-lg font-medium text-gray-600 mb-4 text-center">THÊM ẢNH</h3>

        <!-- Ảnh Chính -->
        <div class="mb-6">
          <h4 class="text-lg font-medium text-gray-600 mb-2">Ảnh Chính</h4>
          <table class="w-full border-collapse border border-gray-300">
            <thead>
            <tr class="bg-gray-100">
              <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">STT</th>
              <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">LOẠI ẢNH</th>
              <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">ẢNH</th>
              <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">TẢI LÊN</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <td class="border border-gray-300 p-2 text-center">1</td>
              <td class="border border-gray-300 p-2">Ảnh Trước</td>
              <td class="border border-gray-300 p-2">
                <div class="flex justify-center">
                  <img
                    v-if="mainImages.front.previewUrl"
                    :src="mainImages.front.previewUrl"
                    alt="Front Image Preview"
                    class="w-16 h-16 object-cover rounded-lg border border-gray-300"
                  />
                </div>
              </td>
              <td class="border border-gray-300 p-2">
                <input
                  type="file"
                  accept="image/*"
                  @change="handleMainImageUpload($event, 'front')"
                  class="p-2 border border-gray-300 rounded-lg w-full"
                />
              </td>
            </tr>
            <tr>
              <td class="border border-gray-300 p-2 text-center">2</td>
              <td class="border border-gray-300 p-2">Ảnh Sau</td>
              <td class="border border-gray-300 p-2">
                <div class="flex justify-center">
                  <img
                    v-if="mainImages.back.previewUrl"
                    :src="mainImages.back.previewUrl"
                    alt="Back Image Preview"
                    class="w-16 h-16 object-cover rounded-lg border border-gray-300"
                  />
                </div>
              </td>
              <td class="border border-gray-300 p-2">
                <input
                  type="file"
                  accept="image/*"
                  @change="handleMainImageUpload($event, 'back')"
                  class="p-2 border border-gray-300 rounded-lg w-full"
                />
              </td>
            </tr>
            </tbody>
          </table>
        </div>

        <!-- Ảnh Theo Màu Sắc -->
        <div v-if="uniqueColors.length > 0">
          <h4 class="text-lg font-medium text-gray-600 mb-2">Ảnh Theo Màu Sắc</h4>
          <div v-for="(color, colorIndex) in uniqueColors" :key="color.colorId" class="mb-6">
            <h5 class="text-md font-medium text-gray-600 mb-2">
              MÀU: {{ color.colorName }}
            </h5>
            <table class="w-full border-collapse border border-gray-300">
              <thead>
              <tr class="bg-gray-100">
                <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">STT</th>
                <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">TÊN MÀU</th>
                <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">MÀU SẮC</th>
                <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">ẢNH</th>
                <th class="border border-gray-300 p-2 text-sm font-medium text-gray-700">TẢI LÊN</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td class="border border-gray-300 p-2 text-center">{{ colorIndex + 1 }}</td>
                <td class="border border-gray-300 p-2">
                  {{ color.colorName }}
                </td>
                <td class="border border-gray-300 p-2">
                  <div class="flex items-center justify-center">
                      <span
                        class="w-16 h-8 inline-block"
                        :style="{ backgroundColor: getColorFromName(color.colorName) || '#000' }"
                      ></span>
                  </div>
                </td>
                <td class="border border-gray-300 p-2">
                  <div class="flex justify-center">
                    <img
                      v-if="colorImages[color.colorId]?.previewUrl"
                      :src="colorImages[color.colorId].previewUrl"
                      alt="Color Image Preview"
                      class="w-16 h-16 object-cover rounded-lg border border-gray-300"
                    />
                  </div>
                </td>
                <td class="border border-gray-300 p-2">
                  <input
                    type="file"
                    accept="image/*"
                    @change="handleColorImageUpload($event, color.colorId)"
                    class="p-2 border border-gray-300 rounded-lg w-full"
                  />
                </td>
              </tr>
              </tbody>
            </table>
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
            class="px-4 py-2 bg-orange-500 text-white font-semibold rounded-lg hover:bg-orange-600 transition"
          >
            Lưu
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
              <input v-model="entityData.tenCongNgheMang" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên công nghệ mạng" />
            </div>
          </div>
          <div v-if="currentAttribute === 'congSac'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Cổng Sạc</label>
              <input v-model="entityData.congSac" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên cổng sạc" />
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
              <label class="block text-sm font-medium text-gray-700">Tên Chỉ Số Kháng Bụi Nước</label>
              <input v-model="entityData.tenChiSo" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên chỉ số kháng bụi nước" />
            </div>
          </div>
          <div v-if="currentAttribute === 'tinhTrang'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Tình Trạng</label>
              <input v-model="entityData.loaiTinhTrang" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên tình trạng" />
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
                placeholder="Nhập mã màu sắc (ví dụ: #000000 cho Đen)"
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
        </template>
      </FormModal>

      <!-- Color Selection Modal -->
      <div v-if="showColorModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg shadow-lg p-6 w-1/2">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-medium text-gray-700">Chọn màu sắc</h3>
            <button @click="closeColorModal" class="text-gray-500 hover:text-gray-700">
              ✕
            </button>
          </div>
          <div class="grid grid-cols-5 gap-4 mb-4">
            <label
              v-for="mau in mauSacOptions"
              :key="mau.id"
              class="flex items-center space-x-2"
            >
              <input
                type="checkbox"
                :value="mau.id"
                v-model="currentVariant.selectedMauSacs"
                class="form-checkbox h-5 w-5 text-blue-600"
              />
              <span
                class="w-8 h-8 rounded border border-gray-300"
                :style="{ backgroundColor: getColorFromName(mau.tenMau) || '#FFFFFF' }"
              ></span>
              <span class="text-sm text-gray-700">{{ mau.tenMau }}</span>
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
            <button @click="closeImeiModal" class="text-gray-500 hover:text-gray-700">
              ✕
            </button>
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">Nhập IMEI (mỗi IMEI trên một dòng)</label>
            <textarea
              v-model="imeiInput"
              rows="5"
              class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Nhập IMEI, mỗi IMEI trên một dòng..."
            ></textarea>
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">Hoặc nhập từ file Excel</label>
            <input
              type="file"
              accept=".xlsx, .xls"
              @change="handleExcelImport"
              class="w-full p-2 border border-gray-300 rounded-lg"
            />
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
    </div>
  </div>
</template>

<script>
import { defineComponent, computed, ref } from 'vue';
import ToastNotification from '@/components/ToastNotification.vue';
import FormModal from '@/components/FormModal.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import { ref as logicRef, onMounted, computed as logicComputed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';

const addProductLogic = () => {
  const router = useRouter();
  const route = useRoute();
  const toast = logicRef(null);
  const showFormModal = logicRef(false);
  const currentAttribute = logicRef('');
  const productData = logicRef({
    id: '',
    idHeDieuHanh: '',
    idManHinh: '',
    idNhaSanXuat: '',
    idCumCamera: '',
    idSim: '',
    idThietKe: '',
    idPin: '',
    idCpu: '',
    idGpu: '',
    idCongNgheMang: '',
    idCongSac: '',
    idHoTroCongNgheSac: '',
    idChiSoKhangBuiVaNuoc: '',
    idLoaiTinhTrang: '',
    tienIchDacBiet: '',
    giaBan: '',
  });

  const productVariants = logicRef([]);
  const currentVariant = logicRef({
    selectedRams: [],
    selectedBoNhoTrongs: [],
    selectedMauSacs: [],
  });

  const productImages = logicRef([]);
  const currentImage = logicRef({
    file: null,
    fileName: '',
  });

  const variantImages = logicRef({});

  const heDieuHanhOptions = logicRef([]);
  const manHinhOptions = logicRef([]);
  const nhaSanXuatOptions = logicRef([]);
  const cumCameraOptions = logicRef([]);
  const simOptions = logicRef([]);
  const thietKeOptions = logicRef([]);
  const pinOptions = logicRef([]);
  const cpuOptions = logicRef([]);
  const gpuOptions = logicRef([]);
  const congNgheMangOptions = logicRef([]);
  const congSacOptions = logicRef([]);
  const hoTroCongNgheSacOptions = logicRef([]);
  const chiSoKhangBuiVaNuocOptions = logicRef([]);
  const tinhTrangOptions = logicRef([]);
  const ramOptions = logicRef([]);
  const boNhoTrongOptions = logicRef([]);
  const mauSacOptions = logicRef([]);

  const dropdownOpen = logicRef({
    ram: false,
    boNhoTrong: false,
    mauSac: false,
  });

  const breadcrumbItems = logicComputed(() => {
    if (typeof route.meta.breadcrumb === "function") {
      return route.meta.breadcrumb(route);
    }
    return route.meta?.breadcrumb || ["Thêm Sản Phẩm"];
  });

  const fetchOptions = async () => {
    try {
      const [
        heDieuHanhRes,
        manHinhRes,
        nhaSanXuatRes,
        cumCameraRes,
        simRes,
        thietKeRes,
        pinRes,
        cpuRes,
        gpuRes,
        congNgheMangRes,
        congSacRes,
        hoTroCongNgheSacRes,
        chiSoKhangBuiVaNuocRes,
        tinhTrangRes,
        ramRes,
        boNhoTrongRes,
        mauSacRes,
      ] = await Promise.all([
        axios.get('http://localhost:8080/api/he-dieu-hanh'),
        axios.get('http://localhost:8080/api/man-hinh'),
        axios.get('http://localhost:8080/api/nha-san-xuat'),
        axios.get('http://localhost:8080/api/cum-camera/details'),
        axios.get('http://localhost:8080/api/sim'),
        axios.get('http://localhost:8080/api/thiet-ke'),
        axios.get('http://localhost:8080/api/pin'),
        axios.get('http://localhost:8080/api/cpu'),
        axios.get('http://localhost:8080/api/gpu'),
        axios.get('http://localhost:8080/api/cong-nghe-mang'),
        axios.get('http://localhost:8080/api/cong-sac'),
        axios.get('http://localhost:8080/api/ho-tro-cong-nghe-sac/details'),
        axios.get('http://localhost:8080/api/chi-so-khang-bui-va-nuoc'),
        axios.get('http://localhost:8080/api/tinh-trang'),
        axios.get('http://localhost:8080/api/ram'),
        axios.get('http://localhost:8080/api/bo-nho-trong'),
        axios.get('http://localhost:8080/api/mau-sac'),
      ]);

      heDieuHanhOptions.value = heDieuHanhRes.data.content;
      manHinhOptions.value = manHinhRes.data.content;
      nhaSanXuatOptions.value = nhaSanXuatRes.data.content;
      cumCameraOptions.value = cumCameraRes.data.content;
      simOptions.value = simRes.data.content;
      thietKeOptions.value = thietKeRes.data.content;
      pinOptions.value = pinRes.data.content;
      cpuOptions.value = cpuRes.data.content;
      gpuOptions.value = gpuRes.data.content;
      congNgheMangOptions.value = congNgheMangRes.data.content;
      congSacOptions.value = congSacRes.data.content;
      hoTroCongNgheSacOptions.value = hoTroCongNgheSacRes.data.content;
      chiSoKhangBuiVaNuocOptions.value = chiSoKhangBuiVaNuocRes.data.content;
      tinhTrangOptions.value = tinhTrangRes.data.content;
      ramOptions.value = ramRes.data.content;
      boNhoTrongOptions.value = boNhoTrongRes.data.content;
      mauSacOptions.value = mauSacRes.data.content;
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
      }
      console.error('Fetch options error:', error);
    }
  };

  const toggleDropdown = (type) => {
    dropdownOpen.value[type] = !dropdownOpen.value[type];
    Object.keys(dropdownOpen.value).forEach((key) => {
      if (key !== type) {
        dropdownOpen.value[key] = false;
      }
    });
  };

  const handleImageUpload = (event) => {
    currentImage.value.file = event.target.files[0];
    currentImage.value.fileName = event.target.files[0] ? event.target.files[0].name : 'No file chosen';
  };

  const handleVariantImageUpload = (event, index) => {
    const file = event.target.files[0];
    if (file) {
      variantImages.value[index] = {
        file,
        fileName: file.name,
      };
    }
  };

  const addVariant = () => {
    if (
      currentVariant.value.selectedRams.length === 0 ||
      currentVariant.value.selectedBoNhoTrongs.length === 0 ||
      currentVariant.value.selectedMauSacs.length === 0
    ) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Vui lòng chọn ít nhất một RAM, một Bộ Nhớ Trong và một Màu Sắc!');
      }
      return;
    }

    const newVariants = [];
    currentVariant.value.selectedRams.forEach((ramId) => {
      currentVariant.value.selectedBoNhoTrongs.forEach((boNhoId) => {
        currentVariant.value.selectedMauSacs.forEach((mauSacId) => {
          newVariants.push({
            idRam: ramId,
            idBoNhoTrong: boNhoId,
            idMauSac: mauSacId,
            soLuong: 0,
            donGia: productData.value.giaBan || '',
          });
        });
      });
    });

    productVariants.value.push(...newVariants);
    currentVariant.value = {
      selectedRams: [],
      selectedBoNhoTrongs: [],
      selectedMauSacs: [],
    };
  };

  const removeVariant = (index) => {
    productVariants.value.splice(index, 1);
    delete variantImages.value[index];
  };

  const addImage = () => {
    if (currentImage.value.file) {
      productImages.value.push({ ...currentImage.value });
      currentImage.value = { file: null, fileName: '' };
    } else {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Vui lòng chọn file ảnh!');
      }
    }
  };

  const handleSubmit = async () => {
    if (
      !productData.value.id ||
      !productData.value.giaBan ||
      Object.values(productData.value).some((val) => val === '')
    ) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Vui lòng nhập đầy đủ thông tin sản phẩm!');
      }
      return;
    }

    const formData = new FormData();
    formData.append('productData', JSON.stringify(productData.value));
    productVariants.value.forEach((variant, index) => {
      formData.append(`variants[${index}]`, JSON.stringify(variant));
      if (variantImages.value[index]?.file) {
        formData.append(`variantImages[${index}]`, variantImages.value[index].file);
      }
    });
    productImages.value.forEach((image, index) => {
      formData.append(`images[${index}][file]`, image.file);
    });

    try {
      await axios.post('http://localhost:8080/api/chi-tiet-san-pham', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      if (toast.value) {
        toast.value?.kshowToast('success', 'Thêm mới thành công!');
      }
      await router.push('/san-pham');
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi lưu dữ liệu: ' + (error.response?.data?.error || error.message));
      }
      console.error('Save error:', error);
    }
  };

  const resetForm = () => {
    productData.value = {
      id: '',
      idHeDieuHanh: '',
      idManHinh: '',
      idNhaSanXuat: '',
      idCumCamera: '',
      idSim: '',
      idThietKe: '',
      idPin: '',
      idCpu: '',
      idGpu: '',
      idCongNgheMang: '',
      idCongSac: '',
      idHoTroCongNgheSac: '',
      idChiSoKhangBuiVaNuoc: '',
      idLoaiTinhTrang: '',
      tienIchDacBiet: '',
      giaBan: '',
    };
    productVariants.value = [];
    productImages.value = [];
    variantImages.value = {};
    currentVariant.value = { selectedRams: [], selectedBoNhoTrongs: [], selectedMauSacs: [] };
    currentImage.value = { file: null, fileName: '' };
  };

  const openAddModal = (attribute) => {
    currentAttribute.value = attribute;
    showFormModal.value = true;
  };

  const handleAddAttribute = async (data) => {
    try {
      let response;
      switch (currentAttribute.value) {
        case 'id':
          break;
        case 'heDieuHanh':
          response = await axios.post('http://localhost:8080/api/he-dieu-hanhs', { tenHeDieuHanh: data.tenHeDieuHanh });
          heDieuHanhOptions.value.push(response.data);
          break;
        case 'manHinh':
          response = await axios.post('http://localhost:8080/api/man-hinhs', { kichThuoc: data.kichThuoc });
          manHinhOptions.value.push(response.data);
          break;
        case 'nhaSanXuat':
          response = await axios.post('http://localhost:8080/api/nha-san-xuats', { tenNhaSanXuat: data.tenNhaSanXuat });
          nhaSanXuatOptions.value.push(response.data);
          break;
        case 'cumCamera':
          response = await axios.post('http://localhost:8080/api/cum-cameras', { tenCamera: data.tenCamera });
          cumCameraOptions.value.push(response.data);
          break;
        case 'sim':
          response = await axios.post('http://localhost:8080/api/sims', { loaiSim: data.loaiSim });
          simOptions.value.push(response.data);
          break;
        case 'thietKe':
          response = await axios.post('http://localhost:8080/api/thiet-kes', { tenThietKe: data.tenThietKe });
          thietKeOptions.value.push(response.data);
          break;
        case 'pin':
          response = await axios.post('http://localhost:8080/api/pins', { dungLuong: data.dungLuong });
          pinOptions.value.push(response.data);
          break;
        case 'cpu':
          response = await axios.post('http://localhost:8080/api/cpus', { tenCpu: data.tenCpu });
          cpuOptions.value.push(response.data);
          break;
        case 'gpu':
          response = await axios.post('http://localhost:8080/api/gpus', { tenGpu: data.tenGpu });
          gpuOptions.value.push(response.data);
          break;
        case 'congNgheMang':
          response = await axios.post('http://localhost:8080/api/cong-nghe-mangs', { tenCongNgheMang: data.tenCongNgheMang });
          congNgheMangOptions.value.push(response.data);
          break;
        case 'congSac':
          response = await axios.post('http://localhost:8080/api/cong-sacs', { congSac: data.congSac });
          congSacOptions.value.push(response.data);
          break;
        case 'hoTroCongNgheSac':
          response = await axios.post('http://localhost:8080/api/ho-tro-cong-nghe-sacs', { ten: data.ten });
          hoTroCongNgheSacOptions.value.push(response.data);
          break;
        case 'chiSoKhangBuiVaNuoc':
          response = await axios.post('http://localhost:8080/api/chi-so-khang-bui-va-nuocs', { tenChiSo: data.tenChiSo });
          chiSoKhangBuiVaNuocOptions.value.push(response.data);
          break;
        case 'tinhTrang':
          response = await axios.post('http://localhost:8080/api/tinh-trangs', { loaiTinhTrang: data.loaiTinhTrang });
          tinhTrangOptions.value.push(response.data);
          break;
        case 'ram':
          response = await axios.post('http://localhost:8080/api/ram', { ma: data.ma, dungLuong: data.dungLuong });
          ramOptions.value.push(response.data);
          break;
        case 'boNhoTrong':
          response = await axios.post('http://localhost:8080/api/bo-nho-trong', { ma: data.ma, dungLuong: data.dungLuong });
          boNhoTrongOptions.value.push(response.data);
          break;
        case 'mauSac':
          response = await axios.post('http://localhost:8080/api/mau-sac', { ma: data.ma, tenMau: data.tenMau });
          mauSacOptions.value.push(response.data);
          break;
        case 'tienIchDacBiet':
          productData.value.tienIchDacBiet = data.tienIchDacBiet;
          break;
        case 'giaBan':
          productData.value.giaBan = data.giaBan;
          break;
      }
      if (toast.value) {
        toast.value?.kshowToast('success', `Thêm ${currentAttribute.value} thành công!`);
      }
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi thêm thuộc tính: ' + (error.response?.data?.error || error.message));
      }
      console.error('Add attribute error:', error);
    }
    closeFormModal();
  };

  const closeFormModal = () => {
    showFormModal.value = false;
    currentAttribute.value = '';
  };

  onMounted(() => {
    fetchOptions();
  });

  return {
    toast,
    showFormModal,
    currentAttribute,
    productData,
    productVariants,
    currentVariant,
    productImages,
    currentImage,
    variantImages,
    heDieuHanhOptions,
    manHinhOptions,
    nhaSanXuatOptions,
    cumCameraOptions,
    simOptions,
    thietKeOptions,
    pinOptions,
    cpuOptions,
    gpuOptions,
    congNgheMangOptions,
    congSacOptions,
    hoTroCongNgheSacOptions,
    chiSoKhangBuiVaNuocOptions,
    tinhTrangOptions,
    ramOptions,
    boNhoTrongOptions,
    mauSacOptions,
    breadcrumbItems,
    dropdownOpen,
    fetchOptions,
    toggleDropdown,
    handleImageUpload,
    handleVariantImageUpload,
    addVariant,
    removeVariant,
    addImage,
    handleSubmit,
    resetForm,
    openAddModal,
    handleAddAttribute,
    closeFormModal,
  };
};

export default defineComponent({
  name: 'AddProduct',
  components: {
    ToastNotification,
    FormModal,
    BreadcrumbWrapper,
  },
  setup() {
    const logic = addProductLogic();

    // State for color selection modal
    const showColorModal = ref(false);
    const showImeiModal = ref(false);
    const showImageSection = ref(false);
    const imeiInput = ref('');
    const currentVariantIndex = ref(null);
    const variantImeis = ref({});
    const mainImages = ref({
      front: { file: null, fileName: '', previewUrl: '' },
      back: { file: null, fileName: '', previewUrl: '' },
    });
    const colorImages = ref({});

    // New state for common inputs and selection
    const commonPrice = ref('');
    const commonQuantity = ref('');
    const selectedVariants = ref([]);

    const openColorModal = () => {
      showColorModal.value = true;
    };

    const closeColorModal = () => {
      showColorModal.value = false;
    };

    const confirmColorSelection = () => {
      logic.addVariant();
      showImageSection.value = true;
      closeColorModal();
    };

    const openImeiModal = (index) => {
      currentVariantIndex.value = index;
      imeiInput.value = variantImeis.value[index]?.join('\n') || '';
      showImeiModal.value = true;
    };

    const closeImeiModal = () => {
      showImeiModal.value = false;
      imeiInput.value = '';
      currentVariantIndex.value = null;
    };

    const saveImei = () => {
      const imeis = imeiInput.value
        .split('\n')
        .map(imei => imei.trim())
        .filter(imei => imei.length > 0);
      variantImeis.value[currentVariantIndex.value] = imeis;
      closeImeiModal();
    };

    const handleExcelImport = (event) => {
      const file = event.target.files[0];
      if (!file) return;
      const mockImeis = ['123456789012345', '678901234567890'];
      imeiInput.value = mockImeis.join('\n');
    };

    const vietnameseToEnglishColorMap = {
      'đen': 'black',
      'bạc': 'silver',
      'cam': 'orange',
      'đỏ': 'red',
      'vàng': 'yellow',
      'gold': 'gold',
      'xanh': 'green',
      'trắng': 'white',
      'hồng': 'pink',
      'tím': 'purple',
      'xám': 'gray',
      'nâu': 'brown',
      'lam': 'blue',
      'xanh lam': 'blue',
      'xanh lá': 'green',
      'xanh dương': 'blue',
    };

    const colorNameToHex = {
      'black': '#000000',
      'silver': '#C0C0C0',
      'orange': '#FFA500',
      'red': '#FF0000',
      'yellow': '#FFFF00',
      'gold': '#FFD700',
      'green': '#008000',
      'white': '#FFFFFF',
      'pink': '#FF69B4',
      'purple': '#800080',
      'gray': '#808080',
      'brown': '#A52A2A',
      'blue': '#0000FF',
    };

    const hashStringToColor = (str) => {
      let hash = 0;
      for (let i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash);
      }
      const r = (hash & 0xFF0000) >> 16;
      const g = (hash & 0x00FF00) >> 8;
      const b = hash & 0x0000FF;
      return `#${((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1).padStart(6, '0')}`;
    };

    const getColorFromName = (colorName) => {
      if (!colorName) return '#FFFFFF';
      const normalizedName = colorName.toLowerCase().trim();
      const englishColorName = vietnameseToEnglishColorMap[normalizedName] || normalizedName;
      if (colorNameToHex[englishColorName]) {
        return colorNameToHex[englishColorName];
      }
      return hashStringToColor(colorName);
    };

    const handleMainImageUpload = (event, type) => {
      const file = event.target.files[0];
      if (file) {
        if (mainImages.value[type].previewUrl) {
          URL.revokeObjectURL(mainImages.value[type].previewUrl);
        }
        mainImages.value[type] = {
          file,
          fileName: file.name,
          previewUrl: URL.createObjectURL(file),
        };
      }
    };

    const handleColorImageUpload = (event, colorId) => {
      const file = event.target.files[0];
      if (file) {
        if (colorImages.value[colorId]?.previewUrl) {
          URL.revokeObjectURL(colorImages.value[colorId].previewUrl);
        }
        colorImages.value[colorId] = {
          file,
          fileName: file.name,
          previewUrl: URL.createObjectURL(file),
        };
      }
    };

    const resetForm = () => {
      Object.values(mainImages.value).forEach(image => {
        if (image.previewUrl) {
          URL.revokeObjectURL(image.previewUrl);
        }
      });
      mainImages.value = {
        front: { file: null, fileName: '', previewUrl: '' },
        back: { file: null, fileName: '', previewUrl: '' },
      };

      Object.values(colorImages.value).forEach(image => {
        if (image?.previewUrl) {
          URL.revokeObjectURL(image.previewUrl);
        }
      });
      colorImages.value = {};

      logic.resetForm();
      showImageSection.value = false;
      commonPrice.value = '';
      commonQuantity.value = '';
      selectedVariants.value = [];
    };

    const updateSelectedVariants = () => {
      selectedVariants.value.forEach(index => {
        if (logic.productVariants.value[index]) {
          logic.productVariants.value[index].donGia = commonPrice.value;
          logic.productVariants.value[index].soLuong = commonQuantity.value;
        }
      });
    };

    const toggleGroupSelection = (group, isChecked) => {
      const groupIndices = group.variants.map((_, i) => group.startIndex + i);
      if (isChecked) {
        selectedVariants.value = [...new Set([...selectedVariants.value, ...groupIndices])];
      } else {
        selectedVariants.value = selectedVariants.value.filter(index => !groupIndices.includes(index));
      }
      updateSelectedVariants();
    };

    const updateSelectedCount = () => {
      selectedVariants.value = selectedVariants.value.filter(index =>
        index >= 0 && index < logic.productVariants.value.length
      );
      updateSelectedVariants();
    };

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

    const uniqueColors = computed(() => {
      const seen = new Set();
      const colors = [];

      logic.productVariants.value.forEach(variant => {
        const colorId = variant.idMauSac;
        const colorName = logic.mauSacOptions.value.find(mau => mau.id === colorId)?.tenMau || 'N/A';

        if (!seen.has(colorId)) {
          seen.add(colorId);
          colors.push({
            colorId,
            colorName,
          });
        }
      });

      return colors;
    });

    return {
      ...logic,
      groupVariantsByRamAndRom,
      uniqueColors,
      showColorModal,
      openColorModal,
      closeColorModal,
      confirmColorSelection,
      showImeiModal,
      openImeiModal,
      closeImeiModal,
      saveImei,
      imeiInput,
      handleExcelImport,
      getColorFromName,
      showImageSection,
      mainImages,
      handleMainImageUpload,
      colorImages,
      handleColorImageUpload,
      resetForm,
      commonPrice,
      commonQuantity,
      selectedVariants,
      updateSelectedVariants,
      toggleGroupSelection,
      updateSelectedCount,
    };
  },
});
</script>