// Nhập các hàm từ Vue để quản lý trạng thái và vòng đời
import { ref, onMounted } from "vue";
// Nhập axios để thực hiện các yêu cầu HTTP
import axios from "axios";

/**
 * Hàm hook để quản lý danh sách lịch sử hóa đơn.
 * @returns {Object} - Đối tượng chứa dữ liệu bảng, cột và hàm hỗ trợ.
 */
export default function useLichSuHoaDonLineList() {
  // Khai báo biến reactive để lưu dữ liệu lịch sử hóa đơn
  const dataTableLSHD = ref([]);

  // Gọi API khi component được gắn kết (mounted)
  onMounted(async () => {
    try {
      // Gửi yêu cầu GET đến endpoint API
      const res = await axios.get("http://localhost:8080/lich-su-hoa-don/home");
      // In dữ liệu từ API ra console để kiểm tra
      console.log("Dữ liệu từ API:", res.data);
      // Gán dữ liệu trả về từ API vào biến dataTableLSHD
      dataTableLSHD.value = res.data;
    } catch (error) {
      // Xử lý lỗi nếu có và in ra console
      console.error("Lỗi:", error);
    }
  });

  // Định nghĩa các cột cho bảng lịch sử hóa đơn
  const columns = [
    {
      label: "#", // Tiêu đề cột số thứ tự
      key: "",   // Không cần key vì dùng formatter để tạo số thứ tự
      formatter: (_, __, index) => index + 1, // Tạo số thứ tự từ index (bắt đầu từ 1)
    },
    {
      label: "Mã", // Tiêu đề cột mã lịch sử
      key: "ma",  // Khóa để truy cập giá trị mã
    },
    {
      label: "Hóa đơn",       // Tiêu đề cột mã hóa đơn
      key: "idHoaDon.ma",    // Khóa lồng nhau để lấy mã hóa đơn
    },
    {
      label: "Nhân viên",    // Tiêu đề cột mã nhân viên
      key: "idNhanVien.ma", // Khóa lồng nhau để lấy mã nhân viên
    },
    {
      label: "Hành động", // Tiêu đề cột hành động
      key: "hanhDong",   // Khóa để truy cập giá trị hành động
    },
    {
      label: "Thời hành động", // Tiêu đề cột thời gian hành động
      key: "thoiGian",        // Khóa để truy cập giá trị thời gian
    },
  ];

  /**
   * Hàm lấy giá trị lồng nhau từ một đối tượng dựa trên chuỗi đường dẫn.
   * @param {Object} obj - Đối tượng đầu vào chứa dữ liệu lồng nhau.
   * @param {string} path - Chuỗi đường dẫn (ví dụ: "idHoaDon.ma").
   * @returns {any|null} - Giá trị tại đường dẫn hoặc null nếu không tìm thấy.
   */
  const getNestedValue = (obj, path) => {
    return (
      path
        .split(".") // Chia chuỗi đường dẫn thành mảng bằng dấu chấm
        .reduce((acc, part) => acc?.[part], obj) // Duyệt qua mảng để truy cập giá trị
      || null // Trả về null nếu không tìm thấy giá trị
    );
  };

  // Trả về các giá trị để sử dụng trong component
  return {
    dataTableLSHD, // Dữ liệu bảng lịch sử hóa đơn
    columns,       // Danh sách cột của bảng
    getNestedValue, // Hàm hỗ trợ lấy giá trị lồng nhau
  };
}