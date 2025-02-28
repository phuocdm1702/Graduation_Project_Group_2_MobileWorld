package com.example.graduation_project_group_2_mobileworld.service.khach_hang;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
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

    public KhachHang findById(Integer id) {
        Optional<KhachHang> khachHang = khachHangRepository.findById(id);
        return khachHang.orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
    }

    public KhachHang addKhachHang(KhachHang khachHang) {
        if (khachHangRepository.existsByMa(khachHang.getMa())) {
            throw new RuntimeException("Mã khách hàng đã tồn tại: " + khachHang.getMa());
        }
        return khachHangRepository.save(khachHang);
    }

    public KhachHang updateKH(Integer id, KhachHang khachHang) {
        Optional<KhachHang> existingKhachHang = khachHangRepository.findById(id);
        if (existingKhachHang.isPresent()) {
            KhachHang kh = existingKhachHang.get();
            // Kiểm tra trùng mã với các khách hàng khác (trừ chính nó)
            if (khachHangRepository.existsByMaAndNotId(khachHang.getMa(), id)) {
                throw new RuntimeException("Mã khách hàng đã tồn tại: " + khachHang.getMa());
            }
            kh.setMa(khachHang.getMa());
            kh.setTen(khachHang.getTen());
            kh.setGioiTinh(khachHang.getGioiTinh());
            kh.setNgaySinh(khachHang.getNgaySinh());
            return khachHangRepository.save(kh);
        } else {
            throw new RuntimeException("Không tìm thấy khách hàng với ID: " + id);
        }
    }

    public String delete(Integer id) {
        Optional<KhachHang> khachHang = khachHangRepository.findById(id);
        if (khachHang.isPresent()) {
            khachHangRepository.deleteById(id);
            return "Xóa khách hàng thành công!";
        } else {
            throw new RuntimeException("Không tìm thấy khách hàng với ID: " + id);
        }
    }
}