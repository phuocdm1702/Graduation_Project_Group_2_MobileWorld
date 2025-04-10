package com.example.graduation_project_group_2_mobileworld.entity;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_gio_hang")
public class ChiTietGioHang {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_gio_hang", nullable = false)
    private GioHang idGioHang;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_chi_tiet_san_pham", nullable = false)
    private ChiTietSanPham idChiTietSanPham;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @Column(name = "trang_thai", columnDefinition = "tinyint not null")
    private Short trangThai;

    @NotNull
    @Column(name = "tong_tien", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTien;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

}