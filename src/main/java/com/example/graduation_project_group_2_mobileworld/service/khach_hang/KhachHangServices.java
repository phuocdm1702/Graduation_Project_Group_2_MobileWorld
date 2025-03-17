package com.example.graduation_project_group_2_mobileworld.service.khach_hang;

import com.example.graduation_project_group_2_mobileworld.dto.khach_hang.KhachHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.diaChiKhachHangRepo;
import com.example.graduation_project_group_2_mobileworld.repository.tai_khoan.TaiKhoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Date;
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

    public List<KhachHang> searchFormAddPgg(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return khachHangRepository.findAll();
        }
        return khachHangRepository.searchFormAdd(keyword);
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

    public String generateMaKH(String tenKH) {
        String[] parts = tenKH.split("\\s+");

        String lastName = removeAccents(parts[parts.length - 1]).toLowerCase();

        String initials = "";
        for (int i = 0; i < parts.length - 1; i++) {
            if (!parts[i].isEmpty()) {
                initials += removeAccents(parts[i]).toLowerCase().charAt(0);
            }
        }

        String baseCode = lastName + initials;

        String finalCode = baseCode;
        int counter = 1;
        while (khachHangRepository.existsByMa(finalCode)) {
            finalCode = baseCode + String.format("%02d", counter);
            counter++;
        }

        return finalCode;
    }

    // Hàm loại bỏ dấu tiếng Việt
    private String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
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
        if (kh.getCreatedAt() == null) {
            kh.setCreatedAt(new Date()); // Tự động thêm nếu không có
        }
        kh.setMa(generateMaKH(khachHangDTO.getTenKH()));
        kh.setIdTaiKhoan(taiKhoan);
        kh.setTen(khachHangDTO.getTenKH());
        kh.setNgaySinh(khachHangDTO.getNgaySinh());
        kh.setCccd(khachHangDTO.getCccd());
        kh.setDeleted(false);
        kh.setGioiTinh(khachHangDTO.getGioiTinh() != null && khachHangDTO.getGioiTinh() ? (short) 1 : (short) 0);
        kh = khachHangRepository.save(kh); // Lưu trước để có ID

        DiaChiKhachHang dchi = new DiaChiKhachHang();
        dchi.setMa(MaDchi());
        dchi.setThanhPho(khachHangDTO.getThanhPho());
        dchi.setQuan(khachHangDTO.getQuan());
        dchi.setPhuong(khachHangDTO.getPhuong());
        dchi.setDiaChiCuThe(khachHangDTO.getDiaChiCuThe());
        dchi.setIdKhachHang(kh);
        diaChiKhachHangRepo.save(dchi);

        kh.setIdDiaChiKH(dchi); // Gán lại địa chỉ khách hàng vào khachHang
        return khachHangRepository.save(kh);
    }

    public Optional<KhachHang> findByIdKH(Integer id) {
        return khachHangRepository.findById(id);
    }


    public KhachHang updateKhachHang(Integer id, KhachHangDTO khachHangDTO) {
        return khachHangRepository.findById(id)
                .map(existingNhanVien -> {
                    existingNhanVien.setCccd(khachHangDTO.getCccd());
                    existingNhanVien.setNgaySinh(khachHangDTO.getNgaySinh());
                    // Lấy thông tin tài khoản qua idTaiKhoan
                    if (existingNhanVien.getIdTaiKhoan() != null) {
                        TaiKhoan taiKhoan = taiKhoanRepository.findById(existingNhanVien.getIdTaiKhoan().getId())
                                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

                        taiKhoan.setEmail(khachHangDTO.getEmail());
                        taiKhoan.setSoDienThoai(khachHangDTO.getSoDienThoai());
                        taiKhoan.setDeleted(khachHangDTO.getGioiTinh());
                        taiKhoanRepository.save(taiKhoan);
                    }
                    return khachHangRepository.save(existingNhanVien);
                }).orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại!"));
}

    public KhachHang updateDchi(Integer id, KhachHangDTO khachHangDTO) {
        return khachHangRepository.findById(id)
                .map(existingKhachHang -> {
                    // Sửa logic nếu cần: CCCD hay tên khách hàng?
                    existingKhachHang.setTen(khachHangDTO.getTenKH());
                    if (existingKhachHang.getIdDiaChiKH() != null) {
                        DiaChiKhachHang diachi = diaChiKhachHangRepo.findById(existingKhachHang.getIdDiaChiKH().getId())
                                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại"));
                        diachi.setDiaChiCuThe(khachHangDTO.getDiaChiCuThe());
                        diachi.setPhuong(khachHangDTO.getPhuong());
                        diachi.setThanhPho(khachHangDTO.getThanhPho());
                        diachi.setQuan(khachHangDTO.getQuan());
                        diaChiKhachHangRepo.save(diachi);
                    }
                    return khachHangRepository.save(existingKhachHang);
                }).orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
    }


    public KhachHang toggleStatus(Integer id) {
        Optional<KhachHang> optionalKhachHang = khachHangRepository.findById(id);
        if (!optionalKhachHang.isPresent()) {
            throw new RuntimeException("Không tìm thấy khách hàng với ID: " + id);
        }
        KhachHang khachHang = optionalKhachHang.get();
        khachHang.setDeleted(!khachHang.isDeleted()); // Toggle trạng thái
        return khachHangRepository.save(khachHang);
    }
}