package com.example.graduation_project_group_2_mobileworld.repository.bao_hanh;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuBaoHanh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuBaoHanhRepository extends JpaRepository<PhieuBaoHanh, Integer> {
}
