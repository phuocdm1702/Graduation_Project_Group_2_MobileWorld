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
    private String ma;
    private KhachHang idKhachHang;
    private NhanVien idNhanVien;
    private PhieuGiamGia idPhieuGiamGia;
    private String tenKhachHang;
    private String soDienThoaiKhachHang;
    private String diaChiKhachHang;
    private String loaiDon;
    private Date ngayTao;
    private BigDecimal tongTien;
    private BigDecimal tongTienSauGiam;
    private BigDecimal phiVanChuyen;
    private Short trangThai;
    private String ghiChu;
    private List<HoaDonChiTietDTO> chiTietHoaDon;
    private List<LichSuHoaDonDTO> lichSuHoaDon;
    private List<HinhThucThanhToanDTO> hinhThucThanhToan;

    public HoaDonDTO() {
    }

    public HoaDonDTO(Integer id, List<HoaDonChiTietDTO> chiTietHoaDon, List<LichSuHoaDonDTO> lichSuHoaDon,
                     List<HinhThucThanhToanDTO> hinhThucThanhToan, KhachHang idKhachHang, NhanVien idNhanVien,
                     PhieuGiamGia idPhieuGiamGia, String ma, String soDienThoaiKhachHang, String loaiDon,
                     BigDecimal tongTien, Date ngayTao, BigDecimal tongTienSauGiam, Short trangThai,
                     String diaChiKhachHang, String tenKhachHang, String ghiChu, BigDecimal phiVanChuyen) {
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
        this.tenKhachHang = tenKhachHang;
        this.ghiChu = ghiChu;
        this.phiVanChuyen = phiVanChuyen;
    }

    // Constructor mới khớp với truy vấn @Query
    public HoaDonDTO(String ma, String maNhanVien, String tenKhachHang, String soDienThoaiKhachHang,
                     BigDecimal tongTienSauGiam, Double phanTramGiamGia, BigDecimal phiVanChuyen,
                     Date ngayTao, String loaiDon, Short trangThai) {
        this.ma = ma;
        this.idNhanVien = new NhanVien(); // Tạo đối tượng NhanVien và gán mã
        this.idNhanVien.setMa(maNhanVien);
        this.tenKhachHang = tenKhachHang;
        this.soDienThoaiKhachHang = soDienThoaiKhachHang;
        this.tongTienSauGiam = tongTienSauGiam;
        this.idPhieuGiamGia = new PhieuGiamGia(); // Tạo đối tượng PhieuGiamGia và gán phần trăm giảm giá
        this.idPhieuGiamGia.setPhanTramGiamGia(phanTramGiamGia);
        this.phiVanChuyen = phiVanChuyen;
        this.ngayTao = ngayTao;
        this.loaiDon = loaiDon;
        this.trangThai = trangThai;
    }
}