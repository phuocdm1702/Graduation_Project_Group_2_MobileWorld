package com.example.graduation_project_group_2_mobileworld.repository.san_pham.mau_sac;

import com.example.graduation_project_group_2_mobileworld.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Integer> {
}
