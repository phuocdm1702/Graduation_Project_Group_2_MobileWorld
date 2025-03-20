package com.example.graduation_project_group_2_mobileworld.repository.chiTietDotGiamGia;

import com.example.graduation_project_group_2_mobileworld.entity.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CTSPForCTDGG extends JpaRepository<ChiTietSanPham,Integer> {
    @Query("SELECT ctsp FROM ChiTietSanPham ctsp WHERE ctsp.idSanPham.id IN :idSanPham AND ctsp.deleted = false")
    public List<ChiTietSanPham> findAllByIdSanPhamIn(@Param("idSanPham") List<Integer> idSanPham);
}
