package com.example.graduation_project_group_2_mobileworld.dto.Imel_da_ban;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImelDaBanDTO {
    private Integer id;
    private String ma;
    private String imel;

    public ImelDaBanDTO(Integer id, String ma, String imel) {
        this.id = id;
        this.ma = ma;
        this.imel = imel;
    }
}
