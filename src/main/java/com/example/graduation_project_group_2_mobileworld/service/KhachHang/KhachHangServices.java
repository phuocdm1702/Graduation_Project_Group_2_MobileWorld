package com.example.graduation_project_group_2_mobileworld.service.KhachHang;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.repository.KhachHang.KhachHangRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhachHangServices {
    private final KhachHangRepository khachHangRepository;

    public KhachHangServices(KhachHangRepository khachHangRepository) {
        this.khachHangRepository = khachHangRepository;
    }


    public List<KhachHang> getall() {
        return khachHangRepository.findAll();
    }


}
