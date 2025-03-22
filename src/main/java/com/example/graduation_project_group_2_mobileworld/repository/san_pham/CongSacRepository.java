package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.CongSac;
import com.example.graduation_project_group_2_mobileworld.entity.HeDieuHanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CongSacRepository extends JpaRepository<CongSac, Integer> {

    List<CongSac> findByDeletedFalse();

    Page<CongSac> findByDeletedFalse(Pageable pageable);
}
