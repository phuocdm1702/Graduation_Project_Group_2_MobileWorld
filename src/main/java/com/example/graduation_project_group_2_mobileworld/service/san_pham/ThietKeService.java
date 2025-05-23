package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.MauSacDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ThietKeDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ThietKe;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ThietKeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<ThietKeDTO> getAllThietKeList() {
        return thietKeRepository.findByDeletedFalse().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ThietKeDTO toDTO(ThietKe entity) {
        return new ThietKeDTO(entity.getId(), entity.getMa(), entity.getChatLieuKhung(), entity.getChatLieuMatLung(), entity.getDeleted());
    }
}
