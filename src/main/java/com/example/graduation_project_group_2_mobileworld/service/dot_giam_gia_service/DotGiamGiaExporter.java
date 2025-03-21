package com.example.graduation_project_group_2_mobileworld.service.dot_giam_gia_service;

import com.example.graduation_project_group_2_mobileworld.entity.ChiTietDotGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.DotGiamGia;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class DotGiamGiaExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet1;
    private XSSFSheet sheet2;
    private List<DotGiamGia> listDGG;
    private List<ChiTietDotGiamGia> listCT;

    public DotGiamGiaExporter(List<DotGiamGia> listDGG, List<ChiTietDotGiamGia> listCT) {
        this.listDGG = listDGG;
        this.listCT = listCT;
        workbook = new XSSFWorkbook();
        sheet1 = workbook.createSheet("dot_giam_gia");
        sheet2 = workbook.createSheet("chi_tiet_dot_giam_gia");

    }

    private void writeHeaderRow1() {
        Row row = sheet1.createRow(0);
        String[] headers = {"ID", "Mã","Tên đợt giảm giá","Loại giảm giá áp dụng","Giá trị giảm giá","Số tiền giảm tối đa", "Ngày Bắt Đầu", "Ngày Kết Thúc","Trạng thái","Deleted"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    private void writeHeaderRow2() {
        Row row = sheet2.createRow(0);
        String[] headers = {"ID", "ID Đợt Giảm giá","ID Sản phẩm","Mã","Giá ban đầu","Giá sau khi giảm","Deleted"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    private void writeDataRow() {
        int rowNum=1;

        for(DotGiamGia dgg:listDGG){
            Row row=sheet1.createRow(rowNum++);
            row.createCell(0).setCellValue(dgg.getId());
            row.createCell(1).setCellValue(dgg.getMa());
            row.createCell(2).setCellValue(dgg.getTenDotGiamGia());
            row.createCell(3).setCellValue(dgg.getLoaiGiamGiaApDung());
            row.createCell(4).setCellValue(dgg.getGiaTriGiamGia().doubleValue());
            row.createCell(5).setCellValue(dgg.getSoTienGiamToiDa().doubleValue());
            row.createCell(6).setCellValue(dgg.getNgayBatDau());
            row.createCell(7).setCellValue(dgg.getNgayKetThuc());
            row.createCell(8).setCellValue(dgg.getTrangThai());
            row.createCell(9).setCellValue(dgg.getDeleted());
        }
    }

    private void writeDataRow2() {
        int rowNum=1;

        for(ChiTietDotGiamGia ct:listCT){
            Row row=sheet2.createRow(rowNum++);
            row.createCell(0).setCellValue(ct.getId());
            row.createCell(1).setCellValue(ct.getIdDotGiamGia().getId());
            row.createCell(2).setCellValue(ct.getIdChiTietSanPham().getId());
            row.createCell(3).setCellValue(ct.getMa());
            row.createCell(4).setCellValue(ct.getGiaBanDau().doubleValue());
            row.createCell(5).setCellValue(ct.getGiaSauKhiGiam().doubleValue());
            row.createCell(6).setCellValue(ct.getDeleted());
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderRow1();
        writeDataRow();

        writeHeaderRow2();
        writeDataRow2();

        ServletOutputStream outputStream= response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
