package com.example.graduation_project_group_2_mobileworld.controller.tai_khoan;

import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import com.example.graduation_project_group_2_mobileworld.service.tai_khoan.TaiKhoanServices;
import jakarta.servlet.http.HttpSession;
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
