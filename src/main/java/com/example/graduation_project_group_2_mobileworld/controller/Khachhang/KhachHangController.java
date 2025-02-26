package com.example.graduation_project_group_2_mobileworld.controller.Khachhang;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.service.KhachHang.KhachHangServices;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:8080"})
@RequestMapping("/khach-hang")
public class KhachHangController {
    private final KhachHangServices khachHangServices;

    public KhachHangController(KhachHangServices khachHangServices) {
        this.khachHangServices = khachHangServices;
    }

    @GetMapping("/home")
    public List<KhachHang> getall(){
      List<KhachHang> khachHangList = khachHangServices.getall();
      return khachHangList;
    }
//    @GetMapping()
//    public List<KhachHang> getall(){
//        return khachHangServices.getall();
//    }
}
