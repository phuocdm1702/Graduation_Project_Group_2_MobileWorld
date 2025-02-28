package com.example.graduation_project_group_2_mobileworld.service.phieu_bao_hanh;

import com.example.graduation_project_group_2_mobileworld.dto.phieu_bao_hanh.PhieuBaoHanhDTO;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuBaoHanh;
import com.example.graduation_project_group_2_mobileworld.repository.bao_hanh.PhieuBaoHanhRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhieuBaoHanhService {
    private final PhieuBaoHanhRepository pbhRepository;

    public PhieuBaoHanhService(PhieuBaoHanhRepository pbhRepository) {
        this.pbhRepository = pbhRepository;
    }

    public List<PhieuBaoHanhDTO> getAllDataPBH(){
        return pbhRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private PhieuBaoHanhDTO toDTO(PhieuBaoHanh pbh){
        return new PhieuBaoHanhDTO(
                pbh.getMa(),
                pbh.getIdImelDaBan(),
                pbh.getNgayBatDau(),
                pbh.getNgayKetThuc(),
                pbh.getGhiChu()
        );
    }
}
