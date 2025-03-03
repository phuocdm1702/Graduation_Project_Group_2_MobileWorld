package com.example.graduation_project_group_2_mobileworld.controller.san_pham.man_hinh;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.man_hinh.CongNgheManHinhDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.man_hinh.CongNgheManHinhService;
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
@RequestMapping("/api/cong-nghe-man-hinh")
@CrossOrigin(origins = "http://localhost:3000")
public class CongNgheManHinhController {

    private final CongNgheManHinhService service;

    public CongNgheManHinhController(CongNgheManHinhService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<CongNgheManHinhDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.getAllCongNgheManHinh(page, size));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody CongNgheManHinhDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMap(result));
        }
        try {
            CongNgheManHinhDTO created = service.createCongNgheManHinh(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @Valid @RequestBody CongNgheManHinhDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMap(result));
        }
        try {
            CongNgheManHinhDTO updated = service.updateCongNgheManHinh(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.deleteCongNgheManHinh(id);
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
            service.deleteMultipleCongNgheManHinh(ids);
            return ResponseEntity.ok(Map.of("message", "Xóa " + ids.size() + " công nghệ màn hình thành công!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CongNgheManHinhDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.searchCongNgheManHinh(keyword, page, size));
    }

    @GetMapping("/exists/ma")
    public ResponseEntity<Boolean> checkMa(@RequestParam String ma) {
        return ResponseEntity.ok(service.existsByMa(ma));
    }

    @GetMapping("/exists/cong-nghe-man-hinh")
    public ResponseEntity<Boolean> checkCongNgheManHinh(@RequestParam("congNgheManHinh") String congNgheManHinh) {
        return ResponseEntity.ok(service.existsByCongNgheManHinh(congNgheManHinh));
    }

    @GetMapping("/exists/chuan-man-hinh")
    public ResponseEntity<Boolean> checkChuanManHinh(@RequestParam("chuanManHinh") String chuanManHinh) {
        return ResponseEntity.ok(service.existsByChuanManHinh(chuanManHinh));
    }

    private Map<String, String> getErrorMap(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }
}