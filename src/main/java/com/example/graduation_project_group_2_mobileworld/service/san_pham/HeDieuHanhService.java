package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.HeDieuHanhDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HeDieuHanh;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.HeDieuHanhRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HeDieuHanhService {

    private final HeDieuHanhRepository heDieuHanhRepository;

    public HeDieuHanhService(HeDieuHanhRepository heDieuHanhRepository) {
        this.heDieuHanhRepository = heDieuHanhRepository;
    }

    public Page<HeDieuHanhDTO> getAllHeDieuHanh(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return heDieuHanhRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private HeDieuHanhDTO toDTO(HeDieuHanh entity) {
        return new HeDieuHanhDTO(entity.getId(), entity.getMa(), entity.getHeDieuHanh(), entity.getPhienBan(), entity.getDeleted());
    }
}
