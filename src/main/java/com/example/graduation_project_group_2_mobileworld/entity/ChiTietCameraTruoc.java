package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_camera_truoc")
public class ChiTietCameraTruoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cum_camera", nullable = false)
    private CumCamera cumCamera;

    @ManyToOne
    @JoinColumn(name = "id_thong_so_camera_truoc", nullable = false)
    private ThongSoCameraTruoc thongSoCameraTruoc;

    private String ma;

    private Boolean deleted = false;
}