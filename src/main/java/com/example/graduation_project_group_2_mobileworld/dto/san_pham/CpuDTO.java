package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CpuDTO {

    private Integer id;

    private String ma;

    private String tenCpu;

    private Integer soNhan;

    private Boolean deleted = false;

    public CpuDTO(Integer id, String ma, String tenCpu, Integer soNhan, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.tenCpu = tenCpu;
        this.soNhan = soNhan;
        this.deleted = deleted;
    }
}
