    package com.example.graduation_project_group_2_mobileworld.controller.san_pham.man_hinh;

    import com.example.graduation_project_group_2_mobileworld.dto.san_pham.man_hinh.ManHinhDTO;
    import com.example.graduation_project_group_2_mobileworld.service.san_pham.man_hinh.ManHinhService;
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
    @RequestMapping("/api/man-hinh")
    @CrossOrigin(
            origins = "http://localhost:3000",
            methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
            allowedHeaders = "*",
            allowCredentials = "true",
            maxAge = 3600
    )
    public class ManHinhController {

        private final ManHinhService service;

        public ManHinhController(ManHinhService service) {
            this.service = service;
        }

        @GetMapping
        public ResponseEntity<Page<ManHinhDTO>> getAll(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size) {
            return ResponseEntity.ok(service.getAllManHinh(page, size));
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> getById(@PathVariable Integer id) {
            try {
                ManHinhDTO manHinh = service.getManHinhById(id);
                return ResponseEntity.ok(manHinh);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            }
        }

        @PostMapping
        public ResponseEntity<?> create(
                @Valid @RequestBody ManHinhDTO dto,
                BindingResult result) {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(getErrorMap(result));
            }
            try {
                ManHinhDTO created = service.createManHinh(dto);
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
                ManHinhDTO updated = service.updateManHinh(id, dto);
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
                @RequestParam(required = false) String keyword,
                @RequestParam(required = false) String kieuManHinh,
                @RequestParam(required = false) Integer idCongNgheManHinh,
                @RequestParam(required = false) String kichThuoc,
                @RequestParam(required = false) String doPhanGiai,
                @RequestParam(required = false) String doSangToiDa,
                @RequestParam(required = false) String tanSoQuet,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<ManHinhDTO> result = service.searchManHinh(
                    keyword,
                    kieuManHinh,
                    idCongNgheManHinh,
                    kichThuoc,
                    doPhanGiai,
                    doSangToiDa,
                    tanSoQuet,
                    pageable
            );
            return ResponseEntity.ok(result);
        }

        @GetMapping("/all-kieu-man-hinh")
        public ResponseEntity<List<String>> getAllKieuManHinhNames() {
            return ResponseEntity.ok(service.getAllKieuManHinhNames());
        }

        @GetMapping("/exists/ma")
        public ResponseEntity<Boolean> checkMa(
                @RequestParam String ma,
                @RequestParam(required = false) Integer excludeId) {
            return ResponseEntity.ok(service.existsByMa(ma, excludeId));
        }

        @GetMapping("/exists/kieu-man-hinh")
        public ResponseEntity<Boolean> checkKieuManHinh(
                @RequestParam String kieuManHinh,
                @RequestParam(required = false) Integer excludeId) {
            return ResponseEntity.ok(service.existsByKieuManHinh(kieuManHinh, excludeId));
        }

        private Map<String, String> getErrorMap(BindingResult result) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return errors;
        }
    }