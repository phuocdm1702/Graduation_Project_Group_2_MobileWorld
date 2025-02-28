package com.example.graduation_project_group_2_mobileworld.controller.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.service.hoa_don.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/hoa-don")
public class HoaDonController {

    private HoaDonService hoaDonService;

    public HoaDonController(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }

    @GetMapping("/home")
    public ResponseEntity<List<HoaDonDTO>> getAllHoaDon() {
        return ResponseEntity.ok(hoaDonService.getAllData());
    }


}

