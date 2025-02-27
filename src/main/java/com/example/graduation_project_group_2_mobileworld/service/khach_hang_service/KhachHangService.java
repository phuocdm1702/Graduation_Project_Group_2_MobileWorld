package com.example.graduation_project_group_2_mobileworld.service.khach_hang_service;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.repository.khachHang.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    public KhachHangService(KhachHangRepository khachHangRepository) {
        this.khachHangRepository = khachHangRepository;
    }

    public List<KhachHang> getAll() {
        return khachHangRepository.findAll();
    }

    public KhachHang findById(Integer id) {
        return khachHangRepository.findById(id).orElse(null);
    }

}
