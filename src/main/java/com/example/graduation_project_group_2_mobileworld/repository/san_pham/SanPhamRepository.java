package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    // Existing method for paginated search with specification
    Page<SanPham> findAll(Specification<SanPham> spec, Pageable pageable);

    // Find all products that are not deleted (for listing active products)
    Page<SanPham> findByDeletedFalse(Pageable pageable);

    // Find a single product by ID, regardless of deleted status
    SanPham findByIdAndDeletedFalse(Integer id);

    // Custom query to soft delete a single product by ID
    @Modifying
    @Query("UPDATE SanPham sp SET sp.deleted = true WHERE sp.id = :id")
    int softDeleteById(@Param("id") Integer id);

    // Custom query to soft delete multiple products by IDs
    @Modifying
    @Query("UPDATE SanPham sp SET sp.deleted = true WHERE sp.id IN :ids")
    int softDeleteByIds(@Param("ids") List<Integer> ids);

    // Find all products by a list of IDs (useful for bulk operations)
    List<SanPham> findAllByIdIn(List<Integer> ids);
}