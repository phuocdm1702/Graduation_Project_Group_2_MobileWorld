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

    public NhanVien add(NhanVien nhanVien) {
        return nhanVienRepository.save(nhanVien);
    }

    public ResponseEntity<Object> updateNV(Integer id, NhanVien nhanVien) {
        Optional<NhanVien> existingNhanVien = nhanVienRepository.findById(id);
        if (!existingNhanVien.isPresent()) {
            return ResponseEntity.status(404).body("Không tìm thấy nhân viên với ID: " + id);
        }

        NhanVien nv = existingNhanVien.get();

        if (nhanVienRepository.existsByMaAndNotId(nhanVien.getMa(), id)) {
            return ResponseEntity.status(400).body("Mã nhân viên đã tồn tại: " + nhanVien.getMa());
        }

        nv.setMa(nhanVien.getMa());
        nv.setTenNhanVien(nhanVien.getTenNhanVien());
        nv.setNgaySinh(nhanVien.getNgaySinh());
        nv.setAnhNhanVien(nhanVien.getAnhNhanVien());
        nv.setGhiChu(nhanVien.getGhiChu());
        nv.setThanhPho(nhanVien.getThanhPho());
        nv.setQuan(nhanVien.getQuan());
        nv.setPhuong(nhanVien.getPhuong());
        nv.setDiaChiCuThe(nhanVien.getDiaChiCuThe());
        nv.setCccd(nhanVien.getCccd());

        return ResponseEntity.ok(nhanVienRepository.save(nv));
    }


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
    }
