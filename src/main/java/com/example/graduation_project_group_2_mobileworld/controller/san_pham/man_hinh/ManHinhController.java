package com.example.graduation_project_group_2_mobileworld.controller.san_pham.man_hinh;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.man_hinh.ManHinhDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.man_hinh.ManHinhService;
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
@RequestMapping("/api/man-hinh")
@CrossOrigin(origins = "http://localhost:3000")
public class ManHinhController {

    private final ManHinhService service;

    public ManHinhController(ManHinhService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ManHinhDTO>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size) {
        return ResponseEntity.ok(service.getAllManHinh(page, size));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody ManHinhDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMap(result));
        }
        try {
            // Gọi service.createManHinh với CongNgheManHinhRepository được inject tự động qua Spring
            ManHinhDTO created = service.createManHinh(dto, null); // null vì repository sẽ được inject trong service
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @Valid @RequestBody ManHinhDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMap(result));
        }
        try {
            ManHinhDTO updated = service.updateManHinh(id, dto, null); // null vì repository sẽ được inject trong service
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.deleteManHinh(id);
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
            service.deleteMultipleManHinh(ids);
            return ResponseEntity.ok(Map.of("message", "Xóa " + ids.size() + " màn hình thành công!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ManHinhDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size) {
        return ResponseEntity.ok(service.searchManHinh(keyword, page, size));
    }

    @GetMapping("/exists/ma")
    public ResponseEntity<Boolean> checkMa(@RequestParam String ma) {
        return ResponseEntity.ok(service.existsByMa(ma));
    }

    @GetMapping("/exists/kieu-man-hinh")
    public ResponseEntity<Boolean> checkKieuManHinh(@RequestParam("kieuManHinh") String kieuManHinh) {
        return ResponseEntity.ok(service.existsByKieuManHinh(kieuManHinh));
    }

    private Map<String, String> getErrorMap(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }
}