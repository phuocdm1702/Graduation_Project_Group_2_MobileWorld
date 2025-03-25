package com.example.graduation_project_group_2_mobileworld.repository.dot_giam_gia_repo;

import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO;

import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface dot_giam_gia_repository extends JpaRepository<DotGiamGia, Integer> {

    @Query("SELECT dgg FROM DotGiamGia dgg WHERE dgg.deleted = false")
    public Page<DotGiamGia> hienThi(Pageable pageable);

    @Query("Select dgg From DotGiamGia dgg")
    public List<DotGiamGia> ForExcel();

    @Query("SELECT dgg FROM DotGiamGia dgg WHERE dgg.deleted = true")
    public Page<DotGiamGia> hienThiFinish(Pageable pageable);

    @Query("SELECT new com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewSanPhamDTO(sp, nsx, hdh, " +
            "(SELECT COUNT(ctsp) FROM ChiTietSanPham ctsp WHERE ctsp.idSanPham.id = sp.id AND ctsp.deleted = false)) " +
            "FROM SanPham sp " +
            "INNER JOIN sp.idHeDieuHanh hdh " +
            "INNER JOIN sp.idNhaSanXuat nsx " +
            "WHERE (:timKiem IS NULL OR :timKiem = '' OR sp.ma LIKE CONCAT('%', :timKiem, '%') OR sp.tenSanPham LIKE CONCAT('%', :timKiem, '%')) " +
            "AND (:idHeDieuHanh IS NULL OR hdh.id IN :idHeDieuHanh) " +
            "AND (:idNhaSanXuat IS NULL OR nsx.id IN :idNhaSanXuat) " +
            "AND sp.deleted = false")
    Page<viewSanPhamDTO> getAllSanPham(@Param("timKiem") String timKiem,
                                       @Param("idHeDieuHanh") List<Integer> idHeDieuHanh,
                                       @Param("idNhaSanXuat") List<Integer> idNhaSanXuat,
                                       Pageable pageable);


    // Lấy tất cả Hệ điều hành không bị xóa
    @Query("SELECT hdh FROM HeDieuHanh hdh WHERE hdh.deleted = false")
    List<HeDieuHanh> findAllHeDieuHanh();

    // Lấy tất cả Nhà sản xuất không bị xóa
    @Query("SELECT nsx FROM NhaSanXuat nsx WHERE nsx.deleted = false")
    List<NhaSanXuat> findAllNhaSanXuat();

//    @Query("SELECT new com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO(sp, ctsp, anh, bnt, ms) " +
//            "FROM SanPham sp " +
//            "INNER JOIN ChiTietSanPham ctsp ON ctsp.idSanPham.id = sp.id " +
//            "INNER JOIN AnhSanPham anh ON ctsp.idAnhSanPham.id = anh.id " +
//            "INNER JOIN BoNhoTrong bnt ON ctsp.idBoNhoTrong.id = bnt.id " +
//            "INNER JOIN MauSac ms ON ctsp.idMauSac.id = ms.id " +
//            "WHERE sp.id IN :ids " +
//            "AND (:idBoNhoTrongs IS NULL OR bnt.id IN :idBoNhoTrongs) " +
//            "AND (:idMauSacs IS NULL OR ms.id IN :idMauSacs) " +
//            "AND ctsp.deleted = false " +
//            "AND ctsp.id IN (" +
//            "    SELECT MIN(ctsp2.id) " +
//            "    FROM ChiTietSanPham ctsp2 " +
//            "    WHERE ctsp2.idSanPham.id = sp.id " +
//            "    AND ctsp2.idMauSac.id = ms.id " +
//            "    AND ctsp2.idBoNhoTrong.id = bnt.id " +
//            "    AND ctsp2.deleted = false " +
//            "    GROUP BY ctsp2.idSanPham.id, ctsp2.idMauSac.id, ctsp2.idBoNhoTrong.id" +
//            ")")
//    Page<viewCTSPDTO> getAllCTSP(@Param("ids") List<Integer> ids,
//                                 @Param("idBoNhoTrongs") List<Integer> idBoNhoTrongs,
//                                 @Param("idMauSacs") List<Integer> idMauSacs,
//                                 Pageable pageable);

    @Query("SELECT new com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO(sp, ctsp, anh, bnt, ms, " +
            "(SELECT COUNT(ctdg) FROM ChiTietDotGiamGia ctdg " +
            "WHERE ctdg.idChiTietSanPham.idSanPham.id = sp.id " +
            "AND ctdg.idChiTietSanPham.idBoNhoTrong.id = bnt.id " +
            "AND ctdg.idChiTietSanPham.idMauSac.id = ms.id " +
            "AND ctdg.idDotGiamGia.deleted = false)) " +
            "FROM SanPham sp " +
            "INNER JOIN ChiTietSanPham ctsp ON ctsp.idSanPham.id = sp.id " +
            "INNER JOIN AnhSanPham anh ON ctsp.idAnhSanPham.id = anh.id " +
            "INNER JOIN BoNhoTrong bnt ON ctsp.idBoNhoTrong.id = bnt.id " +
            "INNER JOIN MauSac ms ON ctsp.idMauSac.id = ms.id " +
            "WHERE sp.id IN :ids " +
            "AND (:idBoNhoTrongs IS NULL OR bnt.id IN :idBoNhoTrongs) " +
            "AND (:idMauSacs IS NULL OR ms.id IN :idMauSacs) " +
            "AND ctsp.deleted = false " +
            "AND ctsp.id IN (" +
            "    SELECT MIN(ctsp2.id) " +
            "    FROM ChiTietSanPham ctsp2 " +
            "    WHERE ctsp2.idSanPham.id = sp.id " +
            "    AND ctsp2.idMauSac.id = ms.id " +
            "    AND ctsp2.idBoNhoTrong.id = bnt.id " +
            "    AND ctsp2.deleted = false " +
            "    GROUP BY ctsp2.idSanPham.id, ctsp2.idMauSac.id, ctsp2.idBoNhoTrong.id" +
            ")")
    Page<viewCTSPDTO> getAllCTSP(@Param("ids") List<Integer> ids,
                                 @Param("idBoNhoTrongs") List<Integer> idBoNhoTrongs,
                                 @Param("idMauSacs") List<Integer> idMauSacs,
                                 Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE DotGiamGia d SET d.deleted = true WHERE d.id = :id")
    public void updateDotGiamGiaDeleted(@Param("id") Integer id);


    @Query("SELECT DISTINCT ctsp.idSanPham FROM ChiTietSanPham ctsp " +
            "JOIN ctsp.idSanPham sp " +
            "JOIN ChiTietDotGiamGia ctdgg ON ctdgg.idChiTietSanPham.id = ctsp.id " +
            "WHERE ctdgg.idDotGiamGia.id = :id AND ctdgg.deleted = false")
    List<SanPham> getThatDongSanPham(@Param("id") Integer id);

    @Query("SELECT COUNT(dgg) > 0 FROM DotGiamGia dgg WHERE dgg.ma = :ma")
    boolean existsByMaAndDeletedTrue(@Param("ma") String ma);

    @Query("SELECT d FROM DotGiamGia d WHERE "
            + "((:maDGG IS NULL AND :tenDGG IS NULL) " // Nếu cả hai null thì bỏ qua điều kiện này
            + " OR (:maDGG IS NOT NULL AND d.ma LIKE CONCAT('%', :maDGG, '%')) "
            + " OR (:tenDGG IS NOT NULL AND d.tenDotGiamGia LIKE CONCAT('%', :tenDGG, '%'))) AND "
            + "(:loaiGiamGiaApDung IS NULL OR d.loaiGiamGiaApDung = :loaiGiamGiaApDung) AND "
            + "(:giaTriGiamGia IS NULL OR d.giaTriGiamGia = :giaTriGiamGia) AND "
            + "(:soTienGiamToiDa IS NULL OR d.soTienGiamToiDa <= :soTienGiamToiDa) AND "
            + "(:ngayBatDau IS NULL OR d.ngayBatDau >= :ngayBatDau) AND "
            + "(:ngayKetThuc IS NULL OR d.ngayKetThuc <= :ngayKetThuc) AND "
            + "(:trangThai IS NULL OR d.trangThai = :trangThai) AND "
            + "((:deleted IS NULL AND d.deleted = false) OR (:deleted IS NOT NULL AND d.deleted = :deleted))")
    Page<DotGiamGia> timKiem(
            Pageable pageable,
            @Param("maDGG") String maDGG,
            @Param("tenDGG") String tenDGG,
            @Param("loaiGiamGiaApDung") String loaiGiamGiaApDung,
            @Param("giaTriGiamGia") BigDecimal giaTriGiamGia,
            @Param("soTienGiamToiDa") BigDecimal soTienGiamToiDa,
            @Param("ngayBatDau") Date ngayBatDau,
            @Param("ngayKetThuc") Date ngayKetThuc,
            @Param("trangThai") Boolean trangThai,
            @Param("deleted") Boolean deleted
    );

    @Modifying
    @Transactional
    @Query("UPDATE DotGiamGia e SET e.trangThai = false WHERE e.ngayBatDau <= :today AND e.trangThai = true")
    void updateStatusIfStartDatePassed(@Param("today") Date today);

    @Modifying
    @Transactional
    @Query("""
                UPDATE DotGiamGia e 
                SET e.deleted = true 
                WHERE e.ngayKetThuc <= :today AND e.deleted = false
            """)
    int updateDeletedIfEndDatePassed(@Param("today") Date today);


}
