package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CumCameraDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.HoTroCongNgheSacDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.NhaSanXuatDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.HoTroCongNgheSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ho-tro-cong-nghe-sac")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class HoTroCongNgheSacController {

    private final HoTroCongNgheSacService hoTroCongNgheSacService;

    @Autowired
    public HoTroCongNgheSacController(HoTroCongNgheSacService hoTroCongNgheSacService) {
        this.hoTroCongNgheSacService = hoTroCongNgheSacService;
    }

    @GetMapping
    public ResponseEntity<Page<HoTroCongNgheSacDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(hoTroCongNgheSacService.getAllCongNgheSac(page, size));
    }

    @GetMapping("/all")
    public ResponseEntity<List<HoTroCongNgheSacDTO>> getAllThuocTinh() {
        return ResponseEntity.ok(hoTroCongNgheSacService.getAllCongNgheSacList());
    }

}