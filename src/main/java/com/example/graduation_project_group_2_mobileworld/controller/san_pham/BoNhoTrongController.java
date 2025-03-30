package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.BoNhoTrongDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.BoNhoTrongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bo-nho-trong")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class BoNhoTrongController {

    private final BoNhoTrongService boNhoTrongService;

    @Autowired
    public BoNhoTrongController(BoNhoTrongService boNhoTrongService) {
        this.boNhoTrongService = boNhoTrongService;
    }

    @GetMapping
    public ResponseEntity<Page<BoNhoTrongDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(boNhoTrongService.getAllBoNhoTrongs(page, size));
    }

    @PostMapping
    public ResponseEntity<BoNhoTrongDTO> addBoNhoTrong(@RequestBody BoNhoTrongDTO boNhoTrongDTO) {
        return ResponseEntity.ok(boNhoTrongService.addBoNhoTrong(boNhoTrongDTO));
    }
}