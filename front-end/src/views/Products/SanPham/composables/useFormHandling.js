import axios from 'axios';
import { useRouter } from 'vue-router';

export function useFormHandling(toast, productData, productVariants, mainImages, colorImages) {
  const router = useRouter(); // Khởi tạo router

  const validateProductData = () => {
    const requiredFields = [
      'tenSanPham', 'idHeDieuHanh', 'idManHinh', 'idNhaSanXuat',
      'idCumCamera', 'idSim', 'idThietKe', 'idPin', 'idCpu',
      'idGpu', 'idCongNgheMang', 'idCongSac', 'idHoTroCongNgheSac',
      'idLoaiTinhTrang'
    ];

    return requiredFields.filter(field => !productData.value[field]);
  };

  const validateVariants = () => {
    return productVariants.value.length === 0;
  };

  const validateImages = () => {
    const allImages = [];
    if (mainImages.value.front.file) allImages.push(mainImages.value.front.file);
    if (mainImages.value.back.file) allImages.push(mainImages.value.back.file);
    Object.values(colorImages.value).forEach((image) => {
      if (image.file) allImages.push(image.file);
    });

    return allImages.length === 0;
  };

  const prepareFormData = () => {
    const formData = new FormData();

    const allImages = [];
    if (mainImages.value.front.file) allImages.push(mainImages.value.front.file);
    if (mainImages.value.back.file) allImages.push(mainImages.value.back.file);
    Object.values(colorImages.value).forEach((image) => {
      if (image.file) allImages.push(image.file);
    });

    productVariants.value.forEach((variant) => {
      const colorImage = colorImages.value[variant.idMauSac];
      variant.imageIndex = colorImage && colorImage.file
        ? allImages.indexOf(colorImage.file)
        : 0;
    });

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
      giaBan: productVariants.value[0]?.donGia || 0,
      createdAt: new Date().toISOString(),
      createdBy: 1,
      updatedAt: new Date().toISOString(),
      updatedBy: 1,
      variants: productVariants.value.map(variant => ({
        idImel: variant.idImel || 1,
        idMauSac: variant.idMauSac,
        idRam: variant.idRam,
        idBoNhoTrong: variant.idBoNhoTrong,
        idLoaiTinhTrang: variant.idLoaiTinhTrang,
        imageIndex: variant.imageIndex,
        soLuong: variant.soLuong,
        donGia: variant.donGia
      }))
    }));

    allImages.forEach((image) => {
      formData.append('images', image);
    });

    return formData;
  };

  const handleSubmit = async () => {
    const missingFields = validateProductData();
    if (missingFields.length > 0) {
      toast.value?.kshowToast('error', `Vui lòng nhập đầy đủ thông tin. Thiếu các trường: ${missingFields.join(', ')}`);
      return;
    }

    if (validateVariants()) {
      toast.value?.kshowToast('error', 'Vui lòng thêm ít nhất một biến thể sản phẩm');
      return;
    }

    if (validateImages()) {
      toast.value?.kshowToast('error', 'Vui lòng tải lên ít nhất một ảnh');
      return;
    }

    try {
      const formData = prepareFormData();
      const response = await axios.post('http://localhost:8080/chi-tiet-san-pham', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      if (response.data) {
        const { sanPhamId, chiTietSanPhamIds, anhSanPhamIds } = response.data.data;
        toast.value?.kshowToast(
          'success',
          `Thêm sản phẩm thành công! ID: ${sanPhamId}, ${chiTietSanPhamIds.length} biến thể, ${anhSanPhamIds.length} ảnh`
        );
        await router.push('/products');
      }
    } catch (error) {
      console.error('Error submitting form:', error);

      let errorMessage = 'Lỗi khi lưu dữ liệu';
      if (error.response) {
        errorMessage += `: ${error.response.data?.message || error.response.statusText}`;
      } else {
        errorMessage += `: ${error.message}`;
      }

      toast.value?.kshowToast('error', errorMessage);
    }
  };

  return {
    handleSubmit
  };
}