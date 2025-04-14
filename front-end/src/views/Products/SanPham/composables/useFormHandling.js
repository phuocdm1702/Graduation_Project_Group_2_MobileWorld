import axios from 'axios';
import { useRouter } from 'vue-router';

export function useFormHandling(toast, productData, productVariants, mainImages, colorImages, variantImeis) {
  const router = useRouter();

  const validateProductData = () => {
    const requiredFields = [
      'tenSanPham', 'idHeDieuHanh', 'congNgheManHinh', 'idNhaSanXuat',
      'idCumCamera', 'idSim', 'idThietKe', 'idPin', 'idCpu',
      'idGpu', 'idCongNgheMang', 'hoTroCongNgheSac'
    ];

    return requiredFields.filter(field => {
      const value = productData.value[field];
      return value === null || value === '' || value === undefined;
    });
  };

  const validateVariants = () => {
    if (productVariants.value.length === 0) return true;
    return productVariants.value.some(variant => !variant.donGia || variant.donGia === '');
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

    productVariants.value.forEach((variant, index) => {
      const colorImage = colorImages.value[variant.idMauSac];
      variant.imageIndex = colorImage && colorImage.file
        ? allImages.indexOf(colorImage.file)
        : 0;
    });

    const giaBan = productVariants.value.length > 0 ? productVariants.value[0].donGia : '0';

    // Only include the `id` values, not the full objects
    formData.append('dto', JSON.stringify({
      tenSanPham: productData.value.tenSanPham,
      idNhaSanXuat: productData.value.idNhaSanXuat?.id || productData.value.idNhaSanXuat,
      idPin: productData.value.idPin?.id || productData.value.idPin,
      congNgheManHinh: productData.value.congNgheManHinh?.id || productData.value.congNgheManHinh,
      idCpu: productData.value.idCpu?.id || productData.value.idCpu,
      idGpu: productData.value.idGpu?.id || productData.value.idGpu,
      idCumCamera: productData.value.idCumCamera?.id || productData.value.idCumCamera,
      idHeDieuHanh: productData.value.idHeDieuHanh?.id || productData.value.idHeDieuHanh,
      idThietKe: productData.value.idThietKe?.id || productData.value.idThietKe,
      idSim: productData.value.idSim?.id || productData.value.idSim,
      hoTroCongNgheSac: productData.value.hoTroCongNgheSac?.id || productData.value.hoTroCongNgheSac,
      idCongNgheMang: productData.value.idCongNgheMang?.id || productData.value.idCongNgheMang,
      idChiSoKhangBuiVaNuoc: productData.value.idChiSoKhangBuiVaNuoc?.id || productData.value.idChiSoKhangBuiVaNuoc,
      tienIchDacBiet: productData.value.tienIchDacBiet,
      giaBan: giaBan,
      variants: productVariants.value.map((variant, index) => ({
        idMauSac: variant.idMauSac,
        idRam: variant.idRam,
        idBoNhoTrong: variant.idBoNhoTrong,
        imageIndex: variant.imageIndex,
        donGia: variant.donGia,
        imeiList: variantImeis.value[index] || []
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
      toast.value?.kshowToast('error', 'Vui lòng thêm ít nhất một biến thể sản phẩm và nhập đầy đủ đơn giá');
      return;
    }

    if (validateImages()) {
      toast.value?.kshowToast('error', 'Vui lòng tải lên ít nhất một ảnh');
      return;
    }

    try {
      const formData = prepareFormData();
      console.log('FormData DTO:', JSON.parse(formData.get('dto'))); // Log DTO for debugging
      const response = await axios.post('http://localhost:8080/chi-tiet-san-pham', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      console.log('Phản hồi đầy đủ:', response);
      console.log('Dữ liệu phản hồi:', response.data);

      setTimeout(() => {
        router.push('/products');
      }, 1000);
    } catch (error) {
      console.error('Lỗi khi gửi biểu mẫu:', error.response?.data || error.message);

      let errorMessage = 'Lỗi khi lưu dữ liệu';
      if (error.response) {
        errorMessage += `: ${error.response.data?.message || error.response.statusText}`;
      } else if (error.message) {
        errorMessage += `: ${error.message}`;
      }
      toast.value?.kshowToast('error', errorMessage);
    }
  };

  return {
    handleSubmit
  };
}