package com.example.graduation_project_group_2_mobileworld.repository.chiTietDotGiamGia;

import com.example.graduation_project_group_2_mobileworld.entity.ChiTietDotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public interface ChiTietDotGiamGiaRepository extends JpaRepository<ChiTietDotGiamGia, Integer> {
    @Query("Select ctdgg From ChiTietDotGiamGia ctdgg")
    List<ChiTietDotGiamGia> xuatExcel();

    @Query("SELECT MAX(c.ma) FROM ChiTietDotGiamGia c")
    String findMaxMa();


    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
            "JOIN ChiTietDotGiamGia ctdgg ON ctdgg.idChiTietSanPham = ctsp " +
            "WHERE ctdgg.idDotGiamGia.id = :id AND ctdgg.deleted = false")
    List<ChiTietSanPham> getChiTietSanPhamByDotGiamGia(@Param("id") Integer id);

    @Query("SELECT c FROM ChiTietDotGiamGia c " +
            "WHERE c.idDotGiamGia = :dotGiamGia " +
            "AND c.idChiTietSanPham = :idChiTietSanPham " +
            "AND c.giaBanDau = :giaBanDau " +
            "AND c.deleted = :deleted")
    List<ChiTietDotGiamGia> findByDotGiamGiaAndIdChiTietSanPhamAndGiaBanDauAndDeleted(
            @Param("dotGiamGia") DotGiamGia dotGiamGia,
            @Param("idChiTietSanPham") ChiTietSanPham idChiTietSanPham,
            @Param("giaBanDau") BigDecimal giaBanDau,
            @Param("deleted") boolean deleted);

    @Query("SELECT c FROM ChiTietDotGiamGia c WHERE c.idDotGiamGia = :idDotGiamGia AND c.deleted = :deleted")
    List<ChiTietDotGiamGia> findByIdDotGiamGiaAndDeleted(
            @Param("idDotGiamGia") DotGiamGia idDotGiamGia,
            @Param("deleted") boolean deleted);

    // Tìm ChiTietDotGiamGia theo idChiTietSanPham và deleted
    @Query("SELECT c FROM ChiTietDotGiamGia c WHERE c.idChiTietSanPham = :idChiTietSanPham AND c.deleted = :deleted")
    List<ChiTietDotGiamGia> findByIdChiTietSanPhamAndDeleted(
            @Param("idChiTietSanPham") ChiTietSanPham idChiTietSanPham,
            @Param("deleted") boolean deleted);

    @Query("SELECT c FROM ChiTietDotGiamGia c WHERE c.idDotGiamGia = :dotGiamGia")
    List<ChiTietDotGiamGia> findByIdDotGiamGia(@Param("dotGiamGia") DotGiamGia dotGiamGia);

    @Query("SELECT ct FROM ChiTietDotGiamGia ct WHERE ct.idDotGiamGia.id = :id AND ct.deleted = :deleted")
    List<ChiTietDotGiamGia> findByIdDotGiamGiaIdAndDeleted(@Param("id") Integer id, @Param("deleted") boolean deleted);

    @Modifying
    @Transactional
    @Query("UPDATE ChiTietDotGiamGia ct SET ct.deleted = true WHERE ct.idDotGiamGia.id = :id")
    public void updateChiTietDotGiamGiaDeleted(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE ChiTietDotGiamGia c SET c.deleted = true WHERE c.idDotGiamGia.id IN " +
            "(SELECT e.id FROM DotGiamGia e WHERE e.deleted = true)")
    int updateDeletedChiTietDotGiamGia();

    @Query("SELECT c FROM ChiTietDotGiamGia c " +
            "WHERE c.idChiTietSanPham = :ctspId " +
            "AND c.idDotGiamGia.ngayBatDau <= :today " +
            "AND c.idDotGiamGia.ngayKetThuc >= :today " +
            "AND c.idDotGiamGia.deleted = false " +
            "AND c.deleted = false")
    List<ChiTietDotGiamGia> findActiveChiTietDotGiamGiaByCtspId(@Param("ctspId") Integer ctspId, @Param("today") Date today);
}
