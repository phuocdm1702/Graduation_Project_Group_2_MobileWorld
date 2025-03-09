package com.example.graduation_project_group_2_mobileworld.repository.tai_khoan;

import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    @Query("SELECT MAX(t.ma) FROM TaiKhoan t WHERE t.ma LIKE 'TK%'")
    String findMaxMaTK();
}
