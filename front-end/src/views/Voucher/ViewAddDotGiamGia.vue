<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Đợt Giảm Giá" />
    <h4 class="text-gray-600">Quản lý Đợt Giảm Giá</h4>
    <div class="bg-white p-4 rounded-md shadow-md flex flex-wrap gap-3">
   

      <h4 class="text-gray-600 text-lg font-semibold">Quản lý Đợt Giảm Giá</h4>

      <!-- Container for form and product list -->
      <div class="flex gap-4 w-full">
        <!-- Form -->
        <div class="w-3/5 overflow-hidden bg-white border rounded-md shadow-md p-4">
          <form @submit.prevent="addData" class="space-y-4">
          <div>
              <label class="block text-gray-700">Mã</label>
              <input type="text" v-model="dotGiamGia.ma" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Tên đợt giảm giá</label>
              <input type="text" v-model="dotGiamGia.tenDotGiamGia" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Loại giảm giá</label>
              <select v-model="dotGiamGia.loaiGiamGiaApDung" class="w-full border rounded p-2">
                <option>Phần trăm</option>
                <option>Tiền mặt</option>
              </select>
            </div>
            <div>
              <label class="block text-gray-700">Giá trị giảm giá</label>
              <input type="number" v-model="dotGiamGia.giaTriGiamGia" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Số tiền giảm tối đa</label>
              <input type="number" v-model="dotGiamGia.soTienGiamToiDa" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Ngày bắt đầu</label>
              <input type="date" v-model="dotGiamGia.ngayBatDau" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Ngày kết thúc</label>
              <input type="date" v-model="dotGiamGia.ngayKetThuc" class="w-full border rounded p-2">
            </div>
            <div class="mt-2">
              <button class="bg-indigo-600 text-white px-4 py-2 rounded">
                {{ edit ? 'Cập nhật' : 'Thêm' }}
              </button>
            </div>
            <div class="mt-2">
              
              <router-link to="/dot-giam-gia">
                <button  class="bg-green-500 text-white px-3 py-1 rounded-md hover:bg-blue-600 transition">Quay về</button>
              </router-link>
            </div>
          </form>
        </div>

        <!-- Bảng Dòng Sản Phẩm -->
        <div class="w-2/5 overflow-hidden bg-white border rounded-md shadow-md p-4">
          <h5 class="text-gray-700 font-semibold">Dòng Sản Phẩm</h5>

          <input
            v-model="searchKeyword"
            placeholder="Tìm kiếm theo tên, mã"
            class="w-full p-2 border border-gray-300 rounded-md mt-2"
          />

          <div class="max-h-[700px] overflow-y-auto">
            <table class="w-full mt-3 border-collapse border border-gray-300">
              <thead class="bg-gray-200">
              <tr>
                <th class="border border-gray-300 px-2 py-1"> </th>
                <th class="border border-gray-300 px-2 py-1">STT</th>
                <th class="border border-gray-300 px-2 py-1">Mã</th>
                <th class="border border-gray-300 px-2 py-1">Dòng sản phẩm</th>
              </tr>
              </thead>
              <tbody>
              <tr
                v-for="(product, index) in dspList"
                :key="product.id"
                class="border border-gray-300"
              >
                <td class="px-2 py-1 border border-gray-300">
                  <input
                    type="checkbox"
                    :value="product.id"
                    v-model="idDSPs"
                    @change="fetchCTSPData"
                  />
                </td>
                <td class="px-2 py-1 border border-gray-300">{{ index + 1 }}</td>
                <td class="px-2 py-1 border border-gray-300">{{ product.ma }}</td>
                <td class="px-2 py-1 border border-gray-300">{{ product.dongSanPham }}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- Bảng Chi Tiết Sản Phẩm -->
      <div class="w-full overflow-hidden bg-white border rounded-md shadow-md p-4 mt-4">
        <h5 class="text-gray-700 font-semibold">Chi Tiết Sản Phẩm</h5>
        <div class="max-h-[700px] overflow-y-auto">
          <table class="w-full mt-3 border-collapse border border-gray-300">
            <thead class="bg-gray-200">
            <tr>
              <th class="border border-gray-300 px-2 py-1">STT</th>
              <th class="border border-gray-300 px-2 py-1">Ảnh</th>
              <th class="border border-gray-300 px-2 py-1">Tên sản phẩm</th>
              <th class="border border-gray-300 px-2 py-1">Đơn giá</th>
              <th class="border border-gray-300 px-2 py-1">Số tiền giảm tối đa</th>
              <th class="border border-gray-300 px-2 py-1">Đơn giá sau giảm giá</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(detail, index) in ctspList" :key="index" class="border border-gray-300">
              <td class="px-2 py-1 border border-gray-300">{{ index+1 }}</td>
              <td class="px-2 py-1 border border-gray-300">
                <img :src="detail.anh.duongDan" alt="Ảnh" class="w-10 h-10 object-cover">
              </td>
              <td class="px-2 py-1 border border-gray-300">{{ detail.dsp.dongSanPham }}</td>
              <td class="px-2 py-1 border border-gray-300">{{ detail.ctsp.giaBan }}</td>
              <td class="px-2 py-1 border border-gray-300"> {{dotGiamGia.soTienGiamToiDa}}</td>
              <td class="px-2 py-1 border border-gray-300">{{ detail.giaSauKhiGiam }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import { onMounted, ref, watch, computed  } from "vue";
import { useRoute } from "vue-router";

const dspList = ref([]);
const ctspList = ref([]);
const searchKeyword = ref("");
const idDSPs = ref([]);

const dotGiamGia = ref({
  id: null,
  ma: "",
  tenDotGiamGia: "",
  loaiGiamGiaApDung: "",
  giaTriGiamGia: 0,
  soTienGiamToiDa: 0,
  ngayBatDau: "",
  ngayKetThuc: "",
  trangThai: false,
  deleted: false
});

const trangThaiComputed = computed(() => {
  if (!dotGiamGia.value.ngayBatDau) return false;

  const ngayBatDau = new Date(dotGiamGia.value.ngayBatDau);
  const ngayHienTai = new Date();

  return ngayBatDau > ngayHienTai;
});



const edit = ref(false);

const capNhatGiaSauKhiGiam = () => {
  const loaiGiamGia = dotGiamGia.value.loaiGiamGiaApDung;
  const giaTriGiam = parseFloat(dotGiamGia.value.giaTriGiamGia) || 0;
  const soTienGiamToiDa = parseFloat(dotGiamGia.value.soTienGiamToiDa) || Infinity;

  ctspList.value = ctspList.value.map((product) => {
    const giaBanDau = parseFloat(product.ctsp.giaBan) || 0;
    let giaSauGiam = giaBanDau;

    if (loaiGiamGia === "Tiền mặt") {
      giaSauGiam = giaBanDau - soTienGiamToiDa;
    } else if (loaiGiamGia === "Phần trăm") {
      const soTienGiam = giaBanDau * (giaTriGiam / 100);
      giaSauGiam = giaBanDau - Math.min(soTienGiam, soTienGiamToiDa);
    }

    return {
      ...product,
      giaSauKhiGiam: giaSauGiam
    };
  });
};

const fetchData = async () => {
  try {
    const res = await axios.post(
      "http://localhost:8080/dot_giam_gia/ViewAddDotGiamGia",
      {
        keyword: searchKeyword.value,
        idDSPs: idDSPs.value || []
      }
    );
    dspList.value = res.data.dspList || [];
    ctspList.value = res.data.ctspList || [];
    dotGiamGia.value.trangThai = trangThaiComputed.value; // Cập nhật trạng thái
    capNhatGiaSauKhiGiam();
  } catch (error) {
    console.error("Lỗi khi gọi API:", error);
  }
};


const fetchDongSanPham = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/dot_giam_gia/viewUpdate?id=${dotGiamGia.value.id}`);
    idDSPs.value = response.data.map(dsp => dsp.id); 
  } catch (error) {
    console.error("Lỗi khi lấy danh sách dòng sản phẩm:", error);
  }
};

// Gọi API khi có ID
watch(() => dotGiamGia.value.id, (newId) => {
  if (newId) fetchDongSanPham();
}, { immediate: true });

const resetForm=()=>{
  dotGiamGia.value={
    id: null,
    ma: "",
    tenDotGiamGia: "",
    loaiGiamGiaApDung: "",
    giaTriGiamGia: 0,
    soTienGiamToiDa: 0,
    ngayBatDau: "",
    ngayKetThuc: "",
    trangThai: false,
    deleted: false
  }
  edit.value = false;
  idDSPs.value=[];
}

const addData = async () => {
  dotGiamGia.value.trangThai = trangThaiComputed.value; // Cập nhật trạng thái trước khi gửi

  const requestData = {
    dotGiamGia: dotGiamGia.value,
    idDSPs: idDSPs.value,
    ctspList: ctspList.value,
  };

  try {
    if(edit.value){
      console.log("Dữ liệu gửi đi:", requestData);
      const response = await axios.put(
        `http://localhost:8080/dot_giam_gia/AddDotGiamGia/${dotGiamGia.value.id}`,
        requestData,
        { headers: { "Content-Type": "application/json" } }
      );
      resetForm();
    } else {
      console.log("Dữ liệu gửi đi:", requestData);
      const response = await axios.post(
        "http://localhost:8080/dot_giam_gia/AddDotGiamGia",
        requestData,
        { headers: { "Content-Type": "application/json" } }
      );
      resetForm();
    }
  } catch (error) {
    console.error("Lỗi khi thêm đợt giảm giá:", error);
    alert("Thêm thất bại!");
  }
};



