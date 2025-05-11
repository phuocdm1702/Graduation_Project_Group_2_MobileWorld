package com.example.graduation_project_group_2_mobileworld.repository.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
    @Query("SELECT hdct FROM HoaDonChiTiet hdct " +
            "JOIN FETCH hdct.hoaDon hd " +
            "JOIN FETCH hdct.idChiTietSanPham ctsp " +
            "JOIN FETCH ctsp.idSanPham sp " +
            "JOIN FETCH ctsp.idImel im " +
            "WHERE hdct.hoaDon.id = :hoaDonId AND hdct.deleted = false")
    List<HoaDonChiTiet> findByHoaDonIdAndNotDeleted(@Param("hoaDonId") Integer hoaDonId);

    @Query("SELECT hdct FROM HoaDonChiTiet hdct " +
            "WHERE hdct.idChiTietSanPham.id = :chiTietSanPhamId AND hdct.deleted = false")
    List<HoaDonChiTiet> findByChiTietSanPhamId(@Param("chiTietSanPhamId") Integer chiTietSanPhamId);
}
