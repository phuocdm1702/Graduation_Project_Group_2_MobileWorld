package com.example.graduation_project_group_2_mobileworld.dto.gio_hang;

import java.math.BigDecimal;

public class ChiTietSPDTO {

    private Integer id;
    private String ma;
    private String tenSanPham; // Lấy từ SanPham.ten
    private BigDecimal giaBan;
    private String imel;

    public ChiTietSPDTO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    public String getImel() {
        return imel;
    }

    public void setImel(String imel) {
        this.imel = imel;
    }
}
