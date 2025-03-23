package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.ChiTietSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer> {

    // Load danh sách sản phẩm chi tiết với phân trang, sắp xếp theo ngày tạo mới nhất
    @Query("SELECT c FROM ChiTietSanPham c WHERE c.deleted = false ORDER BY c.createdAt DESC")
    Page<ChiTietSanPham> findAllActive(Pageable pageable);

    // Tìm kiếm và lọc
    @Query("SELECT c FROM ChiTietSanPham c WHERE c.deleted = false " +
            "AND (:keyword IS NULL OR LOWER(c.tienIchDacBiet) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:idSanPham IS NULL OR c.idSanPham.id = :idSanPham) " +
            "AND (:idMauSac IS NULL OR c.idMauSac.id = :idMauSac) " +
            "AND (:idRam IS NULL OR c.idRam.id = :idRam) " +
            "AND (:idBoNhoTrong IS NULL OR c.idBoNhoTrong.id = :idBoNhoTrong) " +
            "AND (:idLoaiTinhTrang IS NULL OR c.idLoaiTinhTrang.id = :idLoaiTinhTrang)")
    Page<ChiTietSanPham> searchAndFilter(
            @Param("keyword") String keyword,
            @Param("idSanPham") Integer idSanPham,
            @Param("idMauSac") Integer idMauSac,
            @Param("idRam") Integer idRam,
            @Param("idBoNhoTrong") Integer idBoNhoTrong,
            @Param("idLoaiTinhTrang") Integer idLoaiTinhTrang,
            Pageable pageable
    );
}
