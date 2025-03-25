import { ref, onMounted } from 'vue'; // Không cần watch vì không dùng trong code
import axios from 'axios';

export function ThongKeJs() {
  const statistics = ref([]);
  const topProducts = ref([]);
  const filterType = ref('month'); // Mặc định lọc theo tháng
  const startDate = ref('');
  const endDate = ref('');
  const currentPage = ref(0); // Trang hiện tại
  const totalPages = ref(0); // Tổng số trang
  const pageSize = ref(10); // Số sản phẩm mỗi trang

  const formatCurrency = (value) => {
    if (!value) return '0 VND';
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
  };

  const changePage = (page) => {
    currentPage.value = page;
    fetchData();
  };

  const columns = ref([
    {
      key: 'index',
      label: '#',
      formatter: (value, item, index) => (currentPage.value * pageSize.value) + (index + 1) // Tính số thứ tự dựa trên trang
    },
    {
      key: 'imageUrl',
      label: 'Ảnh',
      formatter: (value) => value ? `<img src="${value}" alt="Ảnh" class="w-10 h-10 object-cover rounded">` : 'N/A'
    },
    {
      key: 'productName',
      label: 'Tên Sản Phẩm',
      formatter: (value) => value || 'N/A'
    },
    {
      key: 'price',
      label: 'Giá Bán',
      formatter: (value) => formatCurrency(value)
    },
    {
      key: 'soldQuantity',
      label: 'Số Lượng Đã Bán',
      formatter: (value) => value || '0'
    }
  ]);

  const fetchData = async () => {
    try {
      const params = {
        filterType: filterType.value,
        ...(filterType.value === 'custom' && {
          startDate: startDate.value,
          endDate: endDate.value,
        }),
        page: currentPage.value,
        size: pageSize.value
      };
      const response = await axios.get('http://localhost:8080/dashboard', { params });

      statistics.value = [
        { title: 'Hôm nay', revenue: response.data.ngay[0]?.doanhThu || 0, sold: response.data.ngay[0]?.sanPhamDaBan || 0, orders: response.data.ngay[0]?.tongSoDonHang || 0, bgColor: 'bg-blue-500' },
        { title: 'Tuần này', revenue: response.data.tuan[0]?.doanhThu || 0, sold: response.data.tuan[0]?.sanPhamDaBan || 0, orders: response.data.tuan[0]?.tongSoDonHang || 0, bgColor: 'bg-purple-500' },
        { title: 'Tháng này', revenue: response.data.thang[0]?.doanhThu || 0, sold: response.data.thang[0]?.sanPhamDaBan || 0, orders: response.data.thang[0]?.tongSoDonHang || 0, bgColor: 'bg-green-500' },
        { title: 'Năm nay', revenue: response.data.nam[0]?.doanhThu || 0, sold: response.data.nam[0]?.sanPhamDaBan || 0, orders: response.data.nam[0]?.tongSoDonHang || 0, bgColor: 'bg-teal-600' },
      ];

      topProducts.value = response.data.topProducts || [];
      totalPages.value = response.data.totalPages || 0; 
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  onMounted(() => {
    fetchData();
  });

  return {
    statistics,
    formatCurrency,
    topProducts,
    columns,
    filterType,
    startDate,
    endDate,
    fetchData,
    changePage,
    currentPage,
    totalPages
  };
}