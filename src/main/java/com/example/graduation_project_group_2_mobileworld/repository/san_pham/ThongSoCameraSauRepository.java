package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.ThongSoCameraSau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongSoCameraSauRepository extends JpaRepository<ThongSoCameraSau, Integer> {
    List<ThongSoCameraSau> findByDeletedFalse();
}