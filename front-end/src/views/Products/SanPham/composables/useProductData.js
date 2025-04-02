import { ref } from 'vue';

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

  const resetProductData = () => {
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
  };

  return {
    productData,
    resetProductData
  };
}