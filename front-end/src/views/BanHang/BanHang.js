import {ref, computed, onMounted, watch} from 'vue';
import axios from 'axios';

export default function useBanHang() {
  const toast = ref(null);

  const provinces = ref([]);
  const districts = ref([]);
  const wards = ref([]);

  const cartItems = ref([]);
  const cartColumns = ref([
    {key: 'index', formatter: (_, __, index) => index + 1, label: 'STT'},
    {key: 'name', label: 'Sản phẩm'},
    {key: 'price', label: 'Đơn giá', formatter: (value) => `${value.toLocaleString()} đ`},
    {key: 'imei', label: 'IMEI', formatter: (value) => value || 'N/A'},
    {key: 'actions', label: 'Xóa', cellSlot: 'actionsSlot'},
  ]);

  const productColumns = ref([
    {key: 'index', formatter: (_, __, index) => index + 1, label: 'STT'},
    {key: 'tenSanPham', label: 'Tên sản phẩm'},
    {key: 'ma', label: 'Mã'},
    {key: 'mauSac', label: 'Màu', formatter: (value) => value || 'N/A'},
    {key: 'soLuong', label: 'Số lượng', formatter: (value) => value || 0},
    {key: 'giaBan', label: 'Giá', formatter: (value) => `${value.toLocaleString()} đ`},
    {key: 'actions', label: 'Thao tác', cellSlot: 'productActionsSlot'},
  ]);

  const imeiColumns = ref([
    {key: 'imei', label: 'IMEI'},
    {key: 'imei', label: 'IMEI2'},
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

  const breadcrumbItems = computed(() => {
    const route = window.location.pathname;
    return ['Bán Hàng Tại Quầy'];
  });

  const isReceiverEditable = computed(() => {
    return payOnDelivery.value;
  });

  const totalPrice = computed(() => {
    return cartItems.value.reduce((total, item) => total + item.price, 0);
  });

  const handleProvinceChange = (entityData) => {
    const province = provinces.value.find((prov) => prov.name === entityData.city);
    districts.value = province ? province.districts : [];
    entityData.district = '';
    entityData.ward = '';
    wards.value = [];
  };

  const handleDistrictChange = (entityData) => {
    const district = districts.value.find((dist) => dist.name === entityData.district);
    wards.value = district ? district.wards : [];
    entityData.ward = '';
  };

  const calculateDiscount = () => {
    const total = totalPrice.value;
    let newDiscount = 0;

    if (total > 40000000) {
      newDiscount = 1500000;
      if (toast.value && discount.value !== newDiscount) {
        toast.value.kshowToast('success', 'Đã áp dụng giảm giá 1.500.000 đ cho hóa đơn trên 40 triệu!');
      }
    } else if (total > 30000000) {
      newDiscount = 1000000;
      if (toast.value && discount.value !== newDiscount) {
        toast.value.kshowToast('success', 'Đã áp dụng giảm giá 1.000.000 đ cho hóa đơn trên 30 triệu!');
      }
    } else if (total > 15000000) {
      newDiscount = 500000;
      if (toast.value && discount.value !== newDiscount) {
        toast.value.kshowToast('success', 'Đã áp dụng giảm giá 500.000 đ cho hóa đơn trên 15 triệu!');
      }
    } else {
      newDiscount = 0;
      if (toast.value && discount.value !== 0) {
        toast.value.kshowToast('info', 'Hóa đơn dưới 15 triệu, không áp dụng giảm giá.');
      }
    }

    discount.value = newDiscount;
  };

  watch(totalPrice, () => {
    calculateDiscount();
  });

  const fetchPendingInvoices = async () => {
    try {
      const response = await axios.get('http://localhost:8080/ban-hang/data');
      pendingInvoices.value = response.data.map((hd) => ({
        id: hd.id,
        code: hd.maHoaDon,
        status: hd.trangThai === 0 ? 'Chờ' : 'Đã thanh toán',
        items: hd.items.map((item) => ({
          id: item.id,
          name: item.tenSanPham,
          price: item.giaBan,
          imei: item.imei,
        })),
      }));

      const storedInvoiceId = localStorage.getItem('activeInvoiceId');
      if (storedInvoiceId) {
        activeInvoiceId.value = parseInt(storedInvoiceId);
        const selectedInvoice = pendingInvoices.value.find((i) => i.id === activeInvoiceId.value);
        if (selectedInvoice) {
          await loadPendingInvoice(selectedInvoice);
        } else {
          localStorage.removeItem('activeInvoiceId');
          activeInvoiceId.value = null;
        }
      }
    } catch (error) {
      console.error('Lỗi khi lấy danh sách hóa đơn:', error);
      if (toast.value) toast.value.kshowToast('error', 'Không thể tải danh sách hóa đơn!');
    }
  };

  const fetchProducts = async () => {
    try {
      const response = await axios.get('http://localhost:8080/ban-hang/san-pham', {
        params: {
          keyword: productSearchQuery.value,
          page: 0,
          size: 10,
        },
      });

      products.value = await Promise.all(response.data.map(async (p) => {
        // Gọi API để lấy danh sách IMEI cho sản phẩm
        const imeiResponse = await axios.get(`http://localhost:8080/ban-hang/san-pham/${p.id}/imeis`);
        return {
          id: p.id,
          tenSanPham: p.tenSanPham,
          ma: p.ma,
          mauSac: p.mauSac || 'N/A',
          soLuong: imeiResponse.data.length, // Tính số lượng dựa trên độ dài danh sách IMEI
          giaBan: p.giaBan,
        };
      }));
      filteredProducts.value = products.value;
    } catch (error) {
      console.error('Lỗi khi lấy danh sách sản phẩm:', error);
      if (toast.value) toast.value.kshowToast('error', 'Không thể tải danh sách sản phẩm!');
    }
  };

  // const fetchProducts = async () => {
  //   try {
  //     const response = await axios.get('http://localhost:8080/ban-hang/san-pham', {
  //       params: {
  //         keyword: productSearchQuery.value,
  //         page: 0,
  //         size: 10,
  //       },
  //     });
  //
  //     products.value = response.data.map((p) => ({
  //      
  //       id: p.id,
  //       tenSanPham: p.tenSanPham,
  //       ma: p.ma,
  //       mauSac: p.mauSac || 'N/A',
  //       soLuong: 0, // Backend không trả về số lượng IMEI, sẽ lấy khi cần
  //       giaBan: p.giaBan,
  //     }));
  //     filteredProducts.value = products.value;
  //   } catch (error) {
  //     console.error('Lỗi khi lấy danh sách sản phẩm:', error);
  //     if (toast.value) toast.value.kshowToast('error', 'Không thể tải danh sách sản phẩm!');
  //   }
  // };

  const fetchCartItems = async () => {
    const storedGioHangId = localStorage.getItem('gioHangId');
    if (storedGioHangId) {
      gioHangId.value = parseInt(storedGioHangId);
      try {
        const response = await axios.get(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`);
        cartItems.value = response.data.map((item) => ({
          id: item.id,
          name: item.tenSanPham,
          price: item.giaBan,
          imei: item.imei,
        }));
        calculateDiscount();
      } catch (error) {
        console.error('Lỗi khi lấy danh sách chi tiết giỏ hàng:', error);
        if (toast.value) toast.value.kshowToast('error', 'Không thể tải giỏ hàng!');
      }
    }
  };

  const getNestedValue = (item, key) => {
    if (key === 'imei') return item.imei || 'N/A';
    const keys = key.split('.');
    let value = item;
    for (const k of keys) {
      value = value && value[k] !== undefined ? value[k] : null;
      if (value === null) break;
    }
    return value !== null ? value : 'N/A';
  };

  const editItem = (item) => {
    if (toast.value) toast.value.kshowToast('info', `Chỉnh sửa sản phẩm: ${item.name}`);
  };

  const toggleStatus = (item) => {
    if (toast.value) toast.value.kshowToast('success', `Đã thay đổi trạng thái sản phẩm: ${item.name}`);
  };

  const removeItem = async (item) => {
    try {
      await axios.delete(`http://localhost:8080/ban-hang/gio-hang/chi-tiet/${item.id}`);
      cartItems.value = cartItems.value.filter((i) => i.id !== item.id);
      const invoice = pendingInvoices.value.find((i) => i.id === activeInvoiceId.value);
      if (invoice) {
        invoice.items = invoice.items.filter((i) => i.id !== item.id);
      }
      if (toast.value) toast.value.kshowToast('success', `Đã xóa sản phẩm: ${item.name}`);
      calculateDiscount();
    } catch (error) {
      console.error('Lỗi khi xóa sản phẩm:', error);
      if (toast.value) toast.value.kshowToast('error', 'Không thể xóa sản phẩm!');
    }
  };

  const generateRandomCode = () => {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    let randomCode = '';
    for (let i = 0; i < 5; i++) {
      const randomIndex = Math.floor(Math.random() * characters.length);
      randomCode += characters[randomIndex];
    }
    return `HD${randomCode}`;
  };

  const createNewPendingInvoice = async () => {
    const newInvoice = {
      maHoaDon: generateRandomCode(),
      trangThai: 0,
    };

    try {
      const gioHangResponse = await axios.post('http://localhost:8080/ban-hang/addGioHang', {
        ma: `GH_${newInvoice.maHoaDon}`,
        idKhachHang: 1,
      });
      gioHangId.value = gioHangResponse.data.id;
      localStorage.setItem('gioHangId', gioHangId.value);

      const response = await axios.post('http://localhost:8080/ban-hang/addHD', newInvoice);
      pendingInvoices.value.unshift({
        id: response.data.id,
        code: response.data.ma,
        status: response.data.trangThai === 0 ? 'Chờ' : 'Đã thanh toán',
        items: [],
      });
      await loadPendingInvoice(pendingInvoices.value[0]);
      if (toast.value) toast.value.kshowToast('success', `Đã tạo hóa đơn chờ mới: ${response.data.ma}`);
    } catch (error) {
      console.error('Lỗi khi tạo hóa đơn mới:', error);
      if (toast.value) toast.value.kshowToast('error', 'Không thể tạo hóa đơn mới!');
    }
  };

  const loadPendingInvoice = async (invoice) => {
    activeInvoiceId.value = invoice.id;
    localStorage.setItem('activeInvoiceId', invoice.id);

    if (!gioHangId.value) {
      const gioHangResponse = await axios.post('http://localhost:8080/ban-hang/addGioHang', {
        ma: `GH_${invoice.code}`,
        idKhachHang: 1,
      });
      gioHangId.value = gioHangResponse.data.id;
      localStorage.setItem('gioHangId', gioHangId.value);
    }

    try {
      const response = await axios.get(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`);
      cartItems.value = response.data.map((item) => ({
        id: item.id,
        name: item.tenSanPham,
        price: item.giaBan,
        imei: item.imei,
      }));
      const invoiceToUpdate = pendingInvoices.value.find((i) => i.id === invoice.id);
      if (invoiceToUpdate) {
        invoiceToUpdate.items = [...cartItems.value];
      }
      calculateDiscount();
    } catch (error) {
      console.error('Lỗi khi tải sản phẩm của hóa đơn:', error);
      cartItems.value = [];
    }

    if (toast.value) toast.value.kshowToast('info', `Đã tải hóa đơn chờ: ${invoice.code}`);
  };

  const scanQR = () => {
    if (toast.value) toast.value.kshowToast('info', 'Đang quét QR...');
  };

  const openProductModal = async () => {
    await fetchProducts();
    showProductModal.value = true;
    productSearchQuery.value = '';
  };

  const closeProductModal = () => {
    showProductModal.value = false;
  };

  const showIMEIList = async (product) => {
    selectedProduct.value = product;
    try {
      const response = await axios.get(`http://localhost:8080/ban-hang/san-pham/${product.id}/imeis`);
      availableIMEIs.value = response.data.map(imei => ({imei}));
      selectedIMEIs.value = [];
      showIMEIModal.value = true;
    } catch (error) {
      console.error('Lỗi khi lấy danh sách IMEI:', error);
      if (toast.value) toast.value.kshowToast('error', 'Không thể tải danh sách IMEI!');
    }
  };

  const closeIMEIModal = () => {
    showIMEIModal.value = false;
    selectedProduct.value = null;
    availableIMEIs.value = [];
    selectedIMEIs.value = [];
  };

  // const searchProducts = () => {
  //   if (!productSearchQuery.value) {
  //     filteredProducts.value = products.value;
  //   } else {
  //     filteredProducts.value = products.value.filter(
  //       (product) =>
  //         product.tenSanPham.toLowerCase().includes(productSearchQuery.value.toLowerCase()) ||
  //         product.ma.toLowerCase().includes(productSearchQuery.value.toLowerCase())
  //     );
  //   }
  // };
  const searchProducts = async () => {
    await fetchProducts(); // Gọi lại API với keyword mới
  };

  const addProductWithIMEIs = async () => {
    if (!activeInvoiceId.value) {
      if (toast.value) toast.value.kshowToast('error', 'Vui lòng chọn hoặc tạo hóa đơn trước!');
      return;
    }

    if (selectedIMEIs.value.length === 0) {
      if (toast.value) toast.value.kshowToast('error', 'Vui lòng chọn ít nhất một IMEI!');
      return;
    }

    try {
      if (!gioHangId.value) {
        const gioHangResponse = await axios.post('http://localhost:8080/ban-hang/addGioHang', {
          ma: generateRandomCode(),
          idKhachHang: 1,
        });
        gioHangId.value = gioHangResponse.data.id;
        localStorage.setItem('gioHangId', gioHangId.value);
      }

      for (const imei of selectedIMEIs.value) {
        const chiTietSanPhamResponse = await axios.get(`http://localhost:8080/ban-hang/san-pham?imei=${imei}`);
        const chiTietSanPham = chiTietSanPhamResponse.data.find((p) => p.imei === imei);
        if (!chiTietSanPham) throw new Error('Không tìm thấy sản phẩm với IMEI: ' + imei);

        const response = await axios.post(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`, {
          idChiTietSanPham: chiTietSanPham.id,
          hoaDonId: activeInvoiceId.value,
        });

        const newItem = {
          id: response.data.id,
          name: response.data.tenSanPham,
          price: response.data.giaBan,
          imei: response.data.imei,
        };
        cartItems.value.push(newItem);
        const invoice = pendingInvoices.value.find((i) => i.id === activeInvoiceId.value);
        if (invoice) invoice.items.push(newItem);
      }

      if (toast.value)
        toast.value.kshowToast('success', `Đã thêm ${selectedIMEIs.value.length} sản phẩm: ${selectedProduct.value.tenSanPham}`);
      closeIMEIModal();
      closeProductModal();
      calculateDiscount();
    } catch (error) {
      console.error('Lỗi khi thêm sản phẩm:', error);
      if (toast.value) toast.value.kshowToast('error', 'Không thể thêm sản phẩm: ' + error.message);
    }
  };

  const searchCustomers = async () => {
    if (!searchCustomer.value.trim()) {
      selectedCustomer.value = null;
      customer.value = {name: '', phone: '', city: '', district: '', ward: '', address: ''};
      receiver.value = {name: '', phone: '', city: '', district: '', ward: '', address: ''};
      return;
    }

    try {
      const response = await axios.get(
        `http://localhost:8080/khach-hang/search?query=${encodeURIComponent(searchCustomer.value.trim())}`
      );
      if (response.data && response.data.length > 0) {
        const firstCustomer = response.data[0];
        selectedCustomer.value = true;
        customer.value = {
          name: firstCustomer.ten || '',
          phone: firstCustomer.idTaiKhoan?.soDienThoai || '',
          city: firstCustomer.idDiaChiKH?.thanhPho || '',
          district: firstCustomer.idDiaChiKH?.quan || '',
          ward: firstCustomer.idDiaChiKH?.phuong || '',
          address: firstCustomer.idDiaChiKH?.diaChiCuThe || '',
        };
        receiver.value = {
          name: customer.value.name,
          phone: customer.value.phone,
          city: customer.value.city || 'Tỉnh Phú Thọ',
          district: customer.value.district || 'Huyện Lâm Thao',
          ward: customer.value.ward || 'Xã Xuân Lũng',
          address: customer.value.address || '',
        };
      } else {
        selectedCustomer.value = null;
        customer.value = {name: '', phone: '', city: '', district: '', ward: '', address: ''};
        receiver.value = {name: '', phone: '', city: '', district: '', ward: '', address: ''};
        if (toast.value) toast.value.kshowToast('info', 'Không tìm thấy khách hàng phù hợp');
      }
    } catch (error) {
      console.error('Lỗi khi tìm kiếm khách hàng:', error);
      selectedCustomer.value = null;
      customer.value = {name: '', phone: '', city: '', district: '', ward: '', address: ''};
      receiver.value = {name: '', phone: '', city: '', district: '', ward: '', address: ''};
      if (toast.value)
        toast.value.kshowToast('error', 'Không thể tìm kiếm khách hàng: ' + (error.response?.data?.error || error.message));
    }
  };

  const addNewCustomer = async (newCustomer) => {
    const customerData = {
      tenKH: newCustomer.name?.trim(),
      soDienThoai: newCustomer.phone,
      thanhPho: newCustomer.city,
      quan: newCustomer.district,
      phuong: newCustomer.ward,
      diaChiCuThe: newCustomer.address,
    };

    if (
      !customerData.tenKH ||
      !customerData.soDienThoai ||
      !customerData.thanhPho ||
      !customerData.quan ||
      !customerData.phuong ||
      !customerData.diaChiCuThe
    ) {
      console.error('Dữ liệu không đầy đủ:', customerData);
      if (toast.value) toast.value.kshowToast('error', 'Vui lòng điền đầy đủ thông tin khách hàng');
      return;
    }

    try {
      const response = await axios.post('http://localhost:8080/khach-hang/addBh', customerData, {
        headers: {'Content-Type': 'application/json'},
      });
      selectedCustomer.value = true;
      customer.value = {
        name: response.data.ten,
        phone: response.data.soDienThoai || response.data.idTaiKhoan?.soDienThoai || '',
        city: response.data.idDiaChiKH?.thanhPho,
        district: response.data.idDiaChiKH?.quan,
        ward: response.data.idDiaChiKH?.phuong,
        address: response.data.idDiaChiKH?.diaChiCuThe,
      };
      if (toast.value) toast.value.kshowToast('success', `Đã thêm thành công khách hàng: ${response.data.ten}`);
    } catch (error) {
      console.error('Lỗi khi thêm khách hàng:', error);
      let errorMessage = 'Không thể thêm khách hàng';
      if (error.response?.status === 400 && error.response?.data?.includes('constraint [UQ__')) {
        errorMessage = 'Số điện thoại đã tồn tại. Vui lòng sử dụng số khác.';
      } else {
        errorMessage = error.response?.data || error.message;
      }
      if (toast.value) toast.value.kshowToast('error', errorMessage);
      return;
    }
  };

  const applyDiscount = () => {
    if (discountCode.value === 'MungQuocKhanh') {
      discount.value = 500000;
      if (toast.value) toast.value.kshowToast('success', 'Áp dụng mã giảm giá thành công!');
    } else {
      if (toast.value) toast.value.kshowToast('error', 'Mã giảm giá không hợp lệ!');
    }
  };

  const selectPayment = (method) => {
    paymentMethod.value = method;
    const finalAmount = totalPrice.value - discount.value;
    switch (method) {
      case 'transfer':
        tienChuyenKhoan.value = finalAmount;
        tienMat.value = 0;
        break;
      case 'cash':
        tienMat.value = finalAmount;
        tienChuyenKhoan.value = 0;
        break;
      case 'both':
        tienChuyenKhoan.value = 0;
        tienMat.value = 0;
        break;
    }
    if (toast.value) toast.value.kshowToast('info', `Đã chọn phương thức thanh toán: ${method}`);
  };

  const createOrder = async () => {
    if (!paymentMethod.value && !payOnDelivery.value) {
      if (toast.value)
        toast.value.kshowToast('error', 'Vui lòng chọn phương thức thanh toán hoặc thanh toán khi nhận hàng');
      return;
    }

    if (!activeInvoiceId.value) {
      if (toast.value) toast.value.kshowToast('error', 'Vui lòng chọn hoặc tạo hóa đơn trước!');
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
        if (toast.value)
          toast.value.kshowToast('error', 'Vui lòng điền đầy đủ thông tin người nhận khi chọn bán giao hàng');
        return;
      }
    }

    if (paymentMethod.value === 'both') {
      const finalAmount = totalPrice.value - discount.value;
      if (tienChuyenKhoan.value + tienMat.value !== finalAmount) {
        if (toast.value)
          toast.value.kshowToast('error', 'Tổng tiền chuyển khoản và tiền mặt phải bằng tổng tiền hóa đơn!');
        return;
      }
    }

    const payload = {
      totalPrice: totalPrice.value,
      discount: discount.value,
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
    };

    try {
      await axios.post(`http://localhost:8080/ban-hang/thanh-toan/${activeInvoiceId.value}`, payload);
      pendingInvoices.value = pendingInvoices.value.filter((i) => i.id !== activeInvoiceId.value);
      activeInvoiceId.value = null;
      localStorage.removeItem('activeInvoiceId');
      localStorage.removeItem('gioHangId');
      gioHangId.value = null;
      cartItems.value = [];
      selectedCustomer.value = null;
      searchCustomer.value = '';
      discountCode.value = '';
      discount.value = 0;
      orderNotes.value = '';
      paymentMethod.value = '';
      payOnDelivery.value = false;
      tienChuyenKhoan.value = 0;
      tienMat.value = 0;
      receiver.value = {name: '', phone: '', city: '', district: '', ward: '', address: '', email: ''};
      if (toast.value) toast.value.kshowToast('success', 'Thanh toán thành công!');
    } catch (error) {
      console.error('Lỗi khi thanh toán:', error);
      if (toast.value)
        toast.value.kshowToast('error', 'Thanh toán thất bại: ' + (error.response?.data || error.message));
    }
  };

  const openCustomerModal = () => {
    isCustomerModalOpen.value = true;
  };

  onMounted(async () => {
    try {
      const response = await axios.get('https://provinces.open-api.vn/api/?depth=3', {
        withCredentials: false,
      });
      provinces.value = response.data;
    } catch (error) {
      console.error('Lỗi khi tải dữ liệu địa chỉ:', error);
      if (toast.value)
        toast.value.kshowToast('error', 'Không thể tải danh sách tỉnh/thành phố: ' + (error.response?.data?.error || error.message));
    }
    fetchPendingInvoices();
    fetchCartItems();
  });

  return {
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
    searchProducts,
    addProductWithIMEIs,
    searchCustomers,
    addNewCustomer,
    applyDiscount,
    selectPayment,
    createOrder,
  };
}