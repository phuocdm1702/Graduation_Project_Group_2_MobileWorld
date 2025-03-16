package com.example.graduation_project_group_2_mobileworld.repository.san_pham.gpu;

import com.example.graduation_project_group_2_mobileworld.entity.Gpu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GpuRepository extends JpaRepository<Gpu, Integer> {
}
