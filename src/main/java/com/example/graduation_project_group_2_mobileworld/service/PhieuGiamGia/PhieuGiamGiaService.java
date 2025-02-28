package com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaCaNhanRepository;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhieuGiamGiaService {

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;


    public PhieuGiamGiaService(PhieuGiamGiaRepository phieuGiamGiaRepository) {
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
    }

    public List<PhieuGiamGia> getPGG() {
        return phieuGiamGiaRepository.findAll();
    }

    public PhieuGiamGia addPGG(PhieuGiamGia phieuGiamGia) {
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }

    public Boolean softDelete(Integer id) {
        Optional<PhieuGiamGia> pggDelete = phieuGiamGiaRepository.findById(id);
        if(pggDelete.isPresent()) {
            PhieuGiamGia pgg = pggDelete.get();
            pgg.setTrangThai(false);
            pgg.setDeleted(true);
            phieuGiamGiaRepository.save(pgg);
            return true;
        }
        return false;
    }

    public void updatePGG(Integer id, PhieuGiamGia editPGG) {
        PhieuGiamGia existingPGG = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phiếu giảm giá không tồn tại!"));

        existingPGG.setMa(editPGG.getMa());
        existingPGG.setTenPhieuGiamGia(editPGG.getTenPhieuGiamGia());
        existingPGG.setLoaiPhieuGiamGia(editPGG.getLoaiPhieuGiamGia());
        existingPGG.setPhanTramGiamGia(editPGG.getPhanTramGiamGia());
        existingPGG.setSoTienGiamToiDa(editPGG.getSoTienGiamToiDa());
        existingPGG.setHoaDonToiThieu(editPGG.getHoaDonToiThieu());
        existingPGG.setSoLuongDung(editPGG.getSoLuongDung());
        existingPGG.setNgayBatDau(editPGG.getNgayBatDau());
        existingPGG.setNgayKetThuc(editPGG.getNgayKetThuc());
        existingPGG.setTrangThai(editPGG.getTrangThai());
        existingPGG.setRiengTu(editPGG.getRiengTu());

        phieuGiamGiaRepository.save(existingPGG);
    }

}
