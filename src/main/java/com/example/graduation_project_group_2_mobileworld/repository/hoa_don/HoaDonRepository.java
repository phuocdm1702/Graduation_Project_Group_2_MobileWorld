package com.example.graduation_project_group_2_mobileworld.repository.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}