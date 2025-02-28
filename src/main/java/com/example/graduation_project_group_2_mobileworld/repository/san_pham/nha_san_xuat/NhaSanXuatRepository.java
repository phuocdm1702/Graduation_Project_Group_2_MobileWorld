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
import java.util.Optional;

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

    // Thêm phương thức để kiểm tra xem có bản ghi đã xóa mềm với ma không
    @Query("SELECT COUNT(n) > 0 FROM NhaSanXuat n WHERE n.ma = :ma AND n.deleted = true")
    boolean existsByMaAndDeletedTrue(@Param("ma") String ma);

    // Thêm phương thức để kiểm tra xem có bản ghi đã xóa mềm với nhaSanXuat không
    @Query("SELECT COUNT(n) > 0 FROM NhaSanXuat n WHERE n.nhaSanXuat = :nhaSanXuat AND n.deleted = true")
    boolean existsByNhaSanXuatAndDeletedTrue(@Param("nhaSanXuat") String nhaSanXuat);

    // Thêm phương thức để tìm bản ghi đã xóa mềm với ma
    @Query("SELECT n FROM NhaSanXuat n WHERE n.ma = :ma AND n.deleted = true")
    Optional<NhaSanXuat> findByMaAndDeletedTrue(@Param("ma") String ma);

    // Thêm phương thức để tìm bản ghi đã xóa mềm với nhaSanXuat
    @Query("SELECT n FROM NhaSanXuat n WHERE n.nhaSanXuat = :nhaSanXuat AND n.deleted = true")
    Optional<NhaSanXuat> findByNhaSanXuatAndDeletedTrue(@Param("nhaSanXuat") String nhaSanXuat);
}
