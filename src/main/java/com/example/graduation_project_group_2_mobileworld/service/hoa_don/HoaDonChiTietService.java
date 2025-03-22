package com.example.graduation_project_group_2_mobileworld.service.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonChiTietDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDonChiTiet;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don.HoaDonChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietService {
    private final HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    public HoaDonChiTietService(HoaDonChiTietRepository hoaDonChiTietRepository) {
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
    }

    public List<HoaDonChiTietDTO> getAllDataHDCT() {

        return hoaDonChiTietRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList()
                ;
    }

    private HoaDonChiTietDTO toDTO(HoaDonChiTiet hoaDonChiTiet) {
        return new HoaDonChiTietDTO(
                hoaDonChiTiet.getId(),
                hoaDonChiTiet.getHoaDon().getId(),
                hoaDonChiTiet.getIdChiTietSanPham(),
                hoaDonChiTiet.getIdImelDaBan(),
                hoaDonChiTiet.getMa(),
                hoaDonChiTiet.getGia(),
                hoaDonChiTiet.getTrangThai(),
                hoaDonChiTiet.getGhiChu(),
                hoaDonChiTiet.getId(),
                hoaDonChiTiet.getMa(),
                hoaDonChiTiet.getMa()
        );
    }
}
