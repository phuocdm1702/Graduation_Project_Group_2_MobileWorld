import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { saveAs } from "file-saver";
import jsQR from "jsqr";
import QRCode from "qrcode";

export default function useHoaDonLineList() {
  // Router
  const router = useRouter();

  // State
  const toast = ref(null);
  const dataTable = ref([]);
  const originalData = ref([]);
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalElements = ref(0);
  const totalPages = computed(() => Math.ceil(totalElements.value / pageSize.value));
  const orderTypes = ref([]);

  // Filter State
  const keyword = ref("");
  const minAmount = ref(0);
  const maxAmount = ref("10000000");
  const minRange = ref(0);
  const maxRange = ref(10000000);
  const selectedOrderType = ref("");
  const startDate = ref("");
  const endDate = ref("");
  const selectedStatus = ref(null);
  const isFiltering = ref(false);

  // Modal State
  const showConfirmModal = ref(false);
  const confirmMessage = ref("");
  const confirmedAction = ref(null);

  // QR Scanner State
  const showQrScanner = ref(false);
  const qrResult = ref(null);
  const videoElement = ref(null);
  const canvasElement = ref(null);
  let stream = null;
  let animationFrameId = null;

  // Table Columns Configuration
  const columns = ref([
    { key: "index", label: "#", formatter: (_, __, index) => currentPage.value * pageSize.value + index + 1 },
    { key: "ma", label: "Mã" },
    { key: "idNhanVien.ma", label: "Nhân viên" },
    { key: "tenKhachHang", label: "Khách hàng" },
    { key: "soDienThoaiKhachHang", label: "SDT" },
    {
      key: "tongTienSauGiam",
      label: "Tổng giá trị",
      formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
    },
    {
      key: "idPhieuGiamGia.phanTramGiamGia",
      label: "Tiền giảm",
      formatter: (phanTramGiamGia, item) => {
        if (!phanTramGiamGia || !item?.tongTien) return "0 VND";
        const giamGia = Math.round((item.tongTien * phanTramGiamGia) / 100 / 1000) * 1000;
        return `(${phanTramGiamGia}%) ~ ${giamGia.toLocaleString()}đ`;
      },
    },
    {
      key: "phiVanChuyen",
      label: "Phí",
      formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
    },
    {
      key: "ngayTao",
      label: "TG tạo",
      formatter: (value) => (value ? new Date(value).toLocaleDateString() : "N/A"),
    },
    { key: "loaiDon", label: "Loại Đơn" },
    { key: "trangThaiFormatted", label: "Trạng thái", formatter: (value) => value },
    {
      key: "actions",
      label: "Thao tác",
      formatter: (_, item) => `
        <button class="text-blue-500 hover:text-blue-700 mr-2" onclick="window.viewUpdate(${item.id})">
          <i class="fa-solid fa-edit" style="color: #f97316;"></i>
        </button>
        <button class="text-blue-500 hover:text-blue-700 mr-2" onclick="window.printInvoice(${item.id})">
          <i class="fa-solid fa-print" style="color: #f97316;"></i>
        </button>
        <button class="text-blue-500 hover:text-blue-700" onclick="window.downloadQrCode('${item.ma}')">
          <i class="fa-solid fa-qrcode" style="color: #f97316;"></i>
        </button>
      `,
    },
  ]);

  // Utility Functions
  const getNestedValue = (obj, path) => path.split(".").reduce((acc, part) => acc?.[part], obj) || null;

  const formatStatus = (status) => {
    const statusMap = {
      0: { color: "bg-yellow-500", text: "Chờ xác nhận" },
      1: { color: "bg-blue-500", text: "Chờ giao hàng" },
      2: { color: "bg-orange-500", text: "Đang giao" },
      3: { color: "bg-green-500", text: "Hoàn thành" },
      4: { color: "bg-red-500", text: "Đã hủy" },
    };
    const { color, text } = statusMap[status] || { color: "bg-gray-500", text: "Không xác định" };
    return `<span class="px-3 py-1 inline-block text-white font-semibold rounded-full ${color}">${text}</span>`;
  };

  // Data Fetching Functions
  const fetchOrderTypes = async () => {
    try {
      const { data } = await axios.get("http://localhost:8080/hoa-don/order-types");
      orderTypes.value = data.map((type) => ({
        value: type.toLowerCase(),
        label: type,
      }));
    } catch (error) {
      console.error("Lỗi khi lấy danh sách loại đơn:", error);
      toast.value?.kshowToast("error", "Không thể tải danh sách loại đơn!");
      orderTypes.value = [
        { value: "online", label: "Online" },
        { value: "trực tiếp", label: "Trực tiếp" },
      ];
    }
  };

  const fetchInitialData = async () => {
    try {
      const { data } = await axios.get("http://localhost:8080/hoa-don/home", {
        params: { page: currentPage.value, size: pageSize.value, withCredentials: true },
      });
      processData(data);
    } catch (error) {
      handleFetchError(error);
    }
  };

  const fetchData = async () => {
    try {
      const params = {
        page: currentPage.value,
        size: pageSize.value,
        keyword: keyword.value || null,
        loaiDon: selectedOrderType.value || null,
        minAmount: minAmount.value !== minRange.value ? Number(minAmount.value) : null,
        maxAmount: maxAmount.value !== maxRange.value ? Number(maxAmount.value) : null,
        startDate: startDate.value || null,
        endDate: endDate.value || null,
        trangThai: selectedStatus.value !== null ? Number(selectedStatus.value) : null,
      };
      const { data } = await axios.get("http://localhost:8080/hoa-don/home", { params });
      processFilteredData(data);
    } catch (error) {
      handleFetchError(error);
    }
  };

  const processData = (data) => {
    const processedData = (data.content || []).map((item) => ({
      ...item,
      trangThaiFormatted: formatStatus(item.trangThai ?? 0),
    }));
    dataTable.value = processedData;
    originalData.value = [...processedData];
    totalElements.value = data.totalElements || 0;
    updateRangeValues();
  };

  const processFilteredData = (data) => {
    let filteredData = (data.content || []).map((item) => ({
      ...item,
      trangThaiFormatted: formatStatus(item.trangThai ?? 0),
    }));
    totalElements.value = data.totalElements || 0;

    if (keyword.value) {
      filteredData = searchAllFields(filteredData, keyword.value);
      totalElements.value = filteredData.length;
    }

    const hasFilters =
      keyword.value ||
      selectedOrderType.value ||
      minAmount.value !== minRange.value ||
      maxAmount.value !== maxRange.value ||
      startDate.value ||
      endDate.value ||
      selectedStatus.value !== null;

    dataTable.value = filteredData;
    isFiltering.value = hasFilters;
    if (!hasFilters) totalElements.value = data.totalElements || 0;
  };

  const handleFetchError = (error) => {
    console.error("Lỗi khi gọi API:", error);
    toast.value?.kshowToast("error", "Không thể tải dữ liệu!");
    dataTable.value = [];
    originalData.value = [];
    totalElements.value = 0;
  };

  const searchAllFields = (data, searchKeyword) => {
    if (!searchKeyword) return data;
    const lowerKeyword = searchKeyword.toLowerCase();
    return data.filter((item) =>
      [
        item.ma,
        getNestedValue(item, "idNhanVien.ma"),
        getNestedValue(item, "idKhachHang.ten"),
        item.soDienThoaiKhachHang,
        item.tongTienSauGiam?.toString(),
        getNestedValue(item, "idPhieuGiamGia.phanTramGiamGia") ? `${getNestedValue(item, "idPhieuGiamGia.phanTramGiamGia")} %` : "",
        item.phi_van_chuyen?.toString(),
        item.ngayTao ? new Date(item.ngayTao).toLocaleDateString() : "",
        item.loaiDon,
      ]
        .filter(Boolean)
        .some((field) => field.toLowerCase().includes(lowerKeyword))
    );
  };

  const updateRangeValues = () => {
    const maxValue = Math.max(...dataTable.value.map((item) => item.tongTienSauGiam || 0));
    if (maxValue > maxRange.value) {
      maxRange.value = Math.ceil(maxValue / 1000000) * 1000000;
      maxAmount.value = maxRange.value;
    }
  };

  // Action Handlers
  const applyFilters = () => fetchData();

  const goToPage = (page) => {
    currentPage.value = page;
    isFiltering.value ? fetchData() : fetchInitialData();
  };

  const handleStatusFilter = (status) => {
    selectedStatus.value = status;
    currentPage.value = 0;
    fetchData();
  };

  const viewUpdate = (id) => router.push(`/show-hoa-don/${id}`);

  const printInvoice = async (id) => {
    try {
      const response = await axios.get(`http://localhost:8080/hoa-don/print/${id}`, { responseType: "blob" });
      if (response.headers["content-type"] === "application/pdf") {
        const blobUrl = URL.createObjectURL(response.data);
        const printWindow = window.open(blobUrl);
        if (printWindow) {
          printWindow.onload = () => {
            printWindow.print();
            printWindow.onafterprint = () => printWindow.close();
          };
          toast.value?.kshowToast("success", "Mở hộp thoại in thành công!");
        } else {
          throw new Error("Không thể mở cửa sổ in.");
        }
        URL.revokeObjectURL(blobUrl);
      } else {
        throw new Error(await response.data.text());
      }
    } catch (error) {
      console.error("Lỗi khi in hóa đơn:", error);
      toast.value?.kshowToast("error", error.message || "Không thể mở hộp thoại in!");
    }
  };

  const downloadQrCode = async (ma) => {
    try {
      const qrCodeDataUrl = await QRCode.toDataURL(ma, { width: 300, margin: 1 });
      const blob = await (await fetch(qrCodeDataUrl)).blob();
      saveAs(blob, `QR_HoaDon_${ma}.png`);
      toast.value?.kshowToast("success", "Tải QR code thành công!");
    } catch (error) {
      console.error("Lỗi khi tải QR code:", error);
      toast.value?.kshowToast("error", "Không thể tải QR code!");
    }
  };

  const exportExcel = async () => {
    try {
      const response = await axios.get("http://localhost:8080/hoa-don/export-excel", { responseType: "blob" });
      const blob = response.data;
      const url = URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = response.headers["content-disposition"]?.match(/filename="(.+)"/)?.[1] || "DanhSachHoaDon.xlsx";
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url);
      toast.value?.kshowToast("success", "Xuất Excel thành công!");
    } catch (error) {
      console.error("Lỗi khi xuất Excel:", error);
      toast.value?.kshowToast("error", "Không thể xuất file Excel!");
    }
  };

  // QR Scanner Functions
  const openQrScanner = () => {
    showQrScanner.value = true;
    startQrScanner();
  };

  const startQrScanner = async () => {
    try {
      stream = await navigator.mediaDevices.getUserMedia({ video: { facingMode: "environment" } });
      videoElement.value.srcObject = stream;
      await new Promise((resolve) => (videoElement.value.onloadedmetadata = resolve));
      await videoElement.value.play();
      scanQrCode();
    } catch (error) {
      onQrError(error);
    }
  };

  const scanQrCode = () => {
    if (!videoElement.value || videoElement.value.videoWidth === 0 || videoElement.value.videoHeight === 0) {
      animationFrameId = requestAnimationFrame(scanQrCode);
      return;
    }
    const context = canvasElement.value.getContext("2d");
    canvasElement.value.width = videoElement.value.videoWidth;
    canvasElement.value.height = videoElement.value.videoHeight;

    const scan = () => {
      if (videoElement.value.readyState === videoElement.value.HAVE_ENOUGH_DATA) {
        context.drawImage(videoElement.value, 0, 0, canvasElement.value.width, canvasElement.value.height);
        const imageData = context.getImageData(0, 0, canvasElement.value.width, canvasElement.value.height);
        const code = jsQR(imageData.data, imageData.width, imageData.height);
        if (code) {
          onQrDecode(code.data);
          stopQrScanner();
          return;
        }
      }
      animationFrameId = requestAnimationFrame(scan);
    };
    scan();
  };

  const stopQrScanner = () => {
    if (stream) {
      stream.getTracks().forEach((track) => track.stop());
      stream = null;
    }
    if (animationFrameId) {
      cancelAnimationFrame(animationFrameId);
      animationFrameId = null;
    }
    showQrScanner.value = false;
  };

  const onQrDecode = async (result) => {
    qrResult.value = result;
    showQrScanner.value = false;
    try {
      const { data } = await axios.get(`http://localhost:8080/hoa-don/QR-by-ma/${result}`);
      if (data) {
        dataTable.value = [{ ...data, trangThaiFormatted: formatStatus(data.trangThai ?? 0) }];
        totalElements.value = 1;
        toast.value?.kshowToast("success", "Tìm thấy hóa đơn!");
      }
    } catch (error) {
      console.error("Lỗi khi tìm hóa đơn:", error);
      toast.value?.kshowToast("error", "Không tìm thấy hóa đơn!");
      dataTable.value = [];
      totalElements.value = 0;
    }
  };

  const onQrError = (error) => {
    console.error("Lỗi quét QR:", error);
    const errorMessages = {
      NotAllowedError: "Quyền truy cập camera bị từ chối. Vui lòng cấp quyền và thử lại.",
      NotFoundError: "Không tìm thấy camera trên thiết bị.",
      NotReadableError: "Camera đang được sử dụng bởi ứng dụng khác.",
    };
    toast.value?.kshowToast("error", errorMessages[error.name] || "Lỗi khi quét mã QR!");
  };

  // Modal Handlers
  const confirmAction = (message, action) => {
    confirmMessage.value = message;
    confirmedAction.value = action;
    showConfirmModal.value = true;
  };

  const executeConfirmedAction = () => {
    confirmedAction.value?.();
    closeConfirmModal();
  };

  const closeConfirmModal = () => {
    showConfirmModal.value = false;
    confirmedAction.value = null;
  };

  // Range Adjustment
  const adjustMin = () => {
    if (minAmount.value > maxAmount.value) minAmount.value = maxAmount.value;
  };

  const adjustMax = () => {
    if (maxAmount.value < minAmount.value) maxAmount.value = minAmount.value;
  };

  // Global Window Functions
  window.viewUpdate = viewUpdate;
  window.printInvoice = printInvoice;
  window.downloadQrCode = downloadQrCode;

  // Lifecycle Hooks
  onMounted(() => {
    fetchOrderTypes();
    fetchInitialData();
  });

  return {
    toast,
    dataTable,
    currentPage,
    totalPages,
    keyword,
    minAmount,
    maxAmount,
    selectedOrderType,
    startDate,
    endDate,
    orderTypes,
    columns,
    getNestedValue,
    applyFilters,
    goToPage,
    exportExcel,
    showConfirmModal,
    confirmMessage,
    executeConfirmedAction,
    closeConfirmModal,
    confirmAction,
    minRange,
    maxRange,
    adjustMin,
    adjustMax,
    showQrScanner,
    openQrScanner,
    stopQrScanner,
    videoElement,
    canvasElement,
    downloadQrCode,
    selectedStatus,
    handleStatusFilter,
  };
}