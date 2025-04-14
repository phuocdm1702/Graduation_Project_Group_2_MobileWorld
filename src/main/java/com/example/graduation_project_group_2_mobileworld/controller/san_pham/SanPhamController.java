package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.SanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.SanPham;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/san-pham")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class SanPhamController {

    private final SanPhamService sanPhamService;

    @Autowired
    public SanPhamController(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;
    }

    @GetMapping
    public ResponseEntity<Page<SanPhamDTO>> getAllSanPham(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<SanPhamDTO> sanPhams = sanPhamService.getAllSanPham(page, size);
        return ResponseEntity.ok(sanPhams);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SanPhamDTO>> searchSanPham(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer idNhaSanXuat,
            @RequestParam(required = false) Integer idHeDieuHanh,
            @RequestParam(required = false) String heDieuHanh,
            @RequestParam(required = false) String phienBan,
            @RequestParam(required = false) Integer idCongNgheManHinh,
            @RequestParam(required = false) String congNgheManHinh,
            @RequestParam(required = false) String chuanManHinh,
            @RequestParam(required = false) Integer idPin,
            @RequestParam(required = false) String loaiPin,
            @RequestParam(required = false) String dungLuongPin,
            @RequestParam(required = false) Integer idCpu,
            @RequestParam(required = false) Integer idGpu,
            @RequestParam(required = false) Integer idCumCamera,
            @RequestParam(required = false) Integer idThietKe,
            @RequestParam(required = false) Integer idSim,
            @RequestParam(required = false) Integer idHoTroCongNgheSac,
            @RequestParam(required = false) Integer idCongNgheMang,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<SanPhamDTO> sanPhams = sanPhamService.searchSanPham(
                keyword, idNhaSanXuat, idHeDieuHanh, heDieuHanh, phienBan,
                idCongNgheManHinh, congNgheManHinh, chuanManHinh,
                idPin, loaiPin, dungLuongPin,
                idCpu, idGpu, idCumCamera, idThietKe, idSim,
                idHoTroCongNgheSac, idCongNgheMang, inStock, page, size
        );
        return ResponseEntity.ok(sanPhams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPham> getSanPhamById(@PathVariable Integer id) {
        Optional<SanPham> sanPham = sanPhamService.getSanPhamById(id);
        return sanPham.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSanPham(@PathVariable Integer id, @RequestBody SanPham sanPham) {
        Optional<SanPham> existingSanPham = sanPhamService.getSanPhamById(id);
        if (existingSanPham.isPresent()) {
            try {
                sanPham.setId(id);
                SanPham updatedSanPham = sanPhamService.updateSanPham(sanPham);
                return ResponseEntity.ok(updatedSanPham);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Dữ liệu không hợp lệ: " + e.getMessage());
            }
        }
        return ResponseEntity.notFound().build();
    }
}