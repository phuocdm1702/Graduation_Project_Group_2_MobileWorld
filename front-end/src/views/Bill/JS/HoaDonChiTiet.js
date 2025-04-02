import { ref, onMounted } from "vue";
import axios from "axios";

export default function useHoaDonChiTietLineList() {
  const dataTableHDCT = ref([]);

  onMounted(async () => {
    try {
      const res = await axios.get("http://localhost:8080/hoa-don-chi-tiet/home");
      console.log("Dữ liệu từ API:", res.data);
      dataTableHDCT.value = res.data;
    } catch (error) {
      console.error("Lỗi:", error);
    }
  });

  const columns = [
    {
      label: "#",
      key: "index",
      formatter: (_, __, index) => index + 1,
    },
    {
      label: "Mã",
      key: "ma",
    },
    {
      label: "Hóa đơn",
      key: "idHoaDon.ma",
    },
    {
      label: "Chi tiết sản phẩm",
      key: "idChiTietSanPham.id",
    },
    {
      label: "Imel",
      key: "idImelDaBan.imel",
    },
    {
      label: "Tổng tiền",
      key: "gia",
      formatter: (value) => (value ? `${value.toLocaleString()} VND` : "0 VND"),
    },
    {
      label: "Trạng thái",
      key: "trangThai",
      formatter: (value) =>
        value === 1 ? "Đã thanh toán" : "Chờ thanh toán",
    },
    {
      label: "Ghi chú",
      key: "ghiChu",
      formatter: (value) => value || "Không có",
    },
  ];

  const getNestedValue = (obj, path) => {
    return path.split(".").reduce((acc, part) => acc?.[part], obj) || null;
  };

  return {
    dataTableHDCT,
    columns,
    getNestedValue,
  };
}