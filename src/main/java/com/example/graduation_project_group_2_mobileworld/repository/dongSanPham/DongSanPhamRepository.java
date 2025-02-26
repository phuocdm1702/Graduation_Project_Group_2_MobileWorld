package com.example.graduation_project_group_2_mobileworld.repository.dongSanPham;

import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DongSanPhamRepository extends JpaRepository<DongSanPham, Integer> {
    List<DongSanPham> findByDeletedFalse();
}
