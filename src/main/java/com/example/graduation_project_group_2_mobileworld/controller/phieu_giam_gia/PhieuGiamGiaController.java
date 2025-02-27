package com.example.graduation_project_group_2_mobileworld.controller.phieu_giam_gia;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.service.khach_hang.KhachHangServices;
import com.example.graduation_project_group_2_mobileworld.service.phieu_giam_gia.PhieuGiamGiaService;
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

    @Autowired
    private KhachHangServices khachHangService;

    @GetMapping("/data")
    public List<PhieuGiamGia> fetchData() {
        List<PhieuGiamGia> listPGG = phieuGiamGiaService.getPGG();
        List<KhachHang> listKH = khachHangService.getAll();
        System.out.println("Danh sách hóa đơn: " + listPGG);
        return listPGG;
    }

    @PostMapping("/addPhieuGiamGia")
    public ResponseEntity<PhieuGiamGia> addPGG(@RequestBody PhieuGiamGia phieuGiamGia) {
        PhieuGiamGia newPgg = phieuGiamGiaService.addPGG(phieuGiamGia);
        return ResponseEntity.ok(newPgg);
    }
}
