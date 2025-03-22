package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GpuDTO {

    private Integer id;

    private String ma;

    private String tenGpu;

    private Boolean deleted = false;

    public GpuDTO(Integer id, String ma, String tenGpu, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.tenGpu = tenGpu;
        this.deleted = deleted;
    }
}
