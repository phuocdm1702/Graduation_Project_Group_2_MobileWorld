package com.example.graduation_project_group_2_mobileworld.controller.khach_hang;

import com.example.graduation_project_group_2_mobileworld.dto.khach_hang.KhachHangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.nhan_vien.NhanVienDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DiaChiKhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
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
    public ResponseEntity<?> updateKhachHang(@PathVariable Integer id, @RequestBody KhachHangDTO khachHangDTO) {
        try {
            KhachHang updatedKhachHang = khachHangServices.updateKhachHang(id, khachHangDTO);
            return ResponseEntity.ok(updatedKhachHang);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping("/updateDchi/{id}")
    public ResponseEntity<?> updateDchi(@PathVariable Integer id, @RequestBody KhachHangDTO khachHangDTO) {
        try {
            KhachHang updateDchi = khachHangServices.updateDchi(id, khachHangDTO);
            return ResponseEntity.ok(updateDchi);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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

    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<?> toggleStatus(@PathVariable Integer id) {
        try {
            KhachHang updatedKhachHang = khachHangServices.toggleStatus(id);
            String message = updatedKhachHang.getDeleted() ? "Đã hủy kích hoạt khách hàng!" : "Đã kích hoạt khách hàng!";
            return ResponseEntity.ok(new ResponseMessage(message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage()));
        }
    }
    @PostMapping("/import")
    public ResponseEntity<ResponseMessage> importKhachHangFromExcel(@RequestBody List<KhachHangDTO> khachHangDTOs) {
        try {
            khachHangServices.importKhachHangFromExcel(khachHangDTOs);
            return ResponseEntity.ok(new ResponseMessage("Nhập dữ liệu từ Excel thành công!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Nhập dữ liệu từ Excel thất bại: " + e.getMessage()));
        }
    }


    // Các endpoint khác như /home, /delete/{id}, v.v.
}

class ResponseMessage {
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }






}