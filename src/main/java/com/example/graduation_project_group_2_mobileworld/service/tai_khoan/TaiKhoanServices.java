package com.example.graduation_project_group_2_mobileworld.service.tai_khoan;

import com.example.graduation_project_group_2_mobileworld.dto.tai_khoan.TaiKhoanDTO;
import com.example.graduation_project_group_2_mobileworld.entity.QuyenHan;
import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import com.example.graduation_project_group_2_mobileworld.repository.tai_khoan.TaiKhoanRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service

public class TaiKhoanServices {
    private final TaiKhoanRepository taiKhoanRepository;
    private final JavaMailSenderImpl mailSender;
    private final Map<String, String> otpStorage = new HashMap<>();
    public TaiKhoanServices(TaiKhoanRepository taiKhoanRepository, JavaMailSenderImpl mailSender) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.mailSender = mailSender;
    }

    public String MaTaiKhoan() {
        String maxMaTK = taiKhoanRepository.findMaxMaTK();
        if (maxMaTK != null) {
            int nextNumber = Integer.parseInt(maxMaTK.substring(2)) + 1;
            return String.format("TK%05d", nextNumber);
        } else {
            return "TK00001";
        }
    }

    public TaiKhoan add(TaiKhoan taiKhoan) {
        return taiKhoanRepository.save(taiKhoan);
    }

    public String findById(Integer idTK) {
        Optional<TaiKhoan> tk = taiKhoanRepository.findById(idTK);
        return tk != null ? tk.get().getEmail() : null;
    }

    public TaiKhoan login(String tenDangNhap, String matKhau) {
        TaiKhoan tk = taiKhoanRepository.findByTenDangNhap(tenDangNhap);
        if (tk != null && tk.getMatKhau().equals(matKhau)) {
            return tk; // Trả về tài khoản nếu mật khẩu khớp
        }
        return null; // Trả về null nếu không khớp
    }

    private String RandomSDT() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    public String requestOTP(TaiKhoanDTO taiKhoanDTO) {
        // Kiểm tra email trùng
        if (taiKhoanRepository.findByEmail(taiKhoanDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        if (taiKhoanRepository.findBytenDangNhap(taiKhoanDTO.getTenDangNhap()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã được sử dụng!");
        }

        String otp = RandomSDT();
        otpStorage.put(taiKhoanDTO.getEmail(), otp);
        sendOTP(taiKhoanDTO.getEmail(), otp);
        return "OTP đã được gửi đến email của bạn!";
    }

    private void sendOTP(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP Xác Nhận Đăng Ký");
        message.setText("Mã OTP của bạn là: " + otp + "\nVui lòng sử dụng mã này để hoàn tất đăng ký.");
        mailSender.send(message);
    }

    public TaiKhoan addTK(TaiKhoanDTO taiKhoanDTO,String otp) {
        String storedOtp = otpStorage.get(taiKhoanDTO.getEmail());
        if (storedOtp == null || !storedOtp.equals(otp)) {
            throw new RuntimeException("Mã OTP không hợp lệ hoặc đã hết hạn!");
        }

        if (taiKhoanRepository.findByEmail(taiKhoanDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        if (taiKhoanRepository.findBytenDangNhap(taiKhoanDTO.getTenDangNhap()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã được sử dụng!");
        }

        otpStorage.remove(taiKhoanDTO.getEmail());

        QuyenHan quyenHan = new QuyenHan();
        quyenHan.setId(2);

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setIdQuyenHan(quyenHan);
        taiKhoan.setMa(MaTaiKhoan());
        taiKhoan.setTenDangNhap(taiKhoanDTO.getTenDangNhap());
        taiKhoan.setMatKhau(taiKhoanDTO.getMatKhau());
        taiKhoan.setEmail(taiKhoanDTO.getEmail());
        taiKhoan.setDeleted(false);

        return taiKhoanRepository.save(taiKhoan);
    }
}
