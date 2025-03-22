package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeDieuHanhDTO {

    private Integer id;

    private String ma;

    private String heDieuHanh;

    private String phienBan;

    private Boolean deleted;

    public HeDieuHanhDTO(Integer id, String ma, String heDieuHanh, String phienBan, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.heDieuHanh = heDieuHanh;
        this.phienBan = phienBan;
        this.deleted = deleted;
    }
}
