package com.example.graduation_project_group_2_mobileworld.dto.thongKe;

public class SanPhamHetHangDTO {
    private String tenSanPham;
    private Long soLuong;

    public SanPhamHetHangDTO(String tenSanPham, Long soLuong) {
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
    }

    // Getters và Setters
    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public Long getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Long soLuong) {
        this.soLuong = soLuong;
    }
}
