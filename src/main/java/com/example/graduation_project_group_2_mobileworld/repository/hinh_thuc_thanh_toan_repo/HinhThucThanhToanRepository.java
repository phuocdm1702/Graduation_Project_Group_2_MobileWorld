package com.example.graduation_project_group_2_mobileworld.repository.hinh_thuc_thanh_toan_repo;

import com.example.graduation_project_group_2_mobileworld.entity.HinhThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HinhThucThanhToanRepository extends JpaRepository<HinhThucThanhToan, Integer> {


}
