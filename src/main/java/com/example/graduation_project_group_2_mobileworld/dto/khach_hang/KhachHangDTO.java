package com.example.graduation_project_group_2_mobileworld.dto.khach_hang;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class KhachHangDTO {
    private String ma;
    private String tenKH;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngaySinh;
    private String anhKH;
    private String thanhPho;
    private String quan;
    private String phuong;
    private String diaChiCuThe;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    private String cccd;
    private String email;
    private String soDienThoai;
    private String userName;
    private Boolean gioiTinh;
    private Date createdAt;
    public Boolean getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(Boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public KhachHangDTO() {
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getAnhKH() {
        return anhKH;
    }

    public void setAnhKH(String anhKH) {
        this.anhKH = anhKH;
    }

    public String getThanhPho() {
        return thanhPho;
    }

    public void setThanhPho(String thanhPho) {
        this.thanhPho = thanhPho;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getPhuong() {
        return phuong;
    }

    public void setPhuong(String phuong) {
        this.phuong = phuong;
    }

    public String getDiaChiCuThe() {
        return diaChiCuThe;
    }

    public void setDiaChiCuThe(String diaChiCuThe) {
        this.diaChiCuThe = diaChiCuThe;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}
