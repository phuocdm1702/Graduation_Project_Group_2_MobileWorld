package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CongSacDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.GpuDTO;
import com.example.graduation_project_group_2_mobileworld.entity.CongSac;
import com.example.graduation_project_group_2_mobileworld.entity.Gpu;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CongSacRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.GpuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CongSacService {

    private final CongSacRepository congSacRepository;

    public CongSacService(CongSacRepository congSacRepository) {
        this.congSacRepository = congSacRepository;
    }

    public Page<CongSacDTO> getAllCongSac(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return congSacRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private CongSacDTO toDTO(CongSac entity) {
        return new CongSacDTO(entity.getId(), entity.getMa(), entity.getCongSac(), entity.getDeleted());
    }
}
