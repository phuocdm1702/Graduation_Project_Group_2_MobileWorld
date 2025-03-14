package com.example.graduation_project_group_2_mobileworld.service.khach_hang;

import com.example.graduation_project_group_2_mobileworld.entity.DiaChiKhachHang;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.diaChiKhachHangRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiaChiKhachHangServices {
    private final diaChiKhachHangRepo diaChiRepo;


    public DiaChiKhachHangServices(diaChiKhachHangRepo diaChiRepo) {
        this.diaChiRepo = diaChiRepo;
    }

    public Optional<DiaChiKhachHang> findByIdKH(Integer id) {
        return diaChiRepo.findById(id);
    }
}
