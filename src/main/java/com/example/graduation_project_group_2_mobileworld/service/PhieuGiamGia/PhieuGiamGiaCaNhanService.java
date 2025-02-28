package com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaCaNhanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhieuGiamGiaCaNhanService {

    @Autowired
    private PhieuGiamGiaCaNhanRepository phieuGiamGiaCaNhanRepository;

    public PhieuGiamGiaCaNhanService(PhieuGiamGiaCaNhanRepository phieuGiamGiaCaNhanRepository) {
        this.phieuGiamGiaCaNhanRepository = phieuGiamGiaCaNhanRepository;
    }

    public List<PhieuGiamGiaCaNhan> getPGGCN() {
        return phieuGiamGiaCaNhanRepository.findAll();
    }

    public PhieuGiamGiaCaNhan addPGGCN(PhieuGiamGiaCaNhan phieuGiamGiaCaNhan) {
        return phieuGiamGiaCaNhanRepository.save(phieuGiamGiaCaNhan);
    }

}
