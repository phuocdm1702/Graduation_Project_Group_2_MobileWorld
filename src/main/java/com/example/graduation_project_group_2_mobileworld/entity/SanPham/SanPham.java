package com.example.graduation_project_group_2_mobileworld.entity.SanPham;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "san_pham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ten_san_pham", nullable = false)
    private String tenSanPham;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @ManyToOne
    @JoinColumn(name = "id_chi_so_khang_bui_va_nuoc")
    private ChiSoKhangBuiVaNuoc idChiSoKhangBuiVaNuoc;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_cong_nghe_mang", nullable = false)
    private CongNgheMang idCongNgheMang;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_cpu", nullable = false)
    private Cpu idCpu;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_cum_camera", nullable = false)
    private CumCamera idCumCamera;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_gpu", nullable = false)
    private Gpu idGpu;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_he_dieu_hanh", nullable = false)
    private HeDieuHanh idHeDieuHanh;

    @ManyToOne
    @JoinColumn(name = "id_ho_tro_bo_nho_ngoai")
    private HoTroBoNhoNgoai idHoTroBoNhoNgoai;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_nha_san_xuat", nullable = false)
    private NhaSanXuat idNhaSanXuat;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_pin", nullable = false)
    private Pin idPin;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_sim", nullable = false)
    private Sim idSim;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_thiet_ke", nullable = false)
    private ThietKe idThietKe;

    @ManyToOne
    @JoinColumn(name = "ho_tro_cong_nghe_sac_id")
    private HoTroCongNgheSac hoTroCongNgheSac;

    @ManyToOne
    @JoinColumn(name = "cong_nghe_man_hinh_id")
    private CongNgheManHinh congNgheManHinh;

}