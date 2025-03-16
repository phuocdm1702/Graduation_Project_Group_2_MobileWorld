package com.example.graduation_project_group_2_mobileworld.repository.san_pham.sac;

import com.example.graduation_project_group_2_mobileworld.entity.CongSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CongSacRepository extends JpaRepository<CongSac, Integer> {
}
