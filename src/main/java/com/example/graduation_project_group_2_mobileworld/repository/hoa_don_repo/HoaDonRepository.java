package com.example.graduation_project_group_2_mobileworld.repository.hoa_don_repo;

import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
}
