package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CongNgheManHinhDTO {

    private Integer id;


    @NotBlank(message = "Mã màn hình không được để trống.")
    @Size(max = 255, message = "Mã công nghệ màn hình không được vượt quá 255 ký tự.")
    private String ma;

    @NotBlank(message = "Công nghệ màn hình không được để trống.")
    @Size(max = 255, message = "Công nghệ màn hình không được vượt quá 255 ký tự.")
    private String congNgheManHinh;

    @NotBlank(message = "Chuẩn màn hình không được để trống.")
    @Size(max = 255, message = "Chuẩn màn hình không được vượt quá 255 ký tự.")
    private String chuanManHinh;

    public CongNgheManHinhDTO(Integer id, String ma, String congNgheManHinh, String chuanManHinh) {
        this.id = id;
        this.ma = ma;
        this.congNgheManHinh = congNgheManHinh;
        this.chuanManHinh = chuanManHinh;
    }
}
