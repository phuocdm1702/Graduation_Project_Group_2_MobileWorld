package com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia;

import java.util.List;

public class RequestDTO {
    private String keyword;
    private List<Integer> idDSPs;


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getIdDSPs() {
        return idDSPs;
    }

    public void setIdDSPs(List<Integer> idDSPs) {
        this.idDSPs = idDSPs;
    }
}
