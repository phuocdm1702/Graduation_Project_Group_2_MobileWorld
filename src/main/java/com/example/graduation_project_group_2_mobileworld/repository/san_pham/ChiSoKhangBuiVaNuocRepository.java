package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.ChiSoKhangBuiVaNuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiSoKhangBuiVaNuocRepository extends JpaRepository<ChiSoKhangBuiVaNuoc, Integer> {
}
