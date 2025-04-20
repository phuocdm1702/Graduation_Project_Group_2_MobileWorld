<template>
  <link
    rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css"
    integrity="sha512-MV7K8+y+gLIBoVD59lQIYicR65iaqukzvf/nwasF0nqhPay5w/9lJmVM2hMDcnK1OnMGCdVK+iQrJ7lzPJQd1w=="
    crossorigin="anonymous"
    referrerpolicy="no-referrer"
  />
  <ul class="notifications">
    <li
      v-for="toast in toasts"
      :key="toast.id"
      :class="['toast', toast.type]"
    >
      <div class="column">
        <i :class="['fa', toast.icon, toast.type + '-icon']"></i>
        <span>{{ toast.message }}</span>
      </div>
      <i class="fa-solid fa-xmark close-icon" @click="removeToast(toast.id)"></i>
    </li>
  </ul>
</template>

<script setup>
import { ref } from "vue";

const toasts = ref([]);

const toastDetails = {
  success: { icon: "fa-check-circle" },
  error: { icon: "fa-times-circle" },
  warning: { icon: "fa-exclamation-circle" },
  info: { icon: "fa-info-circle" },
};

const kshowToast = (type, message) => {
  const id = Date.now();
  toasts.value.push({ id, type, message, icon: toastDetails[type].icon });

  setTimeout(() => removeToast(id), 3000);
};

const removeToast = (id) => {
  toasts.value = toasts.value.filter((toast) => toast.id !== id);
};

// Expose kshowToast method
defineExpose({ kshowToast });
</script>

<style scoped>
.notifications {
  position: fixed;
  top: 30px;
  right: 20px;
  z-index: 9999; /* Đảm bảo toast luôn hiển thị trên cùng */
}

.toast {
  display: flex;
  align-items: center;
  width: 400px;
  background: white;
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 4px;
  justify-content: space-between;
  animation: show_toast 0.5s ease forwards;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.toast i {
  font-size: 1.5rem;
  margin-right: 10px;
}

.toast.success { border-left: 5px solid #00b894; }
.toast.error { border-left: 5px solid #d63031; }
.toast.warning { border-left: 5px solid #fdcb6e; }
.toast.info { border-left: 5px solid #0984e3; }

.success-icon { color: #00b894; }
.error-icon { color: #d63031; }
.warning-icon { color: #fdcb6e; }
.info-icon { color: #0984e3; }

.close-icon {
  cursor: pointer;
  font-size: 1.2rem;
  color: #888;
}
.close-icon:hover {
  color: #555;
}

@keyframes show_toast {
  0% { transform: translateX(100%); }
  100% { transform: translateX(0); }
}

/* Thêm hiệu ứng mờ dần khi toast biến mất */
.toast-leave-active {
  transition: opacity 0.5s ease;
}
.toast-leave-to {
  opacity: 0;
}
</style>