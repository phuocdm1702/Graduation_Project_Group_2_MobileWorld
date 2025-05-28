package com.example.graduation_project_group_2_mobileworld.dto.hoa_don;

import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Setter
@Getter
public class LichSuHoaDonDTO {
    private Integer id;
    private Integer idHoaDon;
    private NhanVien idNhanVien;
    private String ma;
    private String hanhDong;
    private Timestamp thoiGian;

    public LichSuHoaDonDTO() {

    }

    public LichSuHoaDonDTO(Integer id, Integer idHoaDon, NhanVien idNhanVien, String ma, String hanhDong, Timestamp thoiGian) {
        this.id = id;
        this.idHoaDon = idHoaDon;
        this.idNhanVien = idNhanVien;
        this.ma = ma;
        this.hanhDong = hanhDong;
        this.thoiGian = thoiGian;
    }


}
