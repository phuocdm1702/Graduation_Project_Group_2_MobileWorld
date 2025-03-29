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

// Sản phẩm
import ProductDetails from "../views/Products/SanPham/ChiTietSanPham.vue";
import ProductDetailAdd from "@/views/Products/SanPham/AddChiTietSanPham.vue";

// Thương hiệu
import Manufacturer from "../views/Products/Brand/Manufacturer/Manufacturer.vue";
import ProductLine from "../views/Products/Brand/ProductLine/ProductLine.vue";
import ProductLineAdd from "@/views/Products/Brand/ProductLine/ProductLineAdd.vue";
import ProductLineEdit from "@/views/Products/Brand/ProductLine/ProductLineEdit.vue";
import ManufacturerAdd from "@/views/Products/Brand/Manufacturer/ManufacturerAdd.vue";
import ManufacturerEdit from "@/views/Products/Brand/Manufacturer/ManufacturerEdit.vue";

// Màn hình
import Display from "../views/Products/Screen/ManHinh.vue";
import DisplayTech from "../views/Products/Screen/CongNgheMH.vue";
import Ram from "../views/Products/MemorySanPham/RamSanPham.vue";
import ManHinhAdd from "@/views/Products/Screen/ManHinhAdd.vue";
import ManHinhEdit from "@/views/Products/Screen/ManHinhEdit.vue";
import AddCongNgheManHinh from "@/views/Products/Screen/AddCongNgheManHinh.vue";
import EditCongNgheManHinh from "@/views/Products/Screen/EditCongNgheManHinh.vue";

// Bộ nhớ
import InternalStorage from "../views/Products/MemorySanPham/BoNhoTrong.vue";
import ExternalStorage from "../views/Products/MemorySanPham/BoNhoNgoai.vue";

// CPU & GPU
import CPU from "../views/Products/Chip/CPU.vue";
import GPU from "../views/Products/Chip/GPU.vue";

// Camera
import FrontCameraSpec from "../views/Products/CameraSanPham/CameraTruoc.vue";
import RearCameraSpec from "../views/Products/CameraSanPham/CameraSau.vue";
import FrontCameraDetail from "../views/Products/CameraSanPham/ChiTietCameraTruoc.vue";
import RearCameraDetail from "../views/Products/CameraSanPham/ChiTietCameraSau.vue";
import CameraModule from "../views/Products/CameraSanPham/CumCamera.vue";

// Sạc
import ChargingTech from "../views/Products/Sac/CongNgheSac.vue";
import ChargingSupport from "../views/Products/Sac/HoTroCongNgheSac.vue";
import Charging from "../views/Products/Sac/HoTroSac.vue";
import ChargingPort from "../views/Products/Sac/CongSac.vue";

// Imel
import Imel from "../views/Products/Imel/Imel.vue";
import ImelDaBan from "../views/Products/Imel/ImelDaBan.vue";
import ImelAdd from "@/views/Products/Imel/ImelAdd.vue";
import ImelEdit from "@/views/Products/Imel/ImelEdit.vue";
import LichSuPhieuBaoHanh from "../views/BaoHanh/LichSuPhieuBaoHanh.vue";
import CongNgheMH from "@/views/Products/Screen/CongNgheMH.vue";
import SanPham from "@/views/Products/SanPham/SanPham.vue";
import AddChiTietSanPham from "@/views/Products/SanPham/AddChiTietSanPham.vue";

