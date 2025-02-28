package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_dot_giam_gia")
public class ChiTietDotGiamGia {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dot_giam_gia", nullable = false)
    private DotGiamGia dotGiamGia;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dong_san_pham", nullable = false)
    private DongSanPham idDongSanPham;

    @NotNull
    @Column(name="ma", nullable = false)
    private String ma;

    @NotNull
    @Column(name = "gia_ban_dau", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaBanDau;

    @NotNull
    @Column(name = "gia_sau_khi_giam", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaSauKhiGiam;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;


}