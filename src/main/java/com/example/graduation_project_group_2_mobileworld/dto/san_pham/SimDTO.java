package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimDTO {

    private Integer id;

    private String ma;

    private Integer soLuongSimHoTro;


    private String cacLoaiSimHoTro;


    private Boolean deleted;

    public SimDTO(Integer id, String ma, Integer soLuongSimHoTro, String cacLoaiSimHoTro, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.soLuongSimHoTro = soLuongSimHoTro;
        this.cacLoaiSimHoTro = cacLoaiSimHoTro;
        this.deleted = deleted;
    }
}
