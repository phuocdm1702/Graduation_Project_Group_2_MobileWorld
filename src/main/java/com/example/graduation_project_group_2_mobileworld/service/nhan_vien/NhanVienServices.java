package com.example.graduation_project_group_2_mobileworld.service.nhan_vien;

import com.example.graduation_project_group_2_mobileworld.dto.nhan_vien.NhanVienDTO;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import com.example.graduation_project_group_2_mobileworld.entity.QuyenHan;
import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import com.example.graduation_project_group_2_mobileworld.repository.nhan_vien.NhanVienRepository;
import com.example.graduation_project_group_2_mobileworld.repository.tai_khoan.TaiKhoanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NhanVienServices {
    private final NhanVienRepository nhanVienRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    public NhanVienServices(NhanVienRepository nhanVienRepository, TaiKhoanRepository taiKhoanRepository) {
        this.nhanVienRepository = nhanVienRepository;
        this.taiKhoanRepository = taiKhoanRepository;
    }

    //MatutangNV
    public String generateMaNhanVien(String tenNhanVien) {
        String[] parts = tenNhanVien.split("\\s+");

        String lastName = removeAccents(parts[parts.length - 1]).toLowerCase();

        String initials = "";
        for (int i = 0; i < parts.length - 1; i++) {
            if (!parts[i].isEmpty()) {
                initials += removeAccents(parts[i]).toLowerCase().charAt(0);
            }
        }
        String baseCode = lastName + initials;

        // Kiểm tra trùng lặp và thêm số thứ tự nếu cần
        String finalCode = baseCode;
        int counter = 1;
        while (nhanVienRepository.existsByMa(finalCode)) {
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
    //MatutangTK
    public String MaTaiKhoan() {
        String maxMaTK = taiKhoanRepository.findMaxMaTK();
        if (maxMaTK != null) {
            int nextNumber = Integer.parseInt(maxMaTK.substring(2)) + 1;
            return String.format("TK%05d", nextNumber);
        } else {
            return "TK00001";
        }
    }






    //hienthi
    public List<NhanVien> getall() {
        return nhanVienRepository.findAllActiveNv();
    }

    //xoa
    public boolean delete(Integer id) {
        Optional<NhanVien> optionalKH = nhanVienRepository.findById(id);
        if (optionalKH.isPresent()) {
            NhanVien kh = optionalKH.get();
            kh.setDeleted(true); // Đánh dấu khách hàng đã bị xóa mềm
            nhanVienRepository.save(kh);
            return true;
        }
        return false;
    }
    //themnhanvien
    public NhanVien addNhanVien(NhanVienDTO dto) {


        QuyenHan quyenHan = new QuyenHan();;
        quyenHan.setId(3);
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setMa(MaTaiKhoan());
        taiKhoan.setEmail(dto.getEmail());
        taiKhoan.setSoDienThoai(dto.getSoDienThoai());
        taiKhoan.setTenDangNhap(dto.getTenDangNhap());
        taiKhoan.setIdQuyenHan(quyenHan);
        taiKhoan.setDeleted(dto.getGioiTinh());
        taiKhoan.setTenDangNhap(dto.getTenDangNhap());
        taiKhoan = taiKhoanRepository.save(taiKhoan);

        NhanVien nhanVien = new NhanVien();
        if (nhanVien.getCreatedAt() == null) {
            nhanVien.setCreatedAt(new Date()); // Tự động thêm nếu không có
        }
        nhanVien.setMa(generateMaNhanVien(dto.getTenNhanVien()));
        nhanVien.setIdTaiKhoan(taiKhoan);
        nhanVien.setTenNhanVien(dto.getTenNhanVien());
        nhanVien.setNgaySinh(dto.getNgaySinh());
        nhanVien.setThanhPho(dto.getThanhPho());
        nhanVien.setQuan(dto.getQuan());
        nhanVien.setPhuong(dto.getPhuong());
        nhanVien.setDiaChiCuThe(dto.getDiaChiCuThe());
        nhanVien.setCccd(dto.getCccd());
        nhanVien.setAnhNhanVien(dto.getAnhNhanVien());
        nhanVien.setDeleted(false);

        return nhanVienRepository.save(nhanVien);
    }
    public Optional<NhanVien> findById(Integer id) {
        return nhanVienRepository.findById(id);
    }

    public NhanVien updateNhanVien(Integer id, NhanVienDTO nhanVienDTO) {
        return nhanVienRepository.findById(id)
                .map(existingNhanVien -> {
                    existingNhanVien.setTenNhanVien(nhanVienDTO.getTenNhanVien());
                    existingNhanVien.setNgaySinh(nhanVienDTO.getNgaySinh());
                    existingNhanVien.setThanhPho(nhanVienDTO.getThanhPho());
                    existingNhanVien.setQuan(nhanVienDTO.getQuan());
                    existingNhanVien.setPhuong(nhanVienDTO.getPhuong());
                    existingNhanVien.setDiaChiCuThe(nhanVienDTO.getDiaChiCuThe());

                    // Lấy thông tin tài khoản qua idTaiKhoan
                    if (existingNhanVien.getIdTaiKhoan() != null) {
                        TaiKhoan taiKhoan = taiKhoanRepository.findById(existingNhanVien.getIdTaiKhoan().getId())
                                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

                        taiKhoan.setEmail(nhanVienDTO.getEmail());
                        taiKhoan.setSoDienThoai(nhanVienDTO.getSoDienThoai());
                        taiKhoan.setDeleted(nhanVienDTO.getGioiTinh());
                        taiKhoanRepository.save(taiKhoan);
                    }

                    existingNhanVien.setUpdatedAt(new Date());
                    existingNhanVien.setUpdatedBy(1); // Hoặc lấy từ session

                    return nhanVienRepository.save(existingNhanVien);
                }).orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại!"));
    }


    public List<NhanVien> searchNhanVien(String keyword, String status) {
        List<NhanVien> allNhanViens = nhanVienRepository.findAll();

        // Lọc theo trạng thái
        if (status != null && !status.isEmpty()) {
            boolean isDeleted = status.equals("da-nghi");
            allNhanViens = allNhanViens.stream()
                    .filter(nv -> nv.getDeleted() == isDeleted)
                    .collect(Collectors.toList());
        }

        // Nếu không có từ khóa, trả về danh sách đã lọc theo trạng thái
        if (keyword == null || keyword.trim().isEmpty()) {
            return allNhanViens;
        }

        // Tìm kiếm theo từ khóa (mã, tên, email, số điện thoại)
        String keywordLower = keyword.toLowerCase();
        return allNhanViens.stream()
                .filter(nv ->
                        (nv.getMa() != null && nv.getMa().toLowerCase().contains(keywordLower)) ||
                                (nv.getTenNhanVien() != null && nv.getTenNhanVien().toLowerCase().contains(keywordLower)) ||
                                (nv.getIdTaiKhoan() != null && nv.getIdTaiKhoan().getEmail() != null && nv.getIdTaiKhoan().getEmail().toLowerCase().contains(keywordLower)) ||
                                (nv.getIdTaiKhoan() != null && nv.getIdTaiKhoan().getSoDienThoai() != null && nv.getIdTaiKhoan().getSoDienThoai().toLowerCase().contains(keywordLower))
                )
                .collect(Collectors.toList());
    }
}
