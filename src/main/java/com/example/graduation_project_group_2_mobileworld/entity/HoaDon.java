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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private KhachHang idKhachHang;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_nhan_vien", nullable = false)
    private NhanVien idNhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_phieu_giam_gia")
    private PhieuGiamGia idPhieuGiamGia;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ma", nullable = false)
    private String ma;

    @NotNull
    @Column(name = "tien_san_pham", nullable = false, precision = 18, scale = 2)
    private BigDecimal tienSanPham;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "loai_don", nullable = false)
    private String loaiDon;

    @NotNull
    @Column(name = "phi_van_chuyen", nullable = false, precision = 18, scale = 2)
    private BigDecimal phiVanChuyen;

    @NotNull
    @Column(name = "tong_tien", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "tong_tien_sau_giam", precision = 18, scale = 2)
    private BigDecimal tongTienSauGiam;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ten_khach_hang", nullable = false)
    private String tenKhachHang;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "dia_chi_khach_hang", nullable = false)
    private String diaChiKhachHang;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "so_dien_thoai_khach_hang", nullable = false, length = 20)
    private String soDienThoaiKhachHang;

    @Size(max = 255)
    @Nationalized
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "ngay_tao", nullable = false)
    private Date ngayTao;

    @Column(name = "ngay_thanh_toan")
    private Date ngayThanhToan;

    @Column(name = "trang_thai", columnDefinition = "tinyint not null")
    private Short trangThai;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

}