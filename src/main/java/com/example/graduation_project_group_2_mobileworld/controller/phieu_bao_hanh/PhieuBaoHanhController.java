package com.example.graduation_project_group_2_mobileworld.controller.phieu_bao_hanh;

import com.example.graduation_project_group_2_mobileworld.dto.phieu_bao_hanh.PhieuBaoHanhDTO;
import com.example.graduation_project_group_2_mobileworld.service.phieu_bao_hanh.PhieuBaoHanhService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/phieu-bao-hanh")
public class PhieuBaoHanhController {

    private PhieuBaoHanhService phieuBaoHanhService;

    public PhieuBaoHanhController(PhieuBaoHanhService phieuBaoHanhService) {
        this.phieuBaoHanhService = phieuBaoHanhService;
    }

    @GetMapping("/home")
    public ResponseEntity<List<PhieuBaoHanhDTO>> getAllDataTable(){
        return ResponseEntity.ok(phieuBaoHanhService.getAllDataPBH());
    }
}
