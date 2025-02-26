package com.example.graduation_project_group_2_mobileworld.controller.hoa_don_controller;

import com.example.graduation_project_group_2_mobileworld.entity.LichSuHoaDon;
import com.example.graduation_project_group_2_mobileworld.service.hoa_don_service.LichSuHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/lich-su-hoa-don")
public class LichSuHoaDonController {
    @Autowired
    private LichSuHoaDonService lichSuHoaDonService;
    @GetMapping("/home")
    public List<LichSuHoaDon> getDataTableLSHD(){
        List<LichSuHoaDon> listLSHD = lichSuHoaDonService.getAllDataLSHD();
        System.out.println(listLSHD);
        return listLSHD;
    }
}
