package com.example.graduation_project_group_2_mobileworld.dto.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.entity.ImelDaBan;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class HoaDonChiTietDTO {
    private HoaDon idHoaDon;
    private ChiTietSanPham idChiTietSanPham;
    private ImelDaBan idImelDaBan;
    private String ma;
    private BigDecimal gia;
    private Short trangThai;
    private String ghiChu;

    public HoaDonChiTietDTO(HoaDon idHoaDon, ChiTietSanPham idChiTietSanPham, ImelDaBan idImelDaBan, String ma, BigDecimal gia, Short trangThai, String ghiChu) {
        this.idHoaDon = idHoaDon;
        this.idChiTietSanPham = idChiTietSanPham;
        this.idImelDaBan = idImelDaBan;
        this.ma = ma;
        this.gia = gia;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }
}
