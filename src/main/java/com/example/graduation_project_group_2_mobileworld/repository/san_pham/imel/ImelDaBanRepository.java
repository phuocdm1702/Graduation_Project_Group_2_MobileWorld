package com.example.graduation_project_group_2_mobileworld.repository.san_pham.imel;

import com.example.graduation_project_group_2_mobileworld.entity.ImelDaBan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImelDaBanRepository extends JpaRepository<ImelDaBan, Integer> {
    Page<ImelDaBan> findByDeletedFalse(Pageable pageable);
}