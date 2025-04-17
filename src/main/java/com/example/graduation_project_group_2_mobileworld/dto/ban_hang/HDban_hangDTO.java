package com.example.graduation_project_group_2_mobileworld.dto.ban_hang;

import java.util.List;

public class HDban_hangDTO {
    private Integer id;
    private String maHoaDon;
    private Short trangThai;
    private List<HDCTban_hangDTO> items;

    public HDban_hangDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public Short getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Short trangThai) {
        this.trangThai = trangThai;
    }

    public List<HDCTban_hangDTO> getItems() {
        return items;
    }

    public void setItems(List<HDCTban_hangDTO> items) {
        this.items = items;
    }
}