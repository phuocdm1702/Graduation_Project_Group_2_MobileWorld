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
