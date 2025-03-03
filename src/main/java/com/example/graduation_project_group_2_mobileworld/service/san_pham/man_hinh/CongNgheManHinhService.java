package com.example.graduation_project_group_2_mobileworld.service.san_pham.man_hinh;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.man_hinh.CongNgheManHinhDTO;
import com.example.graduation_project_group_2_mobileworld.entity.CongNgheManHinh;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.man_hinh.CongNgheManHinhRepository;
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
        // Kiểm tra xem có bản ghi đã xóa mềm với mã, tên hoặc chuẩn màn hình không
        Optional<CongNgheManHinh> existingByMa = repository.findByMaAndDeletedTrue(dto.getMa());
        Optional<CongNgheManHinh> existingByCongNgheManHinh = repository.findByCongNgheManHinhAndDeletedTrue(dto.getCongNgheManHinh());
        Optional<CongNgheManHinh> existingByChuan = repository.findByChuanManHinhAndDeletedTrue(dto.getChuanManHinh());

        if (existingByMa.isPresent()) {
            // Khôi phục bản ghi đã xóa mềm với mã
            CongNgheManHinh entity = existingByMa.get();
            entity.setDeleted(false);
            entity.setCongNgheManHinh(dto.getCongNgheManHinh());
            entity.setChuanManHinh(dto.getChuanManHinh());
            return toDTO(repository.save(entity));
        } else if (existingByCongNgheManHinh.isPresent()) {
            // Khôi phục bản ghi đã xóa mềm với tên
            CongNgheManHinh entity = existingByCongNgheManHinh.get();
            entity.setDeleted(false);
            entity.setMa(dto.getMa());
            entity.setChuanManHinh(dto.getChuanManHinh());
            return toDTO(repository.save(entity));
        } else if (existingByChuan.isPresent()) {
            // Khôi phục bản ghi đã xóa mềm với chuẩn màn hình
            CongNgheManHinh entity = existingByChuan.get();
            entity.setDeleted(false);
            entity.setMa(dto.getMa());
            entity.setCongNgheManHinh(dto.getCongNgheManHinh());
            return toDTO(repository.save(entity));
        } else {
            // Nếu không có bản ghi nào bị xóa mềm, tạo mới
            CongNgheManHinh entity = new CongNgheManHinh();
            entity.setMa(dto.getMa());
            entity.setCongNgheManHinh(dto.getCongNgheManHinh());
            entity.setChuanManHinh(dto.getChuanManHinh());
            entity.setDeleted(false);
            return toDTO(repository.save(entity));
        }
    }

    @Transactional
    public CongNgheManHinhDTO updateCongNgheManHinh(Integer id, CongNgheManHinhDTO dto) {
        return repository.findById(id)
                .filter(d -> !d.getDeleted())
                .map(entity -> {
                    entity.setMa(dto.getMa());
                    entity.setCongNgheManHinh(dto.getCongNgheManHinh());
                    entity.setChuanManHinh(dto.getChuanManHinh());
                    return toDTO(repository.save(entity));
                })
                .orElseThrow(() -> new RuntimeException("Công nghệ màn hình không tồn tại hoặc đã bị xóa!"));
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

    public Page<CongNgheManHinhDTO> searchCongNgheManHinh(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.searchByKeyword(keyword, pageable).map(this::toDTO);
    }

    public boolean existsByMa(String ma) {
        return repository.existsByMa(ma);
    }

    public boolean existsByCongNgheManHinh(String congNgheManHinh) {
        return repository.existsByCongNgheManHinh(congNgheManHinh);
    }

    public boolean existsByChuanManHinh(String chuanManHinh) {
        return repository.existsByChuanManHinh(chuanManHinh);
    }

    private CongNgheManHinhDTO toDTO(CongNgheManHinh entity) {
        return new CongNgheManHinhDTO(entity.getId(), entity.getMa(), entity.getCongNgheManHinh(), entity.getChuanManHinh());
    }
}