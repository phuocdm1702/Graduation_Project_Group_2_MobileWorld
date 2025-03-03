package com.example.graduation_project_group_2_mobileworld.controller.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePGG(@PathVariable Integer id) {
        try {
            phieuGiamGiaService.softDelete(id);
            return ResponseEntity.ok("Xóa thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Phiếu giảm giá không tồn tại!");
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