package com.example.graduation_project_group_2_mobileworld.controller.san_pham.chi_tiet_san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.chi_tiet_san_pham.ChiTietSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.chi_tiet_san_pham.ChiTietSanPhamFilterDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.chi_tiet_san_pham.ChiTietSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chi-tiet-san-pham")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*",
        allowCredentials = "true",
        maxAge = 3600
)
public class ChiTietSanPhamController {

    private final ChiTietSanPhamService chiTietSanPhamService;

    public ChiTietSanPhamController(ChiTietSanPhamService chiTietSanPhamService) {
        this.chiTietSanPhamService = chiTietSanPhamService;
    }

//    @GetMapping
//    public ResponseEntity<Page<ChiTietSanPhamDTO>> getAllChiTietSanPham(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size) {
//        Page<ChiTietSanPhamDTO> result = chiTietSanPhamService.getAllChiTietSanPham(page, size);
//        return ResponseEntity.ok(result);
//    }

    // Tìm kiếm và lọc
//    @GetMapping("/search")
//    public ResponseEntity<Page<ChiTietSanPhamDTO>> searchAndFilterChiTietSanPham(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) Integer idNhaSanXuat,
//            @RequestParam(required = false) Integer idMauSac,
//            @RequestParam(required = false) Integer idPin,
//            @RequestParam(required = false) Integer idManHinh,
//            @RequestParam(required = false) Integer idRam,
//            @RequestParam(required = false) Integer idBoNhoTrong,
//            @RequestParam(required = false) Integer idCpu,
//            @RequestParam(required = false) Integer idGpu,
//            @RequestParam(required = false) Integer idCumCamera,
//            @RequestParam(required = false) Integer idHeDieuHanh,
//            @RequestParam(required = false) Integer idThietKe,
//            @RequestParam(required = false) Integer idSim,
//            @RequestParam(required = false) Integer idCongSac,
//            @RequestParam(required = false) Integer idHoTroCongNgheSac,
//            @RequestParam(required = false) Integer idCongNgheMang,
//            @RequestParam(required = false) Integer idLoaiTinhTrang,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size
//    ) {
//        ChiTietSanPhamFilterDTO filterDTO = new ChiTietSanPhamFilterDTO();
//        filterDTO.setKeyword(keyword);
//        filterDTO.setIdNhaSanXuat(idNhaSanXuat);
//        filterDTO.setIdMauSac(idMauSac);
//        filterDTO.setIdPin(idPin);
//        filterDTO.setIdManHinh(idManHinh);
//        filterDTO.setIdRam(idRam);
//        filterDTO.setIdBoNhoTrong(idBoNhoTrong);
//        filterDTO.setIdCpu(idCpu);
//        filterDTO.setIdGpu(idGpu);
//        filterDTO.setIdCumCamera(idCumCamera);
//        filterDTO.setIdHeDieuHanh(idHeDieuHanh);
//        filterDTO.setIdThietKe(idThietKe);
//        filterDTO.setIdSim(idSim);
//        filterDTO.setIdCongSac(idCongSac);
//        filterDTO.setIdHoTroCongNgheSac(idHoTroCongNgheSac);
//        filterDTO.setIdCongNgheMang(idCongNgheMang);
//        filterDTO.setIdLoaiTinhTrang(idLoaiTinhTrang);
//        filterDTO.setPage(page);
//        filterDTO.setSize(size);
//
//        Page<ChiTietSanPhamDTO> result = chiTietSanPhamService.searchAndFilterChiTietSanPham(filterDTO);
//        return ResponseEntity.ok(result);
//    }

    // Lấy chi tiết sản phẩm theo ID
//    @GetMapping("/{id}")
//    public ResponseEntity<ChiTietSanPhamDTO> getChiTietSanPhamById(@PathVariable Integer id) {
//        ChiTietSanPhamDTO result = chiTietSanPhamService.getChiTietSanPhamById(id);
//        if (result != null) {
//            return ResponseEntity.ok(result);
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    // Thêm mới sản phẩm chi tiết
//    @PostMapping
//    public ResponseEntity<ChiTietSanPhamDTO> createChiTietSanPham(@RequestBody ChiTietSanPhamDTO dto) {
//        ChiTietSanPhamDTO result = chiTietSanPhamService.createChiTietSanPham(dto);
//        return ResponseEntity.ok(result);
//    }

