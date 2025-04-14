package com.example.graduation_project_group_2_mobileworld.service.ban_hang_service;

import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDCTban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.ChiTietGioHangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.ChiTietSPDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.GioHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.repository.gio_hang.ChiTietGioHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.gio_hang.GioHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.hinh_thuc_thanh_toan_repo.HinhThucThanhToanRepository;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don.HoaDonChiTietRepository;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don.HoaDonRepository;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.phuong_thuc_thanh_toan.PhuongThucThanhToanRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ChiTietSanPhamRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BanHangService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private ChiTietGioHangRepository chiTietGioHangRepository;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private HinhThucThanhToanRepository hinhThucThanhToanRepository;

    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    public List<HDban_hangDTO> getAllHD() {
        List<HoaDon> listHD = hoaDonRepository.findAllHDNotConfirm();
        return listHD.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<GioHangDTO> getAllGH(GioHangDTO gioHangDTO) {
        return gioHangRepository.findAll().stream()
                .filter(gioHang -> !gioHang.getDeleted())
                .map(this::convertToGioHangDTO)
                .collect(Collectors.toList());
    }

    public GioHangDTO addGioHang(GioHangDTO gioHangDTO) {
        GioHang gioHang = new GioHang();
        gioHang.setMa(gioHangDTO.getMa());

        KhachHang khachHang = khachHangRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng mặc định"));
        gioHang.setIdKhachHang(khachHang);

        gioHang.setDeleted(false);
        gioHang = gioHangRepository.save(gioHang);
        return convertToGioHangDTO(gioHang);
    }

    public List<ChiTietSPDTO> getAllCTSP() {
        return chiTietSanPhamRepository.findAll().stream()
                .filter(chiTietSanPham -> !chiTietSanPham.getDeleted())
                .map(this::convertToChiTietSanPhamDTO)
                .collect(Collectors.toList());
    }

    public ChiTietGioHangDTO addChiTietGioHang(Integer gioHangId, Integer chiTietSanPhamId, Integer hoaDonId) {
        GioHang gioHang = gioHangRepository.findById(gioHangId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giỏ hàng với ID: " + gioHangId));
        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(chiTietSanPhamId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + chiTietSanPhamId));
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));

        ChiTietGioHang chiTietGioHang = new ChiTietGioHang();
        chiTietGioHang.setIdGioHang(gioHang);
        chiTietGioHang.setIdChiTietSanPham(chiTietSanPham);
        chiTietGioHang.setMa("CTGH" + System.currentTimeMillis());
        chiTietGioHang.setTrangThai((short) 0);
        chiTietGioHang.setTongTien(chiTietSanPham.getGiaBan());
        chiTietGioHang.setDeleted(false);
        chiTietGioHang = chiTietGioHangRepository.save(chiTietGioHang);

        HoaDonChiTiet hoaDonChiTiet = HoaDonChiTiet.builder()
                .hoaDon(hoaDon)
                .idChiTietSanPham(chiTietSanPham)
                .ma("HDCT" + System.currentTimeMillis())
                .gia(chiTietSanPham.getGiaBan())
                .trangThai((short) 0)
                .deleted(false)
                .build();
        hoaDonChiTietRepository.save(hoaDonChiTiet);

        return convertToChiTietGioHangDTO(chiTietGioHang);
    }

    public List<ChiTietGioHangDTO> getChiTietGioHangByGioHangId(Integer gioHangId) {
        return chiTietGioHangRepository.findAll().stream()
                .filter(ctgh -> ctgh.getIdGioHang().getId().equals(gioHangId) && !ctgh.getDeleted())
                .map(this::convertToChiTietGioHangDTO)
                .collect(Collectors.toList());
    }

    public void deleteChiTietGioHang(Integer chiTietGioHangId) {
        ChiTietGioHang chiTietGioHang = chiTietGioHangRepository.findById(chiTietGioHangId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết giỏ hàng với ID: " + chiTietGioHangId));
        chiTietGioHang.setDeleted(true);
        chiTietGioHangRepository.save(chiTietGioHang);

        // Cập nhật HoaDonChiTiet tương ứng (nếu cần)
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(chiTietGioHangId)
                .orElse(null);
        if (hoaDonChiTiet != null) {
            hoaDonChiTiet.setDeleted(true);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        }
    }

    public HoaDon addHD(HoaDon hoaDon) {
        return hoaDonRepository.save(hoaDon);
    }

    public void thanhToan(Integer hoaDonId, Long totalPrice, Long discount, String paymentMethod, Long tienChuyenKhoan, Long tienMat) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));

        // Cập nhật thông tin hóa đơn
        hoaDon.setTongTien(BigDecimal.valueOf(totalPrice));
        hoaDon.setTongTienSauGiam(BigDecimal.valueOf(totalPrice - discount));
        hoaDon.setTrangThai((short) 1); // 1: Đã thanh toán
        hoaDon.setNgayThanhToan(new Date());
        hoaDon.setUpdatedAt(new Date());
        hoaDonRepository.save(hoaDon);

        // Xác định kiểu thanh toán
        String kieuThanhToan;
        switch (paymentMethod) {
            case "transfer":
                kieuThanhToan = "Chuyển khoản";
                break;
            case "cash":
                kieuThanhToan = "Tiền mặt";
                break;
            case "both":
                kieuThanhToan = "Cả hai phương thức";
                break;
            default:
                throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " + paymentMethod);
        }

        // Tìm PhuongThucThanhToan theo kieuThanhToan
        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository.findByKieuThanhToan(kieuThanhToan)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phương thức thanh toán: " + kieuThanhToan));

        // Lưu phương thức thanh toán
        HinhThucThanhToan hinhThucThanhToan = new HinhThucThanhToan();
        hinhThucThanhToan.setHoaDon(hoaDon);
        hinhThucThanhToan.setIdPhuongThucThanhToan(phuongThucThanhToan);
        hinhThucThanhToan.setMa("HTTT" + System.currentTimeMillis());
        hinhThucThanhToan.setTienChuyenKhoan(BigDecimal.valueOf(tienChuyenKhoan));
        hinhThucThanhToan.setTienMat(BigDecimal.valueOf(tienMat));
        hinhThucThanhToan.setDeleted(false);
        hinhThucThanhToanRepository.save(hinhThucThanhToan);
    }

    private HDban_hangDTO mapToDTO(HoaDon hd) {
        HDban_hangDTO dto = new HDban_hangDTO();
        dto.setId(hd.getId());
        dto.setMa(hd.getMa());
        dto.setStatus(hd.getTrangThai());
        dto.setItems(hd.getChiTietHoaDon().stream().map(this::mapChiTietToDTO).collect(Collectors.toList()));
        return dto;
    }

    private HDCTban_hangDTO mapChiTietToDTO(HoaDonChiTiet chiTiet) {
        HDCTban_hangDTO dto = new HDCTban_hangDTO();
        dto.setId(chiTiet.getId());
        dto.setName(chiTiet.getIdChiTietSanPham() != null && chiTiet.getIdChiTietSanPham().getIdSanPham() != null
                ? chiTiet.getIdChiTietSanPham().getIdSanPham().getTenSanPham()
                : "Unknown");
        dto.setImei(chiTiet.getIdImelDaBan() != null ? chiTiet.getIdImelDaBan().getImel() : "N/A");
        dto.setPrice(chiTiet.getGia() != null ? chiTiet.getGia().longValue() : 0L);
        return dto;
    }

    private GioHangDTO convertToGioHangDTO(GioHang gioHang) {
        GioHangDTO dto = new GioHangDTO();
        dto.setId(gioHang.getId());
        dto.setMa(gioHang.getMa());
        dto.setIdKhachHang(gioHang.getIdKhachHang() != null ? gioHang.getIdKhachHang().getId() : null);
        dto.setChiTietGioHangs(chiTietGioHangRepository.findAll().stream()
                .filter(ctgh -> ctgh.getIdGioHang().getId().equals(gioHang.getId()) && !ctgh.getDeleted())
                .map(this::convertToChiTietGioHangDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private ChiTietSPDTO convertToChiTietSanPhamDTO(ChiTietSanPham chiTietSanPham) {
        ChiTietSPDTO dto = new ChiTietSPDTO();
        dto.setId(chiTietSanPham.getId());
        dto.setMa(chiTietSanPham.getMa());
        dto.setTenSanPham(chiTietSanPham.getIdSanPham().getTenSanPham());
        dto.setGiaBan(chiTietSanPham.getGiaBan());
        dto.setImel(chiTietSanPham.getIdImel().getMa());
        return dto;
    }

    private ChiTietGioHangDTO convertToChiTietGioHangDTO(ChiTietGioHang chiTietGioHang) {
        ChiTietGioHangDTO dto = new ChiTietGioHangDTO();
        dto.setId(chiTietGioHang.getId());
        dto.setIdGioHang(chiTietGioHang.getIdGioHang().getId());
        dto.setIdChiTietSanPham(chiTietGioHang.getIdChiTietSanPham().getId());
        dto.setMa(chiTietGioHang.getMa());
        dto.setTenSanPham(chiTietGioHang.getIdChiTietSanPham().getIdSanPham().getTenSanPham());
        dto.setTongTien(chiTietGioHang.getTongTien());
        dto.setImel(chiTietGioHang.getIdChiTietSanPham().getIdImel().getMa());
        return dto;
    }
}