package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "dot_giam_gia")
public class DotGiamGia {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ten_dot_giam_gia", nullable = false)
    private String tenDotGiamGia;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "loai_giam_gia_ap_dung", nullable = false)
    private String loaiGiamGiaApDung;

    @NotNull
    @Column(name = "gia_tri_giam_gia", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTriGiamGia;

    @NotNull
    @Column(name = "so_tien_giam_toi_da", nullable = false, precision = 18, scale = 2)
    private BigDecimal soTienGiamToiDa;

    @NotNull
    @Column(name = "ngay_bat_dau", nullable = false)
    private Instant ngayBatDau;

    @NotNull
    @Column(name = "ngay_ket_thuc", nullable = false)
    private Instant ngayKetThuc;

    @NotNull
    @Column(name = "trang_thai", nullable = false)
    private Boolean trangThai = false;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;



}