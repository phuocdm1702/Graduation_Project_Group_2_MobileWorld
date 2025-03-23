package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CongNgheMangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.GpuDTO;
import com.example.graduation_project_group_2_mobileworld.entity.CongNgheMang;
import com.example.graduation_project_group_2_mobileworld.entity.Gpu;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CongNgheMangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.GpuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CongNgheMangService {

    private final CongNgheMangRepository ngheMangRepository;

    public CongNgheMangService(CongNgheMangRepository ngheMangRepository) {
        this.ngheMangRepository = ngheMangRepository;
    }

    public Page<CongNgheMangDTO> getAllCongNgheMang(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ngheMangRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private CongNgheMangDTO toDTO(CongNgheMang entity) {
        return new CongNgheMangDTO(entity.getId(), entity.getMa(), entity.getTenCongNgheMang(), entity.getDeleted());
    }
}
