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
@Table(name = "ho_tro_bo_nho_ngoai")
public class HoTroBoNhoNgoai {
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
    @Column(name = "ho_tro_bo_nho_ngoai", nullable = false)
    private String hoTroBoNhoNgoai;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

}