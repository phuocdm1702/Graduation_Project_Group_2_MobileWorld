import { ref } from 'vue';
import axios from 'axios';

export function useApiRequests(toast) {
  const heDieuHanhOptions = ref([]);
  const congNgheManHinhOptions = ref([]);
  const nhaSanXuatOptions = ref([]);
  const cumCameraOptions = ref([]);
  const simOptions = ref([]);
  const thietKeOptions = ref([]);
  const pinOptions = ref([]);
  const cpuOptions = ref([]);
  const gpuOptions = ref([]);
  const congNgheMangOptions = ref([]);
  const hoTroCongNgheSacOptions = ref([]);
  const chiSoKhangBuiVaNuocOptions = ref([]);
  const ramOptions = ref([]);
  const boNhoTrongOptions = ref([]);
  const mauSacOptions = ref([]);

  const fetchOptions = async () => {
    try {
      const requests = [
        { key: 'heDieuHanh', url: 'http://localhost:8080/he-dieu-hanh/all', ref: heDieuHanhOptions },
        { key: 'congNgheManHinh', url: 'http://localhost:8080/cong-nghe-man-hinh/all', ref: congNgheManHinhOptions },
        { key: 'nhaSanXuat', url: 'http://localhost:8080/nha-san-xuat/all', ref: nhaSanXuatOptions },
        { key: 'cumCamera', url: 'http://localhost:8080/cum-camera/all', ref: cumCameraOptions },
        { key: 'sim', url: 'http://localhost:8080/sim/all', ref: simOptions },
        { key: 'thietKe', url: 'http://localhost:8080/thiet-ke/all', ref: thietKeOptions },
        { key: 'pin', url: 'http://localhost:8080/pin/all', ref: pinOptions },
        { key: 'cpu', url: 'http://localhost:8080/cpu/all', ref: cpuOptions },
        { key: 'gpu', url: 'http://localhost:8080/gpu/all', ref: gpuOptions },
        { key: 'congNgheMang', url: 'http://localhost:8080/cong-nghe-mang/all', ref: congNgheMangOptions },
        { key: 'hoTroCongNgheSac', url: 'http://localhost:8080/ho-tro-cong-nghe-sac/all', ref: hoTroCongNgheSacOptions },
        { key: 'chiSoKhangBuiVaNuoc', url: 'http://localhost:8080/chi-so-khang-bui-va-nuoc/all', ref: chiSoKhangBuiVaNuocOptions },
        { key: 'ram', url: 'http://localhost:8080/ram/all', ref: ramOptions },
        { key: 'boNhoTrong', url: 'http://localhost:8080/bo-nho-trong/all', ref: boNhoTrongOptions },
        { key: 'mauSac', url: 'http://localhost:8080/mau-sac/all', ref: mauSacOptions },
      ];

      const results = await Promise.allSettled(requests.map(req => axios.get(req.url)));

      results.forEach((result, index) => {
        const { key, ref } = requests[index];
        if (result.status === 'fulfilled') {
          const data = result.value?.data;
          ref.value = Array.isArray(data?.content) ? data.content : Array.isArray(data) ? data : [];
          if (!ref.value.length) {
            console.warn(`Dữ liệu rỗng hoặc không đúng định dạng cho ${key}:`, data);
          }
        } else {
          console.error(`Lỗi khi tải ${key} từ ${requests[index].url}:`, result.reason?.response?.data || result.reason?.message);
          if (toast.value && typeof toast.value.kshowToast === 'function') {
            toast.value.kshowToast('error', `Lỗi khi tải danh sách ${key}: ${result.reason?.message || 'Không xác định'}`);
          }
          ref.value = [];
        }
      });
    } catch (error) {
      console.error('Lỗi chung khi tải danh sách tùy chọn:', error);
      if (toast.value && typeof toast.value.kshowToast === 'function') {
        toast.value.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
      }
    }
  };

  const handleAddAttribute = async (currentAttribute, data, productData, currentVariant) => {
    try {
      let response;
      switch (currentAttribute) {
        case 'heDieuHanh':
          response = await axios.post('http://localhost:8080/he-dieu-hanh', {
            heDieuHanh: data.heDieuHanh,
            phienBan: data.phienBan,
          });
          heDieuHanhOptions.value.push(response.data);
          productData.value.idHeDieuHanh = response.data.id;
          break;
        case 'congNgheManHinh':
          response = await axios.post('http://localhost:8080/cong-nghe-man-hinh', {
            kichThuoc: data.kichThuoc,
            doPhanGiai: data.doPhanGiai,
          });
          congNgheManHinhOptions.value.push(response.data);
          productData.value.congNgheManHinh = response.data.id;
          break;
        case 'nhaSanXuat':
          response = await axios.post('http://localhost:8080/nha-san-xuat', {
            nhaSanXuat: data.nhaSanXuat,
            ma: 'NSX0010',
            trangThai: 1,
          });
          nhaSanXuatOptions.value.unshift(response.data);
          productData.value.idNhaSanXuat = response.data.id;
          break;
        case 'cumCamera':
          response = await axios.post('http://localhost:8080/cum-camera', {
            cameraTruoc: data.cameraTruoc,
            cameraSau: data.cameraSau,
          });
          cumCameraOptions.value.push(response.data);
          productData.value.idCumCamera = response.data.id;
          break;
        case 'sim':
          response = await axios.post('http://localhost:8080/sim', {
            cacLoaiSimHoTro: data.cacLoaiSimHoTro,
            soLuongSimHoTro: data.soLuongSimHoTro,
          });
          simOptions.value.push(response.data);
          productData.value.idSim = response.data.id;
          break;
        case 'thietKe':
          response = await axios.post('http://localhost:8080/thiet-ke', {
            chatLieuKhung: data.chatLieuKhung,
            chatLieuMatLung: data.chatLieuMatLung,
          });
          thietKeOptions.value.push(response.data);
          productData.value.idThietKe = response.data.id;
          break;
        case 'pin':
          response = await axios.post('http://localhost:8080/pin', {
            loaiPin: data.loaiPin,
            dungLuongPin: data.dungLuongPin,
          });
          pinOptions.value.push(response.data);
          productData.value.idPin = response.data.id;
          break;
        case 'cpu':
          response = await axios.post('http://localhost:8080/cpu', {
            tenCpu: data.tenCpu,
            soNhan: data.soNhan,
          });
          cpuOptions.value.push(response.data);
          productData.value.idCpu = response.data.id;
          break;
        case 'gpu':
          response = await axios.post('http://localhost:8080/gpu', {
            tenGpu: data.tenGpu,
          });
          gpuOptions.value.push(response.data);
          productData.value.idGpu = response.data.id;
          break;
        case 'congNgheMang':
          response = await axios.post('http://localhost:8080/cong-nghe-mang', {
            tenCongNgheMang: data.tenCongNgheMang,
          });
          congNgheMangOptions.value.push(response.data);
          productData.value.idCongNgheMang = response.data.id;
          break;
        case 'hoTroCongNgheSac':
          response = await axios.post('http://localhost:8080/ho-tro-cong-nghe-sac', {
            tenCongNgheSac: data.tenCongNgheSac,
          });
          hoTroCongNgheSacOptions.value.push(response.data);
          productData.value.hoTroCongNgheSac = response.data.id;
          break;
        case 'chiSoKhangBuiVaNuoc':
          response = await axios.post('http://localhost:8080/chi-so-khang-bui-va-nuoc', {
            tenChiSo: data.tenChiSo,
          });
          chiSoKhangBuiVaNuocOptions.value.push(response.data);
          productData.value.idChiSoKhangBuiVaNuoc = response.data.id;
          break;
        case 'ram':
          response = await axios.post('http://localhost:8080/ram', {
            ma: data.ma,
            dungLuong: data.dungLuong,
          });
          ramOptions.value.push(response.data);
          currentVariant.value.selectedRams.push(response.data.id);
          break;
        case 'boNhoTrong':
          response = await axios.post('http://localhost:8080/bo-nho-trong', {
            ma: data.ma,
            dungLuong: data.dungLuong,
          });
          boNhoTrongOptions.value.push(response.data);
          currentVariant.value.selectedBoNhoTrongs.push(response.data.id);
          break;
        case 'mauSac':
          response = await axios.post('http://localhost:8080/mau-sac', {
            ma: data.ma,
            tenMau: data.tenMau,
          });
          mauSacOptions.value.push(response.data);
          currentVariant.value.selectedMauSacs.push(response.data.id);
          break;
        default:
          console.warn('Unknown attribute:', currentAttribute);
          return false;
      }

      if (toast.value && typeof toast.value.kshowToast === 'function') {
        toast.value.kshowToast('success', `Thêm ${currentAttribute} thành công!`);
      }
      return true;
    } catch (error) {
      const errorMessage = error.response?.data?.error || error.message;
      if (toast.value && typeof toast.value.kshowToast === 'function') {
        toast.value.kshowToast('error', `Lỗi khi thêm ${currentAttribute}: ${errorMessage}`);
      }
      console.error(`Add ${currentAttribute} error:`, error);
      return false;
    }
  };

  return {
    heDieuHanhOptions,
    congNgheManHinhOptions,
    nhaSanXuatOptions,
    cumCameraOptions,
    simOptions,
    thietKeOptions,
    pinOptions,
    cpuOptions,
    gpuOptions,
    congNgheMangOptions,
    hoTroCongNgheSacOptions,
    chiSoKhangBuiVaNuocOptions,
    ramOptions,
    boNhoTrongOptions,
    mauSacOptions,
    fetchOptions,
    handleAddAttribute,
  };
}