    // Cập nhật sản phẩm chi tiết
//    @PutMapping("/{id}")
//    public ResponseEntity<ChiTietSanPhamDTO> updateChiTietSanPham(
//            @PathVariable Integer id,
//            @RequestBody ChiTietSanPhamDTO dto) {
//        ChiTietSanPhamDTO result = chiTietSanPhamService.updateChiTietSanPham(id, dto);
//        if (result != null) {
//            return ResponseEntity.ok(result);
//        }
//        return ResponseEntity.notFound().build();
//    }

    // Xóa mềm sản phẩm chi tiết
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChiTietSanPham(@PathVariable Integer id) {
        boolean deleted = chiTietSanPhamService.deleteChiTietSanPham(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Lấy danh sách thuộc tính để load lên combobox
    @GetMapping("/nha-san-xuats")
    public ResponseEntity<List<NhaSanXuat>> getAllNhaSanXuat() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllNhaSanXuat());
    }

//    @GetMapping("/dong-san-phams")
//    public ResponseEntity<List<DongSanPham>> getAllDongSanPham() {
//        return ResponseEntity.ok(chiTietSanPhamService.getAllDongSanPham());
//    }

    @GetMapping("/mau-sacs")
    public ResponseEntity<List<MauSac>> getAllMauSac() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllMauSac());
    }

    @GetMapping("/pins")
    public ResponseEntity<List<Pin>> getAllPin() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllPin());
    }

    @GetMapping("/man-hinhs")
    public ResponseEntity<List<ManHinh>> getAllManHinh() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllManHinh());
    }

    @GetMapping("/rams")
    public ResponseEntity<List<Ram>> getAllRam() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllRam());
    }

    @GetMapping("/bo-nho-trongs")
    public ResponseEntity<List<BoNhoTrong>> getAllBoNhoTrong() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllBoNhoTrong());
    }

    @GetMapping("/cpus")
    public ResponseEntity<List<Cpu>> getAllCpu() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllCpu());
    }

    @GetMapping("/gpus")
    public ResponseEntity<List<Gpu>> getAllGpu() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllGpu());
    }

    @GetMapping("/cum-cameras")
    public ResponseEntity<List<CumCamera>> getAllCumCamera() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllCumCamera());
    }

    @GetMapping("/he-dieu-hanhs")
    public ResponseEntity<List<HeDieuHanh>> getAllHeDieuHanh() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllHeDieuHanh());
    }

    @GetMapping("/thiet-kes")
    public ResponseEntity<List<ThietKe>> getAllThietKe() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllThietKe());
    }

    @GetMapping("/sims")
    public ResponseEntity<List<Sim>> getAllSim() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllSim());
    }

    @GetMapping("/cong-sacs")
    public ResponseEntity<List<CongSac>> getAllCongSac() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllCongSac());
    }

    @GetMapping("/ho-tro-cong-nghe-sacs")
    public ResponseEntity<List<HoTroCongNgheSac>> getAllHoTroCongNgheSac() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllHoTroCongNgheSac());
    }

    @GetMapping("/cong-nghe-mangs")
    public ResponseEntity<List<CongNgheMang>> getAllCongNgheMang() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllCongNgheMang());
    }

    @GetMapping("/tinh-trangs")
    public ResponseEntity<List<TinhTrang>> getAllTinhTrang() {
        return ResponseEntity.ok(chiTietSanPhamService.getAllTinhTrang());
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteBulkChiTietSanPham(@RequestBody List<Integer> ids) {
        ids.forEach(id -> chiTietSanPhamService.deleteChiTietSanPham(id));
        return ResponseEntity.noContent().build();
    }
}