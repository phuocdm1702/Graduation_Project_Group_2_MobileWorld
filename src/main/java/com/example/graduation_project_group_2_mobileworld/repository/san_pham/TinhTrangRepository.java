package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.Gpu;
import com.example.graduation_project_group_2_mobileworld.entity.TinhTrang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TinhTrangRepository extends JpaRepository<TinhTrang, Integer> {

    List<TinhTrang> findByDeletedFalse();

    Page<TinhTrang> findByDeletedFalse(Pageable pageable);
}
