package com.example.graduation_project_group_2_mobileworld.repository.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTOGet;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

    // Lấy các hóa đơn chưa xác nhận
    @Query("SELECT hd FROM HoaDon hd WHERE hd.trangThai = 0 AND hd.deleted = false")
    List<HoaDon> findAllHDNotConfirm();

    // Lấy tất cả hóa đơn với phân trang

    @Query(
            """
                    select new com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTO
                    (
                   hd.ma,
                   hd.idNhanVien.ma,
                   hd.tenKhachHang,
                   hd.soDienThoaiKhachHang,
                   hd.tongTienSauGiam,
                   hd.idPhieuGiamGia.phanTramGiamGia,
                   hd.phiVanChuyen,
                   hd.ngayTao,
                   hd.loaiDon,
                   hd.trangThai
                    )
                    from HoaDon hd 
                    """
    )
    Page<HoaDonDTO> findAllWithPagination(Pageable pageable);
//    @Query("SELECT hd FROM HoaDon hd ORDER BY hd.id DESC")
//    Page<HoaDon> findAllWithPagination(Pageable pageable);

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

    @Query("""
                SELECT new com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTO(
                    hd.ma,
                    hd.idNhanVien.ma,
                    hd.tenKhachHang,
                    hd.soDienThoaiKhachHang,
                    hd.tongTienSauGiam,
                    hd.idPhieuGiamGia.phanTramGiamGia,
                    hd.phiVanChuyen,
                    hd.ngayTao,
                    hd.loaiDon,
                    hd.trangThai
                )
                FROM HoaDon hd
                WHERE (:keyword IS NULL 
                    OR hd.ma LIKE %:keyword% 
                    OR hd.idNhanVien.tenNhanVien LIKE %:keyword% 
                    OR hd.tenKhachHang LIKE %:keyword% 
                    OR hd.soDienThoaiKhachHang LIKE %:keyword%)
                AND (:loaiDon IS NULL OR hd.loaiDon = :loaiDon)
                AND (:minAmount IS NULL OR hd.tongTienSauGiam >= :minAmount)
                AND (:maxAmount IS NULL OR hd.tongTienSauGiam <= :maxAmount)
                AND (:startDate IS NULL OR hd.ngayTao >= :startDate)
                AND (:endDate IS NULL OR hd.ngayTao <= :endDate)
                AND (:trangThai IS NULL OR hd.trangThai = :trangThai)
                ORDER BY hd.id DESC
            """)
    Page<HoaDonDTOGet> findHoaDonWithFilters(
            @Param("keyword") String keyword,
            @Param("loaiDon") String loaiDon,
            @Param("minAmount") Long minAmount,
            @Param("maxAmount") Long maxAmount,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("trangThai") Short trangThai,
            Pageable pageable);



//    @Query("SELECT hd FROM HoaDon hd " +
//            "WHERE (:keyword IS NULL OR hd.ma LIKE %:keyword% OR hd.idNhanVien.tenNhanVien LIKE %:keyword% " +
//            "OR hd.tenKhachHang LIKE %:keyword% OR hd.soDienThoaiKhachHang LIKE %:keyword%) " +
//            "AND (:loaiDon IS NULL OR hd.loaiDon = :loaiDon) " +
//            "AND (:minAmount IS NULL OR hd.tongTienSauGiam >= :minAmount) " +
//            "AND (:maxAmount IS NULL OR hd.tongTienSauGiam <= :maxAmount) " +
//            "AND (:startDate IS NULL OR hd.ngayTao >= :startDate) " +
//            "AND (:endDate IS NULL OR hd.ngayTao <= :endDate) " +
//            "AND (:trangThai IS NULL OR hd.trangThai = :trangThai) " +
//            "ORDER BY hd.id DESC")
//    Page<HoaDon> findHoaDonWithFilters(
//            @Param("keyword") String keyword,
//            @Param("loaiDon") String loaiDon,
//            @Param("minAmount") Long minAmount,
//            @Param("maxAmount") Long maxAmount,
//            @Param("startDate") Timestamp startDate,
//            @Param("endDate") Timestamp endDate,
//            @Param("trangThai") Short trangThai,
//            Pageable pageable);

    // Tìm hóa đơn theo mã
    @Query("SELECT hd FROM HoaDon hd WHERE hd.ma = :ma")
    Optional<HoaDonDTO> findByMa(@Param("ma") String ma);
}