package com.example.graduation_project_group_2_mobileworld.entity.SanPham;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cum_camera")
public class CumCamera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ma;

    private Boolean deleted = false;

    @Size(max = 255)
    @Nationalized
    @Column(name = "thong_so_camera_sau")
    private String thongSoCameraSau;

    @Size(max = 255)
    @Nationalized
    @Column(name = "thong_so_camera_truoc")
    private String thongSoCameraTruoc;

    @OneToMany(mappedBy = "idCumCamera")
    private Set<SanPham> sanPhams = new LinkedHashSet<>();

}