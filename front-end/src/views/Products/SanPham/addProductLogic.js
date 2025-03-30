import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
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
    idHoTroCongNgheSac: '',
    idChiSoKhangBuiVaNuoc: '',
    idLoaiTinhTrang: '',
    tienIchDacBiet: '',
    giaBan: '',
  });

  const productVariants = ref([]);
  const currentVariant = ref({
    selectedRams: [],
    selectedBoNhoTrongs: [],
    selectedMauSacs: [],
  });

  const productImages = ref([]);
  const currentImage = ref({
    file: null,
    fileName: '',
  });

  const variantImages = ref({});

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
  const hoTroCongNgheSacOptions = ref([]);
  const chiSoKhangBuiVaNuocOptions = ref([]);
  const tinhTrangOptions = ref([]);
  const ramOptions = ref([]);
  const boNhoTrongOptions = ref([]);
  const mauSacOptions = ref([]);

  const dropdownOpen = ref({
    ram: false,
    boNhoTrong: false,
    mauSac: false,
  });

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
        hoTroCongNgheSacRes,
        chiSoKhangBuiVaNuocRes,
        tinhTrangRes,
        ramRes,
        boNhoTrongRes,
        mauSacRes,
      ] = await Promise.all([
        axios.get('http://localhost:8080/he-dieu-hanh'),
        axios.get('http://localhost:8080/man-hinh'),
        axios.get('http://localhost:8080/nha-san-xuat'),
        axios.get('http://localhost:8080/cum-camera/details'),
        axios.get('http://localhost:8080/sim'),
        axios.get('http://localhost:8080/thiet-ke'),
        axios.get('http://localhost:8080/pin'),
        axios.get('http://localhost:8080/cpu'),
        axios.get('http://localhost:8080/gpu'),
        axios.get('http://localhost:8080/cong-nghe-mang'),
        axios.get('http://localhost:8080/cong-sac'),
        axios.get('http://localhost:8080/ho-tro-cong-nghe-sac/details'),
        axios.get('http://localhost:8080/chi-so-khang-bui-va-nuoc'),
        axios.get('http://localhost:8080/tinh-trang'),
        axios.get('http://localhost:8080/ram'),
        axios.get('http://localhost:8080/bo-nho-trong'),
        axios.get('http://localhost:8080/mau-sac'),
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
      hoTroCongNgheSacOptions.value = hoTroCongNgheSacRes.data.content;
      chiSoKhangBuiVaNuocOptions.value = chiSoKhangBuiVaNuocRes.data.content;
      tinhTrangOptions.value = tinhTrangRes.data.content;
      ramOptions.value = ramRes.data.content;
      boNhoTrongOptions.value = boNhoTrongRes.data.content;
      mauSacOptions.value = mauSacRes.data.content;
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
      }
      console.error('Fetch options error:', error);
    }
  };

  const toggleDropdown = (type) => {
    dropdownOpen.value[type] = !dropdownOpen.value[type];
    Object.keys(dropdownOpen.value).forEach((key) => {
      if (key !== type) {
        dropdownOpen.value[key] = false;
      }
    });
  };

  const handleImageUpload = (event) => {
    currentImage.value.file = event.target.files[0];
    currentImage.value.fileName = event.target.files[0] ? event.target.files[0].name : 'No file chosen';
  };

  const handleVariantImageUpload = (event, index) => {
    const file = event.target.files[0];
    if (file) {
      variantImages.value[index] = {
        file,
        fileName: file.name,
      };
    }
  };

  const addVariant = () => {
    if (
      currentVariant.value.selectedRams.length === 0 ||
      currentVariant.value.selectedBoNhoTrongs.length === 0 ||
      currentVariant.value.selectedMauSacs.length === 0
    ) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Vui lòng chọn ít nhất một RAM, một Bộ Nhớ Trong và một Màu Sắc!');
      }
      return;
    }

    const newVariants = [];
    currentVariant.value.selectedRams.forEach((ramId) => {
      currentVariant.value.selectedBoNhoTrongs.forEach((boNhoId) => {
        currentVariant.value.selectedMauSacs.forEach((mauSacId) => {
          newVariants.push({
            idRam: ramId,
            idBoNhoTrong: boNhoId,
            idMauSac: mauSacId,
            soLuong: 0,
            donGia: productData.value.giaBan || '',
          });
        });
      });
    });

    productVariants.value.push(...newVariants);
    currentVariant.value = {
      selectedRams: [],
      selectedBoNhoTrongs: [],
      selectedMauSacs: [],
    };
  };

  const removeVariant = (index) => {
    productVariants.value.splice(index, 1);
    delete variantImages.value[index];
  };

  const addImage = () => {
    if (currentImage.value.file) {
      productImages.value.push({ ...currentImage.value });
      currentImage.value = { file: null, fileName: '' };
    } else {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Vui lòng chọn file ảnh!');
      }
    }
  };

  const handleSubmit = async () => {
    if (
      !productData.value.id ||
      !productData.value.giaBan ||
      Object.values(productData.value).some((val) => val === '')
    ) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Vui lòng nhập đầy đủ thông tin sản phẩm!');
      }
      return;
    }

    const formData = new FormData();// Thay đổi cách thêm dữ liệu vào FormData
    formData.append('dto', JSON.stringify({
      tenSanPham: productData.value.tenSanPham,
      idNhaSanXuat: productData.value.idNhaSanXuat,
      idPin: productData.value.idPin,
      idManHinh: productData.value.idManHinh,
      idCpu: productData.value.idCpu,
      idGpu: productData.value.idGpu,
      idCumCamera: productData.value.idCumCamera,
      idHeDieuHanh: productData.value.idHeDieuHanh,
      idThietKe: productData.value.idThietKe,
      idSim: productData.value.idSim,
      idCongSac: productData.value.idCongSac,
      idHoTroCongNgheSac: productData.value.idHoTroCongNgheSac,
      idCongNgheMang: productData.value.idCongNgheMang,
      idChiSoKhangBuiVaNuoc: productData.value.idChiSoKhangBuiVaNuoc || null,
      tienIchDacBiet: productData.value.tienIchDacBiet,
      giaBan: productVariants.value[0].donGia, // Lấy giá từ biến thể đầu tiên
      createdBy: 'admin', // Thay bằng ID người dùng thực tế
      updatedBy: 'admin', // Thay bằng ID người dùng thực tế
      variants: productVariants.value.map(variant => ({
        idImel: variant.idImel,
        idMauSac: variant.idMauSac,
        idRam: variant.idRam,
        idBoNhoTrong: variant.idBoNhoTrong,
        idLoaiTinhTrang: variant.idLoaiTinhTrang,
        imageIndex: variant.imageIndex,
        soLuong: variant.soLuong,
        donGia: variant.donGia
      }))
    }));

    // Thêm ảnh vào FormData
    productImages.value.forEach((image, index) => {
      formData.append('images', image.file);
    });

    try {
      const response = await axios.post('http://localhost:8080/chi-tiet-san-pham', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      if (toast.value) {
        toast.value?.kshowToast('success', 'Thêm sản phẩm thành công!');
      }
      await router.push('/san-pham');
    } catch (error) {
      console.error('Error:', error);
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi lưu dữ liệu: ' + (error.response?.data?.message || error.message));
      }
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
      idHoTroCongNgheSac: '',
      idChiSoKhangBuiVaNuoc: '',
      idLoaiTinhTrang: '',
      tienIchDacBiet: '',
      giaBan: '',
    };
    productVariants.value = [];
    productImages.value = [];
    variantImages.value = {};
    currentVariant.value = { selectedRams: [], selectedBoNhoTrongs: [], selectedMauSacs: [] };
    currentImage.value = { file: null, fileName: '' };
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
          response = await axios.post('http://localhost:8080/he-dieu-hanhs', { tenHeDieuHanh: data.tenHeDieuHanh });
          heDieuHanhOptions.value.push(response.data);
          break;
        case 'manHinh':
          response = await axios.post('http://localhost:8080/man-hinhs', { kichThuoc: data.kichThuoc });
          manHinhOptions.value.push(response.data);
          break;
        case 'nhaSanXuat':
          response = await axios.post('http://localhost:8080/nha-san-xuats', { tenNhaSanXuat: data.tenNhaSanXuat });
          nhaSanXuatOptions.value.push(response.data);
          break;
        case 'cumCamera':
          response = await axios.post('http://localhost:8080/cum-cameras', { tenCamera: data.tenCamera });
          cumCameraOptions.value.push(response.data);
          break;
        case 'sim':
          response = await axios.post('http://localhost:8080/sims', { loaiSim: data.loaiSim });
          simOptions.value.push(response.data);
          break;
        case 'thietKe':
          response = await axios.post('http://localhost:8080/thiet-kes', { tenThietKe: data.tenThietKe });
          thietKeOptions.value.push(response.data);
          break;
        case 'pin':
          response = await axios.post('http://localhost:8080/pins', { dungLuong: data.dungLuong });
          pinOptions.value.push(response.data);
          break;
        case 'cpu':
          response = await axios.post('http://localhost:8080/cpus', { tenCpu: data.tenCpu });
          cpuOptions.value.push(response.data);
          break;
        case 'gpu':
          response = await axios.post('http://localhost:8080/gpus', { tenGpu: data.tenGpu });
          gpuOptions.value.push(response.data);
          break;
        case 'congNgheMang':
          response = await axios.post('http://localhost:8080/cong-nghe-mangs', { tenCongNgheMang: data.tenCongNgheMang });
          congNgheMangOptions.value.push(response.data);
          break;
        case 'congSac':
          response = await axios.post('http://localhost:8080/cong-sacs', { congSac: data.congSac });
          congSacOptions.value.push(response.data);
          break;
        case 'hoTroCongNgheSac':
          response = await axios.post('http://localhost:8080/ho-tro-cong-nghe-sacs', { ten: data.ten });
          hoTroCongNgheSacOptions.value.push(response.data);
          break;
        case 'chiSoKhangBuiVaNuoc':
          response = await axios.post('http://localhost:8080/chi-so-khang-bui-va-nuocs', { tenChiSo: data.tenChiSo });
          chiSoKhangBuiVaNuocOptions.value.push(response.data);
          break;
        case 'tinhTrang':
          response = await axios.post('http://localhost:8080/tinh-trangs', { loaiTinhTrang: data.loaiTinhTrang });
          tinhTrangOptions.value.push(response.data);
          break;
        case 'ram':
          response = await axios.post('http://localhost:8080/ram', { ma: data.ma, dungLuong: data.dungLuong });
          ramOptions.value.push(response.data);
          break;
        case 'boNhoTrong':
          response = await axios.post('http://localhost:8080/bo-nho-trong', { ma: data.ma, dungLuong: data.dungLuong });
          boNhoTrongOptions.value.push(response.data);
          break;
        case 'mauSac':
          response = await axios.post('http://localhost:8080/mau-sac', { ma: data.ma, tenMau: data.tenMau });
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
    variantImages,
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
    hoTroCongNgheSacOptions,
    chiSoKhangBuiVaNuocOptions,
    tinhTrangOptions,
    ramOptions,
    boNhoTrongOptions,
    mauSacOptions,
    breadcrumbItems,
    dropdownOpen,
    fetchOptions,
    toggleDropdown,
    handleImageUpload,
    handleVariantImageUpload,
    addVariant,
    removeVariant,
    addImage,
    handleSubmit,
    resetForm,
    openAddModal,
    handleAddAttribute,
    closeFormModal,
  };
}