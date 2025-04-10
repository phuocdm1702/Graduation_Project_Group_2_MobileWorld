package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham.CongNgheManHinh;
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
public interface CongNgheManHinhRepository extends JpaRepository<CongNgheManHinh, Integer> {

    List<CongNgheManHinh> findByDeletedFalse();

    @Query("SELECT c FROM CongNgheManHinh c WHERE c.deleted = false ORDER BY c.id ASC")
    Page<CongNgheManHinh> findByDeletedFalse(Pageable pageable);

    @Query("SELECT c FROM CongNgheManHinh c " +
            "WHERE c.deleted = false " +
            "AND (:keyword IS NULL OR " +
            "LOWER(REPLACE(c.ma, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(c.congNgheManHinh, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(c.chuanManHinh, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%'))) " +
            "AND (:congNgheManHinh IS NULL OR c.congNgheManHinh = :congNgheManHinh) " +
            "AND (:chuanManHinh IS NULL OR c.chuanManHinh = :chuanManHinh)")
    Page<CongNgheManHinh> searchByKeyword(
            @Param("keyword") String keyword,
            @Param("congNgheManHinh") String congNgheManHinh,
            @Param("chuanManHinh") String chuanManHinh,
            Pageable pageable);

    @Query("SELECT COUNT(c) > 0 FROM CongNgheManHinh c WHERE c.ma = :ma AND c.deleted = false")
    boolean existsByMa(@Param("ma") String ma);

    @Query("SELECT COUNT(c) > 0 FROM CongNgheManHinh c WHERE c.ma = :ma AND c.deleted = false AND c.id != :excludeId")
    boolean existsByMa(@Param("ma") String ma, @Param("excludeId") Integer excludeId);

    @Query("SELECT COUNT(c) > 0 FROM CongNgheManHinh c WHERE c.ma = :ma AND c.deleted = false AND (:excludeId IS NULL OR c.id != :excludeId)")
    boolean existsByMaAndNotId(@Param("ma") String ma, @Param("excludeId") Integer excludeId);

    @Query("SELECT COUNT(c) > 0 FROM CongNgheManHinh c WHERE c.congNgheManHinh = :congNgheManHinh AND c.deleted = false AND (:excludeId IS NULL OR c.id != :excludeId)")
    boolean existsByCongNgheManHinhAndNotId(@Param("congNgheManHinh") String congNgheManHinh, @Param("excludeId") Integer excludeId);

    @Query("SELECT COUNT(c) > 0 FROM CongNgheManHinh c WHERE c.chuanManHinh = :chuanManHinh AND c.deleted = false AND (:excludeId IS NULL OR c.id != :excludeId)")
    boolean existsByChuanManHinhAndNotId(@Param("chuanManHinh") String chuanManHinh, @Param("excludeId") Integer excludeId);

    @Modifying
    @Query("UPDATE CongNgheManHinh c SET c.deleted = true WHERE c.id IN :ids AND c.deleted = false")
    int softDeleteByIds(@Param("ids") List<Integer> ids);

    @Query("SELECT c FROM CongNgheManHinh c WHERE c.ma = :ma AND c.deleted = true")
    Optional<CongNgheManHinh> findByMaAndDeletedTrue(@Param("ma") String ma);

    @Query("SELECT c FROM CongNgheManHinh c WHERE c.congNgheManHinh = :congNgheManHinh AND c.deleted = true")
    Optional<CongNgheManHinh> findByCongNgheManHinhAndDeletedTrue(@Param("congNgheManHinh") String congNgheManHinh);

    @Query("SELECT c FROM CongNgheManHinh c WHERE c.chuanManHinh = :chuanManHinh AND c.deleted = true")
    Optional<CongNgheManHinh> findByChuanManHinhAndDeletedTrue(@Param("chuanManHinh") String chuanManHinh);
}