package com.example.graduation_project_group_2_mobileworld.repository.dot_giam_gia_repo;

import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.dot_giam_gia_DTO;
import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO;
import com.example.graduation_project_group_2_mobileworld.entity.ChiTietDotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

public interface dot_giam_gia_repository extends JpaRepository<DotGiamGia, Integer> {

    @Query("Select dgg From DotGiamGia dgg where dgg.deleted=false ")
    public List<DotGiamGia> hienThi();

    @Query("SELECT dsp FROM DongSanPham dsp WHERE (:timKiem IS NULL OR :timKiem = '' OR dsp.ma LIKE CONCAT('%', :timKiem, '%') OR dsp.dongSanPham LIKE CONCAT('%', :timKiem, '%')) AND dsp.deleted = false ")
    public List<DongSanPham> getAllDongSanPham(@Param("timKiem") String timKiem);

    @Query("SELECT new com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO(dsp, ctsp, anh) " +
            "FROM DongSanPham dsp " +
            "INNER JOIN ChiTietSanPham ctsp ON ctsp.idDongSanPham.id = dsp.id " +
            "INNER JOIN AnhSanPham anh ON ctsp.idAnhSanPham.id = anh.id " +
            "WHERE dsp.id IN :ids")
    List<viewCTSPDTO> getAllCTSP(@Param("ids") List<Integer> ids);

    @Modifying
    @Transactional
    @Query("UPDATE DotGiamGia d SET d.deleted = true WHERE d.id = :id")
    public void updateDotGiamGiaDeleted(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE ChiTietDotGiamGia ct SET ct.deleted = true WHERE ct.dotGiamGia.id = :id")
    public void updateChiTietDotGiamGiaDeleted(@Param("id") Integer id);

    @Query("SELECT dsp FROM DongSanPham dsp " +
            "JOIN ChiTietDotGiamGia ctdgg ON ctdgg.idDongSanPham = dsp " +
            "JOIN ctdgg.dotGiamGia dgg " +
            "WHERE dgg.id = :id")
    List<DongSanPham> getThatDongSanPham(@Param("id") Integer id);



}
