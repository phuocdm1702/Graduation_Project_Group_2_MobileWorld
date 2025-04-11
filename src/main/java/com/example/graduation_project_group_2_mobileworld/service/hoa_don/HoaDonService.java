package com.example.graduation_project_group_2_mobileworld.service.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hinh_thuc_thanh_toan.HinhThucThanhToanDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonChiTietDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTOGet;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.LichSuHoaDonDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don.HoaDonRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HoaDonService {

    private final HoaDonRepository hoaDonRepository;
    private final InHoaDonPDF inHoaDonPDF;

    public HoaDonService(HoaDonRepository hoaDonRepository, InHoaDonPDF inHoaDonPDF) {
        this.hoaDonRepository = hoaDonRepository;
        this.inHoaDonPDF = inHoaDonPDF;
    }

    // Lấy tất cả hóa đơn
    public List<HoaDonDTO> getAllData() {
        return hoaDonRepository.findAll().stream().map(this::toDTO).toList();
    }

    // Lấy hóa đơn với phân trang
    public Page<HoaDonDTO> getHoaDonWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hoaDonRepository.findAllWithPagination(pageable);
    }

    // Lấy hóa đơn với bộ lọc
//    public Page<HoaDonDTOGet> getHoaDonWithFilters(int page, int size, String keyword, String loaiDon, Long minAmount,
//                                                   Long maxAmount, String startDate, String endDate, Short trangThai) {
//        Pageable pageable = PageRequest.of(page, size);
//        Timestamp start = parseDateToTimestamp(startDate);
//        Timestamp end = parseDateToTimestamp(endDate);
//        return hoaDonRepository.findHoaDonWithFilters(keyword, loaiDon, minAmount, maxAmount, start, end, trangThai, pageable);
//    }

    // Lấy hóa đơn với bộ lọc
    public Page<HoaDonDTO> getHoaDonWithFilters(int page, int size, String keyword, String loaiDon, Long minAmount,
                                                Long maxAmount, String startDate, String endDate, Short trangThai) {
        Pageable pageable = PageRequest.of(page, size);
        Timestamp start = parseDateToTimestamp(startDate);
        Timestamp end = parseDateToTimestamp(endDate);
        return hoaDonRepository.findHoaDonWithFilters(keyword, loaiDon, minAmount, maxAmount, start, end, trangThai, pageable)
                .map(this::toDTO);
    }

    // Lấy chi tiết hóa đơn theo ID
    public HoaDonDTO getHoaDonById(Integer id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + id));
        return toDTO(hoaDon);
    }

    // Lấy chi tiết đầy đủ hóa đơn (bao gồm chi tiết và lịch sử)
    public HoaDonDTO getFullHoaDonDetails(Integer id) {
        HoaDon hoaDon = hoaDonRepository.findHoaDonWithDetailsAndHistoryById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));
        return toDTO(hoaDon);
    }

    // Tạo PDF hóa đơn
    public byte[] generateHoaDonPdf(Integer id) throws Exception {
        HoaDonDTO hoaDon = getFullHoaDonDetails(id);
        return inHoaDonPDF.generateHoaDonPdf(hoaDon);
    }

    // Xuất danh sách hóa đơn ra Excel
    public void exportHoaDonToExcel(HttpServletResponse response) throws IOException {
        List<HoaDonDTO> listHD = getAllData();
        List<HoaDonChiTietDTO> listCT = listHD.stream()
                .filter(hd -> hd.getChiTietHoaDon() != null)
                .flatMap(hd -> hd.getChiTietHoaDon().stream())
                .collect(Collectors.toList());

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XuatDanhSachHoaDon exporter = new XuatDanhSachHoaDon(workbook, workbook.createSheet("DanhSachHoaDon"), listHD, listCT);
            exporter.export(response);
        }
    }

    // Lấy hóa đơn theo mã
    public HoaDonDTO getHoaDonByMa(String ma) {
        HoaDonDTO hoaDon = hoaDonRepository.findByMa(ma)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + ma));
        return hoaDon;
    }

    // Đếm số lượng hóa đơn theo trạng thái
    public Map<Short, Long> getHoaDonStatusCount() {
        return hoaDonRepository.findAll().stream()
                .collect(Collectors.groupingBy(HoaDon::getTrangThai, Collectors.counting()));
    }

    // Chuyển từ entity sang DTO
    private HoaDonDTO toDTO(HoaDon hoaDon) {
        HoaDonDTO dto = new HoaDonDTO();
        dto.setId(hoaDon.getId());
        dto.setIdKhachHang(hoaDon.getIdKhachHang());
        dto.setIdNhanVien(hoaDon.getIdNhanVien());
        dto.setIdPhieuGiamGia(hoaDon.getIdPhieuGiamGia());
        dto.setMa(hoaDon.getMa());
        dto.setSoDienThoaiKhachHang(hoaDon.getSoDienThoaiKhachHang());
        dto.setLoaiDon(hoaDon.getLoaiDon());
        dto.setTongTien(hoaDon.getTongTien());
        dto.setNgayTao(hoaDon.getNgayTao());
        dto.setTongTienSauGiam(hoaDon.getTongTienSauGiam());
        dto.setTrangThai(hoaDon.getTrangThai());
        dto.setDiaChiKhachHang(hoaDon.getDiaChiKhachHang());
        dto.setTenKhachHang(hoaDon.getTenKhachHang());
        dto.setGhiChu(hoaDon.getGhiChu());
        dto.setPhiVanChuyen(hoaDon.getPhiVanChuyen());

        mapChiTietHoaDon(dto, hoaDon);
        mapLichSuHoaDon(dto, hoaDon);
        mapHinhThucThanhToan(dto, hoaDon);

        return dto;
    }

    // Ánh xạ chi tiết hóa đơn
    private void mapChiTietHoaDon(HoaDonDTO dto, HoaDon hoaDon) {
        if (hoaDon.getChiTietHoaDon() != null) {
            dto.setChiTietHoaDon(hoaDon.getChiTietHoaDon().stream().map(cthd -> {
                HoaDonChiTietDTO cthdDTO = new HoaDonChiTietDTO();
                cthdDTO.setId(cthd.getId());
                cthdDTO.setIdHoaDon(hoaDon.getId());
                cthdDTO.setIdChiTietSanPham(cthd.getIdChiTietSanPham());
                cthdDTO.setIdImelDaBan(cthd.getIdImelDaBan());
                cthdDTO.setMa(cthd.getMa());
                cthdDTO.setGia(cthd.getGia());
                cthdDTO.setTrangThai(cthd.getTrangThai());
                cthdDTO.setGhiChu(cthd.getGhiChu());
                return cthdDTO;
            }).collect(Collectors.toList()));
        }
    }

    // Ánh xạ lịch sử hóa đơn
    private void mapLichSuHoaDon(HoaDonDTO dto, HoaDon hoaDon) {
        if (hoaDon.getLichSuHoaDon() != null) {
            dto.setLichSuHoaDon(hoaDon.getLichSuHoaDon().stream().map(lshd -> {
                LichSuHoaDonDTO lshdDTO = new LichSuHoaDonDTO();
                lshdDTO.setId(lshd.getId());
                lshdDTO.setIdHoaDon(hoaDon.getId());
                lshdDTO.setIdNhanVien(dto.getIdNhanVien());
                lshdDTO.setMa(lshd.getMa());
                lshdDTO.setHanhDong(lshd.getHanhDong());
                lshdDTO.setThoiGian(lshd.getThoiGian());
                return lshdDTO;
            }).collect(Collectors.toList()));
        }
    }

    // Ánh xạ hình thức thanh toán
    private void mapHinhThucThanhToan(HoaDonDTO dto, HoaDon hoaDon) {
        if (hoaDon.getHinhThucThanhToan() != null) {
            dto.setHinhThucThanhToan(hoaDon.getHinhThucThanhToan().stream().map(httt -> {
                HinhThucThanhToanDTO htttDTO = new HinhThucThanhToanDTO();
                htttDTO.setId(httt.getId());
                htttDTO.setIdHoaDon(hoaDon.getId());
                htttDTO.setIdPhuongThucThanhToan(httt.getIdPhuongThucThanhToan());
                htttDTO.setTienChuyenKhoan(httt.getTienChuyenKhoan());
                htttDTO.setTienMat(httt.getTienMat());
                htttDTO.setMa(httt.getMa());
                htttDTO.setDeleted(httt.getDeleted());
                return htttDTO;
            }).collect(Collectors.toList()));
        }
    }

    // Chuyển đổi String ngày thành Timestamp
    private Timestamp parseDateToTimestamp(String date) {
        return (date != null && !date.isEmpty())
                ? Timestamp.valueOf(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay())
                : null;
    }
}