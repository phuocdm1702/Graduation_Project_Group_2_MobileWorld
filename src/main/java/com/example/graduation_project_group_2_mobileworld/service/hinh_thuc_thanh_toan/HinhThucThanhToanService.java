package com.example.graduation_project_group_2_mobileworld.service.hinh_thuc_thanh_toan;

import com.example.graduation_project_group_2_mobileworld.dto.hinh_thuc_thanh_toan.HinhThucThanhToanDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonChiTietDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HinhThucThanhToan;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDonChiTiet;
import com.example.graduation_project_group_2_mobileworld.repository.hinh_thuc_thanh_toan_repo.HinhThucThanhToanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HinhThucThanhToanService {
    private final HinhThucThanhToanRepository hinhThucThanhToanRepository;

    public HinhThucThanhToanService(HinhThucThanhToanRepository hinhThucThanhToanRepository) {
        this.hinhThucThanhToanRepository = hinhThucThanhToanRepository;
    }

    public List<HinhThucThanhToanDTO> getAllData(){
        return hinhThucThanhToanRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList()
                ;
    }
    private HinhThucThanhToanDTO toDTO(HinhThucThanhToan httt) {
        return new HinhThucThanhToanDTO(
                httt.getId(),
                httt.getHoaDon().getId(),
                httt.getIdPhuongThucThanhToan(),
                httt.getTienChuyenKhoan(),
                httt.getTienMat(),
                httt.getMa(),
                httt.getDeleted()
        );
    }

}
