package com.example.graduation_project_group_2_mobileworld.service.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.LichSuHoaDon;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don.LichSuHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LichSuHoaDonService {
    private LichSuHoaDonRepository lichSuHoaDonRepository;
    @Autowired
    public LichSuHoaDonService(LichSuHoaDonRepository lichSuHoaDonRepository) {
        this.lichSuHoaDonRepository = lichSuHoaDonRepository;
    }

    public List<LichSuHoaDon> getAllDataLSHD(){
        return lichSuHoaDonRepository.findAll();
    }
}
