package com.example.graduation_project_group_2_mobileworld.repository.san_pham.cum_camera;

import com.example.graduation_project_group_2_mobileworld.entity.CumCamera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CumCameraRepository extends JpaRepository<CumCamera, Integer> {
}
