package com.example.graduation_project_group_2_mobileworld.repository.san_pham.dong_san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DongSanPhamRepository extends JpaRepository<SanPham, Integer> {

//    List<DongSanPham> findByDeletedFalse();
//
//    Page<DongSanPham> findByDeletedFalse(Pageable pageable);
//
//    Optional<DongSanPham> findByIdAndDeletedFalse(Integer id);
//
//    @Query("SELECT d FROM DongSanPham d " +
//            "WHERE d.deleted = false AND " +
//            "(LOWER(REPLACE(d.ma, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
//            "LOWER(REPLACE(d.dongSanPham, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')))")
//    Page<DongSanPham> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
//
//    @Query("SELECT COUNT(d) > 0 FROM DongSanPham d WHERE d.ma = :ma AND d.deleted = false")
//    boolean existsByMaAndDeletedFalse(@Param("ma") String ma);
//
//    @Query("SELECT COUNT(d) > 0 FROM DongSanPham d WHERE d.dongSanPham = :dongSanPham AND d.deleted = false")
//    boolean existsByDongSanPhamAndDeletedFalse(@Param("dongSanPham") String dongSanPham);
//
//    @Query("SELECT COUNT(d) > 0 FROM DongSanPham d WHERE d.ma = :ma AND d.deleted = false AND d.id != :excludeId")
//    boolean existsByMaAndDeletedFalse(@Param("ma") String ma, @Param("excludeId") Integer excludeId);
//
//    @Query("SELECT COUNT(d) > 0 FROM DongSanPham d WHERE d.dongSanPham = :dongSanPham AND d.deleted = false AND d.id != :excludeId")
//    boolean existsByDongSanPhamAndDeletedFalse(@Param("dongSanPham") String dongSanPham, @Param("excludeId") Integer excludeId);
//
//    @Modifying
//    @Query("UPDATE DongSanPham d SET d.deleted = true WHERE d.id IN :ids AND d.deleted = false")
//    int softDeleteByIds(@Param("ids") List<Integer> ids);
//
//    @Query("SELECT COUNT(d) > 0 FROM DongSanPham d WHERE d.ma = :ma AND d.deleted = true")
//    boolean existsByMaAndDeletedTrue(@Param("ma") String ma);
//
//    @Query("SELECT COUNT(d) > 0 FROM DongSanPham d WHERE d.dongSanPham = :dongSanPham AND d.deleted = true")
//    boolean existsByDongSanPhamAndDeletedTrue(@Param("dongSanPham") String dongSanPham);
//
//    @Query("SELECT d FROM DongSanPham d WHERE d.ma = :ma AND d.deleted = true")
//    Optional<DongSanPham> findByMaAndDeletedTrue(@Param("ma") String ma);
//
//    @Query("SELECT d FROM DongSanPham d WHERE d.dongSanPham = :dongSanPham AND d.deleted = true")
//    Optional<DongSanPham> findByDongSanPhamAndDeletedTrue(@Param("dongSanPham") String dongSanPham);
}