const route = useRoute();

// Hàm chuyển đổi ngày từ database về định dạng yyyy-MM-dd theo múi giờ địa phương
const formatDateLocal = (dateString) => {
  const date = new Date(dateString);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
};

watch(
  () => route.query,
  (newQuery) => {
    if (newQuery.id) {
      edit.value = true;
      dotGiamGia.value = {
        id: newQuery.id,
        ma: newQuery.ma || "",
        tenDotGiamGia: newQuery.tenDotGiamGia || "",
        loaiGiamGiaApDung: newQuery.loaiGiamGiaApDung || "",
        giaTriGiamGia: newQuery.giaTriGiamGia || "",
        soTienGiamToiDa: newQuery.soTienGiamToiDa || "",
        ngayBatDau: newQuery.ngayBatDau
          ? formatDateLocal(newQuery.ngayBatDau)
          : "",
        ngayKetThuc: newQuery.ngayKetThuc
          ? formatDateLocal(newQuery.ngayKetThuc)
          : "",
        trangThai: newQuery.trangThai || "",
      };
    }
  },
  { immediate: true }
);


watch(() => dotGiamGia.value.ngayBatDau, () => {
  dotGiamGia.value.trangThai = trangThaiComputed.value;
});
watch(
  () => [dotGiamGia.value.loaiGiamGiaApDung, dotGiamGia.value.giaTriGiamGia],
  () => {
    capNhatGiaSauKhiGiam();
  }
);

onMounted(fetchData);

watch(searchKeyword, fetchData);
watch(idDSPs, fetchData);
</script>


<style src="@/assets/VoucherCss/dotVoucher.css"></style>
