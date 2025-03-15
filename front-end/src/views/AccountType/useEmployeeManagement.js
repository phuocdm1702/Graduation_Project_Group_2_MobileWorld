import { ref, onMounted, onUnmounted } from "vue";
import axios from "axios";
import { RouterLink } from "vue-router";

export default function useEmployeeManagement() {
  // Toast notification
  const visible = ref(false);
  const message = ref("");
  const type = ref("success");

  // Employee image
  const employeeImage = ref(null);

  // Data table
  const dataTable = ref([]);
  const originalData = ref([]); // Lưu trữ dữ liệu gốc để tìm kiếm

  // Confirm modal
  const showConfirmModal = ref(false);
  const selectedNVId = ref(null);

  // Filter and search
  const filterStatus = ref("tat-ca");
  const searchNV = ref("");

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
      originalData.value = res.data || []; // Lưu trữ dữ liệu gốc

      applyFilterAndSearch(); // Áp dụng lọc và tìm kiếm
    } catch (error) {
      console.error("Lỗi khi lấy danh sách nhân viên:", error);
      showToast("error", "Không thể lấy danh sách nhân viên!");
    }
  };

  // Apply filter and search logic
  const applyFilterAndSearch = () => {
    let filteredData = [...originalData.value];

    // Lọc theo trạng thái
    if (filterStatus.value === "dang-lam") {
      filteredData = filteredData.filter((nv) => !nv.deleted); // Đang làm
    } else if (filterStatus.value === "da-nghi") {
      filteredData = filteredData.filter((nv) => nv.deleted); // Đã nghỉ
    }

    // Tìm kiếm
    if (searchNV.value.trim()) {
      filteredData = filteredData.filter(
        (nhanvien) =>
          nhanvien?.idTaiKhoan?.email?.toLowerCase().includes(searchNV.value.toLowerCase()) ||
          nhanvien?.tenNhanVien?.toLowerCase().includes(searchNV.value.toLowerCase()) ||
          nhanvien?.idTaiKhoan?.soDienThoai?.toLowerCase().includes(searchNV.value.toLowerCase())
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

  // Show toast notification
  const showToast = (toastType, msg) => {
    message.value = msg;
    type.value = toastType;
    visible.value = true;
    setTimeout(() => {
      visible.value = false;
    }, 3000);
  };

  // Show confirm modal for deletion
  const showDeleteConfirm = (id) => {
    selectedNVId.value = id;
    showConfirmModal.value = true;
  };

  // Delete employee
  const deleteNv = async () => {
    if (!selectedNVId.value) return;

    try {
      await axios.put(`http://localhost:8080/nhan-vien/delete/${selectedNVId.value}`);
      showToast("success", "Xóa nhân viên thành công!");
      await fetchNhanVien(); // Cập nhật lại danh sách sau khi xóa
    } catch (error) {
      console.error("Lỗi khi xóa nhân viên:", error);
      showToast("error", "Xóa nhân viên thất bại!");
    } finally {
      showConfirmModal.value = false;
      selectedNVId.value = null;
    }
  };

  // Định nghĩa các cột cho DynamicTable
  const tableColumns = [
    { key: "index", label: "#", formatter: (value, item, index) => index + 1 },
    { key: "ma", label: "Mã" },
    { key: "tenNhanVien", label: "Tên" },
    { key: "idTaiKhoan.email", label: "Email" },
    { key: "idTaiKhoan.soDienThoai", label: "SĐT" },
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
          ? `<div style="
              display: inline-block;
              background-color: #f3f4f6;
              color: #ef4444;
              padding: 4px 12px;
              border-radius: 16px;
              font-weight: 500;
            ">Đã nghỉ</div>`
          : `<div style="
              display: inline-block;
              background-color: #f3f4f6;
              color: #10b981;
              padding: 4px 12px;
              border-radius: 16px;
              font-weight: 500;
            ">Đang làm</div>`,
    },
    {
      key: "actions",
      label: "Thao Tác",
      formatter: (value, item) => `
        <td class="px-6 py-4 text-center">
          <a href="/update-nhan-vien?id=${item.id}" class="text-blue-600 hover:text-blue-800 transition">
            <i class="fas fa-pen-to-square"></i>
          </a>
          <button class="text-red-600 hover:text-red-800 transition ml-4" onclick="document.dispatchEvent(new CustomEvent('showDeleteConfirm', { detail: '${item.id}' }))">
            <i class="fas fa-trash"></i>
          </button>
        </td>
      `,
    },
  ];

  // Hàm getNestedValue để truy cập thuộc tính lồng nhau
  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => acc && acc[part], obj) || "";
  };

  // Xử lý sự kiện showDeleteConfirm từ DynamicTable
  const handleShowDeleteConfirm = (event) => {
    showDeleteConfirm(event.detail);
  };

  // Thêm sự kiện lắng nghe và dọn dẹp
  onMounted(() => {
    document.addEventListener("showDeleteConfirm", handleShowDeleteConfirm);
    fetchNhanVien();
  });

  // Dọn dẹp sự kiện khi component bị hủy
  onUnmounted(() => {
    document.removeEventListener("showDeleteConfirm", handleShowDeleteConfirm);
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
    onFilterChange, // Trả về để sử dụng trong template khi filter thay đổi
    deleteNv, // Hàm xóa nhân viên
    tableColumns,
    getNestedValue,
  };
}