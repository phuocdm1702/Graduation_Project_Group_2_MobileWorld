package com.example.graduation_project_group_2_mobileworld.controller.thong_ke;

import com.example.graduation_project_group_2_mobileworld.dto.thongKe.HangBanChayDTO;
import com.example.graduation_project_group_2_mobileworld.dto.thongKe.LoaiHoaDonDTO;
import com.example.graduation_project_group_2_mobileworld.dto.thongKe.SanPhamHetHangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.thongKe.SoLieuDTO;
import com.example.graduation_project_group_2_mobileworld.dto.thongKe.TopSellingProductDTO;
import com.example.graduation_project_group_2_mobileworld.service.thong_ke.ThongKeService;
import com.example.graduation_project_group_2_mobileworld.service.thong_ke.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/dashboard")
public class thongKeController {
    @Autowired
    private ThongKeService sr;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> hienThi(
            @RequestParam(required = false, defaultValue = "month") String filterType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") int sanPhamHetHangPage,
            @RequestParam(defaultValue = "8") int sanPhamHetHangSize) {
        Map<String, Object> response = new HashMap<>();

        // Dữ liệu thống kê hiện tại
        Map<String, Object> ngay = sr.getThongKeTheoNgay();
        Map<String, Object> tuan = sr.getThongKeTheoTuan();
        Map<String, Object> thang = sr.getThongKeTheoThang();
        Map<String, Object> nam = sr.getThongKeTheoNam();

        SoLieuDTO ngayDTO = new SoLieuDTO(
                (BigDecimal) ngay.get("doanhThu"),
                (Long) ngay.get("sanPhamDaBan"),
                ((Long) ngay.get("tongSoDonHang")).intValue()
        );
        SoLieuDTO tuanDTO = new SoLieuDTO(
                (BigDecimal) tuan.get("doanhThu"),
                (Long) tuan.get("sanPhamDaBan"),
                ((Long) tuan.get("tongSoDonHang")).intValue()
        );
        SoLieuDTO thangDTO = new SoLieuDTO(
                (BigDecimal) thang.get("doanhThu"),
                (Long) thang.get("sanPhamDaBan"),
                ((Long) thang.get("tongSoDonHang")).intValue()
        );
        SoLieuDTO namDTO = new SoLieuDTO(
                (BigDecimal) nam.get("doanhThu"),
                (Long) nam.get("sanPhamDaBan"),
                ((Long) nam.get("tongSoDonHang")).intValue()
        );

        response.put("ngay", Collections.singletonList(ngayDTO));
        response.put("tuan", Collections.singletonList(tuanDTO));
        response.put("thang", Collections.singletonList(thangDTO));
        response.put("nam", Collections.singletonList(namDTO));

        // Thêm dữ liệu thống kê HangBanChay
        response.put("hangBanChay", sr.thongKeHangBanChay());

        // Thêm dữ liệu thống kê LoaiHoaDon
        response.put("loaiHoaDon", sr.thongKeLoaiHoaDon());

        // Thêm dữ liệu thống kê SanPhamHetHang (có phân trang)
        Pageable sanPhamHetHangPageable = PageRequest.of(sanPhamHetHangPage, sanPhamHetHangSize);
        Page<SanPhamHetHangDTO> sanPhamHetHangPageData = sr.thongKeSanPhamHetHang(sanPhamHetHangPageable);
        response.put("sanPhamHetHang", sanPhamHetHangPageData.getContent());
        response.put("sanPhamHetHangTotalPages", sanPhamHetHangPageData.getTotalPages());
        response.put("sanPhamHetHangCurrentPage", sanPhamHetHangPageData.getNumber());

        // Thêm dữ liệu thống kê SanPhamBanChay (có  phân trang)
        Page<TopSellingProductDTO> topProductsPage = sr.getTopSellingProducts(filterType, startDate, endDate, page, size);
        response.put("topProducts", topProductsPage.getContent());
        response.put("totalPages", topProductsPage.getTotalPages());
        response.put("currentPage", topProductsPage.getNumber());

        // Dữ liệu tăng trưởng (so sánh với kỳ trước)
        Map<String, Object> growthData = sr.getGrowthData();
        response.put("growthData", growthData);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/order-status-stats")
    public ResponseEntity<Map<String, Long>> getOrderStatusStats(
            @RequestParam(defaultValue = "month") String filterType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date
    ) {
        if (date == null) {
            date = new Date(); // Mặc định là ngày hiện tại nếu không truyền date
        }
        Map<String, Long> statusStats = sr.getOrderStatusStats(filterType, date);
        return ResponseEntity.ok(statusStats);
    }

    @GetMapping("/export-excel")
    public ResponseEntity<Resource> exportExcel(
            @RequestParam(defaultValue = "year") String filterType,
            @RequestParam(required = false) String startDate, // Thêm cho custom
            @RequestParam(required = false) String endDate   // Thêm cho custom
    ) {
        Map<String, Object> ngay = sr.getThongKeTheoNgay();
        Map<String, Object> tuan = sr.getThongKeTheoTuan();
        Map<String, Object> thang = sr.getThongKeTheoThang();
        Map<String, Object> nam = sr.getThongKeTheoNam();
        List<Map<String, Object>> topProducts = sr.getAllTopSellingProducts(filterType, startDate, endDate);
        Map<String, Long> orderStatusStats = sr.getOrderStatusStats(filterType, new Date());
        List<Map<String, Object>> loaiHoaDon = sr.getAllLoaiHoaDon();
        List<Map<String, Object>> sanPhamHetHang = sr.getAllSanPhamHetHang();

        Workbook workbook = new XSSFWorkbook();
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // Sheet 1: Thống Kê Tổng Quan
        Sheet sheetTongQuan = workbook.createSheet("Thống Kê Tổng Quan");
        Row headerRowTongQuan = sheetTongQuan.createRow(0);
        String[] headersTongQuan = {"Khoảng Thời Gian", "Doanh Thu (VND)", "Số Sản Phẩm Bán", "Tổng Số Đơn Hàng"};
        for (int i = 0; i < headersTongQuan.length; i++) {
            Cell cell = headerRowTongQuan.createCell(i);
            cell.setCellValue(headersTongQuan[i]);
            cell.setCellStyle(headerStyle);
        }
        Row row1 = sheetTongQuan.createRow(1);
        row1.createCell(0).setCellValue("Hôm Nay");
        row1.createCell(1).setCellValue(((BigDecimal) ngay.get("doanhThu") != null ? (BigDecimal) ngay.get("doanhThu") : BigDecimal.ZERO).doubleValue());
        row1.createCell(2).setCellValue(((Long) ngay.get("sanPhamDaBan") != null ? (Long) ngay.get("sanPhamDaBan") : 0L));
        row1.createCell(3).setCellValue(((Long) ngay.get("tongSoDonHang") != null ? ((Long) ngay.get("tongSoDonHang")).intValue() : 0));

        Row row2 = sheetTongQuan.createRow(2);
        row2.createCell(0).setCellValue("Tuần Này");
        row2.createCell(1).setCellValue(((BigDecimal) tuan.get("doanhThu") != null ? (BigDecimal) tuan.get("doanhThu") : BigDecimal.ZERO).doubleValue());
        row2.createCell(2).setCellValue(((Long) tuan.get("sanPhamDaBan") != null ? (Long) tuan.get("sanPhamDaBan") : 0L));
        row2.createCell(3).setCellValue(((Long) tuan.get("tongSoDonHang") != null ? ((Long) tuan.get("tongSoDonHang")).intValue() : 0));

        Row row3 = sheetTongQuan.createRow(3);
        row3.createCell(0).setCellValue("Tháng Này");
        row3.createCell(1).setCellValue(((BigDecimal) thang.get("doanhThu") != null ? (BigDecimal) thang.get("doanhThu") : BigDecimal.ZERO).doubleValue());
        row3.createCell(2).setCellValue(((Long) thang.get("sanPhamDaBan") != null ? (Long) thang.get("sanPhamDaBan") : 0L));
        row3.createCell(3).setCellValue(((Long) thang.get("tongSoDonHang") != null ? ((Long) thang.get("tongSoDonHang")).intValue() : 0));

        Row row4 = sheetTongQuan.createRow(4);
        row4.createCell(0).setCellValue("Năm Nay");
        row4.createCell(1).setCellValue(((BigDecimal) nam.get("doanhThu") != null ? (BigDecimal) nam.get("doanhThu") : BigDecimal.ZERO).doubleValue());
        row4.createCell(2).setCellValue(((Long) nam.get("sanPhamDaBan") != null ? (Long) nam.get("sanPhamDaBan") : 0L));
        row4.createCell(3).setCellValue(((Long) nam.get("tongSoDonHang") != null ? ((Long) nam.get("tongSoDonHang")).intValue() : 0));

        // Sheet: Sản Phẩm Bán Chạy
        Sheet sheetTopProducts = workbook.createSheet("Sản Phẩm Bán Chạy");
        Row headerRowTopProducts = sheetTopProducts.createRow(0);
        String[] headersTopProducts = {"STT", "Tên Sản Phẩm", "Giá Bán (VND)", "Số Lượng Đã Bán"};
        for (int i = 0; i < headersTopProducts.length; i++) {
            Cell cell = headerRowTopProducts.createCell(i);
            cell.setCellValue(headersTopProducts[i]);
            cell.setCellStyle(headerStyle);
        }
        for (int i = 0; i < topProducts.size(); i++) {
            Map<String, Object> product = topProducts.get(i);
            Row row = sheetTopProducts.createRow(i + 1);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue((String) product.get("productName"));
            row.createCell(2).setCellValue(((BigDecimal) product.get("price")).doubleValue());
            row.createCell(3).setCellValue(((Number) product.get("soldQuantity")).longValue());
        }

        // Sheet: Trạng Thái Đơn Hàng
        Sheet sheetOrderStatus = workbook.createSheet("Trạng Thái Đơn Hàng");
        Row headerRowOrderStatus = sheetOrderStatus.createRow(0);
        String[] headersOrderStatus = {"Trạng Thái", "Số Lượng"};
        for (int i = 0; i < headersOrderStatus.length; i++) {
            Cell cell = headerRowOrderStatus.createCell(i);
            cell.setCellValue(headersOrderStatus[i]);
            cell.setCellStyle(headerStyle);
        }
        String[] statuses = {"Chờ xác nhận", "Chờ giao hàng", "Đang giao", "Hoàn thành", "Đã hủy"};
        for (int i = 0; i < statuses.length; i++) {
            Row row = sheetOrderStatus.createRow(i + 1);
            row.createCell(0).setCellValue(statuses[i]);
            row.createCell(1).setCellValue(orderStatusStats.getOrDefault(statuses[i], 0L));
        }

        // Sheet: Phân Phối Đa Kênh
        Sheet sheetLoaiHoaDon = workbook.createSheet("Phân Phối Đa Kênh");
        Row headerRowLoaiHoaDon = sheetLoaiHoaDon.createRow(0);
        String[] headersLoaiHoaDon = {"Loại Hóa Đơn", "Số Lượng"};
        for (int i = 0; i < headersLoaiHoaDon.length; i++) {
            Cell cell = headerRowLoaiHoaDon.createCell(i);
            cell.setCellValue(headersLoaiHoaDon[i]);
            cell.setCellStyle(headerStyle);
        }
        for (int i = 0; i < loaiHoaDon.size(); i++) {
            Map<String, Object> loaiDon = loaiHoaDon.get(i);
            Row row = sheetLoaiHoaDon.createRow(i + 1);
            row.createCell(0).setCellValue((String) loaiDon.get("loaiDon"));
            row.createCell(1).setCellValue(((Number) loaiDon.get("soLuong")).longValue());
        }

        // Sheet: Sản Phẩm Sắp Hết Hàng
        Sheet sheetSanPhamHetHang = workbook.createSheet("Sản Phẩm Sắp Hết Hàng");
        Row headerRowSanPhamHetHang = sheetSanPhamHetHang.createRow(0);
        String[] headersSanPhamHetHang = {"STT", "Tên Sản Phẩm", "Số Lượng"};
        for (int i = 0; i < headersSanPhamHetHang.length; i++) {
            Cell cell = headerRowSanPhamHetHang.createCell(i);
            cell.setCellValue(headersSanPhamHetHang[i]);
            cell.setCellStyle(headerStyle);
        }
        for (int i = 0; i < sanPhamHetHang.size(); i++) {
            Map<String, Object> product = sanPhamHetHang.get(i);
            Row row = sheetSanPhamHetHang.createRow(i + 1);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue((String) product.get("tenSanPham"));
            row.createCell(2).setCellValue(((Number) product.get("soLuong")).longValue());
        }

        for (Sheet sheet : workbook) {
            for (int i = 0; i < 10; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo file Excel", e);
        }

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=thong_ke_day_du.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(outputStream.size())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}