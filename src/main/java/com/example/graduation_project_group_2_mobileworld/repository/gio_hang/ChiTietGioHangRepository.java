package com.example.graduation_project_group_2_mobileworld.repository.gio_hang;

import com.example.graduation_project_group_2_mobileworld.entity.ChiTietGioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, Integer> {
}
