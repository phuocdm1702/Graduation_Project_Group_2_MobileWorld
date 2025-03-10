package com.example.graduation_project_group_2_mobileworld.dto.san_pham.imel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImelDTO {

    private Integer id;

    @NotBlank(message = "Mã Imel không được để trống")
    @Size(max = 255, message = "Mã Imel không được vượt quá 255 ký tự")
    private String ma;

    @NotBlank(message = "Imel phẩm không được để trống")
    @Size(max = 255, message = "Imel không được vượt quá 255 ký tự")
    private String imel;

    public ImelDTO(Integer id, String ma, String imel) {
        this.id = id;
        this.ma = ma;
        this.imel = imel;
    }
}
