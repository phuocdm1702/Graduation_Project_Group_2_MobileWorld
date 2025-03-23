package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.CumCamera;
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
            "tscTruoc.thongSoCamTruoc AS cameraTruoc, tscSau.thongSoCamera AS cameraSau " +
            "FROM CumCamera cc " +
            "LEFT JOIN ChiTietCameraTruoc ctct ON cc.id = ctct.id " +
            "LEFT JOIN ThongSoCameraTruoc tscTruoc ON ctct.id = tscTruoc.id " +
            "LEFT JOIN ChiTietCameraSau ctcs ON cc.id = ctcs.id " +
            "LEFT JOIN ThongSoCameraSau tscSau ON ctcs.id = tscSau.id " +
            "WHERE cc.deleted = false " +
            "AND (ctct.deleted = false OR ctct.deleted IS NULL) " +
            "AND (ctcs.deleted = false OR ctcs.deleted IS NULL) " +
            "AND (tscTruoc.deleted = false OR tscTruoc.deleted IS NULL) " +
            "AND (tscSau.deleted = false OR tscSau.deleted IS NULL) " +
            "ORDER BY cc.ma")
    Page<Object[]> findCameraDetails(Pageable pageable);
}
