package com.example.graduation_project_group_2_mobileworld.repository.phuong_thuc_thanh_toan;

import com.example.graduation_project_group_2_mobileworld.entity.PhuongThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PhuongThucThanhToanRepository extends JpaRepository<PhuongThucThanhToan, Integer> {
    @Query("SELECT p FROM PhuongThucThanhToan p WHERE p.kieuThanhToan = :kieuThanhToan AND p.deleted = false")
    Optional<PhuongThucThanhToan> findByKieuThanhToan(String kieuThanhToan);
}
