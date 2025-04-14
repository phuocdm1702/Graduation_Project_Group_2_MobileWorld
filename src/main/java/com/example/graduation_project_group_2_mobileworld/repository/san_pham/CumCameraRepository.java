package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham.CumCamera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CumCameraRepository extends JpaRepository<CumCamera, Integer> {

    List<CumCamera> findByDeletedFalse();

    Page<CumCamera> findByDeletedFalse(Pageable pageable);

    @Query("SELECT cc.id AS cumCameraId, cc.ma AS cumCameraMa, " +
            "cc.cameraTruoc AS cameraTruoc, cc.cameraSau AS cameraSau " +
            "FROM CumCamera cc " +
            "WHERE cc.deleted = false " +
            "ORDER BY cc.ma")
    Page<Object[]> findCameraDetails(Pageable pageable);
}