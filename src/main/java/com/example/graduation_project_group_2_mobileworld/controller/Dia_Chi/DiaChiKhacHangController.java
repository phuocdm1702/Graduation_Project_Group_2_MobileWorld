package com.example.graduation_project_group_2_mobileworld.controller.Dia_Chi;

import com.example.graduation_project_group_2_mobileworld.dto.DiaChi.DiaChiDTO;
import com.example.graduation_project_group_2_mobileworld.dto.khach_hang.KhachHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DiaChiKhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.service.khach_hang.DiaChiKhachHangServices;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/dia-chi")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class DiaChiKhacHangController {
    private final DiaChiKhachHangServices diachiServices;

    public DiaChiKhacHangController(DiaChiKhachHangServices diachiServices) {
        this.diachiServices = diachiServices;
    }

    @PostMapping("/addDchi")
    public ResponseEntity<DiaChiKhachHang> addDiaChi(@RequestBody DiaChiDTO khachHangDTO) {
        try {
            DiaChiKhachHang diaChi = diachiServices.addDiaChi(khachHangDTO);
            return ResponseEntity.ok(diaChi);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu có
        }
    }

    @GetMapping("/getByKhachHang/{idKhachHang}")
    public ResponseEntity<?> getAllAddressesByKhachHangId(@PathVariable Integer idKhachHang) {
        try {
            List<DiaChiKhachHang> addresses = diachiServices.getAllAddressesByKhachHangId(idKhachHang);
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDiaChi(@PathVariable Integer id) {
        try {
            diachiServices.deleteDiaChi(id);
            return ResponseEntity.ok("Xóa địa chỉ thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    @PutMapping("/updateDchi/{id}")
    public ResponseEntity<?> updateDiaChi(@PathVariable Integer id,@RequestBody DiaChiDTO updatedDiaChiDTO) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (updatedDiaChiDTO == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Dữ liệu địa chỉ không được để trống!");
                return ResponseEntity.badRequest().body(error);
            }
            DiaChiDTO updatedDiaChi = diachiServices.updateDiaChi(id, updatedDiaChiDTO);
            return ResponseEntity.ok(updatedDiaChi);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(404).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Lỗi không xác định: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PutMapping("/setDefault/{id}")
    public ResponseEntity<String> setDefaultAddress(@PathVariable Integer id, @RequestBody DiaChiKhachHang diaChi) {
        try {
            diachiServices.setMacDinh( id, diaChi.getMacDinh());
            return ResponseEntity.ok("Cập nhật địa chỉ mặc định thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật địa chỉ mặc định: " + e.getMessage());
        }
    }
    }
