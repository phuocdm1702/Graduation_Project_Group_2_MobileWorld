package com.example.graduation_project_group_2_mobileworld.repository.giam_gia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer> {
    @Query("SELECT pgg FROM PhieuGiamGia pgg WHERE CONCAT(pgg.ma, pgg.tenPhieuGiamGia, pgg.moTa) LIKE %?1% ORDER BY pgg.ngayBatDau DESC ")
    Page<PhieuGiamGia> search(String keyword, Pageable pageable);

    @Query("SELECT pgg FROM PhieuGiamGia pgg WHERE " +
            "(?1 IS NULL OR pgg.loaiPhieuGiamGia = ?1) " +
            "AND (?2 IS NULL OR pgg.trangThai = ?2) " +
            "AND (?3 IS NULL OR pgg.ngayBatDau >= ?3) " +
            "AND (?4 IS NULL OR pgg.ngayKetThuc <= ?4) " +
            "AND (?5 IS NULL OR pgg.hoaDonToiThieu >= ?5) " +
            "AND (?6 IS NULL OR pgg.phanTramGiamGia = ?6) " +
            "AND (pgg.deleted = false OR pgg.deleted IS NULL) " +
            "ORDER BY pgg.ngayBatDau DESC")
    Page<PhieuGiamGia> filterPhieuGiamGia(
            String loaiPhieuGiamGia, // Thêm tham số
            Boolean trangThai,
            Date ngayBatDau,
            Date ngayKetThuc,
            Double hoaDonToiThieu,
            Double phanTramGiamGia,
            Pageable pageable
    );



}
