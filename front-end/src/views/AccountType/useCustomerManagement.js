import { ref, onMounted, onUnmounted } from "vue";
import axios from "axios";
import { useRouter, useRoute } from "vue-router";
import { format } from 'date-fns';
import { vi } from 'date-fns/locale';

export default function useCustomerManagement(toastRef) { // Nhận toastRef từ component cha
  const router = useRouter();
  const route = useRoute();

  const dataTable = ref([]);
  const originalData = ref([]);
  const allData = ref([]); // Lưu trữ toàn bộ dữ liệu khi cần tìm kiếm/lọc
  const searchKH = ref("");
  const filterStatus = ref("tat-ca");
  const currentPage = ref(0);
  const totalPages = ref(1);
  const visible = ref(false); // Giữ lại để tương thích, nhưng sẽ không dùng nếu có toastRef
  const message = ref(""); // Giữ lại để tương thích
  const type = ref("success"); // Giữ lại để tương thích
  const showConfirmModal = ref(false);
  const selectedCustomerId = ref(null);
  const isLoading = ref(false);
  const itemsPerPage = 5;

  const showToast = (toastType, msg) => {
    if (toastRef?.value) {
      toastRef.value.kshowToast(toastType, msg); // Sử dụng toastRef từ component cha
    } else {
      // Fallback nếu không có toastRef
      message.value = msg;
      type.value = toastType;
      visible.value = true;
      setTimeout(() => {
        visible.value = false;
      }, 3000);
    }
  };

  const fetchCustomers = async (page = 0) => {
    isLoading.value = true;
    try {
      const params = {
        page,
        size: itemsPerPage,
      };

      const res = await axios.get("http://localhost:8080/khach-hang/home", { params });
      originalData.value = res.data.content || res.data || [];
      totalPages.value = res.data.totalPages || 1;
      currentPage.value = page;

      // Đồng bộ địa chỉ mặc định từ danh sách địa chỉ
      for (const customer of originalData.value) {
        const addressRes = await axios.get(`http://localhost:8080/dia-chi/getByKhachHang/${customer.id}`);
        const addresses = addressRes.data || [];
        const defaultAddress = addresses.find(addr => addr.macDinh) || addresses[0] || {};
        customer.idDiaChiKH = {
          diaChiCuThe: defaultAddress.diaChiCuThe || "Chưa có dữ liệu",
          thanhPho: defaultAddress.thanhPho || "",
          quan: defaultAddress.quan || "",
          phuong: defaultAddress.phuong || "",
        };
      }

      const newCustomer = route.query.newCustomer;
      if (newCustomer) {
        const parsedCustomer = JSON.parse(newCustomer);
        originalData.value.unshift(parsedCustomer);
        router.replace({ query: null });
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

  // Lấy toàn bộ dữ liệu từ API (dùng khi tìm kiếm/lọc)
  const fetchAllCustomers = async () => {
    isLoading.value = true;
    try {
      let allCustomers = [];
      let page = 0;
      let hasMore = true;

      while (hasMore) {
        const params = {
          page,
          size: itemsPerPage,
        };
        const res = await axios.get("http://localhost:8080/khach-hang/home", { params });
        const customers = res.data.content || res.data || [];
        allCustomers = [...allCustomers, ...customers];

        // Đồng bộ địa chỉ mặc định
        for (const customer of customers) {
          const addressRes = await axios.get(`http://localhost:8080/dia-chi/getByKhachHang/${customer.id}`);
          const addresses = addressRes.data || [];
          const defaultAddress = addresses.find(addr => addr.macDinh) || addresses[0] || {};
          customer.idDiaChiKH = {
            diaChiCuThe: defaultAddress.diaChiCuThe || "Chưa có dữ liệu",
            thanhPho: defaultAddress.thanhPho || "",
            quan: defaultAddress.quan || "",
            phuong: defaultAddress.phuong || "",
          };
        }

        if (customers.length < itemsPerPage || page >= res.data.totalPages - 1) {
          hasMore = false;
        } else {
          page++;
        }
      }

      allData.value = allCustomers;
      applyFilterAndSearch();
    } catch (error) {
      console.error("Lỗi khi lấy toàn bộ danh sách khách hàng:", error);
      showToast("error", "Không thể lấy toàn bộ danh sách khách hàng!");
    } finally {
      isLoading.value = false;
    }
  };

  const applyFilterAndSearch = () => {
    let filteredData = [];

    // Nếu có tìm kiếm hoặc lọc, sử dụng allData (toàn bộ dữ liệu)
    if (searchKH.value.trim() || filterStatus.value !== "tat-ca") {
      filteredData = [...allData.value];
    } else {
      // Nếu không có tìm kiếm/lọc, sử dụng dữ liệu từ API (phân trang phía server)
      filteredData = [...originalData.value];
    }

    // Áp dụng bộ lọc trạng thái
    if (filterStatus.value === "kich-hoat") {
      filteredData = filteredData.filter((kh) => !kh.deleted);
    } else if (filterStatus.value === "huy-kich-hoat") {
      filteredData = filteredData.filter((kh) => kh.deleted);
    }

    // Áp dụng tìm kiếm
    if (searchKH.value.trim()) {
      filteredData = filteredData.filter(
        (khachhang) =>
          khachhang?.idTaiKhoan?.email?.toLowerCase().includes(searchKH.value.toLowerCase()) ||
          khachhang?.ten?.toLowerCase().includes(searchKH.value.toLowerCase()) ||
          khachhang?.idTaiKhoan?.soDienThoai?.toLowerCase().includes(searchKH.value.toLowerCase())
      );
    }

    // Cập nhật totalPages và dataTable
    totalPages.value = Math.ceil(filteredData.length / itemsPerPage) || 1;
    currentPage.value = Math.min(currentPage.value, totalPages.value - 1);
    const startIndex = currentPage.value * itemsPerPage;
    dataTable.value = filteredData.slice(startIndex, startIndex + itemsPerPage);
  };

  const btnSearch = () => {
    if (!allData.value.length) {
      fetchAllCustomers(); // Lấy toàn bộ dữ liệu nếu chưa có
    } else {
      currentPage.value = 0;
      applyFilterAndSearch();
    }
  };

  const backSearch = async () => {
    searchKH.value = "";
    filterStatus.value = "tat-ca";
    currentPage.value = 0;
    allData.value = []; // Reset allData để sử dụng dữ liệu từ API
    await fetchCustomers(0);
  };

  const toggleStatus = async (id) => {
    try {
      isLoading.value = true;
      const response = await axios.put(`http://localhost:8080/khach-hang/toggle-status/${id}`);
      await fetchCustomers(currentPage.value);
      allData.value = []; // Reset allData để đồng bộ lại
      showToast("success", response.data.message || "Đã thay đổi trạng thái thành công!");
    } catch (error) {
      console.error("Lỗi khi thay đổi trạng thái:", error);
      showToast("error", "Không thể thay đổi trạng thái: " + (error.response?.data?.message || error.message));
    } finally {
      isLoading.value = false;
    }
  };

  const showDeleteConfirm = (id) => {
    selectedCustomerId.value = id;
    showConfirmModal.value = true;
  };

  const confirmDelete = async () => {
    if (!selectedCustomerId.value) return;

    try {
      await axios.put(`http://localhost:8080/khach-hang/delete/${selectedCustomerId.value}`);
      showToast("success", "Hủy kích hoạt khách hàng thành công!");
      await fetchCustomers(currentPage.value);
      allData.value = []; // Reset allData để đồng bộ lại
    } catch (error) {
      console.error("Lỗi khi xóa khách hàng:", error);
      showToast("error", "Không thể xóa khách hàng!");
    } finally {
      showConfirmModal.value = false;
      selectedCustomerId.value = null;
    }
  };

  const editCustomer = (customer) => {
    console.log("Editing customer:", customer);
  };

  const goToPage = (page) => {
    if (page >= 0 && page < totalPages.value && page !== currentPage.value) {
      if (searchKH.value.trim() || filterStatus.value !== "tat-ca") {
        // Nếu đang tìm kiếm hoặc lọc, chỉ cập nhật trang cục bộ
        currentPage.value = page;
        applyFilterAndSearch();
      } else {
        // Nếu không tìm kiếm hoặc lọc, gọi API để lấy dữ liệu trang mới
        fetchCustomers(page);
      }
    }
  };

  const importExcel = () => {
    console.log("Import Excel functionality not implemented yet.");
    showToast("info", "Chức năng nhập bằng Excel đang được phát triển!");
  };

  const tableColumns = [
    { key: "index", label: "#", formatter: (value, item, index) => index + 1 + (currentPage.value * itemsPerPage) },
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
    {
      key: "idDiaChiKH.diaChiCuThe",
      label: "Địa chỉ",
      formatter: (value, item) => `
        <div style="
          min-width: 150px;
          max-width: 150px;
          text-align: center;
          white-space: normal;
          word-wrap: break-word;
          min-height: 60px;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          padding: 8px;
          line-height: 1.5;
        ">
          ${item.idDiaChiKH ? `${item.idDiaChiKH.diaChiCuThe}
          , ${item.idDiaChiKH.phuong},
           ${item.idDiaChiKH.quan}
          , ${item.idDiaChiKH.thanhPho}` : "Chưa có dữ liệu"}
        </div>
      `,
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
          <a href="/update-khach-hang?id=${item.id}" class="text-orange-500 hover:text-orange-500 transition">
            <i class="fas fa-pen-to-square"></i>
          </a>
          <label class="switch ml-4">
            <input type="checkbox" ${item.deleted ? '' : 'checked'} onchange="document.dispatchEvent(new CustomEvent('toggleStatus', { detail: '${item.id}' }))">
            <span class="slider round"></span>
          </label>
        </td>
      `,
    },
  ];

  function injectCSS() {
    const styleTag = document.createElement("style");
    styleTag.type = "text/css";
    styleTag.innerHTML = `
      table {
        table-layout: fixed;
        width: 100%;
        font-family: Arial, sans-serif;
      }
      td {
        color: #444;
      }
      th:nth-child(7),
      td:nth-child(7) {
        min-width: 150px !important;
        max-width: 150px !important;
        white-space: normal !important;
        word-wrap: break-word !important;
        min-height: 60px !important;
        display: flex !important;
        flex-direction: column !important;
        justify-content: center !important;
        align-items: center !important;
        padding: 8px !important;
        line-height: 1.5 !important;
      }
      th:nth-child(1), td:nth-child(1) { min-width: 50px; max-width: 50px; }
      th:nth-child(2), td:nth-child(2) { min-width: 100px; max-width: 100px; }
      th:nth-child(3), td:nth-child(3) { min-width: 150px; max-width: 150px; }
      th:nth-child(4), td:nth-child(4) { min-width: 200px; max-width: 200px; }
      th:nth-child(5), td:nth-child(5) { min-width: 120px; max-width: 120px; }
      th:nth-child(6), td:nth-child(6) { min-width: 150px; max-width: 150px; }
      th:nth-child(7), td:nth-child(7) { min-width: 150px; max-width: 150px; }
      th:nth-child(8), td:nth-child(8) { min-width: 120px; max-width: 120px; }
      th:nth-child(9), td:nth-child(9) { min-width: 120px; max-width: 120px; }

      .switch {
        position: relative;
        display: inline-block;
        width: 40px;
        height: 20px;
      }
      .switch input {
        opacity: 0;
        width: 0;
        height: 0;
      }
      .slider {
        position: absolute;
        cursor: pointer;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #ccc;
        transition: .4s;
        border-radius: 20px;
      }
      .slider:before {
        position: absolute;
        content: "";
        height: 16px;
        width: 16px;
        left: 2px;
        bottom: 2px;
        background-color: white;
        transition: .4s;
        border-radius: 50%;
      }
      input:checked + .slider:before {
        transform: translateX(20px);
      }
      input:checked + .slider {
        background-color: #f97316;
        box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.15);
      }
    `;
    document.head.appendChild(styleTag);
  }

  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => (acc && acc[part] !== undefined ? acc[part] : ""), obj) || "";
  };

  const handleShowDeleteConfirm = (event) => {
    showDeleteConfirm(event.detail);
  };

  const handleDefaultAddressChanged = (event) => {
    const { id, diaChiCuThe, thanhPho, quan, phuong } = event.detail;
    const customerIndex = dataTable.value.findIndex(customer => customer.id === id);
    if (customerIndex !== -1) {
      dataTable.value[customerIndex].idDiaChiKH = {
        ...dataTable.value[customerIndex].idDiaChiKH,
        diaChiCuThe,
        thanhPho,
        quan,
        phuong,
      };
      const originalIndex = originalData.value.findIndex(customer => customer.id === id);
      if (originalIndex !== -1) {
        originalData.value[originalIndex].idDiaChiKH = {
          ...originalData.value[originalIndex].idDiaChiKH,
          diaChiCuThe,
          thanhPho,
          quan,
          phuong,
        };
      }
    }
  };

  onMounted(async () => {
    document.addEventListener("showDeleteConfirm", handleShowDeleteConfirm);
    document.addEventListener("toggleStatus", (event) => toggleStatus(event.detail));
    document.addEventListener("defaultAddressChanged", handleDefaultAddressChanged);
    await fetchCustomers();
    injectCSS();
  });

  onUnmounted(() => {
    document.removeEventListener("showDeleteConfirm", handleShowDeleteConfirm);
    document.removeEventListener("toggleStatus", (event) => toggleStatus(event.detail));
    document.removeEventListener("defaultAddressChanged", handleDefaultAddressChanged);
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
    isLoading,
  };
}