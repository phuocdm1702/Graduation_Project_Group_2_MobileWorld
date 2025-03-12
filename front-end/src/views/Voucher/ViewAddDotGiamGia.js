import axios from "axios";
import { onMounted, ref, watch, computed } from "vue";
import { useRoute } from "vue-router";

// Định nghĩa hàm toàn cục để gọi từ formatter
window.handleCheckboxChange = function(id) {
  const { fetchCTSPData } = useDotGiamGiaInstance;
  if (fetchCTSPData) fetchCTSPData(id);
};

// Lưu instance của useDotGiamGia để truy cập từ hàm toàn cục
let useDotGiamGiaInstance = null;

export const useDotGiamGia = () => {
  const dspList = ref([]);
  const ctspList = ref([]);
  const searchKeyword = ref("");
  const idDSPs = ref([]);
  const selectedDongSanPham = ref(null);
  const selectedBoNhoTrong = ref(null);

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

    ctspList.value = ctspList.value.map((product) => {
      const giaBanDau = parseFloat(product.ctsp.giaBan) || 0;
      let giaSauGiam = giaBanDau;

      if (loaiGiamGia === "Tiền mặt") {
        giaSauGiam = giaBanDau - soTienGiamToiDa;
      } else if (loaiGiamGia === "Phần trăm") {
        const soTienGiam = giaBanDau * (giaTriGiam / 100);
        giaSauGiam = giaBanDau - Math.min(soTienGiam, soTienGiamToiDa);
      }

      return {
        ...product,
        giaSauKhiGiam: giaSauGiam
      };
    });
  };

  const fetchData = async () => {
    try {
      const res = await axios.post(
        "http://localhost:8080/dot_giam_gia/ViewAddDotGiamGia",
        {
          keyword: searchKeyword.value,
          idDSPs: idDSPs.value || [],
          idBoNhoTrongs: selectedBoNhoTrong.value ? [selectedBoNhoTrong.value] : null
        }
      );
      dspList.value = res.data.dspList || [];
      ctspList.value = res.data.ctspList || [];
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
    fetchData(); // Cập nhật dữ liệu khi tick/un-tick
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
            ${idDSPs.value.includes(item.id) ? 'checked' : ''}
            onchange="handleCheckboxChange(${item.id})"
          />
        `;
      },
    },
    { key: "index", label: "STT", formatter: (value, item, index) => index + 1 },
    { key: "ma", label: "Mã" },
    { key: "dongSanPham", label: "Dòng sản phẩm" },
  ]);

  const getNestedValue = (obj, key) => (key === "index" ? null : obj[key]);

  const columns2 = ref([
    {
      key: "index",
      label: "STT",
      formatter: (_, __, index) => index + 1,
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
      formatter: () => dotGiamGia.value.soTienGiamToiDa ?? "N/A",
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
    if (idDSPs.value.length === 0) return []; // Nếu không chọn dòng sản phẩm nào, trả về rỗng
    return ctspList.value.filter(ctsp => {
      const dspId = ctsp.dsp.id; // Giả sử ctsp.dsp có id
      const matchDSP = idDSPs.value.includes(dspId);
      const matchBoNhoTrong = selectedBoNhoTrong.value
        ? ctsp.bnt.dungLuongBoNhoTrong === selectedBoNhoTrong.value
        : true;
      return matchDSP && matchBoNhoTrong;
    });
  });

  const checkDuplicate = async (field, value, excludeId = null) => {
    try {
      const { data } = await axios.get(`http://localhost:8080/dot_giam_gia/ViewAddDotGiamGia/exists/${field}`, {
        params: { [field]: value, excludeId },
      });
      return data;
    } catch (error) {
      console.error("Error calling API:", error);
      alert("Sảy ra lỗi");
      return false;
    }
  };

  const validate = async function () {
    const today = new Date().toISOString().split("T")[0];
    if (dotGiamGia.value.ma == "") {
      alert("Vui lòng nhập mã");
      return false;
    }
    if (edit.value == false) {
      const isDuplicate = await checkDuplicate('ma', dotGiamGia.value.ma);
      if (isDuplicate) {
        alert("Mã đã tồn tại");
        return false;
      }
    }
    if (dotGiamGia.value.loaiGiamGiaApDung == "") {
      alert("Vui lòng chọn loại giảm giá");
      return false;
    }
    if (dotGiamGia.value.giaTriGiamGia == 0 && dotGiamGia.value.loaiGiamGiaApDung != "Tiền mặt") {
      alert("Vui lòng nhập giá trị giảm giá");
      return false;
    }
    if (dotGiamGia.value.soTienGiamToiDa == 0) {
      alert("Vui lòng nhập số tiền giảm tối đa");
      return false;
    }
    if (dotGiamGia.value.ngayBatDau == "") {
      alert("Vui lòng chọn ngày bắt đầu");
      return false;
    }
    if (dotGiamGia.value.ngayBatDau < today) {
      alert("Ngày bắt đầu không được nhỏ hơn ngày hiện tại");
      return false;
    }
    if (dotGiamGia.value.ngayKetThuc == "" || dotGiamGia.value.ngayKetThuc < dotGiamGia.value.ngayBatDau) {
      alert("Vui lòng chọn lại ngày kết thúc");
      return false;
    }
    if (dotGiamGia.value.ngayKetThuc < dotGiamGia.value.ngayBatDau) {
      alert("Ngày kết thúc không được nhỏ hơn ngày bắt đầu");
      return false;
    }
    if (idDSPs.value.length === 0) {
      alert("Vui lòng chọn dòng sản phẩm trong đợt giảm giá");
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
    if (confirm(message)) {
      await addData();
    }
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
            { headers: { "Content-Type": "application/json" } }
          );
          alert("Sửa thành công");
          resetForm();
        } else {
          console.log("Dữ liệu gửi đi:", requestData);
          const response = await axios.post(
            "http://localhost:8080/dot_giam_gia/AddDotGiamGia",
            requestData,
            { headers: { "Content-Type": "application/json" } }
          );
          alert("Thêm thành công");
          resetForm();
        }
      } catch (error) {
        console.error("Lỗi khi thêm đợt giảm giá:", error);
        alert("Thêm thất bại!");
      }
    }
  };

  const route = useRoute();

  const formatDateLocal = (dateString) => {
    const date = new Date(dateString);
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
  };

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
          giaTriGiamGia: newQuery.giaTriGiamGia || "",
          soTienGiamToiDa: newQuery.soTienGiamToiDa || "",
          ngayBatDau: newQuery.ngayBatDau ? formatDateLocal(newQuery.ngayBatDau) : "",
          ngayKetThuc: newQuery.ngayKetThuc ? formatDateLocal(newQuery.ngayKetThuc) : "",
          trangThai: newQuery.trangThai || "",
        };
        console.log("Updated dotGiamGia:", dotGiamGia.value);
        fetchDongSanPham();
      }
    },
    { immediate: true }
  );

  watch(
    () => [dotGiamGia.value.loaiGiamGiaApDung, dotGiamGia.value.giaTriGiamGia],
    () => {
      capNhatGiaSauKhiGiam();
    }
  );

  watch(selectedDongSanPham, async () => {
    await fetchData();
    selectedBoNhoTrong.value = null;
  });

  onMounted(fetchData);

  watch(searchKeyword, fetchData);
  watch(idDSPs, fetchData);

  watch(() => dotGiamGia.value.loaiGiamGiaApDung, (newVal) => {
    if (newVal === 'Tiền mặt') {
      dotGiamGia.value.giaTriGiamGia = null;
    }
  });

  // Lưu instance để dùng trong hàm toàn cục
  useDotGiamGiaInstance = {
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
    fetchCTSPData
  };

  return useDotGiamGiaInstance;
};