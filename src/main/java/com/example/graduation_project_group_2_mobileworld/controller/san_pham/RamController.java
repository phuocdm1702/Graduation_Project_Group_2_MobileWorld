package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.RamDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.SimDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.RamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ram")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class RamController {

    private final RamService ramService;

    @Autowired
    public RamController(RamService ramService) {
        this.ramService = ramService;
    }

    @GetMapping
    public ResponseEntity<Page<RamDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(ramService.getAllRams(page, size));
    }

    @PostMapping
    public ResponseEntity<RamDTO> addRam(@RequestBody RamDTO ramDTO) {
        return ResponseEntity.ok(ramService.addRam(ramDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RamDTO>> getAllThuocTinh() {
        return ResponseEntity.ok(ramService.getAllRamsList());
    }
}