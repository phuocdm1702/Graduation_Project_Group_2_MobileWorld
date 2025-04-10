package com.example.graduation_project_group_2_mobileworld.entity.SanPham;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "he_dieu_hanh")
public class HeDieuHanh {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "he_dieu_hanh", nullable = false)
    private String heDieuHanh;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "phien_ban", nullable = false)
    private String phienBan;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

}