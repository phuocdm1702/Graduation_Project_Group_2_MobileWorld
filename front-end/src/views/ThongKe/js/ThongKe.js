import { ref, onMounted } from 'vue';
import axios from 'axios';
import Chart from 'chart.js/auto';
import ChartDataLabels from 'chartjs-plugin-datalabels';

Chart.register(ChartDataLabels);

export function ThongKeJs() {
  const statistics = ref([]);
  const topProducts = ref([]);
  const growthData = ref({});
  const orderStatusStats = ref({});
  const filterType = ref('month');
  const startDate = ref('');
  const endDate = ref('');
  const chartFilterType = ref('month');
  const currentPage = ref(0);
  const totalPages = ref(0);
  const pageSize = ref(10);
  let orderStatusChart = null;

  const formatCurrency = (value) => {
    if (!value) return '0 đ';
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
  };

  const formatGrowth = (value) => {
    if (value > 0) return `<i class="fas fa-arrow-up text-green-500"></i> ${value.toFixed(1)}%`;
    if (value < 0) return `<i class="fas fa-arrow-down text-red-500"></i> ${Math.abs(value).toFixed(1)}%`;
    return `<i class="fas fa-arrow-down text-red-500"></i> 0%`;
  };

  const changePage = (page) => {
    currentPage.value = page;
    fetchData();
  };

  const columnsTopProducts = ref([
    { key: 'index', label: '#', formatter: (value, item, index) => (currentPage.value * pageSize.value) + (index + 1) },
    { key: 'imageUrl', label: 'Ảnh', formatter: (value) => value ? `<img src="${value}" alt="Ảnh" class="w-10 h-10 object-cover rounded">` : 'N/A' },
    { key: 'productName', label: 'Tên Sản Phẩm', formatter: (value) => value || 'N/A' },
    { key: 'price', label: 'Giá Bán', formatter: (value) => formatCurrency(value) },
    { key: 'soldQuantity', label: 'Số Lượng Đã Bán', formatter: (value) => value || '0' }
  ]);

  const fetchData = async () => {
    try {
      const params = {
        filterType: filterType.value,
        ...(filterType.value === 'custom' && { startDate: startDate.value, endDate: endDate.value }),
        page: currentPage.value,
        size: pageSize.value
      };
      const response = await axios.get('http://localhost:8080/dashboard/dashboard', { params });

      statistics.value = [
        { title: 'Hôm nay', revenue: response.data.ngay[0]?.doanhThu || 0, sold: response.data.ngay[0]?.sanPhamDaBan || 0, orders: response.data.ngay[0]?.tongSoDonHang || 0, bgColor: 'bg-blue-500' },
        { title: 'Tuần này', revenue: response.data.tuan[0]?.doanhThu || 0, sold: response.data.tuan[0]?.sanPhamDaBan || 0, orders: response.data.tuan[0]?.tongSoDonHang || 0, bgColor: 'bg-purple-500' },
        { title: 'Tháng này', revenue: response.data.thang[0]?.doanhThu || 0, sold: response.data.thang[0]?.sanPhamDaBan || 0, orders: response.data.thang[0]?.tongSoDonHang || 0, bgColor: 'bg-green-500' },
        { title: 'Năm nay', revenue: response.data.nam[0]?.doanhThu || 0, sold: response.data.nam[0]?.sanPhamDaBan || 0, orders: response.data.nam[0]?.tongSoDonHang || 0, bgColor: 'bg-teal-600' },
      ];

      topProducts.value = response.data.topProducts || [];
      totalPages.value = response.data.totalPages || 0;
      growthData.value = response.data.growthData || {};
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const fetchOrderStatusStats = async () => {
    try {
      const params = { filterType: chartFilterType.value };
      const response = await axios.get('http://localhost:8080/dashboard/order-status-stats', { params });
      orderStatusStats.value = response.data;
      updateOrderStatusChart();
    } catch (error) {
      console.error('Error fetching order status stats:', error);
    }
  };

  const updateOrderStatusChart = () => {
    const ctx = document.getElementById('orderStatusChart')?.getContext('2d');
    if (!ctx) return;

    if (orderStatusChart) {
      orderStatusChart.destroy();
    }

    orderStatusChart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['Chờ xác nhận', 'Chờ giao hàng', 'Đang giao', 'Hoàn thành', 'Đã hủy'],
        datasets: [{
          data: [
            orderStatusStats.value['Chờ xác nhận'] || 0,
            orderStatusStats.value['Chờ giao hàng'] || 0,
            orderStatusStats.value['Đang giao'] || 0,
            orderStatusStats.value['Hoàn thành'] || 0,
            orderStatusStats.value['Đã hủy'] || 0
          ],
          backgroundColor: [
            '#FF6384', // Chờ xác nhận (hồng)
            '#FFCE56', // Chờ giao hàng (vàng)
            '#4BC0C0', // Đang giao (xanh lam nhạt)
            '#9966FF', // Hoàn thành (tím)
            '#FF4444'  // Đã hủy (đỏ)
          ],
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              boxWidth: 20,
              padding: 15
            }
          },
          datalabels: {
            color: '#fff',
            formatter: (value) => value > 0 ? value : '',
            font: {
              weight: 'bold'
            }
          }
        }
      }
    });
  };

  onMounted(() => {
    fetchData();
    fetchOrderStatusStats();
  });

  return {
    statistics,
    formatCurrency,
    formatGrowth,
    topProducts,
    columnsTopProducts,
    growthData,
    orderStatusStats,
    filterType,
    startDate,
    endDate,
    chartFilterType,
    fetchData,
    fetchOrderStatusStats,
    changePage,
    currentPage,
    totalPages
  };
}