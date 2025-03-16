package com.example.graduation_project_group_2_mobileworld.repository.san_pham.tinh_trang;

import com.example.graduation_project_group_2_mobileworld.entity.TinhTrang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TinhTrangRepository extends JpaRepository<TinhTrang, Integer> {
}
