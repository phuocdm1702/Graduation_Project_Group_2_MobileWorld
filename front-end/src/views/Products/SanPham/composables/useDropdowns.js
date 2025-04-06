import { ref } from 'vue';

export function useDropdowns() {
  const dropdownOpen = ref({
    ram: false,
    boNhoTrong: false,
    mauSac: false,
  });

  const toggleDropdown = (type) => {
    dropdownOpen.value[type] = !dropdownOpen.value[type];
    Object.keys(dropdownOpen.value).forEach((key) => {
      if (key !== type) {
        dropdownOpen.value[key] = false;
      }
    });
  };

  return {
    dropdownOpen,
    toggleDropdown,
  };
}