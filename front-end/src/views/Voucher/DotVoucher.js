import axios from "axios";
import { onMounted, ref, watch } from "vue";
import { useRouter } from "vue-router";
import {saveAs} from 'file-saver';

export function useDiscountManagement() {
  const toast = ref(null);
  const router = useRouter();
  const currentPage = ref(0);
  const pageSize = ref(10);
  const totalPages = ref(0);
  
  //Toast
  const showConfirmModal = ref(false);
  const confirmMessage = ref('');
  const confirmedAction = ref(null);

  // Tìm kiếm
  const searchQuery = ref("");
  const filterType = ref("");
  const filterStatus = ref("");
  const startDate = ref("");
  const endDate = ref("");
  const minOrder = ref(null);
  const saleValue = ref("");
  const deleted = ref("");

  const isModalOpen = ref(false);
  const openModal = () => {
    isModalOpen.value = true;
  };

  const dataTable = ref([]);

  const dotGiamGia = ref({
    id: null,
    ma: "",
    tenDotGiamGia: "",
    loaiGiamGiaApDung: "",
    giaTriGiamGia: "",
    soTienGiamToiDa: "",
    ngayBatDau: "",
    ngayKetThuc: "",
    trangThai: "",
    deleted: "",
  });

  const changePage = (page) => {
    currentPage.value = page;
    fetchData();
  };

  const fetchData = async () => {
    try {
      const params = {
        page: currentPage.value,
        size: pageSize.value,
        maDGG: searchQuery.value || null,
        tenDGG: searchQuery.value || null,
        loaiGiamGiaApDung: filterType.value || null,
        trangThai: filterStatus.value === "0" ? false : filterStatus.value === "1" ? true : null,
        deleted: filterStatus.value === "deleted" ? true : filterStatus.value === "" ? null : false,
        giaTriGiamGia: saleValue.value ? Number(saleValue.value) : null,
        soTienGiamToiDa: minOrder.value ? Number(minOrder.value) : null,
        ngayBatDau: startDate.value || null,
        ngayKetThuc: endDate.value || null,
      };
      console.log("Params sent:", params); // Debug
      const res = await axios.get("http://localhost:8080/dot_giam_gia/search", { params });
      console.log("API Response:", res.data); // Debug

      const processedData = res.data.content && res.data.content.length > 0
        ? res.data.content.map(item => {
          let textColor = item.deleted
            ? "text-red-500"
            : item.trangThai
              ? "text-blue-500"
              : "text-green-500";

          let text = item.deleted
            ? "Đã kết thúc"
            : item.trangThai
              ? "Sắp tới"
              : "Đang diễn ra";

          return {
            ...item,
            trangThaiFormatted: `<span class="px-3 py-1 inline-block font-semibold rounded-full bg-gray-200 ${textColor}">${text}</span>`
          };
        })
        : [];

      dataTable.value = processedData;
      totalPages.value = res.data.totalPages || 0;
    } catch (error) {
      console.error("Lỗi:", error.response?.data || error.message);
      toast.value?.kshowToast("error", "Không thể tải dữ liệu!");
    }
  };  
  const confirmDelete = (discountId) => {
    confirmAction('Bạn có chắc chắn muốn xóa đợt giảm giá này?', () => deleteDotGiamGia(discountId));
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

  const deleteDotGiamGia = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/dot_giam_gia/${id}`);
      toast.value?.kshowToast('success', 'Xóa thành công!');
      await fetchData();
    } catch (error) {
      console.error("Lỗi khi xóa đợt giảm giá:", error);
      toast.value?.kshowToast('error', 'Lỗi khi xóa đợt giảm giá!');
    }
  };

  const viewUpdate = async (discount) => {
    try {
      let discountData = discount;
      if (typeof discount === "number" || typeof discount === "string") {
        discountData = dataTable.value.find(item => item.id === discount);
      }

      if (!discountData) {
        throw new Error("Không tìm thấy dữ liệu");
      }

      // Gọi API để lấy danh sách dòng sản phẩm liên quan
      const response = await axios.get(`http://localhost:8080/dot_giam_gia/viewUpdate`, {
        params: { id: discountData.id }
      });

      router.push({
        path: "/ViewAddDotGiamGia",
        query: {
          id: discountData.id,
          ma: discountData.ma || "",
          tenDotGiamGia: discountData.tenDotGiamGia || "",
          loaiGiamGiaApDung: discountData.loaiGiamGiaApDung || "",
          giaTriGiamGia: discountData.giaTriGiamGia || "",
          soTienGiamToiDa: discountData.soTienGiamToiDa || "",
          ngayBatDau: discountData.ngayBatDau || "",
          ngayKetThuc: discountData.ngayKetThuc || "",
          trangThai: discountData.trangThai !== undefined ? discountData.trangThai : "",
          dongSanPham: JSON.stringify(response.data)
        },
      });
    } catch (error) {
      console.error("Lỗi khi xem cập nhật:", error);
      toast.value?.kshowToast("error", "Không thể tải dữ liệu cập nhật!");
    }
  };


  const exportExcel = async () => {
    try {
      const response = await axios.get('http://localhost:8080/dot_giam_gia/exportExcel', {
        responseType: 'blob',
      });
      const blob = response.data;
      saveAs(blob, 'dotGiamGia.xlsx')
    } catch (error) {
      console.error('Lỗi khi xuất Excel:', error);
      if (error.response) {
        const errorText = await error.response.data.text();
        toast.value?.kshowToast('error', `Không thể xuất file Excel: ${errorText}`);
      } else {
        toast.value?.kshowToast('error', 'Không thể xuất file Excel!');
      }
    }
  };

  
  // Định nghĩa biến toàn cục
  window.confirmDelete = confirmDelete;
  window.viewUpdate = viewUpdate;

  const columns = ref([
    { key: "index", label: "#",  formatter: (value, item, index) => {
        return (currentPage.value * pageSize.value) + (index + 1);
      } },
    { key: "ma", label: "Mã" },
    { key: "tenDotGiamGia", label: "Tên đợt giảm giá" },
    // { key: "loaiGiamGiaApDung", label: "Loại giảm giá" },
    {
      key: "giaTriGiamGia",
      label: "Giá trị",
      formatter: value => value + "%"
    },
    {
      key: "soTienGiamToiDa",
      label: "Số tiền giảm tối đa",
      formatter: value => value.toLocaleString("vi-VN") + " VND",
    },
    {
      key: "ngayBatDau",
      label: "Ngày bắt đầu",
      formatter: value => new Date(value).toLocaleDateString("vi-VN"),
    },
    {
      key: "ngayKetThuc",
      label: "Ngày kết thúc",
      formatter: value => new Date(value).toLocaleDateString("vi-VN"),
    },
    {
      key: "trangThaiFormatted",
      label: "Trạng thái",
      formatter: (value) => value  // Trả về chuỗi HTML đã được xử lý
    },
    {
      key: "actions",
      label: "Hành động",
      formatter: (value, item) => `
      <button class="bg-red-500 text-white px-3 py-1 rounded-md hover:bg-red-600 transition" data-id="${item.id}" onclick="document.dispatchEvent(new CustomEvent('confirmDelete', { detail: ${item.id} }))">
        <i class="fa-solid fa-trash"></i>
      </button>
      <button onclick="window.viewUpdate(${item.id})" class="bg-blue-500 text-white px-3 py-1 rounded-md hover:bg-blue-600 transition">
        <i class="fa-solid fa-edit"></i>
      </button>
    `,
    },
  ]);
  
  const getNestedValue = (obj, key) => (key === "index" ? null : obj[key]);

  const handleCustomEvents = () => {
    document.addEventListener('confirmDelete', (event) => {
      confirmDelete(event.detail);
    });
    document.addEventListener('viewUpdate', (event) => {
      viewUpdate(event.detail);
    });
  };

  onMounted(() => {
    fetchData();
    handleCustomEvents();
  });

  watch([searchQuery, filterType, filterStatus, startDate, endDate, minOrder, saleValue, deleted], () => {
    currentPage.value = 0;
    fetchData();
  });

  return {
    router,
    toast,
    currentPage,
    pageSize,
    totalPages,
    searchQuery,
    filterType,
    filterStatus,
    startDate,
    endDate,
    minOrder,
    saleValue,
    deleted,
    isModalOpen,
    openModal,
    dataTable,
    dotGiamGia,
    changePage,
    fetchData,
    confirmDelete,
    deleteDotGiamGia,
    viewUpdate,
    columns,
    getNestedValue,
    showConfirmModal,
    confirmMessage,
    confirmedAction,
    confirmAction,
    executeConfirmedAction,
    closeConfirmModal,
    exportExcel
  };
}