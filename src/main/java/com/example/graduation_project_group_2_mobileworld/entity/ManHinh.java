package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "man_hinh")
public class ManHinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cong_nghe_man_hinh", referencedColumnName = "id", nullable = false)
    @NotNull
    private CongNgheManHinh idCongNgheManHinh;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "kich_thuoc", nullable = false)
    private String kichThuoc;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "do_phan_giai", nullable = false)
    private String doPhanGiai;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "do_sang_toi_da", nullable = false)
    private String doSangToiDa;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "tan_so_quet", nullable = false)
    private String tanSoQuet;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "kieu_man_hinh", nullable = false)
    private String kieuManHinh;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
}
