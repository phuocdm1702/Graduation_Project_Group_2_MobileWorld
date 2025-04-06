<template>
  <div>
    <BreadcrumbWrapper v-if="breadcrumbItems" :breadcrumb-items="breadcrumbItems"/>

    <!-- Kiểm tra an toàn cho heDieuHanhOptions -->
    <div v-if="heDieuHanhOptions && heDieuHanhOptions.length" class="mt-2 mx-auto">
      <ToastNotification ref="toast"/>

      <div class="bg-white shadow-lg rounded-lg p-5 mb-4">
        <h3 class="text-lg font-medium text-gray-600 mb-4 text-center">THÊM SẢN PHẨM</h3>
        <div class="grid grid-cols-1 gap-6">
          <div class="flex items-center">
            <label class="w-40 text-sm font-medium text-gray-700">Tên Sản Phẩm</label>
            <input
              v-model="productData.tenSanPham"
              type="text"
              placeholder="Nhập Tên sản phẩm"
              @input="trimSpaces"
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
                <option v-for="nha in nhaSanXuatOptions" :key="nha.id" :value="nha.id">
                  {{ nha.nhaSanXuat }}
                </option>
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
          <div v-if="productVariants && productVariants.length > 0" class="mt-6 col-span-3">
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
                      class="w-full p-1 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </td>
                  <!-- Kiểm tra an toàn cho variantImeis -->
                  <td class="border border-gray-300 p-2 text-center">
                    {{ (variantImeis && variantImeis[group.startIndex + variantIndex]) ? variantImeis[group.startIndex + variantIndex].length : 0 }}
                  </td>
                  <td class="border border-gray-300 p-2 flex justify-center gap-2">
                    <button
                      @click="openImeiModal(group.startIndex + variantIndex)"
                      class="px-2 py-1 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
                    >
                      Nhập IMEI
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
      </div>

      <!-- Phần Thêm Ảnh Tự Động -->
      <div v-if="showImageSection && productVariants && productVariants.length > 0" class="bg-white shadow-lg rounded-lg p-5 mb-4">
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
            Thêm
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
            <label class="block text-sm font-medium text-gray-700 mb-2">Nhập từ file Excel</label>
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
    <div v-else>Loading...</div>
  </div>
</template>

