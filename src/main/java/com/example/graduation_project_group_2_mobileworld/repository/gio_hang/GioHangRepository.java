package com.example.graduation_project_group_2_mobileworld.repository.gio_hang;

import com.example.graduation_project_group_2_mobileworld.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
//    @Query("Select gh From GioHang gh where gh.id = ?1")
//    Optional<GioHang> findByHoaDonId(Long hoaDonId);

    //    @Query("SELECT DISTINCT ctgh.idGioHang FROM ChiTietGioHang ctgh " +
//            "JOIN ctgh.idChiTietSanPham ctsp " +
//            "JOIN HoaDonChiTiet hdct " +
//            "WHERE hdct.idChiTietSanPham = ctsp " +
//            "AND hdct.hoaDon.id = ?1 " +
//            "AND ctgh.idGioHang.deleted = false")
//    Optional<GioHang> findByHoaDonId(Integer hoaDonId);
    @Query("SELECT DISTINCT ctgh.idGioHang FROM ChiTietGioHang ctgh " +
            "JOIN ctgh.idChiTietSanPham ctsp " +
            "JOIN HoaDonChiTiet hdct ON hdct.idChiTietSanPham = ctsp " +
            "WHERE hdct.hoaDon.id = :hoaDonId " +
            "AND ctgh.idGioHang.deleted = false " +
            "AND hdct.deleted = false")
    Optional<GioHang> findByHoaDonId(@Param("hoaDonId") Integer hoaDonId);
}
