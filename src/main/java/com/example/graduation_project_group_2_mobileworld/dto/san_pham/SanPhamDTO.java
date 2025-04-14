package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SanPhamDTO {
    private Integer id;
    private String ma;
    private String tenSanPham;
    private String nhaSanXuat; // Tên nhà sản xuất thay vì ID
    private String heDieuHanh; // Tên hệ điều hành
    private String phienBan; // Phiên bản hệ điều hành
    private String congNgheManHinh; // Công nghệ màn hình
    private String chuanManHinh; // Chuẩn màn hình
    private String tenCpu; // Tên CPU
    private String cameraTruoc; // Thông số camera trước
    private String cameraSau; // Thông số camera sau
    private String loaiPin; // Loại pin
    private String dungLuongPin; // Dung lượng pin
    private Integer soLuongSimHoTro; // Số lượng SIM hỗ trợ
    private String cacLoaiSimHoTro; // Các loại SIM hỗ trợ
    private Boolean deleted;
    private Date createdAt;
    private Integer createdBy;
    private Date updatedAt;
    private Integer updatedBy;
    private Long imeiCount; // Số lượng IMEI
    private BigDecimal minPrice; // Giá thấp nhất
    private BigDecimal maxPrice; // Giá cao nhất
}