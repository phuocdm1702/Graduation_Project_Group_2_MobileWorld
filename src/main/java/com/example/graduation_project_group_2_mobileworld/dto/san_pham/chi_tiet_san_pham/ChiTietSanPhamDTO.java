package com.example.graduation_project_group_2_mobileworld.dto.san_pham.chi_tiet_san_pham;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChiTietSanPhamDTO {
    private Integer id;
    private Integer idImel;
    private Integer idAnhSanPham;
    private Integer idNhaSanXuat;
    private Integer idDongSanPham;
    private Integer idMauSac;
    private Integer idPin;
    private Integer idManHinh;
    private Integer idRam;
    private Integer idBoNhoTrong;
    private Integer idHoTroBoNhoNgoai;
    private Integer idCpu;
    private Integer idGpu;
    private Integer idCumCamera;
    private Integer idHeDieuHanh;
    private Integer idChiSoKhangBuiVaNuoc;
    private Integer idThietKe;
    private Integer idSim;
    private Integer idCongSac;
    private Integer idHoTroCongNgheSac;
    private Integer idCongNgheMang;
    private Integer idLoaiTinhTrang;
    private String tienIchDacBiet;
    private BigDecimal giaBan;
    private Boolean deleted;
    private Date createdAt;
    private Integer createdBy;
    private Date updatedAt;
    private Integer updatedBy;

    // Các trường tên thuộc tính để hiển thị trên combobox
    private String dongSanPham;
    private String tenNhaSanXuat;
    private String tenMauSac;
    private String dungLuongPin;
    private String kichThuocManHinh;
    private String dungLuongRam;
    private String dungLuongBoNhoTrong;
    private String tenCpu;
    private String tenGpu;
    private String tenCumCamera;
    private String tenHeDieuHanh;
    private String maChiSoKhangBuiVaNuoc;
    private String tenThietKe;
    private String loaiSim;
    private String tenCongSac;
    private String tenHoTroCongNgheSac;
    private String tenCongNgheMang;
    private String tenTinhTrang;


}
