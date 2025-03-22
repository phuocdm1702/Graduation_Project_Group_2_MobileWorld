package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CongSacDTO {

    private Integer id;

    private String ma;

    private String congSac;

    private Boolean deleted = false;

    public CongSacDTO(Integer id, String ma, String congSac, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.congSac = congSac;
        this.deleted = deleted;
    }
}
