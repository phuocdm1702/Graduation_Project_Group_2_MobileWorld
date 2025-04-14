package com.example.graduation_project_group_2_mobileworld.dto.gio_hang;

import java.math.BigDecimal;

public class ChiTietGioHangDTO {

    private Integer id;
    private Integer idGioHang;
    private Integer idChiTietSanPham;
    private String ma;
    private String tenSanPham; // Lấy từ ChiTietSanPham -> SanPham.ten
    private BigDecimal tongTien;

    private String imel;

    public ChiTietGioHangDTO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdGioHang() {
        return idGioHang;
    }

    public void setIdGioHang(Integer idGioHang) {
        this.idGioHang = idGioHang;
    }

    public Integer getIdChiTietSanPham() {
        return idChiTietSanPham;
    }

    public void setIdChiTietSanPham(Integer idChiTietSanPham) {
        this.idChiTietSanPham = idChiTietSanPham;
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

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public String getImel() {
        return imel;
    }

    public void setImel(String imel) {
        this.imel = imel;
    }
}
