
<template>
  <div v-if="visible" :class="`toast ${type}`">
    <span v-if="type === 'success'" class="checkmark">✔</span>
    <span v-if="type === 'error'" class="crossmark">✖</span>
    {{ message }}
  </div>
  <div>
    <Breadcrumb breadcrumb="Quản lý Khách Hàng" />

    <div class="mb-3">
      <h4 class="text-gray-600 text-4xl font-bold">Quản lý Khách Hàng</h4>
      
      <div class="mt-4">
        <div class="w-full overflow-hidden bg-white border rounded-md shadow-md">
          <form @submit.prevent="addCustomer">
            <div class="flex items-center justify-between px-5 py-3 text-gray-700 border-b">
              <h3 class="text-sm font-bold">Thêm Khách Hàng</h3>
              <button type="button">
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            <div class="p-5 text-gray-700 grid grid-cols-2 gap-4 bg-white">
              <div>
                <label class="text-xs">Mã</label>
                <input v-model="khachhang.ma" type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Tên Khách Hàng</label>
                <input v-model="khachhang.ten" type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Giới tính</label>
                <select v-model="khachhang.gioiTinh" class="w-full px-4 py-2 mt-2 border rounded-md">
                  <option value="null">----Chọn giới tính----</option>
                  <option value="0">Nam</option>
                  <option value="1">Nữ</option>
                </select>
              </div>
              <div>
                <label class="text-xs">Ngày sinh</label>
                <input v-model="khachhang.ngaySinh" type="date" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
            </div>

            <div class="px-5 py-3 flex justify-between">
              <button type="reset" class="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition"
                      @click="isEditing = false">Reset thông tin</button>

              <button v-if="!isEditing" type="submit" class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition">
                Thêm khách hàng
              </button>

              <button v-if="isEditing" @click="updateCustomer" type="button"
                      class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition">
                Sửa khách hàng
              </button>
            </div>

          </form>
        </div>
      </div>
    </div>

    <div class="flex items-center space-x-2">
      <input v-model="searchKH" placeholder="Search theo ma va ten..." type="text" class="w-full px-4 py-2 border rounded-md" />
      <button @click="BtnSearch" type="reset" class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition">Search</button>
    </div>



    <div class="mt-8">
      <h4 class="text-gray-600 text-2xl font-semibold">Danh sách Khách Hàng</h4>
      <div class="mt-4 overflow-hidden border rounded-lg shadow-lg">
        <table class="w-full bg-white rounded-md">
          <thead>
          <tr class="bg-blue-500 text-black">
            <th class="px-6 py-3 text-left">ID</th>
            <th class="px-6 py-3 text-left">ID_TK</th>
            <th class="px-6 py-3 text-left">Mã</th>
            <th class="px-6 py-3 text-left">Tên Khách Hàng</th>
            <th class="px-6 py-3 text-left">Giới tính</th>
            <th class="px-6 py-3 text-left">Ngày sinh</th>
            <th class="px-6 py-3 text-center">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="customer in dataTable" :key="customer.id"
              class="border-t text-gray-700 hover:bg-gray-100 transition">
            <td class="px-6 py-3">{{ customer.id }}</td>
            <td class="px-6 py-3">{{ customer.idTaiKhoan?.id }}</td>
            <td class="px-6 py-3 font-semibold">{{ customer.ma }}</td>
            <td class="px-6 py-3">{{ customer.ten }}</td>
            <td class="px-6 py-3">
            <span>
              {{ customer.gioiTinh == 0 ? 'Nam' : 'Nữ' }}
            </span>
            </td>
            <td class="px-6 py-3">{{ new Date(customer.ngaySinh).toLocaleDateString() }}</td>
            <td class="px-6 py-3 text-center">
              <button @click="editCustomer(customer)" class="text-blue-600 hover:text-blue-800 font-semibold px-2">Sửa</button>
              <button @click="showDeleteConfirm(customer.id)" class="text-red-600 hover:text-red-800 font-semibold px-2">Xóa</button>
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
    @confirm="confirmDelete"
    @cancel="showConfirmModal = false"
  />
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import ConfirmModal from "@/components/ConfirmModal.vue";

