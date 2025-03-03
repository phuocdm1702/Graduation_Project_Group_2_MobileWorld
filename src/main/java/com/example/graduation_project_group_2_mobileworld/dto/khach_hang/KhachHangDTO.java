package com.example.graduation_project_group_2_mobileworld.dto.khach_hang;

import java.util.Date;

public class KhachHangDTO {
    private Integer idTaiKhoan;
    private String ma;
    private String ten;
    private Short gioiTinh;
    private Date ngaySinh;

    public KhachHangDTO() {
    }

    public Integer getIdTaiKhoan() {
        return idTaiKhoan;
    }

    public void setIdTaiKhoan(Integer idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Short getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(Short gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
}
