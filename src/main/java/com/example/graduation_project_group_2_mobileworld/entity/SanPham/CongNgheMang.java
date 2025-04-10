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
@Table(name = "cong_nghe_mang")
public class CongNgheMang {
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
    @Column(name = "ten_cong_nghe_mang", nullable = false)
    private String tenCongNgheMang;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

}