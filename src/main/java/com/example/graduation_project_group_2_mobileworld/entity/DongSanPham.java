package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "dong_san_pham")
public class DongSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ten_dong_san_pham", nullable = false)
    private String dongSanPham;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
}