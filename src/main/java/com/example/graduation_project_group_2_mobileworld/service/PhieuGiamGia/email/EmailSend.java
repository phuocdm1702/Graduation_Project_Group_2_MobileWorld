package com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSend {
    private final JavaMailSender mailSender;

    public EmailSend(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendDiscountEmail(String toEmail, String maPhieu, String tenPhieu, String ngayHetHan) {
        try {
            // Tạo MimeMessage
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Thiết lập thông tin email
            helper.setTo(toEmail);
            helper.setSubject("🎉 Thông báo: Bạn nhận được phiếu giảm giá mới!");

            // Nội dung HTML của email
            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="vi">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Phiếu Giảm Giá Mới</title>
                        <style>
                            body {
                                margin: 0;
                                padding: 0;
                                font-family: 'Arial', sans-serif;
                                background-color: #f5f6fa;
                            }
                            .container {
                                width: 900px;
                                margin: 0 auto;
                                padding: 20px;
                            }
                            .header {
                                display: flex;
                                align-items: center;
                                justify-content: space-between;
                                padding: 20px;
                                background-color: #f5a623;
                                color: #ffffff;
                                border-radius: 8px 8px 0 0;
                            }
                            .header img {
                                max-width: 120px; /* Tăng kích thước logo */
                                height: auto;
                            }
                            .header h1 {
                                margin: 0;
                                font-size: 28px; /* Tăng kích thước font chữ */
                                font-weight: bold;
                            }
                            .content {
                                background-color: #ffffff;
                                padding: 30px;
                                border-radius: 0 0 8px 8px;
                                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
                            }
                            .content h2 {
                                color: #4a4a4a;
                                font-size: 22px; /* Tăng font chữ */
                                margin-bottom: 20px;
                            }
                            .coupon-details {
                                background-color: #f9fafb;
                                padding: 20px;
                                border-radius: 8px;
                                margin-bottom: 20px;
                            }
                            .coupon-details p {
                                margin: 8px 0;
                                color: #4a4a4a;
                                font-size: 16px;
                            }
                            .coupon-details p strong {
                                color: #f5a623;
                            }
                            .cta-button {
                                display: inline-block;
                                padding: 12px 24px;
                                background-color: #f5a623;
                                color: #ffffff;
                                text-decoration: none;
                                border-radius: 8px;
                                font-size: 16px;
                                font-weight: 500;
                                transition: background-color 0.3s ease;
                            }
                            .cta-button:hover {
                                background-color: #e69520;
                            }
                            .footer {
                                text-align: center;
                                padding: 20px 0;
                                color: #4a4a4a;
                                font-size: 14px;
                            }
                            .footer a {
                                color: #f5a623;
                                text-decoration: none;
                            }
                            
                            @media only screen and (max-width: 575px) {
                                    .html-content {
                                        display: none !important;
                                    }
                                    .plain-text-content {
                                        display: block !important;
                                    }
                                }
                                @media only screen and (min-width: 576px) {
                                    .html-content {
                                        display: block !important;
                                    }
                                    .plain-text-content {
                                        display: none !important;
                                    }
                                }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h1>Mobile World</h1>
                            </div>
                            <div class="content">
                                <h2>Chào bạn,</h2>
                                <p>Bạn vừa nhận được một phiếu giảm giá mới từ Mobile World! Dưới đây là thông tin chi tiết:</p>
                                <div class="coupon-details">
                                    <p><strong>Mã phiếu:</strong> %s</p>
                                    <p><strong>Tên phiếu:</strong> %s</p>
                                    <p><strong>Hạn sử dụng:</strong> %s</p>
                                </div>
                                <p style="text-align: center;">
                                    <a href="http://localhost:3000/phieu-giam-gia" class="cta-button">Sử dụng ngay</a>
                                </p>
                            </div>
                            <div class="footer">
                                <p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>
                                <p>Trân trọng, <strong>Mobile World</strong></p>
                                <p>Liên hệ: <a href="mailto:support@mobileworld.com">support@mobileworld.com</a></p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """.formatted(maPhieu, tenPhieu, ngayHetHan);

            // Thiết lập nội dung HTML
            helper.setText(htmlContent, true);

            // Gửi email
            mailSender.send(message);
            System.out.println("Email HTML đã được gửi tới: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email HTML tới " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}