package com.example.graduation_project_group_2_mobileworld.dto.san_pham.nha_san_xuat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhaSanXuatDTO {

    private Integer id;

    @NotBlank(message = "Mã nhà sản xuất không được để trống")
    @Size(max = 255, message = "Mã nhà sản xuất không được vượt quá 255 ký tự")
    private String ma;

    @NotBlank(message = "Tên nhà sản xuất không được để trống")
    @Size(max = 255, message = "Tên nhà sản xuất không được vượt quá 255 ký tự")
    private String nhaSanXuat;

    private Boolean deleted;


    public NhaSanXuatDTO(Integer id, String ma, String nhaSanXuat, boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.nhaSanXuat = nhaSanXuat;
        this.deleted = deleted;
    }
}
