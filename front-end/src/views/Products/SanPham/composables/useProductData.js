import { ref, onMounted } from 'vue';

export function useProductData() {
  const productData = ref({
    tenSanPham: '',
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
    createdBy: 'admin',
    updatedBy: 'admin'
  });

  const setDefaultValues = (options) => {
    productData.value.idHeDieuHanh = options.heDieuHanhOptions[0]?.id || '';
    productData.value.idManHinh = options.manHinhOptions[0]?.id || '';
    productData.value.idNhaSanXuat = options.nhaSanXuatOptions[0]?.id || '';
    productData.value.idCumCamera = options.cumCameraOptions[0]?.id || '';
    productData.value.idSim = options.simOptions[0]?.id || '';
    productData.value.idThietKe = options.thietKeOptions[0]?.id || '';
    productData.value.idPin = options.pinOptions[0]?.id || '';
    productData.value.idCpu = options.cpuOptions[0]?.id || '';
    productData.value.idGpu = options.gpuOptions[0]?.id || '';
    productData.value.idCongNgheMang = options.congNgheMangOptions[0]?.id || '';
    productData.value.idCongSac = options.congSacOptions[0]?.id || '';
    productData.value.idHoTroCongNgheSac = options.hoTroCongNgheSacOptions[0]?.id || '';
    productData.value.idChiSoKhangBuiVaNuoc = options.chiSoKhangBuiVaNuocOptions[0]?.id || '';
    productData.value.idLoaiTinhTrang = options.tinhTrangOptions[0]?.id || '';
  };

  const resetProductData = (options = {}) => {
    productData.value = {
      tenSanPham: '',
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