package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.HoTroCongNgheSacDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.NhaSanXuatDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.HoTroCongNgheSac;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.NhaSanXuat;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.HoTroCongNgheSacRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HoTroCongNgheSacService {

    private final HoTroCongNgheSacRepository hoTroCongNgheSacRepository;

    public HoTroCongNgheSacService(HoTroCongNgheSacRepository hoTroCongNgheSacRepository) {
        this.hoTroCongNgheSacRepository = hoTroCongNgheSacRepository;
    }

    public Page<HoTroCongNgheSacDTO> getAllCongNgheSac(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hoTroCongNgheSacRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private HoTroCongNgheSacDTO toDTO(HoTroCongNgheSac entity) {
        return new HoTroCongNgheSacDTO(entity.getId(), entity.getMa(), entity.getCongSac(), entity.getCongNgheHoTro(), entity.getDeleted());
    }
}