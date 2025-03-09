
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
          <form @submit.prevent>
            <div class="p-5 grid grid-cols-3 gap-6 items-center">
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
                      id="kich-hoat"
                      v-model="filterStatus"
                      value="kich-hoat"
                      @change="fetchNhanVien"
                    />
                    <span>Kích hoạt</span>
                  </label>

                  <label class="flex items-center gap-2 cursor-pointer">
                    <input
                      type="radio"
                      name="status"
                      id="huy-kich-hoat"
                      v-model="filterStatus"
                      value="huy-kich-hoat"
                      @change="fetchNhanVien"
                    />
                    <span>Hủy kích hoạt</span>
                  </label>
                  <div class="flex justify-end items-end">
                    <router-link to="/them-khach-hang">
                      <button class="px-2 py-3 bg-green-500 text-white rounded-md">
                        Thêm Khách hàng
                      </button>
                    </router-link>
                  </div>
                </div>

              </div>

            </div>
          </form>
          <div class="mt-8">
            <div class="mt-4 overflow-hidden border rounded-lg shadow-lg">
              <table class="w-full bg-white rounded-md">
                <thead>
                <tr class="bg-blue-500 text-black">
                  <th class="px-6 py-3 text-left">#</th>
                  <th class="px-6 py-3 text-left">Tên</th>
                  <th class="px-6 py-3 text-left">Email</th>
                  <th class="px-6 py-3 text-left">SDT</th>
                  <th class="px-6 py-3 text-left">Ngày tham gia</th>
                  <th class="px-6 py-3 text-center">Trạng thái</th>
                  <th class="px-6 py-3 text-center">Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(customer,index) in dataTable" :key="customer.id"
                    class="border-t text-gray-700 hover:bg-gray-100 transition">
                  <td class="px-6 py-3">{{ index+1 }}</td>
                  <td class="px-6 py-3">{{ customer.ten }}</td>
                  <td class="px-6 py-3 font-semibold">{{ customer.idTaiKhoan.email }}</td>
                  <td class="px-6 py-3">{{ customer.idTaiKhoan.soDienThoai }}</td>
                  <td class="px-6 py-3">{{ new Date(customer.createdAt).toLocaleDateString() }}</td>
                  <td class="px-6 py-4 text-center font-semibold"
                      :class="{'text-red-500': customer.deleted, 'text-green-600': !customer.deleted}">
                    {{ customer.deleted ? 'Hủy kích hoạt' : 'Kích hoạt' }}
                  </td>                  <td class="px-6 py-3 text-center">
                  <button @click="editCustomer(customer)" class="text-blue-600 hover:text-blue-800 font-semibold px-2">Sửa</button>
                </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
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
    dataTable.value = res.data.filter(kh => !kh.deleted);
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
const dkcheck = () => {
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
}
//Add khachang
const addCustomer = async () => {
  if (!dkcheck()){
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
    await axios.put(`http://localhost:8080/khach-hang/delete/${selectedCustomerId.value}`);
    showToast("success", "Xóa mềm thành công!");
    fetchCustomers();
  } catch (error) {
    console.error("Lỗi khi xóa khách hàng:", error);
    showToast("error", "Không thể xóa khách hàng!");
  }
  showConfirmModal.value = false;
};



const searchKH = ref("");

//SearchKH
const BtnSearch =  () => {
  if (!searchKH.value.trim()){
    showToast("error","Vui long` nhap ten muon tim kiem!");
    return;
  }
  dataTable.value = dataTable.value.filter(khachhang =>
    khachhang.ten?.toLowerCase().includes(searchKH.value.toLowerCase()) ||
    khachhang.ma?.toLowerCase().includes(searchKH.value.toLowerCase())
  );
}
//backSearch
const backSearch = () => {
  fetchCustomers();
  searchKH.value = "";
  return;
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
