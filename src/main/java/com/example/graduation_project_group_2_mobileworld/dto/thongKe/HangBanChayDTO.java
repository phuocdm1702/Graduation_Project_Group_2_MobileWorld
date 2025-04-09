package com.example.graduation_project_group_2_mobileworld.dto.thongKe;

import java.math.BigDecimal;

public class HangBanChayDTO {
    private String nhaSanXuat;
    private BigDecimal doanhThu;

    public HangBanChayDTO(String nhaSanXuat, BigDecimal doanhThu) {
        this.nhaSanXuat = nhaSanXuat;
        this.doanhThu = doanhThu;
    }

    // Getters và Setters
    public String getNhaSanXuat() { return nhaSanXuat; }
    public void setNhaSanXuat(String nhaSanXuat) { this.nhaSanXuat = nhaSanXuat; }
    public BigDecimal getDoanhThu() { return doanhThu; }
    public void setDoanhThu(BigDecimal doanhThu) { this.doanhThu = doanhThu; }
}