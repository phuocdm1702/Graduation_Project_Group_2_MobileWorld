package com.example.graduation_project_group_2_mobileworld.repository.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

    @Query("SELECT hd FROM HoaDon hd " +
            "ORDER BY hd.id DESC")
    Page<HoaDon> findAllWithPagination(Pageable pageable);

    // Lấy hóa đơn với chi tiết
    @Query("SELECT hd FROM HoaDon hd LEFT JOIN FETCH hd.chiTietHoaDon cthd WHERE hd.id = :id")
    Optional<HoaDon> findHoaDonWithDetailsById(@Param("id") Integer id);

    // Lấy hóa đơn với lịch sử
    @Query("SELECT hd FROM HoaDon hd LEFT JOIN FETCH hd.lichSuHoaDon lshd WHERE hd.id = :id")
    Optional<HoaDon> findHoaDonWithHistoryById(@Param("id") Integer id);


    // Truy vấn mới: Lấy cả chi tiết và lịch sử cùng lúc
    @Query("SELECT hd FROM HoaDon hd " +
            "LEFT JOIN hd.chiTietHoaDon cthd " +
            "LEFT JOIN hd.lichSuHoaDon lshd " +
            "LEFT JOIN hd.hinhThucThanhToan httt " +
            "WHERE hd.id = :id")
    Optional<HoaDon> findHoaDonWithDetailsAndHistoryById(@Param("id") Integer id);

    @Query("SELECT hd FROM HoaDon hd " +
            "WHERE (:keyword IS NULL OR hd.ma LIKE %:keyword% OR hd.idNhanVien.tenNhanVien LIKE %:keyword% OR hd.tenKhachHang LIKE %:keyword% OR hd.soDienThoaiKhachHang LIKE %:keyword%) " +
            "AND (:loaiDon IS NULL OR hd.loaiDon = :loaiDon) " +
            "AND (:minAmount IS NULL OR hd.tongTienSauGiam >= :minAmount) " +
            "AND (:maxAmount IS NULL OR hd.tongTienSauGiam <= :maxAmount) " +
            "AND (:startDate IS NULL OR hd.ngayTao >= :startDate) " +
            "AND (:endDate IS NULL OR hd.ngayTao <= :endDate) " +
            "ORDER BY hd.id DESC")
    Page<HoaDon> findHoaDonWithFilters(
            @Param("keyword") String keyword,
            @Param("loaiDon") String loaiDon,
            @Param("minAmount") Long minAmount,
            @Param("maxAmount") Long maxAmount,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            Pageable pageable);
    @Query("SELECT hd FROM HoaDon hd WHERE hd.ma = :ma")
    Optional<HoaDon> findByMa(@Param("ma") String ma);
}