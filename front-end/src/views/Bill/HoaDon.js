// @/views/Bill/HoaDon.js
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { saveAs } from 'file-saver';

export default function useHoaDonLineList() {
  const router = useRouter();
  const toast = ref(null);
  const dataTable = ref([]);
  const originalData = ref([]); // Lưu dữ liệu gốc
  const currentPage = ref(0);
  const pageSize = ref(11);
  const totalElements = ref(0);
  const orderTypes = ref([]);

  // Filter variables
  const keyword = ref("");
  const minAmount = ref(0);
  const maxAmount = ref(10000000);
  const minRange = ref(0);
  const maxRange = ref(10000000);
  const selectedOrderType = ref("");
  const startDate = ref("");
  const endDate = ref("");
  const isFiltering = ref(false);

  // Modal controls
  const showConfirmModal = ref(false);
  const confirmMessage = ref('');
  const confirmedAction = ref(null);

  const totalPages = computed(() => Math.ceil(totalElements.value / pageSize.value));

  const columns = ref([
    {
      key: "index",
      label: "#",
      formatter: (_, __, index) => currentPage.value * pageSize.value + index + 1
    },
    { key: "ma", label: "Mã" },
    { key: "idNhanVien.ma", label: "Nhân viên" },
    { key: "idKhachHang.ten", label: "Khách hàng" },
    { key: "soDienThoaiKhachHang", label: "SDT" },
    {
      key: "tongTienSauGiam",
      label: "Tổng giá trị",
      formatter: (value) => value ? `${value.toLocaleString()} VND` : "0 VND"
    },
    {
      key: "idPhieuGiamGia.phanTramGiamGia",
      label: "Tiền giảm",
      formatter: (phanTramGiamGia, item) => {
        if (!phanTramGiamGia || !item?.tongTien) return "0 VND";
        const giamGia = (item.tongTien * (phanTramGiamGia / 100)) || 0;
        const formattedGiamGia = Math.round(giamGia / 1000) * 1000;
        return `(${phanTramGiamGia}%) ~ ${formattedGiamGia.toLocaleString()}đ`;
      }
    },
    {
      key: "phi_van_chuyen",
      label: "Phí",
      formatter: (value) => value ? `${value.toLocaleString()} VND` : "0 VND"
    },
    {
      key: "ngayTao",
      label: "TG tạo",
      formatter: (value) => value ? new Date(value).toLocaleDateString() : "N/A"
    },
    { key: "loaiDon", label: "Loại Đơn" },
    {
      key: "trangThaiFormatted", // Sử dụng trường mới
      label: "Trạng thái",
      formatter: (value) => value  // Trả về chuỗi HTML đã được xử lý
    },
    {
      key: "actions",
      label: "Thao tác",
      formatter: (value, item) => `
        <button class="text-blue-500 hover:text-blue-700 mr-2" onclick="window.viewUpdate(${item.id})">
          <i class="fa-solid fa-edit" style="color: #f97316;"></i>
        </button>
        <button class="text-blue-500 hover:text-blue-700" onclick="printInvoice(${item.id})">
          <i class="fa-solid fa-print" style="color: #f97316;"></i>
        </button>
      `
    }
  ]);

  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => acc?.[part], obj) || null;
  };

  const fetchOrderTypes = async () => {
    try {
      const res = await axios.get("http://localhost:8080/hoa-don/order-types");
      orderTypes.value = res.data || [];
    } catch (error) {
      console.error("Lỗi khi lấy danh sách loại đơn:", error);
      orderTypes.value = [
        { value: "online", label: "Online" },
        { value: "trực tiếp", label: "Trực tiếp" }
      ];
    }
  };
  const fetchInitialData = async () => {
    try {
      const res = await axios.get("http://localhost:8080/hoa-don/home", {
        params: {
          page: currentPage.value,
          size: pageSize.value
        }
      });

      // Debug: Kiểm tra dữ liệu từ API
      console.log("Initial Data Response:", res.data.content);

      const processedData = res.data.content && res.data.content.length > 0
        ? res.data.content.map(item => {
          // Xác định trạng thái, xử lý trường hợp null hoặc undefined
          const status = item.trangThai !== undefined && item.trangThai !== null ? item.trangThai : 0;
          let colorClass = status === 1 ? "bg-green-500" : "bg-yellow-500";
          let text = status === 1 ? "Hoàn thành" : "Chờ xử lý";
          return {
            ...item,
            trangThaiFormatted: `<span class="px-3 py-1 inline-block text-white font-semibold rounded-full ${colorClass}">${text}</span>`
          };
        })
        : [];

      dataTable.value = processedData;
      originalData.value = [...processedData]; // Lưu dữ liệu gốc
      totalElements.value = res.data.totalElements || 0;

      // Cập nhật maxRange dựa trên dữ liệu thực tế
      const maxValue = Math.max(...dataTable.value.map(item => item.tongTienSauGiam || 0));
      if (maxValue > maxRange.value) {
        maxRange.value = Math.ceil(maxValue / 1000000) * 1000000;
        maxAmount.value = maxRange.value;
      }
    } catch (error) {
      console.error("Lỗi khi gọi API:", error);
      toast.value?.kshowToast("error", "Không thể tải dữ liệu!");
      dataTable.value = [];
      originalData.value = [];
      totalElements.value = 0;
    }
  };
  // const fetchInitialData = async () => {
  //   try {
  //     const res = await axios.get("http://localhost:8080/hoa-don/home", {
  //       params: {
  //         page: currentPage.value,
  //         size: pageSize.value
  //       }
  //     });
  //     dataTable.value = res.data.content || [];
  //     originalData.value = [...dataTable.value]; // Lưu dữ liệu gốc
  //     totalElements.value = res.data.totalElements || 0;
  //
  //     // Cập nhật maxRange dựa trên dữ liệu thực tế
  //     const maxValue = Math.max(...dataTable.value.map(item => item.tongTienSauGiam || 0));
  //     if (maxValue > maxRange.value) {
  //       maxRange.value = Math.ceil(maxValue / 1000000) * 1000000;
  //       maxAmount.value = maxRange.value;
  //     }
  //   } catch (error) {
  //     console.error("Lỗi khi gọi API:", error);
  //     toast.value?.kshowToast("error", "Không thể tải dữ liệu!");
  //     dataTable.value = [];
  //     originalData.value = [];
  //     totalElements.value = 0;
  //   }
  // };

  // const searchAllFields = (data, searchKeyword) => {
  //   if (!searchKeyword) return data;
  //   const lowerKeyword = searchKeyword.toLowerCase();
  //
  //   return data.filter(item => {
  //     // Các trường cần tìm kiếm
  //     const fields = [
  //       item.ma || "",
  //       getNestedValue(item, "idNhanVien.ma") || "",
  //       getNestedValue(item, "idKhachHang.ten") || "",
  //       item.soDienThoaiKhachHang || "",
  //       item.tongTienSauGiam ? item.tongTienSauGiam.toString() : "",
  //       getNestedValue(item, "idPhieuGiamGia.phanTramGiamGia") ?
  //         `${getNestedValue(item, "idPhieuGiamGia.phanTramGiamGia")} %` : "",
  //       item.phi_van_chuyen ? item.phi_van_chuyen.toString() : "",
  //       item.ngayTao ? new Date(item.ngayTao).toLocaleDateString() : "",
  //       item.loaiDon || "",
  //       item.trangThai === 1 ? "Hoàn thành" : "Chờ xử lý"
  //     ];
  //
  //     // Kiểm tra xem có bất kỳ trường nào khớp với từ khóa không
  //     return fields.some(field => field.toLowerCase().includes(lowerKeyword));
  //   });
  // };

  const searchAllFields = (data, searchKeyword) => {
    if (!searchKeyword) return data;
    const lowerKeyword = searchKeyword.toLowerCase();

    return data.filter(item => {
      // Các trường cần tìm kiếm
      const fields = [
        item.ma || "",
        getNestedValue(item, "idNhanVien.ma") || "",
        getNestedValue(item, "idKhachHang.ten") || "",
        item.soDienThoaiKhachHang || "",
        item.tongTienSauGiam ? item.tongTienSauGiam.toString() : "",
        getNestedValue(item, "idPhieuGiamGia.phanTramGiamGia") ?
          `${getNestedValue(item, "idPhieuGiamGia.phanTramGiamGia")} %` : "",
        item.phi_van_chuyen ? item.phi_van_chuyen.toString() : "",
        item.ngayTao ? new Date(item.ngayTao).toLocaleDateString() : "",
        item.loaiDon || "",
        item.trangThai === 1 ? "Hoàn thành" : "Chờ xử lý" // Tìm kiếm dựa trên trạng thái văn bản
      ];

      // Kiểm tra xem có bất kỳ trường nào khớp với từ khóa không
      return fields.some(field => field.toLowerCase().includes(lowerKeyword));
    });
  };
  const fetchData = async () => {
    try {
      const params = {
        page: currentPage.value,
        size: pageSize.value,
        keyword: keyword.value || null,
        loaiDon: selectedOrderType.value || null,
        minAmount: minAmount.value !== minRange.value ? Number(minAmount.value) : null,
        maxAmount: maxAmount.value !== maxRange.value ? Number(maxAmount.value) : null,
        startDate: startDate.value || null,
        endDate: endDate.value || null
      };

      // Gọi API với các tham số
      const res = await axios.get("http://localhost:8080/hoa-don/home", { params });
      let filteredData = res.data.content || [];
      totalElements.value = res.data.totalElements || 0;

      // Debug: Kiểm tra dữ liệu từ API
      console.log("API Response:", res.data.content);

      // Xử lý dữ liệu để thêm trường trangThaiFormatted
      const processedData = filteredData.length > 0
        ? filteredData.map(item => {
          // Xác định trạng thái, xử lý trường hợp null hoặc undefined
          const status = item.trangThai !== undefined && item.trangThai !== null ? item.trangThai : 0;
          let colorClass = status === 1 ? "bg-green-500" : "bg-yellow-500";
          let text = status === 1 ? "Hoàn thành" : "Chờ xử lý";

          // Thêm trường trangThaiFormatted vào mỗi item
          return {
            ...item,
            trangThaiFormatted: `<span class="px-3 py-1 inline-block text-white font-semibold rounded-full ${colorClass}">${text}</span>`
          };
        })
        : [];

      // Nếu có từ khóa tìm kiếm, lọc thêm trên client
      if (keyword.value) {
        filteredData = searchAllFields(processedData, keyword.value);
        totalElements.value = filteredData.length; // Cập nhật tổng số phần tử sau khi lọc
      } else {
        filteredData = processedData;
      }

      // Chỉ gọi API nếu có bộ lọc được áp dụng
      const hasFilters =
        keyword.value ||
        selectedOrderType.value ||
        minAmount.value !== minRange.value ||
        maxAmount.value !== maxRange.value ||
        startDate.value ||
        endDate.value;

      if (hasFilters) {
        dataTable.value = filteredData;
        isFiltering.value = true;
      } else {
        // Nếu không có bộ lọc, sử dụng dữ liệu gốc
        dataTable.value = processedData;
        totalElements.value = res.data.totalElements || 0;
        isFiltering.value = false;
      }
    } catch (error) {
      console.error("Lỗi khi gọi API:", error);
      toast.value?.kshowToast("error", "Không thể tải dữ liệu!");
      dataTable.value = [];
      totalElements.value = 0;
    }
  };
  // const fetchData = async () => {
  //   try {
  //     const params = {
  //       page: currentPage.value,
  //       size: pageSize.value,
  //       keyword: keyword.value || null,
  //       loaiDon: selectedOrderType.value || null,
  //       minAmount: minAmount.value !== minRange.value ? Number(minAmount.value) : null,
  //       maxAmount: maxAmount.value !== maxRange.value ? Number(maxAmount.value) : null,
  //       startDate: startDate.value || null,
  //       endDate: endDate.value || null
  //     };
  //
  //     // Gọi API với các tham số
  //     const res = await axios.get("http://localhost:8080/hoa-don/home", { params });
  //     let filteredData = res.data.content || [];
  //     totalElements.value = res.data.totalElements || 0;
  //
  //     // Nếu API không hỗ trợ tìm kiếm trên tất cả các trường, lọc phía client
  //     if (keyword.value) {
  //       filteredData = searchAllFields(filteredData, keyword.value);
  //       totalElements.value = filteredData.length; // Cập nhật tổng số phần tử sau khi lọc
  //     }
  //
  //     // Chỉ gọi API nếu có bộ lọc được áp dụng
  //     const hasFilters =
  //       keyword.value ||
  //       selectedOrderType.value ||
  //       minAmount.value !== minRange.value ||
  //       maxAmount.value !== maxRange.value ||
  //       startDate.value ||
  //       endDate.value;
  //
  //     if (hasFilters) {
  //       dataTable.value = filteredData;
  //       isFiltering.value = true;
  //     } else {
  //       // Nếu không có bộ lọc, sử dụng dữ liệu gốc
  //       dataTable.value = originalData.value;
  //       totalElements.value = originalData.value.length;
  //       isFiltering.value = false;
  //     }
  //   } catch (error) {
  //     console.error("Lỗi khi gọi API:", error);
  //     toast.value?.kshowToast("error", "Không thể tải dữ liệu!");
  //     dataTable.value = [];
  //     totalElements.value = 0;
  //   }
  // };

  const applyFilters = () => {
    fetchData();
  };

  const goToPage = (page) => {
    currentPage.value = page;
    if (isFiltering.value) {
      fetchData();
    } else {
      fetchInitialData();
    }
  };

  const viewUpdate = (id) => {
    router.push(`/show-hoa-don/${id}`);
  };

  const exportExcel = async () => {
    try {
      const response = await axios.get('http://localhost:8080/hoa-don/exportExcel', {
        responseType: 'blob',
      });
      const blob = response.data;
      saveAs(blob, 'hoaDon.xlsx');
      toast.value?.kshowToast('success', 'Xuất Excel thành công!');
    } catch (error) {
      console.error('Lỗi khi xuất Excel:', error);
      toast.value?.kshowToast('error', 'Không thể xuất file Excel!');
    }
  };

  const confirmAction = (message, action) => {
    confirmMessage.value = message;
    confirmedAction.value = action;
    showConfirmModal.value = true;
  };

  const executeConfirmedAction = () => {
    if (confirmedAction.value) {
      confirmedAction.value();
    }
    closeConfirmModal();
  };

  const closeConfirmModal = () => {
    showConfirmModal.value = false;
    confirmedAction.value = null;
  };

  // Range slider adjustments
  const adjustMin = () => {
    if (minAmount.value > maxAmount.value) {
      minAmount.value = maxAmount.value;
    }
  };

  const adjustMax = () => {
    if (maxAmount.value < minAmount.value) {
      maxAmount.value = minAmount.value;
    }
  };

  // Global functions
  window.viewUpdate = viewUpdate;

  onMounted(() => {
    fetchOrderTypes();
    fetchInitialData();
  });

  return {
    toast,
    dataTable,
    currentPage,
    totalPages,
    keyword,
    minAmount,
    maxAmount,
    selectedOrderType,
    startDate,
    endDate,
    orderTypes,
    columns,
    getNestedValue,
    applyFilters,
    goToPage,
    exportExcel,
    showConfirmModal,
    confirmMessage,
    executeConfirmedAction,
    closeConfirmModal,
    minRange,
    maxRange,
    adjustMin,
    adjustMax
  };
}