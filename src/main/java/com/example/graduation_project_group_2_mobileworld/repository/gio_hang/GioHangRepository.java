package com.example.graduation_project_group_2_mobileworld.repository.gio_hang;

import com.example.graduation_project_group_2_mobileworld.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    @Query("Select gh From GioHang gh where gh.id = ?1")
    Optional<GioHang> findByHoaDonId(Long hoaDonId);
}
