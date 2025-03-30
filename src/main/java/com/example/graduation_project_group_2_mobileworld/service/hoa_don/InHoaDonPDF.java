package com.example.graduation_project_group_2_mobileworld.service.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hinh_thuc_thanh_toan.HinhThucThanhToanDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonChiTietDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InHoaDonPDF {
    private static final Logger logger = LoggerFactory.getLogger(InHoaDonPDF.class);

    public byte[] generateHoaDonPdf(HoaDonDTO hoaDon) throws Exception {
        logger.info("Starting to generate PDF for HoaDon: {}", hoaDon.getId());
        if (hoaDon == null) {
            logger.error("HoaDonDTO is null");
            throw new IllegalArgumentException("Hóa đơn không được null");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ClassPathResource reportResource = new ClassPathResource("reports/hoa_don.jrxml");
            if (!reportResource.exists()) {
                logger.error("File hoa_don.jrxml không tồn tại trong thư mục resources/reports");
                throw new Exception("File hoa_don.jrxml không tồn tại trong thư mục resources/reports");
            }

            try (InputStream reportStream = reportResource.getInputStream()) {
                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("maHoaDon", hoaDon.getMa() != null ? hoaDon.getMa() : "N/A");
                parameters.put("tenKhachHang", hoaDon.getTenKhachHang() != null ? hoaDon.getTenKhachHang() : "N/A");
                parameters.put("tenNhanVien", hoaDon.getIdNhanVien() != null && hoaDon.getIdNhanVien().getTenNhanVien() != null ? hoaDon.getIdNhanVien().getTenNhanVien() : "N/A");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                parameters.put("ngayBan", hoaDon.getNgayTao() != null ? dateFormat.format(hoaDon.getNgayTao()) : "N/A");
                parameters.put("soDienThoai", hoaDon.getSoDienThoaiKhachHang() != null ? hoaDon.getSoDienThoaiKhachHang() : "N/A");
                parameters.put("diaChi", hoaDon.getDiaChiKhachHang() != null ? hoaDon.getDiaChiKhachHang() : "N/A");

                // Xử lý hình thức thanh toán từ List<HinhThucThanhToanDTO>
                String hinhThucThanhToanStr = "N/A";
                if (hoaDon.getHinhThucThanhToan() != null && !hoaDon.getHinhThucThanhToan().isEmpty()) {
                    List<String> paymentMethods = new ArrayList<>();
                    for (HinhThucThanhToanDTO httt : hoaDon.getHinhThucThanhToan()) {
                        // Giả sử HinhThucThanhToanDTO có phương thức getTenPhuongThuc() để lấy tên phương thức
                        String method = (httt.getIdPhuongThucThanhToan() != null && httt.getIdPhuongThucThanhToan().getKieuThanhToan() != null)
                                ? httt.getIdPhuongThucThanhToan().getKieuThanhToan()
                                : "Không xác định";
                        paymentMethods.add(method);
                    }
                    hinhThucThanhToanStr = String.join(", ", paymentMethods); // Nối các phương thức nếu có nhiều
                }
                parameters.put("hinhThucThanhToan", hinhThucThanhToanStr);

                parameters.put("tongTien", hoaDon.getTongTien() != null ? hoaDon.getTongTien() : BigDecimal.ZERO);
                parameters.put("tongTienSauGiam", hoaDon.getTongTienSauGiam() != null ? hoaDon.getTongTienSauGiam() : BigDecimal.ZERO);

                Double tienGiam = 0.0;
                Double phanTramGiam = 0.0;
                if (hoaDon.getIdPhieuGiamGia() != null) {
                    if (hoaDon.getIdPhieuGiamGia().getSoTienGiamToiDa() != null) {
                        tienGiam = hoaDon.getIdPhieuGiamGia().getSoTienGiamToiDa().doubleValue();
                    }
                    if (hoaDon.getIdPhieuGiamGia().getPhanTramGiamGia() != null) {
                        phanTramGiam = hoaDon.getIdPhieuGiamGia().getPhanTramGiamGia().doubleValue();
                    }
                }
                parameters.put("tienGiam", tienGiam);
                parameters.put("phanTramGiam", phanTramGiam);

                // Tạo mã QR với logo bên trong
                BufferedImage logoQrImage = generateQRWithLogo(hoaDon.getMa());
                parameters.put("logoQrImage", logoQrImage);

                List<HoaDonChiTietDTO> chiTietList = hoaDon.getChiTietHoaDon();
                List<HoaDonChiTietDTO> chiTietListWithStt = new ArrayList<>();

                if (chiTietList != null && !chiTietList.isEmpty()) {
                    int stt = 1;
                    for (HoaDonChiTietDTO chiTiet : chiTietList) {
                        HoaDonChiTietDTO chiTietWithStt = new HoaDonChiTietDTO();
                        chiTietWithStt.setStt(stt++);
                        chiTietWithStt.setTenSanPham(chiTiet.getIdChiTietSanPham() != null && chiTiet.getIdChiTietSanPham().getIdSanPham() != null
                                ? chiTiet.getIdChiTietSanPham().getIdSanPham().getTenSanPham() : "N/A");
                        chiTietWithStt.setImel(chiTiet.getIdImelDaBan() != null ? chiTiet.getIdImelDaBan().getImel() : "N/A");
                        chiTietWithStt.setMauSac(chiTiet.getIdChiTietSanPham() != null && chiTiet.getIdChiTietSanPham().getIdMauSac() != null
                                ? chiTiet.getIdChiTietSanPham().getIdMauSac().getMauSac() : "N/A");
                        chiTietWithStt.setBoNho(chiTiet.getIdChiTietSanPham() != null ? chiTiet.getIdChiTietSanPham().getIdBoNhoTrong().getDungLuongBoNhoTrong() : "N/A");
                        chiTietWithStt.setGia(chiTiet.getGia() != null ? chiTiet.getGia() : BigDecimal.ZERO);

                        chiTietListWithStt.add(chiTietWithStt);
                    }
                } else {
                    logger.warn("Danh sách chi tiết hóa đơn trống cho HoaDon: {}", hoaDon.getId());
                }

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(chiTietListWithStt);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                JasperExportManager.exportReportToPdfStream(jasperPrint, baos);

                logger.info("PDF generated successfully for HoaDon: {}", hoaDon.getId());
                return baos.toByteArray();
            }

        } catch (JRException e) {
            logger.error("Lỗi khi tạo PDF với JasperReports: {}", e.getMessage(), e);
            throw new Exception("Không thể tạo PDF: " + e.getMessage(), e);
        } finally {
            baos.close();
        }
    }

    // Tạo mã QR với logo lớn hơn và rõ hơn
    private BufferedImage generateQRWithLogo(String maHoaDon) throws Exception {
        // Chuẩn bị nội dung QR (mã hóa đơn)
        String qrCodeText = maHoaDon != null ? maHoaDon : "N/A";
        int qrSize = 250; // Kích thước QR 250x250
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // Cấu hình QR với mức sửa lỗi cao và viền trắng
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // Mức sửa lỗi cao (30%)
        hints.put(EncodeHintType.MARGIN, 4); // Viền trắng tiêu chuẩn
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // Hỗ trợ ký tự tiếng Việt

        // Tạo BitMatrix cho QR
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, qrSize, qrSize, hints);

        // Kích thước logo là 25% (62px với QR 250x250)
        int logoSize = qrSize / 4; // 25% kích thước QR
        int padding = 1; // Padding nhỏ để giữ dữ liệu QR
        int whiteAreaSize = logoSize + padding * 2; // Vùng trắng bao quanh logo
        // Đặt logo ở chính giữa QR
        int whiteAreaXOffset = (qrSize - whiteAreaSize) / 2; // Căn giữa theo chiều ngang
        int whiteAreaYOffset = (qrSize - whiteAreaSize) / 2; // Căn giữa theo chiều dọc

        // Xóa dữ liệu QR trong vùng trắng hình tròn ở giữa
        for (int x = 0; x < qrSize; x++) {
            for (int y = 0; y < qrSize; y++) {
                int centerX = whiteAreaXOffset + whiteAreaSize / 2;
                int centerY = whiteAreaYOffset + whiteAreaSize / 2;
                double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                if (distance <= whiteAreaSize / 2) {
                    bitMatrix.unset(x, y); // Xóa chấm QR trong vùng logo
                }
            }
        }

        // Tạo hình ảnh QR từ BitMatrix (QR và vùng trắng trên cùng một layer)
        BufferedImage qrImage = new BufferedImage(qrSize, qrSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dQr = qrImage.createGraphics();
        g2dQr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ QR với vùng trắng
        for (int x = 0; x < qrSize; x++) {
            for (int y = 0; y < qrSize; y++) {
                qrImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        // Tải logo
        ClassPathResource logoResource = new ClassPathResource("static/images/Logo_Mobile_World_vector.png");
        BufferedImage logoImage = ImageIO.read(logoResource.getInputStream());

        // Tạo logo với nền tròn
        BufferedImage circularLogo = new BufferedImage(logoSize, logoSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dCircular = circularLogo.createGraphics();
        g2dCircular.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2dCircular.setColor(Color.BLACK); // Nền đen để logo nổi bật
        g2dCircular.fillOval(0, 0, logoSize, logoSize);

        // Tăng kích thước logo bên trong nền tròn lên 90% để rõ hơn
        int logoInnerSize = (int) (logoSize * 0.9); // 90% để logo gọn và rõ
        int logoInnerOffset = (logoSize - logoInnerSize) / 2;
        g2dCircular.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2dCircular.drawImage(logoImage, logoInnerOffset, logoInnerOffset, logoInnerSize, logoInnerSize, null);
        g2dCircular.dispose();

        // Căn logo ở chính giữa QR
        int xOffset = (qrSize - logoSize) / 2; // Căn giữa theo chiều ngang
        int yOffset = (qrSize - logoSize) / 2; // Căn giữa theo chiều dọc

        // Vẽ logo lên QR (trên cùng một layer)
        g2dQr.drawImage(circularLogo, xOffset, yOffset, null); // Vẽ logo trực tiếp
        g2dQr.dispose();

        // Lưu ảnh QR để kiểm tra (tuỳ chọn)
        // ImageIO.write(qrImage, "PNG", new File("qr_test_" + qrCodeText + ".png"));
        logger.info("Generated QR code with logo for maHoaDon: {}", qrCodeText);

        return qrImage;
    }
}