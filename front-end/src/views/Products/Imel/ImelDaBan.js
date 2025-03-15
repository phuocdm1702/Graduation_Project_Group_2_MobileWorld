import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

export default function useImelDaBan() {
  const toast = ref(null);
  const imelDaBans = ref([]);
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);

  const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/imel-da-ban', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      imelDaBans.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Không thể tải dữ liệu!');
      }
      console.error('Fetch error:', error);
    }
  };

  const goToPage = async (page) => {
    currentPage.value = page;
    await fetchData();
  };

  onMounted(fetchData);

  return {
    toast,
    imelDaBans,
    currentPage,
    pageSize,
    totalItems,
    totalPages,
    fetchData,
    goToPage,
  };
}