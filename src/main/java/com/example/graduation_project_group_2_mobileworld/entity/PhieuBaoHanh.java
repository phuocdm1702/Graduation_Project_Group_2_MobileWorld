package com.example.graduation_project_group_2_mobileworld.entity;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ImelDaBan;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "phieu_bao_hanh")
public class PhieuBaoHanh {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_imel_da_ban", referencedColumnName = "id")
    private ImelDaBan idImelDaBan;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Column(name = "ngay_bat_dau")
    private Date ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private Date ngayKetThuc;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "deleted")
    private Boolean deleted;

}