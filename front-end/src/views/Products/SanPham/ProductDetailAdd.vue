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
          <!-- ID Sản Phẩm -->
          <div class="flex items-center">
            <label class="w-1/5 text-sm font-medium text-gray-700">ID Sản Phẩm</label>
            <input
              v-model="productData.id"
              type="text"
              placeholder="Nhập ID sản phẩm"
              class="w-4/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button
              @click="openAddModal('id')"
              class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
            >
              +
            </button>
          </div>

          <!-- Hệ Điều Hành, Màn Hình, Nhà Sản Xuất -->
          <div class="grid grid-cols-3 gap-6">
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">Hệ Điều Hành</label>
              <select
                v-model="productData.idHeDieuHanh"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Hệ Điều Hành</option>
                <option v-for="he in heDieuHanhOptions" :key="he.id" :value="he.id">{{ he.tenHeDieuHanh }}</option>
              </select>
              <button
                @click="openAddModal('heDieuHanh')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">Màn Hình</label>
              <select
                v-model="productData.idManHinh"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Màn Hình</option>
                <option v-for="man in manHinhOptions" :key="man.id" :value="man.id">{{ man.kichThuoc }}</option>
              </select>
              <button
                @click="openAddModal('manHinh')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">Nhà Sản Xuất</label>
              <select
                v-model="productData.idNhaSanXuat"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Nhà Sản Xuất</option>
                <option v-for="nha in nhaSanXuatOptions" :key="nha.id" :value="nha.id">{{ nha.tenNhaSanXuat }}</option>
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
              <label class="w-2/5 text-sm font-medium text-gray-700">Cụm Camera</label>
              <select
                v-model="productData.idCumCamera"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Cụm Camera</option>
                <option v-for="camera in cumCameraOptions" :key="camera.id" :value="camera.id">{{ camera.tenCamera }}</option>
              </select>
              <button
                @click="openAddModal('cumCamera')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">Sim</label>
              <select
                v-model="productData.idSim"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Sim</option>
                <option v-for="sim in simOptions" :key="sim.id" :value="sim.id">{{ sim.loaiSim }}</option>
              </select>
              <button
                @click="openAddModal('sim')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">Thiết Kế</label>
              <select
                v-model="productData.idThietKe"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Thiết Kế</option>
                <option v-for="thietKe in thietKeOptions" :key="thietKe.id" :value="thietKe.id">{{ thietKe.tenThietKe }}</option>
              </select>
              <button
                @click="openAddModal('thietKe')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
          </div>

          <!-- Pin, CPU -->
          <div class="grid grid-cols-2 gap-6">
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">Pin</label>
              <select
                v-model="productData.idPin"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Pin</option>
                <option v-for="pin in pinOptions" :key="pin.id" :value="pin.id">{{ pin.dungLuong }}</option>
              </select>
              <button
                @click="openAddModal('pin')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">CPU</label>
              <select
                v-model="productData.idCpu"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn CPU</option>
                <option v-for="cpu in cpuOptions" :key="cpu.id" :value="cpu.id">{{ cpu.tenCpu }}</option>
              </select>
              <button
                @click="openAddModal('cpu')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
          </div>

          <!-- GPU, Công Nghệ Mạng -->
          <div class="grid grid-cols-2 gap-6">
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">GPU</label>
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
              <label class="w-2/5 text-sm font-medium text-gray-700">Công Nghệ Mạng</label>
              <select
                v-model="productData.idCongNgheMang"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Công Nghệ Mạng</option>
                <option v-for="congNghe in congNgheMangOptions" :key="congNghe.id" :value="congNghe.id">{{ congNghe.tenCongNghe }}</option>
              </select>
              <button
                @click="openAddModal('congNgheMang')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
          </div>

          <!-- Công Nghệ Sạc, Hỗ Trợ Công Nghệ Sạc, Chỉ Số Kháng Bụi Nước -->
          <div class="grid grid-cols-3 gap-6">
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">Công Nghệ Sạc</label>
              <select
                v-model="productData.idCongSac"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Công Nghệ Sạc</option>
                <option v-for="congSac in congSacOptions" :key="congSac.id" :value="congSac.id">{{ congSac.tenCongNghe }}</option>
              </select>
              <button
                @click="openAddModal('congSac')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">Hỗ Trợ Công Nghệ Sạc</label>
              <select
                v-model="productData.idHoTroCongNgheSac"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Hỗ Trợ Công Nghệ Sạc</option>
                <option v-for="hoTro in hoTroCongNgheSacOptions" :key="hoTro.id" :value="hoTro.id">{{ hoTro.ten }}</option>
              </select>
              <button
                @click="openAddModal('hoTroCongNgheSac')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">Kháng Bụi Nước</label>
              <select
                v-model="productData.idChiSoKhangBuiVaNuoc"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Chỉ Số Kháng Bụi Nước</option>
                <option v-for="chiSo in chiSoKhangBuiVaNuocOptions" :key="chiSo.id" :value="chiSo.id">{{ chiSo.maChiSo }}</option>
              </select>
              <button
                @click="openAddModal('chiSoKhangBuiVaNuoc')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
          </div>

          <!-- Tình Trạng, Tiện Ích Đặc Biệt, Giá Bán -->
          <div class="grid grid-cols-3 gap-6">
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">Tình Trạng</label>
              <select
                v-model="productData.idLoaiTinhTrang"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn Tình Trạng</option>
                <option v-for="tinhTrang in tinhTrangOptions" :key="tinhTrang.id" :value="tinhTrang.id">{{ tinhTrang.tenTinhTrang }}</option>
              </select>
              <button
                @click="openAddModal('tinhTrang')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">Tiện Ích Đặc Biệt</label>
              <input
                v-model="productData.tienIchDacBiet"
                type="text"
                placeholder="Nhập Tiện Ích Đặc Biệt"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <button
                @click="openAddModal('tienIchDacBiet')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
            <div class="flex items-center">
              <label class="w-2/5 text-sm font-medium text-gray-700">Giá Bán</label>
              <input
                v-model="productData.giaBan"
                type="text"
                placeholder="Nhập Giá Bán"
                class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <button
                @click="openAddModal('giaBan')"
                class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
              >
                +
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Phần Thêm Phiên Bản -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-4">
        <h3 class="text-lg font-medium text-gray-600 mb-4 text-center">THÊM PHIÊN BẢN</h3>
        <div class="grid grid-cols-3 gap-6">
          <div class="flex items-center">
            <label class="w-2/5 text-sm font-medium text-gray-700">RAM</label>
            <select
              v-model="currentVariant.idRam"
              class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Chọn RAM</option>
              <option v-for="ram in ramOptions" :key="ram.id" :value="ram.id">{{ ram.dungLuong }}</option>
            </select>
            <button
              @click="openAddModal('ram')"
              class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
            >
              +
            </button>
          </div>
          <div class="flex items-center">
            <label class="w-2/5 text-sm font-medium text-gray-700">Bộ Nhớ Trong</label>
            <select
              v-model="currentVariant.idBoNhoTrong"
              class="w-3/5 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Chọn Bộ Nhớ Trong</option>
              <option v-for="boNho in boNhoTrongOptions" :key="boNho.id" :value="boNho.id">{{ boNho.dungLuong }}</option>
            </select>
            <button
              @click="openAddModal('boNhoTrong')"
              class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
            >
              +
            </button>
          </div>
          <div class="flex items-center">
            <label class="w-2/5 text-sm font-medium text-gray-700">Màu Sắc</label>
            <input
              v-model="currentVariant.idMauSac"
              type="color"
              class="w-3/5 p-1 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button
              @click="openAddModal('mauSac')"
              class="ml-2 px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
            >
              +
            </button>
          </div>
        </div>
        <div class="mt-4 flex justify-end">
          <button
            @click="addVariant"
            class="px-4 py-2 bg-red-500 text-white font-semibold rounded-lg hover:bg-red-600 transition"
          >
            +
          </button>
        </div>
        <div v-for="(variant, index) in productVariants" :key="index" class="grid grid-cols-3 gap-6 mt-2">
          <div class="flex items-center">
            <input
              :value="ramOptions.find(ram => ram.id === variant.idRam)?.dungLuong || ''"
              type="text"
              class="w-full p-2 border border-gray-300 rounded-lg bg-gray-100"
              readonly
            />
          </div>
          <div class="flex items-center">
            <input
              :value="boNhoTrongOptions.find(boNho => boNho.id === variant.idBoNhoTrong)?.dungLuong || ''"
              type="text"
              class="w-full p-2 border border-gray-300 rounded-lg bg-gray-100"
              readonly
            />
          </div>
          <div class="flex items-center">
            <input
              :value="variant.idMauSac || ''"
              type="text"
              class="w-full p-2 border border-gray-300 rounded-lg bg-gray-100"
              readonly
            />
          </div>
        </div>
      </div>

      <!-- Phần Thêm Ảnh -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-4">
        <h3 class="text-lg font-medium text-gray-600 mb-4 text-center">THÊM ẢNH</h3>
        <div class="grid grid-cols-1 gap-6">
          <div class="flex items-center">
            <label class="w-1/5 text-sm font-medium text-gray-700">Ảnh</label>
            <input
              type="file"
              @change="handleImageUpload"
              class="w-4/5 p-2 border border-gray-300 rounded-lg"
            />
          </div>
        </div>
        <div class="mt-4 flex justify-end">
          <button
            @click="addImage"
            class="px-4 py-2 bg-red-500 text-white font-semibold rounded-lg hover:bg-red-600 transition"
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
              <label class="block text-sm font-medium text-gray-700">Tên Công Nghệ Sạc</label>
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
              <label class="block text-sm font-medium text-gray-700">Dung Lượng RAM</label>
              <input v-model="entityData.dungLuong" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập dung lượng RAM" />
            </div>
          </div>
          <div v-if="currentAttribute === 'boNhoTrong'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Dung Lượng Bộ Nhớ Trong</label>
              <input v-model="entityData.dungLuong" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập dung lượng bộ nhớ trong" />
            </div>
          </div>
          <div v-if="currentAttribute === 'mauSac'" class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Màu Sắc</label>
              <input v-model="entityData.tenMau" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Nhập tên màu sắc" />
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

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import FormModal from '@/components/FormModal.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue'; // Import BreadcrumbWrapper
import axios from 'axios';

