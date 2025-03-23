package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_camera_sau")
public class ChiTietCameraSau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cum_camera", nullable = false)
    private CumCamera cumCamera;

    @ManyToOne
    @JoinColumn(name = "id_thong_so_camera_sau", nullable = false)
    private ThongSoCameraSau thongSoCameraSau;

    private String ma;

    private Boolean deleted = false;
}