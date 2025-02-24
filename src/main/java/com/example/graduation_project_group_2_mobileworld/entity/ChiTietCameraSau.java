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
@Table(name = "chi_tiet_camera_sau")
public class ChiTietCameraSau {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cum_camera", nullable = false)
    private CumCamera idCumCamera;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_thong_so_camera_sau", nullable = false)
    private ThongSoCameraSau idThongSoCameraSau;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

}