package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThietKeDTO {

    private Integer id;

    private String ma;

    private String chatLieuKhung;

    private String chatLieuMatLung;

    private Boolean deleted;

    public ThietKeDTO(Integer id, String ma, String chatLieuKhung, String chatLieuMatLung, Boolean deleted) {
        this.id = id;
        this.ma = ma;
        this.chatLieuKhung = chatLieuKhung;
        this.chatLieuMatLung = chatLieuMatLung;
        this.deleted = deleted;
    }
}
