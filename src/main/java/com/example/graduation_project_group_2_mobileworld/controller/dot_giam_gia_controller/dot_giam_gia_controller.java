package com.example.graduation_project_group_2_mobileworld.controller.dot_giam_gia_controller;

import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.RequestDTO;
import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.addDotGiamGiaDTO;
import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import com.example.graduation_project_group_2_mobileworld.service.dot_giam_gia_service.dot_giam_gia_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/dot_giam_gia")
public class dot_giam_gia_controller {
    @Autowired
    private dot_giam_gia_service sr;

    @GetMapping("/home")
    public Page<DotGiamGia> hienThi(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    Model model) {
        Pageable pageable = PageRequest.of(page, size);
        return sr.HienThi(pageable);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DotGiamGia>> search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String maDGG,
            @RequestParam(required = false) String tenDGG,
            @RequestParam(required = false) String loaiGiamGiaApDung,
            @RequestParam(required = false) BigDecimal giaTriGiamGia,
            @RequestParam(required = false) BigDecimal soTienGiamToiDa,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngayBatDau,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngayKetThuc,
            @RequestParam(required = false) Boolean trangThai,
            @RequestParam(required = false) Boolean deleted
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Date sqlNgayBatDau = ngayBatDau != null ? java.sql.Date.valueOf(ngayBatDau) : null;
        Date sqlNgayKetThuc = ngayKetThuc != null ? java.sql.Date.valueOf(ngayKetThuc) : null;

        Page<DotGiamGia> result = sr.timKiem(
                pageable, maDGG, tenDGG, loaiGiamGiaApDung,giaTriGiamGia, soTienGiamToiDa, sqlNgayBatDau, sqlNgayKetThuc, trangThai,deleted);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/showFinish")
    public Page<DotGiamGia> showFinish(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        return sr.hienThiFinish(pageable);
    }



    @PostMapping("/ViewAddDotGiamGia")
    public ResponseEntity<CombinedResponse> hienThiAdd(@RequestBody RequestDTO request) {
        String keyword = request.getKeyword();
        List<Integer> idDSPs = request.getIdDSPs();

        List<DongSanPham> dspList;
        List<viewCTSPDTO> ctspList = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            dspList = sr.getDSP(null);
        } else {
            dspList = sr.getDSP(keyword); // Tìm kiếm theo từ khóa
        }

        if (idDSPs != null && !idDSPs.isEmpty()) {
            ctspList = sr.getAllCTSP(idDSPs);
        }

        CombinedResponse response = new CombinedResponse(dspList, ctspList);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/AddDotGiamGia")
    public ResponseEntity<?> addData(@RequestBody addDotGiamGiaDTO request) {
        if (request == null || request.getDotGiamGia() == null) {
            return ResponseEntity.badRequest().body("Dữ liệu không hợp lệ");
        }
        try {
            System.out.println("Dữ liệu nhận được: " + request.toString());
            sr.addDotGiamGia(request.getDotGiamGia(), request.getIdDSPs(), request.getCtspList());
            return ResponseEntity.ok("Thêm thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/ViewAddDotGiamGia/exists/ma")
    public ResponseEntity<Boolean> checkMa(@RequestParam String ma){
        System.out.println(ma);
        return ResponseEntity.ok(sr.existByMa(ma));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDotGiamGia(@PathVariable("id") Integer id) {
        try {
            sr.deleteDotGiamGiaById(id);  // Gọi service để xóa
            sr.deleteChiTietDotGiamGiaById(id);
            return ResponseEntity.noContent().build(); // Trả về trạng thái thành công
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // Trả về lỗi nếu có vấn đề
        }
    }

    @GetMapping("/viewUpdate")
    public ResponseEntity<?> viewUpdateDotGiamGia(@RequestParam Integer id) {
        try {
            List<DongSanPham> dspList = sr.getThatDongSanPham(id);
            return ResponseEntity.ok(dspList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }

    @PutMapping("/AddDotGiamGia/{id}")
    public ResponseEntity<?> updateData(@RequestBody addDotGiamGiaDTO request, @PathVariable("id") Integer id) {
        if (request == null || request.getDotGiamGia() == null) {
            return ResponseEntity.badRequest().body("Dữ liệu không hợp lệ");
        }
        try {
            System.out.println("Dữ liệu nhận được: " + request.toString());
            sr.updateDotGiamGia(id,request.getDotGiamGia(), request.getIdDSPs(), request.getCtspList());
            return ResponseEntity.ok("Thêm thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }

}

