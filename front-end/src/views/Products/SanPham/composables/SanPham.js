import { ref, computed, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import '@/assets/sanPham.css';

export default function sanPham(toast) {
  const router = useRouter();

  // State
  const productDetails = ref([]);
  const searchKeyword = ref('');
  const searchFilters = ref({
    idNhaSanXuat: '',
    idHeDieuHanh: '',
    heDieuHanh: '',
    phienBan: '',
    idCongNgheManHinh: '',
    congNgheManHinh: '',
    chuanManHinh: '',
    idPin: '',
    loaiPin: '',
    dungLuongPin: '',
    idCpu: '',
    idGpu: '',
    idCumCamera: '',
    idThietKe: '',
    idSim: '',
    idHoTroCongNgheSac: '',
    idCongNgheMang: '',
    stockStatus: '',
  });
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const isSearching = ref(false);
  const showConfirmModal = ref(false);
  const confirmMessage = ref('');
  const confirmedAction = ref(null);

  // Options for all filters
  const nhaSanXuatOptions = ref([]);
  const heDieuHanhOptions = ref([]);
  const congNgheManHinhOptions = ref([]);
  const pinOptions = ref([]);
  const cpuOptions = ref([]);
  const gpuOptions = ref([]);
  const cumCameraOptions = ref([]);
  const thietKeOptions = ref([]);
  const simOptions = ref([]);
  const hoTroCongNgheSacOptions = ref([]);
  const congNgheMangOptions = ref([]);

  // Computed properties
  const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

  // Fetch product details
  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/san-pham', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      productDetails.value = data.content.map(item => ({
        ...item,
        nhaSanXuat: item.nhaSanXuat || 'N/A',
        heDieuHanh: item.heDieuHanh || 'N/A',
        phienBan: item.phienBan || 'N/A',
        congNgheManHinh: item.congNgheManHinh || 'N/A',
        chuanManHinh: item.chuanManHinh || 'N/A',
        tenCpu: item.tenCpu || 'N/A',
        cameraTruoc: item.cameraTruoc || 'N/A',
        cameraSau: item.cameraSau || 'N/A',
        loaiPin: item.loaiPin || 'N/A',
        dungLuongPin: item.dungLuongPin || 'N/A',
        soLuongSimHoTro: item.soLuongSimHoTro || 'N/A',
        cacLoaiSimHoTro: item.cacLoaiSimHoTro || 'N/A',
        imeiCount: item.imeiCount || 0,
        minPrice: item.minPrice || 0,
        maxPrice: item.maxPrice || 0,
      }));
      totalItems.value = data.totalElements;
    } catch (error) {
      toast.value?.kshowToast('error', 'Không thể tải dữ liệu!');
      console.error('Fetch error:', error);
    }
  };

  // Fetch options for all filters
  const fetchOptions = async () => {
    try {
      const [
        nhaSanXuatRes,
        heDieuHanhRes,
        congNgheManHinhRes,
        pinRes,
        cumCameraRes,
        cpuRes,
        gpuRes,
        thietKeRes,
        simRes,
        congNgheMangRes,
      ] = await Promise.all([
        axios.get('http://localhost:8080/nha-san-xuat/all'),
        axios.get('http://localhost:8080/he-dieu-hanh/all'),
        axios.get('http://localhost:8080/cong-nghe-man-hinh/all'),
        axios.get('http://localhost:8080/pin/all'),
        axios.get('http://localhost:8080/cum-camera/all'),
        axios.get('http://localhost:8080/cpu/all'),
        axios.get('http://localhost:8080/gpu/all'),
        axios.get('http://localhost:8080/thiet-ke/all'),
        axios.get('http://localhost:8080/sim/all'),
        axios.get('http://localhost:8080/cong-nghe-mang/all'),
      ]);

      console.log('nhaSanXuatRes:', nhaSanXuatRes.data);
      console.log('heDieuHanhRes:', heDieuHanhRes.data);
      console.log('congNgheManHinhRes:', congNgheManHinhRes.data);
      console.log('pinRes:', pinRes.data);

      nhaSanXuatOptions.value = nhaSanXuatRes.data.content || nhaSanXuatRes.data || [];
      heDieuHanhOptions.value = heDieuHanhRes.data.content || heDieuHanhRes.data || [];
      congNgheManHinhOptions.value = congNgheManHinhRes.data.content || congNgheManHinhRes.data || [];
      pinOptions.value = pinRes.data.content || pinRes.data || [];
      cpuOptions.value = cpuRes.data.content || cpuRes.data || [];
      gpuOptions.value = gpuRes.data.content || gpuRes.data || [];
      cumCameraOptions.value = cumCameraRes.data.content || cumCameraRes.data || [];
      thietKeOptions.value = thietKeRes.data.content || thietKeRes.data || [];
      simOptions.value = simRes.data.content || simRes.data || [];
      congNgheMangOptions.value = congNgheMangRes.data.content || congNgheMangRes.data || [];

      console.log('heDieuHanhOptions:', heDieuHanhOptions.value);
      console.log('congNgheManHinhOptions:', congNgheManHinhOptions.value);
      console.log('pinOptions:', pinOptions.value);
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
      console.error('Fetch options error:', error);
    }
  };

  // Go to specific page
  const goToPage = async (page) => {
    currentPage.value = page;
    if (isSearching.value) {
      await searchProductDetails();
    } else {
      await fetchData();
    }
  };

  // Search product details with all filters
  const searchProductDetails = async () => {
    const keyword = searchKeyword.value;
    try {
      let inStock;
      if (searchFilters.value.stockStatus === 'inStock') {
        inStock = true;
      } else if (searchFilters.value.stockStatus === 'outOfStock') {
        inStock = false;
      } else {
        inStock = undefined;
      }

      const params = {
        keyword: keyword || undefined,
        idNhaSanXuat: searchFilters.value.idNhaSanXuat || undefined,
        idHeDieuHanh: searchFilters.value.idHeDieuHanh || undefined,
        heDieuHanh: searchFilters.value.heDieuHanh || undefined,
        phienBan: searchFilters.value.phienBan || undefined,
        idCongNgheManHinh: searchFilters.value.idCongNgheManHinh || undefined,
        congNgheManHinh: searchFilters.value.congNgheManHinh || undefined,
        chuanManHinh: searchFilters.value.chuanManHinh || undefined,
        idPin: searchFilters.value.idPin || undefined,
        loaiPin: searchFilters.value.loaiPin || undefined,
        dungLuongPin: searchFilters.value.dungLuongPin || undefined,
        idCpu: searchFilters.value.idCpu || undefined,
        idGpu: searchFilters.value.idGpu || undefined,
        idCumCamera: searchFilters.value.idCumCamera || undefined,
        idThietKe: searchFilters.value.idThietKe || undefined,
        idSim: searchFilters.value.idSim || undefined,
        idHoTroCongNgheSac: searchFilters.value.idHoTroCongNgheSac || undefined,
        idCongNgheMang: searchFilters.value.idCongNgheMang || undefined,
        inStock: inStock,
        page: currentPage.value,
        size: pageSize.value,
      };

      console.log('Search params:', params);

      const { data } = await axios.get('http://localhost:8080/san-pham/search', { params });
      console.log('API response:', data);

      productDetails.value = data.content.map(item => ({
        ...item,
        nhaSanXuat: item.nhaSanXuat || 'N/A',
        heDieuHanh: item.heDieuHanh || 'N/A',
        phienBan: item.phienBan || 'N/A',
        congNgheManHinh: item.congNgheManHinh || 'N/A',
        chuanManHinh: item.chuanManHinh || 'N/A',
        tenCpu: item.tenCpu || 'N/A',
        cameraTruoc: item.cameraTruoc || 'N/A',
        cameraSau: item.cameraSau || 'N/A',
        loaiPin: item.loaiPin || 'N/A',
        dungLuongPin: item.dungLuongPin || 'N/A',
        soLuongSimHoTro: item.soLuongSimHoTro || 'N/A',
        cacLoaiSimHoTro: item.cacLoaiSimHoTro || 'N/A',
        imeiCount: item.imeiCount || 0,
        minPrice: item.minPrice || 0,
        maxPrice: item.maxPrice || 0,
      }));

      totalItems.value = data.totalElements;
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi tìm kiếm!');
      console.error('Search error:', error);
    }
  };

  // Reset search
  const resetSearch = () => {
    searchKeyword.value = '';
    searchFilters.value = {
      idNhaSanXuat: '',
      idHeDieuHanh: '',
      heDieuHanh: '',
      phienBan: '',
      idCongNgheManHinh: '',
      congNgheManHinh: '',
      chuanManHinh: '',
      idPin: '',
      loaiPin: '',
      dungLuongPin: '',
      idCpu: '',
      idGpu: '',
      idCumCamera: '',
      idThietKe: '',
      idSim: '',
      idHoTroCongNgheSac: '',
      idCongNgheMang: '',
      stockStatus: '',
    };
    isSearching.value = false;
    currentPage.value = 0;
    fetchData();
  };

  // Navigation
  const navigateToAddPage = () => router.push('/products/add');
  const navigateToEditPage = (id) => router.push(`/products/edit/${id}`);

  // Utility
  const getNestedValue = (obj, path) => path.split('.').reduce((acc, part) => acc && acc[part], obj);

  // Modal handlers
  const confirmAction = (message, action) => {
    confirmMessage.value = message;
    confirmedAction.value = action;
    showConfirmModal.value = true;
  };

  const executeConfirmedAction = () => {
    if (confirmedAction.value) confirmedAction.value();
    closeConfirmModal();
  };

  const closeConfirmModal = () => {
    showConfirmModal.value = false;
    confirmedAction.value = null;
  };

  // Event handlers
  const handleCustomEvents = () => {
    document.addEventListener('viewDetails', (event) => {
      router.push(`/products/${event.detail.id}`);
    });
    document.addEventListener('editProduct', (event) => {
      navigateToEditPage(event.detail);
    });
  };

  // Watchers
  watch(
    [searchKeyword, searchFilters],
    () => {
      currentPage.value = 0;
      if (searchKeyword.value || Object.values(searchFilters.value).some(val => val)) {
        isSearching.value = true;
        searchProductDetails();
      } else {
        isSearching.value = false;
        fetchData();
      }
    },
    { deep: true }
  );

  // Lifecycle
  onMounted(() => {
    fetchData();
    fetchOptions();
    handleCustomEvents();
  });

  return {
    productDetails,
    searchKeyword,
    searchFilters,
    currentPage,
    totalPages,
    nhaSanXuatOptions,
    heDieuHanhOptions,
    congNgheManHinhOptions,
    pinOptions,
    cpuOptions,
    gpuOptions,
    cumCameraOptions,
    thietKeOptions,
    simOptions,
    hoTroCongNgheSacOptions,
    congNgheMangOptions,
    fetchData,
    fetchOptions,
    goToPage,
    searchProductDetails,
    resetSearch,
    navigateToEditPage,
    navigateToAddPage,
    getNestedValue,
    showConfirmModal,
    confirmMessage,
    confirmAction,
    executeConfirmedAction,
    closeConfirmModal,
  };
}