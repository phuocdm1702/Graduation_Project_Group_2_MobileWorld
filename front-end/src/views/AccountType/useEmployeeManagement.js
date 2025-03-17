import { ref, onMounted, onUnmounted } from "vue";
import axios from "axios";
import { useRouter, useRoute } from "vue-router"; // Thêm useRoute để lấy query
import { format } from 'date-fns';
import { vi } from 'date-fns/locale';

export default function useEmployeeManagement() {
  const router = useRouter();
  const route = useRoute(); // Thêm route để đọc query

  // Toast notification
  const visible = ref(false);
  const message = ref("");
  const type = ref("success");

  // Employee image
  const employeeImage = ref(null);

  const dataTable = ref([]);
  const originalData = ref([]); // Lưu trữ dữ liệu gốc để tìm kiếm

  const currentPage = ref(0);
  const itemsPerPage = 5  ;
  const totalPages = ref(0);

  const showConfirmModal = ref(false);
  const selectedNVId = ref(null);

  const filterStatus = ref("tat-ca");
  const searchNV = ref("");


  const isLoading = ref(false);
  
  // Preview image
  function previewImage(event) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        employeeImage.value = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  // Fetch employees
  const fetchNhanVien = async () => {
    try {
      const res = await axios.get("http://localhost:8080/nhan-vien/home");
      originalData.value = res.data || [];

      // Kiểm tra nếu có nhân viên mới từ query
      const newEmployee = route.query.newEmployee;
      if (newEmployee) {
        const parsedEmployee = JSON.parse(newEmployee);
        originalData.value.unshift(parsedEmployee); // Thêm nhân viên mới vào đầu danh sách
        router.replace({ query: null }); // Xóa query sau khi xử lý
      }

      applyFilterAndSearch();
    } catch (error) {
      console.error("Lỗi khi lấy danh sách nhân viên:", error);
      showToast("error", "Không thể lấy danh sách nhân viên!");
    }
  };

  const applyFilterAndSearch = () => {
    let filteredData = [...originalData.value];

    if (filterStatus.value === "dang-lam") {
      filteredData = filteredData.filter((nv) => !nv.deleted);
    } else if (filterStatus.value === "da-nghi") {
      filteredData = filteredData.filter((nv) => nv.deleted);
    }

    if (searchNV.value.trim()) {
      filteredData = filteredData.filter(
        (nhanvien) =>
          nhanvien?.idTaiKhoan?.email?.toLowerCase().includes(searchNV.value.toLowerCase()) ||
          nhanvien?.tenNhanVien?.toLowerCase().includes(searchNV.value.toLowerCase()) ||
          nhanvien?.idTaiKhoan?.soDienThoai?.toLowerCase().includes(searchNV.value.toLowerCase())
      );
    }

    totalPages.value = Math.ceil(filteredData.length / itemsPerPage);

    const start = currentPage.value * itemsPerPage;
    const end = start + itemsPerPage;
    dataTable.value = filteredData.slice(start, end);
  };

  const btnSearch = () => {
    currentPage.value = 0;
    applyFilterAndSearch();
  };

  const onFilterChange = () => {
    currentPage.value = 0;
    applyFilterAndSearch();
  };

  const goToPage = (page) => {
    currentPage.value = page;
    applyFilterAndSearch();
  };

  const showToast = (toastType, msg) => {
    message.value = msg;
    type.value = toastType;
    visible.value = true;
    setTimeout(() => {
      visible.value = false;
    }, 3000);
  };

  const showDeleteConfirm = (id) => {
    selectedNVId.value = id;
    showConfirmModal.value = true;
  };

  // Delete employee
  const deleteNv = async () => {
    if (!selectedNVId.value) return;

    try {
      await axios.put(`http://localhost:8080/nhan-vien/delete/${selectedNVId.value}`);
      showToast("success", "Cho nhân viên nghỉ thành công!");
      await fetchNhanVien();
    } catch (error) {
      console.error("Lỗi khi xóa nhân viên:", error);
      showToast("error", "Xóa nhân viên thất bại!");
    } finally {
      showConfirmModal.value = false;
      selectedNVId.value = null;
    }
  };

  const toggleStatus = async (id) => {
    try {
      isLoading.value = true;
      const response = await axios.put(`http://localhost:8080/nhan-vien/toggle-status/${id}`);
      await fetchNhanVien(currentPage.value);
      showToast("success", response.data.message || "Đã thay đổi trạng thái thành công!");
    } catch (error) {
      console.error("Lỗi khi thay đổi trạng thái:", error);
      showToast("error", "Không thể thay đổi trạng thái: " + (error.response?.data?.message || error.message));
    } finally {
      isLoading.value = false;
    }
  };

  const tableColumns = [
    { key: "index", label: "#", formatter: (value, item, index) => currentPage.value * itemsPerPage + index + 1 },
    { key: "ma", label: "Mã" },
    { key: "tenNhanVien", label: "Tên" },
    { key: "idTaiKhoan.email", label: "Email" },
    { key: "idTaiKhoan.soDienThoai", label: "SĐT" },
    {
      key: "createdAt",
      label: "Ngày tham gia",
      formatter: (value) => {
        if (!value || isNaN(Date.parse(value))) return "Chưa có dữ liệu";
        return format(new Date(value), 'dd/MM/yyyy HH:mm:ss', { locale: vi });
      },
    },
    {
      key: "diaChiCuThe",
      label: "Địa chỉ",
      formatter: (value) => `
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
          ${value || "Chưa có dữ liệu"}
        </div>
      `,
    },
    {
      key: "deleted",
      label: "Trạng thái",
      formatter: (value) =>
        value
          ? `<div style="display: inline-block; background-color: #f3f4f6; color: #ef4444; padding: 4px 12px; border-radius: 16px; font-weight: 500;">Đã nghỉ</div>`
          : `<div style="display: inline-block; background-color: #f3f4f6; color: #10b981; padding: 4px 12px; border-radius: 16px; font-weight: 500;">Đang làm</div>`,
    },
    {
      key: "actions",
      label: "Thao Tác",
      formatter: (value, item) => `
        <td class="px-6 py-4 text-center">
          <a href="/update-nhan-vien?id=${item.id}" class="text-orange-500 hover:text-orange-500 transition">
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
//css cho Table
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
    `;
    document.head.appendChild(styleTag);
  }

  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => acc && acc[part], obj) || "";
  };

  // Xử lý sự kiện showDeleteConfirm từ DynamicTable
  const handleShowDeleteConfirm = (event) => {
    showDeleteConfirm(event.detail);
  };

  onMounted(async () => {
    document.addEventListener("showDeleteConfirm", handleShowDeleteConfirm);
    document.addEventListener("toggleStatus", (event) => toggleStatus(event.detail));
    await fetchNhanVien();
    injectCSS();
  });

  onUnmounted(() => {
    document.removeEventListener("showDeleteConfirm", handleShowDeleteConfirm);
    document.removeEventListener("toggleStatus", (event) => toggleStatus(event.detail));    
  });

  return {
    visible,
    message,
    type,
    employeeImage,
    previewImage,
    dataTable,
    showConfirmModal,
    selectedNVId,
    showDeleteConfirm,
    showToast,
    filterStatus,
    fetchNhanVien,
    searchNV,
    btnSearch,
    onFilterChange,
    deleteNv,
    tableColumns,
    getNestedValue,
    currentPage,
    totalPages,
    goToPage,
  };
}