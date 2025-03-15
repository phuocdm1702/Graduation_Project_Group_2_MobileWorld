package com.example.graduation_project_group_2_mobileworld.service.san_pham.nha_san_xuat;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.nha_san_xuat.NhaSanXuatDTO;
import com.example.graduation_project_group_2_mobileworld.entity.NhaSanXuat;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.nha_san_xuat.NhaSanXuatRepository;
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
public class NhaSanXuatService {

    private final NhaSanXuatRepository repository;

    public NhaSanXuatService(NhaSanXuatRepository repository) {
        this.repository = repository;
    }

    public Page<NhaSanXuatDTO> getAllNhaSanXuat(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    public NhaSanXuatDTO getNhaSanXuatById(Integer id) {
        NhaSanXuat entity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Nhà sản xuất không tồn tại hoặc đã bị xóa!"));
        return toDTO(entity);
    }

    @Transactional
    public NhaSanXuatDTO createNhaSanXuat(NhaSanXuatDTO dto) {
        // Kiểm tra trùng lặp với các bản ghi chưa xóa mềm (deleted = false)
        if (repository.existsByMaAndDeletedFalse(dto.getMa())) {
            throw new RuntimeException("Mã nhà sản xuất đã tồn tại!");
        }
        if (repository.existsByNhaSanXuatAndDeletedFalse(dto.getNhaSanXuat())) {
            throw new RuntimeException("Tên nhà sản xuất đã tồn tại!");
        }

        // Kiểm tra xem có bản ghi đã xóa mềm với ma hoặc tên này không
        Optional<NhaSanXuat> existingNhaSanXuatByMa = repository.findByMaAndDeletedTrue(dto.getMa());
        Optional<NhaSanXuat> existingNhaSanXuatByName = repository.findByNhaSanXuatAndDeletedTrue(dto.getNhaSanXuat());

        if (existingNhaSanXuatByMa.isPresent()) {
            NhaSanXuat entity = existingNhaSanXuatByMa.get();
            entity.setDeleted(false);
            entity.setNhaSanXuat(dto.getNhaSanXuat());
            return toDTO(repository.save(entity));
        } else if (existingNhaSanXuatByName.isPresent()) {
            NhaSanXuat entity = existingNhaSanXuatByName.get();
            entity.setDeleted(false);
            entity.setMa(dto.getMa());
            return toDTO(repository.save(entity));
        } else {
            NhaSanXuat entity = new NhaSanXuat();
            entity.setMa(dto.getMa());
            entity.setNhaSanXuat(dto.getNhaSanXuat());
            entity.setDeleted(false);
            return toDTO(repository.save(entity));
        }
    }

    @Transactional
    public NhaSanXuatDTO updateNhaSanXuat(Integer id, NhaSanXuatDTO dto) {
        NhaSanXuat entity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Nhà sản xuất không tồn tại hoặc đã bị xóa!"));

        // Kiểm tra trùng lặp mã, loại trừ bản ghi hiện tại
        if (!entity.getMa().equals(dto.getMa()) && repository.existsByMaAndDeletedFalse(dto.getMa(), id)) {
            throw new RuntimeException("Mã nhà sản xuất đã tồn tại!");
        }

        // Kiểm tra trùng lặp tên, loại trừ bản ghi hiện tại
        if (!entity.getNhaSanXuat().equals(dto.getNhaSanXuat()) && repository.existsByNhaSanXuatAndDeletedFalse(dto.getNhaSanXuat(), id)) {
            throw new RuntimeException("Tên nhà sản xuất đã tồn tại!");
        }

        // Cập nhật thông tin
        entity.setMa(dto.getMa());
        entity.setNhaSanXuat(dto.getNhaSanXuat());
        return toDTO(repository.save(entity));
    }

    @Transactional
    public void deleteNhaSanXuat(Integer id) {
        repository.findByIdAndDeletedFalse(id)
                .ifPresentOrElse(
                        nsx -> {
                            nsx.setDeleted(true);
                            repository.save(nsx);
                        },
                        () -> {
                            throw new RuntimeException("Nhà sản xuất không tồn tại hoặc đã bị xóa!");
                        }
                );
    }

    @Transactional
    public void deleteMultipleNhaSanXuat(List<Integer> ids) {
        int updatedCount = repository.softDeleteByIds(ids);
        if (updatedCount == 0) {
            throw new RuntimeException("Không tìm thấy nhà sản xuất nào để xóa!");
        }
    }

    public Page<NhaSanXuatDTO> searchNhaSanXuat(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<NhaSanXuat> allResults = repository.findByDeletedFalse()
                .stream()
                .filter(n -> matchesKeyword(n, keyword))
                .collect(Collectors.toList());
        return toPage(allResults, pageable);
    }

    public Page<NhaSanXuatDTO> filterByNhaSanXuat(String nhaSanXuat, Pageable pageable) {
        List<NhaSanXuat> allResults = repository.findByDeletedFalse()
                .stream()
                .filter(n -> n.getNhaSanXuat().equalsIgnoreCase(nhaSanXuat))
                .collect(Collectors.toList());
        return toPage(allResults, pageable);
    }

    public List<String> getAllManufacturerNames() {
        return repository.findByDeletedFalse()
                .stream()
                .map(NhaSanXuat::getNhaSanXuat)
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

    public boolean existsByNhaSanXuat(String nhaSanXuat, Integer excludeId) {
        if (excludeId != null) {
            return repository.existsByNhaSanXuatAndDeletedFalse(nhaSanXuat, excludeId);
        }
        return repository.existsByNhaSanXuatAndDeletedFalse(nhaSanXuat);
    }

    private boolean matchesKeyword(NhaSanXuat nsx, String keyword) {
        String keywordLower = keyword.toLowerCase().replaceAll("\\s+", "");
        String maLower = nsx.getMa().toLowerCase().replaceAll("\\s+", "");
        String nhaSanXuatLower = nsx.getNhaSanXuat().toLowerCase();
        return maLower.contains(keywordLower) || nhaSanXuatLower.contains(keywordLower);
    }

    private Page<NhaSanXuatDTO> toPage(List<NhaSanXuat> results, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), results.size());
        List<NhaSanXuatDTO> subList = start < end ? results.subList(start, end).stream()
                .map(this::toDTO)
                .collect(Collectors.toList()) : List.of();
        return new PageImpl<>(subList, pageable, results.size());
    }

    private NhaSanXuatDTO toDTO(NhaSanXuat entity) {
        return new NhaSanXuatDTO(entity.getId(), entity.getMa(), entity.getNhaSanXuat(), entity.getDeleted());
    }
}