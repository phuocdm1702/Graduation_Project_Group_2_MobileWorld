package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham.HeDieuHanh;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.Pin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PinRepository extends JpaRepository<Pin, Integer> {

    List<Pin> findByDeletedFalse();

    Page<Pin> findByDeletedFalse(Pageable pageable);
}
