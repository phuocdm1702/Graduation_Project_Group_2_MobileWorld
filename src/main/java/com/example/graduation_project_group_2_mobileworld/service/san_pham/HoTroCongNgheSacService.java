package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.HoTroCongNgheSacDTO;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.HoTroCongNgheSacRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HoTroCongNgheSacService {

    private final HoTroCongNgheSacRepository hoTroCongNgheSacRepository;

    public HoTroCongNgheSacService(HoTroCongNgheSacRepository hoTroCongNgheSacRepository) {
        this.hoTroCongNgheSacRepository = hoTroCongNgheSacRepository;
    }

    public Page<HoTroCongNgheSacDTO> getChargingTechDetails(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = hoTroCongNgheSacRepository.findChargingTechDetails(pageable);
        return results.map(result -> {
            HoTroCongNgheSacDTO dto = new HoTroCongNgheSacDTO();
            dto.setId((Integer) result[0]);              // ho_tro_cong_nghe_sac_id
            dto.setMa((String) result[1]);               // ho_tro_cong_nghe_sac_ma
            dto.setTenCongNgheSac((String) result[2]);   // ten_cong_nghe_sac
            return dto;
        });
    }
}