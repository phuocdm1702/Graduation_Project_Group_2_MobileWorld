package com.example.graduation_project_group_2_mobileworld.service.nhan_vien;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class    EmailServices {
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
        try {
            // Tạo MimeMessage
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Thiết lập thông tin email
            helper.setTo(to);
            helper.setSubject("🎉 Chào mừng bạn gia nhập MobileWorld!");
            helper.setFrom("no-reply@mobileworld.com.vn");

            // Nội dung HTML của email
            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="vi">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Chào Mừng Nhân Viên Mới</title>
                        <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@700&display=swap" rel="stylesheet">
                        <style>
                            body {
                                margin: 0;
                                padding: 0;
                                font-family: Arial, sans-serif;
                                background-color: #f5f6fa;
                            }
                            .container {
                                width: 100%;
                                max-width: 600px;
                                margin: 0 auto;
                                padding: 20px;
                            }
                            .header {
                                background-color: #f5a623; /* Màu cam của MobileWorld */
                                padding: 20px;
                                text-align: center;
                                border-radius: 8px 8px 0 0;
                            }
                            .header h1 {
                                margin: 0;
                                font-size: 24px;
                                color: #ffffff;
                                font-weight: bold;
                            }
                            .content {
                                background-color: #ffffff;
                                padding: 20px;
                                border-radius: 0 0 8px 8px;
                                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
                                text-align: center;
                            }
                            .thank-you-section {
                                text-align: center;
                            }
                            .thank-you-section h2 {
                                font-family: 'Dancing Script', cursive;
                                font-size: 36px;
                                color: #f5a623; /* Màu cam của MobileWorld */
                                margin: 0;
                                line-height: 1.2;
                            }
                            .thank-you-section p {
                                color: #4a4a4a;
                                font-size: 14px;
                                margin: 8px 0 20px;
                            }
                            .coupon-details {
                                background-color: #fff5e6;
                                padding: 15px;
                                border-radius: 8px;
                                margin-bottom: 20px;
                                text-align: left;
                            }
                            .coupon-details p {
                                margin: 8px 0;
                                color: #4a4a4a;
                                font-size: 14px;
                            }
                            .coupon-details p strong {
                                color: #f5a623;
                                font-weight: bold;
                            }
                            .discount-box {
                                background-color: #f5a623;
                                color: #ffffff;
                                padding: 10px;
                                border-radius: 8px;
                                font-size: 18px;
                                font-weight: bold;
                                margin-bottom: 20px;
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
                            }
                            .footer {
                                text-align: center;
                                padding: 20px 0;
                                color: #4a4a4a;
                                font-size: 12px;
                            }
                            .footer a {
                                color: #f5a623;
                                text-decoration: none;
                            }

                            /* Responsive cho tablet (576px - 768px) */
                            @media only screen and (max-width: 768px) {
                                .container {
                                    padding: 15px;
                                }
                                .header h1 {
                                    font-size: 20px;
                                }
                                .thank-you-section h2 {
                                    font-size: 30px;
                                }
                                .thank-you-section p {
                                    font-size: 13px;
                                }
                                .coupon-details p {
                                    font-size: 13px;
                                }
                                .discount-box {
                                    font-size: 16px;
                                }
                                .cta-button {
                                    padding: 10px 20px;
                                    font-size: 14px;
                                }
                            }

                            /* Responsive cho mobile (dưới 576px) */
                            @media only screen and (max-width: 575px) {
                                .container {
                                    padding: 10px;
                                }
                                .header h1 {
                                    font-size: 18px;
                                }
                                .thank-you-section h2 {
                                    font-size: 24px;
                                }
                                .thank-you-section p {
                                    font-size: 12px;
                                }
                                .coupon-details p {
                                    font-size: 12px;
                                }
                                .discount-box {
                                    font-size: 14px;
                                    padding: 8px;
                                }
                                .cta-button {
                                    padding: 8px 16px;
                                    font-size: 12px;
                                }
                                .footer {
                                    font-size: 10px;
                                }
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h1>MobileWorld</h1>
                            </div>
                            <div class="content">
                                <div class="thank-you-section">
                                    <h2>Chào mừng!</h2>
                                    <p>Xin chào {employeeName}, bạn đã chính thức gia nhập đội ngũ MobileWorld</p>
                                </div>
                                <div class="discount-box">
                                    Chúc mừng bạn trở thành nô lê mới!
                                </div>
                                <div class="coupon-details">
                                    <p><strong>Tên đăng nhập:</strong> {email}</p>
                                    <p><strong>Mật khẩu:</strong> {password}</p>
                                    <p>Lưu ý: Vui lòng đăng nhập để kích hoạt tài khoản của bạn. Nếu có bất kỳ vấn đề nào, hãy liên hệ với chúng tôi qua email lequangphuc2872006@gmail.com</p>
                                </div>
                                <p>
                                    <a href="http://localhost:3000/login" class="cta-button">ĐĂNG NHẬP NGAY</a>
                                </p>
                            </div>
                            <div class="footer">
                                <p>Chúng tôi rất mong chờ được làm việc cùng bạn!</p>
                                <p>Trân trọng, <strong>MobileWorld</strong></p>
                                <p>Liên hệ: <a href="lequangphuc2872006@gmail.com">lequangphuc2872006@gmail.com</a></p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """;

            // Thay thế các placeholder trong HTML
            String finalHtmlContent = htmlContent
                    .replace("{employeeName}", employeeName)
                    .replace("{email}", email)
                    .replace("{password}", password);

            // Log nội dung HTML để kiểm tra
            System.out.println("Nội dung HTML trước khi gửi: " + finalHtmlContent);

            // Nội dung plain-text (dự phòng nếu HTML không hiển thị)
            String plainTextContent = """
                    Chào mừng bạn gia nhập MobileWorld!

                    Xin chào %s,

                    Bạn đã chính thức trở thành nô lê của MobileWorld. Dưới đây là thông tin tài khoản của bạn:
                    - Tên đăng nhập: %s
                    - Mật khẩu: %s

                    Lưu ý: Vui lòng đăng nhập để kích hoạt tài khoản của bạn. Nếu có bất kỳ vấn đề nào, hãy liên hệ với chúng tôi qua email lequangphuc2872006@gmail.com

                    Nhấn vào liên kết để đăng nhập ngay: http://localhost:3000/login

                    Trân trọng,
                    MobileWorld
                    Liên hệ: lequangphuc2872006@gmail.com
                    """.formatted(employeeName, email, password);

            // Log nội dung plain-text để kiểm tra
            System.out.println("Nội dung Plain Text trước khi gửi: " + plainTextContent);

            // Thiết lập nội dung HTML và plain-text
            helper.setText(plainTextContent, finalHtmlContent);

            // Gửi email
            mailSender.send(message);
            System.out.println("Email chào mừng đã được gửi tới: " + to);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email chào mừng tới " + to + ": " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void EmailKH(String to, String employeeName, String email, String password) throws MessagingException {
        try {
            // Tạo MimeMessage
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Thiết lập thông tin email
            helper.setTo(to);
            helper.setSubject("🎉 Chào mừng bạn trở thành khách hàng của MobileWorld!");
            helper.setFrom("no-reply@mobileworld.com.vn");

            // Nội dung HTML của email
            String htmlContent = """
                <!DOCTYPE html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Chào Mừng Khách Hàng</title>
                    <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@700&display=swap" rel="stylesheet">
                    <style>
                        body {
                            margin: 0;
                            padding: 0;
                            font-family: Arial, sans-serif;
                            background-color: #f5f6fa;
                        }
                        .container {
                            width: 100%;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .header {
                            background-color: #f5a623; /* Màu cam của MobileWorld */
                            padding: 20px;
                            text-align: center;
                            border-radius: 8px 8px 0 0;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 24px;
                            color: #ffffff;
                            font-weight: bold;
                        }
                        .content {
                            background-color: #ffffff;
                            padding: 20px;
                            border-radius: 0 0 8px 8px;
                            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
                            text-align: center;
                        }
                        .thank-you-section {
                            text-align: center;
                        }
                        .thank-you-section h2 {
                            font-family: 'Dancing Script', cursive;
                            font-size: 36px;
                            color: #f5a623; /* Màu cam của MobileWorld */
                            margin: 0;
                            line-height: 1.2;
                        }
                        .thank-you-section p {
                            color: #4a4a4a;
                            font-size: 14px;
                            margin: 8px 0 20px;
                        }
                        .account-details {
                            background-color: #fff5e6;
                            padding: 15px;
                            border-radius: 8px;
                            margin-bottom: 20px;
                            text-align: left;
                        }
                        .account-details p {
                            margin: 8px 0;
                            color: #4a4a4a;
                            font-size: 14px;
                        }
                        .account-details p strong {
                            color: #f5a623;
                            font-weight: bold;
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
                        }
                        .footer {
                            text-align: center;
                            padding: 20px 0;
                            color: #4a4a4a;
                            font-size: 12px;
                        }
                        .footer a {
                            color: #f5a623;
                            text-decoration: none;
                        }

                        /* Responsive cho tablet (576px - 768px) */
                        @media only screen and (max-width: 768px) {
                            .container {
                                padding: 15px;
                            }
                            .header h1 {
                                font-size: 20px;
                            }
                            .thank-you-section h2 {
                                font-size: 30px;
                            }
                            .thank-you-section p {
                                font-size: 13px;
                            }
                            .account-details p {
                                font-size: 13px;
                            }
                            .cta-button {
                                padding: 10px 20px;
                                font-size: 14px;
                            }
                        }

                        /* Responsive cho mobile (dưới 576px) */
                        @media only screen and (max-width: 575px) {
                            .container {
                                padding: 10px;
                            }
                            .header h1 {
                                font-size: 18px;
                            }
                            .thank-you-section h2 {
                                font-size: 24px;
                            }
                            .thank-you-section p {
                                font-size: 12px;
                            }
                            .account-details p {
                                font-size: 12px;
                            }
                            .cta-button {
                                padding: 8px 16px;
                                font-size: 12px;
                            }
                            .footer {
                                font-size: 10px;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>MobileWorld</h1>
                        </div>
                        <div class="content">
                            <div class="thank-you-section">
                                <h2>Chào mừng!</h2>
                                <p>Xin chào {employeeName}, bạn đã chính thức trở thành khách hàng của MobileWorld</p>
                            </div>
                            <div class="account-details">
                                <p><strong>Tên đăng nhập:</strong> {email}</p>
                                <p><strong>Mật khẩu:</strong> {password}</p>
                                <p>Lưu ý: Vui lòng đăng nhập để kích hoạt tài khoản của bạn. Nếu có bất kỳ vấn đề nào, hãy liên hệ với chúng tôi qua email lequangphuc2872006@gmail.com</p>
                            </div>
                            <p>
                                <a href="http://localhost:3000/login" class="cta-button">ĐĂNG NHẬP NGAY</a>
                            </p>
                        </div>
                        <div class="footer">
                            <p>Chúng tôi rất mong được đồng hành cùng bạn!</p>
                            <p>Trân trọng, <strong>MobileWorld</strong></p>
                            <p>Liên hệ: <a href="mailto:lequangphuc2872006@gmail.com">lequangphuc2872006@gmail.com</a></p>
                        </div>
                    </div>
                </body>
                </html>
                """;

            // Thay thế các placeholder trong HTML
            String finalHtmlContent = htmlContent
                    .replace("{employeeName}", employeeName)
                    .replace("{email}", email)
                    .replace("{password}", password);

            // Log nội dung HTML để kiểm tra
            System.out.println("Nội dung HTML trước khi gửi: " + finalHtmlContent);

            // Nội dung plain-text (dự phòng nếu HTML không hiển thị)
            String plainTextContent = """
                Chào mừng bạn trở thành khách hàng của MobileWorld!

                Xin chào %s,

                Bạn đã chính thức trở thành khách hàng của MobileWorld. Dưới đây là thông tin tài khoản của bạn:
                - Tên đăng nhập: %s
                - Mật khẩu: %s

                Lưu ý: Vui lòng đăng nhập để kích hoạt tài khoản của bạn. Nếu có bất kỳ vấn đề nào, hãy liên hệ với chúng tôi qua email lequangphuc2872006@gmail.com

                Nhấn vào liên kết để đăng nhập ngay: http://localhost:3000/login

                Trân trọng,
                MobileWorld
                Liên hệ: support@mobileworld.com.vn
                """.formatted(employeeName, email, password);

            // Log nội dung plain-text để kiểm tra
            System.out.println("Nội dung Plain Text trước khi gửi: " + plainTextContent);

            // Thiết lập nội dung HTML và plain-text
            helper.setText(plainTextContent, finalHtmlContent);

            // Gửi email
            mailSender.send(message);
            System.out.println("Email chào mừng đã được gửi tới: " + to);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email chào mừng tới " + to + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

