package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CongNgheManHinhDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CongNgheMangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.CongNgheMang;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CongNgheMangRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<CongNgheMangDTO> getAllCongNgheMangList() {
        return ngheMangRepository.findByDeletedFalse().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private CongNgheMangDTO toDTO(CongNgheMang entity) {
        return new CongNgheMangDTO(entity.getId(), entity.getMa(), entity.getTenCongNgheMang(), entity.getDeleted());
    }
}
