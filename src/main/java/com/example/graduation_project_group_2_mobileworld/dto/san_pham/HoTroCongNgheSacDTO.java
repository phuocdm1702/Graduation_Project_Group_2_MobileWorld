package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HoTroCongNgheSacDTO {
    private Integer id;
    private String ma;
    private String congSac;

    private String congNgheHoTro;

    private Boolean deleted = false;

}