
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
            <div class="p-5 grid grid-cols-3 gap-6 items-center">
              <!-- Search -->
              <div>
                <label class="text-sm font-semibold block mb-2">Nhập thông tin tìm kiếm</label>
                <input
                  v-model="searchNV"
                  @input="btnSearch"
                  placeholder="Tìm theo mã hoặc tên..."
                  type="text"
                  class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
                />
              </div>

              <!-- Trạng thái -->
              <div>
                <label class="text-sm font-semibold block mb-2">Trạng thái</label>
                <div class="flex items-center gap-4">
                  <label class="flex items-center gap-2 cursor-pointer">
                    <input
                      type="radio"
                      name="status"
                      id="tat-ca"
                      v-model="filterStatus"
                      value="tat-ca"
                      @change="fetchNhanVien"
                    />
                    <span>Tất cả</span>
                  </label>

                  <label class="flex items-center gap-2 cursor-pointer">
                    <input
                      type="radio"
                      name="status"
                      id="dang-lam"
                      v-model="filterStatus"
                      value="dang-lam"
                      @change="fetchNhanVien"
                    />
                    <span>Đang làm</span>
                  </label>

                  <label class="flex items-center gap-2 cursor-pointer">
                    <input
                      type="radio"
                      name="status"
                      id="da-nghi"
                      v-model="filterStatus"
                      value="da-nghi"
                      @change="fetchNhanVien"
                    />
                    <span>Đã nghỉ</span>
                  </label>
                  <div class="flex justify-end items-end">
                    <router-link to="/them-nhan-vien">
                      <button class="px-4 py-2 bg-green-500 text-white rounded-md">
                        Thêm Nhân Viên
                      </button>
                    </router-link>
                  </div>
                </div>
              </div>
            </div>
          </form>


          <!--        Modeladdnhanvien-->
<!--          <div v-if="isModalOpen" class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">-->
<!--            <div class="bg-white p-6 rounded-lg shadow-lg w-[700px]">-->
<!--              <h2 class="text-2xl font-bold mb-4">Thông tin nhân viên</h2>-->

<!--              <div class="grid grid-cols-2 gap-4">-->
<!--                <div>-->
<!--                  <label class="block mb-2">Tên Nhân Viên</label>-->
<!--                  <input type="text" id="tenNhanVien" class="w-full px-3 py-2 border rounded-md" placeholder="Nhập tên nhân viên">-->
<!--                </div>-->

<!--                <div>-->
<!--                  <label class="block mb-2">UserName</label>-->
<!--                  <input type="text" id="maNhanVien" class="w-full px-3 py-2 border rounded-md" placeholder="Nhập UserNames">-->
<!--                </div>-->

<!--                <div>-->
<!--                  <label class="block mb-2">SDT</label>-->
<!--                  <input type="text" id="maNhanVien" class="w-full px-3 py-2 border rounded-md" placeholder="Nhập SDT">-->
<!--                </div>-->

<!--                <div>-->
<!--                  <label class="block mb-2">Email</label>-->
<!--                  <input type="text" id="maNhanVien" class="w-full px-3 py-2 border rounded-md" placeholder="Nhập Email">-->
<!--                </div>-->

<!--                <div class="col-span-2">-->
<!--                  <label class="block mb-2">Địa chỉ cụ thể</label>-->
<!--                  <input type="text" id="maNhanVien" class="w-full px-3 py-2 border rounded-md" placeholder="Nhập địa chỉ cụ thể">-->
<!--                </div>-->
<!--                -->
<!--                <div class="flex gap-4 col-span-2">-->
<!--                  <div class="w-1/3">-->
<!--                    <label class="block mb-2">Tỉnh/Thành phố</label>-->
<!--                    <select v-model="selectedProvince" @change="handleProvinceChange" class="w-full px-3 py-2 border rounded-md">-->
<!--                      <option value="" disabled>Chọn tỉnh/thành phố</option>-->
<!--                      <option v-for="province in provinces" :key="province.code">{{ province.name }}</option>-->
<!--                    </select>-->
<!--                  </div>-->

<!--                  <div class="w-1/3">-->
<!--                    <label class="block mb-2">Quận/Huyện</label>-->
<!--                    <select v-model="selectedDistrict" @change="handleDistrictChange" class="w-full px-3 py-2 border rounded-md">-->
<!--                      <option value="" disabled>Chọn quận/huyện</option>-->
<!--                      <option v-for="district in districts" :key="district.code">{{ district.name }}</option>-->
<!--                    </select>-->
<!--                  </div>-->

