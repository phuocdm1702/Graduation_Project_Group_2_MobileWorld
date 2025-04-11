package com.example.graduation_project_group_2_mobileworld.controller.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTOGet;
import com.example.graduation_project_group_2_mobileworld.service.hoa_don.HoaDonService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/hoa-don")
public class HoaDonController {

    private final HoaDonService hoaDonService;

    public HoaDonController(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }

    // API lấy danh sách hóa đơn với phân trang và bộ lọc
    @GetMapping("/home")
    public ResponseEntity<Page<HoaDonDTO>> getAllHoaDon(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String loaiDon,
            @RequestParam(required = false) Long minAmount,
            @RequestParam(required = false) Long maxAmount,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Short trangThai,
            Authentication authentication) {
        logAuthentication(authentication);
        return ResponseEntity.ok(hoaDonService.getHoaDonWithFilters(page, size, keyword, loaiDon, minAmount, maxAmount, startDate, endDate, trangThai));
    }

    // API lấy danh sách loại đơn
    @GetMapping("/order-types")
    public ResponseEntity<List<String>> getOrderTypes() {
        return ResponseEntity.ok(Arrays.asList("Online", "Trực tiếp"));
    }

    // API lấy chi tiết hóa đơn theo ID
    @GetMapping("/detail/{id}")
    public ResponseEntity<HoaDonDTO> getFullHoaDonDetail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(hoaDonService.getFullHoaDonDetails(id));
    }

    // API in hóa đơn dưới dạng PDF
    @GetMapping("/print/{id}")
    public ResponseEntity<byte[]> printHoaDon(@PathVariable("id") Integer id) {
        try {
            byte[] pdfBytes = hoaDonService.generateHoaDonPdf(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "hoa_don_" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return createErrorResponse("Không thể tạo PDF: " + e.getMessage());
        }
    }

    // API xuất danh sách hóa đơn ra Excel
    @GetMapping("/export-excel")
    public void exportHoaDonToExcel(HttpServletResponse response) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "DanhSachHoaDon_" + timestamp + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        hoaDonService.exportHoaDonToExcel(response);
    }

    // API lấy hóa đơn theo mã QR
    @GetMapping("/QR-by-ma/{ma}")
    public ResponseEntity<HoaDonDTO> getHoaDonByMa(@PathVariable String ma) {
        try {
            return ResponseEntity.ok(hoaDonService.getHoaDonByMa(ma));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // API đếm số lượng hóa đơn theo trạng thái
    @GetMapping("/status-count")
    public ResponseEntity<Map<Short, Long>> getHoaDonStatusCount() {
        return ResponseEntity.ok(hoaDonService.getHoaDonStatusCount());
    }

    // Hàm hỗ trợ ghi log xác thực
    private void logAuthentication(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("Authenticated user: " + authentication.getName());
        } else {
            System.out.println("No authenticated user");
        }
    }

    // Hàm tạo response lỗi chung
    private ResponseEntity<byte[]> createErrorResponse(String message) {
        byte[] errorBytes = message.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(errorBytes, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}