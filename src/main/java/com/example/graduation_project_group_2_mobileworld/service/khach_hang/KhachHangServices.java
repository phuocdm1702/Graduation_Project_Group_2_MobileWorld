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

    public List<KhachHang> getAllCustomers() {
        return khachHangRepository.findAllActiveCustomers(); // Chỉ lấy khách hàng chưa bị xóa mềm
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

    public boolean delete(Integer id) {
        Optional<KhachHang> optionalKH = khachHangRepository.findById(id);
        if (optionalKH.isPresent()) {
            KhachHang kh = optionalKH.get();
            kh.setDeleted(true); // Đánh dấu khách hàng đã bị xóa mềm
            khachHangRepository.save(kh);
            return true;
        }
        return false;
    }

}