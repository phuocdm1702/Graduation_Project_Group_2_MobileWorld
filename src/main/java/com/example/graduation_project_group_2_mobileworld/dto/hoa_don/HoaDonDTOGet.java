package com.example.graduation_project_group_2_mobileworld.dto.hoa_don;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class HoaDonDTOGet {
    private String ma;
    private String maNhanVien;
    private String tenKhachHang;
    private String soDienThoaiKhachHang;
    private BigDecimal tongTienSauGiam;
    private Double phanTramGiamGia;
    private BigDecimal phiVanChuyen;
    private Date ngayTao;
    private String loaiDon;
    private Short trangThai;

    // Constructor mặc định
    public HoaDonDTOGet() {
    }

    // Constructor khớp với truy vấn @Query
    public HoaDonDTOGet(String ma, String maNhanVien, String tenKhachHang, String soDienThoaiKhachHang,
                        BigDecimal tongTienSauGiam, Double phanTramGiamGia, BigDecimal phiVanChuyen,
                        Date ngayTao, String loaiDon, Short trangThai) {
        this.ma = ma;
        this.maNhanVien = maNhanVien;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoaiKhachHang = soDienThoaiKhachHang;
        this.tongTienSauGiam = tongTienSauGiam;
        this.phanTramGiamGia = phanTramGiamGia;
        this.phiVanChuyen = phiVanChuyen;
        this.ngayTao = ngayTao;
        this.loaiDon = loaiDon;
        this.trangThai = trangThai;
    }
}