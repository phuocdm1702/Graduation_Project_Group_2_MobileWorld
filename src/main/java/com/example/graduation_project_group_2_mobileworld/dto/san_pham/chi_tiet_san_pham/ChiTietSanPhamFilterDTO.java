package com.example.graduation_project_group_2_mobileworld.dto.san_pham.chi_tiet_san_pham;

import lombok.Data;

@Data
public class ChiTietSanPhamFilterDTO {
    private String keyword;
    private Integer idNhaSanXuat;
    private Integer idMauSac;
    private Integer idPin;
    private Integer idManHinh;
    private Integer idRam;
    private Integer idBoNhoTrong;
    private Integer idCpu;
    private Integer idGpu;
    private Integer idCumCamera;
    private Integer idHeDieuHanh;
    private Integer idThietKe;
    private Integer idSim;
    private Integer idCongSac;
    private Integer idHoTroCongNgheSac;
    private Integer idCongNgheMang;
    private Integer idLoaiTinhTrang;
    private Integer page;
    private Integer size;
}
