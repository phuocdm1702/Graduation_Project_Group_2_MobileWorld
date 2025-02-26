package com.example.graduation_project_group_2_mobileworld.service.dot_giam_gia_service;

import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import com.example.graduation_project_group_2_mobileworld.repository.dot_giam_gia_repo.dot_giam_gia_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class dot_giam_gia_service{

    private dot_giam_gia_repository repository;

    @Autowired
    public dot_giam_gia_service(dot_giam_gia_repository repository) {
        this.repository = repository;
    }


    public List<DotGiamGia> getAll(){
        return repository.findAll();
    }

    public List<DongSanPham> getDSP(String timKiem){
        return repository.getAllDongSanPham(timKiem);
    }

//    public List<Objects[]> getCTSP(){
//        return repository.getCTSP();
//    }
}
