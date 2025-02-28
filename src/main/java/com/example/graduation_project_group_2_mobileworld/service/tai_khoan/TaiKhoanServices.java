package com.example.graduation_project_group_2_mobileworld.service.tai_khoan;

import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import com.example.graduation_project_group_2_mobileworld.repository.tai_khoan.TaiKhoanRepository;
import org.springframework.stereotype.Service;

@Service

public class TaiKhoanServices {
    private final TaiKhoanRepository taiKhoanRepository;

    public TaiKhoanServices(TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    public TaiKhoan add(TaiKhoan taiKhoan) {

        return taiKhoanRepository.save(taiKhoan);
    }
}
