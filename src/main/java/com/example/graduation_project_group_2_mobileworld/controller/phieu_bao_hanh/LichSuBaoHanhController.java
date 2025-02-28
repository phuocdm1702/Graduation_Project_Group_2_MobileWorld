package com.example.graduation_project_group_2_mobileworld.controller.phieu_bao_hanh;

import com.example.graduation_project_group_2_mobileworld.dto.phieu_bao_hanh.LichSuBaoHanhDTO;
import com.example.graduation_project_group_2_mobileworld.entity.LichSuBaoHanh;
import com.example.graduation_project_group_2_mobileworld.service.phieu_bao_hanh.LichSuBaoHanhService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/lich-su-bao-hanh")
public class LichSuBaoHanhController {
    private LichSuBaoHanhService lichSuBaoHanhService;

    public LichSuBaoHanhController(LichSuBaoHanhService lichSuBaoHanhService) {
        this.lichSuBaoHanhService = lichSuBaoHanhService;
    }

    @GetMapping("/home")
    public ResponseEntity<List<LichSuBaoHanhDTO>> getAllDataTable() {
        return ResponseEntity.ok(lichSuBaoHanhService.getAllData());
    }
}
