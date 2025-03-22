package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CongNgheSacDTO {

    private Integer id;

    private String ma;

    private String tenCongNghe;

    private Boolean deleted = false;

    public CongNgheSacDTO(Integer id, String ma, String tenCongNghe, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.tenCongNghe = tenCongNghe;
        this.deleted = deleted;
    }
}
