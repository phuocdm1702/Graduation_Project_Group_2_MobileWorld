package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.SimDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.HeDieuHanh;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.Sim;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.SimRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SimService {

    private final SimRepository simRepository;

    public SimService(SimRepository simRepository) {
        this.simRepository = simRepository;
    }

    public Page<SimDTO> getAllSim(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return simRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private SimDTO toDTO(Sim entity) {
        return new SimDTO(entity.getId(), entity.getMa(), entity.getSoLuongSimHoTro(), entity.getCacLoaiSimHoTro(), entity.getDeleted());
    }
}
