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
        taiKhoan = taiKhoanRepository.save(taiKhoan);

        NhanVien nhanVien = new NhanVien();
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

}
