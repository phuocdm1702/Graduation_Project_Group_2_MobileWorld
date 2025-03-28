package com.example.graduation_project_group_2_mobileworld.service.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hinh_thuc_thanh_toan.HinhThucThanhToanDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonChiTietDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTO;
import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.LichSuHoaDonDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HinhThucThanhToan;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDonChiTiet;
import com.example.graduation_project_group_2_mobileworld.entity.LichSuHoaDon;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don.HoaDonRepository;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.source.ByteArrayOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class HoaDonService {
    private final HoaDonRepository hoaDonRepository;
    private final InHoaDonPDF inHoaDonPDF;


    public HoaDonService(HoaDonRepository hoaDonRepository, InHoaDonPDF inHoaDonPDF) {
        this.hoaDonRepository = hoaDonRepository;
        this.inHoaDonPDF = inHoaDonPDF;
    }

    public List<HoaDonDTO> getAllData() {
        return hoaDonRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList()
                ;
    }

    public Page<HoaDonDTO> getHoaDonWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hoaDonRepository.findAllWithPagination(pageable).map(this::toDTO);
    }

    public Page<HoaDonDTO> getHoaDonWithFilters(
            int page, int size, String keyword, String loaiDon, Long minAmount, Long maxAmount, String startDate, String endDate) {
        Pageable pageable = PageRequest.of(page, size);

        // Chuyển đổi ngày từ String sang Timestamp (nếu có)
        Timestamp start = (startDate != null && !startDate.isEmpty()) ?
                Timestamp.valueOf(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay()) : null;
        Timestamp end = (endDate != null && !endDate.isEmpty()) ?
                Timestamp.valueOf(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay()) : null;

        // Gọi repository với các tham số lọc
        return hoaDonRepository.findHoaDonWithFilters(keyword, loaiDon, minAmount, maxAmount, start, end, pageable)
                .map(this::toDTO);
    }


    // Chuyển từ HoaDon entity sang HoaDonDTO
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

        // Ánh xạ chi tiết hóa đơn
        if (hoaDon.getChiTietHoaDon() != null) {
            List<HoaDonChiTietDTO> chiTietDTOs = new ArrayList<>();
            for (HoaDonChiTiet cthd : hoaDon.getChiTietHoaDon()) {
                HoaDonChiTietDTO cthdDTO = new HoaDonChiTietDTO();
                cthdDTO.setId(cthd.getId());
                cthdDTO.setIdHoaDon(hoaDon.getId());
                cthdDTO.setIdChiTietSanPham(cthd.getIdChiTietSanPham());
                cthdDTO.setIdImelDaBan(cthd.getIdImelDaBan());
                cthdDTO.setMa(cthd.getMa());
                cthdDTO.setGia(cthd.getGia());
                cthdDTO.setTrangThai(cthd.getTrangThai());
                cthdDTO.setGhiChu(cthd.getGhiChu());
                chiTietDTOs.add(cthdDTO);
            }
            dto.setChiTietHoaDon(chiTietDTOs);
        }

        // Ánh xạ lịch sử hóa đơn
        if (hoaDon.getLichSuHoaDon() != null) {
            List<LichSuHoaDonDTO> lichSuDTOs = new ArrayList<>();
            for (LichSuHoaDon lshd : hoaDon.getLichSuHoaDon()) {
                LichSuHoaDonDTO lshdDTO = new LichSuHoaDonDTO();
                lshdDTO.setId(lshd.getId());
                lshdDTO.setIdHoaDon(hoaDon.getId());
                lshdDTO.setIdNhanVien(dto.getIdNhanVien());
                lshdDTO.setMa(lshd.getMa());
                lshdDTO.setHanhDong(lshd.getHanhDong());
                lshdDTO.setThoiGian(lshd.getThoiGian());
                lichSuDTOs.add(lshdDTO);
            }
            dto.setLichSuHoaDon(lichSuDTOs);
        }

        // Ánh xạ hình thức thanh toán
        if (hoaDon.getHinhThucThanhToan() != null) {
            List<HinhThucThanhToanDTO> htttDTOs = new ArrayList<>();
            for (HinhThucThanhToan httt : hoaDon.getHinhThucThanhToan()) {
                HinhThucThanhToanDTO htttDTO = new HinhThucThanhToanDTO();
                htttDTO.setId(httt.getId());
                htttDTO.setIdHoaDon(hoaDon.getId());
                htttDTO.setIdPhuongThucThanhToan(httt.getIdPhuongThucThanhToan());
                htttDTO.setTienChuyenKhoan(httt.getTienChuyenKhoan());
                htttDTO.setTienMat(httt.getTienMat());
                htttDTO.setMa(httt.getMa());
                htttDTO.setDeleted(httt.getDeleted());
                htttDTOs.add(htttDTO);
            }
            dto.setHinhThucThanhToan(htttDTOs);
        }

        return dto;
    }

    // Lấy cả chi tiết và lịch sử hóa đơn theo ID (nếu cần)
    public HoaDonDTO getFullHoaDonDetails(Integer id) {
        // Có thể cần thêm một phương thức mới trong repository để lấy cả chi tiết và lịch sử
        HoaDon hoaDon;

        // Gọi thêm lịch sử để đảm bảo đầy đủ dữ liệu
        hoaDon = hoaDonRepository.findHoaDonWithDetailsAndHistoryById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        return toDTO(hoaDon);
    }

    public byte[] generateHoaDonPdf(Integer id) throws Exception {
        HoaDonDTO hoaDon = getFullHoaDonDetails(id);
        return inHoaDonPDF.generateHoaDonPdf(hoaDon);
    }

    // Lấy hóa đơn theo ID
    public HoaDonDTO getHoaDonById(Integer id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + id));
        return toDTO(hoaDon);
    }

    public void exportHoaDonToExcel(HttpServletResponse response) throws IOException, java.io.IOException {
        List<HoaDonDTO> listHD = getAllData();

        List<HoaDonChiTietDTO> listCT = new ArrayList<>();
        for (HoaDonDTO hoaDon : listHD) {
            if (hoaDon.getChiTietHoaDon() != null) {
                listCT.addAll(hoaDon.getChiTietHoaDon());
            }
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet1 = workbook.createSheet("DanhSachHoaDon");

            XuatDanhSachHoaDon exporter = new XuatDanhSachHoaDon(workbook, sheet1, listHD, listCT);
            exporter.export(response);
        }
    }
    // Trong HoaDonService.java
    public HoaDonDTO getHoaDonByMa(String ma) {
        HoaDon hoaDon = hoaDonRepository.findByMa(ma)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + ma));
        return toDTO(hoaDon);
    }
}
