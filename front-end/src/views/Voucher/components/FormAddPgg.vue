<template>
  <div class="flex h-screen p-6 bg-gray-100">
    <!-- Form container -->
    <div class="w-3/4 p-6 bg-white border rounded-md shadow-md">
      <form>
        <!-- Header -->
        <div class="flex items-center justify-between pb-4 border-b">
          <h3 class="text-lg font-semibold">Thêm Phiếu Giảm Giá</h3>
          <button @click="closeModal">&times;</button>
        </div>

        <!-- Form nội dung -->
        <div class="grid grid-cols-3 gap-4 mt-4">
          <div>
            <label class="text-sm font-medium">Mã</label>
            <input type="text" class="form-input" v-model="ma" />
            <p class="error" v-if="errors.ma">{{ errors.ma }}</p>
          </div>
          <div>
            <label class="text-sm font-medium">Tên Phiếu Giảm Giá</label>
            <input type="text" class="form-input" v-model="tenPhieuGiamGia" />
            <p class="error" v-if="errors.tenPhieuGiamGia">{{ errors.tenPhieuGiamGia }}</p>
          </div>
          <div>
            <label class="text-sm font-medium">Loại Phiếu</label>
            <div class="flex items-center mt-2 space-x-4">
              <label class="flex items-center">
                <input type="radio" name="voucherType" value="Phần trăm" v-model="loaiPhieuGiamGia" class="mr-2" />
                Phần trăm
              </label>
              <label class="flex items-center">
                <input type="radio" name="voucherType" value="Tiền mặt" v-model="loaiPhieuGiamGia" class="mr-2" />
                Tiền mặt
              </label>
              <p class="error" v-if="errors.loaiPhieuGiamGia">{{ errors.loaiPhieuGiamGia }}</p>
            </div>
          </div>
          <div>
            <label class="text-sm font-medium">Phần trăm giảm giá</label>
            <input type="number" class="form-input" v-model="phanTramGiamGia" />
            <p class="error" v-if="errors.phanTramGiamGia">{{ errors.phanTramGiamGia }}</p>
          </div>
          <div>
            <label class="text-sm font-medium">Số tiền giảm tối đa</label>
            <input type="number" class="form-input" v-model="soTienGiamToiDa" />
            <p class="error" v-if="errors.soTienGiamToiDa">{{ errors.soTienGiamToiDa }}</p>
          </div>
          <div>
            <label class="text-sm font-medium">Hóa đơn tối thiểu</label>
            <input type="number" class="form-input" v-model="hoaDonToiThieu" />
            <p class="error" v-if="errors.hoaDonToiThieu">{{ errors.hoaDonToiThieu }}</p>
          </div>
          <div>
            <label class="text-sm font-medium">Số lượng sử dụng</label>
            <input type="number" class="form-input" v-model="soLuongDung" />
            <p class="error" v-if="errors.soLuongDung">{{ errors.soLuongDung }}</p>
          </div>
          <div>
            <label class="text-sm font-medium">Ngày bắt đầu</label>
            <input type="date" class="form-input" v-model="ngayBatDau" />
            <p class="error" v-if="errors.ngayBatDau">{{ errors.ngayBatDau }}</p>
          </div>
          <div>
            <label class="text-sm font-medium">Ngày kết thúc</label>
            <input type="date" class="form-input" v-model="ngayKetThuc" />
            <p class="error" v-if="errors.ngayKetThuc">{{ errors.ngayKetThuc }}</p>
          </div>
          <div class="flex items-center">
            <label class="text-sm font-medium">Trạng thái</label>
            <input type="checkbox" v-model="trangThai" class="ml-2" checked/>
          </div>
          <div class="flex items-center">
            <label class="text-sm font-medium">Riêng tư</label>
            <input type="checkbox" v-model="riengTu" class="ml-2" />
          </div>
          <div>
            <label class="text-sm font-medium">Mô tả</label>
            <textarea class="form-input" v-model="moTa"></textarea>
          </div>
        </div>

        <!-- Button actions -->
        <div class="flex justify-between mt-4">
          <button
            @click="closeModal"
            class="px-4 py-2 text-sm bg-gray-300 rounded-md hover:bg-gray-400"
          >
            Hủy
          </button>
          <button
            @click="submitForm"
            class="px-4 py-2 text-sm text-white bg-indigo-600 rounded-md hover:bg-indigo-500"
          >
            Thêm
          </button>
        </div>
      </form>
    </div>
    
    <div
      class="w-2/4 ml-4 p-4 bg-white border rounded-md shadow-md"
    >
      <h4 class="text-lg font-semibold mb-2">Chọn Khách Hàng</h4>
      <div class="table-container">
        <table class="w-full">
          <thead>
          <tr>
            <th>Chọn</th>
            <th>Mã KH</th>
            <th>Tên Khách Hàng</th>
            <th>Ngày sinh</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="customer in customers" :key="customer.id">
            <td><input type="checkbox" v-model="selectedCustomers" :value="customer.id" /></td>
            <td>{{ customer.ma }}</td>
            <td>{{ customer.ten }}</td>
            <td>{{ new Date(customer.ngaySinh).toLocaleDateString('vi-VN') }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from "vue";
import {useRouter} from "vue-router";
import axios from "axios";

const customers = ref([]);
const selectedCustomers = ref([]);
const router = useRouter();

const ma = ref("");
const tenPhieuGiamGia = ref("");
const loaiPhieuGiamGia = ref("Chung");
const phanTramGiamGia = ref("");
const soTienGiamToiDa = ref("");
const hoaDonToiThieu = ref("");
const soLuongDung = ref("");
const ngayBatDau = ref("");
const ngayKetThuc = ref("");
const trangThai = ref(true); // Boolean
const riengTu = ref(false); // Boolean
const moTa = ref("");
const deleted = ref(false); // Mặc định là false

const fetchDataKH = async () => {
  try {
    const response = await axios.get("http://localhost:8080/add-phieu-giam-gia/data-kh")
    customers.value = response.data;
  } catch (error) {
    console.log("Error: ", error);
  }
  
}

const errors = ref({});

const validateForm = () => {
  errors.value = {}; // Reset lỗi

  if (!ma.value) errors.value.ma = "Mã phiếu không được để trống";
  if (!tenPhieuGiamGia.value) errors.value.tenPhieuGiamGia = "Tên phiếu không được để trống";
  if (!loaiPhieuGiamGia.value) errors.value.loaiPhieuGiamGia = "Vui lòng chọn loại phiếu";

  if (phanTramGiamGia.value < 0 || phanTramGiamGia.value > 100) errors.value.phanTramGiamGia = "Phần trăm giảm giá phải từ 0 đến 100";
  if (soTienGiamToiDa.value < 0) errors.value.soTienGiamToiDa = "Số tiền giảm không hợp lệ";
  if (soLuongDung.value < 1) errors.value.soLuongDung = "Số lượng phải lớn hơn 0";
  if (hoaDonToiThieu.value < 0) errors.value.hoaDonToiThieu = "Hóa đơn tối thiểu không hợp lệ";

  if (!ngayBatDau.value) errors.value.ngayBatDau = "Vui lòng chọn ngày bắt đầu";
  if (!ngayKetThuc.value) errors.value.ngayKetThuc = "Vui lòng chọn ngày kết thúc";
  if (ngayBatDau.value && ngayKetThuc.value && ngayBatDau.value > ngayKetThuc.value) {
    errors.value.ngayKetThuc = "Ngày kết thúc phải sau ngày bắt đầu";
  }

  return Object.keys(errors.value).length === 0;
};

const submitForm = async () => {
  if(!validateForm()) {
    return;
  }
  
  const newPgg = {
    ma: ma.value,
    tenPhieuGiamGia: tenPhieuGiamGia.value,
    loaiPhieuGiamGia: loaiPhieuGiamGia.value,
    phanTramGiamGia: phanTramGiamGia.value,
    soTienGiamToiDa: soTienGiamToiDa.value,
    hoaDonToiThieu: hoaDonToiThieu.value,
    soLuongDung: soLuongDung.value,
    ngayBatDau: ngayBatDau.value ? new Date(ngayBatDau.value).toISOString() : null,
    ngayKetThuc: ngayKetThuc.value ? new Date(ngayKetThuc.value).toISOString() : null,
    trangThai: trangThai.value ? 1 : 0, 
    riengTu: riengTu.value ? 1 : 0, 
    moTa: moTa.value,
    deleted: deleted.value,
    customerIds: riengTu.value ? selectedCustomers.value : [],
  };

  console.log("Dữ liệu gửi lên:", newPgg);

  try {
    const response = await axios.post("http://localhost:8080/add-phieu-giam-gia/addPhieuGiamGia", newPgg);
    console.log("Response: ", response);
    alert("Thêm phiếu giảm giá thành công!");
    await router.push("/phieu-giam-gia");
  } catch (error) {
    console.error("Lỗi khi thêm phiếu giảm giá:", error);
    alert("Thêm thất bại, vui lòng kiểm tra lại!");
  }
};


onMounted(fetchDataKH);
</script>

<style scoped>
.form-input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc; 
  border-radius: 4px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  white-space: nowrap; 
  text-align: left;
  padding: 8px;
}

th {
  background-color: #f3f4f6;
  font-weight: bold;
}

thead {
  display: table-header-group;
}

tbody {
  display: table-row-group;
}

.table-container {
  overflow-x: auto;
}

</style>