package com.example.graduation_project_group_2_mobileworld.dto.gio_hang;

import java.util.List;

public class GioHangDTO {
    private Integer id;
    private String ma;
    private Integer idKhachHang;
    private Short trangThai;
    private List<ChiTietGioHangDTO> chiTietGioHangList;

    public GioHangDTO() {
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

    public Integer getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(Integer idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public Short getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Short trangThai) {
        this.trangThai = trangThai;
    }

    public List<ChiTietGioHangDTO> getChiTietGioHangList() {
        return chiTietGioHangList;
    }

    public void setChiTietGioHangList(List<ChiTietGioHangDTO> chiTietGioHangList) {
        this.chiTietGioHangList = chiTietGioHangList;
    }
}