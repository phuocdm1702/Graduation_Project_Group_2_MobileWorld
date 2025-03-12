import { ref, onMounted } from "vue";
import axios from "axios";

export default function useEmployeeManagement() {
  // Toast notification
  const visible = ref(false);
  const message = ref("");
  const type = ref("success");

  // Employee image
  const employeeImage = ref(null);

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

  // Data table
  const dataTable = ref([]);

  // Confirm modal
  const showConfirmModal = ref(false);
  const selectedNVId = ref(null);

  const showDeleteConfirm = (id) => {
    selectedNVId.value = id;
    showConfirmModal.value = true;
  };

  const showToast = (toastType, msg) => {
    message.value = msg;
    type.value = toastType;
    visible.value = true;
    setTimeout(() => {
      visible.value = false;
    }, 3000);
  };

  // Fetch employees
  const filterStatus = ref("tat-ca");

  const fetchNhanVien = async () => {
    try {
      const res = await axios.get("http://localhost:8080/nhan-vien/home");

      if (filterStatus.value === "dang-lam") {
        dataTable.value = res.data.filter((nv) => !nv.deleted); // Đang làm
      } else if (filterStatus.value === "da-nghi") {
        dataTable.value = res.data.filter((nv) => nv.deleted); // Đã nghỉ
      } else {
        dataTable.value = res.data; // Tất cả
      }
    } catch (error) {
      console.error("Lỗi khi lấy danh sách nhân viên:", error);
    }
  };

  onMounted(fetchNhanVien);

  // Delete employee
  const deleteNv = async () => {
    try {
      await axios.put(`http://localhost:8080/nhan-vien/delete/${selectedNVId.value}`);
      showToast("success", "Xóa thành công!");
      fetchNhanVien();
    } catch (e) {
      showToast("error", "Xóa thất bại!");
    }
    showConfirmModal.value = false;
  };

  // Search
  const searchNV = ref("");
  const btnSearch = () => {
    if (!searchNV.value.trim()) {
      fetchNhanVien();
      return;
    }
    dataTable.value = dataTable.value.filter(
      (nhanvien) =>
        nhanvien.idTaiKhoan.email.toLowerCase().includes(searchNV.value.toLowerCase()) ||
        nhanvien.tenNhanVien.toLowerCase().includes(searchNV.value.toLowerCase()) ||
        nhanvien.idTaiKhoan.soDienThoai.toLowerCase().includes(searchNV.value.toLowerCase())
    );
  };

  // Định nghĩa các cột cho DynamicTable
  const tableColumns = [
    { key: "index", label: "#", formatter: (value, item, index) => index + 1 },
    { key: "tenNhanVien", label: "Tên" },
    { key: "idTaiKhoan.email", label: "Email" },
    { key: "idTaiKhoan.soDienThoai", label: "SĐT" },
    {
      key: "createdAt",
      label: "Ngày tham gia",
      formatter: (value) => new Date(value).toLocaleDateString("vi-VN"),
    },
    {
      key: "trangThai",
      label: "Trạng thái",
      formatter: (value, item) => (item.deleted ? "Đã nghỉ" : "Đang làm"),
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

  // Hàm getNestedValue để truy cập thuộc tính lồng nhau
  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => acc && acc[part], obj);
  };

  // Xử lý sự kiện showDeleteConfirm từ DynamicTable
  const handleShowDeleteConfirm = (event) => {
    showDeleteConfirm(event.detail);
  };
  document.addEventListener("showDeleteConfirm", handleShowDeleteConfirm);

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
    deleteNv,
    searchNV,
    btnSearch,
    tableColumns, // Trả về tableColumns để sử dụng trong DynamicTable
    getNestedValue, // Trả về getNestedValue để sử dụng trong DynamicTable
  };
}