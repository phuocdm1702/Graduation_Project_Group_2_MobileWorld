package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "nhan_vien")
public class NhanVien {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_tai_khoan", referencedColumnName = "id")
    private TaiKhoan idTaiKhoan;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ten_nhan_vien")
    private String tenNhanVien;

    @Column(name = "ngay_sinh")
    private Instant ngaySinh;

    @Size(max = 255)
    @Nationalized
    @Column(name = "anh_nhan_vien")
    private String anhNhanVien;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;

    @Size(max = 255)
    @Nationalized
    @Column(name = "thanh_pho")
    private String thanhPho;

    @Size(max = 255)
    @Nationalized
    @Column(name = "quan")
    private String quan;

    @Size(max = 255)
    @Nationalized
    @Column(name = "phuong")
    private String phuong;

    @Size(max = 255)
    @Nationalized
    @Column(name = "dia_chi_cu_the")
    private String diaChiCuThe;

    @Size(max = 255)
    @Nationalized
    @Column(name = "cccd")
    private String cccd;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    public NhanVien() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaiKhoan getIdTaiKhoan() {
        return idTaiKhoan;
    }

    public void setIdTaiKhoan(TaiKhoan idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public @Size(max = 255) String getMa() {
        return ma;
    }

    public void setMa(@Size(max = 255) String ma) {
        this.ma = ma;
    }

    public @Size(max = 255) String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(@Size(max = 255) String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public Instant getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Instant ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public @Size(max = 255) String getAnhNhanVien() {
        return anhNhanVien;
    }

    public void setAnhNhanVien(@Size(max = 255) String anhNhanVien) {
        this.anhNhanVien = anhNhanVien;
    }

    public @Size(max = 255) String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(@Size(max = 255) String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public @Size(max = 255) String getThanhPho() {
        return thanhPho;
    }

    public void setThanhPho(@Size(max = 255) String thanhPho) {
        this.thanhPho = thanhPho;
    }

    public @Size(max = 255) String getQuan() {
        return quan;
    }

    public void setQuan(@Size(max = 255) String quan) {
        this.quan = quan;
    }

    public @Size(max = 255) String getPhuong() {
        return phuong;
    }

    public void setPhuong(@Size(max = 255) String phuong) {
        this.phuong = phuong;
    }

    public @Size(max = 255) String getDiaChiCuThe() {
        return diaChiCuThe;
    }

    public void setDiaChiCuThe(@Size(max = 255) String diaChiCuThe) {
        this.diaChiCuThe = diaChiCuThe;
    }

    public @Size(max = 255) String getCccd() {
        return cccd;
    }

    public void setCccd(@Size(max = 255) String cccd) {
        this.cccd = cccd;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
}