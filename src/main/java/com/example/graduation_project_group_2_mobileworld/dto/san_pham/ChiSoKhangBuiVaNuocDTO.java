package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChiSoKhangBuiVaNuocDTO {

    private Integer id;

    private String ma;

    private String tenChiSo;

    private Boolean deleted = false;

    public ChiSoKhangBuiVaNuocDTO(Integer id, String ma, String tenChiSo, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.tenChiSo = tenChiSo;
        this.deleted = deleted;
    }
}
