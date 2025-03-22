package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TinhTrangDTO {

    private Integer id;

    private String ma;

    private String loaiTinhTrang;

    private Boolean deleted;

    public TinhTrangDTO(Integer id, String ma, String loaiTinhTrang, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.loaiTinhTrang = loaiTinhTrang;
        this.deleted = deleted;
    }
}
