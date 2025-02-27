package com.example.graduation_project_group_2_mobileworld.controller.san_pham.nha_san_xuat;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.nha_san_xuat.NhaSanXuatDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.nha_san_xuat.NhaSanXuatService;
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
@RequestMapping("/api/nha-san-xuat")
@CrossOrigin(origins = "*")
public class NhaSanXuatController {

    private final NhaSanXuatService service;

    public NhaSanXuatController(NhaSanXuatService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<NhaSanXuatDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.getAllNhaSanXuat(page, size));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody NhaSanXuatDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMap(result));
        }
        try {
            NhaSanXuatDTO created = service.createNhaSanXuat(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @Valid @RequestBody NhaSanXuatDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMap(result));
        }
        try {
            NhaSanXuatDTO updated = service.updateNhaSanXuat(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.deleteNhaSanXuat(id);
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
            service.deleteMultipleNhaSanXuat(ids);
            return ResponseEntity.ok(Map.of("message", "Xóa " + ids.size() + " nhà sản xuất thành công!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NhaSanXuatDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.searchNhaSanXuat(keyword, page, size));
    }

    @GetMapping("/exists/ma")
    public ResponseEntity<Boolean> checkMa(@RequestParam String ma) {
        return ResponseEntity.ok(service.existsByMa(ma));
    }

    @GetMapping("/exists/nhaSanXuat")
    public ResponseEntity<Boolean> checkNhaSanXuat(@RequestParam String nhaSanXuat) {
        return ResponseEntity.ok(service.existsByNhaSanXuat(nhaSanXuat));
    }

    private Map<String, String> getErrorMap(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }
}