package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CongNgheMangDTO {

    private Integer id;

    private String ma;

    private String tenCongNgheMang;

    private Boolean deleted = false;

    public CongNgheMangDTO(Integer id, String ma, String tenCongNgheMang, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.tenCongNgheMang = tenCongNgheMang;
        this.deleted = deleted;
    }
}
