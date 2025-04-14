import { ref } from 'vue';
import * as XLSX from 'xlsx';

export function useModals(dropdownOpen, toggleDropdown) {
  const showFormModal = ref(false);
  const currentAttribute = ref('');
  const showColorModal = ref(false);
  const showImeiModal = ref(false);
  const currentVariantIndex = ref(null);
  const imeiInput = ref('');
  const variantImeis = ref({});

  const openAddModal = (attribute) => {
    currentAttribute.value = attribute;
    showFormModal.value = true;
  };

  const closeFormModal = () => {
    showFormModal.value = false;
    currentAttribute.value = '';
  };

  const openColorModal = () => {
    showColorModal.value = true;
    // Đóng tất cả các dropdown khi mở modal màu sắc
    dropdownOpen.value.ram = false;
    dropdownOpen.value.boNhoTrong = false;
    dropdownOpen.value.mauSac = false;
  };

  const closeColorModal = () => {
    showColorModal.value = false;
  };

  const openImeiModal = (index) => {
    currentVariantIndex.value = index;
    imeiInput.value = variantImeis.value[index]?.join('\n') || '';
    showImeiModal.value = true;
  };

  const closeImeiModal = () => {
    showImeiModal.value = false;
    imeiInput.value = '';
    currentVariantIndex.value = null;
  };

  const saveImei = () => {
    if (currentVariantIndex.value === null) return;

    // Tách các IMEI thành mảng, loại bỏ các dòng trống
    const imeis = imeiInput.value
      .split('\n')
      .map(imei => imei.trim())
      .filter(imei => imei.length > 0);

    // Lưu tất cả IMEI vào variant hiện tại
    variantImeis.value = {
      ...variantImeis.value,
      [currentVariantIndex.value]: imeis
    };

    closeImeiModal();
  };

  const handleExcelImport = (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (e) => {
      try {
        const data = new Uint8Array(e.target.result);
        const workbook = XLSX.read(data, { type: 'array' });
        const firstSheet = workbook.Sheets[workbook.SheetNames[0]];
        const jsonData = XLSX.utils.sheet_to_json(firstSheet, { header: 1 });

        // Bỏ qua dòng đầu tiên (header), đọc từ dòng thứ hai
        const imeis = jsonData
          .slice(1) // Bỏ qua dòng đầu tiên
          .map(row => row[0]?.toString().trim()) // Lấy giá trị cột đầu tiên
          .filter(imei => imei && imei.length > 0); // Loại bỏ giá trị rỗng hoặc không hợp lệ

        if (imeis.length === 0) {
          console.warn('Không tìm thấy IMEI nào trong file Excel.');
          alert('Không tìm thấy IMEI hợp lệ trong file Excel. Vui lòng kiểm tra lại.');
          return;
        }

        imeiInput.value = imeis.join('\n');
      } catch (error) {
        console.error('Error reading Excel file:', error);
        alert('Lỗi khi đọc file Excel: ' + error.message);
      }
    };
    reader.onerror = (error) => {
      console.error('Error loading file:', error);
      alert('Không thể tải file: ' + error.message);
    };
    reader.readAsArrayBuffer(file);
  };

  const resetModals = () => {
    showFormModal.value = false;
    showColorModal.value = false;
    showImeiModal.value = false;
    currentAttribute.value = '';
    currentVariantIndex.value = null;
    imeiInput.value = '';
    variantImeis.value = {};
    dropdownOpen.value.ram = false;
    dropdownOpen.value.boNhoTrong = false;
    dropdownOpen.value.mauSac = false;
  };

  const downloadImeiTemplate = () => {
    // Tạo dữ liệu mẫu với tiêu đề cột IMEI
    const wsData = [['IMEI']]; // Chỉ một cột tiêu đề
    const ws = XLSX.utils.aoa_to_sheet(wsData);

    // Tạo workbook và thêm worksheet
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'IMEI Template');

    // Xuất file Excel
    XLSX.writeFile(wb, 'imei_template.xlsx');
  };

  return {
    showFormModal,
    currentAttribute,
    showColorModal,
    showImeiModal,
    currentVariantIndex,
    imeiInput,
    variantImeis,
    openAddModal,
    closeFormModal,
    openColorModal,
    closeColorModal,
    openImeiModal,
    closeImeiModal,
    saveImei,
    handleExcelImport,
    resetModals,
    downloadImeiTemplate,
  };
}