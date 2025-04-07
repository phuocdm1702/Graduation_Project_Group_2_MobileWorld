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
  const fileInputRef = ref(null);
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

  const fetchCustomers = async () => {
    isLoading.value = true;
    try {
      const res = await axios.get("http://localhost:8080/khach-hang/home");
      originalData.value = res.data || [];

      for (const customer of originalData.value) {
        const addressRes = await axios.get(`http://localhost:8080/dia-chi/getByKhachHang/${customer.id}`);
        const addresses = addressRes.data || [];
        const defaultAddress = addresses.find((addr) => addr.macDinh) || addresses[0] || {};
        customer.idDiaChiKH = {
          diaChiCuThe: defaultAddress.diaChiCuThe || "Chưa có dữ liệu",
          thanhPho: defaultAddress.thanhPho || "N/A",
          quan: defaultAddress.quan || "N/A",
          phuong: defaultAddress.phuong || "N/A",
        };
      }

      const newCustomer = route.query.newCustomer;
      if (newCustomer) {
        const parsedCustomer = JSON.parse(newCustomer);
        originalData.value.unshift(parsedCustomer);
        router.replace({ query: null });
        currentPage.value = 0; // Đặt lại về trang đầu
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

  const applyFilterAndSearch = () => {
    let filteredData = [...originalData.value];

    // Sắp xếp theo createdAt giảm dần (mới nhất lên đầu)
    filteredData.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

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
    currentPage.value = 0;
    applyFilterAndSearch();
  };

  const onFilterChange = () => {
    currentPage.value = 0;
    applyFilterAndSearch();
  };

  const backSearch = async () => {
    searchKH.value = "";
    filterStatus.value = "tat-ca";
    currentPage.value = 0;
    await fetchCustomers();
  };

  const toggleStatus = async (id) => {
    try {
      isLoading.value = true;
      const response = await axios.put(`http://localhost:8080/khach-hang/toggle-status/${id}`);
      await fetchCustomers();
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
      await fetchCustomers();
    } catch (error) {
      console.error("Lỗi khi xóa khách hàng:", error);
      showToast("error", "Không thể xóa khách hàng!");
    } finally {
      showConfirmModal.value = false;
      selectedCustomerId.value = null;
    }
  };

  const goToPage = (page) => {
    if (page >= 0 && page < totalPages.value && page !== currentPage.value) {
      currentPage.value = page;
      applyFilterAndSearch();
    }
  };

  const importExcel = async (event) => {
    const file = event.target.files[0];
    if (!file) {
      showToast("error", "Vui lòng chọn file Excel!");
      return;
    }

    try {
      isLoading.value = true;

      const reader = new FileReader();
      reader.onload = async (e) => {
        try {
          const data = new Uint8Array(e.target.result);
          const workbook = XLSX.read(data, { type: "array" });
          const worksheet = workbook.Sheets[workbook.SheetNames[0]];
          const jsonData = XLSX.utils.sheet_to_json(worksheet);

          if (!jsonData.length) {
            showToast("error", "File Excel không có dữ liệu!");
            return;
          }

          const customersFromExcel = jsonData.map((row, index) => {
            let createdAt;
            const rawDate = row["Ngày tham gia"];
            if (rawDate) {
              try {
                const parsedDate = parse(rawDate, "dd/MM/yyyy HH:mm:ss", new Date(), { locale: vi });
                createdAt = isValid(parsedDate) ? parsedDate.toISOString() : new Date().toISOString();
              } catch (error) {
                console.warn(`Ngày không hợp lệ tại hàng ${index + 1}: ${rawDate}`);
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
              ma: row["Mã"]?.toString() || "N/A",
              tenKH: row["Tên"]?.toString() || "N/A",
              email: row["Email"]?.toString() || "N/A",
              soDienThoai: row["SĐT"]?.toString() || "N/A",
              userName: row["Tên đăng nhập"]?.toString() || row["Email"]?.toString() || "N/A",
              gioiTinh: row["Giới tính"] === "Nam",
              createdAt: createdAt,
              diaChiCuThe,
              thanhPho,
              quan,
              phuong,
              deleted: row["Trạng thái"] === "Hủy kích hoạt",
            };
          });

          for (let i = 0; i < customersFromExcel.length; i++) {
            const customer = customersFromExcel[i];
            if (customer.ma === "N/A") {
              showToast("error", `Hàng ${i + 1}: Mã không được để trống!`);
              return;
            }
            if (customer.tenKH === "N/A") {
              showToast("error", `Hàng ${i + 1}: Tên không được để trống!`);
              return;
            }
            if (customer.email === "N/A") {
              showToast("error", `Hàng ${i + 1}: Email không được để trống!`);
              return;
            }
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(customer.email)) {
              showToast("error", `Hàng ${i + 1}: Email không hợp lệ!`);
              return;
            }
            if (customer.soDienThoai !== "N/A") {
              const phoneRegex = /^[0-9]{10,11}$/;
              if (!phoneRegex.test(customer.soDienThoai)) {
                showToast("error", `Hàng ${i + 1}: Số điện thoại không hợp lệ!`);
                return;
              }
            }
          }

          const response = await axios.post("http://localhost:8080/khach-hang/import", customersFromExcel);
          if (response.status === 200) {
            showToast("success", "Nhập dữ liệu khách hàng từ Excel thành công!");
            await fetchCustomers();
          } else {
            showToast("error", "Lưu dữ liệu khách hàng vào server thất bại!");
          }
        } catch (error) {
          console.error("Lỗi trong quá trình đọc file Excel:", error);
          if (error.response) {
            showToast("error", `Lỗi từ server: ${error.response.data.message || "Không xác định"}`);
          } else {
            showToast("error", "Đọc file Excel thất bại!");
          }
        } finally {
          if (fileInputRef.value) {
            fileInputRef.value.value = "";
          }
          isLoading.value = false;
        }
      };
      reader.onerror = () => {
        showToast("error", "Lỗi khi đọc file Excel!");
        if (fileInputRef.value) {
          fileInputRef.value.value = "";
        }
        isLoading.value = false;
      };
      reader.readAsArrayBuffer(file);
    } catch (error) {
      console.error("Lỗi khi nhập Excel:", error);
      showToast("error", "Nhập dữ liệu từ Excel thất bại!");
      if (fileInputRef.value) {
        fileInputRef.value.value = "";
      }
      isLoading.value = false;
    }
  };

  const exportToExcel = () => {
    try {
      isLoading.value = true;
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
            khachhang?.ma?.toLowerCase().includes(searchKH.value.toLowerCase()) ||
            khachhang?.idTaiKhoan?.soDienThoai?.toLowerCase().includes(searchKH.value.toLowerCase())
        );
      }

      if (!filteredData.length) {
        showToast("error", "Không có dữ liệu để xuất!");
        return;
      }

      const dataToExport = filteredData.map((item, index) => ({
        "#": index + 1,
        "Mã": item.ma || "N/A",
        "Tên": item.ten || "N/A",
        "Email": item.idTaiKhoan?.email || "N/A",
        "SĐT": item.idTaiKhoan?.soDienThoai || "N/A",
        "Tên đăng nhập": item.idTaiKhoan?.tenDangNhap || item.idTaiKhoan?.email || "N/A",
        "Ngày tham gia": item.createdAt
          ? format(new Date(item.createdAt), "dd/MM/yyyy HH:mm:ss", { locale: vi })
          : "Chưa có dữ liệu",
        "Địa chỉ": `${item.idDiaChiKH?.diaChiCuThe || "Chưa có dữ liệu"}, ${item.idDiaChiKH?.thanhPho || "N/A"}, ${item.idDiaChiKH?.quan || "N/A"}, ${item.idDiaChiKH?.phuong || "N/A"}`,
        "Trạng thái": item.deleted ? "Hủy kích hoạt" : "Kích hoạt",
        "Giới tính": item.idTaiKhoan?.gioiTinh ? "Nam" : "Nữ",
      }));

      const worksheet = XLSX.utils.json_to_sheet(dataToExport);
      const workbook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(workbook, worksheet, "DanhSachKhachHang");

      const currentDate = format(new Date(), "yyyy-MM-dd_HH-mm-ss", { locale: vi });
      XLSX.writeFile(workbook, `Danh_sach_khach_hang_${currentDate}.xlsx`);

      showToast("success", "Xuất file Excel thành công!");
    } catch (error) {
      console.error("Lỗi khi xuất Excel:", error);
      showToast("error", "Xuất file Excel thất bại!");
    } finally {
      isLoading.value = false;
    }
  };

  const downloadTemplate = () => {
    try {
      isLoading.value = true;

      const templateHeaders = tableColumns.map((column) => ({
        [column.label]: "",
      }));

      const templateData = [Object.assign({}, ...templateHeaders)];

      const worksheet = XLSX.utils.json_to_sheet(templateData);
      const workbook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(workbook, worksheet, "MauKhachHang");

      XLSX.writeFile(workbook, "MauKhachHang.xlsx");
      showToast("success", "Tải file mẫu thành công!");
    } catch (error) {
      console.error("Lỗi khi tải file mẫu:", error);
      showToast("error", "Tải file mẫu thất bại!");
    } finally {
      isLoading.value = false;
    }
  };

  const tableColumns = [
    { key: "index", label: "#", formatter: (value, item, index) => currentPage.value * itemsPerPage + index + 1 },
    { key: "ma", label: "Mã" },
    { key: "ten", label: "Tên" },
    { key: "idTaiKhoan.email", label: "Email" },
    { key: "idTaiKhoan.soDienThoai", label: "SĐT" },
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
          ${item.idDiaChiKH ? `${item.idDiaChiKH.diaChiCuThe}, ${item.idDiaChiKH.thanhPho}, ${item.idDiaChiKH.quan}, ${item.idDiaChiKH.phuong}` : "Chưa có dữ liệu"}
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

  onMounted(async () => {
    document.addEventListener("showDeleteConfirm", handleShowDeleteConfirm);
    document.addEventListener("toggleStatus", (event) => toggleStatus(event.detail));
    await fetchCustomers();
    injectCSS();
  });

  onUnmounted(() => {
    document.removeEventListener("showDeleteConfirm", handleShowDeleteConfirm);
    document.removeEventListener("toggleStatus", (event) => toggleStatus(event.detail));
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
    onFilterChange,
    backSearch,
    showDeleteConfirm,
    confirmDelete,
    goToPage,
    importExcel,
    exportToExcel,
    downloadTemplate,
    tableColumns,
    getNestedValue,
    isLoading,
    fileInputRef,
  };
}