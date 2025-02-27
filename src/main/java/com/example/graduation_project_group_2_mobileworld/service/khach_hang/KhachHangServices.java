package com.example.graduation_project_group_2_mobileworld.service.khach_hang;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public KhachHang updateKH(Integer id, KhachHang khachHang) {
        Optional<KhachHang> existingKhachHang = khachHangRepository.findById(id);

        if (existingKhachHang.isPresent()) {
            KhachHang kh = existingKhachHang.get();
            kh.setMa(khachHang.getMa());
            kh.setTen(khachHang.getTen());
            kh.setGioiTinh(khachHang.getGioiTinh());
            kh.setNgaySinh(khachHang.getNgaySinh());

            return khachHangRepository.save(kh);
        } else {
            throw new RuntimeException("Không tìm thấy khách hàng với ID: " + id);
        }
    }
}
