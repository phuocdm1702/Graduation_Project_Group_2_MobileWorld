package com.example.graduation_project_group_2_mobileworld.repository.bao_hanh;

import com.example.graduation_project_group_2_mobileworld.entity.LichSuBaoHanh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LichSuBaoHanhRepository extends JpaRepository<LichSuBaoHanh, Integer> {
}
