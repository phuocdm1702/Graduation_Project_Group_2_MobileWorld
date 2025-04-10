package com.example.graduation_project_group_2_mobileworld.entity.SanPham;

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
@Table(name = "pin")
public class Pin {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Size(max = 255)
    @Nationalized
    @Column(name = "loai_pin")
    private String loaiPin;

    @Size(max = 255)
    @Nationalized
    @Column(name = "dung_luong_pin")
    private String dungLuongPin;

    @Column(name = "deleted")
    private Boolean deleted;

}