package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CpuDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.PinDTO;
import com.example.graduation_project_group_2_mobileworld.entity.Cpu;
import com.example.graduation_project_group_2_mobileworld.entity.Pin;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CpuRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.PinRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CpuService {

    private final CpuRepository cpuRepository;

    public CpuService(CpuRepository cpuRepository) {
        this.cpuRepository = cpuRepository;
    }

    public Page<CpuDTO> getAllCpu(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cpuRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private CpuDTO toDTO(Cpu entity) {
        return new CpuDTO(entity.getId(), entity.getMa(), entity.getTenCpu(), entity.getSoNhan(), entity.getDeleted());
    }
}
