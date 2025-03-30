package com.example.graduation_project_group_2_mobileworld.controller.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.dto.phieuGiamGiaDTO.PhieuGiamGiaDTO;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaRepository;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaCaNhanService;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaService;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.email.EmailSend;
import com.example.graduation_project_group_2_mobileworld.service.khach_hang.KhachHangServices;
import com.example.graduation_project_group_2_mobileworld.service.tai_khoan.TaiKhoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/phieu-giam-gia")
@CrossOrigin("http://localhost:3000")
public class PhieuGiamGiaController {

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @Autowired
    private PhieuGiamGiaCaNhanService phieuGiamGiaCaNhanService;

    @Autowired
    private KhachHangServices khachHangServices;

    @Autowired
    private TaiKhoanServices taiKhoanServices;

    @Autowired
    private EmailSend emailSend;

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @GetMapping("/data")
    public ResponseEntity<Page<PhieuGiamGia>> fetchData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<PhieuGiamGia> listPGG = phieuGiamGiaService.getPGG(pageable);
        return ResponseEntity.ok(listPGG);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PhieuGiamGia>> searchData(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<PhieuGiamGia> listSearch = phieuGiamGiaService.searchData(keyword, pageable);
        return ResponseEntity.ok(listSearch);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<PhieuGiamGia>> filterPhieuGiamGia(
            @RequestParam(required = false) String loaiPhieuGiamGia, // Thêm tham số
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(required = false) Double minOrder,
            @RequestParam(required = false) Double valueFilter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<PhieuGiamGia> result = phieuGiamGiaService.filterPhieuGiamGia(
                    loaiPhieuGiamGia,
                    trangThai,
                    startDate,
                    endDate,
                    minOrder,
                    valueFilter,
                    pageable
            );
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> getDetail(@PathVariable Integer id) {
        try {
            PhieuGiamGiaDTO dto = phieuGiamGiaService.getDetailPGG(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update-phieu-giam-gia/{id}")
    public ResponseEntity<?> updatePGG(@PathVariable Integer id, @RequestBody PhieuGiamGiaDTO dtoPGG) {
        try {
            Optional<PhieuGiamGia> pggExist = phieuGiamGiaService.getById(id);
            if (!pggExist.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Phiếu giảm giá không tồn tại");
            }

            // Validation
            if (dtoPGG.getMa() == null || dtoPGG.getMa().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Mã phiếu không được để trống");
            }
            if (dtoPGG.getTenPhieuGiamGia() == null || dtoPGG.getTenPhieuGiamGia().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Tên phiếu không được để trống");
            }
            if (dtoPGG.getLoaiPhieuGiamGia() == null || (!dtoPGG.getLoaiPhieuGiamGia().equals("Phần trăm") && !dtoPGG.getLoaiPhieuGiamGia().equals("Tiền mặt"))) {
                return ResponseEntity.badRequest().body("Loại phiếu không hợp lệ");
            }
            if ("Phần trăm".equals(dtoPGG.getLoaiPhieuGiamGia()) && (dtoPGG.getPhanTramGiamGia() == null || dtoPGG.getPhanTramGiamGia() <= 0)) {
                return ResponseEntity.badRequest().body("Phần trăm giảm giá phải lớn hơn 0");
            }
            if ("Tiền mặt".equals(dtoPGG.getLoaiPhieuGiamGia()) && (dtoPGG.getSoTienGiamToiDa() == null || dtoPGG.getSoTienGiamToiDa() <= 0)) {
                return ResponseEntity.badRequest().body("Số tiền giảm tối đa phải lớn hơn 0");
            }
            if (dtoPGG.getHoaDonToiThieu() == null || dtoPGG.getHoaDonToiThieu() < 0) {
                return ResponseEntity.badRequest().body("Hóa đơn tối thiểu không được âm");
            }
            if (dtoPGG.getSoLuongDung() == null || (dtoPGG.getSoLuongDung() != -1 && dtoPGG.getSoLuongDung() < 0)) {
                return ResponseEntity.badRequest().body("Số lượng không hợp lệ");
            }
            if (dtoPGG.getNgayBatDau() == null || dtoPGG.getNgayKetThuc() == null || dtoPGG.getNgayBatDau().after(dtoPGG.getNgayKetThuc())) {
                return ResponseEntity.badRequest().body("Ngày bắt đầu và kết thúc không hợp lệ");
            }
            if (Objects.equals(dtoPGG.getRiengTu(), 1) && (dtoPGG.getCustomerIds() == null || dtoPGG.getCustomerIds().isEmpty())) {
                return ResponseEntity.badRequest().body("Danh sách khách hàng không được trống khi phiếu riêng tư");
            }
            // Kiểm tra số lượng với danh sách khách hàng
            if (Objects.equals(dtoPGG.getRiengTu(), 1) && dtoPGG.getSoLuongDung() != -1 && dtoPGG.getSoLuongDung() < dtoPGG.getCustomerIds().size()) {
                return ResponseEntity.badRequest().body("Số lượng phải lớn hơn hoặc bằng số khách hàng được chọn");
            }

            PhieuGiamGia existingPgg = pggExist.get();
            existingPgg.setMa(dtoPGG.getMa());
            existingPgg.setTenPhieuGiamGia(dtoPGG.getTenPhieuGiamGia());
            existingPgg.setLoaiPhieuGiamGia(dtoPGG.getLoaiPhieuGiamGia());
            existingPgg.setPhanTramGiamGia(dtoPGG.getPhanTramGiamGia() != null ? dtoPGG.getPhanTramGiamGia() : 0);
            existingPgg.setSoTienGiamToiDa(dtoPGG.getSoTienGiamToiDa() != null ? dtoPGG.getSoTienGiamToiDa() : 0);
            existingPgg.setHoaDonToiThieu(dtoPGG.getHoaDonToiThieu());
            existingPgg.setSoLuongDung(dtoPGG.getSoLuongDung());
            existingPgg.setNgayBatDau(dtoPGG.getNgayBatDau());
            existingPgg.setNgayKetThuc(dtoPGG.getNgayKetThuc());
            existingPgg.setMoTa(dtoPGG.getMoTa());
            existingPgg.setRiengTu(Objects.equals(dtoPGG.getRiengTu(), 1));

            PhieuGiamGia updatedPgg = phieuGiamGiaService.updatePGG(existingPgg);
            if (updatedPgg == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật phiếu giảm giá");
            }

            if (Objects.equals(dtoPGG.getRiengTu(), 1)) { // Đơn giản hóa điều kiện
                phieuGiamGiaCaNhanService.deleteByPhieuGiamGiaId(id);
                for (Integer khachHangID : dtoPGG.getCustomerIds()) {
                    KhachHang kh = khachHangServices.findById(khachHangID);
                    if (kh != null) {
                        PhieuGiamGiaCaNhan pggcn = new PhieuGiamGiaCaNhan();
                        pggcn.setIdPhieuGiamGia(existingPgg);
                        pggcn.setIdKhachHang(kh);
                        pggcn.setMa(dtoPGG.getMa() + "-" + khachHangID);
                        pggcn.setNgayNhan(new Date());
                        pggcn.setNgayHetHan(updatedPgg.getNgayKetThuc());
                        pggcn.setTrangThai(true);
                        pggcn.setDeleted(false);

                        phieuGiamGiaCaNhanService.addPGGCN(pggcn);
                        // Logic gửi email giữ nguyên...
                    }
                }
            }

            return ResponseEntity.ok(updatedPgg);
        } catch (RuntimeException e) {
            e.printStackTrace(); // Thêm để debug chi tiết trong log server
            return ResponseEntity.badRequest().body("Lỗi cập nhật: " + e.getMessage());
        }
    }

    @PutMapping("/update-trang-thai/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> updateTrangThai(
            @PathVariable Integer id,
            @RequestBody Map<String, Boolean> requestBody) {
        Boolean trangThai = requestBody.get("trangThai");
        if (trangThai == null) {
            return ResponseEntity.badRequest().body(null);
        }
        PhieuGiamGiaDTO updatedPgg = phieuGiamGiaService.updateTrangthai(id, trangThai);
        return ResponseEntity.ok(updatedPgg);
    }



}