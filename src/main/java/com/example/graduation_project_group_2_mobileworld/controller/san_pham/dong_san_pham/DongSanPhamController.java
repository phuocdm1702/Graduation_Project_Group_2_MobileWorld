package com.example.graduation_project_group_2_mobileworld.controller.san_pham.dong_san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.dong_san_pham.DongSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.dong_san_pham.DongSanPhamService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dong-san-pham")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*",
        allowCredentials = "true",
        maxAge = 3600
)
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            DongSanPhamDTO dongSanPham = service.getDongSanPhamById(id);
            return ResponseEntity.ok(dongSanPham);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
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
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String dongSanPham,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (dongSanPham != null && !dongSanPham.isEmpty()) {
            return ResponseEntity.ok(service.filterByDongSanPham(dongSanPham, pageable));
        }
        if (keyword != null && !keyword.isEmpty()) {
            return ResponseEntity.ok(service.searchDongSanPham(keyword, pageable));
        }
        return ResponseEntity.ok(service.getAllDongSanPham(page, size));
    }

    @GetMapping("/all-names")
    public ResponseEntity<List<String>> getAllProductLineNames() {
        return ResponseEntity.ok(service.getAllProductLineNames());
    }

    @GetMapping("/exists/ma")
    public ResponseEntity<Boolean> checkMa(
            @RequestParam String ma,
            @RequestParam(required = false) Integer excludeId) {
        return ResponseEntity.ok(service.existsByMa(ma, excludeId));
    }

    @GetMapping("/exists/dongSanPham")
    public ResponseEntity<Boolean> checkDongSanPham(
            @RequestParam String dongSanPham,
            @RequestParam(required = false) Integer excludeId) {
        return ResponseEntity.ok(service.existsByDongSanPham(dongSanPham, excludeId));
    }

    private Map<String, String> getErrorMap(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }
}