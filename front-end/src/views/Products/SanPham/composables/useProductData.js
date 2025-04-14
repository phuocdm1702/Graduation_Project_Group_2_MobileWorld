import { ref, onMounted } from 'vue';

export function useProductData() {
  const productData = ref({
    tenSanPham: '',
    idHeDieuHanh: '',
    congNgheManHinh: '',
    idNhaSanXuat: '',
    idCumCamera: '',
    idSim: '',
    idThietKe: '',
    idPin: '',
    idCpu: '',
    idGpu: '',
    idCongNgheMang: '',
    hoTroCongNgheSac: '',
    idChiSoKhangBuiVaNuoc: '',
    createdBy: 'admin',
    updatedBy: 'admin'
  });

  const setDefaultValues = (options) => {
    productData.value.idHeDieuHanh = options.heDieuHanhOptions?.[0]?.id || '';
    productData.value.congNgheManHinh = options.congNgheManHinhOptions?.[0]?.id || '';
    productData.value.idNhaSanXuat = options.nhaSanXuatOptions?.[0]?.id || '';
    productData.value.idCumCamera = options.cumCameraOptions?.[0]?.id || '';
    productData.value.idSim = options.simOptions?.[0]?.id || '';
    productData.value.idThietKe = options.thietKeOptions?.[0]?.id || '';
    productData.value.idPin = options.pinOptions?.[0]?.id || '';
    productData.value.idCpu = options.cpuOptions?.[0]?.id || '';
    productData.value.idGpu = options.gpuOptions?.[0]?.id || '';
    productData.value.idCongNgheMang = options.congNgheMangOptions?.[0]?.id || '';
    productData.value.hoTroCongNgheSac = options.hoTroCongNgheSacOptions?.[0]?.id || '';
    productData.value.idChiSoKhangBuiVaNuoc = options.chiSoKhangBuiVaNuocOptions?.[0]?.id || '';
  };

  const resetProductData = (options = {}) => {
    productData.value = {
      tenSanPham: '',
      idHeDieuHanh: '',
      congNgheManHinh: '',
      idNhaSanXuat: '',
      idCumCamera: '',
      idSim: '',
      idThietKe: '',
      idPin: '',
      idCpu: '',
      idGpu: '',
      idCongNgheMang: '',
      hoTroCongNgheSac: '',
      idChiSoKhangBuiVaNuoc: '',
      createdBy: 'admin',
      updatedBy: 'admin'
    };
    if (Object.keys(options).length > 0) {
      setDefaultValues(options);
    }
  };

  return {
    productData,
    resetProductData,
    setDefaultValues
  };
}