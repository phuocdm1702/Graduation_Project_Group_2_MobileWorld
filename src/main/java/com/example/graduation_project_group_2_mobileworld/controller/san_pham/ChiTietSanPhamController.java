package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ChiTietSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.ChiTietSanPhamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chi-tiet-san-pham")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class ChiTietSanPhamController {

    private final ChiTietSanPhamService chiTietSanPhamService;
    private final ObjectMapper objectMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public ChiTietSanPhamController(ChiTietSanPhamService chiTietSanPhamService, ObjectMapper objectMapper) {
        this.chiTietSanPhamService = chiTietSanPhamService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createChiTietSanPham(
            @RequestPart("dto") String dtoJson,
            @RequestPart(value = "images", required = true) List<MultipartFile> images) {
        try {
            ChiTietSanPhamDTO dto = objectMapper.readValue(dtoJson, ChiTietSanPhamDTO.class);
            ChiTietSanPhamService.ChiTietSanPhamResponse response = chiTietSanPhamService.createChiTietSanPham(dto, images);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "sanPhamId", response.sanPhamId(),
                    "chiTietSanPhamIds", response.chiTietSanPhamIds(),
                    "anhSanPhamIds", response.anhSanPhamIds()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi khi xử lý ảnh: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi server: " + e.getMessage(), "details", e.toString()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ChiTietSanPhamDTO>> getChiTietSanPhamById(@PathVariable Integer id) {
        List<ChiTietSanPhamDTO> details = chiTietSanPhamService.getChiTietSanPhamBySanPhamId(id);
        return details.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(details);
    }

    @GetMapping("/{id}/price-range")
    public ResponseEntity<Map<String, BigDecimal>> getPriceRange(@PathVariable Integer id) {
        Map<String, BigDecimal> priceRange = chiTietSanPhamService.getPriceRange(id);
        return ResponseEntity.ok(priceRange);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<Page<ChiTietSanPhamDTO>> getChiTietSanPhamDetails(
            @PathVariable Integer id,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer idMauSac,
            @RequestParam(required = false) Integer idBoNhoTrong,
            @RequestParam(required = false) Integer idRam,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ChiTietSanPhamDTO> details = chiTietSanPhamService.getChiTietSanPhamDetails(id, keyword, status, idMauSac, idBoNhoTrong, idRam, minPrice, maxPrice, page, size);
        return ResponseEntity.ok(details);
    }

    @PutMapping("/{id}/update-price")
    public ResponseEntity<Map<String, String>> updatePrice(@PathVariable("id") String idStr, @RequestParam("newPrice") BigDecimal newPrice) {
        Map<String, String> response = new HashMap<>();
        try {
            // Kiểm tra và chuyển đổi id từ String sang Integer
            if (idStr == null || idStr.trim().isEmpty() || "undefined".equals(idStr)) {
                response.put("status", "error");
                response.put("message", "ID sản phẩm không hợp lệ!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Integer id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                response.put("status", "error");
                response.put("message", "ID sản phẩm phải là một số nguyên hợp lệ!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            chiTietSanPhamService.updatePrice(id, newPrice);
            response.put("status", "success");
            response.put("message", "Cập nhật giá thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Không thể cập nhật giá: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}