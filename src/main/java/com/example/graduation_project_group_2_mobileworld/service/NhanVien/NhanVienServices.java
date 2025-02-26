package com.example.graduation_project_group_2_mobileworld.service.NhanVien;

import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import com.example.graduation_project_group_2_mobileworld.repository.NhanVien.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
