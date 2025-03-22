package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.CongNgheManHinhDTO;
import com.example.graduation_project_group_2_mobileworld.entity.CongNgheManHinh;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CongNgheManHinhRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CongNgheManHinhService {

    private final CongNgheManHinhRepository repository;

    public CongNgheManHinhService(CongNgheManHinhRepository repository) {
        this.repository = repository;
    }

    public Page<CongNgheManHinhDTO> getAllCongNgheManHinh(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    @Transactional
    public CongNgheManHinhDTO createCongNgheManHinh(CongNgheManHinhDTO dto) {
        if (dto.getMa() == null || dto.getMa().isEmpty()) {
            throw new RuntimeException("Mã công nghệ màn hình không được để trống!");
        }
        if (dto.getCongNgheManHinh() == null || dto.getCongNgheManHinh().isEmpty()) {
            throw new RuntimeException("Tên công nghệ màn hình không được để trống!");
        }
        if (dto.getChuanManHinh() == null || dto.getChuanManHinh().isEmpty()) {
            throw new RuntimeException("Chuẩn màn hình không được để trống!");
        }

        if (existsByMa(dto.getMa(), null)) {
            throw new RuntimeException("Mã công nghệ màn hình đã tồn tại!");
        }
        if (existsByCongNgheManHinh(dto.getCongNgheManHinh(), null)) {
            throw new RuntimeException("Tên công nghệ màn hình đã tồn tại!");
        }
        if (existsByChuanManHinh(dto.getChuanManHinh(), null)) {
            throw new RuntimeException("Chuẩn màn hình đã tồn tại!");
        }

        Optional<CongNgheManHinh> existingByMa = repository.findByMaAndDeletedTrue(dto.getMa());
        Optional<CongNgheManHinh> existingByCongNgheManHinh = repository.findByCongNgheManHinhAndDeletedTrue(dto.getCongNgheManHinh());
        Optional<CongNgheManHinh> existingByChuan = repository.findByChuanManHinhAndDeletedTrue(dto.getChuanManHinh());

        CongNgheManHinh entity;
        if (existingByMa.isPresent()) {
            entity = existingByMa.get();
            entity.setDeleted(false);
            entity.setCongNgheManHinh(dto.getCongNgheManHinh());
            entity.setChuanManHinh(dto.getChuanManHinh());
        } else if (existingByCongNgheManHinh.isPresent()) {
            entity = existingByCongNgheManHinh.get();
            entity.setDeleted(false);
            entity.setMa(dto.getMa());
            entity.setChuanManHinh(dto.getChuanManHinh());
        } else if (existingByChuan.isPresent()) {
            entity = existingByChuan.get();
            entity.setDeleted(false);
            entity.setMa(dto.getMa());
            entity.setCongNgheManHinh(dto.getCongNgheManHinh());
        } else {
            entity = new CongNgheManHinh();
            entity.setMa(dto.getMa());
            entity.setCongNgheManHinh(dto.getCongNgheManHinh());
            entity.setChuanManHinh(dto.getChuanManHinh());
            entity.setDeleted(false);
        }

        return toDTO(repository.save(entity));
    }

    @Transactional
    public CongNgheManHinhDTO updateCongNgheManHinh(Integer id, CongNgheManHinhDTO dto) {
        CongNgheManHinh entity = repository.findById(id)
                .filter(d -> !d.getDeleted())
                .orElseThrow(() -> new RuntimeException("Công nghệ màn hình không tồn tại hoặc đã bị xóa!"));

        if (dto.getMa() == null || dto.getMa().isEmpty()) {
            throw new RuntimeException("Mã công nghệ màn hình không được để trống!");
        }
        if (dto.getCongNgheManHinh() == null || dto.getCongNgheManHinh().isEmpty()) {
            throw new RuntimeException("Tên công nghệ màn hình không được để trống!");
        }
        if (dto.getChuanManHinh() == null || dto.getChuanManHinh().isEmpty()) {
            throw new RuntimeException("Chuẩn màn hình không được để trống!");
        }

        if (!entity.getMa().equals(dto.getMa()) && existsByMa(dto.getMa(), id)) {
            throw new RuntimeException("Mã công nghệ màn hình đã tồn tại!");
        }
        if (!entity.getCongNgheManHinh().equals(dto.getCongNgheManHinh()) && existsByCongNgheManHinh(dto.getCongNgheManHinh(), id)) {
            throw new RuntimeException("Tên công nghệ màn hình đã tồn tại!");
        }
        if (!entity.getChuanManHinh().equals(dto.getChuanManHinh()) && existsByChuanManHinh(dto.getChuanManHinh(), id)) {
            throw new RuntimeException("Chuẩn màn hình đã tồn tại!");
        }

        entity.setMa(dto.getMa());
        entity.setCongNgheManHinh(dto.getCongNgheManHinh());
        entity.setChuanManHinh(dto.getChuanManHinh());
        return toDTO(repository.save(entity));
    }

    @Transactional
    public void deleteCongNgheManHinh(Integer id) {
        repository.findById(id)
                .filter(d -> !d.getDeleted())
                .ifPresentOrElse(
                        entity -> {
                            entity.setDeleted(true);
                            repository.save(entity);
                        },
                        () -> {
                            throw new RuntimeException("Công nghệ màn hình không tồn tại hoặc đã bị xóa!");
                        }
                );
    }

    @Transactional
    public void deleteMultipleCongNgheManHinh(List<Integer> ids) {
        int updatedCount = repository.softDeleteByIds(ids);
        if (updatedCount == 0) {
            throw new RuntimeException("Không tìm thấy công nghệ màn hình nào để xóa!");
        }
    }

    public Page<CongNgheManHinhDTO> searchCongNgheManHinh(String keyword, String congNgheManHinh, String chuanManHinh, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.searchByKeyword(keyword, congNgheManHinh, chuanManHinh, pageable).map(this::toDTO);
    }

    public boolean existsByMa(String ma, Integer excludeId) {
        return repository.existsByMaAndNotId(ma, excludeId);
    }

    public boolean existsByCongNgheManHinh(String congNgheManHinh, Integer excludeId) {
        return repository.existsByCongNgheManHinhAndNotId(congNgheManHinh, excludeId);
    }

    public boolean existsByChuanManHinh(String chuanManHinh, Integer excludeId) {
        return repository.existsByChuanManHinhAndNotId(chuanManHinh, excludeId);
    }

    private CongNgheManHinhDTO toDTO(CongNgheManHinh entity) {
        return new CongNgheManHinhDTO(entity.getId(), entity.getMa(), entity.getCongNgheManHinh(), entity.getChuanManHinh());
    }
}