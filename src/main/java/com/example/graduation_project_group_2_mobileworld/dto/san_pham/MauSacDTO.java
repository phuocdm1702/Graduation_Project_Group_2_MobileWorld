package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

public class MauSacDTO {
    private Integer id;
    private String ma;
    private String tenMau;

    // Constructors
    public MauSacDTO() {}

    public MauSacDTO(Integer id, String ma, String tenMau) {
        this.id = id;
        this.ma = ma;
        this.tenMau = tenMau;
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getMa() { return ma; }
    public void setMa(String ma) { this.ma = ma; }
    public String getTenMau() { return tenMau; }
    public void setTenMau(String tenMau) { this.tenMau = tenMau; }
}