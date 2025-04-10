package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CumCameraDTO {
    private Integer id;
    private String ma;

    private String cameraTruoc;
    private String cameraSau;
    private Boolean deleted;

    // Constructors, getters, setters
    public CumCameraDTO() {}

    public CumCameraDTO(Integer id, String ma, String cameraTruoc, String cameraSau, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.cameraTruoc = cameraTruoc;
        this.cameraSau = cameraSau;
        this.deleted = deleted;
    }
}