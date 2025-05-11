package com.example.graduation_project_group_2_mobileworld.repository.gio_hang;

import com.example.graduation_project_group_2_mobileworld.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    @Query("SELECT gh FROM GioHang gh WHERE gh.deleted = false")
    List<GioHang> findByDeletedFalse();
}
