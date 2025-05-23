package com.example.graduation_project_group_2_mobileworld.dto.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ImelDaBan;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class HoaDonChiTietDTO {
    private Integer id;
    private Integer idHoaDon;
    private ChiTietSanPham idChiTietSanPham;
    private ImelDaBan idImelDaBan;
    private String ma;
    private BigDecimal gia;
    private Short trangThai;
    private String ghiChu;
    private Integer stt; // Số thứ tự tự tăng
    private String tenSanPham; // Tên sản phẩm
    private String imel; // IMEI
    private String mauSac; // mauSac
    private String boNho; // nhoBo

    public HoaDonChiTietDTO() {

    }

    public HoaDonChiTietDTO(Integer id, Integer idHoaDon, ChiTietSanPham idChiTietSanPham, ImelDaBan idImelDaBan, String ma, BigDecimal gia, Short trangThai, String ghiChu, Integer stt, String tenSanPham, String imel, String mauSac, String boNho) {
        this.id = id;
        this.idHoaDon = idHoaDon;
        this.idChiTietSanPham = idChiTietSanPham;
        this.idImelDaBan = idImelDaBan;
        this.ma = ma;
        this.gia = gia;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
        this.stt = stt;
        this.tenSanPham = tenSanPham;
        this.imel = imel;
        this.mauSac = mauSac;
        this.boNho = boNho;
    }
}
