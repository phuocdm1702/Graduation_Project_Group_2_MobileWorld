package com.example.graduation_project_group_2_mobileworld.controller.DongSanPhamController;

import com.example.graduation_project_group_2_mobileworld.dto.dongSanPhamDTO.DongSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.service.dongSanPhamService.DongSanPhamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dong-san-pham")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class DongSanPhamController {

    private final DongSanPhamService dongSanPhamService;

    public DongSanPhamController(DongSanPhamService dongSanPhamService) {
        this.dongSanPhamService = dongSanPhamService;
    }

    // Lấy danh sách tất cả dòng sản phẩm
    @GetMapping
    public List<DongSanPhamDTO> getAllDongSanPham() {
        return dongSanPhamService.getAllDongSanPham();
    }

    // Thêm mới dòng sản phẩm (validate đầu vào)
    @PostMapping
    public ResponseEntity<?> createDongSanPham(@Valid @RequestBody DongSanPhamDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        DongSanPhamDTO created = dongSanPhamService.createDongSanPham(dto);
        return ResponseEntity.ok(created);
    }

    // Sửa dòng sản phẩm (validate đầu vào)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDongSanPham(@PathVariable Integer id, @Valid @RequestBody DongSanPhamDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        DongSanPhamDTO updated = dongSanPhamService.updateDongSanPham(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Xóa dòng sản phẩm (bắt lỗi nếu ID không tồn tại)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDongSanPham(@PathVariable Integer id) {
        boolean isDeleted = dongSanPhamService.deleteDongSanPham(id);
        if (isDeleted) {
            return ResponseEntity.ok("Xóa thành công!");
        } else {
            return ResponseEntity.badRequest().body("ID không tồn tại hoặc đã bị xóa trước đó!");
        }
    }

    // Tìm kiếm dòng sản phẩm theo keyword
    @GetMapping("/search")
    public List<DongSanPhamDTO> searchDongSanPham(@RequestParam String keyword) {
        return dongSanPhamService.searchDongSanPham(keyword);
    }
}
