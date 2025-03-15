  import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
  // Thống kê
  import Dashboard from "../views/ThongKe/AppDashboard.vue";
  
  // Giảm giá
  import Voucher from "../views/Voucher/AppVoucher.vue";
  import DotVoucher from "../views/Voucher/DotVoucher.vue"
  import FormAddPgg from "../views/Voucher/components/FormAddPgg.vue";
  import PhieuBaoHanh from "../views/BaoHanh/PhieuBaoHanh.vue";
  
  // View Login
  import Login from "../views/LoginView/AppLogin.vue";
  
  // Đơn hàng
  import LichSuHD from "../views/Bill/LichSuHoaDon.vue";
  import HoaDon from "../views/Bill/HoaDon.vue";
  import HoaDonChiTiet from "../views/Bill/HoaDonChiTiet.vue";
  import ShowHoaDon from "../views/Bill/ShowHoaDon.vue"; // Import component mới
  
  
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
  
  // Thương hiệu
  import Manufacturer from "../views/Products/Brand/Manufacturer/Manufacturer.vue";
  import ProductLine from "../views/Products/Brand/ProductLine/ProductLine.vue";
  
  //NhanVien
  
  // Màn hình
  import Display from "../views/Products/Screen/ManHinh.vue";
  import DisplayTech from "../views/Products/Screen/CongNgheMH.vue";
  import Ram from "../views/Products/Screen/RamSanPham.vue";
  
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
  import Imel from "../views/Products/Imel/Imel.vue"
  import ImelDaBan from "../views/Products/Imel/ImelDaBan.vue"
  import LichSuPhieuBaoHanh from "../views/BaoHanh/LichSuPhieuBaoHanh.vue";
  import ProductLineAdd from "@/views/Products/Brand/ProductLine/ProductLineAdd.vue";
  import ProductLineEdit from "@/views/Products/Brand/ProductLine/ProductLineEdit.vue";
  import ManufacturerAdd from "@/views/Products/Brand/Manufacturer/ManufacturerAdd.vue";
  import ManufacturerEdit from "@/views/Products/Brand/Manufacturer/ManufacturerEdit.vue";
  import ImelAdd from "@/views/Products/Imel/ImelAdd.vue";
  import ImelEdit from "@/views/Products/Imel/ImelEdit.vue";
  
  
  const routes: Array<RouteRecordRaw> = [
    { path: "/", name: "Login", component: Login, meta: { layout: "empty" } },
    { path: "/dashboard", name: "Dashboard", component: Dashboard },
  
    // Giam gia
    { path: "/phieu-giam-gia", name: "Voucher", component: Voucher },
    { path: "/phieu-bao-hanh", name: "PhieuBaoHanh", component: PhieuBaoHanh },
    { path: "/lich-su-bao-hanh", name: "LichSuPhieuBaoHanh", component: LichSuPhieuBaoHanh },
    { path: "/dot-giam-gia", name: "DotVoucher", component: DotVoucher },
    { path: "/add-phieu-giam-gia", name: "FormAddPgg", component: FormAddPgg },
  
  
    { path: "/hoa-don", name: "HoaDon", component: HoaDon },
    { path: "/show-hoa-don/:id", name: "ShowHoaDon", component: ShowHoaDon, props: true }, // Thêm :id và props: true
    { path: "/lich-su-hoa-don", name: "LichSuHD", component: LichSuHD },
    { path: "/hoa-don-chi-tiet", name: "HDCT", component: HoaDonChiTiet },
    { path: "/nhan-vien", name: "NhanVien", component: NhanVien },
    { path: "/khach-hang", name: "KhachHang", component: KhachHang },
  
    // Sản phẩm
    { path: "/san-pham/chi-tiet", name: "Chi Tiết Sản Phẩm", component: ProductDetails },
  
    // Thương hiệu
    { path: "/manufacturer", name: "manufacturer", component: Manufacturer },
    { path: "/manufacturer/add", name: "manufacturer-add", component: ManufacturerAdd },
    { path: "/manufacturer/edit/:id", name: "manufacturer-edit", component: ManufacturerEdit },
    { path: "/product-line", name: "product-line", component: ProductLine },
    { path: "/product-line/add", name: "product-line-add", component: ProductLineAdd },
    { path: "/product-line/edit/:id", name: "product-line-edit", component: ProductLineEdit },
  
    // Màn hình
    { path: "/man-hinh", name: "Màn Hình", component: Display },
    { path: "/man-hinh/cong-nghe", name: "Công Nghệ Màn Hình", component: DisplayTech },
    { path: "/ram", name: "RAM", component: Ram },
  
    // Bộ nhớ
    { path: "/bo-nho-trong", name: "Bộ Nhớ Trong", component: InternalStorage },
    { path: "/bo-nho-trong", name: "Bộ Nhớ Trong", component: ExternalStorage },
  
    // CPU & GPU
    { path: "/cpu", name: "CPU", component: CPU },
    { path: "/gpu", name: "GPU", component: GPU },
  
    // Camera
    { path: "/camera-truoc", name: "Thông Số Camera Trước", component: FrontCameraSpec },
    { path: "/camera-sau", name: "Thông Số Camera Sau", component: RearCameraSpec },
    { path: "/chi-tiet-camera-truoc", name: "Chi Tiết Camera Trước", component: FrontCameraDetail },
    { path: "/chi-tiet-camera-sau", name: "Chi Tiết Camera Sau", component: RearCameraDetail },
    { path: "/cum-camera", name: "Cụm Camera", component: CameraModule },
  
    // Sạc
    { path: "/sac/cong-nghe", name: "Công Nghệ Sạc", component: ChargingTech },
    { path: "/sac/ho-tro-cong-nghe", name: "Hỗ Trợ Công Nghệ Sạc", component: ChargingSupport },
    { path: "/sac/ho-tro", name: "Hỗ Trợ Sạc", component: Charging },
    { path: "/sac/cong-sac", name: "Cổng Sạc", component: ChargingPort },
  
    // Imel
    { path: "/imel", name: "imel", component: Imel },
    { path: "/imel/add", name: "imel-add", component: ImelAdd },
    { path: "/imel/edit/:id", name: "imel-edit", component: ImelEdit },
    { path: "/imel-da-ban", name: "Imel đã bán", component: ImelDaBan },
  
    { path: "/:pathMatch(.*)*", component: NotFound },
  
    {
      path: "/ViewAddDotGiamGia",
      name: "ViewAddDotGiamGia",
      component: () => import("@/views/Voucher/ViewAddDotGiamGia.vue"),
    },
    
    { path: '/', component: NhanVien },
    { path: '/them-nhan-vien', component: EmployeeForm },
  
    { path: '/', component: EmployeeForm },
    { path: '/back', component: NhanVien },
  
    { path: '/', component: KhachHang },
    { path: '/them-khach-hang', component: CustmerForm },

    { path: '/', component: NhanVien },
    { path: '/update-nhan-vien', component: UpdateNhanVien },

    { path: '/', component: EmployeeForm },
    { path: '/back', component: NhanVien },
    
    { path: '/', component: UpdateCustmer },
    { path: '/backKH', component: KhachHang },
    
    { path: '/', component: KhachHang },
    { path: '/update-khach-hang', component: UpdateCustmer },
  ];
  
  const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
  });
  
  export default router;
