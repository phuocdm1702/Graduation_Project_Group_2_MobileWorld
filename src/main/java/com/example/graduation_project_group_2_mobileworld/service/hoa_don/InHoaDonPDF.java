package com.example.graduation_project_group_2_mobileworld.service.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonChiTietDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

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

        // Tạo ByteArrayOutputStream để lưu PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            // Tải file template .jrxml từ thư mục resources
            ClassPathResource reportResource = new ClassPathResource("reports/hoa_don.jrxml");

            // Kiểm tra xem file có tồn tại không
            if (!reportResource.exists()) {
                logger.error("File hoa_don.jrxml không tồn tại trong thư mục resources/reports");
                throw new Exception("File hoa_don.jrxml không tồn tại trong thư mục resources/reports");
            }

            // Đọc file .jrxml từ InputStream và biên dịch thành JasperReport
            try (InputStream reportStream = reportResource.getInputStream()) {
                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

                // Chuẩn bị dữ liệu cho template
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("maHoaDon", hoaDon.getMa() != null ? hoaDon.getMa() : "N/A");
                parameters.put("tenKhachHang", hoaDon.getTenKhachHang() != null ? hoaDon.getTenKhachHang() : "N/A");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                parameters.put("ngayBan", hoaDon.getNgayTao() != null ? dateFormat.format(hoaDon.getNgayTao()) : "N/A");
                parameters.put("tenNhanVien", hoaDon.getIdNhanVien() != null && hoaDon.getIdNhanVien().getTenNhanVien() != null ? hoaDon.getIdNhanVien().getTenNhanVien() : "N/A");
                parameters.put("soDienThoai", hoaDon.getSoDienThoaiKhachHang() != null ? hoaDon.getSoDienThoaiKhachHang() : "N/A");
                parameters.put("diaChi", hoaDon.getDiaChiKhachHang() != null ? hoaDon.getDiaChiKhachHang() : "N/A");
                parameters.put("tongTien", hoaDon.getTongTien() != null ? hoaDon.getTongTien() : 0);
                parameters.put("tongTienSauGiam", hoaDon.getTongTienSauGiam() != null ? hoaDon.getTongTienSauGiam() : 0);

                // Chuẩn bị dữ liệu cho bảng chi tiết sản phẩm với stt tự tăng
                List<HoaDonChiTietDTO> chiTietList = hoaDon.getChiTietHoaDon();
                List<HoaDonChiTietDTO> chiTietListWithStt = new ArrayList<>();

                if (chiTietList != null && !chiTietList.isEmpty()) {
                    int stt = 1; // Bắt đầu từ 1
                    for (HoaDonChiTietDTO chiTiet : chiTietList) {
                        HoaDonChiTietDTO chiTietWithStt = new HoaDonChiTietDTO();
                        chiTietWithStt.setStt(stt++); // Gán stt tự tăng
                        // Tên sản phẩm từ ChiTietSanPham
                        chiTietWithStt.setTenSanPham(chiTiet.getIdChiTietSanPham() != null && chiTiet.getIdChiTietSanPham().getIdSanPham() != null
                                ? chiTiet.getIdChiTietSanPham().getIdSanPham().getTenSanPham() : "N/A");
                        // IMEI từ ImelDaBan
                        chiTietWithStt.setImel(chiTiet.getIdImelDaBan() != null ? chiTiet.getIdImelDaBan().getImel() : "N/A");
                        // Đơn giá từ gia trong HoaDonChiTietDTO
                        chiTietWithStt.setGia(chiTiet.getGia() != null ? chiTiet.getGia() : BigDecimal.ZERO);
                        chiTietListWithStt.add(chiTietWithStt);
                    }
                } else {
                    logger.warn("Danh sách chi tiết hóa đơn trống cho HoaDon: {}", hoaDon.getId());
                }

                // Tạo JRBeanCollectionDataSource từ danh sách đã thêm stt
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(chiTietListWithStt);

                // Tạo JasperPrint
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

                // Xuất PDF
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
}