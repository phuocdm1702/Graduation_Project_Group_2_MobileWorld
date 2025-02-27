package com.example.graduation_project_group_2_mobileworld.service.nhan_vien;

import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import com.example.graduation_project_group_2_mobileworld.repository.nhan_vien.NhanVienRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NhanVienServices {
    private final NhanVienRepository nhanVienRepository;

    public NhanVienServices(NhanVienRepository nhanVienRepository) {
        this.nhanVienRepository = nhanVienRepository;
    }


    public List<NhanVien> getall() {
        return nhanVienRepository.findAll();
    }

    public NhanVien add(NhanVien nhanVien) {
        return nhanVienRepository.save(nhanVien);
    }

    public ResponseEntity<String> delete(Integer id) {
        nhanVienRepository.deleteById(id);
        return null;
    }
}
