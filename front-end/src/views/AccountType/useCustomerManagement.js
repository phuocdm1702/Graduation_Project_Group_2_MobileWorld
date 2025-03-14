import { ref, watch } from "vue";
import axios from "axios";

export default function useCustomerManagement() {
  // Data
  const dataTable = ref([]);
  const searchKH = ref("");
  const filterStatus = ref("tat-ca");
  const currentPage = ref(1);
  const totalPages = ref(0);
  const visible = ref(false);
  const message = ref("");
  const type = ref("success");
  const showConfirmModal = ref(false);
  const selectedCustomerId = ref(null);

  // Toast notification
  const showToast = (toastType, msg) => {
    message.value = msg;
    type.value = toastType;
    visible.value = true;
    setTimeout(() => {
      visible.value = false;
    }, 3000);
  };

  // Fetch customers with pagination, search, and status filter
  const fetchCustomers = async (page = 1, size = 10) => {
    try {
      const status = filterStatus.value === "tat-ca" ? null : filterStatus.value;
      const params = {
        page: page - 1, // Backend uses 0-based paging
        size,
      };
      // Thêm tham số search nếu có giá trị
      if (searchKH.value && searchKH.value.trim()) {
        params.search = searchKH.value.trim();
      }
      // Thêm tham số status nếu có giá trị
      if (status) {
        params.status = status === "kich-hoat" ? false : status === "huy-kich-hoat" ? true : null;
      }

      const res = await axios.get("http://localhost:8080/khach-hang/home", { params });
      dataTable.value = res.data.content || res.data; // Adjust based on your API response
      totalPages.value = res.data.totalPages || 1;
      currentPage.value = page;
    } catch (error) {
      console.error("Lỗi khi lấy danh sách khách hàng:", error);
      showToast("error", "Không thể lấy danh sách khách hàng!");
    }
  };

  // Search customers
  const btnSearch = () => {
    // Gọi lại fetchCustomers với giá trị search hiện tại
    fetchCustomers(1); // Reset về trang 1 khi search
  };

  // Reset search
  const backSearch = () => {
    searchKH.value = "";
    filterStatus.value = "tat-ca"; // Reset status filter
    fetchCustomers(1); // Reset về trang 1 khi reset
  };

  // Delete customer
  const showDeleteConfirm = (id) => {
    selectedCustomerId.value = id;
    showConfirmModal.value = true;
  };

  const confirmDelete = async () => {
    try {
      await axios.put(`http://localhost:8080/khach-hang/delete/${selectedCustomerId.value}`);
      showToast("success", "Xóa mềm thành công!");
      fetchCustomers(currentPage.value);
    } catch (error) {
      console.error("Lỗi khi xóa khách hàng:", error);
      showToast("error", "Không thể xóa khách hàng!");
    }
    showConfirmModal.value = false;
  };

  // Edit customer
  const editCustomer = (customer) => {
    // Logic to open edit form or modal
    console.log("Editing customer:", customer);
  };

  // Pagination
  const goToPage = (page) => {
    fetchCustomers(page);
  };

  // Import Excel (placeholder)
  const importExcel = () => {
    console.log("Import Excel functionality not implemented yet.");
    showToast("info", "Chức năng nhập bằng Excel đang được phát triển!");
  };

  // Table columns definition for DynamicTable
  const tableColumns = [
    { key: "index", label: "#", formatter: (value, item, index) => index + 1 },
    { key: "ten", label: "Tên" },
    { key: "idTaiKhoan.email", label: "Email" },
    { key: "idTaiKhoan.soDienThoai", label: "SDT" },
    { key: "createdAt", label: "Ngày tham gia", formatter: (value) => new Date(value).toLocaleDateString("vi-VN")},
    {
      key: "trangThai",
      label: "Trạng thái",
      render: (value) => (value ? "Hủy kích hoạt" : "Kích hoạt"),
      class: (value) => (value ? "text-red-500" : "text-green-600"),
    },
    {
      key: "actions",
      label: "Thao Tác",
      formatter: (value, item) => `
        <button class="text-blue-600 hover:text-blue-800 transition" 
                onclick="document.dispatchEvent(new CustomEvent('showDeleteConfirm', { detail: '${item.id}' }))">
          <i class="fa-solid fa-edit text-orange-500"></i>
        </button>
      `,
    },
  ];

  // Utility to get nested value (used by DynamicTable)
  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => acc && acc[part], obj);
  };

  // Watch filterStatus to fetch data when it changes
  watch(filterStatus, () => {
    fetchCustomers(1); // Reset về trang 1 khi thay đổi status
  });

  // Watch searchKH to fetch data when it changes
  watch(searchKH, () => {
    btnSearch(); // Gọi btnSearch khi searchKH thay đổi
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
    showDeleteConfirm,
    confirmDelete,
    editCustomer,
    goToPage,
    importExcel,
    tableColumns,
    getNestedValue,
  };
}