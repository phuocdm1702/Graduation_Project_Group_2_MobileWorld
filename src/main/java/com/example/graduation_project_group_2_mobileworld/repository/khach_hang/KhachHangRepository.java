package com.example.graduation_project_group_2_mobileworld.repository.khach_hang;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

    // Kiểm tra mã khách hàng đã tồn tại hay chưa
    boolean existsByMa(String ma);

    // Kiểm tra mã khách hàng đã tồn tại nhưng không tính khách hàng hiện tại (dùng khi update)
    @Query("SELECT COUNT(kh) > 0 FROM KhachHang kh WHERE kh.ma = :ma AND kh.id != :id")
    boolean existsByMaAndNotId(@Param("ma") String ma, @Param("id") Integer id);

    @Query("SELECT k FROM KhachHang k")
    List<KhachHang> findAllActiveCustomers();

    @Query("SELECT MAX(kh.ma) FROM KhachHang kh")
    String findMaxMa();

    @Query("SELECT k FROM KhachHang k WHERE CONCAT(k.ma, k.ten) LIKE %?1% ")
    List<KhachHang> searchFormAdd(String keyword);
    @Query("SELECT k from KhachHang k where k.ten like %?1% OR k.idTaiKhoan.soDienThoai like %?1%")
    List<KhachHang> searchBh(String keyword);

    Optional<KhachHang> findByMa(String ma);

    List<KhachHang> findByIdTaiKhoan(TaiKhoan taiKhoan);


}