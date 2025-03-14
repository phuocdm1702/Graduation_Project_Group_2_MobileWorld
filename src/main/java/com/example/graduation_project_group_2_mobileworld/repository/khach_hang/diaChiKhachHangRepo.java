package com.example.graduation_project_group_2_mobileworld.repository.khach_hang;

import com.example.graduation_project_group_2_mobileworld.entity.DiaChiKhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface diaChiKhachHangRepo extends JpaRepository<DiaChiKhachHang,Integer> {
    @Query("SELECT MAX(CAST(SUBSTRING(n.ma, 5, LEN(n.ma) - 4) AS int)) FROM DiaChiKhachHang n WHERE LEN(n.ma) >= 5")
    Integer findMaxMa();


}
