package com.example.graduation_project_group_2_mobileworld.controller.khach_hang;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import com.example.graduation_project_group_2_mobileworld.repository.tai_khoan.TaiKhoanRepository;
import com.example.graduation_project_group_2_mobileworld.service.khach_hang.KhachHangServices;
import com.example.graduation_project_group_2_mobileworld.service.tai_khoan.TaiKhoanServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
      List<KhachHang> khachHangList = khachHangServices.getAll();
      return khachHangList;
    }
    @PostMapping("/add")
    public KhachHang addKhachHang(@RequestBody KhachHang khachHang, TaiKhoan taiKhoan){
        return khachHangServices.addKhachhang(khachHang);
    }
    @DeleteMapping("/delete/{id}")
        public ResponseEntity<String> deleteKhachHang(@PathVariable Integer id){
        return khachHangServices.delete(id);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateKH(@PathVariable Integer id,@RequestBody  KhachHang khachHang){
        KhachHang khachHang1 = khachHangServices.updateKH(id,khachHang);
        return ResponseEntity.ok(khachHang1);
    }
//    @GetMapping()
//    public List<KhachHang> getall(){
//        return khachHangServices.getall();
//    }
}