<!--                  <div class="w-1/3">-->
<!--                    <label class="block mb-2">Xã/Phường</label>-->
<!--                    <select v-model="selectedWard" class="w-full px-3 py-2 border rounded-md">-->
<!--                      <option value="" disabled>Chọn xã/phường</option>-->
<!--                      <option v-for="ward in wards" :key="ward.code">{{ ward.name }}</option>-->
<!--                    </select>-->
<!--                  </div>-->
<!--                </div>-->
<!--                <div class="col-span-2">-->
<!--                  <label class="block mb-2">Ảnh Nhân Viên</label>-->
<!--                  <input type="file" @change="previewImage" class="w-full px-3 py-2 border rounded-md">-->

<!--                  &lt;!&ndash; Hiển thị ảnh xem trước &ndash;&gt;-->
<!--                  <div v-if="employeeImage" class="mt-4 flex justify-center">-->
<!--                    <img :src="employeeImage" alt="Ảnh nhân viên" class="w-32 h-32 object-cover rounded-full border">-->
<!--                  </div>-->
<!--                </div>-->
<!--              </div>-->

<!--              <div class="flex justify-end space-x-4 mt-4">-->
<!--                <button @click="closeModal()" class="px-4 py-2 bg-gray-300 rounded-md">Hủy</button>-->
<!--                <button @click="saveEmployee()" class="px-4 py-2 bg-blue-600 text-white rounded-md">Lưu</button>-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->





          <div class="mt-8">
            <div class="mt-4 overflow-x-auto">
              <table class="w-full bg-white rounded-lg shadow-lg overflow-hidden border border-gray-200">
                <thead>
                <tr class="bg-gray-200 text-gray-800 uppercase text-sm tracking-wider">
                  <th class="px-6 py-4 text-center w-10">#</th>
                  <th class="px-6 py-4 text-left">Tên</th>
                  <th class="px-6 py-4 text-left">Email</th>
                  <th class="px-6 py-4 text-center w-40">SĐT</th>
                  <th class="px-6 py-4 text-center w-40">Ngày tham gia</th>
                  <th class="px-6 py-4 text-center w-32">Trạng thái</th>
                  <th class="px-6 py-4 text-center w-32">Thao Tác</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(nv, index) in dataTable" :key="nv.id"
                    class="border-t hover:bg-gray-100 transition-all duration-200"
                    :class="{'bg-gray-50': index % 2 !== 0}">
                  <td class="px-6 py-4 text-center">{{ index + 1 }}</td>
                  <td class="px-6 py-4 text-left">{{ nv.tenNhanVien }}</td>
                  <td class="px-6 py-4 text-left">{{ nv.idTaiKhoan.email }}</td>
                  <td class="px-6 py-4 text-center">{{ nv.idTaiKhoan.soDienThoai }}</td>
                  <td class="px-6 py-4 text-center">{{ new Date(nv.createdAt).toLocaleDateString('vi-VN') }}</td>
                  <td class="px-6 py-4 text-center font-semibold"
                      :class="{'text-red-500': nv.deleted, 'text-green-600': !nv.deleted}">
                    {{ nv.deleted ? 'Đã nghỉ' : 'Đang làm' }}
                  </td>
                  <td class="px-6 py-4 text-center">
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
import EmployeeForm from "@/views/AccountType/EmployeeForm.vue";
const visible = ref(false);
const message = ref("");
const type = ref("success");
import { ref, onMounted, watch } from "vue";
import axios from "axios";


//anhnhanvien
const employeeImage = ref(null);
function previewImage(event) {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      employeeImage.value = e.target.result;
    };
    reader.readAsDataURL(file);
  }
}


//DiaChiThanhPho

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
//getall
const filterStatus = ref('tat-ca'); // Mặc định là "Tất cả"

const fetchNhanVien = async () => {
  try {
    const res = await axios.get("http://localhost:8080/nhan-vien/home");

    if (filterStatus.value === 'dang-lam') {
      dataTable.value = res.data.filter(nv => !nv.deleted); // Đang làm
    } else if (filterStatus.value === 'da-nghi') {
      dataTable.value = res.data.filter(nv => nv.deleted); // Đã nghỉ
    } else {
      dataTable.value = res.data; // Tất cả
    }
  } catch (error) {
    console.error("Lỗi khi lấy danh sách nhân viên:", error);
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
    fetchNhanVien();
    return;
  } 
  dataTable.value = dataTable.value.filter(nhanvien =>
    nhanvien.idTaiKhoan.email.toLowerCase().includes(searchNV.value.toLowerCase()) || 
    nhanvien.tenNhanVien.toLowerCase().includes(searchNV.value.toLowerCase()) || 
    nhanvien.idTaiKhoan.soDienThoai.toLowerCase().includes(searchNV.value.toLowerCase()) 
  );
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
