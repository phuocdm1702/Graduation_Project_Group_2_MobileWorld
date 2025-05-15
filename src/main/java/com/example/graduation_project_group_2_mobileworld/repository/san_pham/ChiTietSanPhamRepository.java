package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer>, JpaSpecificationExecutor<ChiTietSanPham> {

    @Query("SELECT c FROM ChiTietSanPham c WHERE c.idSanPham.id = :sanPhamId AND c.deleted = :deleted")
    List<ChiTietSanPham> findByIdSanPhamIdAndDeletedFalse(@Param("sanPhamId") Integer sanPhamId, @Param("deleted") boolean deleted);

    @Query("SELECT COUNT(c) FROM ChiTietSanPham c WHERE c.idSanPham.id = :sanPhamId AND c.deleted = false")
    Long countByIdSanPhamIdAndDeletedFalse(@Param("sanPhamId") Integer sanPhamId);

    Page<ChiTietSanPham> findAll(org.springframework.data.jpa.domain.Specification<ChiTietSanPham> spec, Pageable pageable);


    Page<ChiTietSanPham> findByDeletedFalse(Pageable pageable);

//    @Query("SELECT COUNT(ctsp) FROM ChiTietSanPham ctsp WHERE ctsp.idSanPham.id = :sanPhamId AND ctsp.deleted = false")
//    long countBySanPhamIdAndNotDeleted(@Param("sanPhamId") Integer sanPhamId);
//
//    @Query("SELECT c.idImel.imel FROM ChiTietSanPham c WHERE c.idSanPham.id = :sanPhamId AND c.deleted = false")
//    List<String> findIMEIsBySanPhamIdAndNotDeleted(@Param("sanPhamId") Integer sanPhamId);
//
//    @Query("SELECT c.idImel.imel FROM ChiTietSanPham c WHERE c.idSanPham.id = :sanPhamId AND c.deleted = false AND NOT EXISTS (SELECT i FROM ImelDaBan i WHERE i.imel = c.idImel.imel AND i.deleted = false)")
//    List<String> findIMEIsBySanPhamIdAndNotDeletedAndNotSold(@Param("sanPhamId") Integer sanPhamId);
//
//    @Query("SELECT COUNT(c) FROM ChiTietSanPham c WHERE c.idSanPham.id = :sanPhamId AND c.deleted = false AND NOT EXISTS (SELECT i FROM ImelDaBan i WHERE i.imel = c.idImel.imel AND i.deleted = false)")
//    Long countByIdSanPhamIdAndDeletedFalseAndNotSold(@Param("sanPhamId") Integer sanPhamId);
//
//    @Query("SELECT sp.tenSanPham AS tenSanPham, MIN(c.ma) AS ma, ms.mauSac AS mauSac, r.dungLuongRam AS dungLuongRam, bnt.dungLuongBoNhoTrong AS dungLuongBoNhoTrong, COUNT(DISTINCT c.idImel.imel) AS soLuong, MIN(c.giaBan) AS giaBan " +
//            "FROM ChiTietSanPham c " +
//            "JOIN c.idSanPham sp " +
//            "LEFT JOIN c.idMauSac ms " +
//            "LEFT JOIN c.idRam r " +
//            "LEFT JOIN c.idBoNhoTrong bnt " +
//            "WHERE c.idSanPham.id = :sanPhamId AND c.deleted = false " +
//            "AND NOT EXISTS (SELECT i FROM ImelDaBan i WHERE i.imel = c.idImel.imel AND i.deleted = false) " +
//            "GROUP BY sp.tenSanPham, ms.mauSac, r.dungLuongRam, bnt.dungLuongBoNhoTrong")
//    List<Object[]> findGroupedProductsBySanPhamId(@Param("sanPhamId") Integer sanPhamId);
//
//    @Query("SELECT c.idImel.imel FROM ChiTietSanPham c " +
//            "WHERE c.idSanPham.id = :sanPhamId AND c.deleted = false " +
//            "AND c.idMauSac.mauSac = :mauSac AND c.idRam.dungLuongRam = :dungLuongRam AND c.idBoNhoTrong.dungLuongBoNhoTrong = :dungLuongBoNhoTrong " +
//            "AND NOT EXISTS (SELECT i FROM ImelDaBan i WHERE i.imel = c.idImel.imel AND i.deleted = false)")
//    List<String> findIMEIsBySanPhamIdAndAttributes(@Param("sanPhamId") Integer sanPhamId,
//                                                   @Param("mauSac") String mauSac,
//                                                   @Param("dungLuongRam") String dungLuongRam,
//                                                   @Param("dungLuongBoNhoTrong") String dungLuongBoNhoTrong);

    @Query("SELECT COUNT(ctsp) FROM ChiTietSanPham ctsp WHERE ctsp.idSanPham.id = :sanPhamId AND ctsp.deleted = false")
    long countBySanPhamIdAndNotDeleted(@Param("sanPhamId") Integer sanPhamId);

    @Query("SELECT c.idImel.imel FROM ChiTietSanPham c WHERE c.idSanPham.id = :sanPhamId AND c.deleted = false")
    List<String> findIMEIsBySanPhamIdAndNotDeleted(@Param("sanPhamId") Integer sanPhamId);

    @Query("SELECT c.idImel.imel FROM ChiTietSanPham c WHERE c.idSanPham.id = :sanPhamId AND c.deleted = false AND NOT EXISTS (SELECT i FROM ImelDaBan i WHERE i.imel = c.idImel.imel AND i.deleted = false)")
    List<String> findIMEIsBySanPhamIdAndNotDeletedAndNotSold(@Param("sanPhamId") Integer sanPhamId);

    @Query("SELECT COUNT(c) FROM ChiTietSanPham c WHERE c.idSanPham.id = :sanPhamId AND c.deleted = false AND NOT EXISTS (SELECT i FROM ImelDaBan i WHERE i.imel = c.idImel.imel AND i.deleted = false)")
    Long countByIdSanPhamIdAndDeletedFalseAndNotSold(@Param("sanPhamId") Integer sanPhamId);

    @Query("SELECT sp.tenSanPham AS tenSanPham, MIN(c.ma) AS ma, ms.mauSac AS mauSac, r.dungLuongRam AS dungLuongRam, bnt.dungLuongBoNhoTrong AS dungLuongBoNhoTrong, COUNT(DISTINCT c.idImel.imel) AS soLuong, MIN(c.giaBan) AS giaBan, sp.id AS idSanPham " +
            "FROM ChiTietSanPham c " +
            "JOIN c.idSanPham sp " +
            "LEFT JOIN c.idMauSac ms " +
            "LEFT JOIN c.idRam r " +
            "LEFT JOIN c.idBoNhoTrong bnt " +
            "WHERE (:sanPhamId IS NULL OR c.idSanPham.id = :sanPhamId) AND c.deleted = false " +
            "AND NOT EXISTS (SELECT i FROM ImelDaBan i WHERE i.imel = c.idImel.imel AND i.deleted = false) " +
            "GROUP BY sp.id, sp.tenSanPham, ms.mauSac, r.dungLuongRam, bnt.dungLuongBoNhoTrong")
    List<Object[]> findGroupedProductsBySanPhamId(@Param("sanPhamId") Integer sanPhamId);

    @Query("SELECT c.idImel.imel FROM ChiTietSanPham c " +
            "WHERE c.idSanPham.id = :sanPhamId AND c.deleted = false " +
            "AND c.idMauSac.mauSac = :mauSac AND c.idRam.dungLuongRam = :dungLuongRam AND c.idBoNhoTrong.dungLuongBoNhoTrong = :dungLuongBoNhoTrong " +
            "AND NOT EXISTS (SELECT i FROM ImelDaBan i WHERE i.imel = c.idImel.imel AND i.deleted = false)")
    List<String> findIMEIsBySanPhamIdAndAttributes(@Param("sanPhamId") Integer sanPhamId,
                                                   @Param("mauSac") String mauSac,
                                                   @Param("dungLuongRam") String dungLuongRam,
                                                   @Param("dungLuongBoNhoTrong") String dungLuongBoNhoTrong);

    @Query("SELECT c FROM ChiTietSanPham c WHERE c.idImel.imel = :imei AND c.deleted = false")
    Optional<ChiTietSanPham> findByIdImelImelAndDeletedFalse(@Param("imei") String imei);

}