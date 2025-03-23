package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "thong_so_camera_sau")
public class ThongSoCameraSau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ma;

    @Column(name = "thong_so_camera")
    private String thongSoCamera;

    private Boolean deleted = false;
}