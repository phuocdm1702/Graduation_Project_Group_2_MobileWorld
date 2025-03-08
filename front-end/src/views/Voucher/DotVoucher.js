import axios from "axios";
import { onMounted, ref, watch } from "vue";
import { useRouter } from "vue-router";

export function useDiscountManagement() {
  const router = useRouter();
  const toast = ref(null);
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalPages = ref(0);

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
    currentPage.value = page - 1;
    fetchData();
  };

  const fetchData = async () => {
    try {
      const params = {
        page: currentPage.value,
        size: pageSize.value,
        maDGG: searchQuery.value,
        tenDGG: searchQuery.value,
        loaiGiamGiaApDung: filterType.value || null,
        trangThai: filterStatus.value === "" ? null : filterStatus.value === "1",
        giaTriGiamGia: saleValue.value || null,
        soTienGiamToiDa: minOrder.value || null,
        ngayBatDau: startDate.value || null,
        ngayKetThuc: endDate.value || null,
        deleted: deleted.value || null
      };

      const res = await axios.get("http://localhost:8080/dot_giam_gia/search", { params });
      dataTable.value = res.data.content;
      totalPages.value = res.data.totalPages;
    } catch (error) {
      console.error("Lỗi:", error);
      toast.value?.showToast("error", "Không thể tải dữ liệu!");
    }
  };
  

  const confirmDelete = (discountId) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa đợt giảm giá này?")) {
      deleteDotGiamGia(discountId);
    }
  };

  const deleteDotGiamGia = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/dot_giam_gia/${id}`);
      toast.value?.showToast('success', 'Xóa thành công!');
      await fetchData();
    } catch (error) {
      console.error("Lỗi khi xóa đợt giảm giá:", error);
      toast.value?.showToast('error', 'Lỗi khi xóa đợt giảm giá!');
    }
  };

  const viewUpdate = (discount) => {
    router.push({
      path: "/ViewAddDotGiamGia",
      query: {
        id: discount.id,
        ma: discount.ma,
        tenDotGiamGia: discount.tenDotGiamGia,
        loaiGiamGiaApDung: discount.loaiGiamGiaApDung,
        giaTriGiamGia: discount.giaTriGiamGia,
        soTienGiamToiDa: discount.soTienGiamToiDa,
        ngayBatDau: discount.ngayBatDau,
        ngayKetThuc: discount.ngayKetThuc,
        trangThai: discount.trangThai,
      },
    });
  };

  onMounted(() => {
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
  };
}