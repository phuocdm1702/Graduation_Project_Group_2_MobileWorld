package com.example.graduation_project_group_2_mobileworld.service.ban_hang_service;

import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.ChiTietSanPhamGroupDTO;
import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDCTban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.ThanhToanRequestDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.ChiTietGioHangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.GioHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ImelDaBan;
import com.example.graduation_project_group_2_mobileworld.repository.gio_hang.ChiTietGioHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.gio_hang.GioHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.hinh_thuc_thanh_toan_repo.HinhThucThanhToanRepository;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don.HoaDonChiTietRepository;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don.HoaDonRepository;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.phuong_thuc_thanh_toan.PhuongThucThanhToanRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ChiTietSanPhamRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.SanPhamRepository;
import com.example.graduation_project_group_2_mobileworld.repository.hoa_don.LichSuHoaDonRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ImelDaBanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private LichSuHoaDonRepository lichSuHoaDonRepository;

    @Autowired
    private ImelDaBanRepository imelDaBanRepository;

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

    @Cacheable(value = "sanPhamCache", key = "#page + '-' + #size + '-' + #keyword")
    public Page<ChiTietSanPhamGroupDTO> getAllCTSP(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        List<Object[]> results = chiTietSanPhamRepository.findGroupedProductsBySanPhamId(null); // Lấy tất cả sản phẩm
        List<ChiTietSanPhamGroupDTO> dtos = results.stream().map(this::convertToChiTietSanPhamGroupDTO).collect(Collectors.toList());

        // Lọc theo từ khóa nếu có
        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            dtos = dtos.stream()
                    .filter(dto -> dto.getTenSanPham().toLowerCase().contains(lowerKeyword) ||
                            dto.getMa().toLowerCase().contains(lowerKeyword))
                    .collect(Collectors.toList());
        }

        // Phân trang thủ công
        int start = Math.min((page * size), dtos.size());
        int end = Math.min(((page + 1) * size), dtos.size());
        List<ChiTietSanPhamGroupDTO> pagedDtos = dtos.subList(start, end);

        return new PageImpl<>(pagedDtos, pageable, dtos.size());
    }

    @CacheEvict(value = "sanPhamCache", allEntries = true)
    public void clearSanPhamCache() {
        // Gọi khi sản phẩm được thêm/sửa/xóa
    }

    public int getSoLuongBySanPhamId(Integer sanPhamId) {
        Long count = chiTietSanPhamRepository.countByIdSanPhamIdAndDeletedFalseAndNotSold(sanPhamId);
        return count.intValue();
    }

    public List<String> getIMEIsBySanPhamIdAndAttributes(Integer sanPhamId, String mauSac, String dungLuongRam, String dungLuongBoNhoTrong) {
        return chiTietSanPhamRepository.findIMEIsBySanPhamIdAndAttributes(sanPhamId, mauSac, dungLuongRam, dungLuongBoNhoTrong);
    }

    public ChiTietSanPham findChiTietSanPhamByIMEI(String imei) {
        return chiTietSanPhamRepository.findByIdImelImelAndDeletedFalse(imei)
                .orElse(null);
    }

    public ChiTietGioHangDTO addChiTietGioHang(Integer gioHangId, Integer chiTietSanPhamId, Integer hoaDonId) {
        GioHang gioHang = gioHangRepository.findById(gioHangId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giỏ hàng với ID: " + gioHangId));
        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(chiTietSanPhamId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết sản phẩm với ID: " + chiTietSanPhamId));
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));

        BigDecimal giaBan = chiTietSanPham.getGiaBan();
        BigDecimal tongTien = giaBan;

        chiTietSanPham.setDeleted(true);
        chiTietSanPhamRepository.save(chiTietSanPham);

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

        ChiTietSanPham chiTietSanPham = chiTietGioHang.getIdChiTietSanPham();
        chiTietSanPham.setDeleted(false);
        chiTietSanPhamRepository.save(chiTietSanPham);

        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findAll().stream()
                .filter(hdct -> hdct.getIdChiTietSanPham().getId().equals(chiTietSanPham.getId()) && !hdct.getDeleted())
                .collect(Collectors.toList());
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            hoaDonChiTiet.setDeleted(true);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        }
    }

    public HoaDon addHD(HoaDon hoaDon) {
        return hoaDonRepository.save(hoaDon);
    }

    @Transactional
    public void thanhToan(Integer hoaDonId, ThanhToanRequestDTO request) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));

        hoaDon.setTongTien(BigDecimal.valueOf(request.getTotalPrice()));
        hoaDon.setTongTienSauGiam(BigDecimal.valueOf(request.getTotalPrice() - request.getDiscount()));
        hoaDon.setTrangThai((short) 1);
        hoaDon.setNgayThanhToan(new Date());
        hoaDon.setUpdatedAt(new Date());

        if (Boolean.TRUE.equals(request.getIsDelivery()) && request.getReceiver() != null) {
            ThanhToanRequestDTO.ReceiverDTO receiver = request.getReceiver();
            hoaDon.setTenKhachHang(receiver.getName() != null ? receiver.getName() : "Khách lẻ");
            hoaDon.setSoDienThoaiKhachHang(receiver.getPhone() != null ? receiver.getPhone() : "N/A");
            hoaDon.setEmail(receiver.getEmail());
            StringBuilder diaChi = new StringBuilder();
            if (receiver.getCity() != null && !receiver.getCity().isEmpty()) {
                diaChi.append(receiver.getCity());
            }
            if (receiver.getDistrict() != null && !receiver.getDistrict().isEmpty()) {
                diaChi.append(", ").append(receiver.getDistrict());
            }
            if (receiver.getWard() != null && !receiver.getWard().isEmpty()) {
                diaChi.append(", ").append(receiver.getWard());
            }
            if (receiver.getAddress() != null && !receiver.getAddress().isEmpty()) {
                diaChi.append(", ").append(receiver.getAddress());
            }
            hoaDon.setDiaChiKhachHang(diaChi.length() > 0 ? diaChi.toString() : "N/A");
            hoaDon.setLoaiDon("online");
        } else {
            hoaDon.setTenKhachHang("Khách lẻ");
            hoaDon.setSoDienThoaiKhachHang("N/A");
            hoaDon.setEmail(null);
            hoaDon.setDiaChiKhachHang("N/A");
            hoaDon.setLoaiDon("Tại quầy");
            hoaDon.setTrangThai((short) 3);
        }

        hoaDonRepository.save(hoaDon);

        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setMa("LSHD" + System.currentTimeMillis());
        lichSuHoaDon.setHanhDong("Thanh toán hóa đơn");
        lichSuHoaDon.setThoiGian(new java.sql.Date(new java.util.Date().getTime()));
        lichSuHoaDon.setDeleted(false);
        lichSuHoaDonRepository.save(lichSuHoaDon);

        String kieuThanhToan;
        switch (request.getPaymentMethod() != null ? request.getPaymentMethod() : "") {
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
                kieuThanhToan = Boolean.TRUE.equals(request.getIsDelivery()) ? "Thanh toán khi nhận" : "Không xác định";
                break;
        }

        PhuongThucThanhToan phuongThucThanhToan = new PhuongThucThanhToan();
        phuongThucThanhToan.setMa("PTTT" + System.currentTimeMillis());
        phuongThucThanhToan.setKieuThanhToan(kieuThanhToan);
        phuongThucThanhToan.setDeleted(false);
        phuongThucThanhToan = phuongThucThanhToanRepository.save(phuongThucThanhToan);

        HinhThucThanhToan hinhThucThanhToan = new HinhThucThanhToan();
        hinhThucThanhToan.setHoaDon(hoaDon);
        hinhThucThanhToan.setIdPhuongThucThanhToan(phuongThucThanhToan);
        hinhThucThanhToan.setTienChuyenKhoan(BigDecimal.valueOf(request.getTienChuyenKhoan() != null ? request.getTienChuyenKhoan() : 0));
        hinhThucThanhToan.setTienMat(BigDecimal.valueOf(request.getTienMat() != null ? request.getTienMat() : 0));
        hinhThucThanhToan.setMa("HTTT" + System.currentTimeMillis());
        hinhThucThanhToan.setDeleted(false);
        hinhThucThanhToanRepository.save(hinhThucThanhToan);

        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findAll().stream()
                .filter(hdct -> hdct.getHoaDon().getId().equals(hoaDonId) && !hdct.getDeleted())
                .collect(Collectors.toList());
        for (HoaDonChiTiet hdct : hoaDonChiTiets) {
            ChiTietSanPham ctsp = hdct.getIdChiTietSanPham();
            if (ctsp.getIdImel() == null || ctsp.getIdImel().getMa() == null) {
                throw new IllegalStateException("IMEI của sản phẩm không hợp lệ (null) cho ChiTietSanPham ID: " + ctsp.getId());
            }

            ImelDaBan imelDaBan = new ImelDaBan();
            String maxMa = imelDaBanRepository.findMaxMa();
            String newMa;
            if (maxMa == null) {
                newMa = "IMDB00001";
            } else {
                int number = Integer.parseInt(maxMa.substring(4)) + 1;
                newMa = String.format("IMDB%05d", number);
            }
            imelDaBan.setMa(newMa);
            imelDaBan.setImel(ctsp.getIdImel().getImel());
            imelDaBan.setNgayBan(new java.sql.Date(new java.util.Date().getTime()));
            imelDaBan.setGhiChu("Bán qua hóa đơn " + hoaDon.getMa());
            imelDaBan.setDeleted(false);
            imelDaBan = imelDaBanRepository.save(imelDaBan);

            hdct.setIdImelDaBan(imelDaBan);
            hoaDonChiTietRepository.save(hdct);
        }
    }

    private HDban_hangDTO mapToDTO(HoaDon hoaDon) {
        HDban_hangDTO dto = new HDban_hangDTO();
        dto.setId(hoaDon.getId());
        dto.setMaHoaDon(hoaDon.getMa());
        dto.setTrangThai(hoaDon.getTrangThai());

        List<HDCTban_hangDTO> items = hoaDonChiTietRepository.findAll().stream()
                .filter(hdct -> hdct.getHoaDon().getId().equals(hoaDon.getId()) && !hdct.getDeleted())
                .map(hdct -> {
                    HDCTban_hangDTO itemDTO = new HDCTban_hangDTO();
                    itemDTO.setId(hdct.getId());
                    itemDTO.setTenSanPham(hdct.getIdChiTietSanPham().getIdSanPham().getTenSanPham());
                    itemDTO.setImei(hdct.getIdChiTietSanPham().getIdImel().getImel());
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
                        .filter(ctgh -> ctgh.getIdGioHang().getId().equals(gioHang.getId()) && !ctgh.getDeleted())
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
        dto.setImei(chiTietGioHang.getIdChiTietSanPham().getIdImel().getImel());
        dto.setGiaBan(chiTietGioHang.getIdChiTietSanPham().getGiaBan());
        dto.setTongTien(chiTietGioHang.getTongTien());
        return dto;
    }

    private ChiTietSanPhamGroupDTO convertToChiTietSanPhamGroupDTO(Object[] result) {
        ChiTietSanPhamGroupDTO dto = new ChiTietSanPhamGroupDTO();
        dto.setTenSanPham((String) result[0]);
        dto.setMa((String) result[1]);
        dto.setMauSac((String) result[2]);
        dto.setDungLuongRam((String) result[3]);
        dto.setDungLuongBoNhoTrong((String) result[4]);
        dto.setSoLuong(((Number) result[5]).intValue());
        dto.setGiaBan((BigDecimal) result[6]);
        dto.setIdSanPham(((Number) result[7]).intValue());
        return dto;
    }
}