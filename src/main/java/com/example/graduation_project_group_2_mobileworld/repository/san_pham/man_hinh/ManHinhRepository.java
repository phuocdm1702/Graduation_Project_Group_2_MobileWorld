package com.example.graduation_project_group_2_mobileworld.repository.san_pham.man_hinh;

import com.example.graduation_project_group_2_mobileworld.entity.ManHinh;
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
public interface ManHinhRepository extends JpaRepository<ManHinh, Integer> {

    List<ManHinh> findByDeletedFalse();

    Page<ManHinh> findByDeletedFalse(Pageable pageable);

    Optional<ManHinh> findByIdAndDeletedFalse(Integer id);

    @Query("SELECT m FROM ManHinh m " +
            "WHERE m.deleted = false AND " +
            "(LOWER(REPLACE(m.ma, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(m.kichThuoc, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(m.doPhanGiai, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(m.doSangToiDa, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(m.tanSoQuet, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(m.kieuManHinh, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')))")
    Page<ManHinh> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(m) > 0 FROM ManHinh m WHERE m.ma = :ma AND m.deleted = false")
    boolean existsByMaAndDeletedFalse(@Param("ma") String ma);

    @Query("SELECT COUNT(m) > 0 FROM ManHinh m WHERE m.kieuManHinh = :kieuManHinh AND m.deleted = false")
    boolean existsByKieuManHinhAndDeletedFalse(@Param("kieuManHinh") String kieuManHinh);

    @Query("SELECT COUNT(m) > 0 FROM ManHinh m WHERE m.ma = :ma AND m.deleted = false AND m.id != :excludeId")
    boolean existsByMaAndDeletedFalse(@Param("ma") String ma, @Param("excludeId") Integer excludeId);

    @Query("SELECT COUNT(m) > 0 FROM ManHinh m WHERE m.kieuManHinh = :kieuManHinh AND m.deleted = false AND m.id != :excludeId")
    boolean existsByKieuManHinhAndDeletedFalse(@Param("kieuManHinh") String kieuManHinh, @Param("excludeId") Integer excludeId);

    @Modifying
    @Query("UPDATE ManHinh m SET m.deleted = true WHERE m.id IN :ids AND m.deleted = false")
    int softDeleteByIds(@Param("ids") List<Integer> ids);

    @Query("SELECT m FROM ManHinh m WHERE m.ma = :ma AND m.deleted = true")
    Optional<ManHinh> findByMaAndDeletedTrue(@Param("ma") String ma);

    @Query("SELECT m FROM ManHinh m WHERE m.kieuManHinh = :kieuManHinh AND m.deleted = true")
    Optional<ManHinh> findByKieuManHinhAndDeletedTrue(@Param("kieuManHinh") String kieuManHinh);
}