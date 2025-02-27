package com.example.graduation_project_group_2_mobileworld.dto.dong_san_pham;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DongSanPhamDTO {
    private Integer id;

    @NotBlank(message = "Mã dòng sản phẩm không được để trống")
    @Size(max = 255, message = "Mã dòng sản phẩm không được vượt quá 255 ký tự")
    private String ma;

    @NotBlank(message = "Tên dòng sản phẩm không được để trống")
    @Size(max = 255, message = "Tên dòng sản phẩm không được vượt quá 255 ký tự")
    private String dongSanPham;

    public DongSanPhamDTO(Integer id, String ma, String dongSanPham) {
        this.id = id;
        this.ma = ma;
        this.dongSanPham = dongSanPham;
    }
}
