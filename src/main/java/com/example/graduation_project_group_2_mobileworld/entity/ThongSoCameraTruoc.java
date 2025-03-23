package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "thong_so_camera_truoc")
public class ThongSoCameraTruoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ma;

    @Column(name = "thong_so_cam_truoc")
    private String thongSoCamTruoc;

    private Boolean deleted = false;
}