package com.example.graduation_project_group_2_mobileworld.repository.tai_khoan;

import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    @Query("SELECT MAX(t.ma) FROM TaiKhoan t WHERE t.ma LIKE 'TK%'")
    String findMaxMaTK();


    // Filter for login
    @Query("SELECT tk FROM TaiKhoan tk WHERE tk.tenDangNhap = :tenDangNhap")
    TaiKhoan findByTenDangNhap(@Param("tenDangNhap") String tenDangNhap);

    @Query("SELECT tk FROM TaiKhoan tk WHERE tk.tenDangNhap = :tenDangNhap AND tk.matKhau = :matKhau")
    TaiKhoan findByTenDangNhapAndMatKhau(@Param("tenDangNhap") String tenDangNhap, @Param("matKhau") String matKhau);

    //checkEmail
    Optional<TaiKhoan> findByEmail(String email);
    Optional<TaiKhoan> findBytenDangNhap(String tenDangNhap);
    Optional<TaiKhoan> findBySoDienThoai(String soDienThoai);
}
