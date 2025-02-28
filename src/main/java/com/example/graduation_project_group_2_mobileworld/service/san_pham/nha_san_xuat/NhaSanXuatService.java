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
import java.util.Optional;

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

    @Transactional
    public NhaSanXuatDTO createNhaSanXuat(NhaSanXuatDTO dto) {
        // Kiểm tra xem có bản ghi đã xóa mềm với ma hoặc nhaSanXuat không
        Optional<NhaSanXuat> existingNhaSanXuatByMa = repository.findByMaAndDeletedTrue(dto.getMa());
        Optional<NhaSanXuat> existingNhaSanXuatByName = repository.findByNhaSanXuatAndDeletedTrue(dto.getNhaSanXuat());

        if (existingNhaSanXuatByMa.isPresent()) {
            // Khôi phục bản ghi đã xóa mềm với ma
            NhaSanXuat entity = existingNhaSanXuatByMa.get();
            entity.setDeleted(false);
            entity.setNhaSanXuat(dto.getNhaSanXuat()); // Cập nhật giá trị mới
            return toDTO(repository.save(entity));
        } else if (existingNhaSanXuatByName.isPresent()) {
            // Khôi phục bản ghi đã xóa mềm với nhaSanXuat
            NhaSanXuat entity = existingNhaSanXuatByName.get();
            entity.setDeleted(false);
            entity.setMa(dto.getMa()); // Cập nhật giá trị mới
            return toDTO(repository.save(entity));
        } else {
            // Nếu không có bản ghi nào bị xóa mềm, tạo mới
            NhaSanXuat entity = new NhaSanXuat();
            entity.setMa(dto.getMa());
            entity.setNhaSanXuat(dto.getNhaSanXuat());
            entity.setDeleted(false);
            return toDTO(repository.save(entity));
        }
    }

    @Transactional
    public NhaSanXuatDTO updateNhaSanXuat(Integer id, NhaSanXuatDTO dto) {
        return repository.findById(id)
                .filter(d -> !d.getDeleted())
                .map(nsx -> {
                    nsx.setMa(dto.getMa());
                    nsx.setNhaSanXuat(dto.getNhaSanXuat());
                    return toDTO(repository.save(nsx));
                })
                .orElseThrow(() -> new RuntimeException("Nhà sản xuất không tồn tại hoặc đã bị xóa!"));
    }

    @Transactional
    public void deleteNhaSanXuat(Integer id) {
        repository.findById(id)
                .filter(d -> !d.getDeleted())
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