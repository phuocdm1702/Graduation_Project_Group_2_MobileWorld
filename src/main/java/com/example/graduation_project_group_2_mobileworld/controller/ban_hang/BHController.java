package com.example.graduation_project_group_2_mobileworld.controller.ban_hang;

import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.nhan_vien.NhanVienRepository;
import com.example.graduation_project_group_2_mobileworld.service.ban_hang_service.BanHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ban-hang")
@CrossOrigin(origins = "http://localhost:3000")
public class BHController {

    @Autowired
    private BanHangService banHangService;

    @Autowired
    private NhanVienRepository nhanVienRepository;


    @GetMapping("/data")
    public List<HDban_hangDTO> fetchDataHD() {
        List<HDban_hangDTO> listHD = banHangService.getAllHD();
        if (listHD == null) {
            System.out.println("Danh sach Hoa don tra ve null!");
            return Collections.emptyList();
        }
        return listHD;
    }

    @PostMapping("/addHD")
    public ResponseEntity<HoaDon> addHD(@RequestBody HDban_hangDTO hd_dto) {
        if (hd_dto.getMa() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        HoaDon hd = new HoaDon();
        hd.setMa(hd_dto.getMa());
        hd.setTrangThai((short) 0);

        // Gán các trường bắt buộc
        hd.setTienSanPham(BigDecimal.valueOf(0));
        hd.setLoaiDon("Tại quầy");
        hd.setPhiVanChuyen(BigDecimal.valueOf(0));
        hd.setTongTien(BigDecimal.valueOf(0));
        hd.setTenKhachHang("Khách lẻ");
        hd.setDiaChiKhachHang("N/A");
        hd.setSoDienThoaiKhachHang("0000000000");
        hd.setNgayTao(new Date());
        hd.setDeleted(false);
        hd.setCreatedAt(new Date());

        // Gán idNhanVien
        NhanVien nhanVien = nhanVienRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));
        hd.setIdNhanVien(nhanVien);


        HoaDon saveHD = banHangService.addHD(hd);
        return ResponseEntity.ok(saveHD);
    }
}