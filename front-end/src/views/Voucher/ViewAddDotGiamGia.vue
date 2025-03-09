<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Đợt Giảm Giá"/>
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
                <button class="bg-green-500 text-white px-3 py-1 rounded-md hover:bg-blue-600 transition">Quay về
                </button>
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
                <th class="border border-gray-300 px-2 py-1"></th>
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

      <div class="w-full overflow-hidden bg-white border rounded-md shadow-md p-4 mt-4">
        <h5 class="text-gray-700 font-semibold">Chi Tiết Sản Phẩm</h5>

        <label for="dongSanPham">Dòng sản phẩm:</label>
        <select v-model="selectedDongSanPham" @change="updateBoNhoTrong">
          <option></option>
          <option v-for="dong in uniqueDongSanPhams" :key="dong" :value="dong">
            {{ dong }}
          </option>
        </select>
    
        <label for="boNhoTrong">Bộ nhớ trong:</label>
        <select v-model="selectedBoNhoTrong">
          <option></option>
          <option v-for="boNho in filteredBoNhoTrong" :key="boNho" :value="boNho">
            {{ boNho }}
          </option>
        </select>


        <div class="max-h-[700px] overflow-y-auto">
          <table class="w-full mt-3 border-collapse border border-gray-300">
            <thead class="bg-gray-200">
            <tr>
              <th class="border border-gray-300 px-2 py-1">STT</th>
              <th class="border border-gray-300 px-2 py-1">Ảnh</th>
              <th class="border border-gray-300 px-2 py-1">Tên sản phẩm</th>
              <th class="border border-gray-300 px-2 py-1">Dung lượng bộ nhớ trong</th>
              <th class="border border-gray-300 px-2 py-1">Đơn giá</th>
              <th class="border border-gray-300 px-2 py-1">Số tiền giảm tối đa</th>
              <th class="border border-gray-300 px-2 py-1">Đơn giá sau giảm giá</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(detail, index) in filteredCTSPList" :key="index" class="border border-gray-300">
              <td class="px-2 py-1 border border-gray-300">{{ index + 1 }}</td>
              <td class="px-2 py-1 border border-gray-300">
                <img :src="detail.anh.duongDan" alt="Ảnh" class="w-10 h-10 object-cover">
              </td>
              <td class="px-2 py-1 border border-gray-300">{{ detail.dsp.dongSanPham }}</td>
              <td class="px-2 py-1 border border-gray-300">{{ detail.bnt.dungLuongBoNhoTrong }}</td>
              <td class="px-2 py-1 border border-gray-300">{{ detail.ctsp.giaBan }}</td>
              <td class="px-2 py-1 border border-gray-300"> {{ dotGiamGia.soTienGiamToiDa }}</td>
              <td class="px-2 py-1 border border-gray-300">{{ detail.giaSauKhiGiam }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<!--<script setup>-->
<!--import axios from "axios";-->
<!--import {onMounted, ref, watch, computed} from "vue";-->
<!--import {useRoute} from "vue-router";-->

<!--const dspList = ref([]);-->
<!--const ctspList = ref([]);-->
<!--const searchKeyword = ref("");-->
<!--const idDSPs = ref([]);-->
<!--const selectedDongSanPham = ref(null);-->
<!--const selectedBoNhoTrong = ref(null);-->

<!--const dotGiamGia = ref({-->
<!--  id: null,-->
<!--  ma: "",-->
<!--  tenDotGiamGia: "",-->
<!--  loaiGiamGiaApDung: "",-->
<!--  giaTriGiamGia: 0,-->
<!--  soTienGiamToiDa: 0,-->
<!--  ngayBatDau: "",-->
<!--  ngayKetThuc: "",-->
<!--  trangThai: false,-->
<!--  deleted: false-->
<!--});-->

<!--const edit = ref(false);-->

<!--const capNhatGiaSauKhiGiam = () => {-->
<!--  const loaiGiamGia = dotGiamGia.value.loaiGiamGiaApDung;-->
<!--  const giaTriGiam = parseFloat(dotGiamGia.value.giaTriGiamGia) || 0;-->
<!--  const soTienGiamToiDa = parseFloat(dotGiamGia.value.soTienGiamToiDa) || Infinity;-->

<!--  ctspList.value = ctspList.value.map((product) => {-->
<!--    const giaBanDau = parseFloat(product.ctsp.giaBan) || 0;-->
<!--    let giaSauGiam = giaBanDau;-->

<!--    if (loaiGiamGia === "Tiền mặt") {-->
<!--      giaSauGiam = giaBanDau - soTienGiamToiDa;-->
<!--    } else if (loaiGiamGia === "Phần trăm") {-->
<!--      const soTienGiam = giaBanDau * (giaTriGiam / 100);-->
<!--      giaSauGiam = giaBanDau - Math.min(soTienGiam, soTienGiamToiDa);-->
<!--    }-->

<!--    return {-->
<!--      ...product,-->
<!--      giaSauKhiGiam: giaSauGiam-->
<!--    };-->
<!--  });-->
<!--};-->


<!--// Fetch dữ liệu khi chọn dòng sản phẩm hoặc bộ nhớ trong-->
<!--watch([selectedDongSanPham, selectedBoNhoTrong], async () => {-->
<!--  await fetchData();-->
<!--});-->

<!--const fetchData = async () => {-->
<!--  try {-->
<!--    const res = await axios.post(-->
<!--      "http://localhost:8080/dot_giam_gia/ViewAddDotGiamGia",-->
<!--      {-->
<!--        keyword: searchKeyword.value,-->
<!--        idDSPs: idDSPs.value || [],-->
<!--        idBoNhoTrongs: selectedBoNhoTrong.value ? [selectedBoNhoTrong.value] : null -->
<!--      }-->
<!--    );-->
<!--    dspList.value = res.data.dspList || [];-->
<!--    ctspList.value = res.data.ctspList || [];-->
<!--    capNhatGiaSauKhiGiam();-->
<!--  } catch (error) {-->
<!--    console.error("Lỗi khi gọi API:", error);-->
<!--  }-->
<!--};-->

<!--//Lọc Combo box-->

<!--const uniqueDongSanPhams = computed(() => {-->
<!--  const unique = new Set(ctspList.value.map(ctsp => ctsp.dsp.dongSanPham));-->
<!--  return Array.from(unique);-->
<!--});-->


<!--const filteredBoNhoTrong = computed(() => {-->
<!--  const allBoNhoTrong = ctspList.value.map(ctsp => ctsp.bnt.dungLuongBoNhoTrong);-->
<!--  return [...new Set(allBoNhoTrong)];-->
<!--});-->


<!--const filteredCTSPList = computed(() => {-->
<!--  return ctspList.value.filter(ctsp => {-->
<!--    const matchDongSanPham = selectedDongSanPham.value-->
<!--      ? ctsp.dsp.dongSanPham === selectedDongSanPham.value-->
<!--      : true;-->

<!--    const matchBoNhoTrong = selectedBoNhoTrong.value-->
<!--      ? ctsp.bnt.dungLuongBoNhoTrong === selectedBoNhoTrong.value-->
<!--      : true;-->

<!--    return matchDongSanPham && matchBoNhoTrong;-->
<!--  });-->
<!--});-->


<!--const checkDuplicate = async (field, value, excludeId = null) => {-->
<!--  try {-->
<!--    const {data} = await axios.get(`http://localhost:8080/dot_giam_gia/ViewAddDotGiamGia/exists/${field}`, {-->
<!--      params: {[field]: value, excludeId},-->
<!--    });-->
<!--    return data;-->
<!--  } catch (error) {-->
<!--    console.error("Error calling API:", error);  // In ra lỗi nếu có-->
<!--    alert("Sảy ra lỗi")-->
<!--    return false;-->
<!--  }-->
<!--};-->

<!--const validate = async function () {-->
<!--  const today = new Date().toISOString().split("T")[0];-->
<!--  -->
<!--  if (dotGiamGia.value.ma == "") {-->
<!--    alert("Vui lòng nhập mã");-->
<!--    return false;-->
<!--  }-->

<!--  if (edit.value == false) {-->
<!--    const isDuplicate = await checkDuplicate('ma', dotGiamGia.value.ma);-->
<!--    if (isDuplicate) {-->
<!--      alert("Mã đã tồn tại");-->
<!--      return false;-->
<!--    }-->
<!--  }-->

<!--  if (dotGiamGia.value.loaiGiamGiaApDung == "") {-->
<!--    alert("Vui lòng chọn loại giảm giá");-->
<!--    return false;-->
<!--  }-->

<!--  if (dotGiamGia.value.giaTriGiamGia == 0 && dotGiamGia.value.loaiGiamGiaApDung != "Tiền mặt") {-->
<!--    alert("Vui lòng nhập giá trị giảm giá");-->
<!--    return false;-->
<!--  }-->

<!--  if (dotGiamGia.value.soTienGiamToiDa == 0) {-->
<!--    alert("Vui lòng nhập số tiền giảm tối đa");-->
<!--    return false;-->
<!--  }-->

<!--  if (dotGiamGia.value.ngayBatDau == "") {-->
<!--    alert("Vui lòng chọn ngày bắt đầu");-->
<!--    return false;-->
<!--  }-->
<!--  -->
<!--  if (dotGiamGia.value.ngayBatDau < today) {-->
<!--    alert("Ngày bắt đầu không được nhỏ hơn ngày hiện tại");-->
<!--    return false;-->
<!--  }-->

<!--  if (dotGiamGia.value.ngayKetThuc == "" || dotGiamGia.value.ngayKetThuc < dotGiamGia.value.ngayBatDau) {-->
<!--    alert("Vui lòng chọn lại ngày kết thúc");-->
<!--    return false;-->
<!--  }-->

<!--  if (dotGiamGia.value.ngayKetThuc < dotGiamGia.value.ngayBatDau) {-->
<!--    alert("Ngày kết thúc không được nhỏ hơn ngày bắt đầu");-->
<!--    return false;-->
<!--  }-->

<!--  if (idDSPs.value.length === 0) {-->
<!--    alert("Vui lòng chọn dòng sản phẩm trong đợt giảm giá");-->
<!--    return false;-->
<!--  }-->

<!--  return true;-->
<!--};-->


<!--const fetchDongSanPham = async () => {-->
<!--  try {-->
<!--    const response = await axios.get(`http://localhost:8080/dot_giam_gia/viewUpdate?id=${dotGiamGia.value.id}`);-->
<!--    idDSPs.value = response.data.map(dsp => dsp.id);-->
<!--  } catch (error) {-->
<!--    console.error("Lỗi khi lấy danh sách dòng sản phẩm:", error);-->
<!--  }-->
<!--};-->

<!--// Gọi API khi có ID-->
<!--watch(() => dotGiamGia.value.id, (newId) => {-->
<!--  if (newId) fetchDongSanPham();-->
<!--}, {immediate: true});-->

<!--const resetForm = () => {-->
<!--  dotGiamGia.value = {-->
<!--    id: null,-->
<!--    ma: "",-->
<!--    tenDotGiamGia: "",-->
<!--    loaiGiamGiaApDung: "",-->
<!--    giaTriGiamGia: 0,-->
<!--    soTienGiamToiDa: 0,-->
<!--    ngayBatDau: "",-->
<!--    ngayKetThuc: "",-->
<!--    trangThai: false,-->
<!--    deleted: false-->
<!--  }-->
<!--  edit.value = false;-->
<!--  idDSPs.value = [];-->
<!--}-->

<!--const addData = async () => {-->

<!--  const requestData = {-->
<!--    dotGiamGia: dotGiamGia.value,-->
<!--    idDSPs: idDSPs.value,-->
<!--    ctspList: ctspList.value,-->
<!--  };-->

<!--  const isValid = await validate();-->
<!--  if (isValid) {-->
<!--    try {-->
<!--      validate();-->
<!--      if (edit.value) {-->
<!--        console.log("Dữ liệu gửi đi:", requestData);-->
<!--        const response = await axios.put(-->
<!--          `http://localhost:8080/dot_giam_gia/AddDotGiamGia/${dotGiamGia.value.id}`,-->
<!--          requestData,-->
<!--          {headers: {"Content-Type": "application/json"}}-->
<!--        );-->
<!--        alert("Sửa thành công");-->
<!--        resetForm();-->
<!--      } else {-->
<!--        console.log("Dữ liệu gửi đi:", requestData);-->
<!--        const response = await axios.post(-->
<!--          "http://localhost:8080/dot_giam_gia/AddDotGiamGia",-->
<!--          requestData,-->
<!--          {headers: {"Content-Type": "application/json"}}-->
<!--        );-->
<!--        alert("Thêm thành công");-->
<!--        resetForm();-->
<!--      }-->
<!--    } catch (error) {-->
<!--      console.error("Lỗi khi thêm đợt giảm giá:", error);-->
<!--      alert("Thêm thất bại!");-->
<!--    }-->
<!--  }-->
<!--};-->

<!--const route = useRoute();-->

<!--const formatDateLocal = (dateString) => {-->
<!--  const date = new Date(dateString);-->
<!--  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;-->
<!--};-->

<!--watch(-->
<!--  () => route.query,-->
<!--  (newQuery) => {-->
<!--    if (newQuery.id) {-->
<!--      edit.value = true;-->
<!--      dotGiamGia.value = {-->
<!--        id: newQuery.id,-->
<!--        ma: newQuery.ma || "",-->
<!--        tenDotGiamGia: newQuery.tenDotGiamGia || "",-->
<!--        loaiGiamGiaApDung: newQuery.loaiGiamGiaApDung || "",-->
<!--        giaTriGiamGia: newQuery.giaTriGiamGia || "",-->
<!--        soTienGiamToiDa: newQuery.soTienGiamToiDa || "",-->
<!--        ngayBatDau: newQuery.ngayBatDau-->
<!--          ? formatDateLocal(newQuery.ngayBatDau)-->
<!--          : "",-->
<!--        ngayKetThuc: newQuery.ngayKetThuc-->
<!--          ? formatDateLocal(newQuery.ngayKetThuc)-->
<!--          : "",-->
<!--        trangThai: newQuery.trangThai || "",-->
<!--      };-->
<!--    }-->
<!--  },-->
<!--  {immediate: true}-->
<!--);-->


<!--watch(-->
<!--  () => [dotGiamGia.value.loaiGiamGiaApDung, dotGiamGia.value.giaTriGiamGia],-->
<!--  () => {-->
<!--    capNhatGiaSauKhiGiam();-->
<!--  }-->
<!--);-->


<!--watch(selectedDongSanPham, async () => {-->
<!--  await fetchData();-->
<!--  selectedBoNhoTrong.value = null; // Reset bộ nhớ trong khi chọn dòng mới-->
<!--});-->


<!--onMounted(fetchData);-->

<!--watch(searchKeyword, fetchData);-->
<!--watch(idDSPs, fetchData);-->
<!--</script>-->

<script setup>
import { useDotGiamGia } from './ViewAddDotGiamGia.js';

const {
  dspList,
  ctspList,
  searchKeyword,
  idDSPs,
  selectedDongSanPham,
  selectedBoNhoTrong,
  dotGiamGia,
  edit,
  uniqueDongSanPhams,
  filteredBoNhoTrong,
  filteredCTSPList,
  addData,
  resetForm,
} = useDotGiamGia();
</script>

<style src="@/assets/VoucherCss/dotVoucher.css"></style>
