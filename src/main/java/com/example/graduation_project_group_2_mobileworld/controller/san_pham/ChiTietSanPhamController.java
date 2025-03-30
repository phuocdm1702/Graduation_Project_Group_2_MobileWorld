package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ChiTietSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.ChiTietSanPhamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            ChiTietSanPhamDTO dto = objectMapper.readValue(dtoJson, ChiTietSanPhamDTO.class);
            ChiTietSanPhamService.ChiTietSanPhamResponse response = chiTietSanPhamService.createChiTietSanPham(dto, images);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Tạo chi tiết sản phẩm thành công", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Lỗi khi xử lý ảnh: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Lỗi server: " + e.getMessage(), null));
        }
    }

    record ApiResponse(String message, Object data) {}
}