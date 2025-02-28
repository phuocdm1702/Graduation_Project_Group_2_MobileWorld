import { ref } from "vue";

const isOpen = ref(false)
const isOpen1 = ref(false)
const isOpen2 = ref(false)
const isOpen3 = ref(false)
const isOpen4 = ref(false)
const isOpen5 = ref(false)

export function useSidebar() {
  return {
    isOpen,
    isOpen1,
    isOpen2,
    isOpen3,
    isOpen4,
    isOpen5
  };
}
