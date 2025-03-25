package com.example.graduation_project_group_2_mobileworld.controller.thong_ke;

import com.example.graduation_project_group_2_mobileworld.dto.thongKe.SoLieuDTO;
import com.example.graduation_project_group_2_mobileworld.dto.thongKe.TopSellingProductDTO;
import com.example.graduation_project_group_2_mobileworld.service.thongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/dashboard")
public class thongKeController {
    @Autowired
    private thongKeService sr;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> hienThi(
            @RequestParam(required = false, defaultValue = "month") String filterType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Map<String, Object> response = new HashMap<>();

        // Dữ liệu thống kê
        Map<String, Object> ngay = sr.getThongKeTheoNgay();
        Map<String, Object> tuan = sr.getThongKeTheoTuan();
        Map<String, Object> thang = sr.getThongKeTheoThang();
        Map<String, Object> nam = sr.getThongKeTheoNam();

        SoLieuDTO ngayDTO = new SoLieuDTO(
                (BigDecimal) ngay.get("doanhThu"),
                (BigDecimal) ngay.get("sanPhamDaBan"),
                ((Long) ngay.get("tongSoDonHang")).intValue()
        );
        SoLieuDTO tuanDTO = new SoLieuDTO(
                (BigDecimal) tuan.get("doanhThu"),
                (BigDecimal) tuan.get("sanPhamDaBan"),
                ((Long) tuan.get("tongSoDonHang")).intValue()
        );
        SoLieuDTO thangDTO = new SoLieuDTO(
                (BigDecimal) thang.get("doanhThu"),
                (BigDecimal) thang.get("sanPhamDaBan"),
                ((Long) thang.get("tongSoDonHang")).intValue()
        );
        SoLieuDTO namDTO = new SoLieuDTO(
                (BigDecimal) nam.get("doanhThu"),
                (BigDecimal) nam.get("sanPhamDaBan"),
                ((Long) nam.get("tongSoDonHang")).intValue()
        );

        response.put("ngay", Collections.singletonList(ngayDTO));
        response.put("tuan", Collections.singletonList(tuanDTO));
        response.put("thang", Collections.singletonList(thangDTO));
        response.put("nam", Collections.singletonList(namDTO));

        // Dữ liệu sản phẩm bán chạy với phân trang
        Page<TopSellingProductDTO> topProductsPage = sr.getTopSellingProducts(filterType, startDate, endDate, page, size);
        response.put("topProducts", topProductsPage.getContent());
        response.put("totalPages", topProductsPage.getTotalPages());
        response.put("currentPage", topProductsPage.getNumber());

        return ResponseEntity.ok(response);
    }
}