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

    Page<ManHinh> findByDeletedFalse(Pageable pageable);

    @Query("SELECT mh FROM ManHinh mh " +
            "WHERE mh.deleted = false AND " +
            "(LOWER(REPLACE(mh.ma, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(mh.kichThuoc, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(mh.doPhanGiai, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(mh.tanSoQuet, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(mh.kieuManHinh, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')))")
    Page<ManHinh> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(mh) > 0 FROM ManHinh mh WHERE mh.ma = :ma AND mh.deleted = false")
    boolean existsByMa(@Param("ma") String ma);

    @Query("SELECT COUNT(mh) > 0 FROM ManHinh mh WHERE mh.kieuManHinh = :kieuManHinh AND mh.deleted = false")
    boolean existsByKieuManHinh(@Param("kieuManHinh") String kieuManHinh);

    @Modifying
    @Query("UPDATE ManHinh mh SET mh.deleted = true WHERE mh.id IN :ids AND mh.deleted = false")
    int softDeleteByIds(@Param("ids") List<Integer> ids);

    @Query("SELECT mh FROM ManHinh mh WHERE mh.ma = :ma AND mh.deleted = true")
    Optional<ManHinh> findByMaAndDeletedTrue(@Param("ma") String ma);

    @Query("SELECT mh FROM ManHinh mh WHERE mh.kieuManHinh = :kieuManHinh AND mh.deleted = true")
    Optional<ManHinh> findByKieuManHinhAndDeletedTrue(@Param("kieuManHinh") String kieuManHinh);
}
