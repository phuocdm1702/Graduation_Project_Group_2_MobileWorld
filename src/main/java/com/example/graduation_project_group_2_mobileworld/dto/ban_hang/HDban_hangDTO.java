package com.example.graduation_project_group_2_mobileworld.dto.ban_hang;

import java.util.List;

public class HDban_hangDTO {

    private String ma;
    private Short status;
    private List<HDCTban_hangDTO> items;


    public HDban_hangDTO() {

    }


    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public List<HDCTban_hangDTO> getItems() {
        return items;
    }

    public void setItems(List<HDCTban_hangDTO> items) {
        this.items = items;
    }
}
