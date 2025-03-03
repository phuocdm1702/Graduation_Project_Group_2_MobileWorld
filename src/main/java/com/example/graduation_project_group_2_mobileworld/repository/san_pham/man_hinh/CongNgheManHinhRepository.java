package com.example.graduation_project_group_2_mobileworld.repository.san_pham.man_hinh;

import com.example.graduation_project_group_2_mobileworld.entity.CongNgheManHinh;
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

    Page<CongNgheManHinh> findByDeletedFalse(Pageable pageable);

    @Query("SELECT c FROM CongNgheManHinh c " +
            "WHERE c.deleted = false AND " +
            "(LOWER(REPLACE(c.ma, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(c.congNgheManHinh, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(c.chuanManHinh, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')))")
    Page<CongNgheManHinh> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(c) > 0 FROM CongNgheManHinh c WHERE c.ma = :ma AND c.deleted = false")
    boolean existsByMa(@Param("ma") String ma);

    @Query("SELECT COUNT(c) > 0 FROM CongNgheManHinh c WHERE c.congNgheManHinh = :congNgheManHinh AND c.deleted = false")
    boolean existsByCongNgheManHinh(@Param("congNgheManHinh") String congNgheManHinh);

    @Query("SELECT COUNT(c) > 0 FROM CongNgheManHinh c WHERE c.chuanManHinh = :chuanManHinh AND c.deleted = false")
    boolean existsByChuanManHinh(@Param("chuanManHinh") String chuanManHinh);

    @Modifying
    @Query("UPDATE CongNgheManHinh c SET c.deleted = true WHERE c.id IN :ids AND c.deleted = false")
    int softDeleteByIds(@Param("ids") List<Integer> ids);

    // Tìm bản ghi đã xóa mềm với mã
    @Query("SELECT c FROM CongNgheManHinh c WHERE c.ma = :ma AND c.deleted = true")
    Optional<CongNgheManHinh> findByMaAndDeletedTrue(@Param("ma") String ma);

    // Tìm bản ghi đã xóa mềm với tên
    @Query("SELECT c FROM CongNgheManHinh c WHERE c.congNgheManHinh = :congNgheManHinh AND c.deleted = true")
    Optional<CongNgheManHinh> findByCongNgheManHinhAndDeletedTrue(@Param("congNgheManHinh") String congNgheManHinh);

    // Tìm bản ghi đã xóa mềm với chuẩn màn hình
    @Query("SELECT c FROM CongNgheManHinh c WHERE c.chuanManHinh = :chuanManHinh AND c.deleted = true")
    Optional<CongNgheManHinh> findByChuanManHinhAndDeletedTrue(@Param("chuanManHinh") String chuanManHinh);
}