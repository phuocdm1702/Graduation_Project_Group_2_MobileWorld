import { ref, computed } from 'vue';

export function useProductVariants(ramOptions, boNhoTrongOptions, mauSacOptions, productData, variantImeis) {
  const productVariants = ref([]);
  const currentVariant = ref({
    selectedRams: [],
    selectedBoNhoTrongs: [],
    selectedMauSacs: [],
  });

  const selectedVariants = ref([]);
  const allSelected = ref({});
  const groupCommonValues = ref({});

  const groupVariantsByRamAndRom = computed(() => {
    const grouped = [];
    const seen = new Set();
    let startIndex = 0;

    productVariants.value.forEach((variant, index) => {
      const ram = ramOptions.value.find((r) => r.id === variant.idRam)?.dungLuong || 'N/A';
      const rom = boNhoTrongOptions.value.find((b) => b.id === variant.idBoNhoTrong)?.dungLuong || 'N/A';
      const key = `${ram}/${rom}`;

      if (!seen.has(key)) {
        seen.add(key);
        const variantsInGroup = productVariants.value.filter((v) => {
          const vRam = ramOptions.value.find((r) => r.id === v.idRam)?.dungLuong || 'N/A';
          const vRom = boNhoTrongOptions.value.find((b) => b.id === v.idBoNhoTrong)?.dungLuong || 'N/A';
          return `${vRam}/${vRom}` === key;
        });

        grouped.push({
          ram,
          rom,
          groupKey: key,
          variants: variantsInGroup,
          startIndex,
        });

        startIndex += variantsInGroup.length;
      }
    });

    return grouped;
  });

  const uniqueColors = computed(() => {
    const seen = new Set();
    const colors = [];

    productVariants.value.forEach((variant) => {
      const colorId = variant.idMauSac;
      const colorName = mauSacOptions.value.find((mau) => mau.id === colorId)?.tenMau || 'N/A';

      if (!seen.has(colorId)) {
        seen.add(colorId);
        colors.push({
          colorId,
          colorName,
        });
      }
    });

    return colors;
  });

  const addVariant = () => {
    if (
      currentVariant.value.selectedRams.length === 0 ||
      currentVariant.value.selectedBoNhoTrongs.length === 0 ||
      currentVariant.value.selectedMauSacs.length === 0
    ) {
      return false;
    }

    const newVariants = [];
    currentVariant.value.selectedRams.forEach((ramId) => {
      currentVariant.value.selectedBoNhoTrongs.forEach((boNhoId) => {
        currentVariant.value.selectedMauSacs.forEach((mauSacId) => {
          // Check if the variant already exists to avoid duplicates
          const variantExists = productVariants.value.some(
            (v) =>
              v.idRam === ramId &&
              v.idBoNhoTrong === boNhoId &&
              v.idMauSac === mauSacId
          );

          if (!variantExists) {
            newVariants.push({
              idRam: ramId,
              idBoNhoTrong: boNhoId,
              idMauSac: mauSacId,
              idImel: 1,
              idLoaiTinhTrang: productData.value.idLoaiTinhTrang,
              donGia: '',
              imageIndex: null,
            });
          }
        });
      });
    });

    if (newVariants.length > 0) {
      productVariants.value.push(...newVariants);
    }

    // Do NOT reset currentVariant to keep selections
    // currentVariant.value = {
    //   selectedRams: [],
    //   selectedBoNhoTrongs: [],
    //   selectedMauSacs: [],
    // };

    // Đảm bảo không chọn bất kỳ biến thể nào sau khi thêm
    selectedVariants.value = [];
    allSelected.value = {};

    return true;
  };

  const removeVariant = (index) => {
    productVariants.value.splice(index, 1);

    // Remove corresponding IMEI data
    if (variantImeis.value[index]) {
      delete variantImeis.value[index];
    }

    // Re-index IMEI data
    const newVariantImeis = {};
    Object.keys(variantImeis.value).forEach((key) => {
      const oldIndex = parseInt(key);
      if (oldIndex > index) {
        newVariantImeis[oldIndex - 1] = variantImeis.value[key];
      } else if (oldIndex < index) {
        newVariantImeis[oldIndex] = variantImeis.value[key];
      }
    });
    variantImeis.value = newVariantImeis;

    // Reset if no variants remain
    if (productVariants.value.length === 0) {
      resetVariants();
    }
  };

  const removeMultipleVariants = () => {
    const indicesToRemove = [...selectedVariants.value].sort((a, b) => b - a);

    indicesToRemove.forEach((index) => {
      removeVariant(index);
    });

    selectedVariants.value = [];
    allSelected.value = {};
  };

  const updateSelectedVariants = (group) => {
    const groupKey = `${group.ram}/${group.rom}`;
    const commonPrice = groupCommonValues.value[groupKey]?.price || '';

    const groupIndices = group.variants.map((_, i) => group.startIndex + i);

    selectedVariants.value.forEach((index) => {
      if (groupIndices.includes(index) && commonPrice !== '') {
        productVariants.value[index].donGia = commonPrice;
      }
    });
  };

  const toggleGroupSelection = (group, isChecked) => {
    const groupIndices = group.variants.map((_, i) => group.startIndex + i);
    if (isChecked) {
      selectedVariants.value = [...new Set([...selectedVariants.value, ...groupIndices])];
    } else {
      selectedVariants.value = selectedVariants.value.filter((index) => !groupIndices.includes(index));
    }
    const groupKey = `${group.ram}/${group.rom}`;
    allSelected.value[groupKey] = isChecked;
    updateSelectedVariants(group);
  };

  const toggleAllVariants = (group, isChecked) => {
    const groupKey = `${group.ram}/${group.rom}`;
    allSelected.value[groupKey] = isChecked;
    toggleGroupSelection(group, isChecked);
  };

  const updateSelectedCount = (group) => {
    selectedVariants.value = selectedVariants.value.filter(
      (index) => index >= 0 && index < productVariants.value.length
    );
    const groupIndices = group.variants.map((_, i) => group.startIndex + i);
    const selectedInGroup = selectedVariants.value.filter((index) => groupIndices.includes(index));
    const groupKey = `${group.ram}/${group.rom}`;
    allSelected.value[groupKey] = selectedInGroup.length === group.variants.length;
    updateSelectedVariants(group);
  };

  const resetVariants = () => {
    productVariants.value = [];
    currentVariant.value = {
      selectedRams: [],
      selectedBoNhoTrongs: [],
      selectedMauSacs: [],
    };
    selectedVariants.value = [];
    allSelected.value = {};
    groupCommonValues.value = {};
    variantImeis.value = {};
  };

  return {
    productVariants,
    currentVariant,
    selectedVariants,
    allSelected,
    groupCommonValues,
    groupVariantsByRamAndRom,
    uniqueColors,
    addVariant,
    removeVariant,
    removeMultipleVariants,
    updateSelectedVariants,
    toggleGroupSelection,
    toggleAllVariants,
    updateSelectedCount,
    resetVariants,
  };
}