<template>
  <div v-if="visible" :class="`toast ${type}`">
    <span v-if="type === 'success'" class="checkmark">✔</span>
    <span v-if="type === 'error'" class="crossmark">✖</span>
    {{ message }}
  </div>
  <div>
    
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Quản lý Nhân Viên" />

    <div class="mt-4">
      <h4 class="text-gray-600 text-4xl font-bold">Quản lý Nhân Viên</h4>
      <div class="mt-4">
        <div class="w-full overflow-hidden bg-white border rounded-md shadow-md">
          <form @submit.prevent="addNhanVien">
            <div class="flex items-center justify-between px-5 py-3 text-gray-700 border-b">
              <h3 class="text-sm font-bold">Thêm Nhân Viên</h3>
              <button>
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            <div class="p-5 text-gray-700 bg-gray-200  grid grid-cols-3 gap-4 bg-white">
<!--              <div>-->
<!--                <label class="text-xs">ID Tài khoản</label>-->
<!--                <input v-model="nhanvien.idTaiKhoan" type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />-->
<!--              </div>-->
              <div>
                <label class="text-xs">Mã</label>
                <input v-model="nhanvien.ma" type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Tên Nhân viên</label>
                <input v-model="nhanvien.tenNhanVien" type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Ngày sinh</label>
                <input v-model="nhanvien.ngaySinh" type="date" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Ghi chú</label>
                <input v-model="nhanvien.ghiChu"  type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Thành phố</label>
                <input v-model="nhanvien.thanhPho" type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Quận</label>
                <input v-model="nhanvien.quan" type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Phường</label>
                <input v-model="nhanvien.phuong" type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Địa chỉ cụ thể</label>
                <input v-model="nhanvien.diaChiCuThe" type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">CCCD</label>
                <input v-model="nhanvien.cccd" type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Ảnh nhân viên</label>
                <input type="file" @change="handleFileUpload" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div v-if="nhanvien.anhNhanVien">
                <img :src="nhanvien.anhNhanVien" class="w-50 h-20 mt-2 rounded-md border border-gray-300 shadow-sm" />
              </div>
            </div>
            <div class="px-5 py-3 flex justify-between">
              <button type="reset" class="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition">Reset thông tin</button>
              <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition">
                Thêm nhân viên
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <br>
    
    <div class="flex items-center space-x-2">
      <input v-model="searchNV" placeholder="Search theo ma va ten..." type="text" class="w-full px-4 py-2 border rounded-md" />
      <button @click="btnSearch" type="reset" class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition">Search</button>
    </div>

    <div class="mt-8">
      <h4 class="text-gray-700 font-semibold text-lg">Danh sách Nhân Viên</h4>
      <div class="mt-4 overflow-x-auto">
        <table class="w-full bg-white rounded-lg shadow-lg overflow-hidden">
          <thead>
          <tr class="bg-gradient-to-r from-gray-300 to-gray-200 text-gray-800 uppercase text-sm tracking-wider">
            <th class="px-4 py-3 text-center">ID</th>
            <th class="px-4 py-3 text-center">ID Tài khoản</th>
            <th class="px-4 py-3 text-center">Mã</th>
            <th class="px-4 py-3 text-center">Tên Nhân viên</th>
            <th class="px-4 py-3 text-center">Ngày sinh</th>
            <th class="px-4 py-3 text-center">Ảnh nhân viên</th>
            <th class="px-4 py-3 text-center">Ghi chú</th>
            <th class="px-4 py-3 text-center">Thành phố</th>
            <th class="px-4 py-3 text-center">Quận</th>
            <th class="px-4 py-3 text-center">Phường</th>
            <th class="px-4 py-3 text-center">Địa chỉ cụ thể</th>
            <th class="px-4 py-3 text-center">CCCD</th>
            <th class="px-4 py-3 text-center">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <tr
            v-for="nv in dataTable" :key="nv.id"
            class="border-t text-center hover:bg-gray-100 transition-all duration-200"
            :class="{'bg-gray-50': nv.id % 2 === 0}"
          >
            <td class="px-4 py-3">{{ nv.id }}</td>
            <td class="px-4 py-3">{{ nv.idTaiKhoan.id }}</td>
            <td class="px-4 py-3">{{ nv.ma }}</td>
            <td class="px-4 py-3">{{ nv.tenNhanVien }}</td>
            <td class="px-4 py-3">{{ new Date(nv.ngaySinh).toLocaleDateString() }}</td>
            <td class="px-4 py-3">
              <img :src="nv.anhNhanVien" class="anh-nhan-vien rounded-md shadow-sm border border-gray-300" />
            </td>
            <td class="px-4 py-3">{{ nv.ghiChu }}</td>
            <td class="px-4 py-3">{{ nv.thanhPho }}</td>
            <td class="px-4 py-3">{{ nv.quan }}</td>
            <td class="px-4 py-3">{{ nv.phuong }}</td>
            <td class="px-4 py-3">{{ nv.diaChiCuThe }}</td>
            <td class="px-4 py-3">{{ nv.cccd }}</td>
            <td class="px-4 py-3">
              <button class="text-blue-600 hover:text-blue-800 font-semibold px-2">Sửa</button>
              <button @click="showDeleteConfirm(nv.id)" class="text-red-600 hover:text-red-800 font-semibold px-2">Xóa</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

  </div>

  <ConfirmModal
    :show="showConfirmModal"
    :message="'Bạn có chắc chắn muốn xóa khách hàng này không?'"
    @confirm="deleteNv"
    @cancel="showConfirmModal = false"
  />
