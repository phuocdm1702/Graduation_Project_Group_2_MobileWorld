package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.MauSacDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.MauSac;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.MauSacRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MauSacService {

    private final MauSacRepository mauSacRepository;

    public MauSacService(MauSacRepository mauSacRepository) {
        this.mauSacRepository = mauSacRepository;
    }

    public Page<MauSacDTO> getAllMauSac(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mauSacRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    public List<MauSacDTO> getAllMauSacList() {
        return mauSacRepository.findByDeletedFalse().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MauSacDTO addMauSac(MauSacDTO mauSacDTO) {
        MauSac mauSac = new MauSac();
        mauSac.setMa(mauSacDTO.getMa());
        mauSac.setMauSac(mauSacDTO.getTenMau());
        mauSac.setDeleted(false);
        MauSac savedMauSac = mauSacRepository.save(mauSac);
        return toDTO(savedMauSac);
    }

    private MauSacDTO toDTO(MauSac entity) {
        return new MauSacDTO(entity.getId(), entity.getMa(), entity.getMauSac());
    }
}