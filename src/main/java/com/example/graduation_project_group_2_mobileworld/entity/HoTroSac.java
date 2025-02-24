package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "ho_tro_sac")
public class HoTroSac {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ho_tro_cong_nghe_sac", nullable = false)
    private HoTroCongNgheSac idHoTroCongNgheSac;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cong_nghe_sac", nullable = false)
    private CongNgheSac idCongNgheSac;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

}