package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "phieu_bao_hanh")
public class PhieuBaoHanh {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_imel_da_ban")
    private ImelDaBan idImelDaBan;

    @Column(name = "ngay_bat_dau")
    private Instant ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private Instant ngayKetThuc;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "deleted")
    private Boolean deleted;

}