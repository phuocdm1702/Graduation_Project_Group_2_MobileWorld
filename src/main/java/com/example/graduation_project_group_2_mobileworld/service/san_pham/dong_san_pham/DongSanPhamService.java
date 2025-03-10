package com.example.graduation_project_group_2_mobileworld.service.san_pham.dong_san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.dong_san_pham.DongSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.dong_san_pham.DongSanPhamRepository;
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
public class DongSanPhamService {

    private final DongSanPhamRepository repository;

    public DongSanPhamService(DongSanPhamRepository repository) {
        this.repository = repository;
    }

    public Page<DongSanPhamDTO> getAllDongSanPham(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    @Transactional
    public DongSanPhamDTO createDongSanPham(DongSanPhamDTO dto) {
        // Kiểm tra xem có bản ghi đã xóa mềm với ma hoặc dongSanPham không
        Optional<DongSanPham> existingDongSanPhamByMa = repository.findByMaAndDeletedTrue(dto.getMa());
        Optional<DongSanPham> existingDongSanPhamByName = repository.findByDongSanPhamAndDeletedTrue(dto.getDongSanPham());

        if (existingDongSanPhamByMa.isPresent()) {
            // Khôi phục bản ghi đã xóa mềm với ma
            DongSanPham entity = existingDongSanPhamByMa.get();
            entity.setDeleted(false);
            entity.setDongSanPham(dto.getDongSanPham()); // Cập nhật giá trị mới
            return toDTO(repository.save(entity));
        } else if (existingDongSanPhamByName.isPresent()) {
            // Khôi phục bản ghi đã xóa mềm với dongSanPham
            DongSanPham entity = existingDongSanPhamByName.get();
            entity.setDeleted(false);
            entity.setMa(dto.getMa()); // Cập nhật giá trị mới
            return toDTO(repository.save(entity));
        } else {
            // Nếu không có bản ghi nào bị xóa mềm, tạo mới
            DongSanPham entity = new DongSanPham();
            entity.setMa(dto.getMa());
            entity.setDongSanPham(dto.getDongSanPham());
            entity.setDeleted(false);
            return toDTO(repository.save(entity));
        }
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

    public Page<DongSanPhamDTO> searchDongSanPham(String keyword, Pageable pageable) {
        // Tìm kiếm trên toàn bộ dữ liệu trước, sau đó phân trang
        List<DongSanPham> allResults = repository.findByDeletedFalse()
                .stream()
                .filter(d -> matchesKeyword(d, keyword))
                .collect(Collectors.toList());
        return toPage(allResults, pageable);
    }

    public Page<DongSanPhamDTO> filterByDongSanPham(String dongSanPham, Pageable pageable) {
        // Lọc trên toàn bộ dữ liệu trước, sau đó phân trang
        List<DongSanPham> allResults = repository.findByDeletedFalse()
                .stream()
                .filter(d -> d.getDongSanPham().equalsIgnoreCase(dongSanPham))
                .collect(Collectors.toList());
        return toPage(allResults, pageable);
    }

    public List<String> getAllProductLineNames() {
        return repository.findByDeletedFalse()
                .stream()
                .map(DongSanPham::getDongSanPham)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public boolean existsByMa(String ma) {
        return repository.existsByMa(ma);
    }

    public boolean existsByDongSanPham(String dongSanPham) {
        return repository.existsByDongSanPham(dongSanPham);
    }

    private boolean matchesKeyword(DongSanPham dsp, String keyword) {
        String keywordLower = keyword.toLowerCase().replaceAll("\\s+", "");
        String maLower = dsp.getMa().toLowerCase().replaceAll("\\s+", "");
        String dongSanPhamLower = dsp.getDongSanPham().toLowerCase();
        return maLower.contains(keywordLower) || dongSanPhamLower.contains(keywordLower);
    }

    private Page<DongSanPhamDTO> toPage(List<DongSanPham> results, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), results.size());
        List<DongSanPhamDTO> subList = start < end ? results.subList(start, end).stream()
                .map(this::toDTO)
                .collect(Collectors.toList()) : List.of();
        return new PageImpl<>(subList, pageable, results.size());
    }

    private DongSanPhamDTO toDTO(DongSanPham entity) {
        return new DongSanPhamDTO(entity.getId(), entity.getMa(), entity.getDongSanPham());
    }
}