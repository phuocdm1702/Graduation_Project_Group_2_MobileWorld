package com.example.graduation_project_group_2_mobileworld.dto.phieuGiamGiaDTO;

import java.util.Date;

public class KhPggDTO {
    private Integer id;
    private String ma;
    private String ten; // Đổi từ tenKH thành ten
    private Date ngaySinh;
    private String anhKhachHang;
    private String thanhPho;
    private String quan;
    private String phuong;
    private String diaChiCuThe;
    private String cccd;
    private String email;
    private String soDienThoai;
    private String userName;
    private Boolean gioiTinh;
    private Date createdAt;
    private Boolean macDinh;

    public KhPggDTO() {

    }

    public KhPggDTO(Integer id, String ma, String ten, Date ngaySinh) {
        this.id = id;
        this.ma = ma;
        this.ten = ten;
        this.ngaySinh = ngaySinh;
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

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }


}
