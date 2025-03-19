package com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<PhieuGiamGia> getPGG(Pageable pageable) {
        Page<PhieuGiamGia> listPgg = (Page<PhieuGiamGia>) phieuGiamGiaRepository.findAll(pageable);
        Date now = new Date();

        for (PhieuGiamGia pgg : listPgg) {
            if (pgg.getNgayBatDau() != null && pgg.getNgayKetThuc() != null) {
                // Đã bắt đầu (ngayBatDau <= now) và chưa hết hạn (ngayKetThuc >= now)
                if ((pgg.getNgayBatDau().before(now) || pgg.getNgayBatDau().equals(now)) &&
                        (pgg.getNgayKetThuc().after(now) || pgg.getNgayKetThuc().equals(now))) {
                    if (pgg.getTrangThai() == null || !pgg.getTrangThai()) {
                        pgg.setTrangThai(false);
                        phieuGiamGiaRepository.save(pgg);
                    }
                } else {
                    if (pgg.getTrangThai() == null || pgg.getTrangThai()) {
                        pgg.setTrangThai(true);
                        phieuGiamGiaRepository.save(pgg);
                    }
                }
            }
        }
        return listPgg;
    }

    @Scheduled(fixedRate = 200000)
    public void updateHanPGG() {
        List<PhieuGiamGia> listPgg = phieuGiamGiaRepository.findAll();
        Date now = new Date();

        for (PhieuGiamGia pgg : listPgg) {
            if (pgg.getNgayBatDau() != null && pgg.getNgayKetThuc() != null) {
                // Đã bắt đầu (ngayBatDau <= now) và chưa hết hạn (ngayKetThuc >= now)
                if ((pgg.getNgayBatDau().before(now) || pgg.getNgayBatDau().equals(now)) &&
                        (pgg.getNgayKetThuc().after(now) || pgg.getNgayKetThuc().equals(now))) {
                    if (pgg.getTrangThai() == null || !pgg.getTrangThai()) {
                        pgg.setTrangThai(false); // Cập nhật thành Hoạt động
                        phieuGiamGiaRepository.save(pgg);
                    }
                } else {
                    if (pgg.getTrangThai() == null || pgg.getTrangThai()) {
                        pgg.setTrangThai(true); // Cập nhật thành Không hoạt động
                        phieuGiamGiaRepository.save(pgg);
                    }
                }
            }
        }
    }

    public Page<PhieuGiamGia> searchData(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return (Page<PhieuGiamGia>) phieuGiamGiaRepository.findAll(); // Trả về tất cả nếu không có điều kiện lọc
        }
        return phieuGiamGiaRepository.search(keyword, pageable);
    }

    public Page<PhieuGiamGia> filterPhieuGiamGia(
            String loaiPhieuGiamGia, // Thêm tham số
            String trangThai,
            Date ngayBatDau,
            Date ngayKetThuc,
            Double minOrder,
            Double valueFilter,
            Pageable pageable) {

        String loaiPhieu = loaiPhieuGiamGia;


        if (loaiPhieu != null && ("Tất cả loại phiếu".equals(loaiPhieu) || loaiPhieu.isEmpty())) {
            loaiPhieu = null;
        }

        Boolean trangThaiBoolean = null;
        if (trangThai != null && !trangThai.isEmpty()) {
            if ("Hoạt động".equals(trangThai)) {
                trangThaiBoolean = false;
            } else if ("Không hoạt động".equals(trangThai)) {
                trangThaiBoolean = true;
            }
        }

        // Nếu không có filter nào được chọn, trả về tất cả
        if (loaiPhieu == null && trangThaiBoolean == null && ngayBatDau == null &&
                ngayKetThuc == null && minOrder == null && valueFilter == null) {
            return phieuGiamGiaRepository.findAll(pageable);
        }

        if (ngayBatDau != null && ngayKetThuc != null && ngayBatDau.after(ngayKetThuc)) {
            throw new IllegalArgumentException("Ngày bắt đầu không thể lớn hơn ngày kết thúc");
        }

        if (minOrder != null && minOrder < 0) {
            throw new IllegalArgumentException("Hóa đơn tối thiểu không thể nhỏ hơn 0");
        }

        if (valueFilter != null && valueFilter < 0) {
            throw new IllegalArgumentException("Giá trị phiếu không thể nhỏ hơn 0");
        }



        // Gọi query từ Repository
        return phieuGiamGiaRepository.filterPhieuGiamGia(
                loaiPhieu, // Thêm vào
                trangThaiBoolean,
                ngayBatDau,
                ngayKetThuc,
                minOrder,
                valueFilter,
                pageable
        );
    }

    public Optional<PhieuGiamGia> getById(Integer id) {
        return phieuGiamGiaRepository.findById(id);
    }

    public PhieuGiamGia addPGG(PhieuGiamGia phieuGiamGia) {
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }

    public Boolean updateTrangThai(Integer id, Boolean trangThai) {
        Optional<PhieuGiamGia> pggDelete = phieuGiamGiaRepository.findById(id);
        if(pggDelete.isPresent()) {
            PhieuGiamGia pgg = pggDelete.get();
            pgg.setTrangThai(trangThai);
            phieuGiamGiaRepository.save(pgg);
            return true;
        }
        return false;
    }

    public void updatePGG(PhieuGiamGia editPGG) {
        phieuGiamGiaRepository.save(editPGG);
    }

}
