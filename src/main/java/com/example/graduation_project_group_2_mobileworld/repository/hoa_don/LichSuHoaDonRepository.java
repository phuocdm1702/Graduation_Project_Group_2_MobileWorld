package com.example.graduation_project_group_2_mobileworld.repository.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Integer> {

        @Query("SELECT MAX(id) FROM LichSuHoaDon")
        Integer findMaxId();
}
