package com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.email;

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

    public void sendDiscountEmail(String toEmail, String maPhieu, String ngayHetHan, double phanTram, double STGTD) {
        try {
            // Tạo MimeMessage
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Thiết lập thông tin email
            helper.setTo(toEmail);
            helper.setSubject("🎉 Cảm ơn bạn! Phiếu giảm giá từ MobileWorld");

            // Nội dung HTML của email
            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="vi">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Phiếu Giảm Giá Mới</title>
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
                            .coupon-details .coupon-code {
                                font-size: 18px;
                                font-weight: bold;
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
                                .coupon-details .coupon-code {
                                    font-size: 16px;
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
                                .coupon-details .coupon-code {
                                    font-size: 14px;
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
                                    <h2>Cảm ơn!</h2>
                                    <p>Quý khách đã đăng ký nhận tin email từ MobileWorld</p>
                                </div>
                                <div class="discount-box">
                                    Tặng quý khách ưu đãi {phanTram}% (Tối đa {STGTD}đ)
                                </div>
                                <div class="coupon-details">
                                    <p class="coupon-code"><strong>Mã phiếu:</strong> {maPhieu}</p>
                                    <p><strong>Hạn sử dụng:</strong> {ngayHetHan}</p>
                                    <p>Lưu ý: Mã chỉ sử dụng được 1 lần cho khách hàng có đăng ký nhận tin email từ MobileWorld (ứng với 1 số điện thoại đã đăng ký). Sử dụng mã giảm giá để được giảm giá trực tiếp, và tất cả mã giảm giá đều không có giá trị quy đổi thành tiền mặt.</p>
                                </div>
                                <p>
                                    <a href="http://localhost:3000/phieu-giam-gia" class="cta-button">MUA SẮM NGAY</a>
                                </p>
                            </div>
                            <div class="footer">
                                <p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>
                                <p>Trân trọng, <strong>MobileWorld</strong></p>
                                <p>Liên hệ: <a href="mailto:support@mobileworld.com.vn">support@mobileworld.com.vn</a></p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """;

            // Manually replace all placeholders
            String finalHtmlContent = htmlContent
                    .replace("{maPhieu}", maPhieu)
                    .replace("{ngayHetHan}", ngayHetHan)
                    .replace("{phanTram}", String.valueOf(phanTram))
                    .replace("{STGTD}", String.format("%,.0f", STGTD));

            // Log nội dung HTML để kiểm tra
            System.out.println("Nội dung HTML trước khi gửi: " + finalHtmlContent);

            // Nội dung plain-text (dự phòng nếu HTML không hiển thị)
            String plainTextContent = """
                    Cảm ơn bạn đã đăng ký nhận tin từ MobileWorld!

                    Dưới đây là thông tin phiếu giảm giá của bạn:
                    - Mã phiếu: %s
                    - Hạn sử dụng: %s
                    - Ưu đãi: %s%% (Tối đa %sđ)

                    Lưu ý: Mã chỉ sử dụng được 1 lần cho khách hàng có đăng ký nhận tin email từ MobileWorld (ứng với 1 số điện thoại đã đăng ký). Sử dụng mã giảm giá để được giảm giá trực tiếp, và tất cả mã giảm giá đều không có giá trị quy đổi thành tiền mặt.

                    Nhấn vào liên kết để mua sắm ngay: http://localhost:3000/phieu-giam-gia

                    Trân trọng,
                    MobileWorld
                    Liên hệ: support@mobileworld.com.vn
                    """.formatted(maPhieu, ngayHetHan, phanTram, String.format("%,.0f", STGTD));

            // Log nội dung plain-text để kiểm tra
            System.out.println("Nội dung Plain Text trước khi gửi: " + plainTextContent);

            // Thiết lập nội dung HTML và plain-text
            helper.setText(plainTextContent, finalHtmlContent);

            // Gửi email
            mailSender.send(message);
            System.out.println("Email HTML đã được gửi tới: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email HTML tới " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendUpdateDiscountEmail(String toEmail, String maPhieu, String ngayHetHan, double phanTram, double STGTD) {
        sendDiscountEmail(toEmail, maPhieu, ngayHetHan, phanTram, STGTD, "📢 Cập nhật phiếu giảm giá từ MobileWorld", """
                <div class="thank-you-section">
                    <h2>Cập nhật!</h2>
                    <p>Phiếu giảm giá của bạn đã được cập nhật thông tin mới từ MobileWorld.</p>
                </div>
                """, """
                Thông báo từ MobileWorld!

                Phiếu giảm giá của bạn đã được cập nhật:
                """);
    }

    // Gửi email thông báo thu hồi phiếu giảm giá (cho khách hàng bị xóa)
    public void sendRevokeDiscountEmail(String toEmail, String maPhieu) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("❌ Thông báo thu hồi phiếu giảm giá từ MobileWorld");

            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="vi">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Thu hồi Phiếu Giảm Giá</title>
                        <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@700&display=swap" rel="stylesheet">
                        <!-- CSS giống như mẫu trước đó -->
                        <style>
                            /* Giữ nguyên CSS từ mẫu ban đầu */
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h1>MobileWorld</h1>
                            </div>
                            <div class="content">
                                <div class="thank-you-section">
                                    <h2>Thông báo</h2>
                                    <p>Phiếu giảm giá của bạn đã bị thu hồi bởi MobileWorld.</p>
                                </div>
                                <div class="coupon-details">
                                    <p class="coupon-code"><strong>Mã phiếu:</strong> {maPhieu}</p>
                                    <p>Chúng tôi rất tiếc phải thông báo rằng phiếu giảm giá này không còn hiệu lực nữa.</p>
                                </div>
                                <p>
                                    <a href="http://localhost:3000/phieu-giam-gia" class="cta-button">Xem các ưu đãi khác</a>
                                </p>
                            </div>
                            <div class="footer">
                                <p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>
                                <p>Trân trọng, <strong>MobileWorld</strong></p>
                                <p>Liên hệ: <a href="mailto:support@mobileworld.com.vn">support@mobileworld.com.vn</a></p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """.replace("{maPhieu}", maPhieu);

            String plainTextContent = """
                    Thông báo từ MobileWorld!

                    Phiếu giảm giá của bạn đã bị thu hồi:
                    - Mã phiếu: %s

                    Chúng tôi rất tiếc phải thông báo rằng phiếu giảm giá này không còn hiệu lực nữa.
                    Nhấn vào liên kết để xem các ưu đãi khác: http://localhost:3000/phieu-giam-gia

                    Trân trọng,
                    MobileWorld
                    Liên hệ: support@mobileworld.com.vn
                    """.formatted(maPhieu);

            helper.setText(plainTextContent, htmlContent);
            mailSender.send(message);
            System.out.println("Email thu hồi đã được gửi tới: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email thu hồi tới " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hàm chung để gửi email giảm giá (dùng lại cho các trường hợp)
    private void sendDiscountEmail(String toEmail, String maPhieu, String ngayHetHan, double phanTram, double STGTD, String subject, String thankYouSection, String plainTextHeader) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);

            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="vi">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Phiếu Giảm Giá</title>
                        <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@700&display=swap" rel="stylesheet">
                        <!-- CSS giống như mẫu trước đó -->
                        <style>
                            /* Giữ nguyên CSS từ mẫu ban đầu */
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h1>MobileWorld</h1>
                            </div>
                            <div class="content">
                                {thankYouSection}
                                <div class="discount-box">
                                    Tặng quý khách ưu đãi {phanTram}% (Tối đa {STGTD}đ)
                                </div>
                                <div class="coupon-details">
                                    <p class="coupon-code"><strong>Mã phiếu:</strong> {maPhieu}</p>
                                    <p><strong>Hạn sử dụng:</strong> {ngayHetHan}</p>
                                    <p>Lưu ý: Mã chỉ sử dụng được 1 lần cho khách hàng có đăng ký nhận tin email từ MobileWorld (ứng với 1 số điện thoại đã đăng ký). Sử dụng mã giảm giá để được giảm giá trực tiếp, và tất cả mã giảm giá đều không có giá trị quy đổi thành tiền mặt.</p>
                                </div>
                                <p>
                                    <a href="http://localhost:3000/phieu-giam-gia" class="cta-button">MUA SẮM NGAY</a>
                                </p>
                            </div>
                            <div class="footer">
                                <p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>
                                <p>Trân trọng, <strong>MobileWorld</strong></p>
                                <p>Liên hệ: <a href="mailto:support@mobileworld.com.vn">support@mobileworld.com.vn</a></p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """;

            String finalHtmlContent = htmlContent
                    .replace("{thankYouSection}", thankYouSection)
                    .replace("{maPhieu}", maPhieu)
                    .replace("{ngayHetHan}", ngayHetHan)
                    .replace("{phanTram}", String.valueOf(phanTram))
                    .replace("{STGTD}", String.format("%,.0f", STGTD));

            String plainTextContent = """
                    %s
                    - Mã phiếu: %s
                    - Hạn sử dụng: %s
                    - Ưu đãi: %s%% (Tối đa %sđ)

                    Lưu ý: Mã chỉ sử dụng được 1 lần cho khách hàng có đăng ký nhận tin email từ MobileWorld (ứng với 1 số điện thoại đã đăng ký). Sử dụng mã giảm giá để được giảm giá trực tiếp, và tất cả mã giảm giá đều không có giá trị quy đổi thành tiền mặt.

                    Nhấn vào liên kết để mua sắm ngay: http://localhost:3000/phieu-giam-gia

                    Trân trọng,
                    MobileWorld
                    Liên hệ: support@mobileworld.com.vn
                    """.formatted(plainTextHeader, maPhieu, ngayHetHan, phanTram, String.format("%,.0f", STGTD));

            helper.setText(plainTextContent, finalHtmlContent);
            mailSender.send(message);
            System.out.println("Email đã được gửi tới: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email tới " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}