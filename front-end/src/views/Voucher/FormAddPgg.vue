<template>
  <div v-if="visible" :class="`toast ${type}`">
    <span v-if="type === 'success'" class="checkmark">✔</span>
    <span v-if="type === 'error'" class="crossmark">✖</span>
    {{ message }}
  </div>
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
          </div>
          <div>
            <label class="text-sm font-medium">Tên Phiếu Giảm Giá</label>
            <input type="text" class="form-input" v-model="tenPhieuGiamGia" />
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
            </div>
          </div>
          <div>
            <label class="text-sm font-medium">Phần trăm giảm giá</label>
            <input type="number" class="form-input" v-model="phanTramGiamGia" />
          </div>
          <div>
            <label class="text-sm font-medium">Số tiền giảm tối đa</label>
            <input type="number" class="form-input" v-model="soTienGiamToiDa" />
          </div>
          <div>
            <label class="text-sm font-medium">Hóa đơn tối thiểu</label>
            <input type="number" class="form-input" v-model="hoaDonToiThieu" />
          </div>
          <div>
            <label class="text-sm font-medium">Số lượng sử dụng</label>
            <input type="number" class="form-input" v-model="soLuongDung" />
          </div>
          <div>
            <label class="text-sm font-medium">Ngày bắt đầu</label>
            <input type="date" class="form-input" v-model="ngayBatDau" />
          </div>
          <div>
            <label class="text-sm font-medium">Ngày kết thúc</label>
            <input type="date" class="form-input" v-model="ngayKetThuc" />
          </div>
          <div class="flex items-center">
            <label class="text-sm font-medium">Trạng thái</label>
            <input type="checkbox" v-model="trangThai" class="ml-2" />
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
            
            class="px-4 py-2 text-sm text-white bg-indigo-600 rounded-md hover:bg-indigo-500"
          >
            Thêm
          </button>
        </div>
      </form>
    </div>
    
    <div
      v-if="riengTu"
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

const visible = ref(false);
const message = ref("");
const type = ref("success");

const showToast = (toastType, msg) => {
  message.value = msg;
  type.value = toastType;
  visible.value = true;
  setTimeout(() => {
    visible.value = false;
  }, 3000);
};

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
const trangThai = ref(false); // Boolean
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

const submitForm = async () => {
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