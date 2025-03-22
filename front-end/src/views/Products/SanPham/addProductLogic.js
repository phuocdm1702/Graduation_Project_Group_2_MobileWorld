import {ref, onMounted, computed} from 'vue';
import {useRouter, useRoute} from 'vue-router';
import axios from 'axios';

export default function addProductLogic() {
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
    idCongNgheSac: '',
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
  const congNgheSacOptions = ref([]);
  const hoTroCongNgheSacOptions = ref([]);
  const chiSoKhangBuiVaNuocOptions = ref([]);
  const tinhTrangOptions = ref([]);
  const ramOptions = ref([]);
  const boNhoTrongOptions = ref([]);
  const mauSacOptions = ref([]);

  const breadcrumbItems = computed(() => {
    if (typeof route.meta.breadcrumb === "function") {
      return route.meta.breadcrumb(route);
    }
    return route.meta?.breadcrumb || ["Thêm Sản Phẩm"];
  });

  const fetchOptions = async () => {
    try {
      const [
        heDieuHanhRes,
        manHinhRes,
        nhaSanXuatRes,
        cumCameraRes,
        simRes,
        thietKeRes,
        pinRes,
        cpuRes,
        gpuRes,
        congNgheMangRes,
        congSacRes,
        congNgheSacRes,
        // hoTroCongNgheSacRes,
        // chiSoKhangBuiVaNuocRes,
        // tinhTrangRes,
        // ramRes,
        // boNhoTrongRes,
        // mauSacRes,
      ] = await Promise.all([
        axios.get('http://localhost:8080/api/he-dieu-hanh'),
        axios.get('http://localhost:8080/api/man-hinh'),
        axios.get('http://localhost:8080/api/nha-san-xuat'),
        axios.get('http://localhost:8080/api/cum-camera/details'),
        axios.get('http://localhost:8080/api/sim'),
        axios.get('http://localhost:8080/api/thiet-ke'),
        axios.get('http://localhost:8080/api/pin'),
        axios.get('http://localhost:8080/api/cpu'),
        axios.get('http://localhost:8080/api/gpu'),
        axios.get('http://localhost:8080/api/cong-nghe-mang'),
        axios.get('http://localhost:8080/api/cong-sac'),
        axios.get('http://localhost:8080/api/cong-nghe-sac'),
        // axios.get('http://localhost:8080/api/chi-tiet-san-pham/ho-tro-cong-nghe-sacs'),
        // axios.get('http://localhost:8080/api/chi-tiet-san-pham/chi-so-khang-bui-va-nuocs'),
        // axios.get('http://localhost:8080/api/chi-tiet-san-pham/tinh-trangs'),
        // axios.get('http://localhost:8080/api/chi-tiet-san-pham/rams'),
        // axios.get('http://localhost:8080/api/chi-tiet-san-pham/bo-nho-trongs'),
        // axios.get('http://localhost:8080/api/chi-tiet-san-pham/mau-sacs'),
      ]);

      heDieuHanhOptions.value = heDieuHanhRes.data.content;
      manHinhOptions.value = manHinhRes.data.content;
      nhaSanXuatOptions.value = nhaSanXuatRes.data.content;
      cumCameraOptions.value = cumCameraRes.data.content;
      simOptions.value = simRes.data.content;
      thietKeOptions.value = thietKeRes.data.content;
      pinOptions.value = pinRes.data.content;
      cpuOptions.value = cpuRes.data.content;
      gpuOptions.value = gpuRes.data.content;
      congNgheMangOptions.value = congNgheMangRes.data.content;
      congSacOptions.value = congSacRes.data.content;
      congNgheSacOptions.value = congNgheSacRes.data.content;
      // hoTroCongNgheSacOptions.value = hoTroCongNgheSacRes.data;
      // chiSoKhangBuiVaNuocOptions.value = chiSoKhangBuiVaNuocRes.data;
      // tinhTrangOptions.value = tinhTrangRes.data;
      // ramOptions.value = ramRes.data;
      // boNhoTrongOptions.value = boNhoTrongRes.data;
      // mauSacOptions.value = mauSacRes.data;
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
      productVariants.value.push({...currentVariant.value});
      currentVariant.value = {idRam: '', idBoNhoTrong: '', idMauSac: ''};
    } else {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Vui lòng điền đầy đủ thông tin phiên bản!');
      }
    }
  };

  const addImage = () => {
    if (currentImage.value.file) {
      productImages.value.push({...currentImage.value});
      currentImage.value = {file: null, fileName: ''};
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
        headers: {'Content-Type': 'multipart/form-data'},
      });
      if (toast.value) {
        toast.value?.kshowToast('success', 'Thêm mới thành công!');
      }
      await router.push('/san-pham');
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
      idCongNgheSac: '',
      idHoTroCongNgheSac: '',
      idChiSoKhangBuiVaNuoc: '',
      idLoaiTinhTrang: '',
      tienIchDacBiet: '',
      giaBan: '',
    };
    productVariants.value = [];
    productImages.value = [];
    currentVariant.value = {idRam: '', idBoNhoTrong: '', idMauSac: ''};
    currentImage.value = {file: null, fileName: ''};
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
          break;
        case 'heDieuHanh':
          response = await axios.post('http://localhost:8080/api/he-dieu-hanhs', {tenHeDieuHanh: data.tenHeDieuHanh});
          heDieuHanhOptions.value.push(response.data);
          break;
        case 'manHinh':
          response = await axios.post('http://localhost:8080/api/man-hinhs', {kichThuoc: data.kichThuoc});
          manHinhOptions.value.push(response.data);
          break;
        case 'nhaSanXuat':
          response = await axios.post('http://localhost:8080/api/nha-san-xuats', {tenNhaSanXuat: data.tenNhaSanXuat});
          nhaSanXuatOptions.value.push(response.data);
          break;
        case 'cumCamera':
          response = await axios.post('http://localhost:8080/api/cum-cameras', {tenCamera: data.tenCamera});
          cumCameraOptions.value.push(response.data);
          break;
        case 'sim':
          response = await axios.post('http://localhost:8080/api/sims', {loaiSim: data.loaiSim});
          simOptions.value.push(response.data);
          break;
        case 'thietKe':
          response = await axios.post('http://localhost:8080/api/thiet-kes', {tenThietKe: data.tenThietKe});
          thietKeOptions.value.push(response.data);
          break;
        case 'pin':
          response = await axios.post('http://localhost:8080/api/pins', {dungLuong: data.dungLuong});
          pinOptions.value.push(response.data);
          break;
        case 'cpu':
          response = await axios.post('http://localhost:8080/api/cpus', {tenCpu: data.tenCpu});
          cpuOptions.value.push(response.data);
          break;
        case 'gpu':
          response = await axios.post('http://localhost:8080/api/gpus', {tenGpu: data.tenGpu});
          gpuOptions.value.push(response.data);
          break;
        case 'congNgheMang':
          response = await axios.post('http://localhost:8080/api/cong-nghe-mangs', {tenCongNgheMang: data.tenCongNgheMang});
          congNgheMangOptions.value.push(response.data);
          break;
        case 'congSac':
          response = await axios.post('http://localhost:8080/api/cong-sacs', {congSac: data.congSac});
          congSacOptions.value.push(response.data);
          break;
        case 'congNgheSac':
          response = await axios.post('http://localhost:8080/api/cong-nghe-sacs', {tenCongNghe: data.tenCongNghe});
          congSacOptions.value.push(response.data);
          break;
        case 'hoTroCongNgheSac':
          response = await axios.post('http://localhost:8080/api/ho-tro-cong-nghe-sacs', {ten: data.ten});
          hoTroCongNgheSacOptions.value.push(response.data);
          break;
        case 'chiSoKhangBuiVaNuoc':
          response = await axios.post('http://localhost:8080/api/chi-so-khang-bui-va-nuocs', {maChiSo: data.maChiSo});
          chiSoKhangBuiVaNuocOptions.value.push(response.data);
          break;
        case 'tinhTrang':
          response = await axios.post('http://localhost:8080/api/tinh-trangs', {tenTinhTrang: data.tenTinhTrang});
          tinhTrangOptions.value.push(response.data);
          break;
        case 'ram':
          response = await axios.post('http://localhost:8080/api/rams', {dungLuong: data.dungLuong});
          ramOptions.value.push(response.data);
          break;
        case 'boNhoTrong':
          response = await axios.post('http://localhost:8080/api/bo-nho-trongs', {dungLuong: data.dungLuong});
          boNhoTrongOptions.value.push(response.data);
          break;
        case 'mauSac':
          response = await axios.post('http://localhost:8080/api/mau-sacs', {tenMau: data.tenMau});
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

  return {
    toast,
    showFormModal,
    currentAttribute,
    productData,
    productVariants,
    currentVariant,
    productImages,
    currentImage,
    heDieuHanhOptions,
    manHinhOptions,
    nhaSanXuatOptions,
    cumCameraOptions,
    simOptions,
    thietKeOptions,
    pinOptions,
    cpuOptions,
    gpuOptions,
    congNgheMangOptions,
    congSacOptions,
    congNgheSacOptions,
    hoTroCongNgheSacOptions,
    chiSoKhangBuiVaNuocOptions,
    tinhTrangOptions,
    ramOptions,
    boNhoTrongOptions,
    mauSacOptions,
    breadcrumbItems,
    fetchOptions,
    handleImageUpload,
    addVariant,
    addImage,
    handleSubmit,
    resetForm,
    openAddModal,
    handleAddAttribute,
    closeFormModal,
  };
}