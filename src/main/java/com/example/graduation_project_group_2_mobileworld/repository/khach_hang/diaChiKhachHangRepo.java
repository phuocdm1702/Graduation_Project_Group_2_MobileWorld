package com.example.graduation_project_group_2_mobileworld.repository.khach_hang;

import com.example.graduation_project_group_2_mobileworld.entity.DiaChiKhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface diaChiKhachHangRepo extends JpaRepository<DiaChiKhachHang,Integer> {
    @Query("SELECT MAX(CAST(SUBSTRING(n.ma, 5, LEN(n.ma) - 4) AS int)) FROM DiaChiKhachHang n WHERE LEN(n.ma) >= 5")
    Integer findMaxMa();

    List<DiaChiKhachHang> findAllByIdKhachHangId(Integer idKhachHang); // Thêm phương thức này

    List<DiaChiKhachHang> findByIdKhachHang(@NotNull KhachHang idKhachHang);
}
