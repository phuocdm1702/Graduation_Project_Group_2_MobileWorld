package com.example.graduation_project_group_2_mobileworld.repository.san_pham.bo_nho_trong;

import com.example.graduation_project_group_2_mobileworld.entity.BoNhoTrong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoNhoTrongRepository extends JpaRepository<BoNhoTrong, Integer> {
}