<script>
import { defineComponent, computed, ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import FormModal from '@/components/FormModal.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';

import { useProductData } from './composables/useProductData';
import { useProductVariants } from './composables/useProductVariants';
import { useProductImages } from './composables/useProductImages';
import { useDropdowns } from './composables/useDropdowns';
import { useModals } from './composables/useModals';
import { useFormHandling } from './composables/useFormHandling';
import { useApiRequests } from './composables/useApiRequests';

export default defineComponent({
  name: 'AddChiTietSanPham',
  components: {
    ToastNotification,
    FormModal,
    BreadcrumbWrapper,
  },
  setup() {
    const route = useRoute();
    const toast = ref(null);

    const { productData, resetProductData, setDefaultValues } = useProductData();
    const {
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
      fetchOptions,
      handleAddAttribute,
    } = useApiRequests(toast);

    const { dropdownOpen, toggleDropdown } = useDropdowns();
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
      resetModals
    } = useModals(dropdownOpen, toggleDropdown);

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
      updateSelectedVariants,
      toggleGroupSelection,
      toggleAllVariants,
      updateSelectedCount,
      resetVariants
    } = useProductVariants(ramOptions, boNhoTrongOptions, mauSacOptions, productData, variantImeis);

    const {
      showImageSection,
      mainImages,
      colorImages,
      getColorFromName,
      handleMainImageUpload,
      handleColorImageUpload,
      resetImages
    } = useProductImages();

    const { handleSubmit } = useFormHandling(
      toast,
      productData,
      productVariants,
      mainImages,
      colorImages,
      variantImeis
    );

    const quickAddInputs = ref({
      heDieuHanh: { heDieuHanh: '', phienBan: '' },
      manHinh: { kichThuoc: '', doPhanGiai: '' },
      nhaSanXuat: { nhaSanXuat: '' },
      // Thêm các thuộc tính khác tương tự
    });

    const breadcrumbItems = computed(() => {
      if (typeof route.meta.breadcrumb === 'function') {
        return route.meta.breadcrumb(route) || ['Thêm Sản Phẩm'];
      }
      return route.meta?.breadcrumb || ['Thêm Sản Phẩm'];
    });

    const confirmColorSelection = () => {
      if (addVariant()) {
        showImageSection.value = true;
        closeColorModal();

        groupVariantsByRamAndRom.value.forEach((group) => {
          const groupKey = `${group.ram}/${group.rom}`;
          if (!(groupKey in allSelected.value)) {
            allSelected.value[groupKey] = true;
            groupCommonValues.value[groupKey] = { price: '' };
            toggleAllVariants(group, true);
          }
        });
      } else {
        toast.value?.kshowToast('error', 'Vui lòng chọn ít nhất một RAM, một Bộ Nhớ Trong và một Màu Sắc!');
      }
    };

    const handleAddAttributeLocal = async (data) => {
      try {
        console.log('Adding attribute:', currentAttribute.value, 'with data:', data);
        const success = await handleAddAttribute(
          currentAttribute.value,
          data,
          productData,
          currentVariant
        );

        if (success) {
          toast.value?.kshowToast('success', 'Thêm thành công!');
          closeFormModal();
        } else {
          toast.value?.kshowToast('error', 'Thêm thất bại. Vui lòng thử lại.');
        }
      } catch (error) {
        console.error('Error adding attribute:', error);
        toast.value?.kshowToast('error', 'Có lỗi xảy ra: ' + (error.response?.data?.message || error.message));
      }
    };

    const trimSpaces = () => {
      productData.value.tenSanPham = productData.value.tenSanPham.trim();
    };

    const resetForm = () => {
      resetProductData({
        heDieuHanhOptions: heDieuHanhOptions.value || [],
        manHinhOptions: manHinhOptions.value || [],
        nhaSanXuatOptions: nhaSanXuatOptions.value || [],
        cumCameraOptions: cumCameraOptions.value || [],
        simOptions: simOptions.value || [],
        thietKeOptions: thietKeOptions.value || [],
        pinOptions: pinOptions.value || [],
        cpuOptions: cpuOptions.value || [],
        gpuOptions: gpuOptions.value || [],
        congNgheMangOptions: congNgheMangOptions.value || [],
        congSacOptions: congSacOptions.value || [],
        hoTroCongNgheSacOptions: hoTroCongNgheSacOptions.value || [],
        chiSoKhangBuiVaNuocOptions: chiSoKhangBuiVaNuocOptions.value || [],
        tinhTrangOptions: tinhTrangOptions.value || []
      });
      resetVariants();
      resetImages();
      resetModals();
    };

    onMounted(async () => {
      await fetchOptions();
      setDefaultValues({
        heDieuHanhOptions: heDieuHanhOptions.value || [],
        manHinhOptions: manHinhOptions.value || [],
        nhaSanXuatOptions: nhaSanXuatOptions.value || [],
        cumCameraOptions: cumCameraOptions.value || [],
        simOptions: simOptions.value || [],
        thietKeOptions: thietKeOptions.value || [],
        pinOptions: pinOptions.value || [],
        cpuOptions: cpuOptions.value || [],
        gpuOptions: gpuOptions.value || [],
        congNgheMangOptions: congNgheMangOptions.value || [],
        congSacOptions: congSacOptions.value || [],
        hoTroCongNgheSacOptions: hoTroCongNgheSacOptions.value || [],
        chiSoKhangBuiVaNuocOptions: chiSoKhangBuiVaNuocOptions.value || [],
        tinhTrangOptions: tinhTrangOptions.value || []
      });
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
      toggleDropdown,
      addVariant,
      removeVariant,
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
      handleSubmit,
      resetForm,
      getColorFromName,
      trimSpaces
    };
  },
});
</script>