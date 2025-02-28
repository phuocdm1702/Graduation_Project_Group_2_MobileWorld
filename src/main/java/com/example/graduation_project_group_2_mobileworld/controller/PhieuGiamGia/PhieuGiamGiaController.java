package com.example.graduation_project_group_2_mobileworld.controller.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/phieu-giam-gia")
@RestController
@CrossOrigin("*")
public class PhieuGiamGiaController {

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @GetMapping("/data")
    public ResponseEntity<List<PhieuGiamGia>> fetchData() {
        List<PhieuGiamGia> listPGG = phieuGiamGiaService.getPGG();
        return ResponseEntity.ok(listPGG);
    }
}