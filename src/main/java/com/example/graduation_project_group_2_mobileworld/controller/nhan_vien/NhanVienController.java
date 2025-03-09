package com.example.graduation_project_group_2_mobileworld.controller.nhan_vien;

import com.example.graduation_project_group_2_mobileworld.dto.nhan_vien.NhanVienDTO;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import com.example.graduation_project_group_2_mobileworld.service.nhan_vien.NhanVienServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:8080"})
@RequestMapping("/nhan-vien")
public class NhanVienController {
    private final NhanVienServices nhanVienServices;


    public NhanVienController(NhanVienServices nhanVienServices) {
        this.nhanVienServices = nhanVienServices;
    }

    @GetMapping("/home")
    public ResponseEntity<List<NhanVien>> getAllCustomers() {
        List<NhanVien> kh = nhanVienServices.getall();
        return ResponseEntity.ok(kh);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNhanVien(@RequestBody NhanVienDTO nhanVienDTO) {
        try {
            NhanVien nhanVien = nhanVienServices.addNhanVien(nhanVienDTO);
            return new ResponseEntity<>(nhanVien, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi thêm nhân viên: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Integer id) {
        boolean deleted = nhanVienServices.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Xóa mềm thành công");
        }
        return ResponseEntity.badRequest().body("nv không tồn tại");
    }
//    @PutMapping("/update/{id}")
//    public ResponseEntity<NhanVien> updateNv(@PathVariable Integer id, @RequestBody NhanVien nhanVien) {
//        try {
//            NhanVien updatedKhachHang = nhanVienServices.updatenv(id, nhanVien);
//            return ResponseEntity.ok(updatedKhachHang);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(404).body(null);
//        }
//    }
}
