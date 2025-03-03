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

}