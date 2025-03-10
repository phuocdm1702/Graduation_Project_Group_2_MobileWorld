import axios from "axios";

export const fetchNhanVien = async (page, size, status) => {
  try {
    const res = await axios.get(`http://localhost:8080/nhan-vien/home`, {
      params: {
        page: page - 1,  // Backend thường sử dụng page bắt đầu từ 0
        size,
        status
      }
    });
    return res.data;
  } catch (error) {
    console.error("Lỗi khi lấy danh sách nhân viên:", error);
    return { content: [], totalPages: 0 };
  }
};
// Hiển thị modal khi bấm nút "Thêm nhân viên"
function openModal() {
  document.getElementById("modalAddEmployee").classList.remove("hidden");
}

// Ẩn modal khi bấm "Hủy"
function closeModal() {
  document.getElementById("modalAddEmployee").classList.add("hidden");
}

// Lưu nhân viên mới
function saveEmployee() {
  const tenNhanVien = document.getElementById("tenNhanVien").value;
  const maNhanVien = document.getElementById("maNhanVien").value;

  if (!tenNhanVien || !maNhanVien) {
    alert("Vui lòng nhập đầy đủ thông tin!");
    return;
  }

  console.log("Nhân viên mới:", { tenNhanVien, maNhanVien });

  // Ẩn modal sau khi lưu
  closeModal();
}
