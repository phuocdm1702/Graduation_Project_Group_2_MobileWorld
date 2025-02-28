package com.example.graduation_project_group_2_mobileworld.controller.san_pham.dong_san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.dong_san_pham.DongSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.dong_san_pham.DongSanPhamService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dong-san-pham")
@CrossOrigin(origins = "http://localhost:3000")
public class DongSanPhamController {

    private final DongSanPhamService service;

    public DongSanPhamController(DongSanPhamService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<DongSanPhamDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.getAllDongSanPham(page, size));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody DongSanPhamDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMap(result));
        }
        try {
            DongSanPhamDTO created = service.createDongSanPham(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @Valid @RequestBody DongSanPhamDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMap(result));
        }
        try {
            DongSanPhamDTO updated = service.updateDongSanPham(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.deleteDongSanPham(id);
            return ResponseEntity.ok(Map.of("message", "Xóa thành công!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<?> deleteMultiple(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> ids = request.get("ids");
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Danh sách ID không hợp lệ!"));
        }
        try {
            service.deleteMultipleDongSanPham(ids);
            return ResponseEntity.ok(Map.of("message", "Xóa " + ids.size() + " dòng sản phẩm thành công!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DongSanPhamDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.searchDongSanPham(keyword, page, size));
    }

    @GetMapping("/exists/ma")
    public ResponseEntity<Boolean> checkMa(@RequestParam String ma) {
        return ResponseEntity.ok(service.existsByMa(ma));
    }

    @GetMapping("/exists/dongSanPham")
    public ResponseEntity<Boolean> checkDongSanPham(@RequestParam String dongSanPham) {
        return ResponseEntity.ok(service.existsByDongSanPham(dongSanPham));
    }

    private Map<String, String> getErrorMap(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }
}