package com.example.graduation_project_group_2_mobileworld.controller.dot_giam_gia_controller;

import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;

import java.util.List;

public class CombinedResponse {
    private List<DongSanPham> dspList;
    private List<viewCTSPDTO> ctspList;

    // Constructors, getters và setters
    public CombinedResponse(List<DongSanPham> dspList, List<viewCTSPDTO> ctspList) {
        this.dspList = dspList;
        this.ctspList = ctspList;
    }

    public List<DongSanPham> getDspList() {
        return dspList;
    }

    public void setDspList(List<DongSanPham> dspList) {
        this.dspList = dspList;
    }

    public List<viewCTSPDTO> getCtspList() {
        return ctspList;
    }

    public void setCtspList(List<viewCTSPDTO> ctspList) {
        this.ctspList = ctspList;
    }
}
