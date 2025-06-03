package com.example.graduation_project_group_2_mobileworld.repository.thongKe;

import com.example.graduation_project_group_2_mobileworld.dto.thongKe.HangBanChayDTO;
import com.example.graduation_project_group_2_mobileworld.dto.thongKe.LoaiHoaDonDTO;
import com.example.graduation_project_group_2_mobileworld.dto.thongKe.SanPhamHetHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDonChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface thongKeRepository extends JpaRepository<HoaDon, Integer> {
    // Ngày hôm nay
    @Query("SELECT " +
            "SUM(hd.tongTienSauGiam) as doanhThu, " +
            "COUNT(hdct.idChiTietSanPham) as sanPhamDaBan, " +
            "COUNT(DISTINCT hd.id) as tongSoDonHang " +
            "FROM HoaDon hd " +
            "LEFT JOIN hd.chiTietHoaDon hdct " +
            "WHERE hd.ngayThanhToan >= :ngayHienTai " +
            "AND hd.deleted = false")
    Map<String, Object> thongKeTheoNgay(
            @Param("ngayHienTai") Date ngayHienTai
    );

    // Tuần hiện tại
    @Query("SELECT " +
            "SUM(hd.tongTienSauGiam) as doanhThu, " +
            "COUNT(hdct.idChiTietSanPham) as sanPhamDaBan, " +
            "COUNT(DISTINCT hd.id) as tongSoDonHang " +
            "FROM HoaDon hd " +
            "LEFT JOIN hd.chiTietHoaDon hdct " +
            "WHERE hd.ngayThanhToan >= :startOfWeek " +
            "AND hd.ngayThanhToan <= :endOfWeek " +
            "AND hd.deleted = false")
    Map<String, Object> thongKeTheoTuan(
            @Param("startOfWeek") Date startOfWeek,
            @Param("endOfWeek") Date endOfWeek
    );

    // Tháng hiện tại
    @Query("SELECT " +
            "SUM(hd.tongTienSauGiam) as doanhThu, " +
            "COUNT(hdct.idChiTietSanPham) as sanPhamDaBan, " +
            "COUNT(DISTINCT hd.id) as tongSoDonHang " +
            "FROM HoaDon hd " +
            "LEFT JOIN hd.chiTietHoaDon hdct " +
            "WHERE MONTH(hd.ngayThanhToan) = :thang " +
            "AND YEAR(hd.ngayThanhToan) = :nam " +
            "AND hd.deleted = false")
    Map<String, Object> thongKeTheoThang(
            @Param("thang") int thang,
            @Param("nam") int nam
    );

    // Năm hiện tại
    @Query("SELECT " +
            "SUM(hd.tongTienSauGiam) as doanhThu, " +
            "COUNT(hdct.idChiTietSanPham) as sanPhamDaBan, " +
            "COUNT(DISTINCT hd.id) as tongSoDonHang " +
            "FROM HoaDon hd " +
            "LEFT JOIN hd.chiTietHoaDon hdct " +
            "WHERE YEAR(hd.ngayThanhToan) = :nam " +
            "AND hd.deleted = false")
    Map<String, Object> thongKeTheoNam(
            @Param("nam") int nam
    );

    // Sản phẩm bán chạy (phân trang)
    @Query("SELECT c.idChiTietSanPham.id, COUNT(c.id) as soLuongBan " +
            "FROM HoaDonChiTiet c " +
            "JOIN c.hoaDon h " +
            "WHERE (:startDate IS NULL OR h.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR h.createdAt <= :endDate) " +
            "GROUP BY c.idChiTietSanPham.id")
    Page<Object[]> findTopSellingProducts(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

    // Sản phẩm bán chạy (toàn bộ dữ liệu)
    @Query("SELECT c.idChiTietSanPham.id, COUNT(c.id) as soLuongBan " +
            "FROM HoaDonChiTiet c " +
            "JOIN c.hoaDon h " +
            "WHERE (:startDate IS NULL OR h.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR h.createdAt <= :endDate) " +
            "GROUP BY c.idChiTietSanPham.id")
    List<Object[]> findAllTopSellingProducts(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Tăng trưởng
    @Query("SELECT new map(" +
            "SUM(hd.tongTien) as doanhThu, " +
            "COUNT(hdct.id) as sanPhamDaBan, " +
            "COUNT(DISTINCT hd.id) as tongSoDonHang) " +
            "FROM HoaDon hd " +
            "JOIN hd.chiTietHoaDon hdct " +
            "WHERE day(hd.createdAt) = day(:ngay) " +
            "AND month(hd.createdAt) = month(:ngay) " +
            "AND year(hd.createdAt) = year(:ngay)")
    Map<String, Object> tangTruongTheoNgay(@Param("ngay") Date ngay);

    @Query("SELECT new map(" +
            "SUM(hd.tongTien) as doanhThu, " +
            "COUNT(hdct.id) as sanPhamDaBan, " +
            "COUNT(DISTINCT hd.id) as tongSoDonHang) " +
            "FROM HoaDon hd " +
            "JOIN hd.chiTietHoaDon hdct " +
            "WHERE month(hd.createdAt) = month(:thang) " +
            "AND year(hd.createdAt) = year(:thang)")
    Map<String, Object> tangTruongTheoThang(@Param("thang") Date thang);

    @Query("SELECT new map(" +
            "SUM(hd.tongTien) as doanhThu, " +
            "COUNT(hdct.id) as sanPhamDaBan, " +
            "COUNT(DISTINCT hd.id) as tongSoDonHang) " +
            "FROM HoaDon hd " +
            "JOIN hd.chiTietHoaDon hdct " +
            "WHERE year(hd.createdAt) = year(:nam)")
    Map<String, Object> tangTruongTheoNam(@Param("nam") Date nam);

    // Trạng thái đơn hàng
    @Query("SELECT new map(hd.trangThai as trangThai, COUNT(hd.id) as soLuong) " +
            "FROM HoaDon hd " +
            "WHERE (:filterType = 'day' AND day(hd.createdAt) = day(:date) " +
            "AND month(hd.createdAt) = month(:date) " +
            "AND year(hd.createdAt) = year(:date)) " +
            "OR (:filterType = 'month' AND month(hd.createdAt) = month(:date) " +
            "AND year(hd.createdAt) = year(:date)) " +
            "OR (:filterType = 'year' AND year(hd.createdAt) = year(:date)) " +
            "GROUP BY hd.trangThai")
    List<Map<String, Object>> thongKeTrangThaiDonHang(
            @Param("filterType") String filterType,
            @Param("date") Date date
    );

    // Sản phẩm sắp hết hàng (phân trang)
    @Query("SELECT new com.example.graduation_project_group_2_mobileworld.dto.thongKe.SanPhamHetHangDTO(sp.tenSanPham, COUNT(ctsp)) " +
            "FROM ChiTietSanPham ctsp " +
            "JOIN ctsp.idSanPham sp " +
            "WHERE ctsp.deleted = false "+
            "GROUP BY sp.id, sp.tenSanPham " +
            "HAVING COUNT(ctsp) < 10 " +
            "ORDER BY sp.tenSanPham ASC")
    Page<SanPhamHetHangDTO> thongKeSanPhamHetHang(Pageable pageable);

    @Query("SELECT new com.example.graduation_project_group_2_mobileworld.dto.thongKe.SanPhamHetHangDTO(sp.tenSanPham, COUNT(ctsp)) " +
            "FROM ChiTietSanPham ctsp " +
            "JOIN ctsp.idSanPham sp " +
            "WHERE ctsp.deleted = false "+
            "GROUP BY sp.id, sp.tenSanPham " +
            "HAVING COUNT(ctsp) < 10 " +
            "ORDER BY sp.tenSanPham ASC")
    List<SanPhamHetHangDTO> thongKeSanPhamHetHangNoPage();

    // Phân phối đa kênh
    @Query("SELECT new com.example.graduation_project_group_2_mobileworld.dto.thongKe.LoaiHoaDonDTO(hd.loaiDon, COUNT(hd.id)) " +
            "FROM HoaDon hd " +
            "GROUP BY hd.loaiDon")
    List<LoaiHoaDonDTO> thongKeLoaiHoaDon();


    @Query("SELECT new com.example.graduation_project_group_2_mobileworld.dto.thongKe.HangBanChayDTO(nsx.nhaSanXuat, SUM(hdct.gia)) " +
            "FROM HoaDonChiTiet hdct " +
            "JOIN hdct.idChiTietSanPham ctsp " +
            "JOIN ctsp.idSanPham sp " +
            "JOIN sp.idNhaSanXuat nsx " +
            "GROUP BY nsx.id, nsx.nhaSanXuat")
    List<HangBanChayDTO> thongKeHangBanChay();
}