package com.example.graduation_project_group_2_mobileworld.dto.hinh_thuc_thanh_toan;

import com.example.graduation_project_group_2_mobileworld.entity.PhuongThucThanhToan;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class HinhThucThanhToanDTO {
    private Integer id;

    private Integer idHoaDon;

    private PhuongThucThanhToan idPhuongThucThanhToan;

    private BigDecimal tienChuyenKhoan;

    private BigDecimal tienMat;

    private String ma;

    private Boolean deleted = false;

    public HinhThucThanhToanDTO() {

    }

    public HinhThucThanhToanDTO(Integer id, Integer idHoaDon, PhuongThucThanhToan idPhuongThucThanhToan, BigDecimal tienChuyenKhoan, BigDecimal tienMat, String ma, Boolean deleted) {
        this.id = id;
        this.idHoaDon = idHoaDon;
        this.idPhuongThucThanhToan = idPhuongThucThanhToan;
        this.tienChuyenKhoan = tienChuyenKhoan;
        this.tienMat = tienMat;
        this.ma = ma;
        this.deleted = deleted;
    }


}
