package com.example.graduation_project_group_2_mobileworld.entity.SanPham;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "anh_san_pham")
public class AnhSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ten_anh", nullable = false)
    private String tenAnh;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "duong_dan", length = 500)
    private String duongDan;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Column(name = "hash")
    private String hash;
    @Column(name = "product_group_key")
    private String productGroupKey;
}