package com.example.graduation_project_group_2_mobileworld.dto.thongKe;

import java.math.BigDecimal;

public class SoLieuDTO {
    private BigDecimal doanhThu;
    private BigDecimal sanPhamDaBan;
    private Integer tongSoDonHang;

    public SoLieuDTO() {
    }

    public SoLieuDTO(BigDecimal doanhThu, BigDecimal sanPhamDaBan, Integer tongSoDonHang) {
        this.doanhThu = doanhThu;
        this.sanPhamDaBan = sanPhamDaBan;
        this.tongSoDonHang = tongSoDonHang;
    }

    public BigDecimal getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(BigDecimal doanhThu) {
        this.doanhThu = doanhThu;
    }

    public BigDecimal getSanPhamDaBan() {
        return sanPhamDaBan;
    }

    public void setSanPhamDaBan(BigDecimal sanPhamDaBan) {
        this.sanPhamDaBan = sanPhamDaBan;
    }

    public Integer getTongSoDonHang() {
        return tongSoDonHang;
    }

    public void setTongSoDonHang(Integer tongSoDonHang) {
        this.tongSoDonHang = tongSoDonHang;
    }
}
