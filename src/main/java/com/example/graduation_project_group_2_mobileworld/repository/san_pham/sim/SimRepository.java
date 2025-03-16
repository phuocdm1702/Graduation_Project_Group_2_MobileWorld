package com.example.graduation_project_group_2_mobileworld.repository.san_pham.sim;

import com.example.graduation_project_group_2_mobileworld.entity.Sim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimRepository extends JpaRepository<Sim, Integer> {
}
