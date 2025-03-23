package com.example.graduation_project_group_2_mobileworld.service.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonChiTietDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class XuatDanhSachHoaDon {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet1; // Sheet cho danh sách hóa đơn
    private List<HoaDonDTO> listHD;
    private List<HoaDonChiTietDTO> listCT;
    private Map<Integer, String> hoaDonToSheetMap; // Map để lưu tên sheet chi tiết của mỗi hóa đơn

    public XuatDanhSachHoaDon(XSSFWorkbook workbook, XSSFSheet sheet1, List<HoaDonDTO> listHD, List<HoaDonChiTietDTO> listCT) {
        this.workbook = workbook;
        this.sheet1 = sheet1;
        this.listHD = listHD;
        this.listCT = listCT;
        this.hoaDonToSheetMap = new HashMap<>();
    }

    // Ghi tiêu đề cho sheet1 (danh sách hóa đơn)
    private void writeHeaderRow1() {
        Row row = sheet1.createRow(0);
        String[] headers = {"ID", "Mã", "Nhân viên", "Khách hàng", "SDT", "Tổng giá trị", "Tiền giảm", "Phí", "Thời gian Tạo", "Loại đơn", "Trạng thái"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    // Ghi tiêu đề cho sheet chi tiết hóa đơn
    private void writeHeaderRowForDetailSheet(XSSFSheet detailSheet) {
        Row row = detailSheet.createRow(0);
        String[] headers = {"ID", "ID Hóa Đơn", "Sản phẩm", "Imel", "Đơn giá"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    // Ghi dữ liệu cho sheet1 (danh sách hóa đơn) và thêm hyperlink
    private void writeDataRow() {
        int rowNum = 1;

        for (HoaDonDTO dto : listHD) {
            Row row = sheet1.createRow(rowNum++);
            // Cột ID: Thêm hyperlink trỏ đến sheet chi tiết
            Cell idCell = row.createCell(0);
            idCell.setCellValue(dto.getId());

            // Tạo hyperlink nếu có sheet chi tiết tương ứng
            if (hoaDonToSheetMap.containsKey(dto.getId())) {
                String sheetName = hoaDonToSheetMap.get(dto.getId());
                // Tạo địa chỉ ô trong sheet chi tiết (ví dụ: 'ChiTietHoaDon_1'!A1)
                String address = "'" + sheetName + "'!A1";
                Hyperlink hyperlink = workbook.getCreationHelper().createHyperlink(HyperlinkType.DOCUMENT);
                hyperlink.setAddress(address);
                idCell.setHyperlink(hyperlink);

                // Định dạng cell để hiển thị giống hyperlink (màu xanh, gạch chân)
                CellStyle hyperlinkStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setUnderline(Font.U_SINGLE);
                font.setColor(IndexedColors.BLUE.getIndex());
                hyperlinkStyle.setFont(font);
                idCell.setCellStyle(hyperlinkStyle);
            }

            // Các cột còn lại
            row.createCell(1).setCellValue(dto.getMa());
            row.createCell(2).setCellValue(dto.getIdNhanVien() != null ? dto.getIdNhanVien().getMa() : "N/A");
            row.createCell(3).setCellValue(dto.getTenKhachHang() != null ? dto.getTenKhachHang() : "N/A");
            row.createCell(4).setCellValue(dto.getSoDienThoaiKhachHang());
            row.createCell(5).setCellValue(dto.getTongTienSauGiam() != null ? dto.getTongTienSauGiam().doubleValue() : 0.0);
            row.createCell(6).setCellValue(dto.getIdPhieuGiamGia() != null ? dto.getIdPhieuGiamGia().getPhanTramGiamGia() : 0);
            row.createCell(7).setCellValue(dto.getPhiVanChuyen() != null ? dto.getPhiVanChuyen().doubleValue() : 0.0);
            row.createCell(8).setCellValue(dto.getNgayTao() != null ? dto.getNgayTao().toString() : "N/A");
            row.createCell(9).setCellValue(dto.getLoaiDon());
            row.createCell(10).setCellValue(dto.getTrangThai());
        }
    }

    // Ghi dữ liệu cho các sheet chi tiết hóa đơn (mỗi hóa đơn một sheet)
    private void writeDetailSheets() {
        // Nhóm chi tiết hóa đơn theo idHoaDon
        Map<Integer, List<HoaDonChiTietDTO>> chiTietByHoaDon = listCT.stream()
                .collect(Collectors.groupingBy(HoaDonChiTietDTO::getIdHoaDon));

        // Tạo sheet chi tiết cho từng hóa đơn
        for (Map.Entry<Integer, List<HoaDonChiTietDTO>> entry : chiTietByHoaDon.entrySet()) {
            Integer hoaDonId = entry.getKey();
            List<HoaDonChiTietDTO> chiTietList = entry.getValue();

            // Tạo sheet mới cho hóa đơn này
            String sheetName = "ChiTietHoaDon_" + hoaDonId;
            XSSFSheet detailSheet = workbook.createSheet(sheetName);
            hoaDonToSheetMap.put(hoaDonId, sheetName);

            // Ghi tiêu đề cho sheet chi tiết
            writeHeaderRowForDetailSheet(detailSheet);

            // Ghi dữ liệu chi tiết hóa đơn
            int rowNum = 1;
            for (HoaDonChiTietDTO dtoHDCT : chiTietList) {
                Row row = detailSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dtoHDCT.getId());
                row.createCell(1).setCellValue(dtoHDCT.getIdHoaDon());
                row.createCell(2).setCellValue(dtoHDCT.getIdChiTietSanPham() != null && dtoHDCT.getIdChiTietSanPham().getIdSanPham() != null
                        ? dtoHDCT.getIdChiTietSanPham().getIdSanPham().getTenSanPham() : "N/A");
                row.createCell(3).setCellValue(dtoHDCT.getIdImelDaBan() != null ? dtoHDCT.getIdImelDaBan().getImel() : "N/A");
                row.createCell(4).setCellValue(dtoHDCT.getGia() != null ? dtoHDCT.getGia().doubleValue() : 0.0);
            }
        }
    }

    // Phương thức export: Ghi file Excel trực tiếp vào response
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderRow1();
        writeDetailSheets(); // Tạo các sheet chi tiết trước để có tên sheet
        writeDataRow();      // Ghi sheet1 sau để thêm hyperlink dựa trên tên sheet

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}