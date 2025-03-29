import { ref } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

export default function useLogin() {
  const router = useRouter();
  const tenDangNhap = ref("");
  const matKhau = ref("");
  const error = ref("");
  const showConfirmModal = ref(false);
  const confirmMessage = ref("");

  async function login() {
    try {
      const response = await axios.post(
        "http://localhost:8080/tai-khoan/login",
        {
          tenDangNhap: tenDangNhap.value,
          matKhau: matKhau.value,
        },
        { withCredentials: true } // Gửi cookie/session nếu dùng Spring Security
      );

      if (response.status === 200 && response.data) {
        confirmMessage.value = "Đăng nhập thành công! Chuyển tới dashboard?";
        showConfirmModal.value = true;
      } else {
        error.value = "Tên đăng nhập hoặc mật khẩu không đúng!";
      }
    } catch (err) {
      error.value = "Đăng nhập thất bại. Vui lòng thử lại!";
      console.error("Login error:", err);
    }
  }

  function executeConfirmedAction() {
    router.push("/dashboard");
    closeConfirmModal();
  }

  function closeConfirmModal() {
    showConfirmModal.value = false;
    confirmMessage.value = "";
  }

  return {
    tenDangNhap,
    matKhau,
    error,
    showConfirmModal,
    confirmMessage,
    login,
    executeConfirmedAction,
    closeConfirmModal,
  };
}