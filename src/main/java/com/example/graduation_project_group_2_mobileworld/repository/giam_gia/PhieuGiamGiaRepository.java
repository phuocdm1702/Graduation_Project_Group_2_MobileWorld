package com.example.graduation_project_group_2_mobileworld.repository.giam_gia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer> {
    @Query("SELECT pgg FROM PhieuGiamGia pgg WHERE CONCAT(pgg.ma, pgg.tenPhieuGiamGia, pgg.moTa) LIKE %?1% ORDER BY pgg.ngayBatDau DESC ")
    public List<PhieuGiamGia> search(String keyword);

    @Query("SELECT pgg FROM PhieuGiamGia pgg WHERE " +
            "(?1 IS NULL OR pgg.loaiPhieuGiamGia = ?1) " +
            "AND (?2 IS NULL OR pgg.trangThai = ?2) " +
            "ORDER BY pgg.ngayBatDau DESC")
    List<PhieuGiamGia> filterByLoaiPhieuAndTrangThai(String loaiPhieu, Boolean trangThai);



}
