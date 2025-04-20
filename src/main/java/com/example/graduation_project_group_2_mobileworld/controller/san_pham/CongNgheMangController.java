package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CongNgheManHinhDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CongNgheMangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.GpuDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.CongNgheMangService;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.GpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cong-nghe-mang")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class CongNgheMangController {
    private final CongNgheMangService congNgheMangService;

    @Autowired
    public CongNgheMangController(CongNgheMangService congNgheMangService) {
        this.congNgheMangService = congNgheMangService;
    }

    @GetMapping
    public ResponseEntity<Page<CongNgheMangDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(congNgheMangService.getAllCongNgheMang(page, size));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CongNgheMangDTO>> getAllThuocTinh() {
        return ResponseEntity.ok(congNgheMangService.getAllCongNgheMangList());
    }
}