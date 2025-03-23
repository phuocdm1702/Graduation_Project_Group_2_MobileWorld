package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PinDTO {

    private Integer id;

    private String ma;

    private String loaiPin;

    private String dungLuongPin;

    private Boolean deleted;

    public PinDTO(Integer id, String ma, String loaiPin, String dungLuongPin, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.loaiPin = loaiPin;
        this.dungLuongPin = dungLuongPin;
        this.deleted = deleted;
    }
}
