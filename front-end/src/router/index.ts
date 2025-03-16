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
import ProductDetailAdd from "@/views/Products/SanPham/ProductDetailAdd.vue";

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
    meta: { breadcrumb: ["Quản Lý Phiếu Giảm Giá", "Phiếu giảm giá"] },
  },
  {
    path: "/phieu-bao-hanh",
    name: "PhieuBaoHanh",
    component: PhieuBaoHanh,
    meta: { breadcrumb: ["Bảo hành", "Quản lý Phiếu bảo hành"] },
  },
  {
    path: "/lich-su-bao-hanh",
    name: "LichSuPhieuBaoHanh",
    component: LichSuPhieuBaoHanh,
    meta: { breadcrumb: ["Bảo hành", "Quản lý Lịch sử phiếu bảo hành"] },
  },
  {
    path: "/dot-giam-gia",
    name: "DotVoucher",
    component: DotVoucher,
    meta: { breadcrumb: ["Quản Lý Phiếu Giảm Giá", "Đợt giảm giá"] },
  },
  {
    path: "/add-phieu-giam-gia",
    name: "FormAddPgg",
    component: FormAddPgg,
    meta: { breadcrumb: ["Quản Lý Phiếu Giảm Giá", "Phiếu giảm giá", "Thêm phiếu giảm giá"] },
  },
  {
    path: "/update-phieu-giam-gia/:id",
    name: "FormUpdatePgg",
    component: FormUpdatePgg,
    props: true,
    meta: { breadcrumb: (route: any) => ["Quản Lý Phiếu Giảm Giá", "Phiếu giảm giá", `Cập nhật phiếu giảm giá #${route.params.id}`] },
  },
  {
    path: "/ViewAddDotGiamGia",
    name: "ViewAddDotGiamGia",
    component: () => import("@/views/Voucher/ViewAddDotGiamGia.vue"),
    meta: { breadcrumb: ["Quản Lý Phiếu Giảm Giá", "Đợt giảm giá", "Thêm đợt giảm giá"] },
  },

  // Đơn hàng
  {
    path: "/hoa-don",
    name: "HoaDon",
    component: HoaDon,
    meta: { breadcrumb: ["Quản lý hóa đơn"] },
  },
  {
    path: "/show-hoa-don/:id",
    name: "ShowHoaDon",
    component: ShowHoaDon,
    props: true,
    meta: { breadcrumb: (route: any) => ["Quản lý hóa đơn", `Chi tiết hóa đơn #${route.params.id}`] },
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
    meta: { breadcrumb: ["Quản lý hóa đơn", "Hóa đơn chi tiết"] },
  },

  // Tài khoản
  {
    path: "/nhan-vien",
    name: "NhanVien",
    component: NhanVien,
    meta: { breadcrumb: ["Quản lý tài khoản", "Nhân viên"] },
  },
  {
    path: "/them-nhan-vien",
    name: "ThemNhanVien",
    component: EmployeeForm,
    meta: { breadcrumb: ["Quản lý tài khoản", "Nhân viên", "Thêm nhân viên"] },
  },
  {
    path: "/update-nhan-vien",
    name: "UpdateNhanVien",
    component: UpdateNhanVien,
    meta: { breadcrumb: ["Quản lý tài khoản", "Nhân viên", "Cập nhật nhân viên"] },
  },
  {
    path: "/khach-hang",
    name: "KhachHang",
    component: KhachHang,
    meta: { breadcrumb: ["Quản lý tài khoản", "Khách hàng"] },
  },
  {
    path: "/them-khach-hang",
    name: "ThemKhachHang",
    component: CustmerForm,
    meta: { breadcrumb: ["Quản lý tài khoản", "Khách hàng", "Thêm khách hàng"] },
  },
  {
    path: "/update-khach-hang",
    name: "UpdateKhachHang",
    component: UpdateCustmer,
    meta: { breadcrumb: ["Quản lý tài khoản", "Khách hàng", "Cập nhật khách hàng"] },
  },

  // Sản phẩm
  {
    path: "/san-pham-chi-tiet",
    name: "ChiTietSanPham",
    component: ProductDetails,
    meta: { breadcrumb: ["Sản phẩm", "Chi tiết sản phẩm"] },
  },
  {
    path: "/product-detail/add",
    name: "ThemChiTietSanPham",
    component: ProductDetailAdd,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Chi tiết sản phẩm", "Thêm chi tiết sản phẩm"] },
  },
  {
    path: "/add-product",
    name: "AddProduct",
    component: ProductDetailAdd,
    meta: { breadcrumb: ["Quản Lý Chi Tiết Sản Phẩm", "Thêm Sản Phẩm"] },
  },

  // Thương hiệu
  {
    path: "/manufacturer",
    name: "manufacturer",
    component: Manufacturer,
    meta: { breadcrumb: ["Sản phẩm", "Quản Lý Nhà Sản Xuất"] },
  },
  {
    path: "/manufacturer/add",
    name: "manufacturer-add",
    component: ManufacturerAdd,
    meta: { breadcrumb: ["Sản phẩm", "Quản Lý Nhà Sản Xuất", "Thêm nhà sản xuất"] },
  },
  {
    path: "/manufacturer/edit/:id",
    name: "manufacturer-edit",
    component: ManufacturerEdit,
    meta: { breadcrumb: (route: any) => ["Sản phẩm", "Quản Lý Nhà Sản Xuất", `Cập nhật Nhà Sản Xuất #${route.params.id}`] },
  },
  {
    path: "/product-line",
    name: "product-line",
    component: ProductLine,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Dòng sản phẩm"] },
  },
  {
    path: "/product-line/add",
    name: "product-line-add",
    component: ProductLineAdd,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Dòng sản phẩm", "Thêm dòng sản phẩm"] },
  },
  {
    path: "/product-line/edit/:id",
    name: "product-line-edit",
    component: ProductLineEdit,
    meta: { breadcrumb: (route: any) => ["Sản phẩm", "Quản lý Dòng sản phẩm", `Cập nhật dòng sản phẩm #${route.params.id}`] },
  },

  // Màn hình
  {
    path: "/screen",
    name: "screen",
    component: Display,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Màn hình"] },
  },
  {
    path: "/screen/add",
    name: "screen-add",
    component: ManHinhAdd,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Màn hình", "Thêm màn hình"] },
  },
  {
    path: "/screen/edit/:id",
    name: "screen-edit",
    component: ManHinhEdit,
    meta: { breadcrumb: (route: any) => ["Sản phẩm", "Quản lý Màn hình", `Cập nhật màn hình #${route.params.id}`] },
  },
  {
    path: "/man-hinh/cong-nghe",
    name: "CongNgheManHinh",
    component: CongNgheMH,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Công nghệ màn hình"] },
  },
  {
    path: "/man-hinh/cong-nghe/add",
    name: "ThemCongNgheManHinh",
    component: AddCongNgheManHinh,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Công nghệ màn hình", "Thêm công nghệ màn hình"] },
  },
  {
    path: "/man-hinh/cong-nghe/edit/:id",
    name: "SuaCongNgheManHinh",
    component: EditCongNgheManHinh,
    meta: { breadcrumb: (route: any) => ["Sản phẩm", "Quản lý Công nghệ màn hình", `Cập nhật công nghệ màn hình #${route.params.id}`] },
  },
  {
    path: "/ram",
    name: "RAM",
    component: Ram,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý RAM"] },
  },

  // Bộ nhớ
  {
    path: "/bo-nho-trong",
    name: "BoNhoTrong",
    component: InternalStorage,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Bộ nhớ trong"] },
  },
  {
    path: "/bo-nho-ngoai",
    name: "BoNhoNgoai",
    component: ExternalStorage,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Bộ nhớ ngoài"] },
  },

  // CPU & GPU
  {
    path: "/cpu",
    name: "CPU",
    component: CPU,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý CPU"] },
  },
  {
    path: "/gpu",
    name: "GPU",
    component: GPU,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý GPU"] },
  },

  // Camera
  {
    path: "/camera-truoc",
    name: "CameraTruoc",
    component: FrontCameraSpec,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Thông số camera trước"] },
  },
  {
    path: "/camera-sau",
    name: "CameraSau",
    component: RearCameraSpec,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Thông số camera sau"] },
  },
  {
    path: "/chi-tiet-camera-truoc",
    name: "ChiTietCameraTruoc",
    component: FrontCameraDetail,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Chi tiết camera trước"] },
  },
  {
    path: "/chi-tiet-camera-sau",
    name: "ChiTietCameraSau",
    component: RearCameraDetail,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Chi tiết camera sau"] },
  },
  {
    path: "/cum-camera",
    name: "CumCamera",
    component: CameraModule,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Cụm camera"] },
  },

  // Sạc
  {
    path: "/sac/cong-nghe",
    name: "CongNgheSac",
    component: ChargingTech,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Công nghệ sạc"] },
  },
  {
    path: "/sac/ho-tro-cong-nghe",
    name: "HoTroCongNgheSac",
    component: ChargingSupport,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Hỗ trợ công nghệ sạc"] },
  },
  {
    path: "/sac/ho-tro",
    name: "HoTroSac",
    component: Charging,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Hỗ trợ sạc"] },
  },
  {
    path: "/sac/cong-sac",
    name: "CongSac",
    component: ChargingPort,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Cổng sạc"] },
  },

  // Imel
  {
    path: "/imel",
    name: "imel",
    component: Imel,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Imel"] },
  },
  {
    path: "/imel/add",
    name: "imel-add",
    component: ImelAdd,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Imel", "Thêm Imel"] },
  },
  {
    path: "/imel/edit/:id",
    name: "imel-edit",
    component: ImelEdit,
    meta: { breadcrumb: (route: any) => ["Sản phẩm", "Quản lý Imel", `Cập nhật Imel #${route.params.id}`] },
  },
  {
    path: "/imel-da-ban",
    name: "ImelDaBan",
    component: ImelDaBan,
    meta: { breadcrumb: ["Sản phẩm", "Quản lý Imel đã bán"] },
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