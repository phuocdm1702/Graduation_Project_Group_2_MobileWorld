package com.example.graduation_project_group_2_mobileworld.controller.hoa_don_controller;

import com.example.graduation_project_group_2_mobileworld.entity.HoaDonChiTiet;
import com.example.graduation_project_group_2_mobileworld.service.hoa_don_service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/hoa-don-chi-tiet")
public class HoaDonChiTietController {
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;
    @GetMapping("/home")
    public List<HoaDonChiTiet> getDataTableHDCT(){
        List<HoaDonChiTiet> listHoaDonChiTiets = hoaDonChiTietService.getAllDataHDCT();
        System.out.println(listHoaDonChiTiets);
        return listHoaDonChiTiets;
    }
}
