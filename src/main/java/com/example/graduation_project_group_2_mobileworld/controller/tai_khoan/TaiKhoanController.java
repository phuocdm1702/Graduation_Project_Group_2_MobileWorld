package com.example.graduation_project_group_2_mobileworld.controller.tai_khoan;

import com.example.graduation_project_group_2_mobileworld.dto.nhan_vien.NhanVienDTO;
import com.example.graduation_project_group_2_mobileworld.dto.tai_khoan.TaiKhoanDTO;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import com.example.graduation_project_group_2_mobileworld.service.tai_khoan.TaiKhoanServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:8080"})
@RequestMapping("tai-khoan")
public class TaiKhoanController {
    private final TaiKhoanServices taiKhoanServices;

    public TaiKhoanController(TaiKhoanServices taiKhoanServices) {
        this.taiKhoanServices = taiKhoanServices;
    }

    @PostMapping("/add")
    public TaiKhoan addTaiKhoan(@RequestBody TaiKhoan taiKhoan){
        return taiKhoanServices.add(taiKhoan);
    }

    @PostMapping("/addTk")
    public ResponseEntity<?> addNhanVien(@RequestBody TaiKhoanDTO taiKhoanDTO) {
        try {
            TaiKhoan tk = taiKhoanServices.addTK(taiKhoanDTO);
            return new ResponseEntity<>(tk, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi thêm nhân viên: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<TaiKhoan> login(@RequestBody TaiKhoan taiKhoan) {
        TaiKhoan tk = taiKhoanServices.login(taiKhoan.getTenDangNhap(), taiKhoan.getMatKhau());
        if (tk != null) {
            return ResponseEntity.ok(tk); // Trả về 200 với dữ liệu tài khoản
        } else {
            return ResponseEntity.status(401).body(null); // Trả về 401 nếu thất bại
        }
    }

}
