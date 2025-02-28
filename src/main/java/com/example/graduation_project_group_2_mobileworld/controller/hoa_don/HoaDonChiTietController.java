package com.example.graduation_project_group_2_mobileworld.controller.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonChiTietDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDonChiTiet;
import com.example.graduation_project_group_2_mobileworld.service.hoa_don.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/hoa-don-chi-tiet")
public class HoaDonChiTietController {

    private HoaDonChiTietService hoaDonChiTietService;

    public HoaDonChiTietController(HoaDonChiTietService hoaDonChiTietService) {
        this.hoaDonChiTietService = hoaDonChiTietService;
    }

    @GetMapping("/home")
    public ResponseEntity<List<HoaDonChiTietDTO>> getDataTableHDCT(){
        return ResponseEntity.ok(hoaDonChiTietService.getAllDataHDCT()) ;
    }
}
