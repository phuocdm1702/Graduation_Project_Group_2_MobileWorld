package com.example.graduation_project_group_2_mobileworld.controller.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.dto.phieuGiamGiaDTO.KhPggDTO;
import com.example.graduation_project_group_2_mobileworld.dto.phieuGiamGiaDTO.PhieuGiamGiaDTO;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaCaNhanRepository;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaService;
import com.example.graduation_project_group_2_mobileworld.service.khach_hang.KhachHangServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/phieu-giam-gia")
@CrossOrigin("http://localhost:3000")
public class PhieuGiamGiaController {

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @Autowired
    private KhachHangServices khachHangServices;

    @Autowired
    private PhieuGiamGiaCaNhanRepository phieuGiamGiaCaNhanRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @GetMapping("/data")
    public ResponseEntity<Page<PhieuGiamGia>> fetchData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<PhieuGiamGia> listPGG = phieuGiamGiaService.getPGG(pageable);
        return ResponseEntity.ok(listPGG);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchData(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PhieuGiamGia> listSearch = phieuGiamGiaService.searchData(keyword, pageable);
        return ResponseEntity.ok(listSearch);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterPhieuGiamGia(
            @RequestParam(required = false) String loaiPhieuGiamGia,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(required = false) Double minOrder,
            @RequestParam(required = false) Double valueFilter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<PhieuGiamGia> result = phieuGiamGiaService.filterPhieuGiamGia(
                    loaiPhieuGiamGia,
                    trangThai,
                    startDate,
                    endDate,
                    minOrder,
                    valueFilter,
                    pageable
            );
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> getDetail(@PathVariable Integer id) {
        try {
            PhieuGiamGiaDTO dto = phieuGiamGiaService.getDetailPGG(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update-phieu-giam-gia/{id}")
    public ResponseEntity<?> updatePGG(@PathVariable Integer id, @RequestBody PhieuGiamGiaDTO phieuGiamGiaDTO) {
        try {
            Optional<PhieuGiamGia> pggExist = phieuGiamGiaService.getById(id);
            if (!pggExist.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Phiếu giảm giá không tồn tại");
            }

            PhieuGiamGia existingPgg = pggExist.get();

            // Cập nhật các trường cơ bản
            existingPgg.setMa(phieuGiamGiaDTO.getMa());
            existingPgg.setTenPhieuGiamGia(phieuGiamGiaDTO.getTenPhieuGiamGia());
            existingPgg.setLoaiPhieuGiamGia(phieuGiamGiaDTO.getLoaiPhieuGiamGia());
            existingPgg.setPhanTramGiamGia(phieuGiamGiaDTO.getPhanTramGiamGia());
            existingPgg.setSoTienGiamToiDa(phieuGiamGiaDTO.getSoTienGiamToiDa());
            existingPgg.setHoaDonToiThieu(phieuGiamGiaDTO.getHoaDonToiThieu());
            existingPgg.setSoLuongDung(phieuGiamGiaDTO.getSoLuongDung());
            existingPgg.setNgayBatDau(phieuGiamGiaDTO.getNgayBatDau());
            existingPgg.setNgayKetThuc(phieuGiamGiaDTO.getNgayKetThuc());
            existingPgg.setMoTa(phieuGiamGiaDTO.getMoTa());
            existingPgg.setRiengTu(phieuGiamGiaDTO.getRiengTu());

            // Xử lý danh sách khách hàng qua bảng phieu_giam_gia_ca_nhan
            if (phieuGiamGiaDTO.getRiengTu() && phieuGiamGiaDTO.getCustomerIds() != null) {
                // Xóa các bản ghi cũ liên quan đến phiếu giảm giá này
                phieuGiamGiaCaNhanRepository.deleteByIdPhieuGiamGia(existingPgg);

                // Thêm các bản ghi mới
                List<KhachHang> khachHangs = khachHangRepository.findAllById(phieuGiamGiaDTO.getCustomerIds());
                for (KhachHang khachHang : khachHangs) {
                    PhieuGiamGiaCaNhan pggCaNhan = new PhieuGiamGiaCaNhan();
                    pggCaNhan.setIdPhieuGiamGia(existingPgg);
                    pggCaNhan.setIdKhachHang(khachHang);
                    pggCaNhan.setMa("PGCN_" + existingPgg.getMa() + "_" + khachHang.getMa());
                    pggCaNhan.setNgayNhan(new Date());
                    pggCaNhan.setNgayHetHan(existingPgg.getNgayKetThuc());
                    pggCaNhan.setTrangThai(false); // Chưa sử dụng
                    pggCaNhan.setDeleted(false);
                    phieuGiamGiaCaNhanRepository.save(pggCaNhan);
                }
            } else {
                // Xóa các bản ghi nếu không riêng tư
                phieuGiamGiaCaNhanRepository.deleteByIdPhieuGiamGia(existingPgg);
            }

            phieuGiamGiaService.updatePGG(existingPgg);
            PhieuGiamGiaDTO updatedDTO = phieuGiamGiaService.getDetailPGG(id);
            return ResponseEntity.ok(updatedDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi cập nhật: " + e.getMessage());
        }
    }

    @GetMapping("/customers")
    public ResponseEntity<List<KhPggDTO>> getAllCustomers() {
        List<KhachHang> khachHangs = khachHangServices.getAllCustomers();
        List<KhPggDTO> khachHangDTOs = khachHangs.stream()
                .map(kh -> new KhPggDTO(kh.getId(), kh.getMa(), kh.getTen(), kh.getNgaySinh()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(khachHangDTOs);
    }

    @PutMapping("/update-trang-thai/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> updateTrangThai(
            @PathVariable Integer id,
            @RequestBody Map<String, Boolean> requestBody) {
        Boolean trangThai = requestBody.get("trangThai");
        if (trangThai == null) {
            return ResponseEntity.badRequest().body(null);
        }
        PhieuGiamGiaDTO updatedPgg = phieuGiamGiaService.updateTrangthai(id, trangThai);
        return ResponseEntity.ok(updatedPgg);
    }
}