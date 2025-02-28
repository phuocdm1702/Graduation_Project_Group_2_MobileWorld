package com.example.graduation_project_group_2_mobileworld.controller.dot_giam_gia_controller;

import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.RequestDTO;
import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.addDotGiamGiaDTO;
import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.dot_giam_gia_DTO;
import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.service.dot_giam_gia_service.dot_giam_gia_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/dot_giam_gia")
public class dot_giam_gia_controller {
    @Autowired
    private dot_giam_gia_service sr;

    @GetMapping("/home")
    public List<DotGiamGia> hienThi(Model model) {
        List<DotGiamGia> ds=sr.getAll();
        return ds;
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
            System.out.println("Id dợt giảm giá:"+id);
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

