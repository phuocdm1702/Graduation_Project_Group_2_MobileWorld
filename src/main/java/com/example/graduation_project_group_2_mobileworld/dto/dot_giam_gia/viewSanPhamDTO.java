package com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia;

import com.example.graduation_project_group_2_mobileworld.entity.HeDieuHanh;
import com.example.graduation_project_group_2_mobileworld.entity.NhaSanXuat;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham;

public class viewSanPhamDTO {
    SanPham sp;
    NhaSanXuat nsx;
    HeDieuHanh hdh;
    long soLuongCTSP;

    public viewSanPhamDTO() {
    }

    public viewSanPhamDTO(SanPham sp, NhaSanXuat nsx, HeDieuHanh hdh, long soLuongCTSP) {
        this.sp = sp;
        this.nsx = nsx;
        this.hdh = hdh;
        this.soLuongCTSP = soLuongCTSP;
    }

    public Long getSoLuongCTSP() {
        return soLuongCTSP;
    }

    public void setSoLuongCTSP(Long soLuongCTSP) {
        this.soLuongCTSP = soLuongCTSP;
    }

    public SanPham getSp() {
        return sp;
    }

    public void setSp(SanPham sp) {
        this.sp = sp;
    }

    public NhaSanXuat getNsx() {
        return nsx;
    }

    public void setNsx(NhaSanXuat nsx) {
        this.nsx = nsx;
    }

    public HeDieuHanh getHdh() {
        return hdh;
    }

    public void setHdh(HeDieuHanh hdh) {
        this.hdh = hdh;
    }
}
