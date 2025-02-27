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
        Imel entity = new Imel();
        entity.setMa(dto.getMa());
        entity.setImel(dto.getImel());
        entity.setDeleted(false);
        return toDTO(repository.save(entity));
    }

    @Transactional
    public ImelDTO updateImel(Integer id, ImelDTO dto) {
        return repository.findById(id)
                .filter(d -> !d.getDeleted())
                .map(dsp -> {
                    dsp.setMa(dto.getMa());
                    dsp.setImel(dto.getImel());
                    return toDTO(repository.save(dsp));
                })
                .orElseThrow(() -> new RuntimeException("Imel không tồn tại hoặc đã bị xóa!"));
    }

    @Transactional
    public void deleteImel(Integer id) {
        repository.findById(id)
                .filter(d -> !d.getDeleted())
                .ifPresentOrElse(
                        dsp -> {
                            dsp.setDeleted(true);
                            repository.save(dsp);
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
            throw new RuntimeException("Không tìm thấy imel nào để xóa!");
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