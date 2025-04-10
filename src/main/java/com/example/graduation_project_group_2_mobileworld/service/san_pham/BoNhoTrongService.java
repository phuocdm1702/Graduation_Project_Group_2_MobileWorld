package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.BoNhoTrongDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.BoNhoTrong;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.BoNhoTrongRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BoNhoTrongService {

    private final BoNhoTrongRepository boNhoTrongRepository;

    public BoNhoTrongService(BoNhoTrongRepository boNhoTrongRepository) {
        this.boNhoTrongRepository = boNhoTrongRepository;
    }

    public Page<BoNhoTrongDTO> getAllBoNhoTrongs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return boNhoTrongRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    public BoNhoTrongDTO addBoNhoTrong(BoNhoTrongDTO boNhoTrongDTO) {
        BoNhoTrong boNhoTrong = new BoNhoTrong();
        boNhoTrong.setMa(boNhoTrongDTO.getMa());
        boNhoTrong.setDungLuongBoNhoTrong(boNhoTrongDTO.getDungLuong());
        boNhoTrong.setDeleted(false);
        BoNhoTrong savedBoNhoTrong = boNhoTrongRepository.save(boNhoTrong);
        return toDTO(savedBoNhoTrong);
    }

    private BoNhoTrongDTO toDTO(BoNhoTrong entity) {
        return new BoNhoTrongDTO(entity.getId(), entity.getMa(), entity.getDungLuongBoNhoTrong());
    }
}