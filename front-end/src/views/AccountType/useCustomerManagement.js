import { ref, onMounted, onUnmounted } from "vue";
import axios from "axios";
import { useRouter, useRoute } from "vue-router";
import { format } from 'date-fns';
import { vi } from 'date-fns/locale';

export default function useCustomerManagement() {
  const router = useRouter();
  const route = useRoute();

  // Data
  const dataTable = ref([]);
  const originalData = ref([]);
  const searchKH = ref("");
  const filterStatus = ref("tat-ca");
  const currentPage = ref(1);
  const totalPages = ref(1);
  const visible = ref(false);
  const message = ref("");
  const type = ref("success");
  const showConfirmModal = ref(false);
  const selectedCustomerId = ref(null);
  const isLoading = ref(false);

  // Toast notification
  const showToast = (toastType, msg) => {
    message.value = msg;
    type.value = toastType;
    visible.value = true;
    setTimeout(() => {
      visible.value = false;
    }, 3000);
  };

  // Fetch customers
  const fetchCustomers = async (page = 1, size = 10) => {
    isLoading.value = true;
    try {
      const params = {
        page: page - 1, // Backend uses 0-based paging
        size,
      };

      const res = await axios.get("http://localhost:8080/khach-hang/home", { params });
      originalData.value = res.data.content || res.data || [];
      totalPages.value = res.data.totalPages || 1;
      currentPage.value = page;

      // Kiểm tra và thêm khách hàng mới từ query
      const newCustomer = route.query.newCustomer;
      if (newCustomer) {
        const parsedCustomer = JSON.parse(newCustomer);
        originalData.value.unshift(parsedCustomer); // Thêm khách hàng mới vào đầu danh sách
        router.replace({ query: null }); // Xóa query sau khi xử lý
      }

      applyFilterAndSearch();
    } catch (error) {
      console.error("Lỗi khi lấy danh sách khách hàng:", error);
      dataTable.value = [];
      showToast("error", "Không thể lấy danh sách khách hàng!");
    } finally {
      isLoading.value = false;
    }
  };

  // Apply filter and search logic
  const applyFilterAndSearch = () => {
    let filteredData = [...originalData.value];

    if (filterStatus.value === "kich-hoat") {
      filteredData = filteredData.filter((kh) => !kh.deleted);
    } else if (filterStatus.value === "huy-kich-hoat") {
      filteredData = filteredData.filter((kh) => kh.deleted);
    }

    if (searchKH.value.trim()) {
      filteredData = filteredData.filter(
        (khachhang) =>
          khachhang?.idTaiKhoan?.email?.toLowerCase().includes(searchKH.value.toLowerCase()) ||
          khachhang?.ten?.toLowerCase().includes(searchKH.value.toLowerCase()) ||
          khachhang?.idTaiKhoan?.soDienThoai?.toLowerCase().includes(searchKH.value.toLowerCase())
      );
    }

    dataTable.value = filteredData;
  };

  // Search handler
  const btnSearch = () => {
    applyFilterAndSearch();
  };

  // Filter handler
  const onFilterChange = () => {
    applyFilterAndSearch();
  };

  // Reset search and filter
  const backSearch = () => {
    searchKH.value = "";
    filterStatus.value = "tat-ca";
    applyFilterAndSearch();
  };

  // Delete customer
  const showDeleteConfirm = (id) => {
    selectedCustomerId.value = id;
    showConfirmModal.value = true;
  };

  const confirmDelete = async () => {
    if (!selectedCustomerId.value) return;

    try {
      await axios.put(`http://localhost:8080/khach-hang/delete/${selectedCustomerId.value}`);
      showToast("success", "Xóa mềm thành công!");
      await fetchCustomers(currentPage.value); // Cập nhật lại dữ liệu sau khi xóa
    } catch (error) {
      console.error("Lỗi khi xóa khách hàng:", error);
      showToast("error", "Không thể xóa khách hàng!");
    } finally {
      showConfirmModal.value = false;
      selectedCustomerId.value = null;
    }
  };

  // Edit customer
  const editCustomer = (customer) => {
    console.log("Editing customer:", customer);
    // Logic to open edit form or modal
  };

  // Pagination
  const goToPage = (page) => {
    if (page >= 1 && page <= totalPages.value) {
      fetchCustomers(page);
    }
  };

  // Import Excel (placeholder)
  const importExcel = () => {
    console.log("Import Excel functionality not implemented yet.");
    showToast("info", "Chức năng nhập bằng Excel đang được phát triển!");
  };

  // Table columns definition for DynamicTable
  const tableColumns = [
    { key: "index", label: "#", formatter: (value, item, index) => index + 1 },
    { key: "ma", label: "Mã" },
    { key: "ten", label: "Tên" },
    { key: "idTaiKhoan.email", label: "Email" },
    { key: "idTaiKhoan.soDienThoai", label: "SDT" },
    {
      key: "createdAt",
      label: "Ngày tham gia",
      formatter: (value) => {
        if (!value || isNaN(Date.parse(value))) return "Chưa có dữ liệu";
        return format(new Date(value), 'dd/MM/yyyy HH:mm:ss', { locale: vi });
      },
    },
    { key: "idDiaChiKH.diaChiCuThe", label: "Địa chỉ" },
    {
      key: "deleted",
      label: "Trạng thái",
      formatter: (value) =>
        value
          ? `<div style="display: inline-block; background-color: #f3f4f6; color: #ef4444; padding: 4px 12px; border-radius: 16px; font-weight: 500;">Hủy kích hoạt</div>`
          : `<div style="display: inline-block; background-color: #f3f4f6; color: #10b981; padding: 4px 12px; border-radius: 16px; font-weight: 500;">Kích hoạt</div>`,
    },
    {
      key: "actions",
      label: "Thao Tác",
      formatter: (value, item) => `
        <td class="px-6 py-4 text-center">
          <a href="/update-khach-hang?id=${item.id}" class="text-orange-500 hover:text-orange-500 transition">
            <i class="fas fa-pen-to-square"></i>
          </a>
          <button class="text-red-600 hover:text-red-800 transition ml-4" onclick="document.dispatchEvent(new CustomEvent('showDeleteConfirm', { detail: '${item.id}' }))">
            <i class="fas fa-trash"></i>
          </button>
        </td>
      `,
    },
  ];

  // Utility to get nested value (used by DynamicTable)
  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => (acc && acc[part] !== undefined ? acc[part] : ""), obj) || "";
  };

  // Xử lý sự kiện showDeleteConfirm từ DynamicTable
  const handleShowDeleteConfirm = (event) => {
    showDeleteConfirm(event.detail);
  };

  onMounted(() => {
    document.addEventListener("showDeleteConfirm", handleShowDeleteConfirm);
    fetchCustomers();
  });

  onUnmounted(() => {
    document.removeEventListener("showDeleteConfirm", handleShowDeleteConfirm);
  });

  return {
    dataTable,
    searchKH,
    filterStatus,
    currentPage,
    totalPages,
    visible,
    message,
    type,
    showConfirmModal,
    selectedCustomerId,
    showToast,
    fetchCustomers,
    btnSearch,
    backSearch,
    onFilterChange,
    showDeleteConfirm,
    confirmDelete,
    editCustomer,
    goToPage,
    importExcel,
    tableColumns,
    getNestedValue,
    isLoading,
  };
}