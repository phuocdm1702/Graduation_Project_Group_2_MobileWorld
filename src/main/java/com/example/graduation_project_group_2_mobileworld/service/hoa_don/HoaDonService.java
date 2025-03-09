package com.example.graduation_project_group_2_mobileworld.service.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don.HoaDonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonService {
    private final HoaDonRepository hoaDonRepository;

    public HoaDonService(HoaDonRepository hoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
    }

    public List<HoaDonDTO> getAllData(){
        return hoaDonRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList()
                ;
    }

    public Page<HoaDonDTO> getAllData(Pageable pageable) {
        Page<HoaDon> hoaDonPage = hoaDonRepository.findAll(pageable);
        List<HoaDonDTO> hoaDonDTOs = hoaDonPage.getContent()
                .stream()
                .map(this::toDTO)
                .toList();
        return new PageImpl<>(hoaDonDTOs, pageable, hoaDonPage.getTotalElements());
    }

    private HoaDonDTO toDTO(HoaDon hoaDon){
        return new HoaDonDTO(
                hoaDon.getIdKhachHang(),
                hoaDon.getIdNhanVien(),
                hoaDon.getIdPhieuGiamGia(),
                hoaDon.getMa(),
                hoaDon.getSoDienThoaiKhachHang(),
                hoaDon.getLoaiDon(),
                hoaDon.getTongTien(),
                hoaDon.getNgayTao(),
                hoaDon.getTongTienSauGiam(),
                hoaDon.getTrangThai()
        );
    }


}
