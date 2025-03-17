package com.example.graduation_project_group_2_mobileworld.service.nhan_vien;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class EmailServices {
    private final JavaMailSender mailSender;

    public EmailServices(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }

    public void sendWelcomeEmail(String to, String employeeName, String email, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Chúc mừng bạn đã trở thành nô lệ của tôi bạn sẽ phải phục vụ chúng tôi đến hết đời");
        helper.setFrom("lequangphuc2872006@gmail.com");

        String htmlContent = "<h3>Chúc mừng " + employeeName + "!</h3>" +
                "<p>Bạn đã được nhận vào làm nô lệ của MobileWorld.</p>" +
                "<p>Thông tin tài khoản của bạn:</p>" +
                "<ul>" +
                "<li>Tên đăng nhập: " + email + "</li>" +
                "<li>Mật khẩu: " + password + "</li>" +
                "</ul>" +
                "<p>Vui lòng đăng nhập xác nhận tài khoản có vấn đề gì hãy liên hệ lại với chúng tôi hoặc gửi lại email để giải đáp</p>" +
                "<p>Trân trọng,<br>Đội ngũ MobileWorld</p>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
