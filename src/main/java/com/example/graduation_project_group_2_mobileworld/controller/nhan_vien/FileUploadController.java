package com.example.graduation_project_group_2_mobileworld.controller.nhan_vien;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@RestController
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:8080"})
@RequestMapping("/img")
public class FileUploadController {
    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Định nghĩa thư mục lưu trữ (thay đổi theo nhu cầu)
            String uploadDir = "src/main/resources/static/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Tạo tên file duy nhất
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // Trả về đường dẫn hoặc tên file
            String imageUrl = "/images/" + fileName; // Đường dẫn tương đối
            return new ResponseEntity<>(new UploadResponse(imageUrl), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DTO cho phản hồi
    static class UploadResponse {
        private String imageUrl;

        public UploadResponse(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
