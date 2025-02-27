package com.example.graduation_project_group_2_mobileworld.service.san_pham.nha_san_xuat;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.nha_san_xuat.NhaSanXuatDTO;
import com.example.graduation_project_group_2_mobileworld.entity.NhaSanXuat;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.nha_san_xuat.NhaSanXuatRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NhaSanXuatService {

    private final NhaSanXuatRepository repository;

    public NhaSanXuatService(NhaSanXuatRepository repository) {
        this.repository = repository;
    }

    public List<NhaSanXuatDTO> getAllNhaSanXuat() {
        return repository.findByDeletedFalse()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Page<NhaSanXuatDTO> getAllNhaSanXuat(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    @Transactional
    public NhaSanXuatDTO createNhaSanXuat(NhaSanXuatDTO dto) {
        NhaSanXuat entity = new NhaSanXuat();
        entity.setMa(dto.getMa());
        entity.setNhaSanXuat(dto.getNhaSanXuat());
        entity.setDeleted(false);
        return toDTO(repository.save(entity));
    }

    @Transactional
    public NhaSanXuatDTO updateNhaSanXuat(Integer id, NhaSanXuatDTO dto) {
        return repository.findById(id)
                .filter(d -> !d.getDeleted())
                .map(dsp -> {
                    dsp.setMa(dto.getMa());
                    dsp.setNhaSanXuat(dto.getNhaSanXuat());
                    return toDTO(repository.save(dsp));
                })
                .orElseThrow(() -> new RuntimeException("Nhà sản xuất không tồn tại hoặc đã bị xóa!"));
    }

    @Transactional
    public void deleteNhaSanXuat(Integer id) {
        repository.findById(id)
                .filter(d -> !d.getDeleted())
                .ifPresentOrElse(
                        dsp -> {
                            dsp.setDeleted(true);
                            repository.save(dsp);
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
        return repository.searchByKeyword(keyword, pageable).map(this::toDTO);
    }

    public boolean existsByMa(String ma) {
        return repository.existsByMa(ma);
    }

    public boolean existsByNhaSanXuat(String nhaSanXuat) {
        return repository.existsByNhaSanXuat(nhaSanXuat);
    }

    private NhaSanXuatDTO toDTO(NhaSanXuat entity) {
        return new NhaSanXuatDTO(entity.getId(), entity.getMa(), entity.getNhaSanXuat());
    }
}