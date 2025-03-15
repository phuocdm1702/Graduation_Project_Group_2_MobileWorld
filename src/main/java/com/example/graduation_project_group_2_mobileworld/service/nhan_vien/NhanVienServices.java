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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NhanVienServices {
    private final NhanVienRepository nhanVienRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    public NhanVienServices(NhanVienRepository nhanVienRepository, TaiKhoanRepository taiKhoanRepository) {
        this.nhanVienRepository = nhanVienRepository;
        this.taiKhoanRepository = taiKhoanRepository;
    }

    //MatutangNV
    public String generateMaNhanVien() {
        Integer maxMa = nhanVienRepository.findMaxMa(); // Tìm giá trị lớn nhất dạng số nguyên
        if (maxMa == null) {
            return "NV000001"; // Nếu chưa có mã nào thì bắt đầu từ NV000001
        }
        return String.format("NV%06d", maxMa + 1);
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
        nhanVien.setMa(generateMaNhanVien());
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


}
