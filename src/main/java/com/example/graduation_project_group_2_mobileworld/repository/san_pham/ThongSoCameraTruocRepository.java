package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.ThongSoCameraTruoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongSoCameraTruocRepository extends JpaRepository<ThongSoCameraTruoc, Integer> {
    List<ThongSoCameraTruoc> findByDeletedFalse();
}