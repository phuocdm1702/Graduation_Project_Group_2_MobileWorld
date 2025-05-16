package com.example.graduation_project_group_2_mobileworld.controller.ban_hang;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSendBH {

    private final JavaMailSender mailSender;

    public EmailSendBH(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private static final String EMAIL_CSS = """
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
                background-color: #f5a623;
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
                color: #f5a623;
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
            """;

    // Các phương thức hiện có (giữ nguyên)

    // Phương thức mới cho thanh toán kèm lời chúc
    public void sendPaymentConfirmationEmail(String toEmail, String orderCode, String totalAmount, String paymentMethod, String date) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("✅ Xác nhận thanh toán đơn hàng từ MobileWorld");

            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="vi">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Xác nhận Thanh toán</title>
                        <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@700&display=swap" rel="stylesheet">
                        <style>
                            %s
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h1>MobileWorld</h1>
                            </div>
                            <div class="content">
                                <div class="thank-you-section">
                                    <h2>Cảm ơn bạn!</h2>
                                    <p>Đơn hàng của bạn đã được thanh toán thành công. Chúc bạn một ngày vui vẻ và mua sắm hiệu quả!</p>
                                </div>
                                <div class="coupon-details">
                                    <p><strong>Mã đơn hàng:</strong> %s</p>
                                    <p><strong>Tổng tiền:</strong> %s đ</p>
                                    <p><strong>Phương thức thanh toán:</strong> %s</p>
                                    <p><strong>Thời gian thanh toán:</strong> %s</p>
                                    <p>Chúng tôi đã ghi nhận giao dịch của bạn. Vui lòng kiểm tra thông tin và liên hệ nếu có thắc mắc.</p>
                                </div>
                                <p>
                                    <a href="http://localhost:3000/don-hang" class="cta-button">XEM ĐƠN HÀNG</a>
                                </p>
                            </div>
                            <div class="footer">
                                <p>Cảm ơn bạn đã mua sắm tại MobileWorld!</p>
                                <p>Trân trọng, <strong>MobileWorld</strong></p>
                                <p>Liên hệ: <a href="mailto:support@mobileworld.com.vn">support@mobileworld.com.vn</a></p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """.formatted(EMAIL_CSS, orderCode, totalAmount, paymentMethod, date);

            String plainTextContent = """
                    Cảm ơn bạn đã mua sắm tại MobileWorld!

                    Đơn hàng của bạn đã được thanh toán thành công. Chúc bạn một ngày vui vẻ và mua sắm hiệu quả!

                    - Mã đơn hàng: %s
                    - Tổng tiền: %s đ
                    - Phương thức thanh toán: %s
                    - Thời gian thanh toán: %s

                    Chúng tôi đã ghi nhận giao dịch của bạn. Vui lòng kiểm tra thông tin và liên hệ nếu có thắc mắc.
                    Nhấn vào liên kết để xem đơn hàng: http://localhost:3000/don-hang

                    Trân trọng,
                    MobileWorld
                    Liên hệ: support@mobileworld.com.vn
                    """.formatted(orderCode, totalAmount, paymentMethod, date);

            helper.setText(plainTextContent, htmlContent);
            mailSender.send(message);
            System.out.println("Email xác nhận thanh toán đã được gửi tới: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email xác nhận thanh toán tới " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

}
