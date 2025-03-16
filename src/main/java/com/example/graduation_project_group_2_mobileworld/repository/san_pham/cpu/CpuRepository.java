package com.example.graduation_project_group_2_mobileworld.repository.san_pham.cpu;

import com.example.graduation_project_group_2_mobileworld.entity.Cpu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpuRepository extends JpaRepository<Cpu, Integer> {
}
