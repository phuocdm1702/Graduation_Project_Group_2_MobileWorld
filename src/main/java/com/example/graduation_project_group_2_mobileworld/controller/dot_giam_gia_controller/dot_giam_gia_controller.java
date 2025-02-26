package com.example.graduation_project_group_2_mobileworld.controller.dot_giam_gia_controller;

import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.service.dot_giam_gia_service.dot_giam_gia_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/dot_giam_gia")
public class dot_giam_gia_controller {
    @Autowired
    private dot_giam_gia_service sr;

    @GetMapping("/home")
    public List<DotGiamGia> hienThi(Model model) {
        List<DotGiamGia> ds=sr.getAll();
        return ds;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/ViewAddDotGiamGia")
    public List<DongSanPham> hienThiAdd(@RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return sr.getDSP(null); // Trả về toàn bộ danh sách nếu không có từ khóa
        }
        return sr.getDSP(keyword);
    }




}

