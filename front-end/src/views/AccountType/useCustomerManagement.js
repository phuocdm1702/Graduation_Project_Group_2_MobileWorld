import { ref, onMounted, onUnmounted } from "vue";
import axios from "axios";

export default function useCustomerManagement() {
  // Data
  const dataTable = ref([]);
  const originalData = ref([]); // Lưu trữ dữ liệu gốc để tìm kiếm
  const searchKH = ref("");
  const filterStatus = ref("tat-ca");
  const currentPage = ref(1);
  const totalPages = ref(1); // Mặc định là 1 để tránh lỗi chia cho 0
  const visible = ref(false);
  const message = ref("");
  const type = ref("success");
  const showConfirmModal = ref(false);
  const selectedCustomerId = ref(null);
  const isLoading = ref(false); // Thêm trạng thái loading

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
      originalData.value = res.data.content || res.data || []; // Lưu trữ dữ liệu gốc
      totalPages.value = res.data.totalPages || 1;
      currentPage.value = page;

      applyFilterAndSearch(); // Áp dụng lọc và tìm kiếm trên dữ liệu gốc
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

    // Lọc theo trạng thái
    if (filterStatus.value === "kich-hoat") {
      filteredData = filteredData.filter((kh) => !kh.deleted); // Kích hoạt
    } else if (filterStatus.value === "huy-kich-hoat") {
      filteredData = filteredData.filter((kh) => kh.deleted); // Hủy kích hoạt
    }

    // Tìm kiếm
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
        return new Date(value).toLocaleString("vi-VN", {
          timeZone: "Asia/Ho_Chi_Minh",
        });
      },
    },
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
          <a href="/update-khach-hang?id=${item.id}" class="text-blue-600 hover:text-blue-800 transition">
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

  // Thêm sự kiện lắng nghe và dọn dẹp
  onMounted(() => {
    document.addEventListener("showDeleteConfirm", handleShowDeleteConfirm);
    fetchCustomers();
  });

  // Dọn dẹp sự kiện khi component bị hủy
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
    onFilterChange, // Trả về để sử dụng khi filter thay đổi
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