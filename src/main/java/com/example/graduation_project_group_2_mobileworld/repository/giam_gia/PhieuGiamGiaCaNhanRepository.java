package com.example.graduation_project_group_2_mobileworld.repository.giam_gia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhieuGiamGiaCaNhanRepository extends JpaRepository<PhieuGiamGiaCaNhan, Integer> {
    List<PhieuGiamGiaCaNhan> findByIdPhieuGiamGia(PhieuGiamGia phieuGiamGia);

    @Transactional
    @Modifying
    @Query("DELETE FROM PhieuGiamGiaCaNhan p WHERE p.idPhieuGiamGia = :phieuGiamGia")
    void deleteByIdPhieuGiamGia(PhieuGiamGia phieuGiamGia);
}
