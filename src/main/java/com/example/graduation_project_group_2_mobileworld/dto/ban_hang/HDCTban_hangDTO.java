package com.example.graduation_project_group_2_mobileworld.dto.ban_hang;

import java.math.BigDecimal;

public class HDCTban_hangDTO {
    private Integer id;
    private String tenSanPham;
    private String imei;
    private BigDecimal giaBan;

    public HDCTban_hangDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }
}