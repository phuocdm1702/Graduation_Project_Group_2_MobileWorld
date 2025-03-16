package com.example.graduation_project_group_2_mobileworld.repository.san_pham.mang;

import com.example.graduation_project_group_2_mobileworld.entity.CongNgheMang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CongNgheMangRepository extends JpaRepository<CongNgheMang, Integer> {
}
