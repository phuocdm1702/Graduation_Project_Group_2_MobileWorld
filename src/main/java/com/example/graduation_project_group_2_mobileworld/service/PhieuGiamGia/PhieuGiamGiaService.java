package com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaCaNhanRepository;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        List<PhieuGiamGia> listPgg = phieuGiamGiaRepository.findAll();
        Date now = new Date();

        for (PhieuGiamGia pgg : listPgg) {
            if(pgg.getNgayKetThuc() != null && pgg.getNgayKetThuc().before(now)) {
                pgg.setTrangThai(false);
                phieuGiamGiaRepository.save(pgg);
            }
        }
        return listPgg;
    }

    @Scheduled(fixedRate = 200000)
    public void updateHanPGG() {
        List<PhieuGiamGia> listPgg = phieuGiamGiaRepository.findAll();
        Date now = new Date();

        for (PhieuGiamGia pgg : listPgg) {
            if(pgg.getNgayKetThuc() != null && pgg.getNgayKetThuc().before(now)) {
                pgg.setTrangThai(false);
                phieuGiamGiaRepository.save(pgg);
            }
        }

    }

    public List<PhieuGiamGia> searchData(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return phieuGiamGiaRepository.findAll(); // Trả về tất cả nếu không có điều kiện lọc
        }
        return phieuGiamGiaRepository.search(keyword);
    }

    public List<PhieuGiamGia> filterTrangThaiLoaiPhieu(String loaiPhieu, Boolean trangThai) {
        if ((loaiPhieu == null || "Tất cả loại phiếu".equals(loaiPhieu)) &&
                (trangThai == null)) {
            return phieuGiamGiaRepository.findAll();
        }

        // Nếu "Tất cả loại phiếu", đặt về null để không lọc theo loại phiếu
        if ("Tất cả loại phiếu".equals(loaiPhieu)) {
            loaiPhieu = null;
        }

        // Nếu trangThai không phải true/false, đặt về null
        if (trangThai != null && !trangThai.equals(true) && !trangThai.equals(false)) {
            trangThai = null;
        }

        return phieuGiamGiaRepository.filterByLoaiPhieuAndTrangThai(loaiPhieu, trangThai);
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
