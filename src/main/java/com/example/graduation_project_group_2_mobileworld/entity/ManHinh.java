package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "man_hinh")
public class ManHinh {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cong_nghe_man_hinh")
    private CongNgheManHinh idCongNgheManHinh;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Size(max = 255)
    @Nationalized
    @Column(name = "kich_thuoc")
    private String kichThuoc;

    @Size(max = 255)
    @Nationalized
    @Column(name = "do_phan_giai")
    private String doPhanGiai;

    @Size(max = 255)
    @Nationalized
    @Column(name = "do_sang_toi_da")
    private String doSangToiDa;

    @Size(max = 255)
    @Nationalized
    @Column(name = "tan_so_quet")
    private String tanSoQuet;

    @Size(max = 255)
    @Nationalized
    @Column(name = "kieu_man_hinh")
    private String kieuManHinh;

    @Column(name = "deleted")
    private Boolean deleted;

}