package com.example.graduation_project_group_2_mobileworld.dto.phieuGiamGiaDTO;

import com.example.graduation_project_group_2_mobileworld.dto.khach_hang.KhachHangDTO;

import java.util.Date;
import java.util.List;

public class PhieuGiamGiaDTO {
    private Integer id;
    private String ma;
    private String tenPhieuGiamGia;
    private String loaiPhieuGiamGia;
    private Double phanTramGiamGia;
    private Double soTienGiamToiDa;
    private Double hoaDonToiThieu;
    private Integer soLuongDung;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private Integer trangThai;
    private Integer riengTu;
    private String moTa;
    private List<KhPggDTO> selectedCustomers;
    private List<KhPggDTO> allCustomers;
    private List<Integer> customerIds; // Thêm trường customerIds

    public PhieuGiamGiaDTO() {
    }

    public PhieuGiamGiaDTO(String ma, String tenPhieuGiamGia, String loaiPhieuGiamGia, Double phanTramGiamGia, Double soTienGiamToiDa, Double hoaDonToiThieu, Integer soLuongDung, Date ngayBatDau, Date ngayKetThuc, Integer trangThai, Integer riengTu, String moTa, List<KhPggDTO> selectedCustomers, List<KhPggDTO> allCustomers, List<Integer> customerIds) {
        this.ma = ma;
        this.tenPhieuGiamGia = tenPhieuGiamGia;
        this.loaiPhieuGiamGia = loaiPhieuGiamGia;
        this.phanTramGiamGia = phanTramGiamGia;
        this.soTienGiamToiDa = soTienGiamToiDa;
        this.hoaDonToiThieu = hoaDonToiThieu;
        this.soLuongDung = soLuongDung;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
        this.riengTu = riengTu;
        this.moTa = moTa;
        this.selectedCustomers = selectedCustomers;
        this.allCustomers = allCustomers;
        this.customerIds = customerIds;
    }

    // Getter và Setter cho customerIds

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Integer> customerIds) {
        this.customerIds = customerIds;
    }

    // Các getter/setter khác giữ nguyên
    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTenPhieuGiamGia() {
        return tenPhieuGiamGia;
    }

    public void setTenPhieuGiamGia(String tenPhieuGiamGia) {
        this.tenPhieuGiamGia = tenPhieuGiamGia;
    }

    public String getLoaiPhieuGiamGia() {
        return loaiPhieuGiamGia;
    }

    public void setLoaiPhieuGiamGia(String loaiPhieuGiamGia) {
        this.loaiPhieuGiamGia = loaiPhieuGiamGia;
    }

    public Double getPhanTramGiamGia() {
        return phanTramGiamGia;
    }

    public void setPhanTramGiamGia(Double phanTramGiamGia) {
        this.phanTramGiamGia = phanTramGiamGia;
    }

    public Double getSoTienGiamToiDa() {
        return soTienGiamToiDa;
    }

    public void setSoTienGiamToiDa(Double soTienGiamToiDa) {
        this.soTienGiamToiDa = soTienGiamToiDa;
    }

    public Double getHoaDonToiThieu() {
        return hoaDonToiThieu;
    }

    public void setHoaDonToiThieu(Double hoaDonToiThieu) {
        this.hoaDonToiThieu = hoaDonToiThieu;
    }

    public Integer getSoLuongDung() {
        return soLuongDung;
    }

    public void setSoLuongDung(Integer soLuongDung) {
        this.soLuongDung = soLuongDung;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public Integer getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }

    public Integer getRiengTu() {
        return riengTu;
    }

    public void setRiengTu(Integer riengTu) {
        this.riengTu = riengTu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public List<KhPggDTO> getSelectedCustomers() {
        return selectedCustomers;
    }

    public void setSelectedCustomers(List<KhPggDTO> selectedCustomers) {
        this.selectedCustomers = selectedCustomers;
    }

    public List<KhPggDTO> getAllCustomers() {
        return allCustomers;
    }

    public void setAllCustomers(List<KhPggDTO> allCustomers) {
        this.allCustomers = allCustomers;
    }
}