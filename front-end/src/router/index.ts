import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

// Thống kê
import Dashboard from "../views/ThongKe/AppDashboard.vue";

// Giảm giá
import Voucher from "../views/Voucher/AppVoucher.vue";
import DotVoucher from "../views/Voucher/DotVoucher.vue";
import FormAddPgg from "../views/Voucher/components/FormAddPgg.vue";
import FormUpdatePgg from "@/views/Voucher/components/FormUpdatePgg.vue";
import PhieuBaoHanh from "../views/BaoHanh/PhieuBaoHanh.vue";

// View Login
import Login from "../views/LoginView/AppLogin.vue";
import DangKy from "@/views/LoginView/DangKy/DangKy.vue";
import QuenMk from "@/views/LoginView/QuenMk/QuenMk.vue";

// Đơn hàng
import LichSuHD from "../views/Bill/LichSuHoaDon.vue";
import HoaDon from "../views/Bill/HoaDon.vue";
import HoaDonChiTiet from "../views/Bill/HoaDonChiTiet.vue";
import ShowHoaDon from "../views/Bill/ShowHoaDon.vue";

// Tài khoản
import NhanVien from "../views/AccountType/NhanVien.vue";
import EmployeeForm from "@/views/AccountType/EmployeeForm.vue";
import KhachHang from "../views/AccountType/KhachHang.vue";
import CustmerForm from "@/views/AccountType/CustmerForm.vue";
import NotFound from "../views/NotFound.vue";
import UpdateNhanVien from "@/views/AccountType/UpdateNhanVien.vue";
import UpdateCustmer from "@/views/AccountType/UpdateCustmer.vue";
import UpdateMk from "@/views/LoginView/QuenMk/UpdateMk.vue";

// Thương hiệu
import Manufacturer from "@/views/Products/Manufacturer/Manufacturer.vue"
import ManufacturerAdd from "@/views/Products/Manufacturer/ManufacturerAdd.vue";
import ManufacturerEdit from "@/views/Products/Manufacturer/ManufacturerEdit.vue";

// Màn hình
import AddCongNgheManHinh from "@/views/Products/Screen/AddCongNgheManHinh.vue";
import EditCongNgheManHinh from "@/views/Products/Screen/EditCongNgheManHinh.vue";
import CongNgheMH from "@/views/Products/Screen/CongNgheMH.vue";

// RAM
import Ram from "../views/Products/MemorySanPham/RamSanPham.vue";

// Bộ nhớ
import InternalStorage from "../views/Products/MemorySanPham/BoNhoTrong.vue";
import ExternalStorage from "../views/Products/MemorySanPham/BoNhoNgoai.vue";

// CPU & GPU
import CPU from "../views/Products/Chip/CPU.vue";
import GPU from "../views/Products/Chip/GPU.vue";

// Camera
import CameraModule from "../views/Products/CameraSanPham/CumCamera.vue";

// Sạc
import ChargingSupport from "../views/Products/Sac/HoTroCongNgheSac.vue";

// Bán Hàng
import BanHang from "@/views/BanHang/BanHang.vue";

// Sản Phẩm
import SanPham from "@/views/Products/SanPham/SanPham.vue";
import AddChiTietSanPham from "@/views/Products/SanPham/AddChiTietSanPham.vue";
import SanPhamChiTiet from "@/views/Products/SanPham/SanPhamChiTiet.vue";

// Phiếu Bảo Hành
import LichSuPhieuBaoHanh from "../views/BaoHanh/LichSuPhieuBaoHanh.vue";
import EditSanPham from "@/views/Products/SanPham/EditSanPham.vue";
import EditSanPhamChiTiet from "@/views/Products/SanPham/EditSanPhamChiTiet.vue";

