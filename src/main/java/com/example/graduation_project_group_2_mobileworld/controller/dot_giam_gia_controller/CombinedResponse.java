package com.example.graduation_project_group_2_mobileworld.controller.dot_giam_gia_controller;

import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewCTSPDTO;
import com.example.graduation_project_group_2_mobileworld.dto.dot_giam_gia.viewSanPhamDTO;

import java.util.List;

public class CombinedResponse {
    private List<viewSanPhamDTO> spList;
    private List<viewCTSPDTO> ctspList;
    private int totalPages;
    private int currentPageDSP;
    private long totalElements;
    private int totalPagesCTSP;
    private int currentPageCTSP;
    private long totalElementsCTSP;

    public CombinedResponse() {
    }

    public CombinedResponse(List<viewSanPhamDTO> spList, List<viewCTSPDTO> ctspList, int totalPages, int currentPageDSP, long totalElements, int totalPagesCTSP, int currentPageCTSP, long totalElementsCTSP) {
        this.spList = spList;
        this.ctspList = ctspList;
        this.totalPages = totalPages;
        this.currentPageDSP = currentPageDSP;
        this.totalElements = totalElements;
        this.totalPagesCTSP = totalPagesCTSP;
        this.currentPageCTSP = currentPageCTSP;
        this.totalElementsCTSP = totalElementsCTSP;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPageDSP() {
        return currentPageDSP;
    }

    public void setCurrentPageDSP(int currentPageDSP) {
        this.currentPageDSP = currentPageDSP;
    }

    public List<viewSanPhamDTO> getSpList() {
        return spList;
    }

    public void setSpList(List<viewSanPhamDTO> spList) {
        this.spList = spList;
    }


    public List<viewCTSPDTO> getCtspList() {
        return ctspList;
    }

    public void setCtspList(List<viewCTSPDTO> ctspList) {
        this.ctspList = ctspList;
    }

    public int getTotalPagesDSP() {
        return totalPages;
    }

    public void setTotalPagesDSP(int totalPagesDSP) {
        this.totalPages = totalPagesDSP;
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
