package com.example.graduation_project_group_2_mobileworld.repository.quyen_han;

import com.example.graduation_project_group_2_mobileworld.entity.QuyenHan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuyenHanRepository extends JpaRepository<QuyenHan, Integer> {
    QuyenHan findByCapQuyenHan(Integer capQuyenHan);
}
