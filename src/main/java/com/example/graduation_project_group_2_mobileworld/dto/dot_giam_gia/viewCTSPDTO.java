package com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia;

import com.example.graduation_project_group_2_mobileworld.entity.AnhSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.ChiTietDotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;

import java.math.BigDecimal;

public class viewCTSPDTO {
    DongSanPham dsp;
    ChiTietSanPham ctsp;
    AnhSanPham anh;
    ChiTietDotGiamGia ctgg;

    BigDecimal giaSauKhiGiam;

    public viewCTSPDTO() {
    }

    public viewCTSPDTO(DongSanPham dsp, ChiTietSanPham ctsp, AnhSanPham anh) {
        this.dsp = dsp;
        this.ctsp = ctsp;
        this.anh = anh;
    }

    public DongSanPham getDsp() {
        return dsp;
    }

    public void setDsp(DongSanPham dsp) {
        this.dsp = dsp;
    }

    public ChiTietSanPham getCtsp() {
        return ctsp;
    }

    public void setCtsp(ChiTietSanPham ctsp) {
        this.ctsp = ctsp;
    }

    public AnhSanPham getAnh() {
        return anh;
    }

    public void setAnh(AnhSanPham anh) {
        this.anh = anh;
    }

    public BigDecimal getGiaSauKhiGiam() {
        return giaSauKhiGiam;
    }

    public void setGiaSauKhiGiam(BigDecimal giaSauKhiGiam) {
        this.giaSauKhiGiam = giaSauKhiGiam;
    }
}
