import { ref, watch } from "vue";
import axios from "axios";

export default function usePhieuGiamGia() {
  const vouchers = ref([]);
  const searchQuery = ref("");
  const isEditing = ref(false);
  const editingVoucher = ref({ma: null, 
    tenPhieuGiamGia: null, 
    loaiPhieuGiam: null,
    phanTramGiamGia: 0,
    soLuongDung: 0,
    hoaDonToiThieu: 0,
    ngayBatDau: null,
    ngayKetThuc: null,
    trangThai: null,
    riengTu: false,
    moTa: null,
  });

  const baseURL = "http://localhost:8080/phieu-giam-gia";

  const fetchDataPGG = async () => {
    try {
      const response = await axios.get(`${baseURL}/data`);
      console.log("Dữ liệu nhận được từ API:", response.data);
      vouchers.value = response.data || [];
    } catch (error) {
      console.error("Error loading data:", error);
      vouchers.value = []; // Đảm bảo không bị undefined
    }
  };


  const searchPGG = async () => {
    try {
      let response;
      if (!searchQuery.value.trim()) {
        response = await axios.get(`${baseURL}/data`);
      } else {
        response = await axios.get(`${baseURL}/search`, {
          params: { keyword: searchQuery.value }
        });
      }
      vouchers.value = response.data;
    } catch (error) {
      console.error("Lỗi search!", error);
    }
  };

  const deletePGG = async (id) => {
    try {
      await axios.delete(`${baseURL}/delete/${id}`);
      await fetchDataPGG();
    } catch (error) {
      console.log("Lỗi khi xóa:", error);
    }
  };

  const editPGG = (voucher) => {
    if (!voucher) {
      console.error("Voucher không hợp lệ:", voucher);
      return;
    }
    editingVoucher.value = { ...voucher };
    isEditing.value = true;
  };


  const updatePGG = async () => {
    const {ma,
      tenPhieuGiamGia,
      loaiPhieuGiam,
      phanTramGiamGia,
      soLuongDung,
      hoaDonToiThieu,
      ngayBatDau,
      ngayKetThuc,
      trangThai,
      riengTu,
      moTa,
    } = editingVoucher.value
    try {
      await axios.put(`${baseURL}/update/${editingVoucher.value.id}`, editingVoucher.value);
      isEditing.value = false;
      await fetchDataPGG();
    } catch (error) {
      console.log("Cập nhật thất bại!");
    }
  };

  return {
    vouchers,
    searchQuery,
    searchPGG,
    deletePGG,
    editPGG,
    isEditing,
    editingVoucher,
    updatePGG,
    fetchDataPGG
  };
}
