package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ImelDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.Imel;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ImelRepository;
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
public class ImelService {

    private final ImelRepository repository;

    public ImelService(ImelRepository repository) {
        this.repository = repository;
    }

    public Page<ImelDTO> getAllImel(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    public ImelDTO getImelById(Integer id) {
        Imel entity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Imel không tồn tại hoặc đã bị xóa!"));
        return toDTO(entity);
    }

    @Transactional
    public ImelDTO createImel(ImelDTO dto) {
        // Validate IMEI format
        validateImelFormat(dto.getImel());

        // Kiểm tra trùng IMEI
        if (repository.existsByImelAndDeletedFalse(dto.getImel())) {
            throw new RuntimeException("IMEI đã tồn tại trong hệ thống!");
        }

        // Kiểm tra nếu IMEI đã tồn tại nhưng bị xóa (deleted = true)
        Optional<Imel> existingDeletedImel = repository.findByImelAndDeletedTrue(dto.getImel());

        if (existingDeletedImel.isPresent()) {
            // Khôi phục bản ghi đã xóa
            Imel entity = existingDeletedImel.get();
            entity.setDeleted(false);
            return toDTO(repository.save(entity));
        } else {
            // Tạo mới IMEI
            Imel entity = new Imel();
            entity.setImel(dto.getImel());
            entity.setDeleted(false);
            return toDTO(repository.save(entity));
        }
    }

    @Transactional
    public ImelDTO updateImel(Integer id, ImelDTO dto) {
        // Validate IMEI format
        validateImelFormat(dto.getImel());

        // Tìm IMEI cần cập nhật
        Imel entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imel không tồn tại!"));

        // Kiểm tra trùng IMEI (nếu có thay đổi)
        if (!entity.getImel().equals(dto.getImel())) {
            if (repository.existsByImelAndIdNot(dto.getImel(), id)) {
                throw new RuntimeException("IMEI đã tồn tại trong hệ thống!");
            }
        }

        // Chỉ cập nhật trường imel (giữ nguyên các trường khác)
        entity.setImel(dto.getImel());

        return toDTO(repository.save(entity));
    }

    @Transactional
    public void deleteImel(Integer id) {
        repository.findByIdAndDeletedFalse(id)
                .ifPresentOrElse(
                        imel -> {
                            imel.setDeleted(true);
                            repository.save(imel);
                        },
                        () -> {
                            throw new RuntimeException("Imel không tồn tại hoặc đã bị xóa!");
                        }
                );
    }

    @Transactional
    public void deleteMultipleImel(List<Integer> ids) {
        int updatedCount = repository.softDeleteByIds(ids);
        if (updatedCount == 0) {
            throw new RuntimeException("Không tìm thấy Imel nào để xóa!");
        }
    }

    public Page<ImelDTO> searchImel(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Imel> allResults = repository.findByDeletedFalse()
                .stream()
                .filter(i -> matchesKeyword(i, keyword))
                .collect(Collectors.toList());
        return toPage(allResults, pageable);
    }

    public Page<ImelDTO> filterByImel(String imel, Pageable pageable) {
        List<Imel> allResults = repository.findByDeletedFalse()
                .stream()
                .filter(i -> i.getImel().equalsIgnoreCase(imel))
                .collect(Collectors.toList());
        return toPage(allResults, pageable);
    }

    public List<String> getAllImelNames() {
        return repository.findByDeletedFalse()
                .stream()
                .map(Imel::getImel)
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

    public boolean existsByImel(String imel, Integer excludeId) {
        if (excludeId != null) {
            return repository.existsByImelAndDeletedFalse(imel, excludeId);
        }
        return repository.existsByImelAndDeletedFalse(imel);
    }

    private boolean matchesKeyword(Imel imel, String keyword) {
        String keywordLower = keyword.toLowerCase().replaceAll("\\s+", "");
        String maLower = imel.getMa().toLowerCase().replaceAll("\\s+", "");
        String imelLower = imel.getImel().toLowerCase();
        return maLower.contains(keywordLower) || imelLower.contains(keywordLower);
    }

    private Page<ImelDTO> toPage(List<Imel> results, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), results.size());
        List<ImelDTO> subList = start < end ? results.subList(start, end).stream()
                .map(this::toDTO)
                .collect(Collectors.toList()) : List.of();
        return new PageImpl<>(subList, pageable, results.size());
    }

    private ImelDTO toDTO(Imel entity) {
        return new ImelDTO(entity.getId(), entity.getImel(), entity.getDeleted());
    }

    private void validateImelFormat(String imel) {
        if (imel == null || !imel.matches("\\d{15}")) {
            throw new RuntimeException("IMEI phải là chuỗi 15 chữ số!");
        }
    }
}