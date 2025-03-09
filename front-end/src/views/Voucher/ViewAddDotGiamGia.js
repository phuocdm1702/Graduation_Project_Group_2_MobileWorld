import axios from "axios";
import { onMounted, ref, watch, computed } from "vue";
import { useRoute } from "vue-router";

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

  const uniqueDongSanPhams = computed(() => {
    const unique = new Set(ctspList.value.map(ctsp => ctsp.dsp.dongSanPham));
    return Array.from(unique);
  });

  const filteredBoNhoTrong = computed(() => {
    const allBoNhoTrong = ctspList.value.map(ctsp => ctsp.bnt.dungLuongBoNhoTrong);
    return [...new Set(allBoNhoTrong)];
  });

  const filteredCTSPList = computed(() => {
    return ctspList.value.filter(ctsp => {
      const matchDongSanPham = selectedDongSanPham.value
        ? ctsp.dsp.dongSanPham === selectedDongSanPham.value
        : true;

      const matchBoNhoTrong = selectedBoNhoTrong.value
        ? ctsp.bnt.dungLuongBoNhoTrong === selectedBoNhoTrong.value
        : true;

      return matchDongSanPham && matchBoNhoTrong;
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
      idDSPs.value = response.data.map(dsp => dsp.id); // Gán danh sách ID đã chọn
    } catch (error) {
      console.error("Lỗi khi lấy danh sách dòng sản phẩm:", error);
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
        validate();
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
          ngayBatDau: newQuery.ngayBatDau
            ? formatDateLocal(newQuery.ngayBatDau)
            : "",
          ngayKetThuc: newQuery.ngayKetThuc
            ? formatDateLocal(newQuery.ngayKetThuc)
            : "",
          trangThai: newQuery.trangThai || "",
        };
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
    selectedBoNhoTrong.value = null; // Reset bộ nhớ trong khi chọn dòng mới
  });

  onMounted(fetchData);

  watch(searchKeyword, fetchData);
  watch(idDSPs, fetchData);

  return {
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
  };
};