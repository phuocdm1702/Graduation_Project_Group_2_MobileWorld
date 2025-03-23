package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

public class RamDTO {
    private Integer id;
    private String ma;
    private String dungLuong;

    // Constructors
    public RamDTO() {}

    public RamDTO(Integer id, String ma, String dungLuong) {
        this.id = id;
        this.ma = ma;
        this.dungLuong = dungLuong;
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getMa() { return ma; }
    public void setMa(String ma) { this.ma = ma; }
    public String getDungLuong() { return dungLuong; }
    public void setDungLuong(String dungLuong) { this.dungLuong = dungLuong; }
}