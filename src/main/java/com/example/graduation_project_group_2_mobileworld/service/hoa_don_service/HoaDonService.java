package com.example.graduation_project_group_2_mobileworld.service.hoa_don_service;

import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don_repo.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonService {
    private HoaDonRepository hoaDonRepository;
    @Autowired
    public HoaDonService(HoaDonRepository hoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
    }

    public List<HoaDon> getAllData(){
        return hoaDonRepository.findAll();
    }
}