</template>

<script setup>
import ConfirmModal from "@/components/ConfirmModal.vue";

const visible = ref(false);
const message = ref("");
const type = ref("success");
import { ref, onMounted } from "vue";
import axios from "axios";

const dataTable = ref([]);
const nhanvien = ref({
  idTaiKhoan: { id: 19 },
  ma: "",
  tenNhanVien: "",
  ngaySinh: "",
  anhNhanVien:"",
  ghiChu:"",
  thanhPho:"",
  quan:"",
  phuong:"",
  diaChiCuThe:"",
  cccd: "",
  deleted: 1,
});
//Confirm
const showConfirmModal = ref(false);
const selectedNVId = ref(null);
const showDeleteConfirm = (id) => {
  selectedNVId.value = id;
  showConfirmModal.value = true;
};

const handleFileUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    nhanvien.value.anhNhanVien = URL.createObjectURL(file);
    nhanvien.value.fileAnh = file; // Lưu file để gửi lên server
  }
};

const showToast = (toastType, msg) => {
  message.value = msg;
  type.value = toastType;
  visible.value = true;
  setTimeout(() => {
    visible.value = false;
  }, 3000);
};
const fetchNhanVien = (async () => {
  try {
    const res = await axios.get("http://localhost:8080/nhan-vien/home");
    console.log("Dữ liệu từ API:", res.data);
    dataTable.value = res.data;
  } catch (error) {
    console.error("Lỗi:", error);
  }
});
onMounted(fetchNhanVien);
//add
const addNhanVien = async () => {
  const checkcccd = /^\d{12}$/; 
  const checkten = /^[^\d]+$/; 
  const Ngaysinh = new Date(nhanvien.value.ngaySinh);
  const ngaySinhHt = new Date();

  if (!nhanvien.value.ma.trim()){
    showToast("error","Vui lòng nhập mãNV!");
    return;
  }
  if (!nhanvien.value.tenNhanVien.trim()){
    showToast("error","Vui lòng nhập tên nhân viên!");
    return;
  }
  if (!checkten.test(nhanvien.value.tenNhanVien)){
    showToast("error","Tên nhân viên chỉ được chứa chữ!");
    return;
  }
  if (!nhanvien.value.ghiChu.trim()){
    showToast("error","Vui lòng nhập ghi chú!");
    return;
  }
  if (!nhanvien.value.thanhPho.trim()){
    showToast("error","Vui lòng nhập thành phố!");
    return;
  }
  if (!nhanvien.value.quan.trim()){
    showToast("error","Vui lòng nhập quận!");
    return;
  }
  if (!nhanvien.value.phuong.trim()){
    showToast("error","Vui lòng nhập phường!");
    return;
  }
  if (!nhanvien.value.diaChiCuThe.trim()){
    showToast("error","Vui lòng nhập địa chỉ cụ thể!");
    return;
  }
  if (!nhanvien.value.cccd.trim()){
    showToast("error","Vui lòng nhập CCCD!");
    return;
  }
  if (!checkcccd.test(nhanvien.value.cccd)){
    showToast("error","CCCD phải có đúng 12 chữ số!");
    return;
  }
  if (Ngaysinh > ngaySinhHt){
    showToast("error","Ngay` sinh khong hop le!");
    return;
  }
  if (dataTable.value.some(nv => nv.ma === nhanvien.value.ma)) {
    showToast("error", "Mã nhân viên đã tồn tại!");
    return;
  }

  try {
    const res = await axios.post("http://localhost:8080/nhan-vien/add", nhanvien.value);
    showToast("success", "Thêm nhân viên thành công!");
    dataTable.value.unshift(res.data);
    
  } catch (error) {
    console.error("Lỗi khi thêm khách hàng:", error);
    showToast("error", "Không thể thêm nhân viên. Vui lòng thử lại!");
  }
};
//delete

const deleteNv = async (id) => {
  try {
     await axios.delete(`http://localhost:8080/nhan-vien/delete/${selectedNVId.value}`)
    showToast("success", "Xóa thành công!");
      fetchNhanVien();
  } catch (e) {
    showToast("loi");
  }
  showConfirmModal.value = false;
}
//Search 
const searchNV = ref("");
const btnSearch = () => {
  if (!searchNV.value.trim()){
    showToast("error","Vui long nhap search!");
    fetchNhanVien();
    return;
  } 
  dataTable.value = dataTable.value.filter(nhanvien =>
    nhanvien.ma.toLowerCase().includes(searchNV.value.toLowerCase()) || 
    nhanvien.tenNhanVien.toLowerCase().includes(searchNV.value.toLowerCase()) || 
    nhanvien.cccd.toLowerCase().includes(searchNV.value.toLowerCase()) 
  );
}
</script>
<style scoped>
.toast {
  position: fixed;
  top: 5%;
  right: 20px;
  padding: 10px 20px;
  border-radius: 5px;
  color: white;
}
.success { background: green; }
.error { background: red; }
.info { background: blue; }
.anh-nhan-vien {
  width: 60px;
  height: 60px;
}
</style>