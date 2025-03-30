package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ImelDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.ImelService;
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
@RequestMapping("/imel")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*",
        allowCredentials = "true",
        maxAge = 3600
)
public class ImelController {

    private final ImelService service;

    public ImelController(ImelService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ImelDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.getAllImel(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            ImelDTO imel = service.getImelById(id);
            return ResponseEntity.ok(imel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody ImelDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMap(result));
        }
        try {
            ImelDTO created = service.createImel(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @Valid @RequestBody ImelDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMap(result));
        }
        try {
            ImelDTO updated = service.updateImel(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.deleteImel(id);
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
            service.deleteMultipleImel(ids);
            return ResponseEntity.ok(Map.of("message", "Xóa " + ids.size() + " Imel thành công!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ImelDTO>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String imel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (imel != null && !imel.isEmpty()) {
            return ResponseEntity.ok(service.filterByImel(imel, pageable));
        }
        if (keyword != null && !keyword.isEmpty()) {
            return ResponseEntity.ok(service.searchImel(keyword, page, size));
        }
        return ResponseEntity.ok(service.getAllImel(page, size));
    }

    @GetMapping("/all-names")
    public ResponseEntity<List<String>> getAllImelNames() {
        return ResponseEntity.ok(service.getAllImelNames());
    }

    @GetMapping("/exists/ma")
    public ResponseEntity<Boolean> checkMa(
            @RequestParam String ma,
            @RequestParam(required = false) Integer excludeId) {
        return ResponseEntity.ok(service.existsByMa(ma, excludeId));
    }

    @GetMapping("/exists/imel")
    public ResponseEntity<Boolean> checkImel(
            @RequestParam String imel,
            @RequestParam(required = false) Integer excludeId) {
        return ResponseEntity.ok(service.existsByImel(imel, excludeId));
    }

    private Map<String, String> getErrorMap(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }
}