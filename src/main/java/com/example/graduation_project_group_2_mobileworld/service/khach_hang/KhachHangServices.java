package com.example.graduation_project_group_2_mobileworld.service.khach_hang;

import com.example.graduation_project_group_2_mobileworld.dto.khach_hang.KhachHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.diaChiKhachHangRepo;
import com.example.graduation_project_group_2_mobileworld.repository.tai_khoan.TaiKhoanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KhachHangServices {
    private final KhachHangRepository khachHangRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final diaChiKhachHangRepo diaChiKhachHangRepo;
    public KhachHangServices(KhachHangRepository khachHangRepository, TaiKhoanRepository taiKhoanRepository, com.example.graduation_project_group_2_mobileworld.repository.khach_hang.diaChiKhachHangRepo diaChiKhachHangRepo) {
        this.khachHangRepository = khachHangRepository;
        this.taiKhoanRepository = taiKhoanRepository;
        this.diaChiKhachHangRepo = diaChiKhachHangRepo;
    }

    public List<KhachHang> getAllCustomers() {
        return khachHangRepository.findAllActiveCustomers(); // Chỉ lấy khách hàng chưa bị xóa mềm
    }
    public KhachHang findById(Integer id) {
        Optional<KhachHang> khachHang = khachHangRepository.findById(id);
        return khachHang.orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
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
    public String MaTaiKhoan() {
        String maxMaTK = taiKhoanRepository.findMaxMaTK();
        if (maxMaTK != null) {
            int nextNumber = Integer.parseInt(maxMaTK.substring(2)) + 1;
            return String.format("TK%05d", nextNumber);
        } else {
            return "TK00001";
        }
    }

    public String MaDchi() {
        Integer maxMa = diaChiKhachHangRepo.findMaxMa();
        if (maxMa == null) {
            return "DCKH00001";
        }
        int nextNumber = maxMa + 1;
        return String.format("DCKH%05d", nextNumber);
    }

    public String generateMaKH() {
        String maxMa = khachHangRepository.findMaxMa();
        if (maxMa == null) {
            return "KH00001";
        }
        String numberPart = maxMa.substring(2);
        int nextNumber = Integer.parseInt(numberPart) + 1; // 2
        return String.format("KH%05d", nextNumber);
    }

    public KhachHang addKhachHang(KhachHangDTO khachHangDTO) {
        QuyenHan quyenHan = new QuyenHan();
        quyenHan.setId(2); // Quyền khách hàng

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setMa(MaTaiKhoan());
        taiKhoan.setEmail(khachHangDTO.getEmail());
        taiKhoan.setSoDienThoai(khachHangDTO.getSoDienThoai());
        taiKhoan.setTenDangNhap(khachHangDTO.getUserName());
        taiKhoan.setIdQuyenHan(quyenHan);
        taiKhoan.setDeleted(false);
        taiKhoan = taiKhoanRepository.save(taiKhoan);

        KhachHang kh = new KhachHang();
        kh.setMa(generateMaKH());
        kh.setIdTaiKhoan(taiKhoan);
        kh.setTen(khachHangDTO.getTenKH());
        kh.setNgaySinh(khachHangDTO.getNgaySinh());
        kh.setCccd(khachHangDTO.getCccd());
        kh.setDeleted(false);
        kh.setGioiTinh(khachHangDTO.getGioiTinh() != null && khachHangDTO.getGioiTinh() ? (short) 1 : (short) 0);
        kh = khachHangRepository.save(kh);

        DiaChiKhachHang dchi = new DiaChiKhachHang();
        dchi.setThanhPho(khachHangDTO.getThanhPho());
        dchi.setQuan(khachHangDTO.getQuan());
        dchi.setPhuong(khachHangDTO.getPhuong());
        dchi.setDiaChiCuThe(khachHangDTO.getDiaChiCuThe());
        dchi.setMa(MaDchi());
        dchi.setIdKhachHang(kh);
        diaChiKhachHangRepo.save(dchi);

        return khachHangRepository.save(kh);
    }
}