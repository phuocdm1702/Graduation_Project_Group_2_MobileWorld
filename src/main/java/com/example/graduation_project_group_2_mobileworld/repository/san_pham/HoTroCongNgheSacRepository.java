package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.HoTroCongNgheSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoTroCongNgheSacRepository extends JpaRepository<HoTroCongNgheSac, Integer> {

    List<HoTroCongNgheSac> findByDeletedFalse();

    Page<HoTroCongNgheSac> findByDeletedFalse(Pageable pageable);

    @Query(value = "SELECT htcs.id AS ho_tro_cong_nghe_sac_id, htcs.ma AS ho_tro_cong_nghe_sac_ma, " +
            "cns.ten_cong_nghe AS ten_cong_nghe_sac " +
            "FROM ho_tro_cong_nghe_sac htcs " +
            "LEFT JOIN ho_tro_sac hts ON htcs.id = hts.id_ho_tro_cong_nghe_sac " +
            "LEFT JOIN cong_nghe_sac cns ON hts.id_cong_nghe_sac = cns.id " +
            "WHERE htcs.deleted = 0 " +
            "AND (hts.deleted = 0 OR hts.deleted IS NULL) " +
            "AND (cns.deleted = 0 OR cns.deleted IS NULL) " +
            "ORDER BY htcs.ma",
            nativeQuery = true)
    Page<Object[]> findChargingTechDetails(Pageable pageable);
}