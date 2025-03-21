package com.example.graduation_project_group_2_mobileworld.service.dot_giam_gia_service;

import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.dot_giam_gia_DTO;
import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO;
import com.example.graduation_project_group_2_mobileworld.entity.ChiTietDotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham;
import com.example.graduation_project_group_2_mobileworld.repository.chiTietDotGiamGia.CTSPForCTDGG;
import com.example.graduation_project_group_2_mobileworld.repository.chiTietDotGiamGia.ChiTietDotGiamGiaRepository;
import com.example.graduation_project_group_2_mobileworld.repository.dot_giam_gia_repo.dot_giam_gia_repository;

import com.example.graduation_project_group_2_mobileworld.repository.dot_giam_gia_repo.repoDongSanPhamDGG;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class dot_giam_gia_service {

    private dot_giam_gia_repository repository;
    private ChiTietDotGiamGiaRepository repo2;
    private repoDongSanPhamDGG sanPhamRepository;
    private CTSPForCTDGG chiTietSanPhamRepository;

    public dot_giam_gia_service(dot_giam_gia_repository repository, ChiTietDotGiamGiaRepository repo2, repoDongSanPhamDGG sanPhamRepository, CTSPForCTDGG chiTietSanPhamRepository) {
        this.repository = repository;
        this.repo2 = repo2;
        this.sanPhamRepository = sanPhamRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
    }

    public Page<DotGiamGia> HienThi(Pageable pageable) {
        return repository.hienThi(pageable);
    }

    public List<DotGiamGia> forExcel() {
        return repository.ForExcel();
    }

    public List<ChiTietDotGiamGia> ForExcelCTDGG() {
        return repo2.xuatExcel();
    }

    public Page<DotGiamGia> hienThiFinish(Pageable pageable) {
        return repository.hienThiFinish(pageable);
    }

    public Page<SanPham> getDSP(String timKiem, Pageable pageable) {
        return repository.getAllSanPham(timKiem, pageable);
    }

    public Page<viewCTSPDTO> getAllCTSP(List<Integer> ids, List<Integer> idBoNhoTrongs, List<Integer> mauSac, Pageable pageable) {
        return repository.getAllCTSP(ids, idBoNhoTrongs, mauSac, pageable);
    }

    public Boolean existByMa(String ma) {
        return repository.existsByMaAndDeletedTrue(ma);
    }

    @Transactional
    public void addDotGiamGia(dot_giam_gia_DTO dotGiamGiaDTO, List<Integer> idSanPham, List<viewCTSPDTO> dsCTSP) {
        try {
            // Lưu DotGiamGia
            if (dotGiamGiaDTO.getNgayBatDau() == null || dotGiamGiaDTO.getNgayKetThuc() == null) {
                throw new IllegalArgumentException("Ngày bắt đầu hoặc ngày kết thúc không được null");
            }
            Date ngayBatDau = new Date(dotGiamGiaDTO.getNgayBatDau().getTime());
            Date ngayKetThuc = new Date(dotGiamGiaDTO.getNgayKetThuc().getTime());
            DotGiamGia dotGiamGia = new DotGiamGia();
            dotGiamGia.setMa(dotGiamGiaDTO.getMa());
            dotGiamGia.setTenDotGiamGia(dotGiamGiaDTO.getTenDotGiamGia());
            dotGiamGia.setLoaiGiamGiaApDung(dotGiamGiaDTO.getLoaiGiamGiaApDung());
            dotGiamGia.setGiaTriGiamGia(dotGiamGiaDTO.getGiaTriGiamGia());
            dotGiamGia.setSoTienGiamToiDa(dotGiamGiaDTO.getSoTienGiamToiDa());
            dotGiamGia.setNgayBatDau(ngayBatDau);
            dotGiamGia.setNgayKetThuc(ngayKetThuc);
            dotGiamGia.setDeleted(false);
            dotGiamGia.setTrangThai(ngayBatDau.after(new Date(System.currentTimeMillis())));
            repository.save(dotGiamGia);
            System.out.println("Lưu đợt giảm giá thành công!");

            List<SanPham> dsSanPham = sanPhamRepository.findAllById(idSanPham);
            System.out.println("dsSanPham: " + dsSanPham.size());

            // Map các CTSP được chọn từ dsCTSP
            Map<Integer, viewCTSPDTO> selectedCTSPMap = dsCTSP.stream()
                    .filter(ctsp -> ctsp.getSelected() != null && ctsp.getSelected())
                    .collect(Collectors.toMap(ctsp -> ctsp.getCtsp().getId(), ctsp -> ctsp, (e, r) -> e));
            System.out.println("selectedCTSPMap size: " + selectedCTSPMap.size());
            System.out.println("selectedCTSPMap keys: " + selectedCTSPMap.keySet());

            // Lấy tất cả ChiTietSanPham theo idSanPham
            List<ChiTietSanPham> dsChiTietSanPham = chiTietSanPhamRepository.findAllByIdSanPhamIn(idSanPham);
            System.out.println("dsChiTietSanPham size: " + dsChiTietSanPham.size());
            System.out.println("dsChiTietSanPham IDs: " + dsChiTietSanPham.stream().map(ChiTietSanPham::getId).collect(Collectors.toList()));

            String maxMaChiTiet = repo2.findMaxMa();
            int nextNumber = 1;
            if (maxMaChiTiet != null && maxMaChiTiet.startsWith("CTDGG")) {
                try {
                    nextNumber = Integer.parseInt(maxMaChiTiet.substring(5)) + 1;
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi khi parse mã CTDGG: " + e.getMessage());
                }
            }

            Set<String> addedCTSP = new HashSet<>();

            for (viewCTSPDTO ctspDTO : dsCTSP) {
                if (ctspDTO.getSelected() == null || !ctspDTO.getSelected()) continue;
                Integer idCTSP = ctspDTO.getCtsp().getId();
                ChiTietSanPham selectedChiTietSanPham = dsChiTietSanPham.stream()
                        .filter(ctsp -> ctsp.getId().equals(idCTSP))
                        .findFirst()
                        .orElse(null);
                if (selectedChiTietSanPham == null) continue;

                // Lấy tất cả ChiTietSanPham có cùng idSanPham, idMauSac, idBoNhoTrong
                List<ChiTietSanPham> matchingChiTietSanPhams = dsChiTietSanPham.stream()
                        .filter(ctsp -> ctsp.getIdSanPham().getId().equals(selectedChiTietSanPham.getIdSanPham().getId()) &&
                                ctsp.getIdMauSac().getId().equals(selectedChiTietSanPham.getIdMauSac().getId()) &&
                                ctsp.getIdBoNhoTrong().getId().equals(selectedChiTietSanPham.getIdBoNhoTrong().getId()) &&
                                !ctsp.getDeleted())
                        .collect(Collectors.toList());

                BigDecimal giaBanDau = selectedChiTietSanPham.getGiaBan();
                BigDecimal giaSauKhiGiam = ctspDTO.getGiaSauKhiGiam();

                for (ChiTietSanPham chiTietSanPham : matchingChiTietSanPhams) {
                    Integer idCTSPInGroup = chiTietSanPham.getId();
                    String key = idCTSPInGroup + "_" + giaBanDau;
                    if (addedCTSP.contains(key)) continue;

                    // Kiểm tra xem idChiTietSanPham đã tồn tại trong đợt giảm giá khác chưa
                    List<ChiTietDotGiamGia> existingInOtherDot = repo2.findByIdChiTietSanPhamAndDeleted(chiTietSanPham, false);
                    boolean shouldAdd = true;

                    if (!existingInOtherDot.isEmpty()) {
                        for (ChiTietDotGiamGia existing : existingInOtherDot) {
                            if (!existing.getIdDotGiamGia().getId().equals(dotGiamGia.getId())) {
                                if (existing.getIdDotGiamGia().getNgayKetThuc().equals(dotGiamGia.getNgayKetThuc())) {
                                    if (giaSauKhiGiam.compareTo(existing.getGiaSauKhiGiam()) < 0) {
                                        existing.setDeleted(true);
                                        repo2.save(existing);
                                        System.out.println("Hủy chi tiết giảm giá cũ trong đợt " + existing.getIdDotGiamGia().getId() +
                                                " vì giá mới thấp hơn cho idChiTietSanPham: " + idCTSPInGroup);
                                    } else {
                                        shouldAdd = false;
                                        System.out.println("Không thêm chi tiết giảm giá cho đợt " + dotGiamGia.getId() +
                                                " vì giá không thấp hơn đợt " + existing.getIdDotGiamGia().getId());
                                        continue;
                                    }
                                } else if (isOverlapping(
                                        existing.getIdDotGiamGia().getNgayBatDau(),
                                        existing.getIdDotGiamGia().getNgayKetThuc(),
                                        dotGiamGia.getNgayBatDau(),
                                        dotGiamGia.getNgayKetThuc())) {
                                    existing.setDeleted(true);
                                    repo2.save(existing);
                                    System.out.println("Hủy chi tiết giảm giá cũ trong đợt " + existing.getIdDotGiamGia().getId() +
                                            " do thời gian trùng lặp cho idChiTietSanPham: " + idCTSPInGroup);
                                }
                            }
                        }
                    }

                    if (!shouldAdd) continue;

                    // Thêm bản ghi mới
                    String newMaChiTiet = String.format("CTDGG%05d", nextNumber++);
                    ChiTietDotGiamGia chiTiet = new ChiTietDotGiamGia();
                    chiTiet.setMa(newMaChiTiet);
                    chiTiet.setIdDotGiamGia(dotGiamGia);
                    chiTiet.setIdChiTietSanPham(chiTietSanPham);
                    chiTiet.setGiaBanDau(giaBanDau);
                    chiTiet.setGiaSauKhiGiam(giaSauKhiGiam);
                    chiTiet.setDeleted(false);
                    repo2.save(chiTiet);
                    addedCTSP.add(key);
                    System.out.println("Thêm giảm giá cho sản phẩm: " + idCTSPInGroup);
                }
            }
            System.out.println("Thêm tất cả chi tiết giảm giá thành công!");
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm đợt giảm giá: " + e.getMessage());
            throw e;
        }
    }

    private boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }

    @Modifying
    @Transactional
    public void deleteDotGiamGiaById(Integer id) {
        try {
            repository.updateDotGiamGiaDeleted(id);
            System.out.println("Cập nhật trạng thái deleted cho DotGiamGia với ID: " + id);

            repo2.updateChiTietDotGiamGiaDeleted(id);

            List<ChiTietDotGiamGia> deletedRecords = repo2.findByIdDotGiamGiaIdAndDeleted(id, true);

            for (ChiTietDotGiamGia deletedRecord : deletedRecords) {
                ChiTietSanPham chiTietSanPham = deletedRecord.getIdChiTietSanPham();
                DotGiamGia deletedDotGiamGia = deletedRecord.getIdDotGiamGia();

                List<ChiTietDotGiamGia> otherRecords = repo2.findByIdChiTietSanPhamAndDeleted(chiTietSanPham, true);

                for (ChiTietDotGiamGia otherRecord : otherRecords) {
                    DotGiamGia otherDotGiamGia = otherRecord.getIdDotGiamGia();
                    if (!otherDotGiamGia.getId().equals(deletedDotGiamGia.getId()) && !otherDotGiamGia.getDeleted()) {
                        if (isOverlapping3(
                                otherDotGiamGia.getNgayBatDau(),
                                otherDotGiamGia.getNgayKetThuc(),
                                deletedDotGiamGia.getNgayBatDau(),
                                deletedDotGiamGia.getNgayKetThuc())) {
                            otherRecord.setDeleted(false);
                            repo2.save(otherRecord);
                            System.out.println("Khôi phục chi tiết giảm giá trong đợt " + otherDotGiamGia.getId() +
                                    " cho idChiTietSanPham: " + chiTietSanPham.getId());
                        }
                    }
                }
            }
            System.out.println("Xóa và khôi phục (nếu cần) thành công cho DotGiamGia với ID: " + id);
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa DotGiamGia: " + e.getMessage());
            throw e;
        }
    }

    // Hàm kiểm tra thời gian trùng lặp (sửa lại)
    private boolean isOverlapping3(Date start1, Date end1, Date start2, Date end2) {
        return !start1.after(end2) && !start2.after(end1);  // start1 <= end2 && start2 <= end1
    }


