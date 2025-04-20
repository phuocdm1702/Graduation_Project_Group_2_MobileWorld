package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.SanPham.Imel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ChiTietSanPhamDTO {
    private Integer id;
    private Integer idSanPham;
    private String maSanPham;
    private Integer idNhaSanXuat;
    private Integer idPin;
    private Integer congNgheManHinh;
    private Integer idHoTroBoNhoNgoai;
    private Integer idCpu;
    private Integer idGpu;
    private Integer idCumCamera;
    private Integer idHeDieuHanh;
    private Integer idChiSoKhangBuiVaNuoc;
    private Integer idThietKe;
    private Integer idSim;
    private Integer hoTroCongNgheSac;
    private Integer idCongNgheMang;
    private String ma;
    private String tenSanPham;
    private Boolean deleted;
    private Date createdAt;
    private Integer createdBy;
    private Date updatedAt;
    private Integer updatedBy;
    private BigDecimal giaBan;
    private String tienIchDacBiet;
    private String ghiChu;
    private List<VariantDTO> variants;

    @Getter
    @Setter
    public static class VariantDTO {
        private Imel idImel;
        private Integer idMauSac;
        private String mauSac;
        private Integer idRam;
        private String dungLuongRam;
        private Integer idBoNhoTrong;
        private String dungLuongBoNhoTrong;
        private Integer idLoaiTinhTrang;
        private BigDecimal donGia;
        private Integer imageIndex;
        private List<String> imeiList;
        private Integer quantity; // Thêm trường số lượng
    }
}