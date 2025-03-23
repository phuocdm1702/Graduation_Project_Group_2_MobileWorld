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
  const pageSize = ref(10);
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
      key: "phiVanChuyen",
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
      key: "trangThaiFormatted",
      label: "Trạng thái",
      formatter: (value) => value
    },
    {
      key: "actions",
      label: "Thao tác",
      formatter: (value, item) => `
        <button class="text-blue-500 hover:text-blue-700 mr-2" onclick="window.viewUpdate(${item.id})">
          <i class="fa-solid fa-edit" style="color: #f97316;"></i>
        </button>
        <button class="text-blue-500 hover:text-blue-700" onclick="window.printInvoice(${item.id})">
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

      const processedData = res.data.content && res.data.content.length > 0
        ? res.data.content.map(item => {
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
      originalData.value = [...processedData];
      totalElements.value = res.data.totalElements || 0;

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

  const searchAllFields = (data, searchKeyword) => {
    if (!searchKeyword) return data;
    const lowerKeyword = searchKeyword.toLowerCase();

    return data.filter(item => {
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
        item.trangThai === 1 ? "Hoàn thành" : "Chờ xử lý"
      ];

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

      const res = await axios.get("http://localhost:8080/hoa-don/home", { params });
      let filteredData = res.data.content || [];
      totalElements.value = res.data.totalElements || 0;

      const processedData = filteredData.length > 0
        ? filteredData.map(item => {
          const status = item.trangThai !== undefined && item.trangThai !== null ? item.trangThai : 0;
          let colorClass = status === 1 ? "bg-green-500" : "bg-yellow-500";
          let text = status === 1 ? "Hoàn thành" : "Chờ xử lý";
          return {
            ...item,
            trangThaiFormatted: `<span class="px-3 py-1 inline-block text-white font-semibold rounded-full ${colorClass}">${text}</span>`
          };
        })
        : [];

      if (keyword.value) {
        filteredData = searchAllFields(processedData, keyword.value);
        totalElements.value = filteredData.length;
      } else {
        filteredData = processedData;
      }

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

  const printInvoice = async (id) => {
    try {
      const response = await axios.get(`http://localhost:8080/hoa-don/print/${id}`, {
        responseType: 'blob',
      });

      const contentType = response.headers['content-type'];
      if (contentType === 'application/pdf') {
        const blob = response.data;

        // Tạo URL tạm thời từ blob để hiển thị PDF
        const blobUrl = window.URL.createObjectURL(blob);

        // Mở PDF trong một cửa sổ mới và gọi hộp thoại in
        const printWindow = window.open(blobUrl);
        if (printWindow) {
          printWindow.onload = () => {
            printWindow.print(); // Mở hộp thoại in
            // Tự động đóng cửa sổ sau khi in (tùy chọn)
            printWindow.onafterprint = () => {
              printWindow.close();
            };
          };
          toast.value?.kshowToast('success', 'Mở hộp thoại in thành công!');
        } else {
          console.error('Không thể mở cửa sổ in. Vui lòng kiểm tra trình chặn popup.');
          toast.value?.kshowToast('error', 'Không thể mở cửa sổ in!');
        }

        // Giải phóng URL blob sau khi sử dụng
        window.URL.revokeObjectURL(blobUrl);
      } else {
        const text = await response.data.text();
        console.error('Error response from server:', text);
        toast.value?.kshowToast('error', text);
      }
    } catch (error) {
      console.error('Lỗi khi mở hộp thoại in:', error);
      let errorMessage = 'Không thể mở hộp thoại in!';
      if (error.response && error.response.data) {
        const text = await error.response.data.text();
        console.error('Error response from server:', text);
        errorMessage = text;
      }
      toast.value?.kshowToast('error', errorMessage);
    }
  };
  
  // const printInvoice = async (id) => {
  //   try {
  //     const response = await axios.get(`http://localhost:8080/hoa-don/print/${id}`, {
  //       responseType: 'blob',
  //     });
  //
  //     const contentType = response.headers['content-type'];
  //     if (contentType === 'application/pdf') {
  //       const blob = response.data;
  //       saveAs(blob, `hoa_don_${id}.pdf`);
  //       toast.value?.kshowToast('success', 'In hóa đơn thành công!');
  //     } else {
  //       const text = await response.data.text();
  //       console.error('Error response from server:', text);
  //       toast.value?.kshowToast('error', text);
  //     }
  //   } catch (error) {
  //     console.error('Lỗi khi in hóa đơn:', error);
  //     let errorMessage = 'Không thể in hóa đơn!';
  //     if (error.response && error.response.data) {
  //       const text = await error.response.data.text();
  //       console.error('Error response from server:', text);
  //       errorMessage = text;
  //     }
  //     toast.value?.kshowToast('error', errorMessage);
  //   }
  // };

  // Hàm exportExcel mới: Tạo liên kết tạm thời để trình duyệt hiển thị hộp thoại "Save As"
  const exportExcel = async () => {
    try {
      const response = await axios.get('http://localhost:8080/hoa-don/export-excel', {
        responseType: 'blob',
      });

      // Tạo URL tạm thời từ blob
      const blob = response.data;
      const url = window.URL.createObjectURL(blob);

      // Tạo một thẻ <a> ẩn để kích hoạt tải file
      const link = document.createElement('a');
      link.href = url;

      // Lấy tên file từ header Content-Disposition (nếu có)
      const contentDisposition = response.headers['content-disposition'];
      let filename = 'DanhSachHoaDon.xlsx'; // Tên mặc định nếu không lấy được từ header
      if (contentDisposition) {
        const filenameMatch = contentDisposition.match(/filename="(.+)"/);
        if (filenameMatch && filenameMatch[1]) {
          filename = filenameMatch[1];
        }
      }
      link.setAttribute('download', filename); // Đặt tên file

      // Thêm thẻ <a> vào DOM, kích hoạt tải, và xóa thẻ
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      // Giải phóng URL blob
      window.URL.revokeObjectURL(url);

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

  // Đăng ký các hàm toàn cục
  window.viewUpdate = viewUpdate;
  window.printInvoice = printInvoice; // Đăng ký hàm printInvoice

  onMounted(() => {
    fetchOrderTypes();
    fetchInitialData();
  });

  const applyFilters = () => {
    fetchData();
  };


  return {
    toast,
    confirmAction,
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
    adjustMax,
  };
}