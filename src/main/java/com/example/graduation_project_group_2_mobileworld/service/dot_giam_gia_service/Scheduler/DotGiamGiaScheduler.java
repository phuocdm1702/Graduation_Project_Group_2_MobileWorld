package com.example.graduation_project_group_2_mobileworld.service.dot_giam_gia_service.Scheduler;

import com.example.graduation_project_group_2_mobileworld.entity.ChiTietDotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import com.example.graduation_project_group_2_mobileworld.repository.chiTietDotGiamGia.ChiTietDotGiamGiaRepository;
import com.example.graduation_project_group_2_mobileworld.repository.dot_giam_gia_repo.dot_giam_gia_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Component
public class DotGiamGiaScheduler {
    @Autowired
    private ChiTietDotGiamGiaRepository chiTietDotGiamGiaRepository;

    @Autowired
    private dot_giam_gia_repository dotGiamGiaRepository;

    @Scheduled(cron = "0 0 0 * * *") // Chạy lúc 00:00
    @Transactional
    public void updateExpiredDotGiamGia() {
        LocalDate today = LocalDate.now();
        List<DotGiamGia> expiredDotGiamGias = dotGiamGiaRepository.findByNgayKetThucBeforeAndTrangThaiTrueAndDeletedFalse(today);

        for (DotGiamGia dotGiamGia : expiredDotGiamGias) {
            // Set deleted = true cho ChiTietDotGiamGia đã hết hạn
            List<ChiTietDotGiamGia> expiredRecords = chiTietDotGiamGiaRepository.findByIdDotGiamGiaAndDeleted(dotGiamGia, false);
            for (ChiTietDotGiamGia record : expiredRecords) {
                record.setDeleted(true);
                chiTietDotGiamGiaRepository.save(record);

                // Khôi phục các bản ghi khác nếu cần
                List<ChiTietDotGiamGia> otherRecords = chiTietDotGiamGiaRepository.findByIdChiTietSanPhamAndDeleted(record.getIdChiTietSanPham(), true);
                for (ChiTietDotGiamGia otherRecord : otherRecords) {
                    if (!otherRecord.getIdDotGiamGia().getId().equals(dotGiamGia.getId()) &&
                            !isOverlappingWithActiveDotGiamGia(otherRecord.getIdChiTietSanPham(), otherRecord.getIdDotGiamGia())) {
                        otherRecord.setDeleted(false);
                        chiTietDotGiamGiaRepository.save(otherRecord);
                        System.out.println("Khôi phục chi tiết giảm giá trong đợt " + otherRecord.getIdDotGiamGia().getId() + " cho idChiTietSanPham: " + record.getIdChiTietSanPham().getId());
                    }
                }
            }
            dotGiamGia.setTrangThai(false);
            dotGiamGiaRepository.save(dotGiamGia);
        }
    }

    private boolean isOverlappingWithActiveDotGiamGia(ChiTietSanPham chiTietSanPham, DotGiamGia excludeDotGiamGia) {
        List<ChiTietDotGiamGia> activeRecords = chiTietDotGiamGiaRepository.findByIdChiTietSanPhamAndDeleted(chiTietSanPham, false);
        for (ChiTietDotGiamGia record : activeRecords) {
            if (!record.getIdDotGiamGia().getId().equals(excludeDotGiamGia.getId()) &&
                    isOverlapping(
                            record.getIdDotGiamGia().getNgayBatDau(),
                            record.getIdDotGiamGia().getNgayKetThuc(),
                            excludeDotGiamGia.getNgayBatDau(),
                            excludeDotGiamGia.getNgayKetThuc())) {
                return true;
            }
        }
        return false;
    }

    private boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }
}
