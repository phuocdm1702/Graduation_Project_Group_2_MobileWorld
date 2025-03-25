package com.example.graduation_project_group_2_mobileworld.service;

import com.example.graduation_project_group_2_mobileworld.dto.thongKe.TopSellingProductDTO;
import com.example.graduation_project_group_2_mobileworld.entity.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.repository.thongKe.CTSPForThongKe;
import com.example.graduation_project_group_2_mobileworld.repository.thongKe.thongKeRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class thongKeService {
    private static thongKeRepository tkRepo;

    @Autowired
    private CTSPForThongKe chiTietSanPhamRepository;

    @Autowired
    public thongKeService(thongKeRepository tkRepo) {
        this.tkRepo = tkRepo;

    }

    public static Map<String, Object> getThongKeTheoNgay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date ngayHienTai = cal.getTime();
        return tkRepo.thongKeTheoNgay(ngayHienTai);
    }

    public static Map<String, Object> getThongKeTheoTuan() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date startOfWeek = cal.getTime();
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date endOfWeek = cal.getTime();
        return tkRepo.thongKeTheoTuan(startOfWeek, endOfWeek);
    }

    public static Map<String, Object> getThongKeTheoThang() {
        Calendar cal = Calendar.getInstance();
        int thang = cal.get(Calendar.MONTH) + 1; // +1 vì tháng bắt đầu từ 0
        int nam = cal.get(Calendar.YEAR);
        return tkRepo.thongKeTheoThang(thang, nam);
    }

    public static Map<String, Object> getThongKeTheoNam() {
        Calendar cal = Calendar.getInstance();
        int nam = cal.get(Calendar.YEAR);
        return tkRepo.thongKeTheoNam(nam);
    }

    public Page<TopSellingProductDTO> getTopSellingProducts(String filterType, String startDateStr, String endDateStr, int page, int size) {
        Date startDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        if ("day".equals(filterType)) {
            startDate = new Date();
            endDate = startDate;
        } else if ("month".equals(filterType)) {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDate = cal.getTime();
            endDate = new Date();
        } else if ("year".equals(filterType)) {
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDate = cal.getTime();
            endDate = new Date();
        } else if ("custom".equals(filterType) && startDateStr != null && endDateStr != null) {
            try {
                startDate = sdf.parse(startDateStr);
                endDate = sdf.parse(endDateStr);
            } catch (ParseException e) {
                throw new RuntimeException("Invalid date format");
            }
        }

        // Sắp xếp theo soLuongBan giảm dần thông qua Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by("soLuongBan").descending());
        Page<Object[]> results = tkRepo.findTopSellingProducts(startDate, endDate, pageable);

        List<TopSellingProductDTO> dtos = results.getContent().stream().map(result -> {
            Integer chiTietSanPhamId = (Integer) result[0];
            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(chiTietSanPhamId).orElse(null);
            if (ctsp != null) {
                String productName = String.format("%s - %s - %s",
                        ctsp.getIdSanPham().getTenSanPham(),
                        ctsp.getIdMauSac().getMauSac(),
                        ctsp.getIdBoNhoTrong().getDungLuongBoNhoTrong());
                return new TopSellingProductDTO(
                        ctsp.getIdAnhSanPham().getDuongDan(),
                        productName,
                        ctsp.getGiaBan(),
                        ((Number) result[1]).intValue()
                );
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, results.getTotalElements());
    }
}

