package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.HeDieuHanhDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.PinDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HeDieuHanh;
import com.example.graduation_project_group_2_mobileworld.entity.Pin;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.HeDieuHanhRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.PinRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PinService {

    private final PinRepository pinRepository;

    public PinService(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    public Page<PinDTO> getAllPin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return pinRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private PinDTO toDTO(Pin entity) {
        return new PinDTO(entity.getId(), entity.getMa(), entity.getLoaiPin(), entity.getDungLuongPin(), entity.getDeleted());
    }
}
