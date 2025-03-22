package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CongNgheSacDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CongSacDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.CongNgheSacService;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.CongSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cong-nghe-sac")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class CongNgheSacController {
    private final CongNgheSacService congSacService ;

    @Autowired
    public CongNgheSacController(CongNgheSacService congSacService) {
        this.congSacService = congSacService;
    }

    @GetMapping
    public ResponseEntity<Page<CongNgheSacDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(congSacService.getAllCongSac(page, size));
    }
}