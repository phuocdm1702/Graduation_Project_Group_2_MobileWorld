package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.RamDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.SimDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.Ram;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.RamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RamService {

    private final RamRepository ramRepository;

    public RamService(RamRepository ramRepository) {
        this.ramRepository = ramRepository;
    }

    public Page<RamDTO> getAllRams(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ramRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    public List<RamDTO> getAllRamsList() {
        return ramRepository.findByDeletedFalse().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RamDTO addRam(RamDTO ramDTO) {
        Ram ram = new Ram();
        ram.setMa(ramDTO.getMa());
        ram.setDungLuongRam(ramDTO.getDungLuong());
        ram.setDeleted(false);
        Ram savedRam = ramRepository.save(ram);
        return toDTO(savedRam);
    }

    private RamDTO toDTO(Ram entity) {
        return new RamDTO(entity.getId(), entity.getMa(), entity.getDungLuongRam());
    }
}