package com.example.graduation_project_group_2_mobileworld.repository.san_pham.nha_san_xuat;

import com.example.graduation_project_group_2_mobileworld.entity.NhaSanXuat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhaSanXuatRepository extends JpaRepository<NhaSanXuat, Integer> {

    List<NhaSanXuat> findByDeletedFalse();

    Page<NhaSanXuat> findByDeletedFalse(Pageable pageable);

    @Query("SELECT d FROM NhaSanXuat d " +
            "WHERE d.deleted = false AND " +
            "(LOWER(REPLACE(d.ma, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(d.nhaSanXuat, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')))")
    Page<NhaSanXuat> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(d) > 0 FROM NhaSanXuat d WHERE d.ma = :ma AND d.deleted = false")
    boolean existsByMa(@Param("ma") String ma);

    @Query("SELECT COUNT(d) > 0 FROM NhaSanXuat d WHERE d.nhaSanXuat = :nhaSanXuat AND d.deleted = false")
    boolean existsByNhaSanXuat(@Param("nhaSanXuat") String nhaSanXuat);

    @Modifying
    @Query("UPDATE NhaSanXuat d SET d.deleted = true WHERE d.id IN :ids AND d.deleted = false")
    int softDeleteByIds(@Param("ids") List<Integer> ids);
}
