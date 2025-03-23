package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

public class HoTroCongNgheSacDTO {
    private Integer id;
    private String ma;
    private String tenCongNgheSac;

    // Constructors
    public HoTroCongNgheSacDTO() {}

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getMa() { return ma; }
    public void setMa(String ma) { this.ma = ma; }
    public String getTenCongNgheSac() { return tenCongNgheSac; }
    public void setTenCongNgheSac(String tenCongNgheSac) { this.tenCongNgheSac = tenCongNgheSac; }
}