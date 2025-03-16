package com.example.graduation_project_group_2_mobileworld.controller.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.dto.phieuGiamGiaDTO.PhieuGiamGiaDTO;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaCaNhanService;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaService;
import com.example.graduation_project_group_2_mobileworld.service.khach_hang.KhachHangServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequestMapping("/add-phieu-giam-gia")
@RestController
@CrossOrigin("http://localhost:3000")
public class AddKhachHangPGGController {

    @Autowired
    private KhachHangServices khachHangService;

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @Autowired
    private PhieuGiamGiaCaNhanService phieuGiamGiaCaNhanService;


    @GetMapping("/data-kh")
    public List<KhachHang> fetchDataKH() {
        List<KhachHang> listKH = khachHangService.getAllCustomers();
        return listKH;
    }

    @GetMapping("/search-kh")
    public ResponseEntity<?> searchKHAddPgg(@RequestParam("keyword") String keyword) {
        List<KhachHang> listSearch = khachHangService.searchFormAddPgg(keyword);
        return ResponseEntity.ok(listSearch);
    }

    @PostMapping("/addPhieuGiamGia")
    public ResponseEntity<String> addPGG(@RequestBody PhieuGiamGiaDTO dtoPGG) {
        PhieuGiamGia pgg = new PhieuGiamGia();
        pgg.setMa(dtoPGG.getMa());
        pgg.setTenPhieuGiamGia(dtoPGG.getTenPhieuGiamGia());
        pgg.setLoaiPhieuGiamGia(dtoPGG.getLoaiPhieuGiamGia());
        pgg.setPhanTramGiamGia(dtoPGG.getPhanTramGiamGia());
        pgg.setSoTienGiamToiDa(dtoPGG.getSoTienGiamToiDa());
        pgg.setHoaDonToiThieu(dtoPGG.getHoaDonToiThieu());
        pgg.setSoLuongDung(dtoPGG.getSoLuongDung());
        pgg.setNgayBatDau(dtoPGG.getNgayBatDau());
        pgg.setNgayKetThuc(dtoPGG.getNgayKetThuc());
        pgg.setTrangThai(false);
        pgg.setRiengTu(dtoPGG.getRiengTu() != null ? dtoPGG.getRiengTu() == 1 : false);
        pgg.setMoTa(dtoPGG.getMoTa());
        pgg.setDeleted(false);

        phieuGiamGiaService.addPGG(pgg);
        if(dtoPGG.getRiengTu() == 1  && dtoPGG.getCustomerIds() != null) {
            for (Integer khachHangID : dtoPGG.getCustomerIds()) {
                KhachHang kh = khachHangService.findById(khachHangID);
                if(kh != null) {
                    PhieuGiamGiaCaNhan pggcn = new PhieuGiamGiaCaNhan();
                    pggcn.setIdPhieuGiamGia(pgg);
                    pggcn.setIdKhachHang(kh);
                    pggcn.setMa(dtoPGG.getMa() + "-" + khachHangID);
                    pggcn.setNgayNhan(new Date());
                    pggcn.setNgayHetHan(pgg.getNgayKetThuc());
                    pggcn.setTrangThai(true);
                    pggcn.setDeleted(false);

                    phieuGiamGiaCaNhanService.addPGGCN(pggcn);
                }
            }
        }

        return ResponseEntity.ok("Thêm thành công !");
    }

}
