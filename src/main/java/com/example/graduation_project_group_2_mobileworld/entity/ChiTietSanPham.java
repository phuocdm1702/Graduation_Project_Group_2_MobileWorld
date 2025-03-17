package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_san_pham")
public class ChiTietSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_imel", nullable = false)
    private Imel idImel;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_san_pham", nullable = false)
    private SanPham idSanPham;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_anh_san_pham", nullable = false)
    private AnhSanPham idAnhSanPham;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_mau_sac", nullable = false)
    private MauSac idMauSac;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_ram", nullable = false)
    private Ram idRam;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_bo_nho_trong", nullable = false)
    private BoNhoTrong idBoNhoTrong;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_loai_tinh_trang", nullable = false)
    private TinhTrang idLoaiTinhTrang;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @Size(max = 255)
    @Nationalized
    @Column(name = "tien_ich_dac_biet")
    private String tienIchDacBiet;

    @NotNull
    @Column(name = "gia_ban", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaBan;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

}