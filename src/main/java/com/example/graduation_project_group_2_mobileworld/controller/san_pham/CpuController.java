package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CpuDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.PinDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.CpuService;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.PinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cpu")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class CpuController {
    private final CpuService cpuService;

    @Autowired
    public CpuController(CpuService cpuService) {
        this.cpuService = cpuService;
    }

    @GetMapping
    public ResponseEntity<Page<CpuDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(cpuService.getAllCpu(page, size));
    }
}