const router = useRouter();
const route = useRoute();
const toast = ref(null);
const showFormModal = ref(false);
const currentAttribute = ref('');
const productData = ref({
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

const productVariants = ref([]);
const currentVariant = ref({
  idRam: '',
  idBoNhoTrong: '',
  idMauSac: '',
});

const productImages = ref([]);
const currentImage = ref({
  file: null,
  fileName: '',
});

// Danh sách tùy chọn từ API
const heDieuHanhOptions = ref([]);
const manHinhOptions = ref([]);
const nhaSanXuatOptions = ref([]);
const cumCameraOptions = ref([]);
const simOptions = ref([]);
const thietKeOptions = ref([]);
const pinOptions = ref([]);
const cpuOptions = ref([]);
const gpuOptions = ref([]);
const congNgheMangOptions = ref([]);
const congSacOptions = ref([]);
const hoTroCongNgheSacOptions = ref([]);
const chiSoKhangBuiVaNuocOptions = ref([]);
const tinhTrangOptions = ref([]);
const ramOptions = ref([]);
const boNhoTrongOptions = ref([]);
const mauSacOptions = ref([]);

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Thêm Sản Phẩm"]; // Mặc định nếu không có breadcrumb
});

const fetchOptions = async () => {
  try {
    const responses = await Promise.all([
      axios.get('http://localhost:8080/api/he-dieu-hanhs'),
      axios.get('http://localhost:8080/api/man-hinhs'),
      axios.get('http://localhost:8080/api/nha-san-xuats'),
      axios.get('http://localhost:8080/api/cum-cameras'),
      axios.get('http://localhost:8080/api/sims'),
      axios.get('http://localhost:8080/api/thiet-kes'),
      axios.get('http://localhost:8080/api/pins'),
      axios.get('http://localhost:8080/api/cpus'),
      axios.get('http://localhost:8080/api/gpus'),
      axios.get('http://localhost:8080/api/cong-nghe-mangs'),
      axios.get('http://localhost:8080/api/cong-sacs'),
      axios.get('http://localhost:8080/api/ho-tro-cong-nghe-sacs'),
      axios.get('http://localhost:8080/api/chi-so-khang-bui-va-nuocs'),
      axios.get('http://localhost:8080/api/tinh-trangs'),
      axios.get('http://localhost:8080/api/rams'),
      axios.get('http://localhost:8080/api/bo-nho-trongs'),
      axios.get('http://localhost:8080/api/mau-sacs'),
    ]);

    [heDieuHanhOptions.value, manHinhOptions.value, nhaSanXuatOptions.value, cumCameraOptions.value,
      simOptions.value, thietKeOptions.value, pinOptions.value, cpuOptions.value, gpuOptions.value,
      congNgheMangOptions.value, congSacOptions.value, hoTroCongNgheSacOptions.value, chiSoKhangBuiVaNuocOptions.value,
      tinhTrangOptions.value, ramOptions.value, boNhoTrongOptions.value, mauSacOptions.value] = responses.map(res => res.data);
  } catch (error) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
    }
    console.error('Fetch options error:', error);
  }
};

