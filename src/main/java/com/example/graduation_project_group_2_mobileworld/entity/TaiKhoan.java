package com.example.graduation_project_group_2_mobileworld.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "tai_khoan")
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_quyen_han", referencedColumnName = "id")
    private QuyenHan idQuyenHan;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ma")
    private String ma;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ten_dang_nhap")
    private String tenDangNhap;

    @Size(max = 255)
    @Nationalized
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Size(max = 255)
    @Nationalized
    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mat_khau")
    private String matKhau;

    @Column(name = "deleted")
    private Boolean deleted;

}