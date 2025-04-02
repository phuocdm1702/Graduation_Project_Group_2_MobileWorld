package com.example.graduation_project_group_2_mobileworld.repository.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

    // Lấy tất cả hóa đơn với phân trang
    @Query("SELECT hd FROM HoaDon hd ORDER BY hd.id DESC")
    Page<HoaDon> findAllWithPagination(Pageable pageable);

    // Lấy hóa đơn với chi tiết
    @Query("SELECT hd FROM HoaDon hd LEFT JOIN FETCH hd.chiTietHoaDon WHERE hd.id = :id")
    Optional<HoaDon> findHoaDonWithDetailsById(@Param("id") Integer id);

    // Lấy hóa đơn với lịch sử
    @Query("SELECT hd FROM HoaDon hd LEFT JOIN FETCH hd.lichSuHoaDon WHERE hd.id = :id")
    Optional<HoaDon> findHoaDonWithHistoryById(@Param("id") Integer id);

    // Lấy hóa đơn với chi tiết và lịch sử
    @Query("SELECT hd FROM HoaDon hd " +
            "LEFT JOIN hd.chiTietHoaDon " +
            "LEFT JOIN hd.lichSuHoaDon " +
            "LEFT JOIN hd.hinhThucThanhToan " +
            "WHERE hd.id = :id")
    Optional<HoaDon> findHoaDonWithDetailsAndHistoryById(@Param("id") Integer id);

    // Lấy hóa đơn với bộ lọc
    @Query("SELECT hd FROM HoaDon hd " +
            "WHERE (:keyword IS NULL OR hd.ma LIKE %:keyword% OR hd.idNhanVien.tenNhanVien LIKE %:keyword% " +
            "OR hd.tenKhachHang LIKE %:keyword% OR hd.soDienThoaiKhachHang LIKE %:keyword%) " +
            "AND (:loaiDon IS NULL OR hd.loaiDon = :loaiDon) " +
            "AND (:minAmount IS NULL OR hd.tongTienSauGiam >= :minAmount) " +
            "AND (:maxAmount IS NULL OR hd.tongTienSauGiam <= :maxAmount) " +
            "AND (:startDate IS NULL OR hd.ngayTao >= :startDate) " +
            "AND (:endDate IS NULL OR hd.ngayTao <= :endDate) " +
            "AND (:trangThai IS NULL OR hd.trangThai = :trangThai) " +
            "ORDER BY hd.id DESC")
    Page<HoaDon> findHoaDonWithFilters(
            @Param("keyword") String keyword,
            @Param("loaiDon") String loaiDon,
            @Param("minAmount") Long minAmount,
            @Param("maxAmount") Long maxAmount,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("trangThai") Short trangThai,
            Pageable pageable);

    // Tìm hóa đơn theo mã
    @Query("SELECT hd FROM HoaDon hd WHERE hd.ma = :ma")
    Optional<HoaDon> findByMa(@Param("ma") String ma);
}