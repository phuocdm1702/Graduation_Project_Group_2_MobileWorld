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
import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @OneToMany(mappedBy = "hoaDon")
    private List<HoaDonChiTiet> chiTietHoaDon;

    @OneToMany(mappedBy = "hoaDon")
    private List<LichSuHoaDon> lichSuHoaDon;

    @OneToMany(mappedBy = "hoaDon")
    private List<HinhThucThanhToan> hinhThucThanhToan;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang", referencedColumnName = "id")
    private KhachHang idKhachHang;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien",referencedColumnName = "id")
    private NhanVien idNhanVien;

    @ManyToOne
    @JoinColumn(name = "id_phieu_giam_gia", referencedColumnName = "id")
    private PhieuGiamGia idPhieuGiamGia;

    @Column(name = "ma", nullable = false)
    private String ma;

    @Column(name = "tien_san_pham", nullable = false, precision = 18, scale = 2)
    private BigDecimal tienSanPham;


    @Column(name = "loai_don")
    private String loaiDon;


    @Column(name = "phi_van_chuyen")
    private BigDecimal phiVanChuyen;


    @Column(name = "tong_tien")
    private BigDecimal tongTien;

    @Column(name = "tong_tien_sau_giam")
    private BigDecimal tongTienSauGiam;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "ten_khach_hang")
    private String tenKhachHang;

    @Column(name = "dia_chi_khach_hang")
    private String diaChiKhachHang;

    @Column(name = "so_dien_thoai_khach_hang")
    private String soDienThoaiKhachHang;

    @Column(name = "email")
    private String email;

    @Column(name = "ngay_tao")
    private Date ngayTao;

    @Column(name = "ngay_thanh_toan")
    private Date ngayThanhToan;

    @Column(name = "trang_thai")
    private Short trangThai;


    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

}