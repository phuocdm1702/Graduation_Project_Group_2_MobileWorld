package com.example.graduation_project_group_2_mobileworld.entity.SanPham;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cong_nghe_man_hinh")
public class CongNgheManHinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "cong_nghe_man_hinh", nullable = false)
    private String congNgheManHinh;

    @Size(max = 255)
    @Nationalized
    @Column(name = "chuan_man_hinh")
    private String chuanManHinh;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Size(max = 50)
    @Nationalized
    @Column(name = "kich_thuoc", length = 50)
    private String kichThuoc;

    @Size(max = 50)
    @Nationalized
    @Column(name = "do_phan_giai", length = 50)
    private String doPhanGiai;

    @Size(max = 50)
    @Nationalized
    @Column(name = "do_sang_toi_da", length = 50)
    private String doSangToiDa;

    @Size(max = 50)
    @Nationalized
    @Column(name = "tan_so_quet", length = 50)
    private String tanSoQuet;

    @Size(max = 50)
    @Nationalized
    @Column(name = "kieu_man_hinh", length = 50)
    private String kieuManHinh;

    @OneToMany(mappedBy = "congNgheManHinh")
    private Set<SanPham> sanPhams = new LinkedHashSet<>();

}