const handleImageUpload = (event) => {
  currentImage.value.file = event.target.files[0];
  currentImage.value.fileName = event.target.files[0] ? event.target.files[0].name : 'No file chosen';
};

const addVariant = () => {
  if (currentVariant.value.idRam && currentVariant.value.idBoNhoTrong && currentVariant.value.idMauSac) {
    productVariants.value.push({ ...currentVariant.value });
    currentVariant.value = { idRam: '', idBoNhoTrong: '', idMauSac: '' };
  } else {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Vui lòng điền đầy đủ thông tin phiên bản!');
    }
  }
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
  if (!productData.value.id || !productData.value.giaBan || Object.values(productData.value).some(val => val === '')) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Vui lòng nhập đầy đủ thông tin sản phẩm!');
    }
    return;
  }

  const formData = new FormData();
  formData.append('productData', JSON.stringify(productData.value));
  productVariants.value.forEach((variant, index) => {
    formData.append(`variants[${index}]`, JSON.stringify(variant));
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
    await router.push('/product-detail');
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
  currentVariant.value = { idRam: '', idBoNhoTrong: '', idMauSac: '' };
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
        // Logic thêm ID (có thể bỏ qua nếu ID tự động tăng từ backend)
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
        response = await axios.post('http://localhost:8080/api/cong-nghe-mangs', { tenCongNghe: data.tenCongNghe });
        congNgheMangOptions.value.push(response.data);
        break;
      case 'congSac':
        response = await axios.post('http://localhost:8080/api/cong-sacs', { tenCongNghe: data.tenCongNghe });
        congSacOptions.value.push(response.data);
        break;
      case 'hoTroCongNgheSac':
        response = await axios.post('http://localhost:8080/api/ho-tro-cong-nghe-sacs', { ten: data.ten });
        hoTroCongNgheSacOptions.value.push(response.data);
        break;
      case 'chiSoKhangBuiVaNuoc':
        response = await axios.post('http://localhost:8080/api/chi-so-khang-bui-va-nuocs', { maChiSo: data.maChiSo });
        chiSoKhangBuiVaNuocOptions.value.push(response.data);
        break;
      case 'tinhTrang':
        response = await axios.post('http://localhost:8080/api/tinh-trangs', { tenTinhTrang: data.tenTinhTrang });
        tinhTrangOptions.value.push(response.data);
        break;
      case 'ram':
        response = await axios.post('http://localhost:8080/api/rams', { dungLuong: data.dungLuong });
        ramOptions.value.push(response.data);
        break;
      case 'boNhoTrong':
        response = await axios.post('http://localhost:8080/api/bo-nho-trongs', { dungLuong: data.dungLuong });
        boNhoTrongOptions.value.push(response.data);
        break;
      case 'mauSac':
        response = await axios.post('http://localhost:8080/api/mau-sacs', { tenMau: data.tenMau });
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
</style>