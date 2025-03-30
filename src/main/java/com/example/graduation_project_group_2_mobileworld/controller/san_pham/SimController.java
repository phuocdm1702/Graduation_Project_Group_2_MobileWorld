package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.HeDieuHanhDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.SimDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.HeDieuHanhService;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.SimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sim")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class SimController {
    private final SimService simService;

    @Autowired
    public SimController(SimService simService) {
        this.simService = simService;
    }

    @GetMapping
    public ResponseEntity<Page<SimDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(simService.getAllSim(page, size));
    }
}