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
      <div class="mt-4">
        <div class="w-full overflow-hidden bg-white border rounded-md shadow-md">
          <form @submit.prevent>
            <div class="p-5  grid grid-cols-2 gap-6">
              <div>
                <label class="text-sm font-semibold block mb-2">Nhập thông tin tìm kiếm</label>
                <input @click="btnSearch" v-model="searchNV" placeholder="Tìm theo mã hoặc tên..." type="text"
                       class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"/>
              </div>
              <div>
                <label class="text-sm font-semibold block mb-2">Trạng thái</label>
                <div class="flex items-center gap-6">
                  <label class="flex items-center gap-2 cursor-pointer">
                    <input type="radio" name="status" id="tat-ca" class="w-4 h-4 text-blue-600">
                    <span>Tất cả</span>
                  </label>
                  <label class="flex items-center gap-2 cursor-pointer">
                    <input type="radio" name="status" id="dang-lam" class="w-4 h-4 text-blue-600">
                    <span>Đang làm</span>
                  </label>
                  <label class="flex items-center gap-2 cursor-pointer">
                    <input type="radio" name="status" id="da-nghi" class="w-4 h-4 text-blue-600">
                    <span>Đã nghỉ</span>
                  </label>
                    <button @click="openModal()"
                            class="bg-green-600  text-white px-4 py-2 rounded-lg hover:bg-green-700 transition">
                      Thêm nhân viên
                    </button>
                </div>
              </div>
            </div>
          </form>
  
          <!-- Modal Thêm Nhân Viên -->
          <div v-if="isModalOpen" class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
            <div class="bg-white p-6 rounded-lg shadow-lg w-96">
              <h2 class="text-xl font-bold mb-4">Thêm Nhân Viên</h2>

              <label class="block mb-2">Tên Nhân Viên</label>
              <input type="text" id="tenNhanVien" class="w-full px-3 py-2 border rounded-md mb-4" placeholder="Nhập tên nhân viên">

              <label class="block mb-2">Mã Nhân Viên</label>
              <input type="text" id="maNhanVien" class="w-full px-3 py-2 border rounded-md mb-4" placeholder="Nhập mã nhân viên">

              <div class="flex justify-end space-x-4">
                <button @click="closeModal()" class="px-4 py-2 bg-gray-300 rounded-md">Hủy</button>
                <button @click="saveEmployee()" class="px-4 py-2 bg-blue-600 text-white rounded-md">Lưu</button>
              </div>
            </div>
          </div>




          <div class="mt-8">
            <div class="mt-4 overflow-x-auto">
              <table class="w-full bg-white rounded-lg shadow-lg overflow-hidden">
                <thead>
                <tr class="bg-gradient-to-r from-gray-300 to-gray-200 text-gray-800 uppercase text-sm tracking-wider">
                  <th class="px-4 py-3 text-center">#</th>
                    <th class="px-4 py-3 text-center">Tên</th>
                  <th class="px-4 py-3 text-center">Email</th>
                  <th class="px-4 py-3 text-center">SĐT</th>
                  <th class="px-4 py-3 text-center">Ngày tham gia</th>
                  <th class="px-4 py-3 text-center">Trạng thái</th>
                  <th class="px-4 py-3 text-center">Thao Tác</th>
                </tr>
                </thead>
                <tbody>

                <tr
                  v-for="(nv,index) in dataTable" :key="nv.id"
                  class="border-t text-center hover:bg-gray-100 transition-all duration-200"
                  :class="{'bg-gray-50': nv.id % 2 === 0}"
                >
                  <td class="px-4 py-3">{{ index+1 }}</td>
                  <td class="px-4 py-3">{{ nv.tenNhanVien }}</td>
                  <td class="px-4 py-3">
                    <img :src="nv.anhNhanVien" class="anh-nhan-vien rounded-md shadow-sm border border-gray-300" />
                  </td>
                  <td class="px-4 py-3">{{ nv.ghiChu }}</td>
                  <td class="px-4 py-3">{{ new Date(nv.ngaySinh).toLocaleDateString() }}</td>
                  <td class="px-4 py-3">{{ nv.quan }}</td>
                  <td class="px-4 py-3">{{ nv.phuong }}</td>
                  <td class="px-4 py-3">{{ nv.diaChiCuThe }}</td>
                  <td class="px-4 py-3">{{ nv.cccd }}</td>
                  <td class="px-4 py-3">
                    <button @click="editNhanVien(nv)" class="text-blue-600 hover:text-blue-800 font-semibold px-2">Sửa</button>
                    <button @click="showDeleteConfirm(nv.id)" class="text-red-600 hover:text-red-800 font-semibold px-2">Xóa</button>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
    <br>
    

    

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
  
const isModalOpen = ref(false);

const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

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
const fetchNhanVien = async () => {
  try {
    const res = await axios.get("http://localhost:8080/nhan-vien/home");
    dataTable.value = res.data.filter(kh => !kh.deleted);
  } catch (error) {
    console.error("Lỗi khi lấy danh sách khách hàng:", error);
  }
};
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
    showToast("error","Ngay` sinh khong quá ngày hiện tại!");
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
     await axios.put(`http://localhost:8080/nhan-vien/delete/${selectedNVId.value}`)
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
//backSearch
const backSearch = () => {
  fetchNhanVien();
  searchNV.value = "";
  return;
}

//MouclickDulieu
const isEditing = ref(false);
const editNhanVien = (customer) => {
  nhanvien.value = {
    ...customer,
    ngaySinh: customer.ngaySinh ? new Date(customer.ngaySinh).toISOString().split("T")[0] : ""
  };
  isEditing.value = true;
};


//UpdateNV
const updateNhanVien = async () => {
  try {
    const res = await axios.put(`http://localhost:8080/nhan-vien/update/${nhanvien.value.id}`, nhanvien.value);
    showToast("success", "Cập nhật nhân viên thành công!");

    // Cập nhật lại danh sách
    const index = dataTable.value.findIndex(nv => nv.id === nhanvien.value.id);
    if (index !== -1) {
      dataTable.value[index] = { ...res.data }; 
    }

    isEditing.value = false;
  } catch (error) {
    console.error("Lỗi khi cập nhật nhân viên:", error);
    showToast("error", "Không thể cập nhật nhân viên!");
  }
};


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