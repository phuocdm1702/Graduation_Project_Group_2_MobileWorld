package com.example.graduation_project_group_2_mobileworld.dto.thongKe;

import java.math.BigDecimal;

public class TopSellingProductDTO {
    private String imageUrl;
    private String productName;
    private BigDecimal price;
    private int soldQuantity;

    public TopSellingProductDTO() {
    }

    public TopSellingProductDTO(String imageUrl, String productName, BigDecimal price, int soldQuantity) {
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.price = price;
        this.soldQuantity = soldQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}
