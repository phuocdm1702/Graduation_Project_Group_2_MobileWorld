package com.example.graduation_project_group_2_mobileworld.repository.nhan_vien;


import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    @Query("SELECT COUNT(n) > 0 FROM NhanVien n WHERE n.ma = :ma AND n.id <> :id")
    boolean existsByMaAndNotId(@Param("ma") String ma, @Param("id") Integer id);

    @Query("SELECT n FROM NhanVien n")
    List<NhanVien> findAllActiveNv();

    @Query("SELECT MAX(CAST(SUBSTRING(n.ma, 3, LENGTH(n.ma)) AS int)) FROM NhanVien n")
    Integer findMaxMa();

    boolean existsByMa(String finalCode);

    @Query("SELECT nv FROM NhanVien nv JOIN nv.idTaiKhoan tk " +
            "WHERE (LOWER(nv.ma) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(nv.tenNhanVien) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(tk.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(tk.soDienThoai) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:status = 'tat-ca' OR (nv.deleted = true AND :status = 'da-nghi') OR (nv.deleted = false AND :status = 'dang-lam'))")
    List<NhanVien> searchNhanVien(@Param("keyword") String keyword, @Param("status") String status);

    Optional<NhanVien> findByMa(String ma);
}
