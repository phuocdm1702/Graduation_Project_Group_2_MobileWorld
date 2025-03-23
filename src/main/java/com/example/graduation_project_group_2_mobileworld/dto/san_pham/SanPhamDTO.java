package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SanPhamDTO {
    private Integer id;
    private Integer idNhaSanXuat;
    private Integer idPin;
    private Integer idManHinh;
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
    private String ma;
    private String tenSanPham;
    private Boolean deleted;
    private Date createdAt;
    private Integer createdBy;
    private Date updatedAt;
    private Integer updatedBy;
}