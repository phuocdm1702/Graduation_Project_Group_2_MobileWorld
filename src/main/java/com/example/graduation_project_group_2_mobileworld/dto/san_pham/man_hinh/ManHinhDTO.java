package com.example.graduation_project_group_2_mobileworld.dto.san_pham.man_hinh;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
public class ManHinhDTO {
    private Integer id;

    @NotNull(message = "Công nghệ màn hình không được để trống.")
    @Min(value = 1, message = "ID công nghệ màn hình phải là số dương.")
    private Integer idCongNgheManHinh; // Chỉ lưu ID để tránh lỗi vòng lặp

    @NotBlank(message = "Mã màn hình không được để trống.")
    @Size(max = 255, message = "Mã màn hình không được vượt quá 255 ký tự.")
    private String ma;

    @NotBlank(message = "Kích thước màn hình không được để trống.")
    @Size(max = 255, message = "Kích thước màn hình không được vượt quá 255 ký tự.")
    private String kichThuoc;

    @NotBlank(message = "Độ phân giải không được để trống.")
    @Size(max = 255, message = "Độ phân giải không được vượt quá 255 ký tự.")
    private String doPhanGiai;

    @NotBlank(message = "Độ sáng tối đa không được để trống.")
    @Size(max = 255, message = "Độ sáng tối đa không được vượt quá 255 ký tự.")
    private String doSangToiDa;

    @NotBlank(message = "Tần số quét không được để trống.")
    @Size(max = 255, message = "Tần số quét không được vượt quá 255 ký tự.")
    private String tanSoQuet;

    @NotBlank(message = "Kiểu màn hình không được để trống.")
    @Size(max = 255, message = "Kiểu màn hình không được vượt quá 255 ký tự.")
    private String kieuManHinh;

    public ManHinhDTO(Integer id, Integer idCongNgheManHinh, String ma, String kichThuoc, String doPhanGiai, String doSangToiDa, String tanSoQuet, String kieuManHinh) {
        this.id = id;
        this.idCongNgheManHinh = idCongNgheManHinh;
        this.ma = ma;
        this.kichThuoc = kichThuoc;
        this.doPhanGiai = doPhanGiai;
        this.doSangToiDa = doSangToiDa;
        this.tanSoQuet = tanSoQuet;
        this.kieuManHinh = kieuManHinh;
    }
}