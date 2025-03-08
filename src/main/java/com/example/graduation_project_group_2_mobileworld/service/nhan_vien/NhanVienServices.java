package com.example.graduation_project_group_2_mobileworld.service.nhan_vien;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import com.example.graduation_project_group_2_mobileworld.repository.nhan_vien.NhanVienRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NhanVienServices {
    private final NhanVienRepository nhanVienRepository;

    public NhanVienServices(NhanVienRepository nhanVienRepository) {
        this.nhanVienRepository = nhanVienRepository;
    }

    public List<NhanVien> getall() {
        return nhanVienRepository.findAllActiveNv();
    }


//    public NhanVien add(NhanVien nhanVien) {
//        return nhanVienRepository.save(nhanVien);
//    }




//    public boolean softDeleteNhanVien(Integer id) {
//        Optional<NhanVien> optionalNV = nhanVienRepository.findById(id);
//        if (optionalNV.isPresent()) {
//            NhanVien nv = optionalNV.get();
//            nv.setDeleted(true);
//            nhanVienRepository.save(nv);
//            return true;
//        }
//        return false;
//    }
//
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
//
//    public NhanVien updatenv(Integer id, NhanVien nhanVien) {
//        Optional<NhanVien> existingKhachHang = nhanVienRepository.findById(id);
//        if (existingKhachHang.isPresent()) {
//            NhanVien kh = existingKhachHang.get();
//            // Kiểm tra trùng mã với các khách hàng khác (trừ chính nó)
//            if (nhanVienRepository.existsByMaAndNotId(nhanVien.getMa(), id)) {
//                throw new RuntimeException("Mã khách hàng đã tồn tại: " + nhanVien.getMa());
//            }
//            kh.setMa(nhanVien.getMa());
//            kh.setTenNhanVien(nhanVien.getTenNhanVien());
//            kh.setNgaySinh(nhanVien.getNgaySinh());
//            kh.setAnhNhanVien(nhanVien.getAnhNhanVien());
//            kh.setGhiChu(nhanVien.getGhiChu());
//            kh.setThanhPho(nhanVien.getThanhPho());
//            kh.setQuan(nhanVien.getQuan());
//            kh.setPhuong(nhanVien.getPhuong());
//            kh.setDiaChiCuThe(nhanVien.getDiaChiCuThe());
//            kh.setCccd(nhanVien.getCccd());
//            return nhanVienRepository.save(kh);
//        } else {
//            throw new RuntimeException("Không tìm thấy khách hàng với ID: " + id);
//        }
//    }
}
