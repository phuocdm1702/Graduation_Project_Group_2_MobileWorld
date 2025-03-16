import axios from "axios";
import {onMounted, ref, watch, computed} from "vue";
import {useRoute} from "vue-router";

// Định nghĩa hàm toàn cục để gọi từ formatter
window.handleCheckboxChange = function (id) {
  const {fetchCTSPData} = useDotGiamGiaInstance;
  if (fetchCTSPData) fetchCTSPData(id);
};

let useDotGiamGiaInstance = null;

export const useDotGiamGia = () => {
  const dspList = ref([]);
  const ctspList = ref([]);
  const searchKeyword = ref("");
  const idDSPs = ref([]);
  const selectedDongSanPham = ref(null);
  const selectedBoNhoTrong = ref(null);
  const currentPageDSP = ref(0);
  const pageSizeDSP = ref(12);
  const totalPagesDSP = ref(0);
  const currentPageCTSP = ref(0);
  const pageSizeCTSP = ref(10);
  const totalPagesCTSP = ref(0);

  //Toast
  const toast = ref(null);
  const showConfirmModal = ref(false);
  const confirmMessage = ref('');
  const confirmedAction = ref(null);


  const confirmActionToast = (message, action) => {
    confirmMessage.value = message;
    confirmedAction.value = action;
    showConfirmModal.value = true;
  };

  const executeConfirmedAction = () => {
    if (confirmedAction.value) {
      confirmedAction.value();
    }
    closeConfirmModal();
  };

  const closeConfirmModal = () => {
    showConfirmModal.value = false;
    confirmedAction.value = null;
  };


  const dotGiamGia = ref({
    id: null,
    ma: "",
    tenDotGiamGia: "",
    loaiGiamGiaApDung: "",
    giaTriGiamGia: 0,
    soTienGiamToiDa: 0,
    ngayBatDau: "",
    ngayKetThuc: "",
    trangThai: false,
    deleted: false
  });

  const edit = ref(false);

  const capNhatGiaSauKhiGiam = () => {
    const loaiGiamGia = dotGiamGia.value.loaiGiamGiaApDung;
    const giaTriGiam = parseFloat(dotGiamGia.value.giaTriGiamGia) || 0;
    const soTienGiamToiDa = parseFloat(dotGiamGia.value.soTienGiamToiDa) || Infinity;

    ctspList.value.forEach((product) => {
      const giaBanDau = parseFloat(product.ctsp.giaBan) || 0;
      let giaSauGiam = giaBanDau;

      if (loaiGiamGia === "Tiền mặt") {
        giaSauGiam = giaBanDau - soTienGiamToiDa;
      } else if (loaiGiamGia === "Phần trăm") {
        const soTienGiam = giaBanDau * (giaTriGiam / 100);
        giaSauGiam = giaBanDau - Math.min(soTienGiam, soTienGiamToiDa);
      }

      product.giaSauKhiGiam = giaSauGiam >= 0 ? giaSauGiam : 0;
    });
  };

  const changePageDSP = (page) => {
    if (page >= 0 && page < totalPagesDSP.value) {
      currentPageDSP.value = page;
      fetchData();
    }
  };

  const changePageCTSP = (page) => {
    if (page >= 0 && page < totalPagesCTSP.value) {
      currentPageCTSP.value = page;
      fetchData();
    }
  };

  const displayedPagesDSP = computed(() => {
    const maxPagesToShow = 5;
    const half = Math.floor(maxPagesToShow / 2);
    let start = Math.max(1, currentPageDSP.value + 1 - half);
    let end = Math.min(totalPagesDSP.value, start + maxPagesToShow - 1);
    start = Math.max(1, end - maxPagesToShow + 1);
    return Array.from({length: end - start + 1}, (_, i) => start + i);
  });

  const displayedPagesCTSP = computed(() => {
    const maxPagesToShow = 5;
    const half = Math.floor(maxPagesToShow / 2);
    let start = Math.max(1, currentPageCTSP.value + 1 - half);
    let end = Math.min(totalPagesCTSP.value, start + maxPagesToShow - 1);
    start = Math.max(1, end - maxPagesToShow + 1);
    return Array.from({length: end - start + 1}, (_, i) => start + i);
  });

  const fetchData = async () => {
    try {
      const res = await axios.post(
        "http://localhost:8080/dot_giam_gia/ViewAddDotGiamGia",
        {
          keyword: searchKeyword.value,
          idDSPs: idDSPs.value || [],
          idBoNhoTrongs: selectedBoNhoTrong.value ? [selectedBoNhoTrong.value] : null
        },
        {
          params: {
            pageDSP: currentPageDSP.value,
            sizeDSP: pageSizeDSP.value,
            pageCTSP: currentPageCTSP.value,
            sizeCTSP: pageSizeCTSP.value
          }
        }
      );

      // Gán danh sách DSP
      dspList.value = res.data.dspList || [];

      // Xử lý danh sách CTSP để loại bỏ trùng lặp
      const uniqueCtspList = [];
      const seenIds = new Set();
      (res.data.ctspList || []).forEach(item => {
        if (!seenIds.has(item.ctsp.id)) {
          seenIds.add(item.ctsp.id);
          uniqueCtspList.push(item);
        }
      });
      ctspList.value = uniqueCtspList;

      // Cập nhật tổng số trang
      totalPagesDSP.value = res.data.totalPages || Math.ceil((res.data.totalElements || dspList.value.length) / pageSizeDSP.value) || 0;
      totalPagesCTSP.value = res.data.totalPagesCTSP || 0;

      // Điều chỉnh trang hiện tại nếu vượt quá tổng số trang
      if (currentPageDSP.value >= totalPagesDSP.value && totalPagesDSP.value > 0) {
        currentPageDSP.value = totalPagesDSP.value - 1;
        fetchData();
      }
      if (currentPageCTSP.value >= totalPagesCTSP.value && totalPagesCTSP.value > 0) {
        currentPageCTSP.value = totalPagesCTSP.value - 1;
        fetchData();
      }

      capNhatGiaSauKhiGiam();
    } catch (error) {
      console.error("Lỗi khi gọi API:", error);
    }
  };

  const fetchCTSPData = (id) => {
    if (idDSPs.value.includes(id)) {
      idDSPs.value = idDSPs.value.filter(dspId => dspId !== id);
    } else {
      idDSPs.value.push(id);
    }
    currentPageCTSP.value = 0;
    fetchData();
  };

  const columns = ref([
    {
      key: "select",
      label: "",
      formatter: (value, item) => {
        return `
          <input
            type="checkbox"
            value="${item.id}"
            ${idDSPs.value.includes(item.id) ? "checked" : ""}
            onchange="handleCheckboxChange(${item.id})"
          />
        `;
      },
    },
    {key: "index", label: "#", formatter: (_, __, index) => (currentPageDSP.value * pageSizeDSP.value) + index + 1},
    {key: "ma", label: "Mã"},
    {key: "dongSanPham", label: "Dòng sản phẩm"},
  ]);

  const getNestedValue = (obj, key) => (key === "index" ? null : obj[key]);

  const columns2 = ref([
    {
      key: "index",
      label: "#",
      formatter: (_, __, index) => (currentPageCTSP.value * pageSizeCTSP.value) + index + 1,
    },
    {
      key: "anh.duongDan",
      label: "Ảnh",
      formatter: (value) => value ? `<img src="${value}" alt="Ảnh" class="w-10 h-10 object-cover">` : "N/A",
    },
    {
      key: "dsp.dongSanPham",
      label: "Tên sản phẩm",
      formatter: (value) => value ?? "Chưa có dữ liệu",
    },
    {
      key: "bnt.dungLuongBoNhoTrong",
      label: "Dung lượng bộ nhớ trong",
      formatter: (value) => value ?? "Chưa có dữ liệu",
    },
    {
      key: "ctsp.giaBan",
      label: "Đơn giá",
      formatter: (value) => value !== undefined ? value.toLocaleString() : "N/A",
    },
    {
      key: "soTienGiamToiDa",
      label: "Số tiền giảm tối đa",
      formatter: (value, item) => {
        const loaiGiamGia = dotGiamGia.value.loaiGiamGiaApDung;
        const giaTriGiam = parseFloat(dotGiamGia.value.giaTriGiamGia) || 0;
        const soTienGiamToiDa = parseFloat(dotGiamGia.value.soTienGiamToiDa) || Infinity;
        const giaBanDau = parseFloat(item.ctsp.giaBan) || 0;

        if (loaiGiamGia === "Phần trăm") {
          const soTienGiamThucTe = giaBanDau * (giaTriGiam / 100);
          return Math.min(soTienGiamThucTe, soTienGiamToiDa).toLocaleString(); // Hiển thị số tiền giảm thực tế hoặc tối đa
        }
        return soTienGiamToiDa.toLocaleString(); // Mặc định cho "Tiền mặt" hoặc khi không có loại giảm giá
      },
    },
    {
      key: "giaSauKhiGiam",
      label: "Đơn giá sau giảm giá",
      formatter: (value) => value !== undefined ? value.toLocaleString() : "N/A",
    },
  ]);

  const getNestedValue2 = (obj, key) => {
    if (key === "index") return null;
    return key.split('.').reduce((o, k) => (o && o[k] !== undefined ? o[k] : 'N/A'), obj);
  };

  const uniqueDongSanPhams = computed(() => {
    const unique = new Set(ctspList.value.map(ctsp => ctsp.dsp.dongSanPham));
    return Array.from(unique);
  });

  const filteredBoNhoTrong = computed(() => {
    const allBoNhoTrong = ctspList.value.map(ctsp => ctsp.bnt.dungLuongBoNhoTrong);
    return [...new Set(allBoNhoTrong)];
  });

  // Sửa filteredCTSPList để hiển thị dựa trên idDSPs
  const filteredCTSPList = computed(() => {
    if (idDSPs.value.length === 0) return [];
    return ctspList.value.filter(ctsp => {
      const dspId = ctsp.dsp.id;
      const matchDSP = idDSPs.value.includes(dspId);
      const matchDongSanPham = selectedDongSanPham.value
        ? ctsp.dsp.dongSanPham === selectedDongSanPham.value
        : true;
      const matchBoNhoTrong = selectedBoNhoTrong.value
        ? ctsp.bnt.dungLuongBoNhoTrong === selectedBoNhoTrong.value
        : true;
      return matchDSP && matchDongSanPham && matchBoNhoTrong;
    });
  });

  const checkDuplicate = async (field, value, excludeId = null) => {
    try {
      const {data} = await axios.get(`http://localhost:8080/dot_giam_gia/ViewAddDotGiamGia/exists/${field}`, {
        params: {[field]: value, excludeId},
      });
      return data;
    } catch (error) {
      console.error("Error calling API:", error);
      toast.value?.kshowToast("error", "Sảy ra lỗi");
      return false;
    }
  };

  const validate = async function () {
    const today = new Date().toISOString().split("T")[0];
    if (dotGiamGia.value.ma == "") {
      toast.value?.kshowToast("error", "Vui lòng nhập mã");
      return false;
    }
    if (edit.value == false) {
      const isDuplicate = await checkDuplicate('ma', dotGiamGia.value.ma);
      if (isDuplicate) {
        toast.value?.kshowToast("error", "Mã đã tồn tại");
        return false;
      }
    }
    if (dotGiamGia.value.tenDotGiamGia == "") {
      toast.value?.kshowToast("error", "Vui lòng nhập tên đợt giảm giá");
      return false;
    }
    if (dotGiamGia.value.loaiGiamGiaApDung == "") {
      toast.value?.kshowToast("error", "Vui lòng chọn loại giảm giá");
      return false;
    }
    if (dotGiamGia.value.giaTriGiamGia == 0 && dotGiamGia.value.loaiGiamGiaApDung != "Tiền mặt") {
      toast.value?.kshowToast("error", "Vui lòng nhập giá trị giảm giá");
      return false;
    }
    if (dotGiamGia.value.soTienGiamToiDa == 0) {
      toast.value?.kshowToast("error", "Vui lòng nhập số tiền giảm tối đa");
      return false;
    }
    if (dotGiamGia.value.ngayBatDau == "") {
      toast.value?.kshowToast("error", "Vui lòng chọn ngày bắt đầu");
      return false;
    }
    if (dotGiamGia.value.ngayBatDau < today) {
      toast.value?.kshowToast("error", "Ngày bắt đầu không được nhỏ hơn ngày hiện tại");
      return false;
    }
    if (dotGiamGia.value.ngayKetThuc == "" || dotGiamGia.value.ngayKetThuc < dotGiamGia.value.ngayBatDau) {
      toast.value?.kshowToast("error", "Vui lòng chọn lại ngày kết thúc");
      return false;
    }
    if (dotGiamGia.value.ngayKetThuc < dotGiamGia.value.ngayBatDau) {
      toast.value?.kshowToast("error", "Ngày kết thúc không được nhỏ hơn ngày bắt đầu");
      return false;
    }
    if (idDSPs.value.length === 0) {
      toast.value?.kshowToast("error", "Vui lòng chọn dòng sản phẩm trong đợt giảm giá");
      return false;
    }
    return true;
  };

  const fetchDongSanPham = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/dot_giam_gia/viewUpdate?id=${dotGiamGia.value.id}`);
      idDSPs.value = response.data.map(dsp => dsp.id);
    } catch (error) {
      console.error("Lỗi khi lấy danh sách dòng sản phẩm:", error);
    }
  };


  const confirmAction = async () => {
    const message = edit.value ? "Có muốn cập nhật dữ liệu không?" : "Có muốn thêm dữ liệu không?";
    confirmActionToast(message, async () => {
      await addData();
    });
  };


  const resetForm = () => {
    dotGiamGia.value = {
      id: null,
      ma: "",
      tenDotGiamGia: "",
      loaiGiamGiaApDung: "",
      giaTriGiamGia: 0,
      soTienGiamToiDa: 0,
      ngayBatDau: "",
      ngayKetThuc: "",
      trangThai: false,
      deleted: false
    };
    edit.value = false;
    idDSPs.value = [];
    selectedDongSanPham.value = null;
    selectedBoNhoTrong.value = null;
  };

  const addData = async () => {
    const requestData = {
      dotGiamGia: dotGiamGia.value,
      idDSPs: idDSPs.value,
      ctspList: ctspList.value,
    };
    const isValid = await validate();
    if (isValid) {
      try {
        if (edit.value) {
          console.log("Dữ liệu gửi đi:", requestData);
          const response = await axios.put(
            `http://localhost:8080/dot_giam_gia/AddDotGiamGia/${dotGiamGia.value.id}`,
            requestData,
            {headers: {"Content-Type": "application/json"}}
          );
          toast.value?.kshowToast("success", "Sửa thành công")
          resetForm();
        } else {
          console.log("Dữ liệu gửi đi:", requestData);
          const response = await axios.post(
            "http://localhost:8080/dot_giam_gia/AddDotGiamGia",
            requestData,
            {headers: {"Content-Type": "application/json"}}
          );
          toast.value?.kshowToast("success", "Thêm thành công");
          resetForm();
        }
      } catch (error) {
        console.error("Lỗi khi thêm đợt giảm giá:", error);
        toast.value?.kshowToast("error", "Thêm thất bại!");
      }
    }
  };

  const route = useRoute();

  const formatDateLocal = (dateString) => {
    const date = new Date(dateString);
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
  };

  watch(
    () => [
      dotGiamGia.value.loaiGiamGiaApDung,
      dotGiamGia.value.giaTriGiamGia,
      dotGiamGia.value.soTienGiamToiDa,
      ctspList.value
    ],
    () => {
      capNhatGiaSauKhiGiam();
    },
    {deep: true}
  );

  watch(
    () => route.query,
    (newQuery) => {
      if (newQuery.id) {
        edit.value = true;
        dotGiamGia.value = {
          id: newQuery.id,
          ma: newQuery.ma || "",
          tenDotGiamGia: newQuery.tenDotGiamGia || "",
          loaiGiamGiaApDung: newQuery.loaiGiamGiaApDung || "",
          giaTriGiamGia: newQuery.giaTriGiamGia || 0.0,
          soTienGiamToiDa: newQuery.soTienGiamToiDa || "",
          ngayBatDau: newQuery.ngayBatDau ? formatDateLocal(newQuery.ngayBatDau) : "",
          ngayKetThuc: newQuery.ngayKetThuc ? formatDateLocal(newQuery.ngayKetThuc) : "",
          trangThai: newQuery.trangThai || "",
        };
        console.log("Updated dotGiamGia:", dotGiamGia.value);
        fetchDongSanPham();
      }
    },
    {immediate: true}
  );

  watch(selectedDongSanPham, () => {
    currentPageCTSP.value = 0;
    fetchData();
  });

  watch(currentPageDSP, fetchData);

  watch(searchKeyword, () => {
    currentPageDSP.value = 0;
    fetchData();
  });

  watch(idDSPs, (newValue, oldValue) => {
    console.log("Watcher triggered - idDSPs changed from:", oldValue, "to:", newValue);
    currentPageCTSP.value = 0;
    fetchData();
  });

  watch(() => dotGiamGia.value.loaiGiamGiaApDung, (newVal) => {
    if (newVal === 'Tiền mặt') {
      dotGiamGia.value.giaTriGiamGia = 0.0;
    }
  });

  onMounted(fetchData);

  // Lưu instance để dùng trong hàm toàn cục
  useDotGiamGiaInstance = {
    toast,
    currentPageDSP,
    changePageDSP,
    pageSizeDSP,
    totalPagesDSP,
    currentPageCTSP,
    changePageCTSP,
    pageSizeCTSP,
    totalPagesCTSP,
    dspList,
    ctspList,
    searchKeyword,
    idDSPs,
    selectedDongSanPham,
    selectedBoNhoTrong,
    dotGiamGia,
    edit,
    uniqueDongSanPhams,
    filteredBoNhoTrong,
    filteredCTSPList,
    addData,
    resetForm,
    confirmAction,
    columns,
    getNestedValue,
    columns2,
    getNestedValue2,
    fetchCTSPData,
    displayedPagesDSP,
    displayedPagesCTSP,
    showConfirmModal,
    confirmMessage,
    confirmedAction,
    confirmActionToast,
    executeConfirmedAction,
    closeConfirmModal,
  };

  return useDotGiamGiaInstance;
};