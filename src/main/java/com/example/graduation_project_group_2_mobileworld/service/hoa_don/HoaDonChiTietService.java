package com.example.graduation_project_group_2_mobileworld.service.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.HoaDonChiTiet;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don.HoaDonChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietService {
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired

    public HoaDonChiTietService(HoaDonChiTietRepository hoaDonChiTietRepository) {
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
    }

    public List<HoaDonChiTiet> getAllDataHDCT(){
        return hoaDonChiTietRepository.findAll();
    }
}
