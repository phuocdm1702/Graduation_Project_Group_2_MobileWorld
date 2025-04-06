package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.*;
import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ChiTietSanPhamDTO {
    // Các trường bắt buộc của SanPham
    private Integer idSanPham; // Có thể null nếu tạo mới
    private Integer idNhaSanXuat;
    private Integer idPin;
    private Integer idManHinh;
    private Integer idCpu;
    private Integer idGpu;
    private Integer idCumCamera;
    private Integer idHeDieuHanh;
    private Integer idThietKe;
    private Integer idSim;
    private Integer idCongSac;
    private Integer idHoTroCongNgheSac;
    private Integer idCongNgheMang;
    private String tenSanPham;
    private String ma;

    private Integer id;

    // Các trường không bắt buộc của SanPham
    private Integer idHoTroBoNhoNgoai;
    private Integer idChiSoKhangBuiVaNuoc;
    private String tienIchDacBiet;
    private String ghiChu;

    // Các trường bắt buộc chung
    private BigDecimal giaBan; // Dùng chung cho tất cả variants

    private Date createdAt;
    private Integer createdBy;
    private Date updatedAt;
    private Integer updatedBy;

    // Variants
    private List<VariantDTO> variants;

    @Data
    public static class VariantDTO {
        private Imel idImel;
        private Integer idMauSac;
        private String mauSac; // Thêm trường này
        private Integer idRam;
        private String dungLuongRam; // Thêm trường này
        private Integer idBoNhoTrong;
        private String dungLuongBoNhoTrong; // Thêm trường này
        private Integer idLoaiTinhTrang;
        private Integer imageIndex;
        private BigDecimal donGia;
        private List<String> imeiList;
    }
}