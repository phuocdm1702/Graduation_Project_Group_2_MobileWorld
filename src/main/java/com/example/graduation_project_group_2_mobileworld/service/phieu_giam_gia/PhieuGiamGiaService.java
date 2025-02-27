package com.example.graduation_project_group_2_mobileworld.service.phieu_giam_gia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaCaNhanRepository;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhieuGiamGiaService {

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Autowired
    private PhieuGiamGiaCaNhanRepository phieuGiamGiaCaNhanRepository;

    public PhieuGiamGiaService(PhieuGiamGiaRepository phieuGiamGiaRepository, PhieuGiamGiaCaNhanRepository phieuGiamGiaCaNhanRepository) {
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.phieuGiamGiaCaNhanRepository = phieuGiamGiaCaNhanRepository;
    }

    public List<PhieuGiamGia> getPGG() {
        return phieuGiamGiaRepository.findAll();
    }

    public List<PhieuGiamGiaCaNhan> getPGGCN() {
        return phieuGiamGiaCaNhanRepository.findAll();
    }

    public PhieuGiamGia addPGG(PhieuGiamGia phieuGiamGia) {
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }

    public PhieuGiamGiaCaNhan addPGGCN(PhieuGiamGiaCaNhan phieuGiamGiaCaNhan) {
        return phieuGiamGiaCaNhanRepository.save(phieuGiamGiaCaNhan);
    }
}
