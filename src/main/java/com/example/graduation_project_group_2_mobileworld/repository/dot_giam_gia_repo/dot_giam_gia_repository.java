package com.example.graduation_project_group_2_mobileworld.repository.dot_giam_gia_repo;

import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.dot_giam_gia_DTO;
import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO;
import com.example.graduation_project_group_2_mobileworld.entity.ChiTietDotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface dot_giam_gia_repository extends JpaRepository<DotGiamGia, Integer> {

    @Query("SELECT dgg FROM DotGiamGia dgg WHERE dgg.deleted = false")
    public Page<DotGiamGia> hienThi(Pageable pageable);

    @Query("SELECT dgg FROM DotGiamGia dgg WHERE dgg.deleted = true")
    public Page<DotGiamGia> hienThiFinish(Pageable pageable);

    @Query("SELECT dsp FROM DongSanPham dsp WHERE (:timKiem IS NULL OR :timKiem = '' OR dsp.ma LIKE CONCAT('%', :timKiem, '%') OR dsp.dongSanPham LIKE CONCAT('%', :timKiem, '%')) AND dsp.deleted = false ")
    public List<DongSanPham> getAllDongSanPham(@Param("timKiem") String timKiem);

    @Query("SELECT new com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO(dsp, ctsp, anh) " +
            "FROM DongSanPham dsp " +
            "INNER JOIN ChiTietSanPham ctsp ON ctsp.idDongSanPham.id = dsp.id " +
            "INNER JOIN AnhSanPham anh ON ctsp.idAnhSanPham.id = anh.id " +
            "WHERE dsp.id IN :ids")
    List<viewCTSPDTO> getAllCTSP(@Param("ids") List<Integer> ids);

    @Modifying
    @Transactional
    @Query("UPDATE DotGiamGia d SET d.deleted = true WHERE d.id = :id")
    public void updateDotGiamGiaDeleted(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE ChiTietDotGiamGia ct SET ct.deleted = true WHERE ct.dotGiamGia.id = :id")
    public void updateChiTietDotGiamGiaDeleted(@Param("id") Integer id);

    @Query("SELECT dsp FROM DongSanPham dsp " +
            "JOIN ChiTietDotGiamGia ctdgg ON ctdgg.idDongSanPham = dsp " +
            "JOIN ctdgg.dotGiamGia dgg " +
            "WHERE dgg.id = :id")
    List<DongSanPham> getThatDongSanPham(@Param("id") Integer id);

    @Query("SELECT COUNT(dgg) > 0 FROM DotGiamGia dgg WHERE dgg.ma = :ma")
    boolean existsByMaAndDeletedTrue(@Param("ma") String ma);

    @Query("SELECT d FROM DotGiamGia d WHERE "
            + "((:maDGG IS NOT NULL AND d.ma LIKE CONCAT('%', :maDGG, '%')) "
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
    @Query("UPDATE DotGiamGia e SET e.deleted = true WHERE e.ngayKetThuc <= :today AND e.deleted = false")
    void updateDeletedIfEndDatePassed(@Param("today") Date today);
}
