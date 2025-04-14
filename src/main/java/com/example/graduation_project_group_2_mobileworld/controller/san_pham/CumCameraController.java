package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CumCameraDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.GpuDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.CumCameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cum-camera")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class CumCameraController {
    private final CumCameraService cumCameraService;

    @Autowired
    public CumCameraController(CumCameraService cumCameraService) {
        this.cumCameraService = cumCameraService;
    }

    @GetMapping
    public ResponseEntity<Page<CumCameraDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(cumCameraService.getAllCumCamera(page, size));
    }
}