package com.example.graduation_project_group_2_mobileworld.service.khach_hang;

import com.example.graduation_project_group_2_mobileworld.dto.khach_hang.KhachHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.DiaChiKhachHangRepo;
import com.example.graduation_project_group_2_mobileworld.repository.tai_khoan.TaiKhoanRepository;
import com.example.graduation_project_group_2_mobileworld.service.nhan_vien.EmailServices;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KhachHangServices {
    private final KhachHangRepository khachHangRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final DiaChiKhachHangRepo diaChiKhachHangRepo;
    private final EmailServices emailServices;
    public KhachHangServices(KhachHangRepository khachHangRepository, TaiKhoanRepository taiKhoanRepository, DiaChiKhachHangRepo diaChiKhachHangRepo, EmailServices emailServices) {
        this.khachHangRepository = khachHangRepository;
        this.taiKhoanRepository = taiKhoanRepository;
        this.diaChiKhachHangRepo = diaChiKhachHangRepo;
        this.emailServices = emailServices;
    }

    public List<KhachHang> getAllCustomers() {
        List<KhachHang> allCustomers = khachHangRepository.findAllActiveCustomers();
        return allCustomers.stream()
                .filter(kh -> !"KH00001".equals(kh.getMa())).collect(Collectors.toList());
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
        if (taiKhoanRepository.findByEmail(khachHangDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        if (taiKhoanRepository.existsBySoDienThoai(khachHangDTO.getSoDienThoai())) {
            throw new RuntimeException("SDT đã được sử dụng!");
        }

        QuyenHan quyenHan = new QuyenHan();
        quyenHan.setId(2); // Quyền khách hàng

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setMa(MaTaiKhoan());
        taiKhoan.setEmail(khachHangDTO.getEmail());
        taiKhoan.setSoDienThoai(khachHangDTO.getSoDienThoai());
        taiKhoan.setTenDangNhap(khachHangDTO.getUserName());
        taiKhoan.setIdQuyenHan(quyenHan);
        taiKhoan.setDeleted(khachHangDTO.getGioiTinh());

        String randomPassword = emailServices.generateRandomPassword(8);
        taiKhoan.setMatKhau(randomPassword);

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
        kh.setAnhKhachHang(khachHangDTO.getAnhKhachHang());
        kh.setGioiTinh(khachHangDTO.getGioiTinh() != null && khachHangDTO.getGioiTinh() ? (short) 1 : (short) 0);
        kh = khachHangRepository.save(kh); // Lưu trước để có ID

        DiaChiKhachHang dchi = new DiaChiKhachHang();
        dchi.setMa(MaDchi());
        dchi.setThanhPho(khachHangDTO.getThanhPho());
        dchi.setQuan(khachHangDTO.getQuan());
        dchi.setPhuong(khachHangDTO.getPhuong());
        dchi.setDiaChiCuThe(khachHangDTO.getDiaChiCuThe());
        dchi.setMacDinh(true);
        dchi.setIdKhachHang(kh);
        diaChiKhachHangRepo.save(dchi);

        kh.setIdDiaChiKH(dchi); // Gán lại địa chỉ khách hàng vào khachHang
        try {
            emailServices.EmailKH(
                    khachHangDTO.getEmail(),
                    khachHangDTO.getTenKH(),
                    khachHangDTO.getEmail(),
                    randomPassword
            );
        } catch (Exception e) {
            // Log lỗi nếu cần, nhưng không làm ảnh hưởng quá trình thêm nhân viên
            System.err.println("Lỗi gửi email: " + e.getMessage());
        }
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
                    existingNhanVien.setTen(khachHangDTO.getTenKH());
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

    public void importKhachHangFromExcel(List<KhachHangDTO> khachHangDTOs) {
        for (KhachHangDTO dto : khachHangDTOs) {
            if (dto.getMa() == null || dto.getTenKH() == null || dto.getEmail() == null) {
                throw new IllegalArgumentException("Mã, tên hoặc email khách hàng không được để trống!");
            }

            Optional<KhachHang> existingKhachHang = khachHangRepository.findByMa(dto.getMa());
            if (existingKhachHang.isPresent()) {
                // Cập nhật khách hàng hiện có
                KhachHang existing = existingKhachHang.get();
                existing.setTen(dto.getTenKH());
                existing.setDeleted(dto.getGioiTinh() != null ? dto.getGioiTinh() : false);
                existing.setUpdatedAt(new Date());

                if (existing.getIdTaiKhoan() != null) {
                    TaiKhoan taiKhoan = taiKhoanRepository.findById(existing.getIdTaiKhoan().getId())
                            .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));
                    taiKhoan.setEmail(dto.getEmail());
                    taiKhoan.setSoDienThoai(dto.getSoDienThoai());
                    taiKhoan.setTenDangNhap(dto.getUserName() != null ? dto.getUserName() : dto.getEmail());
                    taiKhoanRepository.save(taiKhoan);
                }

                List<DiaChiKhachHang> existingAddresses = diaChiKhachHangRepo.findByIdKhachHang(existing);
                if (!existingAddresses.isEmpty()) {
                    DiaChiKhachHang defaultAddress = existingAddresses.stream()
                            .filter(DiaChiKhachHang::getMacDinh)
                            .findFirst()
                            .orElse(existingAddresses.get(0));
                    defaultAddress.setDiaChiCuThe(dto.getDiaChiCuThe() != null ? dto.getDiaChiCuThe() : "Chưa có dữ liệu");
                    defaultAddress.setThanhPho(dto.getThanhPho() != null ? dto.getThanhPho() : "N/A");
                    defaultAddress.setQuan(dto.getQuan() != null ? dto.getQuan() : "N/A");
                    defaultAddress.setPhuong(dto.getPhuong() != null ? dto.getPhuong() : "N/A");
                    diaChiKhachHangRepo.save(defaultAddress);
                } else {
                    DiaChiKhachHang diaChi = new DiaChiKhachHang();
                    diaChi.setMa(MaDchi());
                    diaChi.setIdKhachHang(existing);
                    diaChi.setDiaChiCuThe(dto.getDiaChiCuThe() != null ? dto.getDiaChiCuThe() : "Chưa có dữ liệu");
                    diaChi.setThanhPho(dto.getThanhPho() != null ? dto.getThanhPho() : "N/A");
                    diaChi.setQuan(dto.getQuan() != null ? dto.getQuan() : "N/A");
                    diaChi.setPhuong(dto.getPhuong() != null ? dto.getPhuong() : "N/A");
                    diaChi.setMacDinh(true);
                    diaChiKhachHangRepo.save(diaChi);
                    existing.setIdDiaChiKH(diaChi);
                }

                khachHangRepository.save(existing);
            } else {
                // Thêm khách hàng mới
                QuyenHan quyenHan = new QuyenHan();
                quyenHan.setId(2);

                TaiKhoan taiKhoan = new TaiKhoan();
                taiKhoan.setMa(MaTaiKhoan());
                taiKhoan.setEmail(dto.getEmail());
                taiKhoan.setSoDienThoai(dto.getSoDienThoai());
                taiKhoan.setTenDangNhap(dto.getUserName() != null ? dto.getUserName() : dto.getEmail());
                taiKhoan.setIdQuyenHan(quyenHan);
                taiKhoan.setDeleted(dto.getGioiTinh() != null ? dto.getGioiTinh() : false);

                String randomPassword = emailServices.generateRandomPassword(8);
                taiKhoan.setMatKhau(randomPassword);
                taiKhoan = taiKhoanRepository.save(taiKhoan);

                KhachHang khachHang = new KhachHang();
                khachHang.setMa(dto.getMa());
                khachHang.setIdTaiKhoan(taiKhoan);
                khachHang.setTen(dto.getTenKH());
                khachHang.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : new Date());
                khachHang.setDeleted(dto.getGioiTinh() != null ? dto.getGioiTinh() : false);
                khachHang = khachHangRepository.save(khachHang);

                DiaChiKhachHang diaChi = new DiaChiKhachHang();
                diaChi.setMa(MaDchi());
                diaChi.setIdKhachHang(khachHang);
                diaChi.setDiaChiCuThe(dto.getDiaChiCuThe() != null ? dto.getDiaChiCuThe() : "Chưa có dữ liệu");
                diaChi.setThanhPho(dto.getThanhPho() != null ? dto.getThanhPho() : "N/A");
                diaChi.setQuan(dto.getQuan() != null ? dto.getQuan() : "N/A");
                diaChi.setPhuong(dto.getPhuong() != null ? dto.getPhuong() : "N/A");
                diaChi.setMacDinh(true);
                diaChiKhachHangRepo.save(diaChi);
                khachHang.setIdDiaChiKH(diaChi);
                khachHangRepository.save(khachHang);

                try {
                    emailServices.sendWelcomeEmail(taiKhoan.getEmail(), khachHang.getTen(), taiKhoan.getEmail(), randomPassword);
                } catch (Exception e) {
                    System.err.println("Lỗi gửi email: " + e.getMessage());
                }
            }
        }
    }
    public List<KhachHang> searchKhachHang(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of(); // Trả về danh sách rỗng nếu keyword trống
        }
        return khachHangRepository.searchBh(keyword.trim());
    }

    public KhachHang addKhachHangBH(KhachHangDTO khachHangDTO) {
        if (khachHangDTO == null || khachHangDTO.getSoDienThoai() == null || khachHangDTO.getTenKH() == null
                || khachHangDTO.getDiaChiCuThe() == null || khachHangDTO.getQuan() == null
                || khachHangDTO.getThanhPho() == null || khachHangDTO.getPhuong() == null) {
            throw new IllegalArgumentException("Thông tin khách hàng không đầy đủ");
        }

        // Tạo quyền hạn
        QuyenHan quyenHan = new QuyenHan();
        quyenHan.setId(2); // Quyền khách hàng

        // Tạo và lưu tài khoản
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setSoDienThoai(khachHangDTO.getSoDienThoai());
        taiKhoan.setIdQuyenHan(quyenHan);
        taiKhoan = taiKhoanRepository.save(taiKhoan);

        // Tạo và lưu khách hàng
        KhachHang kh = new KhachHang();
        kh.setCreatedAt(new Date()); // Tự động thêm ngày tạo
        kh.setMa(generateMaKH(khachHangDTO.getTenKH()));
        kh.setIdTaiKhoan(taiKhoan);
        kh.setTen(khachHangDTO.getTenKH());
        kh.setDeleted(false);
        kh = khachHangRepository.save(kh); // Lưu trước để có ID

        // Tạo và lưu địa chỉ khách hàng
        DiaChiKhachHang dchi = new DiaChiKhachHang();
        dchi.setMa(MaDchi());
        dchi.setDiaChiCuThe(khachHangDTO.getDiaChiCuThe());
        dchi.setQuan(khachHangDTO.getQuan());
        dchi.setThanhPho(khachHangDTO.getThanhPho());
        dchi.setPhuong(khachHangDTO.getPhuong());
        dchi.setMacDinh(true);
        dchi.setIdKhachHang(kh);
        dchi = diaChiKhachHangRepo.save(dchi);

        // Cập nhật địa chỉ cho khách hàng
        kh.setIdDiaChiKH(dchi);
        return khachHangRepository.save(kh);
    }
}