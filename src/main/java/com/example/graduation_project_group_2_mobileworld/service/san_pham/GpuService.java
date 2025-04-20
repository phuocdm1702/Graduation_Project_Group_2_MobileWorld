package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.GpuDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.HeDieuHanhDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.Gpu;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.GpuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<GpuDTO> getAllGpuList() {
        return gpuRepository.findByDeletedFalse().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private GpuDTO toDTO(Gpu entity) {
        return new GpuDTO(entity.getId(), entity.getMa(), entity.getTenGpu(), entity.getDeleted());
    }
}
