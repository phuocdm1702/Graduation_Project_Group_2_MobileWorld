package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ImelDTO;
import com.example.graduation_project_group_2_mobileworld.entity.Imel;
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
        if (repository.existsByMaAndDeletedFalse(dto.getMa())) {
            throw new RuntimeException("Mã Imel đã tồn tại!");
        }
        if (repository.existsByImelAndDeletedFalse(dto.getImel())) {
            throw new RuntimeException("Tên Imel đã tồn tại!");
        }

        Optional<Imel> existingImelByMa = repository.findByMaAndDeletedTrue(dto.getMa());
        Optional<Imel> existingImelByName = repository.findByImelAndDeletedTrue(dto.getImel());

        if (existingImelByMa.isPresent()) {
            Imel entity = existingImelByMa.get();
            entity.setDeleted(false);
            entity.setImel(dto.getImel());
            return toDTO(repository.save(entity));
        } else if (existingImelByName.isPresent()) {
            Imel entity = existingImelByName.get();
            entity.setDeleted(false);
            entity.setMa(dto.getMa());
            return toDTO(repository.save(entity));
        } else {
            Imel entity = new Imel();
            entity.setMa(dto.getMa());
            entity.setImel(dto.getImel());
            entity.setDeleted(false);
            return toDTO(repository.save(entity));
        }
    }

    @Transactional
    public ImelDTO updateImel(Integer id, ImelDTO dto) {
        Imel entity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Imel không tồn tại hoặc đã bị xóa!"));

        if (!entity.getMa().equals(dto.getMa()) && repository.existsByMaAndDeletedFalse(dto.getMa(), id)) {
            throw new RuntimeException("Mã Imel đã tồn tại!");
        }

        if (!entity.getImel().equals(dto.getImel()) && repository.existsByImelAndDeletedFalse(dto.getImel(), id)) {
            throw new RuntimeException("Tên Imel đã tồn tại!");
        }

        entity.setMa(dto.getMa());
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
        return new ImelDTO(entity.getId(), entity.getMa(), entity.getImel(), entity.getDeleted());
    }
}