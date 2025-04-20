package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.MauSacDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mau-sac")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class MauSacController {

    private final MauSacService mauSacService;

    @Autowired
    public MauSacController(MauSacService mauSacService) {
        this.mauSacService = mauSacService;
    }

    @GetMapping
    public ResponseEntity<Page<MauSacDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(mauSacService.getAllMauSac(page, size));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MauSacDTO>> getAllMauSac() {
        return ResponseEntity.ok(mauSacService.getAllMauSacList());
    }

    @PostMapping
    public ResponseEntity<MauSacDTO> addMauSac(@RequestBody MauSacDTO mauSacDTO) {
        return ResponseEntity.ok(mauSacService.addMauSac(mauSacDTO));
    }
}