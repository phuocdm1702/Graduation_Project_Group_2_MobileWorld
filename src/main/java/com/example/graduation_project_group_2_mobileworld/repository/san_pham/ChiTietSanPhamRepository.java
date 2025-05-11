package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer>, JpaSpecificationExecutor<ChiTietSanPham> {

    @Query("SELECT c FROM ChiTietSanPham c WHERE c.idSanPham.id = :sanPhamId AND c.deleted = :deleted")
    List<ChiTietSanPham> findByIdSanPhamIdAndDeletedFalse(@Param("sanPhamId") Integer sanPhamId, @Param("deleted") boolean deleted);

    @Query("SELECT COUNT(c) FROM ChiTietSanPham c WHERE c.idSanPham.id = :sanPhamId AND c.deleted = false")
    Long countByIdSanPhamIdAndDeletedFalse(@Param("sanPhamId") Integer sanPhamId);

    Page<ChiTietSanPham> findAll(org.springframework.data.jpa.domain.Specification<ChiTietSanPham> spec, Pageable pageable);


    Page<ChiTietSanPham> findByDeletedFalse(Pageable pageable);

    @Query("SELECT COUNT(ctsp) FROM ChiTietSanPham ctsp WHERE ctsp.idSanPham.id = :sanPhamId AND ctsp.deleted = false")
    long countBySanPhamIdAndNotDeleted(@Param("sanPhamId") Integer sanPhamId);

    @Query("SELECT c.idImel.imel FROM ChiTietSanPham c WHERE c.idSanPham.id = :sanPhamId AND c.deleted = false")
    List<String> findIMEIsBySanPhamIdAndNotDeleted(@Param("sanPhamId") Integer sanPhamId);

}