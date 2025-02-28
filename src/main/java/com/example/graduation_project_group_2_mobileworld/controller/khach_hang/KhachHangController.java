package com.example.graduation_project_group_2_mobileworld.controller.khach_hang;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.service.khach_hang.KhachHangServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/khach-hang")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class KhachHangController {

    private final KhachHangServices khachHangServices;

    @Autowired
    public KhachHangController(KhachHangServices khachHangServices) {
        this.khachHangServices = khachHangServices;
    }

    // Lấy danh sách tất cả khách hàng
    @GetMapping("/home")
    public ResponseEntity<List<KhachHang>> getAllCustomers() {
        List<KhachHang> customers = khachHangServices.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    // Thêm mới khách hàng
    @PostMapping("/add")
    public ResponseEntity<KhachHang> addKhachHang(@RequestBody KhachHang khachHang) {
        try {
            KhachHang newKhachHang = khachHangServices.addKhachHang(khachHang);
            return ResponseEntity.status(201).body(newKhachHang);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    // Cập nhật khách hàng
    @PutMapping("/update/{id}")
    public ResponseEntity<KhachHang> updateKhachHang(@PathVariable Integer id, @RequestBody KhachHang khachHang) {
        try {
            KhachHang updatedKhachHang = khachHangServices.updateKH(id, khachHang);
            return ResponseEntity.ok(updatedKhachHang);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Xóa khách hàng
    @PutMapping("/delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Integer id) {
        boolean deleted = khachHangServices.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Xóa mềm thành công");
        }
        return ResponseEntity.badRequest().body("Khách hàng không tồn tại");
    }

}