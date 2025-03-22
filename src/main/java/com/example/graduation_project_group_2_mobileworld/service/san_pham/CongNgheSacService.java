package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CongNgheSacDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CongSacDTO;
import com.example.graduation_project_group_2_mobileworld.entity.CongNgheSac;
import com.example.graduation_project_group_2_mobileworld.entity.CongSac;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CongNgheSacRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CongSacRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CongNgheSacService {

    private final CongNgheSacRepository congSacRepository;

    public CongNgheSacService(CongNgheSacRepository congSacRepository) {
        this.congSacRepository = congSacRepository;
    }

    public Page<CongNgheSacDTO> getAllCongSac(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return congSacRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private CongNgheSacDTO toDTO(CongNgheSac entity) {
        return new CongNgheSacDTO(entity.getId(), entity.getMa(), entity.getTenCongNghe(), entity.getDeleted());
    }
}
