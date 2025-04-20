package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ChiSoKhangBuiVaNuocDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CongNgheMangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiSoKhangBuiVaNuoc;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ChiSoKhangBuiVaNuocRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<ChiSoKhangBuiVaNuocDTO> getAllChiSoKhangBuiVaNuocList() {
        return repository.findByDeletedFalse().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ChiSoKhangBuiVaNuocDTO toDTO(ChiSoKhangBuiVaNuoc entity) {
        return new ChiSoKhangBuiVaNuocDTO(entity.getId(), entity.getMa(), entity.getTenChiSo(), entity.getDeleted());
    }
}
