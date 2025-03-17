package com.example.graduation_project_group_2_mobileworld.repository.chiTietDotGiamGia;

import com.example.graduation_project_group_2_mobileworld.entity.ChiTietDotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ChiTietDotGiamGiaRepository extends JpaRepository<ChiTietDotGiamGia, Integer> {
    @Query("Select ctdgg From ChiTietDotGiamGia ctdgg")
    List<ChiTietDotGiamGia> xuatExcel();

//    @Query("SELECT c FROM ChiTietDotGiamGia c WHERE c.idDongSanPham = :idDongSanPham AND c.dotGiamGia = :dotGiamGia AND c.deleted = false")
//    List<ChiTietDotGiamGia> findByIdDongSanPhamAndDotGiamGia(@Param("idDongSanPham") DongSanPham idDongSanPham,
//                                                       @Param("dotGiamGia") DotGiamGia dotGiamGia);
//
//    @Query("SELECT c FROM ChiTietDotGiamGia c WHERE c.dotGiamGia = :dotGiamGia AND c.deleted = false")
//    List<ChiTietDotGiamGia> findByDotGiamGia(@Param("dotGiamGia") DotGiamGia dotGiamGia);

    @Query("SELECT MAX(c.ma) FROM ChiTietDotGiamGia c")
    String findMaxMa();

//    @Query("SELECT c FROM ChiTietDotGiamGia c WHERE c.idDongSanPham = :dongSanPham AND c.giaBanDau = :giaBanDau AND c.deleted = false")
//    List<ChiTietDotGiamGia> findByIdDongSanPhamAndGiaBanDau(@Param("dongSanPham") DongSanPham dongSanPham, @Param("giaBanDau") BigDecimal giaBanDau);
//
//    @Query("SELECT c FROM ChiTietDotGiamGia c " +
//            "WHERE c.dotGiamGia = :dotGiamGia " +
//            "AND c.idDongSanPham = :idDongSanPham " +
//            "AND c.giaBanDau = :giaBanDau " +
//            "AND c.deleted = :deleted")
//    List<ChiTietDotGiamGia> findByDotGiamGiaAndIdDongSanPhamAndGiaBanDauAndDeleted(
//            @Param("dotGiamGia") DotGiamGia dotGiamGia,
//            @Param("idDongSanPham") DongSanPham idDongSanPham,
//            @Param("giaBanDau") BigDecimal giaBanDau,
//            @Param("deleted") boolean deleted);

}
