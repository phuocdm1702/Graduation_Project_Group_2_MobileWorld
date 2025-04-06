import { ref } from 'vue';
import axios from 'axios';

export function useApiRequests(toast) {
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

  const fetchOptions = async () => {
    try {
      const [
        heDieuHanhRes, manHinhRes, nhaSanXuatRes, cumCameraRes, simRes,
        thietKeRes, pinRes, cpuRes, gpuRes, congNgheMangRes, congSacRes,
        hoTroCongNgheSacRes, chiSoKhangBuiVaNuocRes, tinhTrangRes, ramRes,
        boNhoTrongRes, mauSacRes,
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

      heDieuHanhOptions.value = heDieuHanhRes.data.content || [];
      manHinhOptions.value = manHinhRes.data.content || [];
      nhaSanXuatOptions.value = nhaSanXuatRes.data.content || [];
      cumCameraOptions.value = cumCameraRes.data.content || [];
      simOptions.value = simRes.data.content || [];
      thietKeOptions.value = thietKeRes.data.content || [];
      pinOptions.value = pinRes.data.content || [];
      cpuOptions.value = cpuRes.data.content || [];
      gpuOptions.value = gpuRes.data.content || [];
      congNgheMangOptions.value = congNgheMangRes.data.content || [];
      congSacOptions.value = congSacRes.data.content || [];
      hoTroCongNgheSacOptions.value = hoTroCongNgheSacRes.data.content || [];
      chiSoKhangBuiVaNuocOptions.value = chiSoKhangBuiVaNuocRes.data.content || [];
      tinhTrangOptions.value = tinhTrangRes.data.content || [];
      ramOptions.value = ramRes.data.content || [];
      boNhoTrongOptions.value = boNhoTrongRes.data.content || [];
      mauSacOptions.value = mauSacRes.data.content || [];
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
      console.error('Fetch options error:', error);
    }
  };

  const handleAddAttribute = async (currentAttribute, data, productData, currentVariant) => {
    try {
      let response;
      switch (currentAttribute) { // Removed .value since currentAttribute is a string
        case 'heDieuHanh':
          response = await axios.post('http://localhost:8080/he-dieu-hanh', {
            heDieuHanh: data.heDieuHanh,
            phienBan: data.phienBan,
          });
          heDieuHanhOptions.value.push(response.data);
          productData.value.idHeDieuHanh = response.data.id;
          break;
        case 'manHinh':
          response = await axios.post('http://localhost:8080/man-hinh', {
            kichThuoc: data.kichThuoc,
            doPhanGiai: data.doPhanGiai,
          });
          manHinhOptions.value.push(response.data);
          productData.value.idManHinh = response.data.id;
          break;
        case 'nhaSanXuat':
          console.log('Adding manufacturer:', data.nhaSanXuat);
          response = await axios.post('http://localhost:8080/nha-san-xuat', {
            nhaSanXuat: data.nhaSanXuat,
            ma: "NSX0010",
            trangThai: 1,
          });
          console.log('API response:', response.data);
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
        case 'congSac':
          response = await axios.post('http://localhost:8080/cong-sac', {
            congSac: data.congSac,
          });
          congSacOptions.value.push(response.data);
          productData.value.idCongSac = response.data.id;
          break;
        case 'hoTroCongNgheSac':
          response = await axios.post('http://localhost:8080/ho-tro-cong-nghe-sac', {
            tenCongNgheSac: data.tenCongNgheSac,
          });
          hoTroCongNgheSacOptions.value.push(response.data);
          productData.value.idHoTroCongNgheSac = response.data.id;
          break;
        case 'chiSoKhangBuiVaNuoc':
          response = await axios.post('http://localhost:8080/chi-so-khang-bui-va-nuoc', {
            tenChiSo: data.tenChiSo,
          });
          chiSoKhangBuiVaNuocOptions.value.push(response.data);
          productData.value.idChiSoKhangBuiVaNuoc = response.data.id;
          break;
        case 'tinhTrang':
          response = await axios.post('http://localhost:8080/tinh-trang', {
            loaiTinhTrang: data.loaiTinhTrang,
          });
          tinhTrangOptions.value.push(response.data);
          productData.value.idLoaiTinhTrang = response.data.id;
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

      toast.value?.kshowToast('success', `Thêm ${currentAttribute} thành công!`);
      return true;
    } catch (error) {
      const errorMessage = error.response?.data?.error || error.message;
      toast.value?.kshowToast('error', `Lỗi khi thêm ${currentAttribute}: ${errorMessage}`);
      console.error(`Add ${currentAttribute} error:`, error);
      return false;
    }
  };

  return {
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
    fetchOptions,
    handleAddAttribute,
  };
}