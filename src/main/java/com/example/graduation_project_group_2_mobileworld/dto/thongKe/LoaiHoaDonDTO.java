package com.example.graduation_project_group_2_mobileworld.dto.thongKe;

public class LoaiHoaDonDTO {
    private String loaiDon;
    private Long soLuong;

    public LoaiHoaDonDTO(String loaiDon, Long soLuong) {
        this.loaiDon = loaiDon;
        this.soLuong = soLuong;
    }

    // Getters và Setters
    public String getLoaiDon() { return loaiDon; }
    public void setLoaiDon(String loaiDon) { this.loaiDon = loaiDon; }
    public Long getSoLuong() { return soLuong; }
    public void setSoLuong(Long soLuong) { this.soLuong = soLuong; }
}
