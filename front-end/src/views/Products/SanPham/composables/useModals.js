import { ref } from 'vue';
import * as XLSX from 'xlsx';

export function useModals(dropdownOpen, toggleDropdown, toast) {
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

  const validateImei = (imei) => {
    return /^\d{15}$/.test(imei);
  };

  const saveImei = () => {
    if (currentVariantIndex.value === null) return;

    const imeis = imeiInput.value
      .split('\n')
      .map(imei => imei.trim())
      .filter(imei => imei.length > 0);

    const invalidImeis = [];
    const validImeis = imeis.filter(imei => {
      const isValid = validateImei(imei);
      if (!isValid) invalidImeis.push(imei);
      return isValid;
    });

    if (invalidImeis.length > 0) {
      toast.value.kshowToast(
        'error',
        `${invalidImeis.length} IMEI không hợp lệ. IMEI phải có đúng 15 chữ số.`
      );
      return;
    }

    variantImeis.value = {
      ...variantImeis.value,
      [currentVariantIndex.value]: validImeis
    };

    toast.value.kshowToast('success', 'Lưu IMEI thành công!');
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

        const imeis = jsonData
          .slice(1)
          .map(row => row[0]?.toString().trim())
          .filter(imei => imei && imei.length > 0);

        if (imeis.length === 0) {
          toast.value.kshowToast('error', 'Không tìm thấy IMEI nào trong file Excel.');
          return;
        }

        const invalidImeis = imeis.filter(imei => !validateImei(imei));
        if (invalidImeis.length > 0) {
          toast.value.kshowToast(
            'warning',
            `File chứa ${invalidImeis.length} IMEI không hợp lệ. IMEI phải có đúng 15 chữ số.`
          );
        }

        imeiInput.value = imeis.join('\n');
        toast.value.kshowToast('success', `Đã nhập ${imeis.length} IMEI từ file Excel.`);
      } catch (error) {
        console.error('Error reading Excel file:', error);
        toast.value.kshowToast('error', 'Lỗi khi đọc file Excel: ' + error.message);
      }
    };
    reader.onerror = (error) => {
      console.error('Error loading file:', error);
      toast.value.kshowToast('error', 'Không thể tải file: ' + error.message);
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
    const wsData = [['IMEI']];
    const ws = XLSX.utils.aoa_to_sheet(wsData);
    ws['!cols'] = [{ wch: 20, style: { numFmt: '@' } }];
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'IMEI Template');
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