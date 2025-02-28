package com.example.graduation_project_group_2_mobileworld.dto.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
@Setter
@Getter
public class LichSuHoaDonDTO {
    private HoaDon idHoaDon;
    private NhanVien idNhanVien;
    private String ma;
    private String hanhDong;
    private Time thoiGian;

    public LichSuHoaDonDTO(HoaDon idHoaDon, NhanVien idNhanVien, String ma, String hanhDong, Time thoiGian) {
        this.idHoaDon = idHoaDon;
        this.idNhanVien = idNhanVien;
        this.ma = ma;
        this.hanhDong = hanhDong;
        this.thoiGian = thoiGian;
    }
}
