package com.example.graduation_project_group_2_mobileworld.controller.nhan_vien;

import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import com.example.graduation_project_group_2_mobileworld.service.nhan_vien.NhanVienServices;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:8080"})
@RequestMapping("/nhan-vien")
public class NhanVienController {
    private final NhanVienServices nhanVienServices;


    public NhanVienController(NhanVienServices nhanVienServices) {
        this.nhanVienServices = nhanVienServices;
    }

    //Lay tat ca du lieu
    @GetMapping("/home")
    public List<NhanVien> getAllnv() {
        List<NhanVien> nv = nhanVienServices.getall();
        System.out.println("Danh sách nv: " + nv);
        return nv;
    }
}
