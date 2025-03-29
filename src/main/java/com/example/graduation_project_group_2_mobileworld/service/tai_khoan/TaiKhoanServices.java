package com.example.graduation_project_group_2_mobileworld.service.tai_khoan;

import com.example.graduation_project_group_2_mobileworld.dto.tai_khoan.TaiKhoanDTO;
import com.example.graduation_project_group_2_mobileworld.entity.QuyenHan;
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

    public String MaTaiKhoan() {
        String maxMaTK = taiKhoanRepository.findMaxMaTK();
        if (maxMaTK != null) {
            int nextNumber = Integer.parseInt(maxMaTK.substring(2)) + 1;
            return String.format("TK%05d", nextNumber);
        } else {
            return "TK00001";
        }
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

    public TaiKhoan addTK(TaiKhoanDTO taiKhoanDTO) {
        QuyenHan quyenHan = new QuyenHan();
        quyenHan.setId(2);
        //taikhoan
        TaiKhoan taiKhoan1 = new TaiKhoan();
        taiKhoan1.setIdQuyenHan(quyenHan);
        taiKhoan1.setMa(MaTaiKhoan());
        taiKhoan1.setTenDangNhap(taiKhoanDTO.getTenDangNhap());
        taiKhoan1.setMatKhau(taiKhoanDTO.getMatKhau());
        taiKhoan1.setDeleted(false);
        return taiKhoanRepository.save(taiKhoan1);
    }
}
