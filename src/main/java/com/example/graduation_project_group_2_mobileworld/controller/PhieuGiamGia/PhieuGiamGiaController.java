package com.example.graduation_project_group_2_mobileworld.controller.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/phieu-giam-gia")
@CrossOrigin("http://localhost:3000")
public class PhieuGiamGiaController {

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @GetMapping("/data")
    public ResponseEntity<List<PhieuGiamGia>> fetchData() {
        List<PhieuGiamGia> listPGG = phieuGiamGiaService.getPGG();
        return ResponseEntity.ok(listPGG);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PhieuGiamGia>> searchData(@RequestParam("keyword") String keyword){
        List<PhieuGiamGia> listSearch = phieuGiamGiaService.searchData(keyword);
        return ResponseEntity.ok(listSearch);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PhieuGiamGia>> filterPhieuGiamGia(
            @RequestParam(required = false) String loaiPhieuGiamGia, // Thêm tham số
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(required = false) Double minOrder,
            @RequestParam(required = false) Double valueFilter) {

        try {
            List<PhieuGiamGia> result = phieuGiamGiaService.filterPhieuGiamGia(
                    loaiPhieuGiamGia, // Thêm vào
                    trangThai,
                    startDate,
                    endDate,
                    minOrder,
                    valueFilter
            );
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/update-trang-thai/{id}")
    public ResponseEntity<String> updateTrangThai(@PathVariable Integer id, @RequestBody Map<String, Boolean> request) {
        try {
            Boolean trangThai = request.get("trangThai");
            if (trangThai == null) {
                return ResponseEntity.badRequest().body("Trạng thái không được để trống!");
            }
            boolean success = phieuGiamGiaService.updateTrangThai(id, trangThai);
            if (success) {
                return ResponseEntity.ok("Cập nhật trạng thái thành công!");
            } else {
                return ResponseEntity.badRequest().body("Phiếu giảm giá không tồn tại!");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi cập nhật trạng thái!");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePGG(@PathVariable Integer id, @RequestBody PhieuGiamGia phieuGiamGia) {
        try {
            phieuGiamGiaService.updatePGG(id, phieuGiamGia);
            return ResponseEntity.ok("Cập nhật thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi cập nhật: " + e.getMessage());
        }
    }



}