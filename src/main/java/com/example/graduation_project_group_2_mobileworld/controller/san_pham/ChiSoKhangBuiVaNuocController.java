package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ChiSoKhangBuiVaNuocDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.GpuDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.ChiSoKhangBuiVaNuocService;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.GpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chi-so-khang-bui-va-nuoc")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class ChiSoKhangBuiVaNuocController {
    private final ChiSoKhangBuiVaNuocService service;

    @Autowired
    public ChiSoKhangBuiVaNuocController(ChiSoKhangBuiVaNuocService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ChiSoKhangBuiVaNuocDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.getAllChiSoKhangBuiVaNuoc(page, size));
    }
}