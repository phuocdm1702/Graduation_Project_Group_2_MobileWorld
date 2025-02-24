package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "sim")
public class Sim {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Column(name = "so_luong_sim_ho_tro")
    private Integer soLuongSimHoTro;

    @Size(max = 255)
    @Nationalized
    @Column(name = "cac_loai_sim_ho_tro")
    private String cacLoaiSimHoTro;

    @Column(name = "deleted")
    private Boolean deleted;

}