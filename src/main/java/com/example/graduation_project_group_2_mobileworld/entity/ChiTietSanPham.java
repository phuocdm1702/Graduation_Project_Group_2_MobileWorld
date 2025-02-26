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

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_san_pham")
public class ChiTietSanPham {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_imel", referencedColumnName = "id")
    private Imel idImel;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_anh_san_pham", referencedColumnName = "id")
    private AnhSanPham idAnhSanPham;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_nha_san_xuat", referencedColumnName = "id")
    private NhaSanXuat idNhaSanXuat;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_dong_san_pham", referencedColumnName = "id")
    private DongSanPham idDongSanPham;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_mau_sac", referencedColumnName = "id")
    private MauSac idMauSac;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_pin", referencedColumnName = "id")
    private Pin idPin;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_man_hinh", referencedColumnName = "id")
    private ManHinh idManHinh;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_ram", referencedColumnName = "id")
    private Ram idRam;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_bo_nho_trong", referencedColumnName = "id")
    private BoNhoTrong idBoNhoTrong;

    @ManyToOne
    @JoinColumn(name = "id_ho_tro_bo_nho_ngoai",referencedColumnName = "id")
    private HoTroBoNhoNgoai idHoTroBoNhoNgoai;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_cpu", referencedColumnName = "id")
    private Cpu idCpu;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_gpu", referencedColumnName = "id")
    private Gpu idGpu;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_cum_camera",referencedColumnName = "id")
    private CumCamera idCumCamera;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_he_dieu_hanh", referencedColumnName = "id")
    private HeDieuHanh idHeDieuHanh;

    @ManyToOne
    @JoinColumn(name = "id_chi_so_khang_bui_va_nuoc",referencedColumnName = "id")
    private ChiSoKhangBuiVaNuoc idChiSoKhangBuiVaNuoc;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_thiet_ke", referencedColumnName = "id")
    private ThietKe idThietKe;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_sim", referencedColumnName = "id")
    private Sim idSim;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_cong_sac", referencedColumnName = "id")
    private CongSac idCongSac;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_ho_tro_cong_nghe_sac", referencedColumnName = "id")
    private HoTroCongNgheSac idHoTroCongNgheSac;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_cong_nghe_mang", referencedColumnName = "id")
    private CongNgheMang idCongNgheMang;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_loai_tinh_trang", referencedColumnName = "id")
    private TinhTrang idLoaiTinhTrang;

    @Size(max = 255)
    @Nationalized
    @Column(name = "tien_ich_dac_biet")
    private String tienIchDacBiet;

    @NotNull
    @Column(name = "gia_ban", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaBan;

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