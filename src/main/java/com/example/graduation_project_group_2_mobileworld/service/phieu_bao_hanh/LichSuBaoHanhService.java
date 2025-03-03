package com.example.graduation_project_group_2_mobileworld.service.phieu_bao_hanh;

import com.example.graduation_project_group_2_mobileworld.dto.phieu_bao_hanh.LichSuBaoHanhDTO;
import com.example.graduation_project_group_2_mobileworld.entity.LichSuBaoHanh;
import com.example.graduation_project_group_2_mobileworld.repository.bao_hanh.LichSuBaoHanhRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LichSuBaoHanhService {

    private final LichSuBaoHanhRepository lsbhRepository;

    public LichSuBaoHanhService(LichSuBaoHanhRepository lsbhRepository) {
        this.lsbhRepository = lsbhRepository;
    }

    public List<LichSuBaoHanhDTO> getAllData(){
        return lsbhRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList()
                ;
    }

    private LichSuBaoHanhDTO toDTO(LichSuBaoHanh lsbh){
        return new LichSuBaoHanhDTO(
                lsbh.getIdPhieuBaoHanh(),
                lsbh.getMa(),
                lsbh.getMoTaLoi(),
                lsbh.getPhuongThucSuaChua(),
                lsbh.getChiPhi(),
                lsbh.getTrangThai(),
                lsbh.getGhiChu()
        );
    }
}
