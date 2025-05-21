package com.example.graduation_project_group_2_mobileworld.controller.ban_hang;

import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.ChiTietSanPhamGroupDTO;
import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.ThanhToanRequestDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.ChiTietGioHangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.ChiTietSPDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.GioHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.repository.gio_hang.GioHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.nhan_vien.NhanVienRepository;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaCaNhanService;
import com.example.graduation_project_group_2_mobileworld.service.ban_hang_service.BanHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/ban-hang")
@CrossOrigin(origins = "http://localhost:3000")
public class BHController {
    @Autowired
    private BanHangService banHangService;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private PhieuGiamGiaCaNhanService phieuGiamGiaCaNhanService;

    @GetMapping("/gio-hang/by-hoa-don/{hoaDonId}")
    public ResponseEntity<?> getGioHangByHoaDonId(@PathVariable Long hoaDonId) {
        try {
            Optional<GioHang> gioHang = gioHangRepository.findByHoaDonId(hoaDonId);
            return ResponseEntity.ok(gioHang.orElse(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy giỏ hàng: " + e.getMessage());
        }
    }

    @GetMapping("/data")
    public ResponseEntity<List<HDban_hangDTO>> fetchDataHD() {
        try {
            List<HDban_hangDTO> listHD = banHangService.getAllHD();
            return ResponseEntity.ok(listHD != null ? listHD : Collections.emptyList());
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @PostMapping("/addHD")
    public ResponseEntity<HoaDon> addHD(@RequestBody HDban_hangDTO hd_dto) {
        if (hd_dto.getMaHoaDon() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        HoaDon hd = new HoaDon();
        hd.setMa(hd_dto.getMaHoaDon());
        hd.setTrangThai(hd_dto.getTrangThai() != null ? hd_dto.getTrangThai() : (short) 0);
        hd.setTienSanPham(BigDecimal.ZERO);
        hd.setLoaiDon("Tại quầy");
        hd.setPhiVanChuyen(BigDecimal.ZERO);
        hd.setTongTien(BigDecimal.ZERO);
        hd.setTenKhachHang("Khách lẻ");
        hd.setDiaChiKhachHang("N/A");
        hd.setSoDienThoaiKhachHang("0000000000");
        hd.setNgayTao(new Date());
        hd.setDeleted(false);
        hd.setCreatedAt(new Date());

        KhachHang khachHang = khachHangRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng mặc định"));
        hd.setIdKhachHang(khachHang);

        NhanVien nhanVien = nhanVienRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));
        hd.setIdNhanVien(nhanVien);

        HoaDon saveHD = banHangService.addHD(hd);
        return ResponseEntity.ok(saveHD);
    }

    @PostMapping("/addGioHang")
    public ResponseEntity<GioHangDTO> addGioHang(@RequestBody GioHangDTO gioHangDTO) {
        try {
            GioHangDTO result = banHangService.addGioHang(gioHangDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm giỏ hàng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/san-pham")
    public Page<ChiTietSanPhamGroupDTO> getSanPham(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "") String keyword) {
        return banHangService.getAllCTSP(page, size, keyword);
    }

    @GetMapping("/san-pham/{sanPhamId}/imeis")
    public ResponseEntity<List<String>> getIMEIsBySanPhamId(@PathVariable Integer sanPhamId,
                                                            @RequestParam String mauSac,
                                                            @RequestParam String dungLuongRam,
                                                            @RequestParam String dungLuongBoNhoTrong) {
        try {
            List<String> imeis = banHangService.getIMEIsBySanPhamIdAndAttributes(sanPhamId, mauSac, dungLuongRam, dungLuongBoNhoTrong);
            return ResponseEntity.ok(imeis);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách IMEI: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/san-pham/by-imei/{imei}")
    public ResponseEntity<ChiTietSPDTO> getChiTietSanPhamByIMEI(@PathVariable String imei) {
        try {
            ChiTietSanPham chiTietSanPham = banHangService.findChiTietSanPhamByIMEI(imei);
            if (chiTietSanPham == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            ChiTietSPDTO dto = new ChiTietSPDTO();
            dto.setId(chiTietSanPham.getId());
            dto.setIdSanPham(chiTietSanPham.getIdSanPham().getId());
            dto.setTenSanPham(chiTietSanPham.getIdSanPham().getTenSanPham());
            dto.setMa(chiTietSanPham.getMa() != null ? chiTietSanPham.getMa() : "N/A");
            dto.setGiaBan(chiTietSanPham.getGiaBan());
            dto.setImei(imei);
            dto.setMauSac(chiTietSanPham.getIdMauSac() != null ? chiTietSanPham.getIdMauSac().getMauSac() : "N/A");
            dto.setRam(chiTietSanPham.getIdRam() != null ? chiTietSanPham.getIdRam().getDungLuongRam() : "N/A");
            dto.setBoNhoTrong(chiTietSanPham.getIdBoNhoTrong() != null ? chiTietSanPham.getIdBoNhoTrong().getDungLuongBoNhoTrong() : "N/A");
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy chi tiết sản phẩm bằng IMEI: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/gio-hang/{gioHangId}/chi-tiet")
    public ResponseEntity<ChiTietGioHangDTO> addCTGH(
            @PathVariable Integer gioHangId,
            @RequestBody Map<String, Object> requestBody) {
        try {
            Integer chiTietSanPhamId = Integer.parseInt(requestBody.get("idChiTietSanPham").toString());
            Integer hoaDonId = Integer.parseInt(requestBody.get("hoaDonId").toString());
            if (chiTietSanPhamId == null || hoaDonId == null) {
                return ResponseEntity.badRequest().body(null);
            }
            ChiTietGioHangDTO result = banHangService.addChiTietGioHang(gioHangId, chiTietSanPhamId, hoaDonId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.out.println("Lỗi server: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/gio-hang/{gioHangId}/chi-tiet")
    public ResponseEntity<List<ChiTietGioHangDTO>> getChiTietGioHang(@PathVariable Integer gioHangId) {
        try {
            List<ChiTietGioHangDTO> chiTietGioHang = banHangService.getChiTietGioHangByGioHangId(gioHangId);
            return ResponseEntity.ok(chiTietGioHang != null ? chiTietGioHang : Collections.emptyList());
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy chi tiết giỏ hàng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @DeleteMapping("/gio-hang/chi-tiet/{chiTietGioHangId}")
    public ResponseEntity<String> deleteChiTietGioHang(@PathVariable Integer chiTietGioHangId) {
        try {
            banHangService.deleteChiTietGioHang(chiTietGioHangId);
            return ResponseEntity.ok("Xóa chi tiết giỏ hàng thành công!");
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu: " + e.getMessage());
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi server: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
        }
    }

    @PostMapping("/thanh-toan/{hoaDonId}")
    public ResponseEntity<String> thanhToan(
            @PathVariable Integer hoaDonId,
            @RequestBody ThanhToanRequestDTO request) {
        try {
            if (request.getTotalPrice() == null || request.getDiscount() == null) {
                return ResponseEntity.badRequest().body("Tổng tiền và giảm giá không được để trống");
            }
            if (request.getIsDelivery() != null && request.getIsDelivery() && request.getReceiver() != null) {
                ThanhToanRequestDTO.ReceiverDTO receiver = request.getReceiver();
                if (receiver.getName() == null || receiver.getPhone() == null) {
                    return ResponseEntity.badRequest().body("Tên và số điện thoại người nhận không được để trống khi giao hàng");
                }
            }

            banHangService.thanhToan(hoaDonId, request);
            return ResponseEntity.ok("Thanh toán thành công!");
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu: " + e.getMessage());
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi server: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
        }
    }
    @GetMapping("/PGG")
    public List<PhieuGiamGiaCaNhan> getall(){
        return phieuGiamGiaCaNhanService.getall();
    }
    @GetMapping("/by-khach-hang/{idKhachHang}")
    public ResponseEntity<List<PhieuGiamGiaCaNhan>> getByKhachHang(@PathVariable Integer idKhachHang) {
        List<PhieuGiamGiaCaNhan> phieuGiamGias = banHangService.findByKhachHangId(idKhachHang);
        return ResponseEntity.ok(phieuGiamGias);
    }
    @GetMapping("/pgg/check")
    public ResponseEntity<PhieuGiamGiaCaNhan> checkDiscountCode(@RequestParam("ma") String ma) {
        Optional<PhieuGiamGiaCaNhan> optional = phieuGiamGiaCaNhanService.checkDiscountCode(ma);
        return optional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}