const routes: Array<RouteRecordRaw> = [
  // Login
  {
    path: "/",
    name: "Login",
    component: Login,
    meta: { layout: "empty", breadcrumb: [] },
  },
  {
    path: "/dang-ky",
    name: "DangKy",
    component: DangKy,
    meta: { layout: "empty", breadcrumb: [] },
  },
  {
    path: "/quen-mk",
    name: "Quenmk",
    component: QuenMk,
    meta: { layout: "empty", breadcrumb: [] },
  },
  {
    path: "/update-mk",
    name: "updatemk",
    component: UpdateMk,
    meta: { layout: "empty", breadcrumb: [] },
  },

  // Dashboard
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
    meta: { breadcrumb: ["Thống kê"] },
  },

  // Bán Hàng
  {
    path: "/ban-hang",
    name: "BanHangTaiQuay",
    component: BanHang,
    meta: { breadcrumb: ["Bán Hàng Tại Quầy"] },
  },

  // Giảm giá
  {
    path: "/phieu-giam-gia",
    name: "Voucher",
    component: Voucher,
    meta: { breadcrumb: ["Phiếu Giảm Giá", "Phiếu giảm giá"] },
  },
  {
    path: "/phieu-bao-hanh",
    name: "PhieuBaoHanh",
    component: PhieuBaoHanh,
    meta: { breadcrumb: ["Bảo hành", "Phiếu bảo hành"] },
  },
  {
    path: "/lich-su-bao-hanh",
    name: "LichSuPhieuBaoHanh",
    component: LichSuPhieuBaoHanh,
    meta: { breadcrumb: ["Bảo hành", "Lịch sử phiếu bảo hành"] },
  },
  {
    path: "/dot-giam-gia",
    name: "DotVoucher",
    component: DotVoucher,
    meta: { breadcrumb: ["Phiếu Giảm Giá", "Đợt giảm giá"] },
  },
  {
    path: "/add-phieu-giam-gia",
    name: "FormAddPgg",
    component: FormAddPgg,
    meta: { breadcrumb: ["Phiếu Giảm Giá", "Phiếu giảm giá", "Thêm phiếu giảm giá"] },
  },
  {
    path: "/update-phieu-giam-gia/:id",
    name: "FormUpdatePgg",
    component: FormUpdatePgg,
    props: true,
    meta: { breadcrumb: (route: any) => ["Phiếu Giảm Giá", "Phiếu giảm giá", `Cập nhật phiếu giảm giá #${route.params.id}`] },
  },
  {
    path: "/ViewAddDotGiamGia",
    name: "ViewAddDotGiamGia",
    component: () => import("@/views/Voucher/ViewAddDotGiamGia.vue"),
    meta: { breadcrumb: ["Phiếu Giảm Giá", "Đợt giảm giá", "Thêm đợt giảm giá"] },
  },

  // Đơn hàng
  {
    path: "/hoa-don",
    name: "HoaDon",
    component: HoaDon,
    meta: { breadcrumb: ["Hóa đơn"] },
  },
  {
    path: "/show-hoa-don/:id",
    name: "ShowHoaDon",
    component: ShowHoaDon,
    props: true,
    meta: { breadcrumb: (route: any) => ["Hóa đơn", `Chi tiết hóa đơn #${route.params.id}`] },
  },
  {
    path: "/lich-su-hoa-don",
    name: "LichSuHD",
    component: LichSuHD,
    meta: { breadcrumb: ["Lịch sử hóa đơn"] },
  },
  {
    path: "/hoa-don-chi-tiet",
    name: "HDCT",
    component: HoaDonChiTiet,
    meta: { breadcrumb: ["Hóa đơn", "Hóa đơn chi tiết"] },
  },

  // Tài khoản
  {
    path: "/nhan-vien",
    name: "NhanVien",
    component: NhanVien,
    meta: { breadcrumb: ["Tài khoản", "Nhân viên"] },
  },
  {
    path: "/them-nhan-vien",
    name: "ThemNhanVien",
    component: EmployeeForm,
    meta: { breadcrumb: ["Tài khoản", "Nhân viên", "Thêm nhân viên"] },
  },
  {
    path: "/update-nhan-vien",
    name: "UpdateNhanVien",
    component: UpdateNhanVien,
    meta: { breadcrumb: ["Tài khoản", "Nhân viên", "Cập nhật nhân viên"] },
  },
  {
    path: "/khach-hang",
    name: "KhachHang",
    component: KhachHang,
    meta: { breadcrumb: ["Tài khoản", "Khách hàng"] },
  },
  {
    path: "/them-khach-hang",
    name: "ThemKhachHang",
    component: CustmerForm,
    meta: { breadcrumb: ["Tài khoản", "Khách hàng", "Thêm khách hàng"] },
  },
  {
    path: "/update-khach-hang",
    name: "UpdateKhachHang",
    component: UpdateCustmer,
    meta: { breadcrumb: ["Tài khoản", "Khách hàng", "Cập nhật khách hàng"] },
  },

  // Sản phẩm
  {
    path: "/products",
    name: "SanPham",
    component: SanPham,
    meta: { breadcrumb: ["Sản phẩm"] },
  },
  {
    path: "/products/add",
    name: "AddProduct",
    component: AddChiTietSanPham,
    meta: { breadcrumb: ["Sản Phẩm", "Thêm Sản Phẩm"] },
  },

  {
    path: '/products/edit/:id',
    name: 'EditSanPham',
    component: EditSanPham,
    meta: { breadcrumb: ["Sản Phẩm", "Sửa Sản Phẩm"] },
  },
  
  {
    path: "/products/:id",
    name: "SanPhamChiTiet",
    component: SanPhamChiTiet,
    meta: { breadcrumb: ["Sản Phẩm", "Chi Tiết Sản Phẩm"] },
  },

  {
    path: '/products/details/:id/edit/:detailId',
    name: 'EditSanPhamChiTiet',
    component: EditSanPhamChiTiet,
    meta: { breadcrumb: ['Sản Phẩm', 'Chi Tiết Sản Phẩm', 'Sửa Chi Tiết Sản Phẩm'] }
  },

  // Thương hiệu
  {
    path: "/manufacturer",
    name: "manufacturer",
    component: Manufacturer,
    meta: { breadcrumb: ["Sản phẩm", "Nhà Sản Xuất"] },
  },
  {
    path: "/manufacturer/add",
    name: "manufacturer-add",
    component: ManufacturerAdd,
    meta: { breadcrumb: ["Sản phẩm", "Nhà Sản Xuất", "Thêm nhà sản xuất"] },
  },
  {
    path: "/manufacturer/edit/:id",
    name: "manufacturer-edit",
    component: ManufacturerEdit,
    meta: { breadcrumb: (route: any) => ["Sản phẩm", "Nhà Sản Xuất", `Cập nhật Nhà Sản Xuất #${route.params.id}`] },
  },

  // Màn hình
  {
    path: "/screens/technology",
    name: "CongNgheManHinh",
    component: CongNgheMH,
    meta: { breadcrumb: ["Sản phẩm", "Công nghệ màn hình"] },
  },
  {
    path: "/screens/technology/add",
    name: "ThemCongNgheManHinh",
    component: AddCongNgheManHinh,
    meta: { breadcrumb: ["Sản phẩm", "Công nghệ màn hình", "Thêm công nghệ màn hình"] },
  },
  {
    path: "/screens/technology/edit/:id",
    name: "SuaCongNgheManHinh",
    component: EditCongNgheManHinh,
    meta: { breadcrumb: (route: any) => ["Sản phẩm", "Công nghệ màn hình", `Cập nhật công nghệ màn hình #${route.params.id}`] },
  },

  // RAM
  {
    path: "/ram",
    name: "RAM",
    component: Ram,
    meta: { breadcrumb: ["Sản phẩm", "RAM"] },
  },

  // Bộ nhớ
  {
    path: "/internal-storage",
    name: "BoNhoTrong",
    component: InternalStorage,
    meta: { breadcrumb: ["Sản phẩm", "Bộ nhớ trong"] },
  },
  {
    path: "/external-storage",
    name: "BoNhoNgoai",
    component: ExternalStorage,
    meta: { breadcrumb: ["Sản phẩm", "Bộ nhớ ngoài"] },
  },

  // CPU & GPU
  {
    path: "/cpu",
    name: "CPU",
    component: CPU,
    meta: { breadcrumb: ["Sản phẩm", "CPU"] },
  },
  {
    path: "/gpu",
    name: "GPU",
    component: GPU,
    meta: { breadcrumb: ["Sản phẩm", "GPU"] },
  },

  // Camera
  {
    path: "/camera-module",
    name: "CumCamera",
    component: CameraModule,
    meta: { breadcrumb: ["Sản phẩm", "Cụm camera"] },
  },

  // Sạc
  {
    path: "/charging/tech-support",
    name: "HoTroCongNgheSac",
    component: ChargingSupport,
    meta: { breadcrumb: ["Sản phẩm", "Hỗ trợ công nghệ sạc"] },
  },
  // Additional Routes from productCategories
  {
    path: "/network-technology",
    name: "CongNgheMang",
    component: () => import("@/views/Products/Other/CongNgheMang.vue"), // Placeholder component
    meta: { breadcrumb: ["Sản phẩm", "Công nghệ mạng"] },
  },
  {
    path: "/operating-system",
    name: "HeDieuHanh",
    component: () => import("@/views/Products/Other/HeDieuHanh.vue"), // Placeholder component
    meta: { breadcrumb: ["Sản phẩm", "Hệ điều hành"] },
  },
  {
    path: "/dust-water-resistance",
    name: "KhangBuiKhangNuoc",
    component: () => import("@/views/Products/Other/KhangBuiNuoc.vue"), // Placeholder component
    meta: { breadcrumb: ["Sản phẩm", "Kháng bụi - Kháng nước"] },
  },
  {
    path: "/color",
    name: "MauSac",
    component: () => import("@/views/Products/Other/MauSac.vue"), // Placeholder component
    meta: { breadcrumb: ["Sản phẩm", "Màu sắc"] },
  },
  {
    path: "/battery",
    name: "Pin",
    component: () => import("@/views/Products/Other/PinSanPham.vue"), // Placeholder component
    meta: { breadcrumb: ["Sản phẩm", "Pin"] },
  },
  {
    path: "/sim",
    name: "Sim",
    component: () => import("@/views/Products/Other/SimSanPham.vue"), // Placeholder component
    meta: { breadcrumb: ["Sản phẩm", "Sim"] },
  },
  {
    path: "/design",
    name: "ThietKe",
    component: () => import("@/views/Products/Other/ThietKe.vue"), // Placeholder component
    meta: { breadcrumb: ["Sản phẩm", "Thiết kế"] },
  },

  // 404 Not Found
  {
    path: "/:pathMatch(.*)*",
    component: NotFound,
    meta: { breadcrumb: ["Không tìm thấy"] },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;