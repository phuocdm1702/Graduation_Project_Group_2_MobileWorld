package com.example.graduation_project_group_2_mobileworld.controller.ban_hang;

import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.ChiTietGioHangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.ChiTietSPDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.GioHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.nhan_vien.NhanVienRepository;
import com.example.graduation_project_group_2_mobileworld.service.ban_hang_service.BanHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/data")
    public List<HDban_hangDTO> fetchDataHD() {
        List<HDban_hangDTO> listHD = banHangService.getAllHD();
        if (listHD == null) {
            System.out.println("Danh sách hóa đơn trả về null!");
            return Collections.emptyList();
        }
        return listHD;
    }

    @PostMapping("/addHD")
    public ResponseEntity<HoaDon> addHD(@RequestBody HDban_hangDTO hd_dto) {
        if (hd_dto.getMa() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        HoaDon hd = new HoaDon();
        hd.setMa(hd_dto.getMa());
        hd.setTrangThai((short) 0);
        hd.setTienSanPham(BigDecimal.valueOf(0));
        hd.setLoaiDon("Tại quầy");
        hd.setPhiVanChuyen(BigDecimal.valueOf(0));
        hd.setTongTien(BigDecimal.valueOf(0));
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
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/san-pham")
    public List<ChiTietSPDTO> dsSanPham() {
        return banHangService.getAllCTSP();
    }

    @PostMapping("/gio-hang/{gioHangId}/chi-tiet")
    public ResponseEntity<ChiTietGioHangDTO> addCTGH(
            @PathVariable Integer gioHangId,
            @RequestBody Map<String, Integer> requestBody) {
        try {
            Integer chiTietSanPhamId = requestBody.get("id");
            Integer hoaDonId = requestBody.get("hoaDonId");
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
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/gio-hang/{gioHangId}/chi-tiet")
    public List<ChiTietGioHangDTO> getChiTietGioHang(@PathVariable Integer gioHangId) {
        return banHangService.getChiTietGioHangByGioHangId(gioHangId);
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
            return ResponseEntity.status(500).body("Lỗi server: " + e.getMessage());
        }
    }

    @PostMapping("/thanh-toan/{hoaDonId}")
    public ResponseEntity<String> thanhToan(
            @PathVariable Integer hoaDonId,
            @RequestBody Map<String, Object> requestBody) {
        try {
            Long totalPrice = Long.parseLong(requestBody.get("totalPrice").toString());
            Long discount = Long.parseLong(requestBody.get("discount").toString());
            String paymentMethod = requestBody.get("paymentMethod").toString();
            Long tienChuyenKhoan = requestBody.get("tienChuyenKhoan") != null ? Long.parseLong(requestBody.get("tienChuyenKhoan").toString()) : 0L;
            Long tienMat = requestBody.get("tienMat") != null ? Long.parseLong(requestBody.get("tienMat").toString()) : 0L;
            banHangService.thanhToan(hoaDonId, totalPrice, discount, paymentMethod, tienChuyenKhoan, tienMat);
            return ResponseEntity.ok("Thanh toán thành công!");
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi dữ liệu: " + e.getMessage());
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi server: " + e.getMessage());
            return ResponseEntity.status(500).body("Lỗi server: " + e.getMessage());
        }
    }
}