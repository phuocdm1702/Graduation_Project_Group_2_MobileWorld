package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.HoTroCongNgheSacDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.HoTroCongNgheSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ho-tro-cong-nghe-sac")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class HoTroCongNgheSacController {

    private final HoTroCongNgheSacService hoTroCongNgheSacService;

    @Autowired
    public HoTroCongNgheSacController(HoTroCongNgheSacService hoTroCongNgheSacService) {
        this.hoTroCongNgheSacService = hoTroCongNgheSacService;
    }

    @GetMapping("/details")
    public ResponseEntity<Page<HoTroCongNgheSacDTO>> getChargingTechDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(hoTroCongNgheSacService.getChargingTechDetails(page, size));
    }
}