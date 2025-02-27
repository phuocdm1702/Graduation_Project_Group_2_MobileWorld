package com.example.graduation_project_group_2_mobileworld.service.dongSanPhamService;

import com.example.graduation_project_group_2_mobileworld.dto.dongSanPhamDTO.DongSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import com.example.graduation_project_group_2_mobileworld.repository.dongSanPham.DongSanPhamRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DongSanPhamService {

    private final DongSanPhamRepository repository;

    public DongSanPhamService(DongSanPhamRepository repository) {
        this.repository = repository;
    }

    public List<DongSanPhamDTO> getAllDongSanPham() {
        return repository.findByDeletedFalse()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Page<DongSanPhamDTO> getAllDongSanPham(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    @Transactional
    public DongSanPhamDTO createDongSanPham(DongSanPhamDTO dto) {
        DongSanPham entity = new DongSanPham();
        entity.setMa(dto.getMa());
        entity.setDongSanPham(dto.getDongSanPham());
        entity.setDeleted(false);
        return toDTO(repository.save(entity));
    }

    @Transactional
    public DongSanPhamDTO updateDongSanPham(Integer id, DongSanPhamDTO dto) {
        return repository.findById(id)
                .filter(d -> !d.getDeleted())
                .map(dsp -> {
                    dsp.setMa(dto.getMa());
                    dsp.setDongSanPham(dto.getDongSanPham());
                    return toDTO(repository.save(dsp));
                })
                .orElseThrow(() -> new RuntimeException("Dòng sản phẩm không tồn tại hoặc đã bị xóa!"));
    }

    @Transactional
    public void deleteDongSanPham(Integer id) {
        repository.findById(id)
                .filter(d -> !d.getDeleted())
                .ifPresentOrElse(
                        dsp -> {
                            dsp.setDeleted(true);
                            repository.save(dsp);
                        },
                        () -> {
                            throw new RuntimeException("Dòng sản phẩm không tồn tại hoặc đã bị xóa!");
                        }
                );
    }

    @Transactional
    public void deleteMultipleDongSanPham(List<Integer> ids) {
        int updatedCount = repository.softDeleteByIds(ids);
        if (updatedCount == 0) {
            throw new RuntimeException("Không tìm thấy dòng sản phẩm nào để xóa!");
        }
    }

    public Page<DongSanPhamDTO> searchDongSanPham(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.searchByKeyword(keyword, pageable).map(this::toDTO);
    }

    public boolean existsByMa(String ma) {
        return repository.existsByMa(ma);
    }

    public boolean existsByDongSanPham(String dongSanPham) {
        return repository.existsByDongSanPham(dongSanPham);
    }

    private DongSanPhamDTO toDTO(DongSanPham entity) {
        return new DongSanPhamDTO(entity.getId(), entity.getMa(), entity.getDongSanPham());
    }
}