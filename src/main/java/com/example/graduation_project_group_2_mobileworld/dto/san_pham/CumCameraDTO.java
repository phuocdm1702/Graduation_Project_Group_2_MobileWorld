package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CumCameraDTO {
    private Integer id;
    private String ma;
    private Boolean deleted;
    private String cameraTruoc;
    private String cameraSau;

    // Constructors, getters, setters
    public CumCameraDTO() {}

    public CumCameraDTO(Integer id, String ma, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.deleted = deleted;
    }

    // Additional getters and setters
    public String getCameraTruoc() { return cameraTruoc; }
    public void setCameraTruoc(String cameraTruoc) { this.cameraTruoc = cameraTruoc; }
    public String getCameraSau() { return cameraSau; }
    public void setCameraSau(String cameraSau) { this.cameraSau = cameraSau; }
}