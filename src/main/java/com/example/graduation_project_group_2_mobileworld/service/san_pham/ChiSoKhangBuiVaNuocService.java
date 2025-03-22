package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ChiSoKhangBuiVaNuocDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.GpuDTO;
import com.example.graduation_project_group_2_mobileworld.entity.ChiSoKhangBuiVaNuoc;
import com.example.graduation_project_group_2_mobileworld.entity.Gpu;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ChiSoKhangBuiVaNuocRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.GpuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ChiSoKhangBuiVaNuocService {

    private final ChiSoKhangBuiVaNuocRepository repository;

    public ChiSoKhangBuiVaNuocService(ChiSoKhangBuiVaNuocRepository repository) {
        this.repository = repository;
    }

    public Page<ChiSoKhangBuiVaNuocDTO> getAllChiSoKhangBuiVaNuoc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private ChiSoKhangBuiVaNuocDTO toDTO(ChiSoKhangBuiVaNuoc entity) {
        return new ChiSoKhangBuiVaNuocDTO(entity.getId(), entity.getMa(), entity.getTenChiSo(), entity.getDeleted());
    }
}
