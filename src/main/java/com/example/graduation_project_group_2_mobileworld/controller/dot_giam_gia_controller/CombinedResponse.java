package com.example.graduation_project_group_2_mobileworld.controller.dot_giam_gia_controller;

import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;

import java.util.List;

public class CombinedResponse {
    private List<DongSanPham> dspList;
    private List<viewCTSPDTO> ctspList;
    private int totalPagesDSP; // Đổi từ totalPages thành totalPagesDSP
    private int currentPage;
    private long totalElements;
    private int totalPagesCTSP;
    private int currentPageCTSP;
    private long totalElementsCTSP;

    public CombinedResponse(List<DongSanPham> dspList, List<viewCTSPDTO> ctspList, int totalPagesDSP, int currentPage, long totalElements, int totalPagesCTSP, int currentPageCTSP, long totalElementsCTSP) {
        this.dspList = dspList;
        this.ctspList = ctspList;
        this.totalPagesDSP = totalPagesDSP;
        this.currentPage = currentPage;
        this.totalElements = totalElements;
        this.totalPagesCTSP = totalPagesCTSP;
        this.currentPageCTSP = currentPageCTSP;
        this.totalElementsCTSP = totalElementsCTSP;
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

    public int getTotalPagesDSP() {
        return totalPagesDSP;
    }

    public void setTotalPagesDSP(int totalPagesDSP) {
        this.totalPagesDSP = totalPagesDSP;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPagesCTSP() {
        return totalPagesCTSP;
    }

    public void setTotalPagesCTSP(int totalPagesCTSP) {
        this.totalPagesCTSP = totalPagesCTSP;
    }

    public int getCurrentPageCTSP() {
        return currentPageCTSP;
    }

    public void setCurrentPageCTSP(int currentPageCTSP) {
        this.currentPageCTSP = currentPageCTSP;
    }

    public long getTotalElementsCTSP() {
        return totalElementsCTSP;
    }

    public void setTotalElementsCTSP(long totalElementsCTSP) {
        this.totalElementsCTSP = totalElementsCTSP;
    }
}
