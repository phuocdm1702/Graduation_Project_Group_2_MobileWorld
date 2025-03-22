package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ChiTietSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.AnhSanPham;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.ChiTietSanPhamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chi-tiet-san-pham")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*") // Cho phép CORS nếu frontend chạy trên localhost:3000
public class ChiTietSanPhamController {

    @Autowired
    private ChiTietSanPhamService chiTietSanPhamService;

    @Autowired
    private ObjectMapper objectMapper; // Để parse JSON từ String

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createChiTietSanPham(
            @RequestPart("dto") String dtoJson, // Nhận DTO dưới dạng chuỗi JSON
            @RequestPart(value = "images", required = true) List<MultipartFile> images) { // Nhận danh sách ảnh
        try {
            // Parse DTO từ chuỗi JSON
            ChiTietSanPhamDTO dto = objectMapper.readValue(dtoJson, ChiTietSanPhamDTO.class);

            // Gọi service để tạo ChiTietSanPham
            chiTietSanPhamService.createChiTietSanPham(dto, images);

            // Trả về danh sách ID ảnh vừa upload (tuỳ chọn)
            List<AnhSanPham> savedImages = chiTietSanPhamService.getLastSavedImages(images.size());
            List<Integer> imageIds = savedImages.stream().map(AnhSanPham::getId).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Tạo chi tiết sản phẩm thành công", imageIds));
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
}

// Class phụ để định dạng phản hồi
record ApiResponse(String message, Object data) {}