//du lieu add
const dataTable = ref([]);
const khachhang = ref({
  idTaiKhoan: { id: 19 },
  ma: "",
  ten: "",
  gioiTinh: null,
  ngaySinh: "",
  deleted: 1,
});
//Confirm
const showConfirmModal = ref(false);
const selectedCustomerId = ref(null);
const showDeleteConfirm = (id) => {
  selectedCustomerId.value = id;
  showConfirmModal.value = true;
};

//Getall Du lieu
const fetchCustomers = async () => {
  try {
    const res = await axios.get("http://localhost:8080/khach-hang/home");
    dataTable.value = res.data;
  } catch (error) {
    console.error("Lỗi khi lấy danh sách khách hàng:", error);
  }
};

onMounted(fetchCustomers);


//Hthi thong bao
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

//Add khachang
const addCustomer = async () => {
  const NamSinh = new Date(khachhang.value.ngaySinh);
  const NamSinhHienTai = new Date();
  const checkNumber = /\d/;
  if (!khachhang.value.ma.trim()){
    showToast("error","Vui lòng hãy nhập mãKH");
    return;
  } else if (!khachhang.value.ten.trim()){
    showToast("error","Vui lòng hãy nhập tênKH");
    return;
  } else if ( checkNumber.test(khachhang.value.ten)){
    showToast("error","Vui lòng không nhập số ở TênKH!");
    return;
  } else if (khachhang.value.gioiTinh == null) {
    showToast("error", "Vui lòng hãy chọn giớiTính");
    return;
  } else if (!khachhang.value.ngaySinh.trim()){
    showToast("error","Vui lòng hãy chọn ngàySinh");
    return;
  } else if (NamSinh > NamSinhHienTai){
    showToast("error","Ngày Sinh ko hợp lệ!");
    return;
  } else if (dataTable.value.some(kh => kh.ma === khachhang.value.ma)) {
    showToast("error", "Mã KH đã tồn tại!");
    return;
  }
  try {
   const res = await axios.post("http://localhost:8080/khach-hang/add", khachhang.value);
    showToast("success", "Thêm khách hàng thành công!");
    dataTable.value.unshift(res.data);
  } catch (error) {
    console.error("Lỗi khi thêm khách hàng:", error);
    showToast("error", "Không thể thêm khách hàng. Vui lòng thử lại!");
  }
};

//delete
const confirmDelete = async () => {
  try {
    await axios.delete(`http://localhost:8080/khach-hang/delete/${selectedCustomerId.value}`);
    showToast("success", "Xóa thành công!");
    fetchCustomers();
  } catch (error) {
    console.error("Lỗi khi xóa khách hàng:", error);
    showToast("error", "Không thể xóa khách hàng. Vui lòng thử lại!");
  }
  showConfirmModal.value = false;
};


const searchKH = ref("");

//SearchKH
const BtnSearch =  () => {
  if (!searchKH.value.trim()){
    showToast("error","Vui long` nhap ten muon tim kiem!");
    fetchCustomers();
    return;
  }
  dataTable.value = dataTable.value.filter(khachhang =>
    khachhang.ten.toLowerCase().includes(searchKH.value.toLowerCase()) ||
    khachhang.ma.toLowerCase().includes(searchKH.value.toLowerCase())
  );
}

const isEditing = ref(false);
//hiendulieuoTable
const editCustomer = (customer) => {
  khachhang.value = {
    ...customer,
    ngaySinh: customer.ngaySinh ? new Date(customer.ngaySinh).toISOString().split("T")[0] : ""
  };
  isEditing.value = true;
};

//UpdateKH
const updateCustomer = async () => {
  try {
    await axios.put(`http://localhost:8080/khach-hang/update/${khachhang.value.id}`, khachhang.value);
    showToast("success", "Cập nhật khách hàng thành công!");
    fetchCustomers();
    reseatKH();
    isEditing.value = false; //Thay doi ve add
  } catch (error) {
    showToast("error", "Không thể cập nhật khách hàng. Vui lòng thử lại!");
  }
};

//ReseatKH
const  reseatKH = () => {
  khachhang.value = {
    ma: "",
    ten: "",
    gioiTinh: null,
    ngaySinh: "", 
  }
};

</script>
//Css thong bao
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
</style>
