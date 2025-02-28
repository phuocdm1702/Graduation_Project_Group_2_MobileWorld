package com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia;

import java.math.BigDecimal;
import java.util.Date;

public class dot_giam_gia_DTO {
    private Integer id;
    private String ma;
    private String tenDotGiamGia;
    private String loaiGiamGiaApDung;
    private BigDecimal giaTriGiamGia;
    private BigDecimal soTienGiamToiDa;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private Boolean trangThai = false;
    private Boolean deleted = false;

    public dot_giam_gia_DTO() {
    }

    public dot_giam_gia_DTO(Integer id, String ma, String tenDotGiamGia, String loaiGiamGiaApDung, BigDecimal giaTriGiamGia, BigDecimal soTienGiamToiDa, Date ngayBatDau, Date ngayKetThuc, Boolean trangThai, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.tenDotGiamGia = tenDotGiamGia;
        this.loaiGiamGiaApDung = loaiGiamGiaApDung;
        this.giaTriGiamGia = giaTriGiamGia;
        this.soTienGiamToiDa = soTienGiamToiDa;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
        this.deleted = deleted;
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

    public String getTenDotGiamGia() {
        return tenDotGiamGia;
    }

    public void setTenDotGiamGia(String tenDotGiamGia) {
        this.tenDotGiamGia = tenDotGiamGia;
    }

    public String getLoaiGiamGiaApDung() {
        return loaiGiamGiaApDung;
    }

    public void setLoaiGiamGiaApDung(String loaiGiamGiaApDung) {
        this.loaiGiamGiaApDung = loaiGiamGiaApDung;
    }

    public BigDecimal getGiaTriGiamGia() {
        return giaTriGiamGia;
    }

    public void setGiaTriGiamGia(BigDecimal giaTriGiamGia) {
        this.giaTriGiamGia = giaTriGiamGia;
    }

    public BigDecimal getSoTienGiamToiDa() {
        return soTienGiamToiDa;
    }

    public void setSoTienGiamToiDa(BigDecimal soTienGiamToiDa) {
        this.soTienGiamToiDa = soTienGiamToiDa;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
