package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.GpuDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.TinhTrangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.Gpu;
import com.example.graduation_project_group_2_mobileworld.entity.TinhTrang;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.GpuRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.TinhTrangRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TinhTrangService {

    private final TinhTrangRepository repository;

    public TinhTrangService(TinhTrangRepository repository) {
        this.repository = repository;
    }

    public Page<TinhTrangDTO> getAllTinhTrang(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private TinhTrangDTO toDTO(TinhTrang entity) {
        return new TinhTrangDTO(entity.getId(), entity.getMa(), entity.getLoaiTinhTrang(), entity.getDeleted());
    }
}
