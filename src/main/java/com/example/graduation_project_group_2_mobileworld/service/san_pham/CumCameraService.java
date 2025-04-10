package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CumCameraDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.CumCamera;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CumCameraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CumCameraService {

    private final CumCameraRepository cumCameraRepository;

    public CumCameraService(CumCameraRepository cumCameraRepository) {
        this.cumCameraRepository = cumCameraRepository;
    }

    public Page<CumCameraDTO> getAllCumCamera(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cumCameraRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    public Page<CumCameraDTO> getCameraDetails(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = cumCameraRepository.findCameraDetails(pageable);
        return results.map(result -> {
            CumCameraDTO dto = new CumCameraDTO();
            dto.setId((Integer) result[0]);           // cumCameraId
            dto.setMa((String) result[1]);            // cumCameraMa
            dto.setCameraTruoc((String) result[2]);   // cameraTruoc
            dto.setCameraSau((String) result[3]);     // cameraSau
            return dto;
        });
    }

    private CumCameraDTO toDTO(CumCamera entity) {
        CumCameraDTO dto = new CumCameraDTO();
        dto.setId(entity.getId());
        dto.setMa(entity.getMa());
        dto.setCameraTruoc(entity.getThongSoCameraTruoc());
        dto.setCameraSau(entity.getThongSoCameraSau());
        dto.setDeleted(entity.getDeleted());
        return dto;
    }
}