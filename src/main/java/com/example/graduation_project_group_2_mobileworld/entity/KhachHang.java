package com.example.graduation_project_group_2_mobileworld.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "khach_hang")
public class KhachHang {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_tai_khoan",referencedColumnName = "id")
    private TaiKhoan idTaiKhoan;

//    @ManyToOne
//    @JoinColumn(name = "id_dia_chi_khach_hang", referencedColumnName = "id")
//    @JsonManagedReference // Phía chính sẽ được serialize
//    private DiaChiKhachHang idDiaChiKH;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ten")
    private String ten;

    @Size(max = 255)
    @Nationalized
    @Column(name = "cccd")
    private String cccd;

    @Column(name = "gioi_tinh", columnDefinition = "tinyint")
    private Short gioiTinh;

    @Column(name = "ngay_sinh")
    private Date ngaySinh;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public @Size(max = 255) String getCccd() {
        return cccd;
    }

    public void setCccd(@Size(max = 255) String cccd) {
        this.cccd = cccd;
    }

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    public KhachHang() {
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

    public boolean isDeleted() {
        return deleted;
    }

    public @Size(max = 255) String getTen() {
        return ten;
    }

    public void setTen(@Size(max = 255) String ten) {
        this.ten = ten;
    }

    public Short getGioiTinh() {
        return gioiTinh;
    }

//    public DiaChiKhachHang getIdDiaChiKH() {
//        return idDiaChiKH;
//    }
//
//    public void setIdDiaChiKH(DiaChiKhachHang idDiaChiKH) {
//        this.idDiaChiKH = idDiaChiKH;
//    }

    public void setGioiTinh(Short gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
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