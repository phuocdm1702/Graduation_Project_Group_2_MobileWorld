import { ref } from 'vue';

export function useModals() {
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
    const imeis = imeiInput.value
      .split('\n')
      .map((imei) => imei.trim())
      .filter((imei) => imei.length > 0);
    variantImeis.value[currentVariantIndex.value] = imeis;
    closeImeiModal();
  };

  const handleExcelImport = (event) => {
    const file = event.target.files[0];
    if (!file) return;
    // Giả lập đọc file Excel (cần thư viện như XLSX để xử lý thực tế)
    const mockImeis = ['123456789012345', '678901234567890'];
    imeiInput.value = mockImeis.join('\n');
  };

  const resetModals = () => {
    showFormModal.value = false;
    showColorModal.value = false;
    showImeiModal.value = false;
    currentAttribute.value = '';
    currentVariantIndex.value = null;
    imeiInput.value = '';
    variantImeis.value = {};
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
    resetModals
  };
}