import {ref, computed, onMounted, watch} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';
import {debounce} from 'lodash';

export default function useBanHang() {
  const toast = ref(null);
  const router = useRouter();
  const isProductsLoaded = ref(false);
  const currentPage = ref(0);
  const pageSize = ref(10);
  const isLoadingMore = ref(false);
  const isCreatingInvoice = ref(false);
  const isCreatingOrder = ref(false);

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

  const breadcrumbItems = computed(() => {
    return ['Bán Hàng Tại Quầy'];
  });

  const isReceiverEditable = computed(() => {
    return payOnDelivery.value;
  });

  const totalPrice = computed(() => {
    return cartItems.value.reduce((total, item) => total + (item.price || 0), 0);
  });

  // Handle province change
  const handleProvinceChange = (entityData) => {
    const province = provinces.value.find((prov) => prov.name === entityData.city);
    districts.value = province ? province.districts : [];
    entityData.district = '';
    entityData.ward = '';
    wards.value = [];
  };

  // Handle district change
  const handleDistrictChange = (entityData) => {
    const district = districts.value.find((dist) => dist.name === entityData.district);
    wards.value = district ? district.wards : [];
    entityData.ward = '';
  };

  // Calculate discount
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

  // Watch totalPrice for discount updates
  watch(totalPrice, () => {
    calculateDiscount();
  });

  // Fetch pending invoices
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

  // Fetch products
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

  // Refresh products
  const refreshProducts = () => {
    productSearchQuery.value = '';
    fetchProducts();
  };

  // Search products
  const searchProducts = debounce(() => {
    fetchProducts();
  }, 300);

  // Show IMEI list
  const showIMEIList = async (product) => {
    if (!product || !product.sanPhamId) {
      toast.value.kshowToast('error', 'Sản phẩm không hợp lệ.');
      return;
    }
    selectedProduct.value = product;
    selectedIMEIs.value = []; // Reset selectedIMEIs when opening the modal
    try {
      const response = await axios.get(`http://localhost:8080/ban-hang/san-pham/${product.sanPhamId}/imeis`, {
        params: {
          mauSac: product.mauSac,
          dungLuongRam: product.dungLuongRam,
          dungLuongBoNhoTrong: product.dungLuongBoNhoTrong,
        },
      });
      if (response.status === 200 && response.data) {
        availableIMEIs.value = response.data.map((imei) => ({imei}));
        showIMEIModal.value = true;
      }
    } catch (error) {
      console.error('Error fetching IMEIs:', error);
      toast.value.kshowToast('error', 'Không thể tải danh sách IMEI.');
    }
  };

  // Find ChiTietSanPham by IMEI
  const findChiTietSanPhamByIMEI = async (imei) => {
    try {
      const response = await axios.get(`http://localhost:8080/ban-hang/san-pham/by-imei/${imei}`);
      if (response.status === 200 && response.data) {
        return {
          id: response.data.id,
          sanPhamId: response.data.idSanPham,
          tenSanPham: response.data.tenSanPham,
          giaBan: response.data.giaBan,
          imei,
        };
      }
      return null;
    } catch (error) {
      console.error('Error finding ChiTietSanPham by IMEI:', error);
      toast.value.kshowToast('error', `Không tìm thấy chi tiết sản phẩm cho IMEI: ${imei}`);
      return null;
    }
  };

  // Add product with IMEIs to cart
  const addProductWithIMEIs = async () => {
    // Step 1: Validate inputs
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
      // Step 2: Process each selected IMEI
      for (const imei of selectedIMEIs.value) {
        // Step 2.1: Fetch product details by IMEI
        const productDetails = await findChiTietSanPhamByIMEI(imei);
        if (!productDetails) {
          toast.value.kshowToast('error', `Không tìm thấy thông tin sản phẩm cho IMEI: ${imei}`);
          continue;
        }

        // Step 2.2: Prepare data to add to cart
        const payload = {
          idChiTietSanPham: productDetails.id,
          hoaDonId: activeInvoiceId.value,
        };

        console.log(`Adding product to cart - GioHangId: ${gioHangId.value}, Payload:`, payload);

        // Step 2.3: Call API to add product to cart
        const addResponse = await axios.post(`http://localhost:8080/ban-hang/gio-hang/${gioHangId.value}/chi-tiet`, payload);

        // Step 2.4: Check response and update cart
        if (addResponse.status === 200 && addResponse.data) {
          const cartItem = {
            id: addResponse.data.id,
            name: productDetails.tenSanPham,
            price: productDetails.giaBan,
            imei: imei,
          };
          cartItems.value.push(cartItem);
          toast.value.kshowToast('success', `Đã thêm sản phẩm ${productDetails.tenSanPham} (IMEI: ${imei}) vào giỏ hàng!`);
        } else {
          toast.value.kshowToast('error', `Không thể thêm sản phẩm với IMEI ${imei} vào giỏ hàng.`);
        }
      }

      // Step 3: Update the pending invoice's items
      const invoice = pendingInvoices.value.find((inv) => inv.id === activeInvoiceId.value);
      if (invoice) {
        invoice.items = [...cartItems.value];
      }

      // Step 4: Close modals and reset states
      closeIMEIModal();
      closeProductModal();
    } catch (error) {
      console.error('Error adding product to cart:', error);
      const errorMessage = error.response?.data || error.message || 'Lỗi không xác định';
      toast.value.kshowToast('error', `Không thể thêm sản phẩm vào giỏ hàng: ${errorMessage}`);
    }
  };

  // Create new pending invoice
  const createNewPendingInvoice = async () => {
    isCreatingInvoice.value = true;
    try {
      const response = await axios.post('http://localhost:8080/ban-hang/addHD', {
        maHoaDon: `HD${Date.now()}`,
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

        // Create new cart and link to the invoice
        const gioHangResponse = await axios.post('http://localhost:8080/ban-hang/addGioHang', {
          ma: `GH${Date.now()}`,
          idKhachHang: 1, // Default customer ID
          hoaDonId: newInvoice.id, // Link to the newly created invoice
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

  // Load pending invoice
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
        // If no GioHang exists, create a new one
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

  // Remove item from cart
  const removeItem = async (item) => {
    try {
      const response = await axios.delete(`http://localhost:8080/ban-hang/gio-hang/chi-tiet/${item.id}`);
      if (response.status === 200) {
        cartItems.value = cartItems.value.filter((cartItem) => cartItem.id !== item.id);
        toast.value.kshowToast('success', 'Đã xóa sản phẩm khỏi giỏ hàng!');
      }
    } catch (error) {
      console.error('Error removing item:', error);
      toast.value.kshowToast('error', 'Không thể xóa sản phẩm.');
    }
  };

  // Open product modal
  const openProductModal = () => {
    showProductModal.value = true;
    fetchProducts();
  };

  // Close product modal
  const closeProductModal = () => {
    showProductModal.value = false;
    products.value = [];
    filteredProducts.value = [];
    productSearchQuery.value = '';
  };

  // Close IMEI modal
  const closeIMEIModal = () => {
    showIMEIModal.value = false;
    selectedProduct.value = null;
    availableIMEIs.value = [];
    selectedIMEIs.value = [];
  };

  // Select payment method
  const selectPayment = (method) => {
    paymentMethod.value = method;
  };
// In BanHang.js, replace the createOrder function with:
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
          }
          : null,
        orderNotes: orderNotes.value || '',
      };

      const response = await axios.post(`http://localhost:8080/ban-hang/thanh-toan/${activeInvoiceId.value}`, payload);
      if (response.status === 200) {
        toast.value.kshowToast('success', 'Thanh toán thành công!');

        // Store invoice ID for redirection
        const invoiceId = activeInvoiceId.value;

        // Reset state
        cartItems.value = [];
        activeInvoiceId.value = null;
        gioHangId.value = null;
        localStorage.removeItem('activeInvoiceId');
        localStorage.removeItem('gioHangId');
        selectedCustomer.value = null;
        searchCustomer.value = '';
        discountCode.value = '';
        discount.value = 0;
        orderNotes.value = '';
        paymentMethod.value = '';
        payOnDelivery.value = false;
        tienChuyenKhoan.value = 0;
        tienMat.value = 0;
        customer.value = {name: '', phone: '', city: '', district: '', ward: '', address: ''};
        receiver.value = {name: '', phone: '', city: '', district: '', ward: '', address: '', email: ''};

        // Refresh pending invoices
        await fetchPendingInvoices();

        // Redirect to invoice details
        router.push(`/show-hoa-don/${invoiceId}`);
      }
    } catch (error) {
      console.error('Error processing payment:', error);
      toast.value.kshowToast('error', 'Thanh toán thất bại: ' + (error.response?.data || error.message));
    } finally {
      isCreatingOrder.value = false;
    }
  };
  // Create order
  // const createOrder = async () => {
  //   if (!activeInvoiceId.value || cartItems.value.length === 0) {
  //     toast.value.kshowToast('error', 'Vui lòng chọn hóa đơn và thêm sản phẩm vào giỏ hàng.');
  //     return;
  //   }
  //
  //   isCreatingOrder.value = true;
  //   try {
  //     const requestBody = {
  //       totalPrice: totalPrice.value,
  //       discount: discount.value,
  //       paymentMethod: paymentMethod.value,
  //       isDelivery: payOnDelivery.value,
  //       receiver: payOnDelivery.value
  //         ? {
  //           name: receiver.value.name,
  //           phone: receiver.value.phone,
  //           city: receiver.value.city,
  //           district: receiver.value.district,
  //           ward: receiver.value.ward,
  //           address: receiver.value.address,
  //           email: receiver.value.email,
  //         }
  //         : null,
  //       tienChuyenKhoan: tienChuyenKhoan.value,
  //       tienMat: tienMat.value,
  //     };
  //
  //     const response = await axios.post(`http://localhost:8080/ban-hang/thanh-toan/${activeInvoiceId.value}`, requestBody);
  //     if (response.status === 200) {
  //       toast.value.kshowToast('success', 'Thanh toán thành công!');
  //
  //       // Lưu ID hóa đơn trước khi reset
  //       const invoiceId = activeInvoiceId.value;
  //
  //       // Reset state
  //       cartItems.value = [];
  //       activeInvoiceId.value = null;
  //       gioHangId.value = null;
  //       localStorage.removeItem('activeInvoiceId');
  //       await fetchPendingInvoices();
  //
  //       // Chuyển hướng đến màn hình chi tiết hóa đơn
  //       router.push(`/show-hoa-don/${invoiceId}`);
  //     }
  //   } catch (error) {
  //     console.error('Error processing payment:', error);
  //     toast.value.kshowToast('error', 'Thanh toán thất bại. Vui lòng thử lại.');
  //   } finally {
  //     isCreatingOrder.value = false;
  //   }
  // };

  // Apply discount
  const applyDiscount = () => {
    calculateDiscount();
  };

  // Search customers
  const searchCustomers = debounce(async () => {
    if (!searchCustomer.value.trim()) {
      selectedCustomer.value = null;
      customer.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
      receiver.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
      return;
    }

    try {
      const response = await axios.get(`http://localhost:8080/khach-hang/search?query=${encodeURIComponent(searchCustomer.value.trim())}`);
      if (response.data && response.data.length > 0) {
        const firstCustomer = response.data[0];
        selectedCustomer.value = true;
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
          city: customer.value.city || "Tỉnh Phú Thọ",
          district: customer.value.district || "Huyện Lâm Thao",
          ward: customer.value.ward || "Xã Xuân Lũng",
          address: customer.value.address || "",
        };
      } else {
        selectedCustomer.value = null;
        customer.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
        receiver.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
        if (toast.value) toast.value.kshowToast("info", "Không tìm thấy khách hàng phù hợp");
      }
    } catch (error) {
      console.error("Lỗi khi tìm kiếm khách hàng:", error);
      selectedCustomer.value = null;
      customer.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
      receiver.value = {name: "", phone: "", city: "", district: "", ward: "", address: ""};
      if (toast.value) toast.value.kshowToast("error", "Không thể tìm kiếm khách hàng: " + (error.response?.data?.error || error.message));
    }
    console.log('Searching customer:', searchCustomer.value);
  }, 300);

  // Add new customer
  const addNewCustomer = async (data) => {
    const customerData = {
      tenKH: newCustomer.name?.trim(),
      soDienThoai: newCustomer.phone,
      thanhPho: newCustomer.city,
      quan: newCustomer.district,
      phuong: newCustomer.ward,
      diaChiCuThe: newCustomer.address,
    };

    if (!customerData.tenKH || !customerData.soDienThoai || !customerData.thanhPho ||
      !customerData.quan || !customerData.phuong || !customerData.diaChiCuThe) {
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
      if (toast.value) toast.value.kshowToast("success", `Đã thêm thành công khách hàng: ${response.data.ten}`);
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

  // Scan QR
  const scanQR = () => {
    toast.value.kshowToast('info', 'Chức năng quét QR đang được phát triển.');
  };

  // Get nested value
  const getNestedValue = (obj, key) => {
    return key.split('.').reduce((o, k) => (o ? o[k] : null), obj);
  };

  // Edit item
  const editItem = (item) => {
    console.log('Editing item:', item);
  };

  // Toggle status
  const toggleStatus = (item) => {
    console.log('Toggling status:', item);
  };

  // Open customer modal
  const openCustomerModal = () => {
    isCustomerModalOpen.value = true;
  };

  // Initialize data on mount
  onMounted(() => {
    fetchPendingInvoices();
    provinces.value = [
      {name: 'Hà Nội', districts: [{name: 'Ba Đình', wards: [{name: 'Trúc Bạch'}]}]},
      {name: 'TP.HCM', districts: [{name: 'Quận 1', wards: [{name: 'Bến Nghé'}]}]},
    ];
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
    searchProducts,
    addProductWithIMEIs,
    searchCustomers,
    addNewCustomer,
    applyDiscount,
    selectPayment,
    createOrder,
    fetchProducts,
    refreshProducts,
  };
}