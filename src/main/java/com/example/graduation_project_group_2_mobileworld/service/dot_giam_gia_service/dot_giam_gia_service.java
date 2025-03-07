package com.example.graduation_project_group_2_mobileworld.service.dot_giam_gia_service;

import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.dot_giam_gia_DTO;
import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO;
import com.example.graduation_project_group_2_mobileworld.entity.ChiTietDotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import com.example.graduation_project_group_2_mobileworld.repository.chiTietDotGiamGia.ChiTietDotGiamGiaRepository;
import com.example.graduation_project_group_2_mobileworld.repository.dot_giam_gia_repo.dot_giam_gia_repository;

import com.example.graduation_project_group_2_mobileworld.repository.dot_giam_gia_repo.repoDongSanPhamDGG;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class dot_giam_gia_service{

    private dot_giam_gia_repository repository;
    private ChiTietDotGiamGiaRepository repo2;
    private repoDongSanPhamDGG dongSanPhamRepository;

    @Autowired
    public dot_giam_gia_service(dot_giam_gia_repository repository, ChiTietDotGiamGiaRepository repo2, repoDongSanPhamDGG dongSanPhamRepository) {
        this.repository = repository;
        this.repo2 = repo2;
        this.dongSanPhamRepository = dongSanPhamRepository;
    }


    public Page<DotGiamGia> HienThi(Pageable pageable){
        return repository.hienThi(pageable);
    }

    public Page<DotGiamGia> hienThiFinish(Pageable pageable){
        return repository.hienThiFinish(pageable);
    }

    public List<DongSanPham> getDSP(String timKiem){
        return repository.getAllDongSanPham(timKiem);
    }

    public List<viewCTSPDTO> getAllCTSP(List<Integer> ids,List<Integer> idBoNhoTrongs) {
        return repository.getAllCTSP(ids,idBoNhoTrongs);
    }

    public Boolean existByMa(String ma){
        return  repository.existsByMaAndDeletedTrue(ma);
    }

    @Transactional
    public void addDotGiamGia(dot_giam_gia_DTO dotGiamGiaDTO, List<Integer> idDongSanPham, List<viewCTSPDTO> dsCTSP) {
        try {
            LocalDate today = LocalDate.now();
            LocalDate ngayBatDau = dotGiamGiaDTO.getNgayBatDau().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            // Tạo và lưu đợt giảm giá mới
            DotGiamGia dotGiamGia = new DotGiamGia();
            dotGiamGia.setMa(dotGiamGiaDTO.getMa());
            dotGiamGia.setTenDotGiamGia(dotGiamGiaDTO.getTenDotGiamGia());
            dotGiamGia.setLoaiGiamGiaApDung(dotGiamGiaDTO.getLoaiGiamGiaApDung());
            dotGiamGia.setGiaTriGiamGia(dotGiamGiaDTO.getGiaTriGiamGia());
            dotGiamGia.setSoTienGiamToiDa(dotGiamGiaDTO.getSoTienGiamToiDa());
            dotGiamGia.setNgayBatDau(Timestamp.valueOf(ngayBatDau.atStartOfDay()));
            dotGiamGia.setNgayKetThuc(Timestamp.valueOf(dotGiamGiaDTO.getNgayKetThuc().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()));
            dotGiamGia.setDeleted(false);
            dotGiamGia.setTrangThai(ngayBatDau.isAfter(today));

            repository.save(dotGiamGia);
            System.out.println("Lưu đợt giảm giá thành công!");

            // Lấy danh sách sản phẩm từ DB
            List<DongSanPham> dsSanPham = dongSanPhamRepository.findAllById(idDongSanPham);
            Map<Integer, DongSanPham> sanPhamMap = dsSanPham.stream()
                    .collect(Collectors.toMap(DongSanPham::getId, dsp -> dsp));

            // Lấy mã lớn nhất hiện có trong database
            String maxMaChiTiet = repo2.findMaxMa();
            int nextNumber = 1;
            if (maxMaChiTiet != null && maxMaChiTiet.startsWith("CTDGG")) {
                try {
                    nextNumber = Integer.parseInt(maxMaChiTiet.substring(5)) + 1;
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi khi parse mã CTDGG: " + e.getMessage());
                }
            }

            Set<String> addedDSP = new HashSet<>();

            for (viewCTSPDTO ctsp : dsCTSP) {
                Integer idDSP = ctsp.getDsp().getId();
                BigDecimal giaBanDau = ctsp.getCtsp().getGiaBan();
                BigDecimal giaSauKhiGiam = ctsp.getGiaSauKhiGiam();
                String key = idDSP + "_" + giaBanDau;

                if (addedDSP.contains(key)) continue;

                DongSanPham dongSanPham = sanPhamMap.get(idDSP);
                if (dongSanPham == null) continue;

                // Lấy danh sách bản ghi cũ có cùng idDongSanPham và giaBanDau
                List<ChiTietDotGiamGia> existingChiTietList = repo2.findByIdDongSanPhamAndGiaBanDau(dongSanPham, giaBanDau);

                boolean isLowerPrice = false;
                if (existingChiTietList.isEmpty()) {
                    // Nếu không có bản ghi cũ, trực tiếp tạo và lưu chi tiết giảm giá mới
                    String newMaChiTiet = String.format("CTDGG%05d", nextNumber++);
                    ChiTietDotGiamGia chiTiet = new ChiTietDotGiamGia();
                    chiTiet.setMa(newMaChiTiet);
                    chiTiet.setDotGiamGia(dotGiamGia);
                    chiTiet.setIdDongSanPham(dongSanPham);
                    chiTiet.setGiaBanDau(giaBanDau);
                    chiTiet.setGiaSauKhiGiam(giaSauKhiGiam);
                    chiTiet.setDeleted(false);
                    repo2.save(chiTiet);
                    addedDSP.add(key); // Thêm key vào danh sách đã thêm
                    System.out.println("Thêm giảm giá cho sản phẩm mới: " + idDSP);
                } else {
                    // Nếu có bản ghi cũ, thực hiện so sánh giá
                    for (ChiTietDotGiamGia existingChiTiet : existingChiTietList) {
                        if (giaSauKhiGiam.compareTo(existingChiTiet.getGiaSauKhiGiam()) < 0) {
                            existingChiTiet.setDeleted(true);
                            isLowerPrice = true;
                        }
                    }

                    // Nếu có bản ghi cũ cần cập nhật, lưu tất cả lại 1 lần
                    if (isLowerPrice) {
                        repo2.saveAll(existingChiTietList);
                    } else {
                        System.out.println("Giá không thấp hơn bản ghi cũ, bỏ qua: " + idDSP);
                        continue;
                    }

                    // Tạo mới mã giảm giá
                    String newMaChiTiet = String.format("CTDGG%05d", nextNumber++);
                    ChiTietDotGiamGia chiTiet = new ChiTietDotGiamGia();
                    chiTiet.setMa(newMaChiTiet);
                    chiTiet.setDotGiamGia(dotGiamGia);
                    chiTiet.setIdDongSanPham(dongSanPham);
                    chiTiet.setGiaBanDau(giaBanDau);
                    chiTiet.setGiaSauKhiGiam(giaSauKhiGiam);
                    chiTiet.setDeleted(false);
                    repo2.save(chiTiet);
                    addedDSP.add(key); // Thêm key vào danh sách đã thêm
                    System.out.println("Thêm giảm giá cho sản phẩm: " + idDSP);
                }
            }


            System.out.println("Thêm tất cả chi tiết giảm giá thành công!");
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm đợt giảm giá: " + e.getMessage());
            throw e;
        }
    }

    @Modifying
@Transactional
    public void deleteDotGiamGiaById(Integer id) {
        try {
            repository.updateDotGiamGiaDeleted(id);
            System.out.println("Cập nhật trạng thái deleted cho DotGiamGia với ID: " + id);
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật DotGiamGia: " + e.getMessage());
            throw e;
        }
    }


    public void deleteChiTietDotGiamGiaById(Integer id) {
        try {
            repository.updateChiTietDotGiamGiaDeleted(id);
            System.out.println("Cập nhật trạng thái deleted cho ChiTietDotGiamGia với ID: " + id);
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật ChiTietDotGiamGia: " + e.getMessage());
            throw e;
        }
    }

    public List<DongSanPham> getThatDongSanPham(Integer id){
        return repository.getThatDongSanPham(id);
    }

    public Optional<DotGiamGia> findOne(Integer id){
        return repository.findById(id);
    }

    @Transactional
    public void updateDotGiamGia(Integer dotGiamGiaId, dot_giam_gia_DTO dotGiamGiaDTO, List<Integer> idDongSanPham, List<viewCTSPDTO> dsCTSP) {
        try {
            DotGiamGia dotGiamGia = repository.findById(dotGiamGiaId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với id: " + dotGiamGiaId));

            LocalDate today = LocalDate.now();

            // Cập nhật thông tin chung của đợt giảm giá
            LocalDateTime ngayBatDauFormatted = dotGiamGiaDTO.getNgayBatDau().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
            dotGiamGia.setNgayBatDau(Timestamp.valueOf(ngayBatDauFormatted));

            LocalDateTime ngayKetThucFormatted = dotGiamGiaDTO.getNgayKetThuc().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
            dotGiamGia.setNgayKetThuc(Timestamp.valueOf(ngayKetThucFormatted));

            dotGiamGia.setMa(dotGiamGiaDTO.getMa());
            dotGiamGia.setTenDotGiamGia(dotGiamGiaDTO.getTenDotGiamGia());
            dotGiamGia.setLoaiGiamGiaApDung(dotGiamGiaDTO.getLoaiGiamGiaApDung());
            dotGiamGia.setGiaTriGiamGia(dotGiamGiaDTO.getGiaTriGiamGia());
            dotGiamGia.setSoTienGiamToiDa(dotGiamGiaDTO.getSoTienGiamToiDa());
            dotGiamGia.setTrangThai(ngayBatDauFormatted.toLocalDate().isAfter(today));
            repository.save(dotGiamGia);

            // Lấy danh sách sản phẩm từ DB
            List<DongSanPham> dsSanPham = dongSanPhamRepository.findAllById(idDongSanPham);
            Map<Integer, DongSanPham> sanPhamMap = dsSanPham.stream()
                    .collect(Collectors.toMap(DongSanPham::getId, dsp -> dsp));

            // Lấy mã lớn nhất hiện có trong database
            String maxMaChiTiet = repo2.findMaxMa();
            int nextNumber = 1;
            if (maxMaChiTiet != null && maxMaChiTiet.startsWith("CTDGG")) {
                try {
                    nextNumber = Integer.parseInt(maxMaChiTiet.substring(5)) + 1;
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi khi parse mã CTDGG: " + e.getMessage());
                }
            }

            Set<String> addedDSP = new HashSet<>();

            // Lấy danh sách các dòng sản phẩm đã chọn từ DB
            Set<Integer> selectedProductIds = dsCTSP.stream()
                    .map(ctsp -> ctsp.getDsp().getId())
                    .collect(Collectors.toSet());

            // Cập nhật hoặc xóa chi tiết đợt giảm giá
            List<ChiTietDotGiamGia> existingChiTietList = repo2.findByDotGiamGia(dotGiamGia);

            // Xóa các chi tiết đợt giảm giá cho sản phẩm không còn được chọn
            for (ChiTietDotGiamGia chiTiet : existingChiTietList) {
                Integer idDSP = chiTiet.getIdDongSanPham().getId();
                if (!selectedProductIds.contains(idDSP)) {
                    chiTiet.setDeleted(true); // Đánh dấu là đã xóa
                }
            }

            // Lưu lại những chi tiết đợt giảm giá bị xóa hoặc cập nhật
            repo2.saveAll(existingChiTietList);

            for (viewCTSPDTO ctsp : dsCTSP) {
                Integer idDSP = ctsp.getDsp().getId();
                BigDecimal giaBanDau = ctsp.getCtsp().getGiaBan();
                BigDecimal giaSauKhiGiam = ctsp.getGiaSauKhiGiam();
                String key = idDSP + "_" + giaBanDau;

                if (addedDSP.contains(key)) continue;

                DongSanPham dongSanPham = sanPhamMap.get(idDSP);
                if (dongSanPham == null) continue;

                // Lấy danh sách bản ghi cũ có cùng idDongSanPham và giaBanDau
                List<ChiTietDotGiamGia> existingChiTietListForDSP = repo2.findByIdDongSanPhamAndGiaBanDau(dongSanPham, giaBanDau);

                boolean isLowerPrice = false;
                if (existingChiTietListForDSP.isEmpty()) {
                    // Nếu không có bản ghi cũ, trực tiếp tạo và lưu chi tiết giảm giá mới
                    String newMaChiTiet = String.format("CTDGG%05d", nextNumber++);
                    ChiTietDotGiamGia chiTiet = new ChiTietDotGiamGia();
                    chiTiet.setMa(newMaChiTiet);
                    chiTiet.setDotGiamGia(dotGiamGia);
                    chiTiet.setIdDongSanPham(dongSanPham);
                    chiTiet.setGiaBanDau(giaBanDau);
                    chiTiet.setGiaSauKhiGiam(giaSauKhiGiam);
                    chiTiet.setDeleted(false);
                    repo2.save(chiTiet);
                    addedDSP.add(key); // Thêm key vào danh sách đã thêm
                    System.out.println("Thêm giảm giá cho sản phẩm mới: " + idDSP);
                } else {
                    // Nếu có bản ghi cũ, thực hiện so sánh giá
                    for (ChiTietDotGiamGia existingChiTiet : existingChiTietListForDSP) {
                        if (giaSauKhiGiam.compareTo(existingChiTiet.getGiaSauKhiGiam()) < 0) {
                            existingChiTiet.setDeleted(true);
                            isLowerPrice = true;
                        }
                    }

                    // Nếu có bản ghi cũ cần cập nhật, lưu tất cả lại 1 lần
                    if (isLowerPrice) {
                        repo2.saveAll(existingChiTietListForDSP);
                    } else {
                        System.out.println("Giá không thấp hơn bản ghi cũ, bỏ qua: " + idDSP);
                        continue;
                    }

                    // Tạo mới mã giảm giá
                    String newMaChiTiet = String.format("CTDGG%05d", nextNumber++);
                    ChiTietDotGiamGia chiTiet = new ChiTietDotGiamGia();
                    chiTiet.setMa(newMaChiTiet);
                    chiTiet.setDotGiamGia(dotGiamGia);
                    chiTiet.setIdDongSanPham(dongSanPham);
                    chiTiet.setGiaBanDau(giaBanDau);
                    chiTiet.setGiaSauKhiGiam(giaSauKhiGiam);
                    chiTiet.setDeleted(false);
                    repo2.save(chiTiet);
                    addedDSP.add(key); // Thêm key vào danh sách đã thêm
                    System.out.println("Thêm giảm giá cho sản phẩm: " + idDSP);
                }
            }

            System.out.println("Cập nhật đợt giảm giá thành công!");
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật đợt giảm giá: " + e.getMessage());
            throw e;
        }
    }


    public Page<DotGiamGia> timKiem(Pageable pageable, String maDGG, String tenDGG, String loaiGiamGiaApDung,BigDecimal giaTriGiamGia, BigDecimal soTienGiamToiDa, Date ngayBatDau,Date ngayKetThuc,Boolean trangThai, Boolean deleted){
        return repository.timKiem(pageable, maDGG,tenDGG, loaiGiamGiaApDung, giaTriGiamGia,soTienGiamToiDa,ngayBatDau,ngayKetThuc,trangThai,deleted);
    }

    @PostConstruct //Chạy sau khi khởi động
    public void initUpdate() {
        updateStatusAutomatically();
    }

    @Scheduled(cron = "0 0 * * * *") // Chạy mỗi giờ
    @Transactional
    public void updateStatusAutomatically() {
        Date today = java.sql.Date.valueOf(LocalDate.now());
        repository.updateStatusIfStartDatePassed(today);
        repository.updateDeletedIfEndDatePassed(today);
        repository.updateDeletedChiTietDotGiamGia();

    }

}
