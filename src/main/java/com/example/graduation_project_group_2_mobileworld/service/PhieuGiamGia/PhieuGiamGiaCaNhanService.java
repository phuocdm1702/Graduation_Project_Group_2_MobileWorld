package com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaCaNhanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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


    @Transactional
    public PhieuGiamGiaCaNhan addPGGCN(PhieuGiamGiaCaNhan phieuGiamGiaCaNhan) {
        return phieuGiamGiaCaNhanRepository.save(phieuGiamGiaCaNhan);
    }

    @Transactional
    public void deleteByPhieuGiamGiaId(Integer phieuGiamGiaId) {
        phieuGiamGiaCaNhanRepository.deleteByIdPhieuGiamGia(phieuGiamGiaId);
    }

    public List<PhieuGiamGiaCaNhan> findByPhieuGiamGiaId(Integer pggId) {
        return phieuGiamGiaCaNhanRepository.findByIdPhieuGiamGia_Id(pggId);
    }

    public List<PhieuGiamGiaCaNhan> getall() {
        return phieuGiamGiaCaNhanRepository.findAll();
    }
}
