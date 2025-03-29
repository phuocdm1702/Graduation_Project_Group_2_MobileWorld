package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.NhaSanXuat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhaSanXuatRepository extends JpaRepository<NhaSanXuat, Integer> {

    List<NhaSanXuat> findByDeletedFalse();
    Page<NhaSanXuat> findByDeletedFalse(Pageable pageable);

    Optional<NhaSanXuat> findByIdAndDeletedFalse(Integer id);

    @Query("SELECT COUNT(n) > 0 FROM NhaSanXuat n WHERE n.ma = :ma AND n.deleted = false")
    boolean existsByMaAndDeletedFalse(@Param("ma") String ma);

    @Query("SELECT COUNT(n) > 0 FROM NhaSanXuat n WHERE n.nhaSanXuat = :nhaSanXuat AND n.deleted = false")
    boolean existsByNhaSanXuatAndDeletedFalse(@Param("nhaSanXuat") String nhaSanXuat);

    @Query("SELECT COUNT(n) > 0 FROM NhaSanXuat n WHERE n.ma = :ma AND n.deleted = false AND n.id != :excludeId")
    boolean existsByMaAndDeletedFalse(@Param("ma") String ma, @Param("excludeId") Integer excludeId);

    @Query("SELECT COUNT(n) > 0 FROM NhaSanXuat n WHERE n.nhaSanXuat = :nhaSanXuat AND n.deleted = false AND n.id != :excludeId")
    boolean existsByNhaSanXuatAndDeletedFalse(@Param("nhaSanXuat") String nhaSanXuat, @Param("excludeId") Integer excludeId);

    @Query("SELECT COUNT(n) > 0 FROM NhaSanXuat n WHERE n.ma = :ma AND n.deleted = true")
    boolean existsByMaAndDeletedTrue(@Param("ma") String ma);

    @Query("SELECT COUNT(n) > 0 FROM NhaSanXuat n WHERE n.nhaSanXuat = :nhaSanXuat AND n.deleted = true")
    boolean existsByNhaSanXuatAndDeletedTrue(@Param("nhaSanXuat") String nhaSanXuat);

    @Query("SELECT n FROM NhaSanXuat n WHERE n.ma = :ma AND n.deleted = true")
    Optional<NhaSanXuat> findByMaAndDeletedTrue(@Param("ma") String ma);

    @Query("SELECT n FROM NhaSanXuat n WHERE n.nhaSanXuat = :nhaSanXuat AND n.deleted = true")
    Optional<NhaSanXuat> findByNhaSanXuatAndDeletedTrue(@Param("nhaSanXuat") String nhaSanXuat);
}