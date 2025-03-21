package com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia;

import java.util.List;

public class RequestDTO {
    private String keyword;
    private List<Integer> idDSPs;
    private List<Integer> idBoNhoTrongs;
    private List<Integer> mauSac;
    private List<Integer> idHeDieuHanh; // Thêm
    private List<Integer> idNhaSanXuat;

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

    public List<Integer> getIdBoNhoTrongs() {
        return idBoNhoTrongs;
    }

    public void setIdBoNhoTrongs(List<Integer> idBoNhoTrongs) {
        this.idBoNhoTrongs = idBoNhoTrongs;
    }

    public List<Integer> getMauSac() {
        return mauSac;
    }

    public void setMauSac(List<Integer> mauSac) {
        this.mauSac = mauSac;
    }

    public List<Integer> getIdHeDieuHanh() {
        return idHeDieuHanh;
    }

    public void setIdHeDieuHanh(List<Integer> idHeDieuHanh) {
        this.idHeDieuHanh = idHeDieuHanh;
    }

    public List<Integer> getIdNhaSanXuat() {
        return idNhaSanXuat;
    }

    public void setIdNhaSanXuat(List<Integer> idNhaSanXuat) {
        this.idNhaSanXuat = idNhaSanXuat;
    }
}
