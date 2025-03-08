package com.example.graduation_project_group_2_mobileworld.dto.nhan_vien;

import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import jakarta.persistence.ManyToOne;

import java.util.Date;

public class NhanVienDTO {
   private  String Manv;
   @ManyToOne
   private TaiKhoan tk;
   private Date Ngaythamgia;
   private boolean Deleted;

    public NhanVienDTO(String manv, TaiKhoan tk, Date ngaythamgia, boolean deleted) {
        Manv = manv;
        this.tk = tk;
        Ngaythamgia = ngaythamgia;
        Deleted = deleted;
    }

    public String getManv() {
        return Manv;
    }

    public void setManv(String manv) {
        Manv = manv;
    }

    public TaiKhoan getTk() {
        return tk;
    }

    public void setTk(TaiKhoan tk) {
        this.tk = tk;
    }

    public Date getNgaythamgia() {
        return Ngaythamgia;
    }

    public void setNgaythamgia(Date ngaythamgia) {
        Ngaythamgia = ngaythamgia;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }
}

