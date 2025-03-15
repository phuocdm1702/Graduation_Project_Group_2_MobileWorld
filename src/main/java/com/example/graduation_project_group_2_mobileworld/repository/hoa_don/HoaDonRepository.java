package com.example.graduation_project_group_2_mobileworld.repository.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    @Query("SELECT hd FROM HoaDon hd " +
            "ORDER BY hd.id DESC")
    Page<HoaDon> findAllWithPagination(Pageable pageable);
}