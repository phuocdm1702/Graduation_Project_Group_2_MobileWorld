package com.example.graduation_project_group_2_mobileworld.controller.khach_hang;

import com.example.graduation_project_group_2_mobileworld.dto.khach_hang.KhachHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DiaChiKhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.service.khach_hang.DiaChiKhachHangServices;
import com.example.graduation_project_group_2_mobileworld.service.khach_hang.KhachHangServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/khach-hang")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class KhachHangController {

    private final KhachHangServices khachHangServices;
    private final DiaChiKhachHangServices diachiservices;
    @Autowired
    public KhachHangController(KhachHangServices khachHangServices, DiaChiKhachHangServices diachiservices) {
        this.khachHangServices = khachHangServices;
        this.diachiservices = diachiservices;
    }

    // Lấy danh sách tất cả khách hàng
    @GetMapping("/home")
    public ResponseEntity<List<KhachHang>> getAllCustomers() {
        List<KhachHang> customers = khachHangServices.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    // Thêm mới khách hàng
    @PostMapping("/add")
    public ResponseEntity<KhachHang> addKhachHang(@RequestBody KhachHangDTO khachHangDTO) {
        try {
            KhachHang newKhachHang = khachHangServices.addKhachHang(khachHangDTO);
            return ResponseEntity.status(201).body(newKhachHang);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getNhanVienDetail(@PathVariable Integer id) {
        Optional<KhachHang> khachHang = khachHangServices.findByIdKH(id);
        if (khachHang.isPresent()) {
            return ResponseEntity.ok(khachHang.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nhân viên không tồn tại");
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