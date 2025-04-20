package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ChiTietSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ChiTietSanPhamRepository;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.ChiTietSanPhamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.criteria.Predicate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chi-tiet-san-pham")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class ChiTietSanPhamController {

    private final ChiTietSanPhamService chiTietSanPhamService;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final ObjectMapper objectMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public ChiTietSanPhamController(
            ChiTietSanPhamService chiTietSanPhamService,
            ChiTietSanPhamRepository chiTietSanPhamRepository,
            ObjectMapper objectMapper) {
        this.chiTietSanPhamService = chiTietSanPhamService;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
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

    @GetMapping("/{id}/imeis")
    public ResponseEntity<List<Map<String, Object>>> getImeisByVariant(
            @PathVariable Integer id,
            @RequestParam Integer idMauSac,
            @RequestParam Integer idRam,
            @RequestParam Integer idBoNhoTrong,
            @RequestParam BigDecimal giaBan) {
        System.out.println("Received request for IMEIs with params: idSanPham=" + id +
                ", idMauSac=" + idMauSac + ", idRam=" + idRam + ", idBoNhoTrong=" + idBoNhoTrong +
                ", giaBan=" + giaBan);
        Specification<ChiTietSanPham> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("idSanPham").get("id"), id));
            predicates.add(cb.equal(root.get("idMauSac").get("id"), idMauSac));
            predicates.add(cb.equal(root.get("idRam").get("id"), idRam));
            predicates.add(cb.equal(root.get("idBoNhoTrong").get("id"), idBoNhoTrong));
            predicates.add(cb.equal(root.get("giaBan"), giaBan));
            predicates.add(cb.equal(root.get("deleted"), false));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        List<ChiTietSanPham> chiTietSanPhams = chiTietSanPhamRepository.findAll(spec);
        List<Map<String, Object>> imeis = chiTietSanPhams.stream()
                .map(ctsp -> {
                    Map<String, Object> imeiData = new HashMap<>();
                    imeiData.put("id", ctsp.getIdImel().getId());
                    imeiData.put("imei", ctsp.getIdImel().getImel());
                    imeiData.put("ma", ctsp.getMa());
                    imeiData.put("deleted", ctsp.getDeleted());
                    return imeiData;
                })
                .filter(data -> data.get("imei") != null)
                .collect(Collectors.toList());
        System.out.println("Returning IMEIs: " + imeis);
        return ResponseEntity.ok(imeis);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateChiTietSanPham(
            @PathVariable Integer id,
            @RequestBody ChiTietSanPhamDTO dto) {
        Map<String, String> response = new HashMap<>();
        try {
            chiTietSanPhamService.updateChiTietSanPham(id, dto);
            response.put("status", "success");
            response.put("message", "Cập nhật chi tiết sản phẩm thành công!");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Lỗi khi cập nhật chi tiết sản phẩm: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}