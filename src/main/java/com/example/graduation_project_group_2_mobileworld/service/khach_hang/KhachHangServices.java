package com.example.graduation_project_group_2_mobileworld.service.khach_hang;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhachHangServices {
    private final KhachHangRepository khachHangRepository;

    public KhachHangServices(KhachHangRepository khachHangRepository) {
        this.khachHangRepository = khachHangRepository;
    }


    public List<KhachHang> getAll() {
        return khachHangRepository.findAll();
    }

    public KhachHang addKhachhang(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }

    public ResponseEntity<String> delete(Integer id) {
        khachHangRepository.deleteById(id);
        return null;
    }
}
