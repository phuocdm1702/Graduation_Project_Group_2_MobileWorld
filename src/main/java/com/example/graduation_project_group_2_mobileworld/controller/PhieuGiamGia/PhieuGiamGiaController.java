package com.example.graduation_project_group_2_mobileworld.controller.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.dto.phieuGiamGiaDTO.PhieuGiamGiaDTO;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/phieu-giam-gia")
@CrossOrigin("http://localhost:3000")
public class PhieuGiamGiaController {

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @GetMapping("/data")
    public ResponseEntity<?> fetchData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<PhieuGiamGia> listPGG = phieuGiamGiaService.getPGG(pageable);
        return ResponseEntity.ok(listPGG);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchData(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<PhieuGiamGia> listSearch = phieuGiamGiaService.searchData(keyword, pageable);
        return ResponseEntity.ok(listSearch);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterPhieuGiamGia(
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
    public ResponseEntity<?> updatePGG(@PathVariable Integer id, @RequestBody PhieuGiamGia phieuGiamGia) {
        try {
            Optional<PhieuGiamGia> pggExist = phieuGiamGiaService.getById(id);
            PhieuGiamGia existingPgg = pggExist.get();
            existingPgg.setMa(phieuGiamGia.getMa());
            existingPgg.setTenPhieuGiamGia(phieuGiamGia.getTenPhieuGiamGia());
            existingPgg.setLoaiPhieuGiamGia(phieuGiamGia.getLoaiPhieuGiamGia());
            existingPgg.setPhanTramGiamGia(phieuGiamGia.getPhanTramGiamGia());
            existingPgg.setSoTienGiamToiDa(phieuGiamGia.getSoTienGiamToiDa());
            existingPgg.setSoLuongDung(phieuGiamGia.getSoLuongDung());
            existingPgg.setHoaDonToiThieu(phieuGiamGia.getHoaDonToiThieu());
            existingPgg.setNgayBatDau(phieuGiamGia.getNgayBatDau());
            existingPgg.setNgayKetThuc(phieuGiamGia.getNgayKetThuc());
            existingPgg.setMoTa(phieuGiamGia.getMoTa());

            phieuGiamGiaService.updatePGG(existingPgg);
            return ResponseEntity.ok(pggExist);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi cập nhật: " + e.getMessage());
        }
    }



}