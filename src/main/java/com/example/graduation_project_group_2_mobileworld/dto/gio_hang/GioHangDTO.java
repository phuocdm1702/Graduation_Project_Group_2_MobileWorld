package com.example.graduation_project_group_2_mobileworld.dto.gio_hang;

import java.util.List;

public class GioHangDTO {

    private Integer id;
    private String ma;
    private Integer idKhachHang;
    private List<ChiTietGioHangDTO> chiTietGioHangs;

    public GioHangDTO(){

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

    public List<ChiTietGioHangDTO> getChiTietGioHangs() {
        return chiTietGioHangs;
    }

    public void setChiTietGioHangs(List<ChiTietGioHangDTO> chiTietGioHangs) {
        this.chiTietGioHangs = chiTietGioHangs;
    }
}
