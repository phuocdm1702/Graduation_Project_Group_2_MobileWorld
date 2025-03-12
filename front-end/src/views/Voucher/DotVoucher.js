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
        maDGG: searchQuery.value || null,
        tenDGG: searchQuery.value || null,
        loaiGiamGiaApDung: filterType.value || null,
        trangThai: filterStatus.value === "" ? null : filterStatus.value === "true",
        giaTriGiamGia: saleValue.value ? Number(saleValue.value) : null,
        soTienGiamToiDa: minOrder.value ? Number(minOrder.value) : null,
        ngayBatDau: startDate.value ? startDate.value.split('T')[0] : null,
        ngayKetThuc: endDate.value ? endDate.value.split('T')[0] : null,
        deleted: deleted.value === "" ? null : deleted.value === "true"
      };
      const res = await axios.get("http://localhost:8080/api/dot_giam_gia/search", { params });
      console.log("API Response:", res.data); // Debug
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
      await axios.delete(`http://localhost:8080/api/dot_giam_gia/${id}`);
      toast.value?.showToast('success', 'Xóa thành công!');
      await fetchData();
    } catch (error) {
      console.error("Lỗi khi xóa đợt giảm giá:", error);
      toast.value?.showToast('error', 'Lỗi khi xóa đợt giảm giá!');
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
      const response = await axios.get(`http://localhost:8080/api/dot_giam_gia/viewUpdate`, {
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
          dongSanPham: JSON.stringify(response.data) // Truyền danh sách dòng sản phẩm
        },
      });
    } catch (error) {
      console.error("Lỗi khi xem cập nhật:", error);
      toast.value?.showToast("error", "Không thể tải dữ liệu cập nhật!");
    }
  };

  // Định nghĩa biến toàn cục
  window.confirmDelete = confirmDelete;
  window.viewUpdate = viewUpdate;

  const columns = ref([
    { key: "index", label: "STT", formatter: (value, item, index) => index + 1 },
    { key: "ma", label: "Mã" },
    { key: "tenDotGiamGia", label: "Tên đợt giảm giá" },
    { key: "loaiGiamGiaApDung", label: "Loại giảm giá" },
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
      key: "trangThai",
      label: "Trạng thái",
      formatter: (value, item) => {
        let colorClass = item.deleted
          ? "bg-red-500"
          : item.trangThai
            ? "bg-blue-500"
            : "bg-green-500";
        let text = item.deleted
          ? "Đã kết thúc"
          : item.trangThai
            ? "Sắp tới"
            : "Đang diễn ra";
        return `<span class="px-3 py-1 inline-block text-white font-semibold rounded-full ${colorClass}">${text}</span>`;
      },
    },
    {
      key: "actions",
      label: "Hành động",
      formatter: (value, item) => `
        <button onclick="window.confirmDelete(${item.id})" class="bg-red-500 text-white px-3 py-1 rounded-md hover:bg-red-600 transition">
          <i class="fa-solid fa-trash"></i>
        </button>
        <button onclick="window.viewUpdate(${item.id})" class="bg-blue-500 text-white px-3 py-1 rounded-md hover:bg-blue-600 transition">
          <i class="fa-solid fa-edit"></i>
        </button>
      `,
    },
  ]);

  const getNestedValue = (obj, key) => (key === "index" ? null : obj[key]);

  onMounted(() => {
    fetchData();
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
  };
}