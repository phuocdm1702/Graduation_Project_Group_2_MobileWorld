package com.example.graduation_project_group_2_mobileworld.repository.nhan_vien;


import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    @Query("SELECT COUNT(n) > 0 FROM NhanVien n WHERE n.ma = :ma AND n.id <> :id")
    boolean existsByMaAndNotId(@Param("ma") String ma, @Param("id") Integer id);

    @Query("SELECT n FROM NhanVien n")
    List<NhanVien> findAllActiveNv();

    @Query("SELECT MAX(CAST(SUBSTRING(n.ma, 3, LENGTH(n.ma)) AS int)) FROM NhanVien n")
    Integer findMaxMa();

}
