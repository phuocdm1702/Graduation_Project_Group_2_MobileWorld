package com.example.graduation_project_group_2_mobileworld.repository.dongSanPham;

import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DongSanPhamRepository extends JpaRepository<DongSanPham, Integer> {

    // Lấy danh sách dòng sản phẩm chưa bị xóa (deleted = false)
    List<DongSanPham> findByDeletedFalse();

    // Lấy danh sách dòng sản phẩm chưa bị xóa với phân trang
    Page<DongSanPham> findByDeletedFalse(Pageable pageable);

    // Tìm kiếm dòng sản phẩm theo keyword (mã hoặc tên) và phân trang
    @Query("SELECT d FROM DongSanPham d " +
            "WHERE (LOWER(d.ma) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.dongSanPham) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND d.deleted = false")
    Page<DongSanPham> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Kiểm tra mã dòng sản phẩm đã tồn tại chưa
    boolean existsByMa(String ma);

    // Kiểm tra tên dòng sản phẩm đã tồn tại chưa
    boolean existsByDongSanPham(String dongSanPham);
}