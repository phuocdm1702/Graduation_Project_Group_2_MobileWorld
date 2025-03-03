package com.example.graduation_project_group_2_mobileworld.service.san_pham.man_hinh;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.man_hinh.ManHinhDTO;
import com.example.graduation_project_group_2_mobileworld.entity.CongNgheManHinh;
import com.example.graduation_project_group_2_mobileworld.entity.ManHinh;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.man_hinh.CongNgheManHinhRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.man_hinh.ManHinhRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManHinhService {

    private final ManHinhRepository repository;
    private final CongNgheManHinhRepository congNgheManHinhRepository;

    public ManHinhService(ManHinhRepository repository, CongNgheManHinhRepository congNgheManHinhRepository) {
        this.repository = repository;
        this.congNgheManHinhRepository = congNgheManHinhRepository;
    }

    public Page<ManHinhDTO> getAllManHinh(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    @Transactional
    public ManHinhDTO createManHinh(ManHinhDTO dto, CongNgheManHinhRepository ignored) { // Tham số này không cần thiết, giữ lại để tương thích với code cũ
        Optional<ManHinh> existingManHinhByMa = repository.findByMaAndDeletedTrue(dto.getMa());
        Optional<ManHinh> existingManHinhByType = repository.findByKieuManHinhAndDeletedTrue(dto.getKieuManHinh());

        if (existingManHinhByMa.isPresent()) {
            ManHinh entity = existingManHinhByMa.get();
            entity.setDeleted(false);
            updateManHinhFields(entity, dto);
            return toDTO(repository.save(entity));
        } else if (existingManHinhByType.isPresent()) {
            ManHinh entity = existingManHinhByType.get();
            entity.setDeleted(false);
            updateManHinhFields(entity, dto);
            return toDTO(repository.save(entity));
        } else {
            ManHinh entity = new ManHinh();
            updateManHinhFields(entity, dto);
            entity.setDeleted(false);
            return toDTO(repository.save(entity));
        }
    }

    @Transactional
    public ManHinhDTO updateManHinh(Integer id, ManHinhDTO dto, CongNgheManHinhRepository ignored) { // Tham số này không cần thiết
        return repository.findById(id)
                .filter(mh -> !mh.getDeleted())
                .map(manHinh -> {
                    updateManHinhFields(manHinh, dto);
                    return toDTO(repository.save(manHinh));
                })
                .orElseThrow(() -> new RuntimeException("Màn hình không tồn tại hoặc đã bị xóa!"));
    }

    @Transactional
    public void deleteManHinh(Integer id) {
        repository.findById(id)
                .filter(mh -> !mh.getDeleted())
                .ifPresentOrElse(
                        manHinh -> {
                            manHinh.setDeleted(true);
                            repository.save(manHinh);
                        },
                        () -> {
                            throw new RuntimeException("Màn hình không tồn tại hoặc đã bị xóa!");
                        }
                );
    }

    @Transactional
    public void deleteMultipleManHinh(List<Integer> ids) {
        int updatedCount = repository.softDeleteByIds(ids);
        if (updatedCount == 0) {
            throw new RuntimeException("Không tìm thấy màn hình nào để xóa!");
        }
    }

    public Page<ManHinhDTO> searchManHinh(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.searchByKeyword(keyword, pageable).map(this::toDTO);
    }

    public boolean existsByMa(String ma) {
        return repository.existsByMa(ma);
    }

    public boolean existsByKieuManHinh(String kieuManHinh) {
        return repository.existsByKieuManHinh(kieuManHinh);
    }

    private void updateManHinhFields(ManHinh entity, ManHinhDTO dto) {
        CongNgheManHinh congNghe = congNgheManHinhRepository.findById(dto.getIdCongNgheManHinh())
                .orElseThrow(() -> new RuntimeException("Công nghệ màn hình không tồn tại"));
        entity.setIdCongNgheManHinh(congNghe);
        entity.setMa(dto.getMa());
        entity.setKichThuoc(dto.getKichThuoc());
        entity.setDoPhanGiai(dto.getDoPhanGiai());
        entity.setDoSangToiDa(dto.getDoSangToiDa());
        entity.setTanSoQuet(dto.getTanSoQuet());
        entity.setKieuManHinh(dto.getKieuManHinh());
    }

    private ManHinhDTO toDTO(ManHinh entity) {
        return new ManHinhDTO(
                entity.getId(),
                entity.getIdCongNgheManHinh().getId(),
                entity.getMa(),
                entity.getKichThuoc(),
                entity.getDoPhanGiai(),
                entity.getDoSangToiDa(),
                entity.getTanSoQuet(),
                entity.getKieuManHinh()
        );
    }
}