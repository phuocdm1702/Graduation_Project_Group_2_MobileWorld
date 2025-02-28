package com.example.graduation_project_group_2_mobileworld.service.san_pham.Imel;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.imel.ImelDTO;
import com.example.graduation_project_group_2_mobileworld.entity.Imel;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.imel.ImelRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public ImelDTO createImel(ImelDTO dto) {
        // Kiểm tra xem có bản ghi đã xóa mềm với ma hoặc imel không
        Optional<Imel> existingImelByMa = repository.findByMaAndDeletedTrue(dto.getMa());
        Optional<Imel> existingImelByImel = repository.findByImelAndDeletedTrue(dto.getImel());

        if (existingImelByMa.isPresent()) {
            // Khôi phục bản ghi đã xóa mềm với ma
            Imel entity = existingImelByMa.get();
            entity.setDeleted(false);
            entity.setImel(dto.getImel()); // Cập nhật giá trị mới
            return toDTO(repository.save(entity));
        } else if (existingImelByImel.isPresent()) {
            // Khôi phục bản ghi đã xóa mềm với imel
            Imel entity = existingImelByImel.get();
            entity.setDeleted(false);
            entity.setMa(dto.getMa()); // Cập nhật giá trị mới
            return toDTO(repository.save(entity));
        } else {
            // Nếu không có bản ghi nào bị xóa mềm, tạo mới
            Imel entity = new Imel();
            entity.setMa(dto.getMa());
            entity.setImel(dto.getImel());
            entity.setDeleted(false);
            return toDTO(repository.save(entity));
        }
    }

    @Transactional
    public ImelDTO updateImel(Integer id, ImelDTO dto) {
        return repository.findById(id)
                .filter(d -> !d.getDeleted())
                .map(imel -> {
                    imel.setMa(dto.getMa());
                    imel.setImel(dto.getImel());
                    return toDTO(repository.save(imel));
                })
                .orElseThrow(() -> new RuntimeException("Imel không tồn tại hoặc đã bị xóa!"));
    }

    @Transactional
    public void deleteImel(Integer id) {
        repository.findById(id)
                .filter(d -> !d.getDeleted())
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
        return repository.searchByKeyword(keyword, pageable).map(this::toDTO);
    }

    public boolean existsByMa(String ma) {
        return repository.existsByMa(ma);
    }

    public boolean existsByImel(String imel) {
        return repository.existsByImel(imel);
    }

    private ImelDTO toDTO(Imel entity) {
        return new ImelDTO(entity.getId(), entity.getMa(), entity.getImel());
    }
}