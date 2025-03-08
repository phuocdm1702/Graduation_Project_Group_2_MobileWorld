package com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia;

import com.example.graduation_project_group_2_mobileworld.entity.*;

import java.math.BigDecimal;

public class viewCTSPDTO {
    DongSanPham dsp;
    ChiTietSanPham ctsp;
    AnhSanPham anh;
    ChiTietDotGiamGia ctgg;
    BoNhoTrong bnt;

    BigDecimal giaSauKhiGiam;

    public viewCTSPDTO() {
    }

    public viewCTSPDTO(DongSanPham dsp, ChiTietSanPham ctsp, AnhSanPham anh, BoNhoTrong bnt) {
        this.dsp = dsp;
        this.ctsp = ctsp;
        this.ctgg = ctgg;
        this.anh = anh;
        this.bnt = bnt;
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

    public ChiTietDotGiamGia getCtgg() {
        return ctgg;
    }

    public void setCtgg(ChiTietDotGiamGia ctgg) {
        this.ctgg = ctgg;
    }

    public BoNhoTrong getBnt() {
        return bnt;
    }

    public void setBnt(BoNhoTrong bnt) {
        this.bnt = bnt;
    }

    public BigDecimal getGiaSauKhiGiam() {
        return giaSauKhiGiam;
    }

    public void setGiaSauKhiGiam(BigDecimal giaSauKhiGiam) {
        this.giaSauKhiGiam = giaSauKhiGiam;
    }
}
