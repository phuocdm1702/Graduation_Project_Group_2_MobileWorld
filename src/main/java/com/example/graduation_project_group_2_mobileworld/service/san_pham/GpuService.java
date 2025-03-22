package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CpuDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.GpuDTO;
import com.example.graduation_project_group_2_mobileworld.entity.Cpu;
import com.example.graduation_project_group_2_mobileworld.entity.Gpu;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CpuRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.GpuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GpuService {

    private final GpuRepository gpuRepository;

    public GpuService(GpuRepository gpuRepository) {
        this.gpuRepository = gpuRepository;
    }

    public Page<GpuDTO> getAllGpu(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return gpuRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private GpuDTO toDTO(Gpu entity) {
        return new GpuDTO(entity.getId(), entity.getMa(), entity.getTenGpu(), entity.getDeleted());
    }
}
