package com.example.graduation_project_group_2_mobileworld.controller.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.dto.khach_hang.KhachHangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.phieuGiamGiaDTO.PhieuGiamGiaDTO;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaCaNhanService;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaService;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.email.EmailSend;
import com.example.graduation_project_group_2_mobileworld.service.khach_hang.KhachHangServices;
import com.example.graduation_project_group_2_mobileworld.service.tai_khoan.TaiKhoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RequestMapping("/add-phieu-giam-gia")
@RestController
@CrossOrigin("http://localhost:3000")
public class AddKhachHangPGGController {

    @Autowired
    private KhachHangServices khachHangService;

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @Autowired
    private PhieuGiamGiaCaNhanService phieuGiamGiaCaNhanService;

    @Autowired
    private TaiKhoanServices taiKhoanServices;

    @Autowired
    private EmailSend emailSend;

    @GetMapping("/data-kh")
    public List<KhachHang> fetchDataKH() {
        List<KhachHang> listKH = khachHangService.getAllCustomers();
        if (listKH == null) {
            System.out.println("Danh sách khách hàng trả về null, trả về mảng rỗng");
            return Collections.emptyList();
        }
        return listKH;
    }

    @GetMapping("/search-kh")
    public ResponseEntity<?> searchKHAddPgg(@RequestParam("keyword") String keyword) {
        List<KhachHang> listSearch = khachHangService.searchFormAddPgg(keyword);
        if (listSearch == null) {
            System.out.println("Danh sách tìm kiếm trả về null, trả về mảng rỗng");
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(listSearch);
    }

    @PostMapping("/addPhieuGiamGia")
    public ResponseEntity<PhieuGiamGia> addPGG(@RequestBody PhieuGiamGiaDTO dtoPGG) {
        try {
            // Validation tương tự updatePGG
            if (dtoPGG.getMa() == null || dtoPGG.getMa().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            if (dtoPGG.getTenPhieuGiamGia() == null || dtoPGG.getTenPhieuGiamGia().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            if (dtoPGG.getLoaiPhieuGiamGia() == null || (!dtoPGG.getLoaiPhieuGiamGia().equals("Phần trăm") && !dtoPGG.getLoaiPhieuGiamGia().equals("Tiền mặt"))) {
                return ResponseEntity.badRequest().body(null);
            }
            if ("Phần trăm".equals(dtoPGG.getLoaiPhieuGiamGia()) && (dtoPGG.getPhanTramGiamGia() == null || dtoPGG.getPhanTramGiamGia() <= 0)) {
                return ResponseEntity.badRequest().body(null);
            }
            if ("Tiền mặt".equals(dtoPGG.getLoaiPhieuGiamGia()) && (dtoPGG.getSoTienGiamToiDa() == null || dtoPGG.getSoTienGiamToiDa() <= 0)) {
                return ResponseEntity.badRequest().body(null);
            }

            PhieuGiamGia pgg = new PhieuGiamGia();
            pgg.setMa(dtoPGG.getMa());
            pgg.setTenPhieuGiamGia(dtoPGG.getTenPhieuGiamGia());
            pgg.setLoaiPhieuGiamGia(dtoPGG.getLoaiPhieuGiamGia());
            pgg.setPhanTramGiamGia(dtoPGG.getPhanTramGiamGia() != null ? dtoPGG.getPhanTramGiamGia() : 0); // Gán mặc định 0 nếu null
            pgg.setSoTienGiamToiDa(dtoPGG.getSoTienGiamToiDa() != null ? dtoPGG.getSoTienGiamToiDa() : 0); // Gán mặc định 0 nếu null
            pgg.setHoaDonToiThieu(dtoPGG.getHoaDonToiThieu());
            pgg.setSoLuongDung(dtoPGG.getSoLuongDung());
            pgg.setNgayBatDau(dtoPGG.getNgayBatDau());
            pgg.setNgayKetThuc(dtoPGG.getNgayKetThuc());
            pgg.setTrangThai(false);
            pgg.setRiengTu(Objects.equals(dtoPGG.getRiengTu(), 1));
            pgg.setMoTa(dtoPGG.getMoTa());
            pgg.setDeleted(false);

            PhieuGiamGia savePgg = phieuGiamGiaService.addPGG(pgg);
            if (savePgg == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            if (Objects.equals(dtoPGG.getRiengTu(), 1) && dtoPGG.getCustomerIds() != null && !dtoPGG.getCustomerIds().isEmpty()) {
                for (Integer khachHangID : dtoPGG.getCustomerIds()) {
                    KhachHang kh = khachHangService.findById(khachHangID);
                    if (kh != null) {
                        PhieuGiamGiaCaNhan pggcn = new PhieuGiamGiaCaNhan();
                        pggcn.setIdPhieuGiamGia(savePgg);
                        pggcn.setIdKhachHang(kh);
                        pggcn.setMa(dtoPGG.getMa() + "-" + khachHangID);
                        pggcn.setNgayNhan(new Date());
                        pggcn.setNgayHetHan(savePgg.getNgayKetThuc());
                        pggcn.setTrangThai(true);
                        pggcn.setDeleted(false);

                        phieuGiamGiaCaNhanService.addPGGCN(pggcn);

                        String email = (kh.getIdTaiKhoan() != null) ? kh.getIdTaiKhoan().getEmail() : null;
                        if (email != null && !email.trim().isEmpty()) {
                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String ngayHH = dateFormat.format(savePgg.getNgayKetThuc());
                                double tinhSTGTD = savePgg.getPhanTramGiamGia() * savePgg.getHoaDonToiThieu() / 100;

                                emailSend.sendDiscountEmail(
                                        email,
                                        pggcn.getMa(),
                                        ngayHH,
                                        savePgg.getPhanTramGiamGia(),
                                        tinhSTGTD
                                );
                            } catch (Exception e) {
                                System.err.println("Failed to send email to " + email + ": " + e.getMessage());
                            }
                        }
                    }
                }
            }

            return ResponseEntity.ok(savePgg);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}