package com.example.graduation_project_group_2_mobileworld.controller.DongSanPhamController;

import com.example.graduation_project_group_2_mobileworld.dto.dongSanPhamDTO.DongSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.service.dongSanPhamService.DongSanPhamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dong-san-pham")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}) // Thêm port Vue
public class DongSanPhamController {

    private final DongSanPhamService dongSanPhamService;

    public DongSanPhamController(DongSanPhamService dongSanPhamService) {
        this.dongSanPhamService = dongSanPhamService;
    }

    // Lấy danh sách tất cả dòng sản phẩm
    @GetMapping
    public List<DongSanPhamDTO> getAllDongSanPham() {
        return dongSanPhamService.getAllDongSanPham();
    }

    // Thêm mới dòng sản phẩm
    @PostMapping
    public DongSanPhamDTO createDongSanPham(@RequestBody DongSanPhamDTO dto) {
        return dongSanPhamService.createDongSanPham(dto);
    }

    // Sửa dòng sản phẩm
    @PutMapping("/{id}")
    public DongSanPhamDTO updateDongSanPham(@PathVariable Integer id, @RequestBody DongSanPhamDTO dto) {
        return dongSanPhamService.updateDongSanPham(id, dto);
    }

    // Xóa dòng sản phẩm
    @DeleteMapping("/{id}")
    public void deleteDongSanPham(@PathVariable Integer id) {
        dongSanPhamService.deleteDongSanPham(id);
    }
}
