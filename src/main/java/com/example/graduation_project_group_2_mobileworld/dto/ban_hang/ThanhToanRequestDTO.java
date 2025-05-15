package com.example.graduation_project_group_2_mobileworld.dto.ban_hang;
public class ThanhToanRequestDTO {
    private Long totalPrice;
    private Long discount;
    private String paymentMethod;
    private Long tienChuyenKhoan;
    private Long tienMat;
    private Boolean isDelivery;
    private ReceiverDTO receiver;

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getTienChuyenKhoan() {
        return tienChuyenKhoan;
    }

    public void setTienChuyenKhoan(Long tienChuyenKhoan) {
        this.tienChuyenKhoan = tienChuyenKhoan;
    }

    public Long getTienMat() {
        return tienMat;
    }

    public void setTienMat(Long tienMat) {
        this.tienMat = tienMat;
    }

    public Boolean getIsDelivery() {
        return isDelivery;
    }

    public void setIsDelivery(Boolean isDelivery) {
        this.isDelivery = isDelivery;
    }

    public ReceiverDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverDTO receiver) {
        this.receiver = receiver;
    }

    public static class ReceiverDTO {
        private String name;
        private String phone;
        private String city;
        private String district;
        private String ward;
        private String address;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getWard() {
            return ward;
        }

        public void setWard(String ward) {
            this.ward = ward;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}