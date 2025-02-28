package com.example.graduation_project_group_2_mobileworld.dto.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class HoaDonDTO {

    private KhachHang idKhachHang;
    private NhanVien idNhanVien;
    private PhieuGiamGia idPhieuGiamGia;
    private String ma;
    private String soDienThoaiKhachHang;
    private String loaiDon;
    private BigDecimal tongTien;
    private Date ngayTao;
    private BigDecimal tongTienSauGiam;
    private Short trangThai;

    public HoaDonDTO( KhachHang idKhachHang, NhanVien idNhanVien, PhieuGiamGia idPhieuGiamGia, String ma, String soDienThoaiKhachHang, String loaiDon, BigDecimal tongTien, Date ngayTao, BigDecimal tongTienSauGiam, Short trangThai) {
        this.idKhachHang = idKhachHang;
        this.idNhanVien = idNhanVien;
        this.idPhieuGiamGia = idPhieuGiamGia;
        this.ma = ma;
        this.soDienThoaiKhachHang = soDienThoaiKhachHang;
        this.loaiDon = loaiDon;
        this.tongTien = tongTien;
        this.ngayTao = ngayTao;
        this.tongTienSauGiam = tongTienSauGiam;
        this.trangThai = trangThai;
    }
}
