import { ref, onMounted, onUnmounted } from "vue";
import axios from "axios";
import { useRouter, useRoute } from "vue-router";
import { format, parse, isValid } from "date-fns";
import { vi } from "date-fns/locale";
import * as XLSX from "xlsx";

export default function useCustomerManagement(toastRef) {
  const router = useRouter();
  const route = useRoute();

  const dataTable = ref([]);
  const originalData = ref([]);
  const allData = ref([]);
  const searchKH = ref("");
  const filterStatus = ref("tat-ca");
  const currentPage = ref(0);
  const totalPages = ref(1);
  const visible = ref(false);
  const message = ref("");
  const type = ref("success");
  const showConfirmModal = ref(false);
  const selectedCustomerId = ref(null);
  const isLoading = ref(false);
  const itemsPerPage = 5;

  const showToast = (toastType, msg) => {
    if (toastRef?.value) {
      toastRef.value.kshowToast(toastType, msg);
    } else {
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

      for (const customer of originalData.value) {
        const addressRes = await axios.get(`http://localhost:8080/dia-chi/getByKhachHang/${customer.id}`);
        const addresses = addressRes.data || [];
        const defaultAddress = addresses.find((addr) => addr.macDinh) || addresses[0] || {};
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

        for (const customer of customers) {
          const addressRes = await axios.get(`http://localhost:8080/dia-chi/getByKhachHang/${customer.id}`);
          const addresses = addressRes.data || [];
          const defaultAddress = addresses.find((addr) => addr.macDinh) || addresses[0] || {};
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

    if (searchKH.value.trim() || filterStatus.value !== "tat-ca") {
      filteredData = [...allData.value];
    } else {
      filteredData = [...originalData.value];
    }

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

    totalPages.value = Math.ceil(filteredData.length / itemsPerPage) || 1;
    currentPage.value = Math.min(currentPage.value, totalPages.value - 1);
    const startIndex = currentPage.value * itemsPerPage;
    dataTable.value = filteredData.slice(startIndex, startIndex + itemsPerPage);
  };

  const btnSearch = () => {
    if (!allData.value.length) {
      fetchAllCustomers();
    } else {
      currentPage.value = 0;
      applyFilterAndSearch();
    }
  };

  const backSearch = async () => {
    searchKH.value = "";
    filterStatus.value = "tat-ca";
    currentPage.value = 0;
    allData.value = [];
    await fetchCustomers(0);
  };

  const toggleStatus = async (id) => {
    try {
      isLoading.value = true;
      const response = await axios.put(`http://localhost:8080/khach-hang/toggle-status/${id}`);
      await fetchCustomers(currentPage.value);
      allData.value = [];
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
      allData.value = [];
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
        currentPage.value = page;
        applyFilterAndSearch();
      } else {
        fetchCustomers(page);
      }
    }
  };

  const importExcel = async (event) => {
    try {
      const file = event.target.files[0];
      if (!file) {
        showToast("error", "Vui lòng chọn file Excel!");
        return;
      }

      const reader = new FileReader();
      reader.onload = async (e) => {
        const data = new Uint8Array(e.target.result);
        const workbook = XLSX.read(data, { type: "array" });

        const worksheet = workbook.Sheets[workbook.SheetNames[0]];
        const jsonData = XLSX.utils.sheet_to_json(worksheet);

        if (!jsonData.length) {
          showToast("error", "File Excel không có dữ liệu!");
          return;
        }

        const customersFromExcel = jsonData.map((row) => {
          let createdAt;
          const rawDate = row["Ngày tham gia"];
          if (rawDate) {
            try {
              const parsedDate = parse(rawDate, "dd/MM/yyyy HH:mm:ss", new Date(), { locale: vi });
              if (isValid(parsedDate)) {
                createdAt = parsedDate.toISOString();
              } else {
                createdAt = new Date().toISOString();
              }
            } catch (error) {
              console.warn(`Ngày không hợp lệ: ${rawDate}, sử dụng ngày hiện tại.`);
              createdAt = new Date().toISOString();
            }
          } else {
            createdAt = new Date().toISOString();
          }

          let diaChiCuThe = "Chưa có dữ liệu";
          let thanhPho = "N/A";
          let quan = "N/A";
          let phuong = "N/A";

          if (row["Địa chỉ"] && typeof row["Địa chỉ"] === "string") {
            const addressParts = row["Địa chỉ"].split(",");
            diaChiCuThe = addressParts[0]?.trim() || "Chưa có dữ liệu";
            thanhPho = addressParts[1]?.trim() || "N/A";
            quan = addressParts[2]?.trim() || "N/A";
            phuong = addressParts[3]?.trim() || "N/A";
          }

          return {
            ma: row["Mã"] || "N/A",
            ten: row["Tên"] || "N/A",
            idTaiKhoan: {
              email: row["Email"] || "N/A",
              soDienThoai: row["SDT"] || "N/A",
              tenDangNhap: row["Email"] || "N/A",
              idQuyenHan: { id: 2 },
              deleted: false,
            },
            createdAt: createdAt,
            idDiaChiKH: {
              diaChiCuThe,
              thanhPho,
              quan,
              phuong,
            },
            deleted: row["Trạng thái"] === "Hủy kích hoạt",
          };
        });

        // Kiểm tra dữ liệu trước khi gửi
        for (const customer of customersFromExcel) {
          if (customer.ma === "N/A" || customer.ten === "N/A" || customer.idTaiKhoan.email === "N/A") {
            showToast("error", "Dữ liệu trong file Excel không hợp lệ: Mã, Tên, Email không được để trống!");
            return;
          }
        }

        customersFromExcel.forEach((newCustomer) => {
          const existingCustomerIndex = originalData.value.findIndex(
            (customer) => customer.ma === newCustomer.ma
          );

          if (existingCustomerIndex !== -1) {
            originalData.value[existingCustomerIndex] = {
              ...originalData.value[existingCustomerIndex],
              ...newCustomer,
            };
          } else {
            originalData.value.push(newCustomer);
          }
        });

        console.log("Dữ liệu gửi lên server:", originalData.value);

        const response = await axios.post("http://localhost:8080/khach-hang/import", originalData.value);
        if (response.status === 200) {
          showToast("success", "Cập nhật dữ liệu từ Excel thành công!");
          await fetchCustomers(currentPage.value);
          allData.value = [];
        } else {
          showToast("error", "Lưu dữ liệu vào server thất bại!");
        }
      };
      reader.readAsArrayBuffer(file);
    } catch (error) {
      console.error("Lỗi khi nhập Excel:", error.response?.data || error.message);
      showToast("error", error.response?.data || "Nhập dữ liệu từ Excel thất bại!");
    }
  };
  const exportExcel = () => {
    try {
      // Dữ liệu để xuất (sử dụng allData nếu có, nếu không thì dùng originalData)
      const dataToExport = allData.value.length > 0 ? allData.value : originalData.value;

      if (!dataToExport.length) {
        showToast("error", "Không có dữ liệu để xuất!");
        return;
      }

      // Chuyển đổi dữ liệu thành định dạng phù hợp cho Excel
      const exportData = dataToExport.map((customer, index) => ({
        "#": index + 1,
        "Mã": customer.ma || "N/A",
        "Tên": customer.ten || "N/A",
        "Email": customer.idTaiKhoan?.email || "N/A",
        "SDT": customer.idTaiKhoan?.soDienThoai || "N/A",
        "Ngày tham gia": customer.createdAt
          ? format(new Date(customer.createdAt), "dd/MM/yyyy HH:mm:ss", { locale: vi })
          : "Chưa có dữ liệu",
        "Địa chỉ": customer.idDiaChiKH
          ? `${customer.idDiaChiKH.diaChiCuThe}, ${customer.idDiaChiKH.thanhPho}, ${customer.idDiaChiKH.quan}, ${customer.idDiaChiKH.phuong}`
          : "Chưa có dữ liệu",
        "Trạng thái": customer.deleted ? "Hủy kích hoạt" : "Kích hoạt",
      }));

      // Tạo worksheet từ dữ liệu
      const worksheet = XLSX.utils.json_to_sheet(exportData);
      const workbook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(workbook, worksheet, "KhachHang");

      // Tải file Excel
      XLSX.writeFile(workbook, "DanhSachKhachHang.xlsx");
      showToast("success", "Xuất dữ liệu ra Excel thành công!");
    } catch (error) {
      console.error("Lỗi khi xuất Excel:", error);
      showToast("error", "Xuất dữ liệu ra Excel thất bại!");
    }
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
        return format(new Date(value), "dd/MM/yyyy HH:mm:ss", { locale: vi });
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
          ${item.idDiaChiKH ? `${item.idDiaChiKH.diaChiCuThe}, ${item.idDiaChiKH.phuong}, ${item.idDiaChiKH.quan}, ${item.idDiaChiKH.thanhPho}` : "Chưa có dữ liệu"}
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
            <input type="checkbox" ${item.deleted ? "" : "checked"} onchange="document.dispatchEvent(new CustomEvent('toggleStatus', { detail: '${item.id}' }))">
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
    const customerIndex = dataTable.value.findIndex((customer) => customer.id === id);
    if (customerIndex !== -1) {
      dataTable.value[customerIndex].idDiaChiKH = {
        ...dataTable.value[customerIndex].idDiaChiKH,
        diaChiCuThe,
        thanhPho,
        quan,
        phuong,
      };
      const originalIndex = originalData.value.findIndex((customer) => customer.id === id);
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
    exportExcel,
    tableColumns,
    getNestedValue,
    isLoading,
  };
}