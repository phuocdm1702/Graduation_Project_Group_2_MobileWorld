package com.example.graduation_project_group_2_mobileworld.repository.giam_gia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhieuGiamGiaCaNhanRepository extends JpaRepository<PhieuGiamGiaCaNhan, Integer> {
    @Modifying
    @Query("DELETE FROM PhieuGiamGiaCaNhan p WHERE p.idPhieuGiamGia.id = :phieuGiamGiaId")
    void deleteByIdPhieuGiamGia(@Param("phieuGiamGiaId") Integer phieuGiamGiaId);

    List<PhieuGiamGiaCaNhan> findByIdPhieuGiamGia(PhieuGiamGia phieuGiamGia);
}
