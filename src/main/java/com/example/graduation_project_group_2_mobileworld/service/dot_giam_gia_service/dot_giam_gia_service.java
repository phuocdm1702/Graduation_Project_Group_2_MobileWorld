package com.example.graduation_project_group_2_mobileworld.service.dot_giam_gia_service;

import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.dot_giam_gia_DTO;
import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO;
import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
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
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


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

    public Page<viewSanPhamDTO> getDSP(String timKiem, List<Integer> idHeDieuHanh, List<Integer> idNhaSanXuat, Pageable pageable) {
        return repository.getAllSanPham(timKiem, idHeDieuHanh, idNhaSanXuat, pageable);
    }

    public List<HeDieuHanh> getAllHeDieuHanh() {
        return repository.findAllHeDieuHanh();
    }

    public List<NhaSanXuat> getAllNhaSanXuat() {
        return repository.findAllNhaSanXuat();
    }

    public Page<viewCTSPDTO> getAllCTSP(List<Integer> ids, List<Integer> idBoNhoTrongs, List<Integer> mauSac, Pageable pageable) {
        return repository.getAllCTSP(ids, idBoNhoTrongs, mauSac, pageable);
    }

    public Boolean existByMa(String ma) {
        return repository.existsByMaAndDeletedTrue(ma);
    }


    private BigDecimal calculateGiaSauKhiGiam(BigDecimal giaBanDau, BigDecimal giaTriGiamGia, BigDecimal soTienGiamToiDa) {
        BigDecimal newGiaSauKhiGiam;
        if (giaTriGiamGia.compareTo(BigDecimal.ZERO) == 0) {
            newGiaSauKhiGiam = giaBanDau.subtract(soTienGiamToiDa);
        } else {
            BigDecimal giamTheoPhanTram = giaBanDau.multiply(giaTriGiamGia)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            if (giamTheoPhanTram.compareTo(soTienGiamToiDa) > 0) {
                newGiaSauKhiGiam = giaBanDau.subtract(soTienGiamToiDa);
            } else {
                newGiaSauKhiGiam = giaBanDau.subtract(giamTheoPhanTram);
            }
        }
        return newGiaSauKhiGiam.max(BigDecimal.ZERO);
    }


    @Transactional
    public void addDotGiamGia(dot_giam_gia_DTO dotGiamGiaDTO, List<Integer> idSanPham, List<viewCTSPDTO> dsCTSP) {
        try {
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
            dotGiamGia.setTrangThai(ngayBatDau.after(Date.valueOf(LocalDate.now())));
            repository.save(dotGiamGia);

            List<SanPham> dsSanPham = sanPhamRepository.findAllById(idSanPham);
            Map<Integer, viewCTSPDTO> selectedCTSPMap = dsCTSP.stream()
                    .filter(ctsp -> ctsp.getSelected() != null && ctsp.getSelected())
                    .collect(Collectors.toMap(ctsp -> ctsp.getCtsp().getId(), ctsp -> ctsp, (e, r) -> e));
            List<ChiTietSanPham> dsChiTietSanPham = chiTietSanPhamRepository.findAllByIdSanPhamIn(idSanPham);

            String maxMaChiTiet = repo2.findMaxMa();
            int nextNumber = 1;
            if (maxMaChiTiet != null && maxMaChiTiet.startsWith("CTDGG")) {
                nextNumber = Integer.parseInt(maxMaChiTiet.substring(5)) + 1;
            }

            Set<String> addedCTSP = new HashSet<>();
            Date today = Date.valueOf(LocalDate.now());

            for (viewCTSPDTO ctspDTO : dsCTSP) {
                if (ctspDTO.getSelected() == null || !ctspDTO.getSelected()) continue;
                Integer idCTSP = ctspDTO.getCtsp().getId();
                ChiTietSanPham selectedChiTietSanPham = dsChiTietSanPham.stream()
                        .filter(ctsp -> ctsp.getId().equals(idCTSP))
                        .findFirst().orElse(null);
                if (selectedChiTietSanPham == null) continue;

                List<ChiTietSanPham> matchingChiTietSanPhams = dsChiTietSanPham.stream()
                        .filter(ctsp -> ctsp.getIdSanPham().getId().equals(selectedChiTietSanPham.getIdSanPham().getId()) &&
                                ctsp.getIdMauSac().getId().equals(selectedChiTietSanPham.getIdMauSac().getId()) &&
                                ctsp.getIdBoNhoTrong().getId().equals(selectedChiTietSanPham.getIdBoNhoTrong().getId()) &&
                                !ctsp.getDeleted())
                        .collect(Collectors.toList());

                BigDecimal giaBanDau = selectedChiTietSanPham.getGiaBan();
                BigDecimal giaSauKhiGiamMoi = calculateGiaSauKhiGiam(giaBanDau, dotGiamGia.getGiaTriGiamGia(), dotGiamGia.getSoTienGiamToiDa());

                for (ChiTietSanPham chiTietSanPham : matchingChiTietSanPhams) {
                    Integer idCTSPInGroup = chiTietSanPham.getId();
                    String key = idCTSPInGroup + "_" + giaBanDau;
                    if (addedCTSP.contains(key)) continue;

                    List<ChiTietDotGiamGia> activeCtggList = repo2.findActiveChiTietDotGiamGiaByCtspId(idCTSPInGroup, today);
                    BigDecimal finalGiaSauKhiGiam = giaSauKhiGiamMoi;

                    if (!activeCtggList.isEmpty()) {
                        boolean isOverlappingAndActive = false;
                        for (ChiTietDotGiamGia existingCtgg : activeCtggList) {
                            DotGiamGia existingDot = existingCtgg.getIdDotGiamGia();
                            if (isOverlapping(dotGiamGia.getNgayBatDau(), dotGiamGia.getNgayKetThuc(),
                                    existingDot.getNgayBatDau(), existingDot.getNgayKetThuc()) &&
                                    today.compareTo(dotGiamGia.getNgayBatDau()) >= 0 &&
                                    today.compareTo(existingDot.getNgayBatDau()) >= 0) {
                                isOverlappingAndActive = true;
                                break;
                            }
                        }

                        if (isOverlappingAndActive) {
                            List<DotGiamGia> overlappingDots = new ArrayList<>();
                            overlappingDots.add(dotGiamGia);
                            for (ChiTietDotGiamGia ctgg : activeCtggList) {
                                overlappingDots.add(ctgg.getIdDotGiamGia());
                            }
                            BigDecimal avgGiaTriGiamGia = overlappingDots.stream()
                                    .map(DotGiamGia::getGiaTriGiamGia)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                                    .divide(BigDecimal.valueOf(overlappingDots.size()), 2, RoundingMode.HALF_UP);
                            BigDecimal avgSoTienGiamToiDa = overlappingDots.stream()
                                    .map(DotGiamGia::getSoTienGiamToiDa)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                                    .divide(BigDecimal.valueOf(overlappingDots.size()), 2, RoundingMode.HALF_UP);
                            finalGiaSauKhiGiam = calculateGiaSauKhiGiam(giaBanDau, avgGiaTriGiamGia, avgSoTienGiamToiDa);

                            for (ChiTietDotGiamGia ctgg : activeCtggList) {
                                ctgg.setGiaSauKhiGiam(finalGiaSauKhiGiam);
                                repo2.save(ctgg);
                            }
                        }
                    }

                    String newMaChiTiet = String.format("CTDGG%05d", nextNumber++);
                    ChiTietDotGiamGia chiTiet = new ChiTietDotGiamGia();
                    chiTiet.setMa(newMaChiTiet);
                    chiTiet.setIdDotGiamGia(dotGiamGia);
                    chiTiet.setIdChiTietSanPham(chiTietSanPham);
                    chiTiet.setGiaBanDau(giaBanDau);
                    chiTiet.setGiaSauKhiGiam(finalGiaSauKhiGiam);
                    chiTiet.setDeleted(false);
                    repo2.save(chiTiet);
                    addedCTSP.add(key);
                }
            }
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
            // 1. Đánh dấu DotGiamGia là deleted
            repository.updateDotGiamGiaDeleted(id);
            System.out.println("Cập nhật trạng thái deleted cho DotGiamGia với ID: " + id);

            // 2. Đánh dấu tất cả ChiTietDotGiamGia liên quan là deleted
            repo2.updateChiTietDotGiamGiaDeleted(id);

            // 3. Lấy các ChiTietDotGiamGia vừa bị xóa
            List<ChiTietDotGiamGia> deletedRecords = repo2.findByIdDotGiamGiaIdAndDeleted(id, true);

            // 4. Xử lý từng ChiTietSanPham bị ảnh hưởng
            for (ChiTietDotGiamGia deletedRecord : deletedRecords) {
                ChiTietSanPham chiTietSanPham = deletedRecord.getIdChiTietSanPham();
                DotGiamGia deletedDotGiamGia = deletedRecord.getIdDotGiamGia();
                BigDecimal giaBanDau = deletedRecord.getGiaBanDau();

                // 5. Tìm các ChiTietDotGiamGia còn hiệu lực (chưa bị xóa)
                List<ChiTietDotGiamGia> activeRecords = repo2.findByIdChiTietSanPhamAndDeleted(chiTietSanPham, false);

                if (!activeRecords.isEmpty()) {
                    // 6. Lấy DotGiamGia còn hiệu lực đầu tiên (hoặc logic khác nếu có nhiều)
                    ChiTietDotGiamGia activeRecord = activeRecords.get(0);
                    DotGiamGia activeDotGiamGia = activeRecord.getIdDotGiamGia();

                    // 7. Kiểm tra xem có trùng thời gian không
                    if (isOverlapping3(
                            activeDotGiamGia.getNgayBatDau(),
                            activeDotGiamGia.getNgayKetThuc(),
                            deletedDotGiamGia.getNgayBatDau(),
                            deletedDotGiamGia.getNgayKetThuc())) {
                        // 8. Tính lại giaSauKhiGiam theo DotGiamGia còn lại
                        BigDecimal newGiaSauKhiGiam;
                        if ("Phần trăm".equals(activeDotGiamGia.getLoaiGiamGiaApDung())) {
                            BigDecimal giamTheoPhanTram = giaBanDau.multiply(activeDotGiamGia.getGiaTriGiamGia())
                                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                            newGiaSauKhiGiam = giaBanDau.subtract(giamTheoPhanTram.min(activeDotGiamGia.getSoTienGiamToiDa()));
                        } else { // Tiền mặt
                            newGiaSauKhiGiam = giaBanDau.subtract(activeDotGiamGia.getSoTienGiamToiDa());
                        }
                        newGiaSauKhiGiam = newGiaSauKhiGiam.max(BigDecimal.ZERO);

                        // 9. Cập nhật bản ghi còn hiệu lực
                        activeRecord.setGiaSauKhiGiam(newGiaSauKhiGiam);
                        repo2.save(activeRecord);
                        System.out.println("Cập nhật giá sau khi giảm cho idChiTietSanPham: " + chiTietSanPham.getId() +
                                " theo DotGiamGia còn lại: " + activeDotGiamGia.getId());
                    }
                } else {
                    System.out.println("Không còn DotGiamGia hiệu lực cho idChiTietSanPham: " + chiTietSanPham.getId());
                }
            }
            System.out.println("Xóa và cập nhật (nếu cần) thành công cho DotGiamGia với ID: " + id);
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa DotGiamGia: " + e.getMessage());
            throw e;
        }
    }

    // Hàm kiểm tra thời gian trùng lặp
    private boolean isOverlapping3(Date start1, Date end1, Date start2, Date end2) {
        return !start1.after(end2) && !start2.after(end1);  // start1 <= end2 && start2 <= end1
    }

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
            Date today = Date.valueOf(LocalDate.now());

            LocalDateTime ngayBatDauFormatted = dotGiamGiaDTO.getNgayBatDau().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
            Date ngayBatDau = new Date(Timestamp.valueOf(ngayBatDauFormatted).getTime());
            LocalDateTime ngayKetThucFormatted = dotGiamGiaDTO.getNgayKetThuc().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
            Date ngayKetThuc = new Date(Timestamp.valueOf(ngayKetThucFormatted).getTime());

            dotGiamGia.setNgayBatDau(ngayBatDau);
            dotGiamGia.setNgayKetThuc(ngayKetThuc);
            dotGiamGia.setMa(dotGiamGiaDTO.getMa());
            dotGiamGia.setTenDotGiamGia(dotGiamGiaDTO.getTenDotGiamGia());
            dotGiamGia.setLoaiGiamGiaApDung(dotGiamGiaDTO.getLoaiGiamGiaApDung());
            dotGiamGia.setGiaTriGiamGia(dotGiamGiaDTO.getGiaTriGiamGia());
            dotGiamGia.setSoTienGiamToiDa(dotGiamGiaDTO.getSoTienGiamToiDa());
            dotGiamGia.setTrangThai(ngayBatDau.after(today));
            repository.save(dotGiamGia);

            List<SanPham> dsSanPham = sanPhamRepository.findAllById(idSanPham);
            Map<Integer, viewCTSPDTO> selectedCTSPMap = dsCTSP.stream()
                    .filter(ctsp -> ctsp.getSelected() != null && ctsp.getSelected())
                    .collect(Collectors.toMap(ctsp -> ctsp.getCtsp().getId(), ctsp -> ctsp, (e, r) -> e));
            List<ChiTietSanPham> dsChiTietSanPham = chiTietSanPhamRepository.findAllByIdSanPhamIn(idSanPham);

            String maxMaChiTiet = repo2.findMaxMa();
            int nextNumber = 1;
            if (maxMaChiTiet != null && maxMaChiTiet.startsWith("CTDGG")) {
                nextNumber = Integer.parseInt(maxMaChiTiet.substring(5)) + 1;
            }
            Set<String> addedCTSP = new HashSet<>();
            Set<Integer> selectedCTSPIds = selectedCTSPMap.keySet();

            List<ChiTietDotGiamGia> existingChiTietList = repo2.findByIdDotGiamGia(dotGiamGia);
            for (ChiTietDotGiamGia chiTiet : existingChiTietList) {
                Integer idCTSP = chiTiet.getIdChiTietSanPham().getId();
                if (!selectedCTSPIds.contains(idCTSP)) {
                    chiTiet.setDeleted(true);
                    repo2.save(chiTiet);
                }
            }

            for (viewCTSPDTO ctspDTO : dsCTSP) {
                if (ctspDTO.getSelected() == null || !ctspDTO.getSelected()) continue;

                Integer idCTSP = ctspDTO.getCtsp().getId();
                ChiTietSanPham selectedChiTietSanPham = dsChiTietSanPham.stream()
                        .filter(ctsp -> ctsp.getId().equals(idCTSP))
                        .findFirst()
                        .orElse(null);
                if (selectedChiTietSanPham == null) continue;

                List<ChiTietSanPham> matchingChiTietSanPhams = dsChiTietSanPham.stream()
                        .filter(ctsp -> ctsp.getIdSanPham().getId().equals(selectedChiTietSanPham.getIdSanPham().getId()) &&
                                ctsp.getIdMauSac().getId().equals(selectedChiTietSanPham.getIdMauSac().getId()) &&
                                ctsp.getIdBoNhoTrong().getId().equals(selectedChiTietSanPham.getIdBoNhoTrong().getId()) &&
                                !ctsp.getDeleted())
                        .collect(Collectors.toList());

                BigDecimal giaBanDau = selectedChiTietSanPham.getGiaBan();
                BigDecimal giaSauKhiGiamMoi = calculateGiaSauKhiGiam(giaBanDau, dotGiamGia.getGiaTriGiamGia(), dotGiamGia.getSoTienGiamToiDa());

                for (ChiTietSanPham chiTietSanPham : matchingChiTietSanPhams) {
                    Integer idCTSPInGroup = chiTietSanPham.getId();
                    String key = idCTSPInGroup + "_" + giaBanDau;
                    if (addedCTSP.contains(key)) continue;

                    List<ChiTietDotGiamGia> activeCtggList = repo2.findActiveChiTietDotGiamGiaByCtspId(idCTSPInGroup, today);
                    BigDecimal finalGiaSauKhiGiam = giaSauKhiGiamMoi;

                    if (!activeCtggList.isEmpty()) {
                        boolean isOverlappingAndActive = false;
                        List<DotGiamGia> overlappingDots = new ArrayList<>();
                        overlappingDots.add(dotGiamGia);

                        for (ChiTietDotGiamGia existingCtgg : activeCtggList) {
                            DotGiamGia existingDot = existingCtgg.getIdDotGiamGia();
                            if (!existingDot.getId().equals(dotGiamGia.getId()) &&
                                    isOverlapping(dotGiamGia.getNgayBatDau(), dotGiamGia.getNgayKetThuc(),
                                            existingDot.getNgayBatDau(), existingDot.getNgayKetThuc()) &&
                                    today.compareTo(dotGiamGia.getNgayBatDau()) >= 0 &&
                                    today.compareTo(existingDot.getNgayBatDau()) >= 0) {
                                isOverlappingAndActive = true;
                                overlappingDots.add(existingDot);
                            }
                        }

                        if (isOverlappingAndActive) {
                            BigDecimal avgGiaTriGiamGia = overlappingDots.stream()
                                    .map(DotGiamGia::getGiaTriGiamGia)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                                    .divide(BigDecimal.valueOf(overlappingDots.size()), 2, RoundingMode.HALF_UP);
                            BigDecimal avgSoTienGiamToiDa = overlappingDots.stream()
                                    .map(DotGiamGia::getSoTienGiamToiDa)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                                    .divide(BigDecimal.valueOf(overlappingDots.size()), 2, RoundingMode.HALF_UP);
                            finalGiaSauKhiGiam = calculateGiaSauKhiGiam(giaBanDau, avgGiaTriGiamGia, avgSoTienGiamToiDa);

                            for (ChiTietDotGiamGia ctgg : activeCtggList) {
                                if (!ctgg.getIdDotGiamGia().getId().equals(dotGiamGia.getId())) {
                                    ctgg.setGiaSauKhiGiam(finalGiaSauKhiGiam);
                                    repo2.save(ctgg);
                                }
                            }
                        }
                    }

                    List<ChiTietDotGiamGia> existingInOtherDot = repo2.findByIdChiTietSanPhamAndDeleted(chiTietSanPham, false);
                    boolean shouldAdd = true;

                    if (!existingInOtherDot.isEmpty()) {
                        for (ChiTietDotGiamGia existing : existingInOtherDot) {
                            if (!existing.getIdDotGiamGia().getId().equals(dotGiamGia.getId())) {
                                if (isOverlapping(dotGiamGia.getNgayBatDau(), dotGiamGia.getNgayKetThuc(),
                                        existing.getIdDotGiamGia().getNgayBatDau(), existing.getIdDotGiamGia().getNgayKetThuc())) {
                                    if (finalGiaSauKhiGiam.compareTo(existing.getGiaSauKhiGiam()) < 0) {
                                        existing.setDeleted(true);
                                        repo2.save(existing);
                                    } else {
                                        shouldAdd = false;
                                        continue;
                                    }
                                }
                            }
                        }
                    }

                    if (!shouldAdd) continue;

                    List<ChiTietDotGiamGia> deletedRecords = repo2.findByDotGiamGiaAndIdChiTietSanPhamAndGiaBanDauAndDeleted(
                            dotGiamGia, chiTietSanPham, giaBanDau, true);
                    if (!deletedRecords.isEmpty()) {
                        for (ChiTietDotGiamGia deletedRecord : deletedRecords) {
                            deletedRecord.setDeleted(false);
                            deletedRecord.setGiaSauKhiGiam(finalGiaSauKhiGiam);
                            repo2.save(deletedRecord);
                        }
                        addedCTSP.add(key);
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
                        chiTiet.setGiaSauKhiGiam(finalGiaSauKhiGiam);
                        chiTiet.setDeleted(false);
                        repo2.save(chiTiet);
                        addedCTSP.add(key);
                    } else {
                        for (ChiTietDotGiamGia existingChiTiet : existingChiTietListForCTSP) {
                            existingChiTiet.setGiaSauKhiGiam(finalGiaSauKhiGiam);
                            repo2.save(existingChiTiet);
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


    public Page<DotGiamGia> timKiem(Pageable pageable, String maDGG, String tenDGG, String loaiGiamGiaApDung, BigDecimal giaTriGiamGia, BigDecimal soTienGiamToiDa, Date ngayBatDau, Date ngayKetThuc, Boolean trangThai, Boolean deleted) {
        return repository.timKiem(pageable, maDGG, tenDGG, loaiGiamGiaApDung, giaTriGiamGia, soTienGiamToiDa, ngayBatDau, ngayKetThuc, trangThai, deleted);
    }

    @PostConstruct
    public void initUpdate() {
        updateStatusAutomatically();
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateStatusAutomatically() {
        Date today = java.sql.Date.valueOf(LocalDate.now());

        // Cập nhật trạng thái và xóa các DotGiamGia hết hạn
        int deletedCount = repository.updateDeletedIfEndDatePassed(today);
        if (deletedCount > 0) {
            System.out.println("Đã đánh dấu " + deletedCount + " DotGiamGia là deleted.");
            int deletedChiTietCount = repo2.updateDeletedChiTietDotGiamGia();
            System.out.println("Đã đánh dấu " + deletedChiTietCount + " ChiTietDotGiamGia là deleted.");
        }
        repository.updateStatusIfStartDatePassed(today);

        // Cập nhật giá cho tất cả ChiTietSanPham
        List<ChiTietSanPham> allCtsp = chiTietSanPhamRepository.findAll();
        for (ChiTietSanPham ctsp : allCtsp) {
            List<ChiTietDotGiamGia> activeCtggList = repo2.findActiveChiTietDotGiamGiaByCtspId(ctsp.getId(), today);

            if (activeCtggList.isEmpty()) {
                continue;
            }

            BigDecimal giaBanDau = activeCtggList.get(0).getGiaBanDau();
            BigDecimal newGiaSauKhiGiam;

            if (activeCtggList.size() >= 2) { // Có trùng lặp
                BigDecimal avgGiaTriGiamGia = activeCtggList.stream()
                        .map(ctgg -> ctgg.getIdDotGiamGia().getGiaTriGiamGia())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(activeCtggList.size()), 2, RoundingMode.HALF_UP);
                BigDecimal avgSoTienGiamToiDa = activeCtggList.stream()
                        .map(ctgg -> ctgg.getIdDotGiamGia().getSoTienGiamToiDa())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(activeCtggList.size()), 2, RoundingMode.HALF_UP);

                newGiaSauKhiGiam = calculateGiaSauKhiGiam(giaBanDau, avgGiaTriGiamGia, avgSoTienGiamToiDa);
                System.out.println("Cập nhật giá trung bình cho ChiTietSanPham " + ctsp.getId() + ": " + newGiaSauKhiGiam);
            } else { // Chỉ có 1 đợt
                ChiTietDotGiamGia activeCtgg = activeCtggList.get(0);
                DotGiamGia activeDot = activeCtgg.getIdDotGiamGia();
                newGiaSauKhiGiam = calculateGiaSauKhiGiam(giaBanDau, activeDot.getGiaTriGiamGia(), activeDot.getSoTienGiamToiDa());
                System.out.println("Cập nhật giá cho ChiTietSanPham " + ctsp.getId() + " theo DotGiamGia " + activeDot.getId() + ": " + newGiaSauKhiGiam);
            }

            for (ChiTietDotGiamGia ctgg : activeCtggList) {
                ctgg.setGiaSauKhiGiam(newGiaSauKhiGiam);
                repo2.save(ctgg);
            }
        }
    }


}
