package com.example.graduation_project_group_2_mobileworld.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "dia_chi_khach_hang")
public class DiaChiKhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_khach_hang", nullable = false)
    @JsonBackReference // Phía này sẽ không được serialize để tránh vòng lặp
    private KhachHang idKhachHang;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "thanh_pho", nullable = false)
    private String thanhPho;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "quan", nullable = false)
    private String quan;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "phuong", nullable = false)
    private String phuong;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "dia_chi_cu_the", nullable = false)
    private String diaChiCuThe;

    @NotNull
    @Column(name = "mac_dinh", nullable = false)
    private Boolean macDinh = false;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

}