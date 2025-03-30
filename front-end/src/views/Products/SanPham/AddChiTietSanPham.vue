<template>
  <div>
    <!-- Thêm BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems"/>

    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast"/>

      <!-- Phần Thêm Sản Phẩm -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-4">
        <h3 class="text-lg font-medium text-gray-600 mb-4 text-center">THÊM SẢN PHẨM</h3>
        <div class="grid grid-cols-1 gap-6">
          <div class="flex items-center">
            <label class="w-40 text-sm font-medium text-gray-700">Tên Sản Phẩm</label>
            <input
              v-model="productData.tenSanPham"
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
                <option v-for="he in heDieuHanhOptions" :key="he.id" :value="he.id">{{
                    he.heDieuHanh + " " + he.phienBan
                  }}
                </option>
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
                <option v-for="man in manHinhOptions" :key="man.id" :value="man.id">
                  {{ man.kichThuoc + " " + man.doPhanGiai }}
                </option>
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
                <option v-for="sim in simOptions" :key="sim.id" :value="sim.id">
                  {{ sim.cacLoaiSimHoTro + " " + "(hỗ trợ " + sim.soLuongSimHoTro + " sim)" }}
                </option>
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
                <option v-for="thietKe in thietKeOptions" :key="thietKe.id" :value="thietKe.id">
                  {{ thietKe.chatLieuKhung + " " + "(" + thietKe.chatLieuMatLung + ")" }}
                </option>
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
                <option v-for="pin in pinOptions" :key="pin.id" :value="pin.id">
                  {{ pin.loaiPin + " " + "(" + pin.dungLuongPin + ")" }}
                </option>
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
                <option v-for="cpu in cpuOptions" :key="cpu.id" :value="cpu.id">
                  {{ cpu.tenCpu + " " + "(" + cpu.soNhan + " lõi)" }}
                </option>
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
                <option v-for="congNghe in congNgheMangOptions" :key="congNghe.id" :value="congNghe.id">
                  {{ congNghe.tenCongNgheMang }}
                </option>
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
                <option v-for="congSac in congSacOptions" :key="congSac.id" :value="congSac.id">{{
                    congSac.congSac
                  }}
                </option>
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
                <option v-for="chiSo in chiSoKhangBuiVaNuocOptions" :key="chiSo.id" :value="chiSo.id">{{
                    chiSo.tenChiSo
                  }}
                </option>
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
                <option v-for="tinhTrang in tinhTrangOptions" :key="tinhTrang.id" :value="tinhTrang.id">
                  {{ tinhTrang.loaiTinhTrang }}
                </option>
              </select>
              <button
                @click="openAddModal('tinhTrang')"
                class="ml-2 px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
              >
                +
              </button>
            </div>
          </div>

          <!-- Tiện Ích Đặc Biệt-->
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
                {{
                  currentVariant.selectedRams.length > 0 ? currentVariant.selectedRams.length + ' RAM đã chọn' : 'Chọn RAM'
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
            <div class="w-3/5 relative">
              <button
                @click="toggleDropdown('boNhoTrong')"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-left"
              >
                {{
                  currentVariant.selectedBoNhoTrongs.length > 0 ? currentVariant.selectedBoNhoTrongs.length + ' ROM đã chọn' : 'Chọn Bộ Nhớ Trong'
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
            <div class="w-3/5 relative">
              <button
                @click="openColorModal"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-left"
              >
                {{
                  currentVariant.selectedMauSacs.length > 0 ? currentVariant.selectedMauSacs.length + ' Màu đã chọn' : 'Chọn Màu Sắc'
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
          <div v-for="(group, groupIndex) in groupVariantsByRamAndRom" :key="groupIndex">
            <div class="flex justify-between m-2 items-center">
              <div class="flex items-center">
                <input
                  type="checkbox"
                  v-model="allSelected[group.groupKey]"
                  @change="toggleAllVariants(group, $event.target.checked)"
                  class="mr-2"
                />
                <h4 class="text-lg font-medium text-gray-600">
                  PHIÊN BẢN {{ group.ram }}/{{ group.rom }}
                </h4>
              </div>
              <div class="flex gap-2">
                <!-- Giá Chung cho nhóm -->
                <div class="flex items-center">
                  <label class="w-36 text-sm font-medium text-gray-700">Giá Chung</label>
                  <input
                    v-model="groupCommonValues[group.groupKey].price"
                    type="text"
                    placeholder="Nhập giá chung"
                    class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    @input="updateSelectedVariants(group)"
                  />
                </div>

                <!-- Số Lượng Chung cho nhóm -->
                <div class="flex items-center">
                  <label class="w-36 text-sm font-medium text-gray-700">Số Lượng Chung</label>
                  <input
                    v-model="groupCommonValues[group.groupKey].quantity"
                    type="number"
                    min="0"
                    placeholder="Nhập số lượng chung"
                    class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    @input="updateSelectedVariants(group)"
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
                    v-model="allSelected[group.groupKey]"
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
            <h5 class="text-md font-medium text-gray-600 mb-2">MÀU: {{ color.colorName }}</h5>
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
                <td class="border border-gray-300 p-2">{{ color.colorName }}</td>
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
          <div v-if="currentAttribute === 'heDieuHanh'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Hệ Điều Hành</label>
              <input
                v-model="entityData.heDieuHanh"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập tên hệ điều hành"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Phiên Bản</label>
              <input
                v-model="entityData.phienBan"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập phiên bản"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'manHinh'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Kích Thước Màn Hình</label>
              <input
                v-model="entityData.kichThuoc"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập kích thước màn hình"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Độ Phân Giải</label>
              <input
                v-model="entityData.doPhanGiai"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập thông số camera trước"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Camera Sau</label>
              <input
                v-model="entityData.cameraSau"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập loại sim"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Số Lượng Sim Hỗ Trợ</label>
              <input
                v-model="entityData.soLuongSimHoTro"
                type="number"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập chất liệu khung"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Chất Liệu Mặt Lưng</label>
              <input
                v-model="entityData.chatLieuMatLung"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập loại pin"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Dung Lượng Pin</label>
              <input
                v-model="entityData.dungLuongPin"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập tên CPU"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Số Nhân</label>
              <input
                v-model="entityData.soNhan"
                type="number"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập tên công nghệ mạng"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'congSac'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Cổng Sạc</label>
              <input
                v-model="entityData.congSac"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập tên cổng sạc"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'hoTroCongNgheSac'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Hỗ Trợ Công Nghệ Sạc</label>
              <input
                v-model="entityData.tenCongNgheSac"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập tên chỉ số kháng bụi nước"
              />
            </div>
          </div>
          <div v-if="currentAttribute === 'tinhTrang'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Tình Trạng</label>
              <input
                v-model="entityData.loaiTinhTrang"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập tên tình trạng"
              />
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
        </template>
      </FormModal>

      <!-- Color Selection Modal -->
      <div v-if="showColorModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg shadow-lg p-6 w-1/2">
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
            <button @click="closeImeiModal" class="text-gray-500 hover:text-gray-700">✕</button>
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

export default defineComponent({
  name: 'AddProduct',
  components: {
    ToastNotification,
    FormModal,
    BreadcrumbWrapper,
  },
  setup() {
    const router = useRouter();
    const route = useRoute();
    const toast = logicRef(null);
    const showFormModal = logicRef(false);
    const currentAttribute = logicRef('');
    const productData = ref({
      tenSanPham: '',
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
      createdBy: 'admin', // Thay bằng ID người dùng thực tế
      updatedBy: 'admin'  // Thay bằng ID người dùng thực tế
    });

    const productVariants = logicRef([]);
    const currentVariant = logicRef({
      selectedRams: [],
      selectedBoNhoTrongs: [],
      selectedMauSacs: [],
    });

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
      if (typeof route.meta.breadcrumb === 'function') {
        return route.meta.breadcrumb(route);
      }
      return route.meta?.breadcrumb || ['Thêm Sản Phẩm'];
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
          axios.get('http://localhost:8080/he-dieu-hanh'),
          axios.get('http://localhost:8080/man-hinh'),
          axios.get('http://localhost:8080/nha-san-xuat'),
          axios.get('http://localhost:8080/cum-camera/details'),
          axios.get('http://localhost:8080/sim'),
          axios.get('http://localhost:8080/thiet-ke'),
          axios.get('http://localhost:8080/pin'),
          axios.get('http://localhost:8080/cpu'),
          axios.get('http://localhost:8080/gpu'),
          axios.get('http://localhost:8080/cong-nghe-mang'),
          axios.get('http://localhost:8080/cong-sac'),
          axios.get('http://localhost:8080/ho-tro-cong-nghe-sac/details'),
          axios.get('http://localhost:8080/chi-so-khang-bui-va-nuoc'),
          axios.get('http://localhost:8080/tinh-trang'),
          axios.get('http://localhost:8080/ram'),
          axios.get('http://localhost:8080/bo-nho-trong'),
          axios.get('http://localhost:8080/mau-sac'),
        ]);

        heDieuHanhOptions.value = heDieuHanhRes.data.content || [];
        manHinhOptions.value = manHinhRes.data.content || [];
        nhaSanXuatOptions.value = nhaSanXuatRes.data.content || [];
        cumCameraOptions.value = cumCameraRes.data.content || [];
        simOptions.value = simRes.data.content || [];
        thietKeOptions.value = thietKeRes.data.content || [];
        pinOptions.value = pinRes.data.content || [];
        cpuOptions.value = cpuRes.data.content || [];
        gpuOptions.value = gpuRes.data.content || [];
        congNgheMangOptions.value = congNgheMangRes.data.content || [];
        congSacOptions.value = congSacRes.data.content || [];
        hoTroCongNgheSacOptions.value = hoTroCongNgheSacRes.data.content || [];
        chiSoKhangBuiVaNuocOptions.value = chiSoKhangBuiVaNuocRes.data.content || [];
        tinhTrangOptions.value = tinhTrangRes.data.content || [];
        ramOptions.value = ramRes.data.content || [];
        boNhoTrongOptions.value = boNhoTrongRes.data.content || [];
        mauSacOptions.value = mauSacRes.data.content || [];
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
              idImel: 1,
              idLoaiTinhTrang: productData.value.idLoaiTinhTrang,
              soLuong: 0,
              donGia: '', // Để trống, người dùng sẽ nhập trong bảng biến thể
              imageIndex: null,
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
    };

    const showColorModal = ref(false);
    const showImeiModal = ref(false);
    const showImageSection = ref(false);
    const imeiInput = ref('');
    const currentVariantIndex = ref(null);
    const variantImeis = ref({});
    const mainImages = ref({
      front: {file: null, fileName: '', previewUrl: ''},
      back: {file: null, fileName: '', previewUrl: ''},
    });
    const colorImages = ref({});
    const selectedVariants = ref([]);
    const allSelected = ref({});
    const groupCommonValues = ref({});

    const openAddModal = (attribute) => {
      currentAttribute.value = attribute;
      showFormModal.value = true;
    };

    const closeFormModal = () => {
      showFormModal.value = false;
      currentAttribute.value = '';
    };

    const openColorModal = () => {
      showColorModal.value = true;
    };

    const closeColorModal = () => {
      showColorModal.value = false;
    };

    const confirmColorSelection = () => {
      addVariant();
      showImageSection.value = true;
      closeColorModal();

      groupVariantsByRamAndRom.value.forEach((group) => {
        const groupKey = `${group.ram}/${group.rom}`;
        if (!(groupKey in allSelected.value)) {
          allSelected.value[groupKey] = true;
          groupCommonValues.value[groupKey] = {price: '', quantity: ''};
          toggleAllVariants(group, true);
        }
      });
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
        .map((imei) => imei.trim())
        .filter((imei) => imei.length > 0);
      variantImeis.value[currentVariantIndex.value] = imeis;
      closeImeiModal();
    };

    const handleExcelImport = (event) => {
      const file = event.target.files[0];
      if (!file) return;
      // Giả lập đọc file Excel (cần thư viện như XLSX để xử lý thực tế)
      const mockImeis = ['123456789012345', '678901234567890'];
      imeiInput.value = mockImeis.join('\n');
    };

    const getColorFromName = (colorName) => {
      const vietnameseToEnglishColorMap = {
        đen: 'black',
        bạc: 'silver',
        cam: 'orange',
        đỏ: 'red',
        vàng: 'yellow',
        gold: 'gold',
        xanh: 'green',
        trắng: 'white',
        hồng: 'pink',
        tím: 'purple',
        xám: 'gray',
        nâu: 'brown',
        lam: 'blue',
        'xanh lam': 'blue',
        'xanh lá': 'green',
        'xanh dương': 'blue',
      };
      const colorNameToHex = {
        black: '#000000',
        silver: '#C0C0C0',
        orange: '#FFA500',
        red: '#FF0000',
        yellow: '#FFFF00',
        gold: '#FFD700',
        green: '#008000',
        white: '#FFFFFF',
        pink: '#FF69B4',
        purple: '#800080',
        gray: '#808080',
        brown: '#A52A2A',
        blue: '#0000FF',
      };
      if (!colorName) return '#FFFFFF';
      const normalizedName = colorName.toLowerCase().trim();
      const englishColorName = vietnameseToEnglishColorMap[normalizedName] || normalizedName;
      return colorNameToHex[englishColorName] || '#FFFFFF';
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

    const handleSubmit = async () => {
      // Validate product data
      const requiredFields = [
        'tenSanPham', 'idHeDieuHanh', 'idManHinh', 'idNhaSanXuat',
        'idCumCamera', 'idSim', 'idThietKe', 'idPin', 'idCpu',
        'idGpu', 'idCongNgheMang', 'idCongSac', 'idHoTroCongNgheSac',
        'idLoaiTinhTrang'
      ];

      const missingFields = requiredFields.filter(field => !productData.value[field]);

      if (missingFields.length > 0) {
        if (toast.value) {
          toast.value?.kshowToast('error', `Vui lòng nhập đầy đủ thông tin. Thiếu các trường: ${missingFields.join(', ')}`);
        }
        return;
      }

      // Validate variants
      if (productVariants.value.length === 0) {
        if (toast.value) {
          toast.value?.kshowToast('error', 'Vui lòng thêm ít nhất một biến thể sản phẩm');
        }
        return;
      }

      // Validate images
      const allImages = [];
      if (mainImages.value.front.file) allImages.push(mainImages.value.front.file);
      if (mainImages.value.back.file) allImages.push(mainImages.value.back.file);
      Object.values(colorImages.value).forEach((image) => {
        if (image.file) allImages.push(image.file);
      });

      if (allImages.length === 0) {
        if (toast.value) {
          toast.value?.kshowToast('error', 'Vui lòng tải lên ít nhất một ảnh');
        }
        return;
      }

      // Prepare variants with image indexes
      productVariants.value.forEach((variant, index) => {
        const colorImage = colorImages.value[variant.idMauSac];
        if (colorImage && colorImage.file) {
          variant.imageIndex = allImages.indexOf(colorImage.file);
        } else {
          variant.imageIndex = 0; // Default to first image
        }
      });

      // Prepare FormData
      const formData = new FormData();

      // Add product data as JSON
      formData.append('dto', JSON.stringify({
        tenSanPham: productData.value.tenSanPham,
        idNhaSanXuat: productData.value.idNhaSanXuat,
        idPin: productData.value.idPin,
        idManHinh: productData.value.idManHinh,
        idCpu: productData.value.idCpu,
        idGpu: productData.value.idGpu,
        idCumCamera: productData.value.idCumCamera,
        idHeDieuHanh: productData.value.idHeDieuHanh,
        idThietKe: productData.value.idThietKe,
        idSim: productData.value.idSim,
        idCongSac: productData.value.idCongSac,
        idHoTroCongNgheSac: productData.value.idHoTroCongNgheSac,
        idCongNgheMang: productData.value.idCongNgheMang,
        idChiSoKhangBuiVaNuoc: productData.value.idChiSoKhangBuiVaNuoc || null,
        tienIchDacBiet: productData.value.tienIchDacBiet,
        giaBan: productVariants.value[0].donGia, // Use price from first variant
        createdAt: new Date().toISOString(),
        createdBy: 1, // Replace with actual user ID
        updatedAt: new Date().toISOString(),
        updatedBy: 1, // Replace with actual user ID
        variants: productVariants.value.map(variant => ({
          idImel: variant.idImel || 1, // Default IMEL if not provided
          idMauSac: variant.idMauSac,
          idRam: variant.idRam,
          idBoNhoTrong: variant.idBoNhoTrong,
          idLoaiTinhTrang: variant.idLoaiTinhTrang,
          imageIndex: variant.imageIndex,
          soLuong: variant.soLuong,
          donGia: variant.donGia
        }))
      }));

      // Add images
      allImages.forEach((image) => {
        formData.append('images', image);
      });

      try {
        const response = await axios.post('http://localhost:8080/chi-tiet-san-pham', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        if (response.data) {
          const { sanPhamId, chiTietSanPhamIds, anhSanPhamIds } = response.data.data;
          if (toast.value) {
            toast.value?.kshowToast(
              'success',
              `Thêm sản phẩm thành công! ID: ${sanPhamId}, ${chiTietSanPhamIds.length} biến thể, ${anhSanPhamIds.length} ảnh`
            );
          }
          await router.push('/products');
        }
      } catch (error) {
        console.error('Error details:', {
          message: error.message,
          response: error.response?.data,
          stack: error.stack
        });

        let errorMessage = 'Lỗi khi lưu dữ liệu';
        if (error.response) {
          if (error.response.data?.message) {
            errorMessage += ': ' + error.response.data.message;
          } else if (error.response.data?.error) {
            errorMessage += ': ' + error.response.data.error;
          }
        } else {
          errorMessage += ': ' + error.message;
        }

        if (toast.value) {
          toast.value?.kshowToast('error', errorMessage);
        }
      }
    };

    const resetForm = () => {
      Object.values(mainImages.value).forEach((image) => {
        if (image.previewUrl) {
          URL.revokeObjectURL(image.previewUrl);
        }
      });
      mainImages.value = {
        front: { file: null, fileName: '', previewUrl: '' },
        back: { file: null, fileName: '', previewUrl: '' },
      };
      Object.values(colorImages.value).forEach((image) => {
        if (image?.previewUrl) {
          URL.revokeObjectURL(image.previewUrl);
        }
      });
      colorImages.value = {};
      productData.value = {
        tenSanPham: '',
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
        createdBy: 'admin',
        updatedBy: 'admin',
        updatedAt: new Date().toISOString(),
      };
      productVariants.value = [];
      currentVariant.value = { selectedRams: [], selectedBoNhoTrongs: [], selectedMauSacs: [] };
      showImageSection.value = false;
      selectedVariants.value = [];
      allSelected.value = {};
      groupCommonValues.value = {};
    };

    const updateSelectedVariants = (group) => {
      const groupKey = `${group.ram}/${group.rom}`;
      const commonPrice = groupCommonValues.value[groupKey].price;
      const commonQuantity = groupCommonValues.value[groupKey].quantity;

      const groupIndices = group.variants.map((_, i) => group.startIndex + i);
      const selectedInGroup = selectedVariants.value.filter((index) => groupIndices.includes(index));
      if (selectedInGroup.length === 0 && group.variants.length > 0) {
        selectedVariants.value = [...new Set([...selectedVariants.value, ...groupIndices])];
        if (toast.value) {
          toast.value.kshowToast('info', `Không có biến thể nào được chọn trong ${groupKey}, áp dụng cho tất cả biến thể trong nhóm.`);
        }
      }

      selectedVariants.value.forEach((index) => {
        if (index >= group.startIndex && index < group.startIndex + group.variants.length) {
          if (productVariants.value[index]) {
            if (commonPrice !== '') {
              productVariants.value[index].donGia = commonPrice;
            }
            if (commonQuantity !== '') {
              productVariants.value[index].soLuong = commonQuantity;
            }
          }
        }
      });
    };

    const toggleGroupSelection = (group, isChecked) => {
      const groupIndices = group.variants.map((_, i) => group.startIndex + i);
      if (isChecked) {
        selectedVariants.value = [...new Set([...selectedVariants.value, ...groupIndices])];
      } else {
        selectedVariants.value = selectedVariants.value.filter((index) => !groupIndices.includes(index));
      }
      const groupKey = `${group.ram}/${group.rom}`;
      allSelected.value[groupKey] = isChecked;
      updateSelectedVariants(group);
    };

    const toggleAllVariants = (group, isChecked) => {
      const groupKey = `${group.ram}/${group.rom}`;
      allSelected.value[groupKey] = isChecked;
      toggleGroupSelection(group, isChecked);
    };

    const updateSelectedCount = (group) => {
      selectedVariants.value = selectedVariants.value.filter((index) => index >= 0 && index < productVariants.value.length);
      const groupIndices = group.variants.map((_, i) => group.startIndex + i);
      const selectedInGroup = selectedVariants.value.filter((index) => groupIndices.includes(index));
      const groupKey = `${group.ram}/${group.rom}`;
      allSelected.value[groupKey] = selectedInGroup.length === group.variants.length;
      updateSelectedVariants(group);
    };

    const groupVariantsByRamAndRom = computed(() => {
      const grouped = [];
      const seen = new Set();
      let startIndex = 0;

      productVariants.value.forEach((variant, index) => {
        const ram = ramOptions.value.find((r) => r.id === variant.idRam)?.dungLuong || 'N/A';
        const rom = boNhoTrongOptions.value.find((b) => b.id === variant.idBoNhoTrong)?.dungLuong || 'N/A';
        const key = `${ram}/${rom}`;

        if (!seen.has(key)) {
          seen.add(key);
          const variantsInGroup = productVariants.value.filter((v) => {
            const vRam = ramOptions.value.find((r) => r.id === v.idRam)?.dungLuong || 'N/A';
            const vRom = boNhoTrongOptions.value.find((b) => b.id === v.idBoNhoTrong)?.dungLuong || 'N/A';
            return `${vRam}/${vRom}` === key;
          });

          grouped.push({
            ram,
            rom,
            groupKey: key,
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

      productVariants.value.forEach((variant) => {
        const colorId = variant.idMauSac;
        const colorName = mauSacOptions.value.find((mau) => mau.id === colorId)?.tenMau || 'N/A';

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

    const handleAddAttribute = async (data) => {
      try {
        let response;
        switch (currentAttribute.value) {
          case 'heDieuHanh':
            response = await axios.post('http://localhost:8080/he-dieu-hanh', {
              heDieuHanh: data.heDieuHanh,
              phienBan: data.phienBan,
            });
            heDieuHanhOptions.value.push(response.data);
            productData.value.idHeDieuHanh = response.data.id;
            break;
          case 'manHinh':
            response = await axios.post('http://localhost:8080/man-hinh', {
              kichThuoc: data.kichThuoc,
              doPhanGiai: data.doPhanGiai,
            });
            manHinhOptions.value.push(response.data);
            productData.value.idManHinh = response.data.id;
            break;
          case 'nhaSanXuat':
            response = await axios.post('http://localhost:8080/nha-san-xuat', {
              nhaSanXuat: data.nhaSanXuat,
            });
            nhaSanXuatOptions.value.push(response.data);
            productData.value.idNhaSanXuat = response.data.id;
            break;
          case 'cumCamera':
            response = await axios.post('http://localhost:8080/cum-camera', {
              cameraTruoc: data.cameraTruoc,
              cameraSau: data.cameraSau,
            });
            cumCameraOptions.value.push(response.data);
            productData.value.idCumCamera = response.data.id;
            break;
          case 'sim':
            response = await axios.post('http://localhost:8080/sim', {
              cacLoaiSimHoTro: data.cacLoaiSimHoTro,
              soLuongSimHoTro: data.soLuongSimHoTro,
            });
            simOptions.value.push(response.data);
            productData.value.idSim = response.data.id;
            break;
          case 'thietKe':
            response = await axios.post('http://localhost:8080/thiet-ke', {
              chatLieuKhung: data.chatLieuKhung,
              chatLieuMatLung: data.chatLieuMatLung,
            });
            thietKeOptions.value.push(response.data);
            productData.value.idThietKe = response.data.id;
            break;
          case 'pin':
            response = await axios.post('http://localhost:8080/pin', {
              loaiPin: data.loaiPin,
              dungLuongPin: data.dungLuongPin,
            });
            pinOptions.value.push(response.data);
            productData.value.idPin = response.data.id;
            break;
          case 'cpu':
            response = await axios.post('http://localhost:8080/cpu', {
              tenCpu: data.tenCpu,
              soNhan: data.soNhan,
            });
            cpuOptions.value.push(response.data);
            productData.value.idCpu = response.data.id;
            break;
          case 'gpu':
            response = await axios.post('http://localhost:8080/gpu', {
              tenGpu: data.tenGpu,
            });
            gpuOptions.value.push(response.data);
            productData.value.idGpu = response.data.id;
            break;
          case 'congNgheMang':
            response = await axios.post('http://localhost:8080/cong-nghe-mang', {
              tenCongNgheMang: data.tenCongNgheMang,
            });
            congNgheMangOptions.value.push(response.data);
            productData.value.idCongNgheMang = response.data.id;
            break;
          case 'congSac':
            response = await axios.post('http://localhost:8080/cong-sac', {
              congSac: data.congSac,
            });
            congSacOptions.value.push(response.data);
            productData.value.idCongSac = response.data.id;
            break;
          case 'hoTroCongNgheSac':
            response = await axios.post('http://localhost:8080/ho-tro-cong-nghe-sac', {
              tenCongNgheSac: data.tenCongNgheSac,
            });
            hoTroCongNgheSacOptions.value.push(response.data);
            productData.value.idHoTroCongNgheSac = response.data.id;
            break;
          case 'chiSoKhangBuiVaNuoc':
            response = await axios.post('http://localhost:8080/chi-so-khang-bui-va-nuoc', {
              tenChiSo: data.tenChiSo,
            });
            chiSoKhangBuiVaNuocOptions.value.push(response.data);
            productData.value.idChiSoKhangBuiVaNuoc = response.data.id;
            break;
          case 'tinhTrang':
            response = await axios.post('http://localhost:8080/tinh-trang', {
              loaiTinhTrang: data.loaiTinhTrang,
            });
            tinhTrangOptions.value.push(response.data);
            productData.value.idLoaiTinhTrang = response.data.id;
            break;
          case 'ram':
            response = await axios.post('http://localhost:8080/ram', {
              ma: data.ma,
              dungLuong: data.dungLuong,
            });
            ramOptions.value.push(response.data);
            currentVariant.value.selectedRams.push(response.data.id);
            break;
          case 'boNhoTrong':
            response = await axios.post('http://localhost:8080/bo-nho-trong', {
              ma: data.ma,
              dungLuong: data.dungLuong,
            });
            boNhoTrongOptions.value.push(response.data);
            currentVariant.value.selectedBoNhoTrongs.push(response.data.id);
            break;
          case 'mauSac':
            response = await axios.post('http://localhost:8080/mau-sac', {
              ma: data.ma,
              tenMau: data.tenMau,
            });
            mauSacOptions.value.push(response.data);
            currentVariant.value.selectedMauSacs.push(response.data.id);
            break;
          default:
            console.warn('Unknown attribute:', currentAttribute.value);
            return;
        }

        if (toast.value) {
          toast.value?.kshowToast('success', `Thêm ${currentAttribute.value} thành công!`);
        }
        closeFormModal();
      } catch (error) {
        if (toast.value) {
          toast.value?.kshowToast('error', `Lỗi khi thêm ${currentAttribute.value}: ${error.response?.data?.message || error.message}`);
        }
        console.error('Add attribute error:', error);
      }
    };

    onMounted(() => {
      fetchOptions();
    });

    return {
      toast,
      productData,
      productVariants,
      currentVariant,
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
      dropdownOpen,
      breadcrumbItems,
      toggleDropdown,
      addVariant,
      removeVariant,
      showFormModal,
      currentAttribute,
      openAddModal,
      closeFormModal,
      handleAddAttribute,
      showColorModal,
      openColorModal,
      closeColorModal,
      confirmColorSelection,
      showImeiModal,
      openImeiModal,
      closeImeiModal,
      saveImei,
      handleExcelImport,
      imeiInput,
      variantImeis,
      mainImages,
      colorImages,
      handleMainImageUpload,
      handleColorImageUpload,
      handleSubmit,
      resetForm,
      getColorFromName,
      showImageSection,
      groupVariantsByRamAndRom,
      uniqueColors,
      selectedVariants,
      allSelected,
      groupCommonValues,
      toggleGroupSelection,
      toggleAllVariants,
      updateSelectedCount,
      updateSelectedVariants,
    };
  },
});
</script>