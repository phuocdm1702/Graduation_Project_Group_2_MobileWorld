package com.example.graduation_project_group_2_mobileworld.service.phieu_bao_hanh;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.imel.ImelDaBanDTO;
import com.example.graduation_project_group_2_mobileworld.dto.phieu_bao_hanh.PhieuBaoHanhDTO;
import com.example.graduation_project_group_2_mobileworld.entity.ImelDaBan;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuBaoHanh;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.imel.ImelDaBanRepository;
import com.example.graduation_project_group_2_mobileworld.repository.bao_hanh.PhieuBaoHanhRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhieuBaoHanhService {
//    private final PhieuBaoHanhRepository pbhRepository;
//    private final ImelDaBanRepository imelDaBanRepository;
//
//    public PhieuBaoHanhService(PhieuBaoHanhRepository pbhRepository, ImelDaBanRepository imelDaBanRepository) {
//        this.pbhRepository = pbhRepository;
//        this.imelDaBanRepository = imelDaBanRepository;
//    }
//
//    public List<PhieuBaoHanhDTO> getAllDataPBH() {
//        return pbhRepository.findAll()
//                .stream()
//                .map(this::toDTO)
//                .toList();
//    }
//
//
//    public List<ImelDaBanDTO> getDataCBO() {
//        return imelDaBanRepository.findAll()
//                .stream()
//                .map(this::toDTO)
//                .toList()
//                ;
//    }
//
//
//    private PhieuBaoHanhDTO toDTO(PhieuBaoHanh pbh) {
//        return new PhieuBaoHanhDTO(
//                pbh.getId(),
//                pbh.getMa(),
//                pbh.getIdImelDaBan(),
//                pbh.getNgayBatDau(),
//                pbh.getNgayKetThuc(),
//                pbh.getGhiChu()
//        );
//    }
//
//
//    private ImelDaBanDTO toDTO(ImelDaBan imel) {
//        return new ImelDaBanDTO(
//                imel.getId(),
//                imel.getMa(),
//                imel.getImel()
//        );
//    }


}
