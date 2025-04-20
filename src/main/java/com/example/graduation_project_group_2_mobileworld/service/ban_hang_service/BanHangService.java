package com.example.graduation_project_group_2_mobileworld.service.ban_hang_service;

import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDCTban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.ChiTietGioHangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.ChiTietSPDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.GioHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.SanPham;
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

    public List<GioHangDTO> getAllGH() {
        return gioHangRepository.findAll().stream()
                .filter(gioHang -> !gioHang.getDeleted())
                .map(this::convertToGioHangDTO)
                .collect(Collectors.toList());
    }

    public GioHangDTO addGioHang(GioHangDTO gioHangDTO) {
        GioHang gioHang = new GioHang();
        gioHang.setMa(gioHangDTO.getMa());

        KhachHang khachHang = khachHangRepository.findById(gioHangDTO.getIdKhachHang() != null ? gioHangDTO.getIdKhachHang() : 1)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
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

    public List<String> getIMEIsBySanPhamId(Integer sanPhamId) {
        return chiTietSanPhamRepository.findAll().stream()
                .filter(ctsp -> ctsp.getIdSanPham().getId().equals(sanPhamId) && !ctsp.getDeleted())
                .map(ctsp -> ctsp.getIdImel().getMa())
                .collect(Collectors.toList());
    }

    public ChiTietGioHangDTO addChiTietGioHang(Integer gioHangId, Integer chiTietSanPhamId, Integer hoaDonId) {
        GioHang gioHang = gioHangRepository.findById(gioHangId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giỏ hàng với ID: " + gioHangId));
        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(chiTietSanPhamId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết sản phẩm với ID: " + chiTietSanPhamId));
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));

        BigDecimal giaBan = chiTietSanPham.getGiaBan();
        BigDecimal tongTien = giaBan; // Số lượng = 1 vì mỗi ChiTietSanPham tương ứng với 1 sản phẩm

        ChiTietGioHang chiTietGioHang = new ChiTietGioHang();
        chiTietGioHang.setIdGioHang(gioHang);
        chiTietGioHang.setIdChiTietSanPham(chiTietSanPham);
        chiTietGioHang.setMa("CTGH" + System.currentTimeMillis());
        chiTietGioHang.setTrangThai((short) 0);
        chiTietGioHang.setTongTien(tongTien);
        chiTietGioHang.setDeleted(false);
        chiTietGioHang = chiTietGioHangRepository.save(chiTietGioHang);

        HoaDonChiTiet hoaDonChiTiet = HoaDonChiTiet.builder()
                .hoaDon(hoaDon)
                .idChiTietSanPham(chiTietSanPham)
                .ma("HDCT" + System.currentTimeMillis())
                .gia(giaBan)
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

        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findAll().stream()
                .filter(hdct -> hdct.getIdChiTietSanPham().getId().equals(chiTietGioHangId))
                .collect(Collectors.toList());
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
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

        hoaDon.setTongTien(BigDecimal.valueOf(totalPrice));
        hoaDon.setTongTienSauGiam(BigDecimal.valueOf(totalPrice - discount));
        hoaDon.setTrangThai((short) 1);
        hoaDon.setNgayThanhToan(new Date());
        hoaDon.setUpdatedAt(new Date());
        hoaDonRepository.save(hoaDon);

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

        PhuongThucThanhToan phuongThucThanhToan = new PhuongThucThanhToan();
        phuongThucThanhToan.setMa("PTTT" + System.currentTimeMillis());
        phuongThucThanhToan.setKieuThanhToan(kieuThanhToan);
        phuongThucThanhToan.setDeleted(false);
        phuongThucThanhToan = phuongThucThanhToanRepository.save(phuongThucThanhToan);

        HinhThucThanhToan hinhThucThanhToan = new HinhThucThanhToan();
        hinhThucThanhToan.setHoaDon(hoaDon);
        hinhThucThanhToan.setIdPhuongThucThanhToan(phuongThucThanhToan);
        hinhThucThanhToan.setTienChuyenKhoan(BigDecimal.valueOf(tienChuyenKhoan));
        hinhThucThanhToan.setTienMat(BigDecimal.valueOf(tienMat));
        hinhThucThanhToan.setMa("HTTT" + System.currentTimeMillis());
        hinhThucThanhToan.setDeleted(false);
        hinhThucThanhToanRepository.save(hinhThucThanhToan);

        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findAll().stream()
                .filter(hdct -> hdct.getHoaDon().getId().equals(hoaDonId))
                .collect(Collectors.toList());
        for (HoaDonChiTiet hdct : hoaDonChiTiets) {
            ChiTietSanPham ctsp = hdct.getIdChiTietSanPham();
            ctsp.setDeleted(true);
            chiTietSanPhamRepository.save(ctsp);
        }
    }

    private HDban_hangDTO mapToDTO(HoaDon hoaDon) {
        HDban_hangDTO dto = new HDban_hangDTO();
        dto.setId(hoaDon.getId());
        dto.setMaHoaDon(hoaDon.getMa());
        dto.setTrangThai(hoaDon.getTrangThai());

        List<HDCTban_hangDTO> items = hoaDonChiTietRepository.findAll().stream()
                .filter(hdct -> hdct.getHoaDon().getId().equals(hoaDon.getId()))
                .map(hdct -> {
                    HDCTban_hangDTO itemDTO = new HDCTban_hangDTO();
                    itemDTO.setId(hdct.getId());
                    itemDTO.setTenSanPham(hdct.getIdChiTietSanPham().getIdSanPham().getTenSanPham());
                    itemDTO.setImei(hdct.getIdChiTietSanPham().getIdImel().getMa());
                    itemDTO.setGiaBan(hdct.getGia());
                    return itemDTO;
                })
                .collect(Collectors.toList());
        dto.setItems(items);

        return dto;
    }

    private GioHangDTO convertToGioHangDTO(GioHang gioHang) {
        GioHangDTO dto = new GioHangDTO();
        dto.setId(gioHang.getId());
        dto.setMa(gioHang.getMa());
        dto.setIdKhachHang(gioHang.getIdKhachHang().getId());
        dto.setTrangThai((short) 0);
        dto.setChiTietGioHangList(
                chiTietGioHangRepository.findAll().stream()
                        .filter(ctgh -> ctgh.getIdGioHang().getId().equals(gioHang.getId()))
                        .map(this::convertToChiTietGioHangDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    private ChiTietGioHangDTO convertToChiTietGioHangDTO(ChiTietGioHang chiTietGioHang) {
        ChiTietGioHangDTO dto = new ChiTietGioHangDTO();
        dto.setId(chiTietGioHang.getId());
        dto.setIdGioHang(chiTietGioHang.getIdGioHang().getId());
        dto.setIdChiTietSanPham(chiTietGioHang.getIdChiTietSanPham().getId());
        dto.setMa(chiTietGioHang.getMa());
        dto.setTenSanPham(chiTietGioHang.getIdChiTietSanPham().getIdSanPham().getTenSanPham());
        dto.setImei(chiTietGioHang.getIdChiTietSanPham().getIdImel().getMa());
        dto.setGiaBan(chiTietGioHang.getIdChiTietSanPham().getGiaBan());
        dto.setTongTien(chiTietGioHang.getTongTien());
        return dto;
    }

    private ChiTietSPDTO convertToChiTietSanPhamDTO(ChiTietSanPham chiTietSanPham) {
        ChiTietSPDTO dto = new ChiTietSPDTO();
        dto.setId(chiTietSanPham.getId());
        dto.setMa(chiTietSanPham.getIdSanPham().getMa());
        dto.setTenSanPham(chiTietSanPham.getIdSanPham().getTenSanPham());
        dto.setGiaBan(chiTietSanPham.getGiaBan());
        dto.setImei(chiTietSanPham.getIdImel().getMa());
        dto.setMauSac(chiTietSanPham.getIdMauSac().getMauSac());
        dto.setRam(chiTietSanPham.getIdRam().getDungLuongRam());
        dto.setBoNhoTrong(chiTietSanPham.getIdBoNhoTrong().getDungLuongBoNhoTrong());
        return dto;
    }
}