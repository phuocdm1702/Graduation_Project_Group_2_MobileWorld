package com.example.graduation_project_group_2_mobileworld.repository.san_pham.chi_tiet_san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.ChiTietSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer> {

    // Load danh sách sản phẩm chi tiết với phân trang
    @Query("SELECT c FROM ChiTietSanPham c WHERE c.deleted = false")
    Page<ChiTietSanPham> findAllActive(Pageable pageable);

    // Tìm kiếm và lọc
    @Query("SELECT c FROM ChiTietSanPham c WHERE c.deleted = false " +
            "AND (:keyword IS NULL OR c.tienIchDacBiet LIKE %:keyword%) " +
            "AND (:idNhaSanXuat IS NULL OR c.idNhaSanXuat.id = :idNhaSanXuat) " +
            "AND (:idMauSac IS NULL OR c.idMauSac.id = :idMauSac) " +
            "AND (:idPin IS NULL OR c.idPin.id = :idPin) " +
            "AND (:idManHinh IS NULL OR c.idManHinh.id = :idManHinh) " +
            "AND (:idRam IS NULL OR c.idRam.id = :idRam) " +
            "AND (:idBoNhoTrong IS NULL OR c.idBoNhoTrong.id = :idBoNhoTrong) " +
            "AND (:idCpu IS NULL OR c.idCpu.id = :idCpu) " +
            "AND (:idGpu IS NULL OR c.idGpu.id = :idGpu) " +
            "AND (:idCumCamera IS NULL OR c.idCumCamera.id = :idCumCamera) " +
            "AND (:idHeDieuHanh IS NULL OR c.idHeDieuHanh.id = :idHeDieuHanh) " +
            "AND (:idThietKe IS NULL OR c.idThietKe.id = :idThietKe) " +
            "AND (:idSim IS NULL OR c.idSim.id = :idSim) " +
            "AND (:idCongSac IS NULL OR c.idCongSac.id = :idCongSac) " +
            "AND (:idHoTroCongNgheSac IS NULL OR c.idHoTroCongNgheSac.id = :idHoTroCongNgheSac) " +
            "AND (:idCongNgheMang IS NULL OR c.idCongNgheMang.id = :idCongNgheMang) " +
            "AND (:idLoaiTinhTrang IS NULL OR c.idLoaiTinhTrang.id = :idLoaiTinhTrang)")
    Page<ChiTietSanPham> searchAndFilter(
            @Param("keyword") String keyword,
            @Param("idNhaSanXuat") Integer idNhaSanXuat,
            @Param("idMauSac") Integer idMauSac,
            @Param("idPin") Integer idPin,
            @Param("idManHinh") Integer idManHinh,
            @Param("idRam") Integer idRam,
            @Param("idBoNhoTrong") Integer idBoNhoTrong,
            @Param("idCpu") Integer idCpu,
            @Param("idGpu") Integer idGpu,
            @Param("idCumCamera") Integer idCumCamera,
            @Param("idHeDieuHanh") Integer idHeDieuHanh,
            @Param("idThietKe") Integer idThietKe,
            @Param("idSim") Integer idSim,
            @Param("idCongSac") Integer idCongSac,
            @Param("idHoTroCongNgheSac") Integer idHoTroCongNgheSac,
            @Param("idCongNgheMang") Integer idCongNgheMang,
            @Param("idLoaiTinhTrang") Integer idLoaiTinhTrang,
            Pageable pageable
    );
}
