package com.example.graduation_project_group_2_mobileworld.dto.phieu_bao_hanh;

import com.example.graduation_project_group_2_mobileworld.entity.ImelDaBan;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
@Getter
@Setter
public class PhieuBaoHanhDTO {
    private Integer id;
    private String ma;
    private ImelDaBan idImelDaBan;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String ghiChu;

    public PhieuBaoHanhDTO(Integer id, String ma, ImelDaBan idImelDaBan, Date ngayBatDau, Date ngayKetThuc, String ghiChu) {
        this.id = id;
        this.ma = ma;
        this.idImelDaBan = idImelDaBan;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.ghiChu = ghiChu;
    }
}
