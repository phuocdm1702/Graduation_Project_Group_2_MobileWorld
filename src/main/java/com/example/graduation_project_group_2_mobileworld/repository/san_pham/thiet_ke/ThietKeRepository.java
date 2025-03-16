package com.example.graduation_project_group_2_mobileworld.repository.san_pham.thiet_ke;

import com.example.graduation_project_group_2_mobileworld.entity.ThietKe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThietKeRepository extends JpaRepository<ThietKe, Integer> {
}
