package com.example.graduation_project_group_2_mobileworld.controller.phieu_bao_hanh;

import com.example.graduation_project_group_2_mobileworld.service.phieu_bao_hanh.PhieuBaoHanhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/phieu-bao-hanh")
public class PhieuBaoHanhController {
//    @Autowired
//    private PhieuBaoHanhService phieuBaoHanhService;
//
//    @GetMapping("/home")
//    public ResponseEntity<List<PhieuBaoHanhDTO>> getAllDataTable() {
//        return ResponseEntity.ok(phieuBaoHanhService.getAllDataPBH());
//    }
//
//    @GetMapping("/cbo")
//    public ResponseEntity<List<ImelDaBanDTO>> DataCBO() {
//        return ResponseEntity.ok(phieuBaoHanhService.getDataCBO());
//    }
//
////    @PostMapping
////    public ResponseEntity<PhieuBaoHanhDTO> createPhieuBaoHanh(@RequestBody PhieuBaoHanhDTO dto) {
////        PhieuBaoHanhDTO created = phieuBaoHanhService.creatPhieuBaoHanh(dto);
////        return ResponseEntity.ok(created);
////    }

}
