package com.example.graduation_project_group_2_mobileworld.repository.dongSanPham;

import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DongSanPhamRepository extends JpaRepository<DongSanPham, Integer> {
    List<DongSanPham> findByDeletedFalse();

    // Tìm theo mã hoặc tên dòng sản phẩm (không phân biệt hoa thường)
    @Query("SELECT d FROM DongSanPham d WHERE d.deleted = false " +
            "AND (LOWER(d.ma) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.dongSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<DongSanPham> searchByKeyword(@Param("keyword") String keyword);
}
