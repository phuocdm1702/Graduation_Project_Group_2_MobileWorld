package com.example.graduation_project_group_2_mobileworld.service.thong_ke;

import com.example.graduation_project_group_2_mobileworld.dto.thongKe.HangBanChayDTO;
import com.example.graduation_project_group_2_mobileworld.dto.thongKe.LoaiHoaDonDTO;
import com.example.graduation_project_group_2_mobileworld.dto.thongKe.SanPhamHetHangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.thongKe.TopSellingProductDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.repository.thongKe.CTSPForThongKe;
import com.example.graduation_project_group_2_mobileworld.repository.thongKe.thongKeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ThongKeService {
    // Loại bỏ static để Spring có thể inject
    private thongKeRepository tkRepo;

    @Autowired
    private CTSPForThongKe chiTietSanPhamRepository;

    @Autowired
    public ThongKeService(thongKeRepository tkRepo) {
        this.tkRepo = tkRepo;
    }

    // Loại bỏ static
    public Map<String, Object> getThongKeTheoNgay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date ngayHienTai = cal.getTime();
        return tkRepo.thongKeTheoNgay(ngayHienTai);
    }

    // Loại bỏ static
    public Map<String, Object> getThongKeTheoTuan() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date startOfWeek = cal.getTime();
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date endOfWeek = cal.getTime();
        return tkRepo.thongKeTheoTuan(startOfWeek, endOfWeek);
    }

    // Loại bỏ static
    public Map<String, Object> getThongKeTheoThang() {
        Calendar cal = Calendar.getInstance();
        int thang = cal.get(Calendar.MONTH) + 1; // +1 vì tháng bắt đầu từ 0
        int nam = cal.get(Calendar.YEAR);
        return tkRepo.thongKeTheoThang(thang, nam);
    }

    // Loại bỏ static
    public Map<String, Object> getThongKeTheoNam() {
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

    public List<Map<String, Object>> getAllTopSellingProducts(String filterType, String startDateStr, String endDateStr) {
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

        List<Object[]> results = tkRepo.findAllTopSellingProducts(startDate, endDate);

        return results.stream().map(result -> {
            Integer chiTietSanPhamId = (Integer) result[0];
            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(chiTietSanPhamId).orElse(null);
            if (ctsp != null) {
                String productName = String.format("%s - %s - %s",
                        ctsp.getIdSanPham().getTenSanPham(),
                        ctsp.getIdMauSac().getMauSac(),
                        ctsp.getIdBoNhoTrong().getDungLuongBoNhoTrong());
                Map<String, Object> map = new HashMap<>();
                map.put("imageUrl", ctsp.getIdAnhSanPham().getDuongDan());
                map.put("productName", productName);
                map.put("price", ctsp.getGiaBan());
                map.put("soldQuantity", ((Number) result[1]).longValue());
                return map;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Map<String, Object> getGrowthData() {
        Map<String, Object> growthData = new HashMap<>();

        Map<String, Object> ngayHienTai = tkRepo.tangTruongTheoNgay(new Date());
        Map<String, Object> thangHienTai = tkRepo.tangTruongTheoThang(new Date());
        Map<String, Object> namHienTai = tkRepo.tangTruongTheoNam(new Date());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Map<String, Object> ngayTruoc = tkRepo.tangTruongTheoNgay(cal.getTime());

        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Map<String, Object> thangTruoc = tkRepo.tangTruongTheoThang(cal.getTime());

        cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        Map<String, Object> namTruoc = tkRepo.tangTruongTheoNam(cal.getTime());

        growthData.put("doanhThuNgay", getDoubleValue(ngayHienTai, "doanhThu"));
        growthData.put("doanhThuThang", getDoubleValue(thangHienTai, "doanhThu"));
        growthData.put("doanhThuNam", getDoubleValue(namHienTai, "doanhThu"));
        growthData.put("sanPhamDaBanThang", getLongValue(thangHienTai, "sanPhamDaBan"));
        growthData.put("hoaDonTheoNgay", getLongValue(ngayHienTai, "tongSoDonHang"));
        growthData.put("hoaDonTheoNam", getLongValue(namHienTai, "tongSoDonHang"));

        growthData.put("growthDoanhThuNgay", calculateGrowth(getDoubleValue(ngayHienTai, "doanhThu"), getDoubleValue(ngayTruoc, "doanhThu")));
        growthData.put("growthDoanhThuThang", calculateGrowth(getDoubleValue(thangHienTai, "doanhThu"), getDoubleValue(thangTruoc, "doanhThu")));
        growthData.put("growthDoanhThuNam", calculateGrowth(getDoubleValue(namHienTai, "doanhThu"), getDoubleValue(namTruoc, "doanhThu")));
        growthData.put("growthSanPhamDaBanThang", calculateGrowth(getDoubleValue(thangHienTai, "sanPhamDaBan"), getDoubleValue(thangTruoc, "sanPhamDaBan")));
        growthData.put("growthHoaDonTheoNgay", calculateGrowth(getDoubleValue(ngayHienTai, "tongSoDonHang"), getDoubleValue(ngayTruoc, "tongSoDonHang")));
        growthData.put("growthHoaDonTheoNam", calculateGrowth(getDoubleValue(namHienTai, "tongSoDonHang"), getDoubleValue(namTruoc, "tongSoDonHang")));

        return growthData;
    }

    private double getDoubleValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        }
        return 0.0;
    }

    private long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Long) {
            return (Long) value;
        }
        return 0L;
    }

    private double calculateGrowth(double current, double previous) {
        if (previous == 0) return 0;
        return ((current - previous) / previous) * 100;
    }

    public Page<SanPhamHetHangDTO> thongKeSanPhamHetHang(Pageable pageable) {
        return tkRepo.thongKeSanPhamHetHang(pageable);
    }

    public List<Map<String, Object>> getAllSanPhamHetHang() {
        List<SanPhamHetHangDTO> results = tkRepo.thongKeSanPhamHetHangNoPage();
        return results.stream().map(dto -> {
            Map<String, Object> map = new HashMap<>();
            map.put("tenSanPham", dto.getTenSanPham());
            map.put("soLuong", dto.getSoLuong());
            return map;
        }).collect(Collectors.toList());
    }

    public List<LoaiHoaDonDTO> thongKeLoaiHoaDon() {
        return tkRepo.thongKeLoaiHoaDon();
    }

    public List<Map<String, Object>> getAllLoaiHoaDon() {
        List<LoaiHoaDonDTO> results = tkRepo.thongKeLoaiHoaDon();
        return results.stream().map(dto -> {
            Map<String, Object> map = new HashMap<>();
            map.put("loaiDon", dto.getLoaiDon());
            map.put("soLuong", dto.getSoLuong());
            return map;
        }).collect(Collectors.toList());
    }

    public List<HangBanChayDTO> thongKeHangBanChay() {
        return tkRepo.thongKeHangBanChay();
    }

    public Map<String, Long> getOrderStatusStats(String filterType, Date date) {
        List<Map<String, Object>> result = tkRepo.thongKeTrangThaiDonHang(filterType, date);
        Map<String, Long> statusStats = new HashMap<>();

        statusStats.put("Chờ xác nhận", 0L);
        statusStats.put("Chờ giao hàng", 0L);
        statusStats.put("Đang giao", 0L);
        statusStats.put("Hoàn thành", 0L);
        statusStats.put("Đã hủy", 0L);

        for (Map<String, Object> entry : result) {
            Object trangThaiObj = entry.get("trangThai");
            Integer trangThai = null;
            if (trangThaiObj instanceof Short) {
                trangThai = ((Short) trangThaiObj).intValue();
            } else if (trangThaiObj instanceof Integer) {
                trangThai = (Integer) trangThaiObj;
            }

            Long soLuong = (Long) entry.get("soLuong");
            if (trangThai != null) {
                switch (trangThai) {
                    case 1:
                        statusStats.put("Chờ xác nhận", soLuong);
                        break;
                    case 2:
                        statusStats.put("Chờ giao hàng", soLuong);
                        break;
                    case 3:
                        statusStats.put("Đang giao", soLuong);
                        break;
                    case 4:
                        statusStats.put("Hoàn thành", soLuong);
                        break;
                    case 5:
                        statusStats.put("Đã hủy", soLuong);
                        break;
                }
            }
        }

        return statusStats;
    }
}

