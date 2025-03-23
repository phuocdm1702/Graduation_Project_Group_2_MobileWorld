package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SanPhamService {

    private final SanPhamRepository sanPhamRepository;

    @Autowired
    public SanPhamService(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }

    /**
     * Lấy tất cả sản phẩm với phân trang
     * @param page Số trang (bắt đầu từ 0)
     * @param size Số lượng sản phẩm mỗi trang
     * @return Page<SanPham> chứa danh sách sản phẩm
     */
    public Page<SanPham> getAllSanPham(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sanPhamRepository.findAll(pageable);
    }

    /**
     * Tìm kiếm sản phẩm theo các tiêu chí
     * @param keyword Từ khóa tìm kiếm theo tên sản phẩm
     * @param idNhaSanXuat ID nhà sản xuất
     * @param idHeDieuHanh ID hệ điều hành
     * @param idManHinh ID màn hình
     * @param page Số trang
     * @param size Số lượng sản phẩm mỗi trang
     * @return Page<SanPham> chứa danh sách sản phẩm khớp tiêu chí
     */
    public Page<SanPham> searchSanPham(String keyword, Integer idNhaSanXuat, Integer idHeDieuHanh, Integer idManHinh, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<SanPham> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("tenSanPham")), "%" + keyword.toLowerCase() + "%"));
            }
            if (idNhaSanXuat != null) {
                predicates.add(cb.equal(root.get("idNhaSanXuat").get("id"), idNhaSanXuat));
            }
            if (idHeDieuHanh != null) {
                predicates.add(cb.equal(root.get("idHeDieuHanh").get("id"), idHeDieuHanh));
            }
            if (idManHinh != null) {
                predicates.add(cb.equal(root.get("idManHinh").get("id"), idManHinh));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return sanPhamRepository.findAll(spec, pageable);
    }
}