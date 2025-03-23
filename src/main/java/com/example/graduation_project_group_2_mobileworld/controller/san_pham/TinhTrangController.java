package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.GpuDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.TinhTrangDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.GpuService;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.TinhTrangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tinh-trang")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class TinhTrangController {
    private final TinhTrangService service;

    @Autowired
    public TinhTrangController(TinhTrangService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<TinhTrangDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.getAllTinhTrang(page, size));
    }
}