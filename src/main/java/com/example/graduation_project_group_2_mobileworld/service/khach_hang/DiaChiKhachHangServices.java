package com.example.graduation_project_group_2_mobileworld.service.khach_hang;

import com.example.graduation_project_group_2_mobileworld.dto.khach_hang.KhachHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DiaChiKhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.diaChiKhachHangRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiaChiKhachHangServices {
    private final diaChiKhachHangRepo diaChiRepo;
    private final KhachHangRepository khachHangRepository;


    public DiaChiKhachHangServices(diaChiKhachHangRepo diaChiRepo, KhachHangRepository khachHangRepository) {
        this.diaChiRepo = diaChiRepo;
        this.khachHangRepository = khachHangRepository;
    }

    public Optional<DiaChiKhachHang> findByIdKH(Integer id) {
        return diaChiRepo.findById(id);
    }

    private String MaDchi() {
        // Logic tạo mã tự động, ví dụ:
        return "DC" + System.currentTimeMillis();
    }



    // Thêm địa chỉ mới
}
