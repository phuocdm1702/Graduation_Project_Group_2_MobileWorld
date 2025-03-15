package com.example.graduation_project_group_2_mobileworld.dto.san_pham.imel;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ImelDaBanDTO {
    private Integer id;
    private String ma;
    private String imel;
    private Date ngayBan;
    private String ghiChu;
    private Boolean deleted;

    public ImelDaBanDTO(Integer id, String ma, String imel, Date ngayBan, String ghiChu, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.imel = imel;
        this.ngayBan = ngayBan;
        this.ghiChu = ghiChu;
        this.deleted = deleted;
    }
}