const routes: Array<RouteRecordRaw> = [
  // Login
  {
    path: "/",
    name: "Login",
    component: Login,
    meta: { layout: "empty", breadcrumb: [] },
  },

  // Dashboard
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
    meta: { breadcrumb: ["Thống kê"] },
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
    meta: { breadcrumb: ["sản Phẩm", "Thêm Sản Phẩm"] },
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
  {
    path: "/productLines",
    name: "productLines",
    component: ProductLine,
    meta: { breadcrumb: ["Sản phẩm", "Dòng sản phẩm"] },
  },
  {
    path: "/productLines/add",
    name: "productLines-add",
    component: ProductLineAdd,
    meta: { breadcrumb: ["Sản phẩm", "Dòng sản phẩm", "Thêm dòng sản phẩm"] },
  },
  {
    path: "/productLines/edit/:id",
    name: "productLines-edit",
    component: ProductLineEdit,
    meta: { breadcrumb: (route: any) => ["Sản phẩm", "Dòng sản phẩm", `Cập nhật dòng sản phẩm #${route.params.id}`] },
  },

// Màn hình
  {
    path: "/screens",
    name: "screen",
    component: Display,
    meta: { breadcrumb: ["Sản phẩm", "Màn hình"] },
  },
  {
    path: "/screens/add",
    name: "screen-add",
    component: ManHinhAdd,
    meta: { breadcrumb: ["Sản phẩm", "Màn hình", "Thêm màn hình"] },
  },
  {
    path: "/screens/edit/:id",
    name: "screen-edit",
    component: ManHinhEdit,
    meta: { breadcrumb: (route: any) => ["Sản phẩm", "Màn hình", `Cập nhật màn hình #${route.params.id}`] },
  },
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
    path: "/front-camera-specs",
    name: "CameraTruoc",
    component: FrontCameraSpec,
    meta: { breadcrumb: ["Sản phẩm", "Thông số camera trước"] },
  },
  {
    path: "/rear-camera-specs",
    name: "CameraSau",
    component: RearCameraSpec,
    meta: { breadcrumb: ["Sản phẩm", "Thông số camera sau"] },
  },
  {
    path: "/front-camera-details",
    name: "ChiTietCameraTruoc",
    component: FrontCameraDetail,
    meta: { breadcrumb: ["Sản phẩm", "Chi tiết camera trước"] },
  },
  {
    path: "/rear-camera-details",
    name: "ChiTietCameraSau",
    component: RearCameraDetail,
    meta: { breadcrumb: ["Sản phẩm", "Chi tiết camera sau"] },
  },
  {
    path: "/camera-module",
    name: "CumCamera",
    component: CameraModule,
    meta: { breadcrumb: ["Sản phẩm", "Cụm camera"] },
  },

// Sạc
  {
    path: "/charging/technology",
    name: "CongNgheSac",
    component: ChargingTech,
    meta: { breadcrumb: ["Sản phẩm", "Công nghệ sạc"] },
  },
  {
    path: "/charging/tech-support",
    name: "HoTroCongNgheSac",
    component: ChargingSupport,
    meta: { breadcrumb: ["Sản phẩm", "Hỗ trợ công nghệ sạc"] },
  },
  {
    path: "/charging/support",
    name: "HoTroSac",
    component: Charging,
    meta: { breadcrumb: ["Sản phẩm", "Hỗ trợ sạc"] },
  },
  {
    path: "/charging/port",
    name: "CongSac",
    component: ChargingPort,
    meta: { breadcrumb: ["Sản phẩm", "Cổng sạc"] },
  },

// Imel
  {
    path: "/imel",
    name: "imel",
    component: Imel,
    meta: { breadcrumb: ["Sản phẩm", "Imel"] },
  },
  {
    path: "/imel/add",
    name: "imel-add",
    component: ImelAdd,
    meta: { breadcrumb: ["Sản phẩm", "Imel", "Thêm Imel"] },
  },
  {
    path: "/imel/edit/:id",
    name: "imel-edit",
    component: ImelEdit,
    meta: { breadcrumb: (route: any) => ["Sản phẩm", "Imel", `Cập nhật Imel #${route.params.id}`] },
  },
  {
    path: "/imel-sold",
    name: "ImelDaBan",
    component: ImelDaBan,
    meta: { breadcrumb: ["Sản phẩm", "Imel đã bán"] },
  },

  // 404 Not Found
  {
    path: "/:pathMatch(.*)*",
    component: NotFound,
    meta: { breadcrumb: ["Không tìm thấy"] },
  },
  //DangKy
  {
    path: "/dang-ky",
    name: "DangKy",
    component: DangKy,
    meta: { layout: "empty", breadcrumb: [] },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;