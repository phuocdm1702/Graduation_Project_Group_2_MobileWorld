package com.example.graduation_project_group_2_mobileworld.entity.SanPham;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ho_tro_cong_nghe_sac")
public class HoTroCongNgheSac {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Size(max = 50)
    @Nationalized
    @Column(name = "cong_sac", length = 50)
    private String congSac;

    @Nationalized
    @Lob
    @Column(name = "cong_nghe_ho_tro")
    private String congNgheHoTro;

    @OneToMany(mappedBy = "hoTroCongNgheSac")
    private Set<SanPham> sanPhams = new LinkedHashSet<>();

}