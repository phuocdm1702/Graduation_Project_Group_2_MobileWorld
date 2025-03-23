package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.SimDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ThietKeDTO;
import com.example.graduation_project_group_2_mobileworld.entity.Sim;
import com.example.graduation_project_group_2_mobileworld.entity.ThietKe;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.SimRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ThietKeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ThietKeService {

    private final ThietKeRepository thietKeRepository;

    public ThietKeService(ThietKeRepository thietKeRepository) {
        this.thietKeRepository = thietKeRepository;
    }

    public Page<ThietKeDTO> getAllThietKe(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return thietKeRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private ThietKeDTO toDTO(ThietKe entity) {
        return new ThietKeDTO(entity.getId(), entity.getMa(), entity.getChatLieuKhung(), entity.getChatLieuMatLung(), entity.getDeleted());
    }
}
