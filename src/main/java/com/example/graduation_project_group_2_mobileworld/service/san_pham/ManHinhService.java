package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ManHinhDTO;
import com.example.graduation_project_group_2_mobileworld.entity.CongNgheManHinh;
import com.example.graduation_project_group_2_mobileworld.entity.ManHinh;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ManHinhRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManHinhService {

    private final ManHinhRepository repository;

    public ManHinhService(ManHinhRepository repository) {
        this.repository = repository;
    }

    public Page<ManHinhDTO> getAllManHinh(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    public ManHinhDTO getManHinhById(Integer id) {
        ManHinh entity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Màn hình không tồn tại hoặc đã bị xóa!"));
        return toDTO(entity);
    }

    @Transactional
    public ManHinhDTO createManHinh(ManHinhDTO dto) {
        if (dto.getIdCongNgheManHinh() == null) {
            throw new RuntimeException("ID Công nghệ màn hình không được để trống!");
        }
        if (repository.existsByMaAndDeletedFalse(dto.getMa())) {
            throw new RuntimeException("Mã màn hình đã tồn tại!");
        }
        // Xóa kiểm tra trùng lặp kieuManHinh

        Optional<ManHinh> existingManHinhByMa = repository.findByMaAndDeletedTrue(dto.getMa());

        ManHinh entity;
        if (existingManHinhByMa.isPresent()) {
            entity = existingManHinhByMa.get();
            entity.setDeleted(false);
        } else {
            entity = new ManHinh();
        }

        // Gán CongNgheManHinh từ ID
        CongNgheManHinh congNgheManHinh = new CongNgheManHinh();
        congNgheManHinh.setId(dto.getIdCongNgheManHinh());
        entity.setIdCongNgheManHinh(congNgheManHinh);

        entity.setMa(dto.getMa());
        entity.setKichThuoc(dto.getKichThuoc());
        entity.setDoPhanGiai(dto.getDoPhanGiai());
        entity.setDoSangToiDa(dto.getDoSangToiDa());
        entity.setTanSoQuet(dto.getTanSoQuet());
        entity.setKieuManHinh(dto.getKieuManHinh());
        return toDTO(repository.save(entity));
    }

    @Transactional
    public ManHinhDTO updateManHinh(Integer id, ManHinhDTO dto) {
        ManHinh entity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Màn hình không tồn tại hoặc đã bị xóa!"));

        if (dto.getIdCongNgheManHinh() == null) {
            throw new RuntimeException("ID Công nghệ màn hình không được để trống!");
        }
        if (!entity.getMa().equals(dto.getMa()) && repository.existsByMaAndDeletedFalse(dto.getMa())) {
            throw new RuntimeException("Mã màn hình đã tồn tại!");
        }
        // Xóa kiểm tra trùng lặp kieuManHinh ở đây

        // Gán CongNgheManHinh từ ID
        CongNgheManHinh congNgheManHinh = new CongNgheManHinh();
        congNgheManHinh.setId(dto.getIdCongNgheManHinh());
        entity.setIdCongNgheManHinh(congNgheManHinh);

        entity.setMa(dto.getMa());
        entity.setKichThuoc(dto.getKichThuoc());
        entity.setDoPhanGiai(dto.getDoPhanGiai());
        entity.setDoSangToiDa(dto.getDoSangToiDa());
        entity.setTanSoQuet(dto.getTanSoQuet());
        entity.setKieuManHinh(dto.getKieuManHinh());
        return toDTO(repository.save(entity));
    }

    @Transactional
    public void deleteManHinh(Integer id) {
        repository.findByIdAndDeletedFalse(id)
                .ifPresentOrElse(
                        mh -> {
                            mh.setDeleted(true);
                            repository.save(mh);
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

    public Page<ManHinhDTO> searchManHinh(String keyword, String kieuManHinh, Integer idCongNgheManHinh, String kichThuoc, String doPhanGiai, String doSangToiDa, String tanSoQuet, Pageable pageable) {
        List<ManHinh> allResults = repository.findByDeletedFalse();

        if (keyword != null && !keyword.isEmpty()) {
            allResults = allResults.stream()
                    .filter(m -> m.getMa().toLowerCase().contains(keyword.toLowerCase()) ||
                            m.getKichThuoc().toLowerCase().contains(keyword.toLowerCase()) ||
                            m.getDoPhanGiai().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (kieuManHinh != null && !kieuManHinh.isEmpty()) {
            allResults = allResults.stream()
                    .filter(m -> m.getKieuManHinh().equalsIgnoreCase(kieuManHinh))
                    .collect(Collectors.toList());
        }
        if (idCongNgheManHinh != null) {
            allResults = allResults.stream()
                    .filter(m -> m.getIdCongNgheManHinh().getId().equals(idCongNgheManHinh))
                    .collect(Collectors.toList());
        }
        if (kichThuoc != null && !kichThuoc.isEmpty()) {
            allResults = allResults.stream()
                    .filter(m -> m.getKichThuoc().replaceAll("\\s+", "").toLowerCase().contains(kichThuoc.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (doPhanGiai != null && !doPhanGiai.isEmpty()) {
            allResults = allResults.stream()
                    .filter(m -> m.getDoPhanGiai().toLowerCase().contains(doPhanGiai.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (doSangToiDa != null && !doSangToiDa.isEmpty()) {
            allResults = allResults.stream()
                    .filter(m -> m.getDoSangToiDa().replaceAll("\\s+", "").toLowerCase().contains(doSangToiDa.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (tanSoQuet != null && !tanSoQuet.isEmpty()) {
            allResults = allResults.stream()
                    .filter(m -> m.getTanSoQuet().toLowerCase().contains(tanSoQuet.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return toPage(allResults, pageable);
    }

    public Page<ManHinhDTO> filterByKieuManHinh(String kieuManHinh, Pageable pageable) {
        List<ManHinh> allResults = repository.findByDeletedFalse()
                .stream()
                .filter(m -> m.getKieuManHinh().equalsIgnoreCase(kieuManHinh))
                .collect(Collectors.toList());
        return toPage(allResults, pageable);
    }

    public List<String> getAllKieuManHinhNames() {
        return repository.findByDeletedFalse()
                .stream()
                .map(ManHinh::getKieuManHinh)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public boolean existsByMa(String ma, Integer excludeId) {
        if (excludeId != null) {
            return repository.existsByMaAndDeletedFalse(ma, excludeId);
        }
        return repository.existsByMaAndDeletedFalse(ma);
    }

    public boolean existsByKieuManHinh(String kieuManHinh, Integer excludeId) {
        if (excludeId != null) {
            return repository.existsByKieuManHinhAndDeletedFalse(kieuManHinh, excludeId);
        }
        return repository.existsByKieuManHinhAndDeletedFalse(kieuManHinh);
    }

    private Page<ManHinhDTO> toPage(List<ManHinh> results, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), results.size());
        List<ManHinhDTO> subList = start < end ? results.subList(start, end).stream()
                .map(this::toDTO)
                .collect(Collectors.toList()) : List.of();
        return new PageImpl<>(subList, pageable, results.size());
    }

    private ManHinhDTO toDTO(ManHinh entity) {
        Integer idCongNgheManHinh = (entity.getIdCongNgheManHinh() != null) ? entity.getIdCongNgheManHinh().getId() : null;
        return new ManHinhDTO(
                entity.getId(),
                idCongNgheManHinh,
                entity.getMa(),
                entity.getKichThuoc(),
                entity.getDoPhanGiai(),
                entity.getDoSangToiDa(),
                entity.getTanSoQuet(),
                entity.getKieuManHinh(),
                entity.getDeleted()
        );
    }
}