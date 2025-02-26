package com.example.graduation_project_group_2_mobileworld.dto.dongSanPhamDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DongSanPhamDTO {
    private Integer id;
    private String ma;
    private String dongSanPham;

    public DongSanPhamDTO(Integer id, String ma, String dongSanPham) {
        this.id = id;
        this.ma = ma;
        this.dongSanPham = dongSanPham;
    }
}
