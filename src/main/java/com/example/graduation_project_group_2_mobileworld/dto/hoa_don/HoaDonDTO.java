package com.example.graduation_project_group_2_mobileworld.dto.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hinh_thuc_thanh_toan.HinhThucThanhToanDTO;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class HoaDonDTO {
    private Integer id;
    private List<HoaDonChiTietDTO> chiTietHoaDon;
    private List<LichSuHoaDonDTO> lichSuHoaDon;
    private List<HinhThucThanhToanDTO> hinhThucThanhToan;
    private KhachHang idKhachHang;
    private NhanVien idNhanVien;
    private PhieuGiamGia idPhieuGiamGia;
    private String ma;
    private String soDienThoaiKhachHang;
    private String loaiDon;
    private BigDecimal tongTien;
    private Date ngayTao;
    private BigDecimal tongTienSauGiam;
    private Short trangThai;
    private String diaChiKhachHang;


    public HoaDonDTO() {
    }

    public HoaDonDTO(Integer id, List<HoaDonChiTietDTO> chiTietHoaDon, List<LichSuHoaDonDTO> lichSuHoaDon, List<HinhThucThanhToanDTO> hinhThucThanhToan, KhachHang idKhachHang, NhanVien idNhanVien, PhieuGiamGia idPhieuGiamGia, String ma, String soDienThoaiKhachHang, String loaiDon, BigDecimal tongTien, Date ngayTao, BigDecimal tongTienSauGiam, Short trangThai, String diaChiKhachHang) {
        this.id = id;
        this.chiTietHoaDon = chiTietHoaDon;
        this.lichSuHoaDon = lichSuHoaDon;
        this.hinhThucThanhToan = hinhThucThanhToan;
        this.idKhachHang = idKhachHang;
        this.idNhanVien = idNhanVien;
        this.idPhieuGiamGia = idPhieuGiamGia;
        this.ma = ma;
        this.soDienThoaiKhachHang = soDienThoaiKhachHang;
        this.loaiDon = loaiDon;
        this.tongTien = tongTien;
        this.ngayTao = ngayTao;
        this.tongTienSauGiam = tongTienSauGiam;
        this.trangThai = trangThai;
        this.diaChiKhachHang = diaChiKhachHang;
    }
}
