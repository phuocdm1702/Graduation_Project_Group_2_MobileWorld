import {ref, computed} from "vue";
import axios from "axios";

export default function usePhieuGiamGia() {
  const vouchers = ref([]);
  const searchQuery = ref("");
  const filterType = ref("");
  const filterStatus = ref("");
  const startDate = ref(null);
  const endDate = ref(null);
  const minOrder = ref(null);
  const valueFilter = ref(null);
  const currentPage = ref(0); 
  const pageSize = ref(10);   
  const totalPages = ref(0); 

  const baseURL = "http://localhost:8080/phieu-giam-gia";

  const fetchDataPGG = async (page = 0) => {
    try {
      const response = await axios.get(`${baseURL}/data`, {
        params: {
          page,
          size: pageSize.value,
        }
      });
      console.log("Dữ liệu nhận được từ API:", response.data);
      vouchers.value = response.data.content || [];
      totalPages.value = response.data.totalPages;
    } catch (error) {
      console.error("Error loading data:", error);
      vouchers.value = [];
    }
  };

  const searchPGG = async (page = 0) => {
    try {
      let response;
      if (!searchQuery.value || searchQuery.value.trim() === "") {
        response = await axios.get(`${baseURL}/data`, {
          params: {
            page,
            size: pageSize.value,
          }
        });
      } else {
        response = await axios.get(`${baseURL}/search`, {
          params: {
            keyword: searchQuery.value.trim(),
            page,
            size: pageSize.value,
          },
        });
      }
      vouchers.value = response.data.content;
      totalPages.value = response.data.totalPages;
    } catch (error) {
      console.error("Lỗi search!", error);
    }
  };

  const toggleStatusPGG = async (item) => {
    try {
      const newStatus = !item.trangThai;
      await axios.put(`${baseURL}/update-trang-thai/${item.id}`, {trangThai: newStatus});
      item.trangThai = newStatus; // Cập nhật local state
    } catch (error) {
      console.error("Lỗi khi cập nhật trạng thái:", error);
      console.log("Response từ server:", error.response?.data);
    }
  };


  // Gán sau khi deletePGG đã được khai báo

  const columns = ref([
    {
      key: "ma",
      label: "Mã"
    },
    {
      key: "tenPhieuGiamGia",
      label: "Tên Phiếu"
    },
    {
      key: "loaiPhieuGiamGia",
      label: "Loại Phiếu"
    },
    {
      key: "phanTramGiamGia",
      label: "Phần trăm giảm giá",
      formatter: (value) => `${value}%`
    },
    {
      key: "soTienGiamToiDa",
      label: "Số tiền giảm tối đa",
      formatter: (value, item) => {
        if (item.loaiPhieuGiamGia === "Phần trăm") {
          const phanTramGiamGia = item.phanTramGiamGia || 0; // Giá trị phần trăm giảm giá
          const hoaDonToiThieu = item.hoaDonToiThieu || 0; // Giá trị hóa đơn tối thiểu
          const soTienGiamToiDa = (phanTramGiamGia / 100) * hoaDonToiThieu; // Tính số tiền giảm tối đa
          return soTienGiamToiDa.toLocaleString("vi-VN", {
            style: "currency",
            currency: "VND",
          }); 
        }
        return value ? value.toLocaleString("vi-VN", {style: "currency", currency: "VND"}) : "0 ₫";
      }  
    },
    {
      key: "soLuongDung",
      label: "Số lượng"
    },
    {
      key: "hoaDonToiThieu",
      label: "Hóa đơn tối thiểu"
    },
    {
      key: "ngayBatDau",
      label: "Ngày bắt đầu",
      formatter: (value) => new Date(value).toLocaleDateString("vi-VN")
    },
    {
      key: "ngayKetThuc",
      label: "Ngày kết thúc",
      formatter: (value) => new Date(value).toLocaleDateString("vi-VN")
    },
    {
      key: "moTa",
      label: "Mô tả"
    },
    {
      key: "trangThai",
      label: "Trạng thái",
      cellSlot: "trangThaiSlot"
    },
    {
      key: "actions",
      label: "Hành động",
      cellSlot: "actionsSlot",
    },
  ]);

  const getNestedValue = (obj, key) => {
    return key.split(".").reduce((o, k) => (o && o[k] !== undefined ? o[k] : null), obj);
  };

  const filterPGG = async (page = 0) => {
    try {
      const params = {
        loaiPhieuGiamGia: filterType.value || null,
        trangThai: filterStatus.value || null,
        startDate: startDate.value || null,
        endDate: endDate.value || null,
        minOrder: minOrder.value ? Number(minOrder.value) : null,
        valueFilter: valueFilter.value ? Number(valueFilter.value) : null,
        page,
        size: pageSize.value,
      };

      if (
        !params.loaiPhieuGiamGia &&
        !params.trangThai &&
        !params.startDate &&
        !params.endDate &&
        !params.minOrder &&
        !params.valueFilter
      ) {
        await fetchDataPGG();
        return;
      }

      const response = await axios.get(`${baseURL}/filter`, {params});
      vouchers.value = response.data || [];
    } catch (error) {
      console.error("Lỗi khi lọc phiếu giảm giá:", error);
      vouchers.value = [];
    }
  };

  const goToPage = (page) => {
    currentPage.value = page;
    if (searchQuery.value.trim().length > 0) {
      searchPGG(page);
    } else if (
      filterType.value ||
      filterStatus.value ||
      startDate.value ||
      endDate.value ||
      minOrder.value ||
      valueFilter.value
    ) {
      filterPGG(page);
    } else {
      fetchDataPGG(page);
    }
  };

  return {
    vouchers,
    columns,
    getNestedValue,
    searchQuery,
    filterType,
    filterStatus,
    startDate,
    endDate,
    minOrder,
    valueFilter,
    currentPage,
    pageSize,
    totalPages,
    searchPGG,
    fetchDataPGG,
    filterPGG,
    toggleStatusPGG,
    goToPage
  };
}