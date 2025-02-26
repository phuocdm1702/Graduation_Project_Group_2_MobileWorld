package com.example.graduation_project_group_2_mobileworld.controller.phieu_giam_gia_controller;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.service.khach_hang_service.KhachHangService;
import com.example.graduation_project_group_2_mobileworld.service.phieu_giam_gia_service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/phieu-giam-gia")
@RestController
@CrossOrigin("http://localhost:3000/")
public class PhieuGiamGiaController {

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("/data")
    public List<PhieuGiamGia> fetchData() {
        List<PhieuGiamGia> listPGG = phieuGiamGiaService.getPGG();
        List<KhachHang> listKH = khachHangService.getAll();
        System.out.println("Danh sách hóa đơn: " + listPGG);
        return listPGG;
    }

    @PostMapping("/addPhieuGiamGia")
    public PhieuGiamGia addPGG(@ModelAttribute("addPGGForm") PhieuGiamGia phieuGiamGia) {
        return phieuGiamGiaService.addPGG(phieuGiamGia);
    }
}
