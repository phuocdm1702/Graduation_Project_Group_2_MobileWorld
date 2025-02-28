package com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia;

import java.util.List;

public class addDotGiamGiaDTO {
    private dot_giam_gia_DTO dotGiamGia;
    private List<Integer> idDSPs;
    private List<viewCTSPDTO> ctspList;

    public dot_giam_gia_DTO getDotGiamGia() {
        return dotGiamGia;
    }

    public void setDotGiamGia(dot_giam_gia_DTO dotGiamGia) {
        this.dotGiamGia = dotGiamGia;
    }

    public List<Integer> getIdDSPs() {
        return idDSPs;
    }

    public void setIdDSPs(List<Integer> idDSPs) {
        this.idDSPs = idDSPs;
    }

    public List<viewCTSPDTO> getCtspList() {
        return ctspList;
    }

    public void setCtspList(List<viewCTSPDTO> ctspList) {
        this.ctspList = ctspList;
    }
}
