import {ref, computed, onMounted, watch} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';
import {debounce} from 'lodash';

export default function useBanHang() {
  const router = useRouter();
  const currentPage = ref(0);
  const pageSize = ref(10);
  const isLoadingMore = ref(false);
  const isCreatingInvoice = ref(false);
  const isCreatingOrder = ref(false);

  const cartItems = ref([]);
  const cartColumns = ref([
    {key: 'index', formatter: (_, __, index) => index + 1, label: 'STT'},
    {
      key: 'name',
      label: 'Sản phẩm',
      formatter: (value, row) => {
        const color = row.color || 'N/A';
        const ram = row.ram || 'N/A';
        const capacity = row.capacity || 'N/A';
        return `
        <span style="font-weight: bold; color: #1f2937;">${value}</span>
        <span > (${color} | ${ram} | ${capacity})</span>
      `;
      }
    },
    // { key: 'color', label: 'Màu', formatter: (value) => value || 'N/A' },
    // { key: 'ram', label: 'Ram', formatter: (value) => value || 'N/A' },
    // { key: 'capacity', label: 'Bộ nhớ', formatter: (value) => value || 'N/A' },
    {key: 'price', label: 'Đơn giá', formatter: (value) => `${value.toLocaleString()} đ`},
    {key: 'imei', label: 'IMEI', formatter: (value) => value || 'N/A'},
    {key: 'actions', label: 'Xóa', cellSlot: 'actionsSlot'},
  ]);

  const productColumns = ref([
    {key: 'index', formatter: (_, __, index) => index + 1, label: 'STT'},
    {key: 'tenSanPham', label: 'Tên sản phẩm'},
    {key: 'ma', label: 'Mã'},
    {key: 'mauSac', label: 'Màu', formatter: (value) => value || 'N/A'},
    {key: 'dungLuongRam', label: 'Ram', formatter: (value) => value || 'N/A'},
    {key: 'dungLuongBoNhoTrong', label: 'Bộ nhớ trong', formatter: (value) => value || 'N/A'},
    {key: 'soLuong', label: 'Số lượng', formatter: (value) => value || 0},
    {key: 'giaBan', label: 'Giá', formatter: (value) => `${value.toLocaleString()} đ`},
    {key: 'actions', label: 'Thao tác', cellSlot: 'productActionsSlot'},
  ]);

  const imeiColumns = ref([
    {key: 'imei', label: 'IMEI'},
    {key: 'actions', label: 'Chọn', cellSlot: 'imeiActionsSlot'},
  ]);

  const searchCustomer = ref('');
  const selectedCustomer = ref(null);
  const customer = ref({name: '', phone: '', city: '', district: '', ward: '', address: ''});
  const receiver = ref({
    name: '',
    phone: '',
    city: '',
    district: '',
    ward: '',
    address: '',
    email: '',
  });
  const discountCode = ref('');
  const discount = ref(0);
  const orderNotes = ref('');
  const payOnDelivery = ref(false);
  const paymentMethod = ref('');
  const activeInvoiceId = ref(null);
  const pendingInvoices = ref([]);
  const showProductModal = ref(false);
  const showIMEIModal = ref(false);
  const products = ref([]);
  const filteredProducts = ref([]);
  const productSearchQuery = ref('');
  const selectedProduct = ref(null);
  const availableIMEIs = ref([]);
  const selectedIMEIs = ref([]);
  const gioHangId = ref(null);
  const tienChuyenKhoan = ref(0);
  const tienMat = ref(0);
  const isCustomerModalOpen = ref(false);
  const isProductsLoaded = ref(false);
  const idKhachHang = ref(null);
  const toast = ref(null);
  const discountCodes = ref([]);
  const discountCodeInput = ref('');
  const isDelivery = ref(false);
  const searchedIMEI = ref(null);

  const breadcrumbItems = computed(() => {
    return ['Bán Hàng Tại Quầy'];
  });

  const isReceiverEditable = computed(() => {
    return payOnDelivery.value;
  });

  const totalPrice = computed(() => {
    return cartItems.value.reduce((total, item) => total + (item.price || 0), 0);
  });

  const fetchPendingInvoices = async () => {
    try {
      const response = await axios.get('http://localhost:8080/ban-hang/data');
      if (response.status === 200 && response.data) {
        pendingInvoices.value = response.data.map((hd) => ({
          id: hd.id,
          code: hd.maHoaDon,
          status: hd.trangThai === 0 ? 'Chờ' : 'Đã thanh toán',
          items: hd.items ? hd.items.map((item) => ({
            id: item.id,
            name: item.tenSanPham,
            color: item.mauSac,
            ram: item.ram,
            capacity: item.boNhoTrong,
            price: item.giaBan,
            imei: item.imei,
          })) : [],
        }));

        const storedInvoiceId = localStorage.getItem('activeInvoiceId');
        if (storedInvoiceId) {
          const invoice = pendingInvoices.value.find((inv) => inv.id === parseInt(storedInvoiceId));
          if (invoice) {
            loadPendingInvoice(invoice);
          }
        }
      }
    } catch (error) {
      console.error('Error fetching invoices:', error);
      toast.value.kshowToast('error', 'Không thể tải danh sách hóa đơn.');
    }
  };

  const fetchProducts = async (loadMore = false) => {
    if (!loadMore) {
      isProductsLoaded.value = false;
      currentPage.value = 0;
      products.value = [];
    }
    isLoadingMore.value = true;
    try {
      const response = await axios.get('http://localhost:8080/ban-hang/san-pham', {
        params: {
          page: currentPage.value,
          size: pageSize.value,
          keyword: productSearchQuery.value,
        },
      });
      if (response.status === 200 && response.data) {
        const newProducts = response.data.content.map((p) => ({
          id: p.id,
          sanPhamId: p.idSanPham,
          tenSanPham: p.tenSanPham,
          ma: p.ma,
          mauSac: p.mauSac || 'N/A',
          dungLuongRam: p.dungLuongRam || 'N/A',
          dungLuongBoNhoTrong: p.dungLuongBoNhoTrong || 'N/A',
          soLuong: p.soLuong || 0,
          giaBan: p.giaBan || 0,
        }));
        products.value = loadMore ? [...products.value, ...newProducts] : newProducts;
        filteredProducts.value = products.value;
        currentPage.value += 1;
        isProductsLoaded.value = true;
      }
    } catch (error) {
      console.error('Error fetching products:', error);
      toast.value.kshowToast('error', 'Không thể tải danh sách sản phẩm.');
    } finally {
      isLoadingMore.value = false;
    }
  };

  const refreshProducts = () => {
    productSearchQuery.value = '';
    fetchProducts();
  };

  const searchProductsWithIMEI = debounce(async () => {
    const query = productSearchQuery.value.trim();
    const isIMEI = /^\d{15}$/.test(query);
    if (isIMEI) {
      try {
        const response = await axios.get(`http://localhost:8080/ban-hang/san-pham/by-imei/${query}`);
        if (response.status === 200 && response.data) {
          const product = {
            id: response.data.id,
            sanPhamId: response.data.idSanPham,
            tenSanPham: response.data.tenSanPham,
            ma: response.data.ma,
            mauSac: response.data.mauSac || 'N/A',
            dungLuongRam: response.data.ram || 'N/A',
            dungLuongBoNhoTrong: response.data.boNhoTrong || 'N/A',
            soLuong: response.data.soLuong || 0,
            giaBan: response.data.giaBan || 0,
          };
          searchedIMEI.value = query;
          showIMEIList(product);
          productSearchQuery.value = '';
          return;
        } else {
          toast.value.kshowToast('error', `Không tìm thấy sản phẩm cho IMEI: ${query}`);
          fetchProducts();
        }
      } catch (error) {
        console.error('Error searching by IMEI:', error);
        toast.value.kshowToast('error', `Không tìm thấy sản phẩm cho IMEI: ${query}`);
        fetchProducts();
      }
    } else {
      fetchProducts();
    }
  }, 300);

  const showIMEIList = async (product) => {
    if (!product || !product.sanPhamId) {
      toast.value.kshowToast('error', 'Sản phẩm không hợp lệ.');
      return;
    }
    selectedProduct.value = product;
    selectedIMEIs.value = [];
    availableIMEIs.value = [];
    try {
      const response = await axios.get(`http://localhost:8080/ban-hang/san-pham/${product.sanPhamId}/imeis`, {
        params: {
          mauSac: product.mauSac !== 'N/A' ? product.mauSac : null,
          dungLuongRam: product.dungLuongRam !== 'N/A' ? product.dungLuongRam : null,
          dungLuongBoNhoTrong: product.dungLuongBoNhoTrong !== 'N/A' ? product.dungLuongBoNhoTrong : null,
        },
      });
      if (response.status === 200 && response.data) {
        availableIMEIs.value = response.data.map((imei) => ({imei}));
        if (searchedIMEI.value && !availableIMEIs.value.some((item) => item.imei === searchedIMEI.value)) {
          availableIMEIs.value.push({imei: searchedIMEI.value});
        }
        if (searchedIMEI.value) {
          selectedIMEIs.value = [searchedIMEI.value];
        }
        showIMEIModal.value = true;
      } else {
        if (searchedIMEI.value) {
          availableIMEIs.value = [{imei: searchedIMEI.value}];
          selectedIMEIs.value = [searchedIMEI.value];
          showIMEIModal.value = true;
        } else {
          toast.value.kshowToast('error', 'Không tìm thấy IMEI nào cho sản phẩm này.');
        }
      }
    } catch (error) {
      console.error('Error fetching IMEIs:', error);
      if (searchedIMEI.value) {
        availableIMEIs.value = [{imei: searchedIMEI.value}];
        selectedIMEIs.value = [searchedIMEI.value];
        showIMEIModal.value = true;
      } else {
        toast.value.kshowToast('error', 'Không thể tải danh sách IMEI.');
      }
    } finally {
      searchedIMEI.value = null;
    }
  };

  const findChiTietSanPhamByIMEI = async (imei) => {
    try {
      const response = await axios.get(`http://localhost:8080/ban-hang/san-pham/by-imei/${imei}`);
      console.log('API Response for IMEI:', imei, response.data);
      if (response.status === 200 && response.data) {
        return {
          id: response.data.id,
          sanPhamId: response.data.idSanPham,
          tenSanPham: response.data.tenSanPham,
          giaBan: response.data.giaBan,
          imei,
          mauSac: response.data.mauSac || 'N/A',
          dungLuongRam: response.data.ram || 'N/A',
          dungLuongBoNhoTrong: response.data.boNhoTrong || 'N/A',
        };
      }
      return null;
    } catch (error) {
      console.error('Error finding ChiTietSanPham by IMEI:', error);
      toast.value.kshowToast('error', `Không tìm thấy chi tiết sản phẩm cho IMEI: ${imei}`);
      return null;
    }
  };

  const addProductWithIMEIs = async () => {
    if (!selectedIMEIs.value.length) {
      toast.value.kshowToast('error', 'Vui lòng chọn ít nhất một IMEI để thêm vào giỏ hàng.');
      return;
    }
    if (!gioHangId.value) {
      toast.value.kshowToast('error', 'Giỏ hàng chưa được tạo. Vui lòng tạo hóa đơn trước.');
      return;
    }
    if (!activeInvoiceId.value) {
      toast.value.kshowToast('error', 'Hóa đơn chưa được chọn. Vui lòng chọn hoặc tạo hóa đơn mới.');
      return;
    }
    if (!selectedProduct.value) {
      toast.value.kshowToast('error', 'Không có sản phẩm nào được chọn. Vui lòng thử lại.');
      return;
    }
    try {
      for (const imei of selectedIMEIs.value) {
        const productDetails = await findChiTietSanPhamByIMEI(imei);
        if (!productDetails) {
          toast.value.kshowToast('error', `Không tìm thấy thông tin sản phẩm cho IMEI: ${imei}`);
          continue;
        }
        const payload = {
          idChiTietSanPham: productDetails.id,
          hoaDonId: activeInvoiceId.value,
        };
        console.log(`Adding product to cart - GioHangId: ${gioHangId.value}, Payload:`, payload);
        const addResponse = await axios.post(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`, payload);
        if (addResponse.status === 200 && addResponse.data) {
          const cartItem = {
            id: addResponse.data.id,
            name: productDetails.tenSanPham,
            color: productDetails.mauSac || 'N/A',
            ram: productDetails.dungLuongRam || 'N/A',
            capacity: productDetails.dungLuongBoNhoTrong || 'N/A',
            price: productDetails.giaBan,
            imei: imei,
          };
          cartItems.value.push(cartItem);
          console.log('productDetails:', productDetails);
          console.log('addResponse.data:', addResponse.data);
          toast.value.kshowToast('success', `Đã thêm sản phẩm ${productDetails.tenSanPham} (IMEI: ${imei}) vào giỏ hàng!`);
        } else {
          toast.value.kshowToast('error', `Không thể thêm sản phẩm với IMEI ${imei} vào giỏ hàng.`);
        }
      }
      const invoice = pendingInvoices.value.find((inv) => inv.id === activeInvoiceId.value);
      if (invoice) {
        invoice.items = [...cartItems.value];
      }
      closeIMEIModal();
      closeProductModal();
    } catch (error) {
      console.error('Error adding product to cart:', error);
      const errorMessage = error.response?.data || error.message || 'Lỗi không xác định';
      toast.value.kshowToast('error', `Không thể thêm sản phẩm vào giỏ hàng: ${errorMessage}`);
    }
  };

  const generateRandomCode = () => {
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    let randomCode = "";
    for (let i = 0; i < 5; i++) {
      const randomIndex = Math.floor(Math.random() * characters.length);
      randomCode += characters[randomIndex];
    }
    return `HD${randomCode}`;
  };

  const createNewPendingInvoice = async () => {
    isCreatingInvoice.value = true;
    try {
      const response = await axios.post('http://localhost:8080/ban-hang/addHD', {
        maHoaDon: generateRandomCode(),
        trangThai: 0,
      });
      if (response.status === 200 && response.data) {
        const newInvoice = {
          id: response.data.id,
          code: response.data.ma,
          status: 'Chờ',
          items: [],
        };
        pendingInvoices.value.push(newInvoice);
        activeInvoiceId.value = newInvoice.id;
        localStorage.setItem('activeInvoiceId', newInvoice.id);
        const gioHangResponse = await axios.post('http://localhost:8080/ban-hang/addGioHang', {
          ma: `GH${Date.now()}`,
          idKhachHang: 1,
          hoaDonId: newInvoice.id,
        });
        if (gioHangResponse.status === 200 && gioHangResponse.data) {
          gioHangId.value = gioHangResponse.data.id;
          console.log('New GioHang created with ID:', gioHangId.value);
        } else {
          throw new Error('Không tạo được giỏ hàng');
        }
        toast.value.kshowToast('success', 'Tạo hóa đơn mới thành công!');
      }
    } catch (error) {
      console.error('Error creating invoice:', error);
      toast.value.kshowToast('error', 'Không thể tạo hóa đơn mới: ' + error.message);
    } finally {
      isCreatingInvoice.value = false;
    }
  };

  const loadPendingInvoice = async (invoice) => {
    activeInvoiceId.value = invoice.id;
    localStorage.setItem('activeInvoiceId', invoice.id);
    cartItems.value = [...invoice.items];
    try {
      const response = await axios.get(`http://localhost:8080/ban-hang/gio-hang/by-hoa-don/${invoice.id}`);
      if (response.status === 200 && response.data) {
        gioHangId.value = response.data.id;
        console.log('Loaded GioHang ID:', gioHangId.value);
      } else {
        const gioHangResponse = await axios.post('http://localhost:8080/ban-hang/addGioHang', {
          ma: `GH${Date.now()}`,
          idKhachHang: 1,
          hoaDonId: invoice.id,
        });
        if (gioHangResponse.status === 200 && gioHangResponse.data) {
          gioHangId.value = gioHangResponse.data.id;
          console.log('Created new GioHang with ID:', gioHangId.value);
        } else {
          throw new Error('Không thể tải hoặc tạo giỏ hàng');
        }
      }
    } catch (error) {
      console.error('Error loading cart:', error);
      toast.value.kshowToast('error', 'Không thể tải giỏ hàng.');
    }
  };

  const reloadCartItems = async () => {
    if (gioHangId.value) {
      try {
        const response = await axios.get(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`);
        if (response.status === 200 && response.data) {
          cartItems.value = response.data.map(item => ({
            id: item.id,
            name: item.tenSanPham,
            color: item.mauSac || 'N/A',
            ram: item.ram || 'N/A',
            capacity: item.boNhoTrong || 'N/A',
            price: item.giaBan,
            imei: item.imei,
          }));
          // Cập nhật lại pendingInvoices
          const invoice = pendingInvoices.value.find((inv) => inv.id === activeInvoiceId.value);
          if (invoice) {
            invoice.items = [...cartItems.value];
          }
        } else {
          toast.value.kshowToast('error', 'Không thể tải danh sách sản phẩm trong giỏ hàng.');
        }
      } catch (error) {
        console.error('Error reloading cart items:', error);
        toast.value.kshowToast('error', 'Lỗi khi tải giỏ hàng: ' + (error.response?.data || error.message));
      }
    }
  };

  const removeItem = async (item) => {
    console.log('Attempting to remove item with ID:', item.id); // Thêm log để kiểm tra ID
    try {
      const response = await axios.delete(`http://localhost:8080/ban-hang/gio-hang/chi-tiet/${item.id}`);
      if (response.status === 200) {
        await reloadCartItems(); // Đồng bộ lại dữ liệu sau khi xóa thành công
        toast.value.kshowToast('success', 'Đã xóa sản phẩm khỏi giỏ hàng!');
      }
    } catch (error) {
      console.error('Error removing item:', error.response?.data || error.message);
      const errorMessage = error.response?.data || 'Không thể xóa sản phẩm khỏi giỏ hàng.';
      toast.value.kshowToast('error', errorMessage);
      // Luôn đồng bộ lại dữ liệu sau khi xóa thất bại để đảm bảo tính nhất quán
      await reloadCartItems();
    }
  };

  const openProductModal = () => {
    showProductModal.value = true;
    fetchProducts();
  };

  const closeProductModal = () => {
    showProductModal.value = false;
    products.value = [];
    filteredProducts.value = [];
    productSearchQuery.value = '';
  };

  const closeIMEIModal = () => {
    showIMEIModal.value = false;
    selectedProduct.value = null;
    availableIMEIs.value = [];
    selectedIMEIs.value = [];
  };

  const selectPayment = (method) => {
    paymentMethod.value = method;
    const finalAmount = totalPrice.value - discount.value;
    switch (method) {
      case "transfer":
        tienChuyenKhoan.value = finalAmount;
        tienMat.value = 0;
        break;
      case "cash":
        tienMat.value = finalAmount;
        tienChuyenKhoan.value = 0;
        break;
      case "both":
        tienChuyenKhoan.value = 0;
        tienMat.value = 0;
        break;
    }
    if (toast.value) toast.value.kshowToast("info", `Đã chọn phương thức thanh toán: ${method}`);
  };

  const discountCodeId = ref(null);

// Cập nhật createOrder để gửi idPhieuGiamGia
  const createOrder = async () => {
    if (!paymentMethod.value && !payOnDelivery.value) {
      toast.value.kshowToast('error', 'Vui lòng chọn phương thức thanh toán hoặc thanh toán khi nhận hàng');
      return;
    }
    if (!activeInvoiceId.value || cartItems.value.length === 0) {
      toast.value.kshowToast('error', 'Vui lòng chọn hóa đơn và thêm sản phẩm vào giỏ hàng.');
      return;
    }
    if (payOnDelivery.value) {
      if (
        !receiver.value.name ||
        !receiver.value.phone ||
        !receiver.value.city ||
        !receiver.value.district ||
        !receiver.value.ward ||
        !receiver.value.address
      ) {
        toast.value.kshowToast('error', 'Vui lòng điền đầy đủ thông tin người nhận khi chọn bán giao hàng');
        return;
      }
    }
    if (paymentMethod.value === 'both') {
      const finalAmount = totalPrice.value - discount.value;
      if (tienChuyenKhoan.value + tienMat.value !== finalAmount) {
        toast.value.kshowToast('error', 'Tổng tiền chuyển khoản và tiền mặt phải bằng tổng tiền hóa đơn!');
        return;
      }
    }
    try {
      const payload = {
        totalPrice: totalPrice.value,
        discount: discount.value,
        maGiamGia: discountCode.value || null,
        idPhieuGiamGia: discountCodeId.value || null, 
        paymentMethod: paymentMethod.value,
        tienChuyenKhoan: tienChuyenKhoan.value,
        tienMat: tienMat.value,
        isDelivery: payOnDelivery.value,
        receiver: payOnDelivery.value
          ? {
            name: receiver.value.name,
            phone: receiver.value.phone,
            city: receiver.value.city,
            district: receiver.value.district,
            ward: receiver.value.ward,
            address: receiver.value.address,
            email: receiver.value.email || null,
          }
          : null,
        customer: selectedCustomer.value
          ? {
            name: customer.value.name,
            phone: customer.value.phone,
            city: customer.value.city,
            district: customer.value.district,
            ward: customer.value.ward,
            address: customer.value.address,
            id: idKhachHang.value // Thêm idKhachHang
          }
          : null,
        orderNotes: orderNotes.value || '',
      };
      console.log("ThanhToan Payload:", payload);
      const response = await axios.post(`http://localhost:8080/ban-hang/thanh-toan/${activeInvoiceId.value}`, payload);
      if (response.status === 200) {
        toast.value.kshowToast('success', 'Thanh toán thành công!');
        const invoiceId = activeInvoiceId.value;
        cartItems.value = [];
        activeInvoiceId.value = null;
        gioHangId.value = null;
        localStorage.removeItem('activeInvoiceId');
        localStorage.removeItem('gioHangId');
        selectedCustomer.value = null;
        searchCustomer.value = '';
        discountCode.value = '';
        discount.value = 0;
        discountCodeId.value = null;
        orderNotes.value = '';
        paymentMethod.value = '';
        payOnDelivery.value = false;
        tienChuyenKhoan.value = 0;
        tienMat.value = 0;
        customer.value = { name: '', phone: '', city: '', district: '', ward: '', address: '' };
        receiver.value = { name: '', phone: '', city: '', district: '', ward: '', address: '', email: '' };
        await fetchPendingInvoices();
        router.push(`/show-hoa-don/${invoiceId}`);
      }
    } catch (error) {
      console.error('Error processing payment:', error);
      toast.value.kshowToast('error', 'Thanh toán thất bại: ' + (error.response?.data || error.message));
    } finally {
      isCreatingOrder.value = false;
    }
  };

  const searchCustomers = debounce(async () => {
    if (!searchCustomer.value.trim()) {
      selectedCustomer.value = null;
      customer.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
      receiver.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
      idKhachHang.value = null;
      discountCodes.value = [];
      discountCodeInput.value = '';
      discount.value = 0;
      discountCode.value = '';
      return;
    }
    try {
      const response = await axios.get(`http://localhost:8080/khach-hang/search?query=${encodeURIComponent(searchCustomer.value.trim())}`);
      if (response.data && response.data.length > 0) {
        const firstCustomer = response.data[0];
        selectedCustomer.value = true;
        idKhachHang.value = firstCustomer.id;
        customer.value = {
          name: firstCustomer.ten || "",
          phone: firstCustomer.idTaiKhoan?.soDienThoai || "",
          city: firstCustomer.idDiaChiKH?.thanhPho || "",
          district: firstCustomer.idDiaChiKH?.quan || "",
          ward: firstCustomer.idDiaChiKH?.phuong || "",
          address: firstCustomer.idDiaChiKH?.diaChiCuThe || "",
        };
        receiver.value = {
          name: customer.value.name,
          phone: customer.value.phone,
          city: customer.value.city || "",
          district: customer.value.district || "",
          ward: customer.value.ward || "",
          address: customer.value.address || "",
        };
        await fetchDiscountCodes();
      } else {
        selectedCustomer.value = null;
        customer.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
        receiver.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
        idKhachHang.value = null;
        discountCodes.value = [];
        discountCodeInput.value = '';
        discount.value = 0;
        discountCode.value = '';
        if (toast.value) toast.value.kshowToast("info", "Không tìm thấy khách hàng phù hợp");
      }
    } catch (error) {
      console.error("Lỗi khi tìm kiếm khách hàng:", error);
      selectedCustomer.value = null;
      customer.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
      receiver.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
      idKhachHang.value = null;
      discountCodes.value = [];
      discountCodeInput.value = '';
      discount.value = 0;
      discountCode.value = '';
      if (toast.value) toast.value.kshowToast("error", "Không thể tìm kiếm khách hàng: " + (error.response?.data?.error || error.message));
    }
  }, 300);
  

  const provinces = ref([]);
  const districts = ref([]);
  const wards = ref([]);

  const fetchProvinces = async () => {
    try {
      const response = await axios.get('https://provinces.open-api.vn/api/p/', {
        withCredentials: false,
      });
      provinces.value = response.data.map(province => ({
        name: province.name,
        code: province.code,
        districts: [],
      }));
    } catch (error) {
      console.error('Lỗi khi lấy danh sách tỉnh:', error);
      if (toast.value) toast.value.kshowToast('error', 'Không thể tải danh sách tỉnh/thành phố.');
    }
  };

  const handleProvinceChange = async (entityData) => {
    const province = provinces.value.find((prov) => prov.name === entityData.city);
    if (!province) {
      districts.value = [];
      wards.value = [];
      entityData.district = '';
      entityData.ward = '';
      return;
    }
    try {
      const response = await axios.get(`https://provinces.open-api.vn/api/p/${province.code}?depth=2`, {
        withCredentials: false,
      });
      districts.value = response.data.districts.map(district => ({
        name: district.name,
        code: district.code,
        wards: [],
      }));
      entityData.district = '';
      entityData.ward = '';
      wards.value = [];
    } catch (error) {
      console.error('Lỗi khi lấy danh sách quận/huyện:', error);
      if (toast.value) toast.value.kshowToast('error', 'Không thể tải danh sách quận/huyện.');
    }
  };

  const handleDistrictChange = async (entityData) => {
    const district = districts.value.find((dist) => dist.name === entityData.district);
    if (!district) {
      wards.value = [];
      entityData.ward = '';
      return;
    }
    try {
      const response = await axios.get(`https://provinces.open-api.vn/api/d/${district.code}?depth=2`, {
        withCredentials: false,
      });
      wards.value = response.data.wards.map(ward => ({
        name: ward.name,
        code: ward.code,
      }));
      entityData.ward = '';
    } catch (error) {
      console.error('Lỗi khi lấy danh sách phường/xã:', error);
      if (toast.value) toast.value.kshowToast('error', 'Không thể tải danh sách phường/xã.');
    }
  };

  const addNewCustomer = async (data) => {
    const selectedProvince = provinces.value.find(p => p.name === data.city);
    const selectedDistrict = districts.value.find(d => d.name === data.district);
    const selectedWard = wards.value.find(w => w.name === data.ward);
    const customerData = {
      tenKH: data.name?.trim(),
      soDienThoai: data.phone?.trim(),
      thanhPho: data.city?.trim(),
      quan: data.district?.trim(),
      phuong: data.ward?.trim(),
      diaChiCuThe: data.address?.trim(),
      provinceCode: selectedProvince?.code || null,
      districtCode: selectedDistrict?.code || null,
      wardCode: selectedWard?.code || null,
    };
    if (!customerData.tenKH || !customerData.soDienThoai || !customerData.thanhPho ||
      !customerData.quan || !customerData.phuong || !customerData.diaChiCuThe) {
      console.error('Dữ liệu không đầy đủ:', customerData);
      if (toast.value) toast.value.kshowToast('error', 'Vui lòng điền đầy đủ thông tin khách hàng.');
      return;
    }
    const phoneRegex = /^\d{10}$/;
    if (!phoneRegex.test(customerData.soDienThoai)) {
      if (toast.value) toast.value.kshowToast('error', 'Số điện thoại phải có đúng 10 chữ số.');
      return;
    }
    try {
      const response = await axios.post('http://localhost:8080/khach-hang/addBh', customerData, {
        headers: {'Content-Type': 'application/json'},
      });
      if (response.status === 200 && response.data) {
        selectedCustomer.value = true;
        idKhachHang.value = response.data.id;
        customer.value = {
          name: response.data.ten || response.data.tenKH || '',
          phone: response.data.soDienThoai || response.data.idTaiKhoan?.soDienThoai || '',
          city: response.data.idDiaChiKH?.thanhPho || '',
          district: response.data.idDiaChiKH?.quan || '',
          ward: response.data.idDiaChiKH?.phuong || '',
          address: response.data.idDiaChiKH?.diaChiCuThe || '',
        };
        if (isDelivery.value) {
          receiver.value = {
            name: customer.value.name,
            phone: customer.value.phone,
            city: customer.value.city,
            district: customer.value.district,
            ward: customer.value.ward,
            address: customer.value.address,
            email: response.data.email || '',
          };
        } else {
          resetReceiver();
        }
        await fetchDiscountCodes();
        if (toast.value) toast.value.kshowToast('success', `Đã thêm thành công khách hàng: ${customer.value.name}`);
      }
    } catch (error) {
      console.error('Lỗi khi thêm khách hàng:', error);
      let errorMessage = 'Không thể thêm khách hàng.';
      if (error.response?.status === 400 && error.response?.data?.includes('constraint [UQ__')) {
        errorMessage = 'Số điện thoại đã tồn tại. Vui lòng sử dụng số khác.';
      } else if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      } else {
        errorMessage = error.message;
      }
      if (toast.value) toast.value.kshowToast('error', errorMessage);
    }
  };

  const scanQR = () => {
    toast.value.kshowToast('info', 'Chức năng quét QR đang được phát triển.');
  };

  const getNestedValue = (obj, key) => {
    return key.split('.').reduce((o, k) => (o ? o[k] : null), obj);
  };

  const editItem = (item) => {
    console.log('Editing item:', item);
  };

  const toggleStatus = (item) => {
    console.log('Toggling status:', item);
  };

  const openCustomerModal = () => {
    isCustomerModalOpen.value = true;
  };
  const selectBestDiscountCode = async () => {
    if (!discountCodes.value.length) {
      discountCodeInput.value = '';
      discount.value = 0;
      discountCode.value = '';
      if (toast.value) toast.value.kshowToast('info', 'Không có mã giảm giá riêng nào khả dụng cho khách hàng này. Vui lòng nhập mã công khai nếu có.');
      return;
    }
    if (!totalPrice.value) {
      discountCodeInput.value = '';
      discount.value = 0;
      discountCode.value = '';
      if (toast.value) toast.value.kshowToast('info', 'Vui lòng thêm sản phẩm vào giỏ hàng để áp dụng mã giảm giá.');
      return;
    }
    let bestDiscountCode = null;
    let maxDiscountAmount = 0;
    const invalidReasons = new Set();
    for (const code of discountCodes.value) {
      if (!code.idPhieuGiamGia) {
        invalidReasons.add(`Mã ${code.ma} thiếu thông tin phiếu giảm giá.`);
        continue;
      }
      if (code.trangThai === false || code.idPhieuGiamGia.trangThai === false) {
        invalidReasons.add(`Mã ${code.ma} bị vô hiệu hóa.`);
        continue;
      }
      const ngayHetHan = new Date(code.ngayHetHan);
      if (ngayHetHan < new Date()) {
        invalidReasons.add(`Mã ${code.ma} đã hết hạn.`);
        continue;
      }
      const hoaDonToiThieu = code.idPhieuGiamGia.hoaDonToiThieu || 0;
      if (totalPrice.value < hoaDonToiThieu) {
        invalidReasons.add(`Mã ${code.ma} yêu cầu tổng tiền hóa đơn tối thiểu ${hoaDonToiThieu.toLocaleString()} đ.`);
        continue;
      }
      let discountAmount = code.idPhieuGiamGia.soTienGiamToiDa || 0;
      if (code.idPhieuGiamGia.loaiPhieuGiamGia === 'Phần trăm' && code.idPhieuGiamGia.phanTramGiamGia) {
        const calculatedDiscount = (totalPrice.value * code.idPhieuGiamGia.phanTramGiamGia) / 100;
        discountAmount = Math.min(calculatedDiscount, code.idPhieuGiamGia.soTienGiamToiDa);
      }
      if (discountAmount > maxDiscountAmount) {
        maxDiscountAmount = discountAmount;
        bestDiscountCode = code;
      }
    }
    if (bestDiscountCode) {
      discountCodeInput.value = bestDiscountCode.ma;
      await applyDiscount();
    } else {
      discountCodeInput.value = '';
      discount.value = 0;
      discountCode.value = '';
      if (invalidReasons.size > 0) {
        const reasonMessage = Array.from(invalidReasons).join(' ');
        if (toast.value) toast.value.kshowToast('warning', `Không thể áp dụng mã giảm giá riêng: ${reasonMessage}`);
      } else {
        if (toast.value) toast.value.kshowToast('info', 'Không có mã giảm giá riêng nào phù hợp. Vui lòng nhập mã công khai nếu có.');
      }
    }
  };

  const applyDiscount = async () => {
    if (!discountCodeInput.value.trim()) {
      toast.value.kshowToast('error', 'Vui lòng nhập hoặc chọn mã giảm giá.');
      return;
    }
    try {
      // Kiểm tra mã trong danh sách mã riêng của khách hàng
      const customerDiscount = discountCodes.value.find(code => code.ma === discountCodeInput.value.trim());
      console.log('Customer Discount:', customerDiscount);
      if (customerDiscount) {
        if (!customerDiscount.trangThai || !customerDiscount.idPhieuGiamGia?.trangThai) {
          toast.value.kshowToast('error', 'Mã giảm giá không hợp lệ hoặc đã bị vô hiệu hóa.');
          discount.value = 0;
          discountCode.value = '';
          return;
        }
        const ngayHetHan = new Date(customerDiscount.ngayHetHan);
        if (ngayHetHan < new Date()) {
          toast.value.kshowToast('error', 'Mã giảm giá đã hết hạn.');
          discount.value = 0;
          discountCode.value = '';
          return;
        }
        const hoaDonToiThieu = customerDiscount.idPhieuGiamGia.hoaDonToiThieu || 0;
        if (totalPrice.value < hoaDonToiThieu) {
          toast.value.kshowToast('error', `Đơn hàng phải từ ${hoaDonToiThieu.toLocaleString()} đ để áp dụng mã này.`);
          discount.value = 0;
          discountCode.value = '';
          return;
        }
        let discountAmount = customerDiscount.idPhieuGiamGia.soTienGiamToiDa || 0;
        if (customerDiscount.idPhieuGiamGia.loaiPhieuGiamGia === 'Phần trăm' && customerDiscount.idPhieuGiamGia.phanTramGiamGia) {
          const calculatedDiscount = (totalPrice.value * customerDiscount.idPhieuGiamGia.phanTramGiamGia) / 100;
          discountAmount = Math.min(calculatedDiscount, customerDiscount.idPhieuGiamGia.soTienGiamToiDa);
        }
        discount.value = discountAmount;
        discountCode.value = customerDiscount.idPhieuGiamGia.ma; // Sử dụng ma từ PhieuGiamGia
        discountCodeInput.value = customerDiscount.idPhieuGiamGia.ma;
        // Lưu idPhieuGiamGia
        discountCodeId.value = customerDiscount.idPhieuGiamGia.id; // Thêm biến mới
        toast.value.kshowToast('success', `Áp dụng mã giảm giá thành công! Giảm ${discountAmount.toLocaleString()} đ.`);
        return;
      }

      // Kiểm tra mã công khai
      const response = await axios.get(`http://localhost:8080/phieu-giam-gia/check-public?ma=${encodeURIComponent(discountCodeInput.value.trim())}`);
      if (response.status === 200 && response.data) {
        const pgg = response.data;
        if (!pgg.trangThai) {
          toast.value.kshowToast('error', 'Mã giảm giá không hợp lệ hoặc đã bị vô hiệu hóa.');
          discount.value = 0;
          discountCode.value = '';
          return;
        }
        const ngayHetHan = new Date(pgg.ngayKetThuc);
        if (ngayHetHan < new Date()) {
          toast.value.kshowToast('error', 'Mã giảm giá đã hết hạn.');
          discount.value = 0;
          discountCode.value = '';
          return;
        }
        const hoaDonToiThieu = pgg.hoaDonToiThieu || 0;
        if (totalPrice.value < hoaDonToiThieu) {
          toast.value.kshowToast('error', `Đơn hàng phải từ ${hoaDonToiThieu.toLocaleString()} đ để áp dụng mã này.`);
          discount.value = 0;
          discountCode.value = '';
          return;
        }
        let discountAmount = pgg.soTienGiamToiDa || 0;
        if (pgg.loaiPhieuGiamGia === 'Phần trăm' && pgg.phanTramGiamGia) {
          const calculatedDiscount = (totalPrice.value * pgg.phanTramGiamGia) / 100;
          discountAmount = Math.min(calculatedDiscount, pgg.soTienGiamToiDa);
        }
        discount.value = discountAmount;
        discountCode.value = discountCodeInput.value;
        discountCodeId.value = pgg.id; // Lưu id cho PGG công khai
        toast.value.kshowToast('success', `Áp dụng mã giảm giá công khai thành công! Giảm ${discountAmount.toLocaleString()} đ.`);
      } else {
        toast.value.kshowToast('error', 'Mã giảm giá không hợp lệ.');
        discount.value = 0;
        discountCode.value = '';
        discountCodeId.value = null;
      }
    } catch (error) {
      console.error('Lỗi khi áp dụng mã giảm giá:', error);
      toast.value.kshowToast('error', 'Lỗi khi áp dụng mã giảm giá: ' + (error.response?.data || error.message));
      discount.value = 0;
      discountCode.value = '';
      discountCodeId.value = null;
    }
  };

  const fetchDiscountCodes = async () => {
    if (!idKhachHang.value) {
      discountCodes.value = [];
      discountCodeInput.value = '';
      discount.value = 0;
      discountCode.value = '';
      if (toast.value) toast.value.kshowToast('info', 'Vui lòng chọn khách hàng trước khi tải mã giảm giá.');
      return;
    }
    try {
      const url = `http://localhost:8080/ban-hang/by-khach-hang/${idKhachHang.value}`;
      const response = await axios.get(url);
      if (response.status === 200 && response.data) {
        discountCodes.value = response.data.map(item => {
          console.log('API item:', item);
          return {
            id: item.id,
            ma: item.ma,
            idPhieuGiamGia: item.idPhieuGiamGia ? {
              id: item.idPhieuGiamGia.id,
              ma: item.idPhieuGiamGia.ma,
              tenPhieuGiamGia: item.idPhieuGiamGia.tenPhieuGiamGia,
              loaiPhieuGiamGia: item.idPhieuGiamGia.loaiPhieuGiamGia,
              phanTramGiamGia: item.idPhieuGiamGia.phanTramGiamGia,
              soTienGiamToiDa: item.idPhieuGiamGia.soTienGiamToiDa,
              hoaDonToiThieu: item.idPhieuGiamGia.hoaDonToiThieu,
              trangThai: item.idPhieuGiamGia.trangThai,
              riengTu: item.idPhieuGiamGia.riengTu
            } : null,
            ngayHetHan: item.ngayHetHan,
            trangThai: item.trangThai
          };
        });
        console.log('Mapped Discount Codes:', discountCodes.value);
        await selectBestDiscountCode();
      } else {
        discountCodes.value = [];
        toast.value.kshowToast('info', 'Không tìm thấy mã giảm giá cho khách hàng này.');
      }
    } catch (error) {
      console.error('Lỗi khi tải mã giảm giá:', error);
      discountCodes.value = [];
      toast.value.kshowToast('error', 'Không thể tải danh sách mã giảm giá.');
    }
  };

  const resetReceiver = () => {
    receiver.value = {
      name: '',
      phone: '',
      city: '',
      district: '',
      ward: '',
      address: '',
      email: '',
    };
  };

  const toggleDelivery = (value) => {
    isDelivery.value = value;
    payOnDelivery.value = value; // Đồng bộ payOnDelivery với isDelivery
    if (!isDelivery.value) {
      resetReceiver();
    } else {
      receiver.value = {...customer.value};
      if (!customer.value.name && !customer.value.phone) {
        toast.value.kshowToast('info', 'Vui lòng chọn hoặc thêm khách hàng để điền thông tin người nhận.');
      }
    }
  };

  const selectDiscountCode = async (ma) => {
    discountCodeInput.value = ma;
    await applyDiscount();
  };

  onMounted(() => {
    fetchPendingInvoices();
    fetchProvinces();
    resetReceiver();
  });

  return {
    discountCodes,
    selectDiscountCode,
    discountCodeInput,
    fetchDiscountCodes,
    idKhachHang,
    toast,
    breadcrumbItems,
    provinces,
    districts,
    wards,
    isCustomerModalOpen,
    openCustomerModal,
    handleProvinceChange,
    handleDistrictChange,
    cartItems,
    cartColumns,
    productColumns,
    imeiColumns,
    searchCustomer,
    selectedCustomer,
    customer,
    receiver,
    discountCode,
    discount,
    orderNotes,
    payOnDelivery,
    paymentMethod,
    activeInvoiceId,
    pendingInvoices,
    showProductModal,
    showIMEIModal,
    products,
    filteredProducts,
    productSearchQuery,
    selectedProduct,
    availableIMEIs,
    selectedIMEIs,
    gioHangId,
    tienChuyenKhoan,
    tienMat,
    isReceiverEditable,
    totalPrice,
    isProductsLoaded,
    isLoadingMore,
    currentPage,
    pageSize,
    getNestedValue,
    editItem,
    toggleStatus,
    removeItem,
    createNewPendingInvoice,
    loadPendingInvoice,
    scanQR,
    openProductModal,
    closeProductModal,
    showIMEIList,
    closeIMEIModal,
    searchProductsWithIMEI,
    addProductWithIMEIs,
    searchCustomers,
    addNewCustomer,
    applyDiscount,
    selectPayment,
    createOrder,
    fetchProducts,
    refreshProducts,
    isDelivery,
    toggleDelivery,
  };
}