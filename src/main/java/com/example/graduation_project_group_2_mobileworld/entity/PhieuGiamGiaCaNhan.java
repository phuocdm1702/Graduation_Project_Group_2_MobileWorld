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
@Table(name = "phieu_giam_gia_ca_nhan")
public class PhieuGiamGiaCaNhan {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_phieu_giam_gia")
    private PhieuGiamGia idPhieuGiamGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang idKhachHang;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Column(name = "ngay_nhan")
    private Instant ngayNhan;

    @Column(name = "ngay_het_han")
    private Instant ngayHetHan;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "deleted")
    private Boolean deleted;

}