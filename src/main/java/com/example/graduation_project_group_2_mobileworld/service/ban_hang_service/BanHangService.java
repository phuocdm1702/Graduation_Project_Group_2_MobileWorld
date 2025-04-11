package com.example.graduation_project_group_2_mobileworld.service.ban_hang_service;

import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDCTban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDonChiTiet;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class BanHangService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    public List<HDban_hangDTO> getAllHD() {
        List<HoaDon> listHD = hoaDonRepository.findAllHDNotConfirm();
        return listHD.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public HoaDon addHD(HoaDon hoaDon) {
        return hoaDonRepository.save(hoaDon);
    }

    private HDban_hangDTO mapToDTO(HoaDon hd) {
        HDban_hangDTO dto = new HDban_hangDTO();
        dto.setMa(hd.getMa());
        dto.setStatus(hd.getTrangThai());
        dto.setItems(hd.getChiTietHoaDon().stream().map(this::mapChiTietToDTO).collect(Collectors.toList()));
        return dto;
    }

    // Ánh xạ từ HoaDonChiTiet sang HoaDonChiTietDTO
    private HDCTban_hangDTO mapChiTietToDTO(HoaDonChiTiet chiTiet) {
        HDCTban_hangDTO dto = new HDCTban_hangDTO();
        dto.setName(chiTiet.getIdChiTietSanPham() != null ? chiTiet.getIdChiTietSanPham().getMa() : "Unknown");
        dto.setImei(chiTiet.getIdImelDaBan() != null ? chiTiet.getIdImelDaBan().getImel() : "N/A");
        dto.setPrice(chiTiet.getGia() != null ? chiTiet.getGia().longValue() : 0L);
        return dto;
    }

}
