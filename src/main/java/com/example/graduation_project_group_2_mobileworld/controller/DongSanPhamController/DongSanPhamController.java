package com.example.graduation_project_group_2_mobileworld.controller.DongSanPhamController;

import com.example.graduation_project_group_2_mobileworld.dto.dongSanPhamDTO.DongSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.service.dongSanPhamService.DongSanPhamService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dong-san-pham")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class DongSanPhamController {

    private final DongSanPhamService dongSanPhamService;

    public DongSanPhamController(DongSanPhamService dongSanPhamService) {
        this.dongSanPhamService = dongSanPhamService;
    }

    @GetMapping
    public Page<DongSanPhamDTO> getAllDongSanPham(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return dongSanPhamService.getAllDongSanPham(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createDongSanPham(@Valid @RequestBody DongSanPhamDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        DongSanPhamDTO created = dongSanPhamService.createDongSanPham(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDongSanPham(@PathVariable Integer id, @Valid @RequestBody DongSanPhamDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        DongSanPhamDTO updated = dongSanPhamService.updateDongSanPham(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDongSanPham(@PathVariable Integer id) {
        boolean isDeleted = dongSanPhamService.deleteDongSanPham(id);
        if (isDeleted) {
            return ResponseEntity.ok("Xóa thành công!");
        } else {
            return ResponseEntity.badRequest().body("ID không tồn tại hoặc đã bị xóa trước đó!");
        }
    }

    @GetMapping("/search")
    public Page<DongSanPhamDTO> searchDongSanPham(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return dongSanPhamService.searchDongSanPham(keyword, page, size);
    }

    // Kiểm tra mã dòng sản phẩm đã tồn tại chưa
    @GetMapping("/exists/ma")
    public ResponseEntity<Boolean> existsByMa(@RequestParam String ma) {
        boolean exists = dongSanPhamService.existsByMa(ma);
        return ResponseEntity.ok(exists);
    }

    // Kiểm tra tên dòng sản phẩm đã tồn tại chưa
    @GetMapping("/exists/dongSanPham")
    public ResponseEntity<Boolean> existsByDongSanPham(@RequestParam String dongSanPham) {
        boolean exists = dongSanPhamService.existsByDongSanPham(dongSanPham);
        return ResponseEntity.ok(exists);
    }
}
