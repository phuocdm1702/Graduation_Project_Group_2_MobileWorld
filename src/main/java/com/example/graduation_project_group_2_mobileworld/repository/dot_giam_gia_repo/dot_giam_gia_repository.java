package com.example.graduation_project_group_2_mobileworld.repository.dot_giam_gia_repo;

import com.example.graduation_project_group_2_mobileworld.entity.AnhSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;

public interface dot_giam_gia_repository extends JpaRepository<DotGiamGia, Integer> {

    @Query("SELECT dsp FROM DongSanPham dsp WHERE (:timKiem IS NULL OR :timKiem = '' OR dsp.ma LIKE CONCAT('%', :timKiem, '%') OR dsp.dongSanPham LIKE CONCAT('%', :timKiem, '%')) AND dsp.deleted = false ")
    public List<DongSanPham> getAllDongSanPham(@Param("timKiem") String timKiem);

//    @Query("Select ctsp,anh,dsp from ChiTietSanPham ctsp inner join AnhSanPham anh on ctsp.idAnhSanPham = anh.id inner join DongSanPham dsp on dsp.id= ctsp.idDongSanPham")
//    public List<Objects[]> getCTSP();
}