//    public void deleteChiTietDotGiamGiaById(Integer id) {
//        try {
//            repo2.updateChiTietDotGiamGiaDeleted(id);
//        } catch (Exception e) {
//            System.err.println("Lỗi khi cập nhật ChiTietDotGiamGia: " + e.getMessage());
//            throw e;
//        }
//    }

    public List<SanPham> getThatDongSanPham(Integer id) {
        return repository.getThatDongSanPham(id);
    }

    public List<ChiTietSanPham> getChiTietSanPhamByDotGiamGia(Integer id) {
        return repo2.getChiTietSanPhamByDotGiamGia(id);
    }

    public Map<String, Object> getDataForUpdate(Integer id) {
        Map<String, Object> response = new HashMap<>();
        response.put("dspList", getThatDongSanPham(id));
        response.put("ctspIds", getChiTietSanPhamByDotGiamGia(id).stream()
                .map(ChiTietSanPham::getId)
                .collect(Collectors.toList()));
        return response;
    }

    public Optional<DotGiamGia> findOne(Integer id) {
        return repository.findById(id);
    }


    @Transactional
    public void updateDotGiamGia(Integer dotGiamGiaId, dot_giam_gia_DTO dotGiamGiaDTO, List<Integer> idSanPham, List<viewCTSPDTO> dsCTSP) {
        try {
            DotGiamGia dotGiamGia = repository.findById(dotGiamGiaId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với id: " + dotGiamGiaId));
            BigDecimal maxDiscountAmountOld = dotGiamGia.getSoTienGiamToiDa();
            LocalDate today = LocalDate.now();

            // Cập nhật thông tin DotGiamGia
            LocalDateTime ngayBatDauFormatted = dotGiamGiaDTO.getNgayBatDau().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
            dotGiamGia.setNgayBatDau(new Date(Timestamp.valueOf(ngayBatDauFormatted).getTime()));
            LocalDateTime ngayKetThucFormatted = dotGiamGiaDTO.getNgayKetThuc().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
            dotGiamGia.setNgayKetThuc(new Date(Timestamp.valueOf(ngayKetThucFormatted).getTime()));
            dotGiamGia.setMa(dotGiamGiaDTO.getMa());
            dotGiamGia.setTenDotGiamGia(dotGiamGiaDTO.getTenDotGiamGia());
            dotGiamGia.setLoaiGiamGiaApDung(dotGiamGiaDTO.getLoaiGiamGiaApDung());
            dotGiamGia.setGiaTriGiamGia(dotGiamGiaDTO.getGiaTriGiamGia());
            dotGiamGia.setSoTienGiamToiDa(dotGiamGiaDTO.getSoTienGiamToiDa());
            dotGiamGia.setTrangThai(ngayBatDauFormatted.toLocalDate().isAfter(today));
            repository.save(dotGiamGia);

            List<SanPham> dsSanPham = sanPhamRepository.findAllById(idSanPham);
            Map<Integer, SanPham> sanPhamMap = dsSanPham.stream()
                    .collect(Collectors.toMap(SanPham::getId, sp -> sp));

            // Map các CTSP được chọn từ dsCTSP
            Map<Integer, viewCTSPDTO> selectedCTSPMap = dsCTSP.stream()
                    .filter(ctsp -> ctsp.getSelected() != null && ctsp.getSelected())
                    .collect(Collectors.toMap(ctsp -> ctsp.getCtsp().getId(), ctsp -> ctsp, (e, r) -> e));

            // Lấy tất cả ChiTietSanPham theo idSanPham
            List<ChiTietSanPham> dsChiTietSanPham = chiTietSanPhamRepository.findAllByIdSanPhamIn(idSanPham);

            String maxMaChiTiet = repo2.findMaxMa();
            int nextNumber = 1;
            if (maxMaChiTiet != null && maxMaChiTiet.startsWith("CTDGG")) {
                nextNumber = Integer.parseInt(maxMaChiTiet.substring(5)) + 1;
            }
            Set<String> addedCTSP = new HashSet<>();
            Set<Integer> selectedCTSPIds = selectedCTSPMap.keySet();

            // Xóa các bản ghi không còn được chọn trong idDotGiamGia hiện tại
            List<ChiTietDotGiamGia> existingChiTietList = repo2.findByIdDotGiamGia(dotGiamGia);
            for (ChiTietDotGiamGia chiTiet : existingChiTietList) {
                Integer idCTSP = chiTiet.getIdChiTietSanPham().getId();
                if (!selectedCTSPIds.contains(idCTSP)) {
                    chiTiet.setDeleted(true);
                    repo2.save(chiTiet);
                }
            }

            // Xử lý thêm hoặc cập nhật ChiTietDotGiamGia
            for (viewCTSPDTO ctspDTO : dsCTSP) {
                if (ctspDTO.getSelected() == null || !ctspDTO.getSelected()) continue;

                Integer idCTSP = ctspDTO.getCtsp().getId();
                ChiTietSanPham selectedChiTietSanPham = dsChiTietSanPham.stream()
                        .filter(ctsp -> ctsp.getId().equals(idCTSP))
                        .findFirst()
                        .orElse(null);
                if (selectedChiTietSanPham == null) continue;

                // Lấy tất cả ChiTietSanPham có cùng idSanPham, idMauSac, idBoNhoTrong
                List<ChiTietSanPham> matchingChiTietSanPhams = dsChiTietSanPham.stream()
                        .filter(ctsp -> ctsp.getIdSanPham().getId().equals(selectedChiTietSanPham.getIdSanPham().getId()) &&
                                ctsp.getIdMauSac().getId().equals(selectedChiTietSanPham.getIdMauSac().getId()) &&
                                ctsp.getIdBoNhoTrong().getId().equals(selectedChiTietSanPham.getIdBoNhoTrong().getId()) &&
                                !ctsp.getDeleted())
                        .collect(Collectors.toList());

                BigDecimal giaBanDau = selectedChiTietSanPham.getGiaBan();
                BigDecimal giaSauKhiGiam = ctspDTO.getGiaSauKhiGiam();

                for (ChiTietSanPham chiTietSanPham : matchingChiTietSanPhams) {
                    Integer idCTSPInGroup = chiTietSanPham.getId();
                    String key = idCTSPInGroup + "_" + giaBanDau;
                    if (addedCTSP.contains(key)) continue;

                    // Kiểm tra xem idChiTietSanPham đã tồn tại trong đợt giảm giá khác chưa
                    List<ChiTietDotGiamGia> existingInOtherDot = repo2.findByIdChiTietSanPhamAndDeleted(chiTietSanPham, false);
                    boolean shouldAdd = true;

                    if (!existingInOtherDot.isEmpty()) {
                        for (ChiTietDotGiamGia existing : existingInOtherDot) {
                            if (!existing.getIdDotGiamGia().getId().equals(dotGiamGia.getId())) {
                                if (existing.getIdDotGiamGia().getNgayKetThuc().equals(dotGiamGia.getNgayKetThuc())) {
                                    if (giaSauKhiGiam.compareTo(existing.getGiaSauKhiGiam()) < 0) {
                                        existing.setDeleted(true);
                                        repo2.save(existing);
                                        System.out.println("Hủy chi tiết giảm giá cũ trong đợt " + existing.getIdDotGiamGia().getId() +
                                                " vì giá mới thấp hơn cho idChiTietSanPham: " + idCTSPInGroup);
                                    } else {
                                        shouldAdd = false;
                                        System.out.println("Không thêm chi tiết giảm giá cho đợt " + dotGiamGia.getId() +
                                                " vì giá không thấp hơn đợt " + existing.getIdDotGiamGia().getId());
                                        continue;
                                    }
                                } else if (isOverlapping2(
                                        existing.getIdDotGiamGia().getNgayBatDau(),
                                        existing.getIdDotGiamGia().getNgayKetThuc(),
                                        dotGiamGia.getNgayBatDau(),
                                        dotGiamGia.getNgayKetThuc())) {
                                    existing.setDeleted(true);
                                    repo2.save(existing);
                                    System.out.println("Hủy chi tiết giảm giá cũ trong đợt " + existing.getIdDotGiamGia().getId() +
                                            " do thời gian trùng lặp cho idChiTietSanPham: " + idCTSPInGroup);
                                }
                            }
                        }
                    }

                    if (!shouldAdd) continue;

                    // Thêm hoặc cập nhật bản ghi trong idDotGiamGia hiện tại
                    List<ChiTietDotGiamGia> deletedRecords = repo2.findByDotGiamGiaAndIdChiTietSanPhamAndGiaBanDauAndDeleted(
                            dotGiamGia, chiTietSanPham, giaBanDau, true);
                    if (!deletedRecords.isEmpty()) {
                        for (ChiTietDotGiamGia deletedRecord : deletedRecords) {
                            deletedRecord.setDeleted(false);
                            deletedRecord.setGiaSauKhiGiam(giaSauKhiGiam);
                            repo2.save(deletedRecord);
                        }
                        addedCTSP.add(key);
                        System.out.println("Khôi phục chi tiết giảm giá bị xóa cho chi tiết sản phẩm: " + idCTSPInGroup);
                        continue;
                    }

                    List<ChiTietDotGiamGia> existingChiTietListForCTSP = repo2.findByDotGiamGiaAndIdChiTietSanPhamAndGiaBanDauAndDeleted(
                            dotGiamGia, chiTietSanPham, giaBanDau, false);
                    if (existingChiTietListForCTSP.isEmpty()) {
                        String newMaChiTiet = String.format("CTDGG%05d", nextNumber++);
                        ChiTietDotGiamGia chiTiet = new ChiTietDotGiamGia();
                        chiTiet.setMa(newMaChiTiet);
                        chiTiet.setIdDotGiamGia(dotGiamGia);
                        chiTiet.setIdChiTietSanPham(chiTietSanPham);
                        chiTiet.setGiaBanDau(giaBanDau);
                        chiTiet.setGiaSauKhiGiam(giaSauKhiGiam);
                        chiTiet.setDeleted(false);
                        repo2.save(chiTiet);
                        addedCTSP.add(key);
                        System.out.println("Thêm mới chi tiết giảm giá cho chi tiết sản phẩm: " + idCTSPInGroup);
                    } else {
                        boolean isDiscountLimitChanged = false;
                        for (ChiTietDotGiamGia existingChiTiet : existingChiTietListForCTSP) {
                            BigDecimal maxDiscountAmountNew = dotGiamGiaDTO.getSoTienGiamToiDa();
                            boolean isDiscountLimitUpdated = maxDiscountAmountNew.compareTo(maxDiscountAmountOld) != 0;
                            if (isDiscountLimitUpdated) {
                                BigDecimal expectedNewPrice = existingChiTiet.getGiaBanDau().subtract(maxDiscountAmountNew);
                                existingChiTiet.setGiaSauKhiGiam(expectedNewPrice);
                                isDiscountLimitChanged = true;
                            } else if (existingChiTiet.getGiaSauKhiGiam().compareTo(giaSauKhiGiam) != 0) {
                                existingChiTiet.setGiaSauKhiGiam(giaSauKhiGiam);
                                isDiscountLimitChanged = true;
                            }
                        }
                        if (isDiscountLimitChanged) {
                            repo2.saveAll(existingChiTietListForCTSP);
                            System.out.println("Cập nhật giá cho chi tiết sản phẩm tồn tại: " + idCTSPInGroup);
                        }
                    }
                }
            }
            System.out.println("Cập nhật đợt giảm giá thành công!");
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật đợt giảm giá: " + e.getMessage());
            throw e;
        }
    }

    private boolean isOverlapping2(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }

    public Page<DotGiamGia> timKiem(Pageable pageable, String maDGG, String tenDGG, String loaiGiamGiaApDung, BigDecimal giaTriGiamGia, BigDecimal soTienGiamToiDa, Date ngayBatDau, Date ngayKetThuc, Boolean trangThai, Boolean deleted) {
        return repository.timKiem(pageable, maDGG, tenDGG, loaiGiamGiaApDung, giaTriGiamGia, soTienGiamToiDa, ngayBatDau, ngayKetThuc, trangThai, deleted);
    }

    @PostConstruct
    public void initUpdate() {
        updateStatusAutomatically();
    }

    @Scheduled(cron = "0 0 0 * * *") // Chạy lúc 00:00
    @Transactional
    public void updateStatusAutomatically() {
        Date today = java.sql.Date.valueOf(LocalDate.now());
        repository.updateStatusIfStartDatePassed(today);
        repository.updateDeletedIfEndDatePassed(today);
        repository.updateDeletedChiTietDotGiamGia();
    }

}
