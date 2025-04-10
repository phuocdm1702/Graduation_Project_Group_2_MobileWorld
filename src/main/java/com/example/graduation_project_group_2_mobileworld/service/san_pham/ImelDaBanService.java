package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ImelDaBanDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ImelDaBan;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ImelDaBanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ImelDaBanService {

    private final ImelDaBanRepository repository;

    public ImelDaBanService(ImelDaBanRepository repository) {
        this.repository = repository;
    }

    public Page<ImelDaBanDTO> getAllImelDaBan(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    private ImelDaBanDTO toDTO(ImelDaBan entity) {
        return new ImelDaBanDTO(
                entity.getId(),
                entity.getMa(),
                entity.getImel(),
                entity.getNgayBan(),
                entity.getGhiChu(),
                entity.getDeleted()
        );
    }
}