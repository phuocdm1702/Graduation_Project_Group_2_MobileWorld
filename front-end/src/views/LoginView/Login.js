import { ref } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

export default function useLogin() {
  const router = useRouter();
  const tenDangNhap = ref("");
  const matKhau = ref("");
  const error = ref("");

  async function login() {
    try {
      const response = await axios.post(
        "http://localhost:8080/tai-khoan/login",
        {
          tenDangNhap: tenDangNhap.value,
          matKhau: matKhau.value,
        },
        { withCredentials: true }
      );

      if (response.status === 200 && response.data) {
        router.push("/dashboard"); // Chuyển hướng trực tiếp tới dashboard
      } else {
        error.value = "Tên đăng nhập hoặc mật khẩu không đúng!";
      }
    } catch (err) {
      error.value = "Đăng nhập thất bại. Vui lòng thử lại!";
      console.error("Login error:", err);
    }
  }

  return {
    tenDangNhap,
    matKhau,
    error,
    login,
  };
}