package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<Page<SanPham>> getAllSanPham(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<SanPham> sanPhams = sanPhamService.getAllSanPham(page, size);
        return ResponseEntity.ok(sanPhams);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SanPham>> searchSanPham(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer idNhaSanXuat,
            @RequestParam(required = false) Integer idHeDieuHanh,
            @RequestParam(required = false) Integer idManHinh,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<SanPham> sanPhams = sanPhamService.searchSanPham(keyword, idNhaSanXuat, idHeDieuHanh, idManHinh, page, size);
        return ResponseEntity.ok(sanPhams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPham> getSanPhamById(@PathVariable Integer id) {
        Optional<SanPham> sanPham = sanPhamService.getSanPhamById(id);
        return sanPham.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteSanPham(@PathVariable Integer id) {
        Optional<SanPham> sanPham = sanPhamService.getSanPhamById(id);
        if (sanPham.isPresent()) {
            sanPhamService.deleteSanPham(id); // Soft delete
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> softDeleteMultipleSanPham(@RequestBody DeleteRequest request) {
        sanPhamService.deleteMultipleSanPham(request.getIds()); // Soft delete multiple
        return ResponseEntity.ok().build();
    }

    // Helper class for bulk delete request
    static class DeleteRequest {
        private List<Integer> ids;

        public List<Integer> getIds() {
            return ids;
        }

        public void setIds(List<Integer> ids) {
            this.ids = ids;
        }
    }
}