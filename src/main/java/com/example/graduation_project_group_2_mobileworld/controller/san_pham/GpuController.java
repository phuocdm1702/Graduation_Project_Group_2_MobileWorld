package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CpuDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.GpuDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.CpuService;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.GpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gpu")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class GpuController {
    private final GpuService gpuService;

    @Autowired
    public GpuController(GpuService gpuService) {
        this.gpuService = gpuService;
    }

    @GetMapping
    public ResponseEntity<Page<GpuDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(gpuService.getAllGpu(page, size));
    }
}