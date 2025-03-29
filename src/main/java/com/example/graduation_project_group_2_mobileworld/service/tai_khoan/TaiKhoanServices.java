package com.example.graduation_project_group_2_mobileworld.service.tai_khoan;

import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import com.example.graduation_project_group_2_mobileworld.repository.tai_khoan.TaiKhoanRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class TaiKhoanServices {
    private final TaiKhoanRepository taiKhoanRepository;

    public TaiKhoanServices(TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    public TaiKhoan add(TaiKhoan taiKhoan) {
        return taiKhoanRepository.save(taiKhoan);
    }

    public String findById(Integer idTK) {
        Optional<TaiKhoan> tk = taiKhoanRepository.findById(idTK);
        return tk != null ? tk.get().getEmail() : null;
    }

    public TaiKhoan login(String tenDangNhap, String matKhau) {
        TaiKhoan tk = taiKhoanRepository.findByTenDangNhap(tenDangNhap);
        if (tk != null && tk.getMatKhau().equals(matKhau)) {
            return tk; // Trả về tài khoản nếu mật khẩu khớp
        }
        return null; // Trả về null nếu không khớp
    }
}
