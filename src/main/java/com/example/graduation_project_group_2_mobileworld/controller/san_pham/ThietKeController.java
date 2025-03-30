package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.SimDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ThietKeDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.SimService;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.ThietKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/thiet-ke")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class ThietKeController {
    private final ThietKeService thietKeService;

    @Autowired
    public ThietKeController(ThietKeService thietKeService) {
        this.thietKeService = thietKeService;
    }

    @GetMapping
    public ResponseEntity<Page<ThietKeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(thietKeService.getAllThietKe(page, size));
    }
}