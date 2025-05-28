package com.example.graduation_project_group_2_mobileworld.service.ban_hang_service;

import com.example.graduation_project_group_2_mobileworld.controller.ban_hang.EmailSendBH;
import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.ChiTietSanPhamGroupDTO;
import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDCTban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.HDban_hangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.ban_hang.ThanhToanRequestDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.ChiTietGioHangDTO;
import com.example.graduation_project_group_2_mobileworld.dto.gio_hang.GioHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ImelDaBan;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaCaNhanRepository;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaRepository;
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
import com.example.graduation_project_group_2_mobileworld.repository.tai_khoan.TaiKhoanRepository;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class BanHangService {
    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private PhieuGiamGiaCaNhanRepository phieuGiamGiaCaNhanRepository;


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

    @Autowired
    private EmailSendBH emailSendBH;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

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

    @Transactional
    public ChiTietGioHangDTO addChiTietGioHang(Integer gioHangId, Integer chiTietSanPhamId, Integer hoaDonId) {
        System.out.println("Adding ChiTietGioHang - GioHangId: " + gioHangId + ", ChiTietSanPhamId: " + chiTietSanPhamId + ", HoaDonId: " + hoaDonId);
        GioHang gioHang = gioHangRepository.findById(gioHangId)
                .orElseThrow(() -> {
                    System.out.println("GioHang not found for ID: " + gioHangId);
                    return new IllegalArgumentException("Không tìm thấy giỏ hàng với ID: " + gioHangId);
                });
        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(chiTietSanPhamId)
                .orElseThrow(() -> {
                    System.out.println("ChiTietSanPham not found for ID: " + chiTietSanPhamId);
                    return new IllegalArgumentException("Không tìm thấy chi tiết sản phẩm với ID: " + chiTietSanPhamId);
                });
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> {
                    System.out.println("HoaDon not found for ID: " + hoaDonId);
                    return new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId);
                });

        if (chiTietSanPham.getDeleted()) {
            System.out.println("ChiTietSanPham ID " + chiTietSanPhamId + " is already marked as deleted.");
            throw new IllegalStateException("Sản phẩm này đã được bán hoặc không khả dụng.");
        }

        BigDecimal giaBan = chiTietSanPham.getGiaBan();
        BigDecimal tongTien = giaBan;

        chiTietSanPham.setDeleted(true); // Đánh dấu sản phẩm đã được thêm vào giỏ
        chiTietSanPham = chiTietSanPhamRepository.save(chiTietSanPham);
        System.out.println("Updated ChiTietSanPham deleted status to true for ID: " + chiTietSanPhamId);

        ChiTietGioHang chiTietGioHang = new ChiTietGioHang();
        chiTietGioHang.setIdGioHang(gioHang);
        chiTietGioHang.setIdChiTietSanPham(chiTietSanPham);
        chiTietGioHang.setMa("CTGH" + System.currentTimeMillis());
        chiTietGioHang.setTrangThai((short) 0);
        chiTietGioHang.setTongTien(tongTien);
        chiTietGioHang.setDeleted(false);
        chiTietGioHang = chiTietGioHangRepository.save(chiTietGioHang);
        System.out.println("Saved ChiTietGioHang with ID: " + chiTietGioHang.getId());

        HoaDonChiTiet hoaDonChiTiet = HoaDonChiTiet.builder()
                .hoaDon(hoaDon)
                .idChiTietSanPham(chiTietSanPham)
                .ma("HDCT" + System.currentTimeMillis())
                .gia(giaBan)
                .trangThai((short) 0)
                .deleted(false)
                .build();
        hoaDonChiTiet = hoaDonChiTietRepository.save(hoaDonChiTiet);
        System.out.println("Saved HoaDonChiTiet with ID: " + hoaDonChiTiet.getId());

        return convertToChiTietGioHangDTO(chiTietGioHang);
    }

    public List<ChiTietGioHangDTO> getChiTietGioHangByGioHangId(Integer gioHangId) {
        System.out.println("Fetching ChiTietGioHang for GioHang ID: " + gioHangId);
        List<ChiTietGioHang> chiTietGioHangs = chiTietGioHangRepository.findAll().stream()
                .filter(ctgh -> ctgh.getIdGioHang().getId().equals(gioHangId) && !ctgh.getDeleted())
                .collect(Collectors.toList());
        System.out.println("Found " + chiTietGioHangs.size() + " non-deleted ChiTietGioHang items for GioHang ID: " + gioHangId);
        if (chiTietGioHangs.isEmpty()) {
            System.out.println("No matching ChiTietGioHang found. Checking GioHang existence...");
            gioHangRepository.findById(gioHangId).ifPresentOrElse(
                    gioHang -> System.out.println("GioHang exists but has no items."),
                    () -> System.out.println("GioHang ID " + gioHangId + " does not exist.")
            );
        }
        return chiTietGioHangs.stream()
                .map(this::convertToChiTietGioHangDTO)
                .collect(Collectors.toList());
    }

    public void deleteChiTietGioHang(Integer chiTietGioHangId) {
        System.out.println("Attempting to delete ChiTietGioHang with ID: " + chiTietGioHangId);
        ChiTietGioHang chiTietGioHang = chiTietGioHangRepository.findById(chiTietGioHangId)
                .orElseThrow(() -> {
                    System.out.println("ChiTietGioHang not found for ID: " + chiTietGioHangId);
                    return new IllegalArgumentException("Không tìm thấy chi tiết giỏ hàng với ID: " + chiTietGioHangId);
                });
        if (chiTietGioHang.getDeleted()) {
            System.out.println("ChiTietGioHang already deleted for ID: " + chiTietGioHangId);
            throw new IllegalArgumentException("Chi tiết giỏ hàng với ID: " + chiTietGioHangId + " đã được xóa trước đó.");
        }
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
        System.out.println("Successfully deleted ChiTietGioHang with ID: " + chiTietGioHangId);
    }

    public HoaDon addHD(HoaDon hoaDon) {
        return hoaDonRepository.save(hoaDon);
    }

    @Transactional
    public void thanhToan(Integer hoaDonId, ThanhToanRequestDTO request) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findByma(request.getMaGiamGia())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá với mã: " + request.getMaGiamGia()));
        hoaDon.setIdPhieuGiamGia(phieuGiamGia);
        hoaDon.setTongTien(BigDecimal.valueOf(request.getTotalPrice()));
        hoaDon.setTongTienSauGiam(BigDecimal.valueOf(request.getTotalPrice() - request.getDiscount()));
        hoaDon.setTrangThai((short) 1);
        hoaDon.setNgayThanhToan(new Date());
        hoaDon.setUpdatedAt(new Date());

        System.out.println("IsDelivery: " + request.getIsDelivery());
        System.out.println("Receiver: " + (request.getReceiver() != null ? request.getReceiver().toString() : "null"));

        // Lấy KhachHang mặc định với id = 1
        final Integer DEFAULT_KHACH_HANG_ID = 1;
        KhachHang defaultKhachHang = khachHangRepository.findById(DEFAULT_KHACH_HANG_ID)
                .orElseThrow(() -> new IllegalStateException("Khách hàng mặc định với ID = 1 không tồn tại"));

        if (Boolean.TRUE.equals(request.getIsDelivery()) && request.getReceiver() != null) {
            // Trường hợp 1: Bán online
            ThanhToanRequestDTO.ReceiverDTO receiver = request.getReceiver();
            String tenKhachHang = receiver.getName() != null && !receiver.getName().isEmpty() ? receiver.getName() : "Khách lẻ";
            String soDienThoai = receiver.getPhone() != null && !receiver.getPhone().isEmpty() ? receiver.getPhone() : "N/A";
            String email = receiver.getEmail() != null && !receiver.getEmail().isEmpty() ? receiver.getEmail() : null;
            StringBuilder diaChi = new StringBuilder();
            if (receiver.getCity() != null && !receiver.getCity().isEmpty()) diaChi.append(receiver.getCity());
            if (receiver.getDistrict() != null && !receiver.getDistrict().isEmpty()) diaChi.append(", ").append(receiver.getDistrict());
            if (receiver.getWard() != null && !receiver.getWard().isEmpty()) diaChi.append(", ").append(receiver.getWard());
            if (receiver.getAddress() != null && !receiver.getAddress().isEmpty()) diaChi.append(", ").append(receiver.getAddress());
            String diaChiKhachHang = diaChi.length() > 0 ? diaChi.toString() : "N/A";

            List<TaiKhoan> taiKhoanList = taiKhoanRepository.findBySoDienThoai(soDienThoai);
            TaiKhoan taiKhoan = taiKhoanList.isEmpty() ? null : taiKhoanList.get(0);

            KhachHang khachHang = null;
            if (taiKhoan != null) {
                List<KhachHang> khachHangList = khachHangRepository.findByIdTaiKhoan(taiKhoan);
                khachHang = khachHangList.isEmpty() ? null : khachHangList.get(0);
            }

            if (taiKhoan == null || khachHang == null) {
                hoaDon.setIdKhachHang(khachHang);
                hoaDon.setTenKhachHang(tenKhachHang);
                hoaDon.setSoDienThoaiKhachHang(soDienThoai);
                hoaDon.setEmail(email);
                hoaDon.setDiaChiKhachHang(diaChiKhachHang);
                hoaDon.setLoaiDon("online");
            } else {
                hoaDon.setIdKhachHang(khachHang);
                hoaDon.setTenKhachHang(khachHang.getTen());
                hoaDon.setSoDienThoaiKhachHang(taiKhoan.getSoDienThoai());
                hoaDon.setEmail(taiKhoan.getEmail());
                hoaDon.setDiaChiKhachHang(khachHang.getIdDiaChiKH() != null ? khachHang.getIdDiaChiKH().getDiaChiCuThe() : diaChiKhachHang);
                hoaDon.setLoaiDon("online");
            }
        } else {
            // Trường hợp 2 & 3: Bán tại quầy
            ThanhToanRequestDTO.ReceiverDTO receiver = request.getReceiver();
            String soDienThoai = receiver != null && receiver.getPhone() != null && !receiver.getPhone().isEmpty() ? receiver.getPhone() : null;

            if (soDienThoai == null) {
                // Trường hợp 2: Khách mới (không có số điện thoại)
                hoaDon.setIdKhachHang(defaultKhachHang); // Sử dụng KhachHang mặc định
                hoaDon.setTenKhachHang("Khách lẻ");
                hoaDon.setSoDienThoaiKhachHang("N/A");
                hoaDon.setEmail(null);
                hoaDon.setDiaChiKhachHang("N/A");
                hoaDon.setLoaiDon("Tại quầy");
                hoaDon.setTrangThai((short) 3);
            } else {
                // Trường hợp 3: Khách cũ (có số điện thoại)
                List<TaiKhoan> taiKhoanList = taiKhoanRepository.findBySoDienThoai(soDienThoai);
                TaiKhoan taiKhoan = taiKhoanList.isEmpty() ? null : taiKhoanList.get(0);

                KhachHang khachHang = null;
                if (taiKhoan != null) {
                    List<KhachHang> khachHangList = khachHangRepository.findByIdTaiKhoan(taiKhoan);
                    khachHang = khachHangList.isEmpty() ? null : khachHangList.get(0);
                }

                if (taiKhoan == null || khachHang == null) {
                    // Nếu không tìm thấy khách hàng, sử dụng KhachHang mặc định
                    hoaDon.setIdKhachHang(defaultKhachHang);
                    hoaDon.setTenKhachHang("Khách lẻ");
                    hoaDon.setSoDienThoaiKhachHang("N/A");
                    hoaDon.setEmail(null);
                    hoaDon.setDiaChiKhachHang("N/A");
                } else {
                    // Nếu tìm thấy, lấy thông tin khách hàng
                    hoaDon.setIdKhachHang(khachHang);
                    hoaDon.setTenKhachHang(khachHang.getTen());
                    hoaDon.setSoDienThoaiKhachHang(taiKhoan.getSoDienThoai());
                    hoaDon.setEmail(taiKhoan.getEmail());
                    hoaDon.setDiaChiKhachHang(khachHang.getIdDiaChiKH() != null ? khachHang.getIdDiaChiKH().getDiaChiCuThe() : "N/A");
                }
                hoaDon.setLoaiDon("Tại quầy");
                hoaDon.setTrangThai((short) 3);
            }
        }

        hoaDonRepository.save(hoaDon);

        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setMa("LSHD" + System.currentTimeMillis());
        lichSuHoaDon.setHanhDong("Thanh toán hóa đơn");
        lichSuHoaDon.setThoiGian(new Timestamp(System.currentTimeMillis()));
        lichSuHoaDon.setDeleted(false);
        lichSuHoaDonRepository.save(lichSuHoaDon);

        PhuongThucThanhToan phuongThucThanhToan;
        switch (request.getPaymentMethod() != null ? request.getPaymentMethod() : "") {
            case "cash":
                phuongThucThanhToan = phuongThucThanhToanRepository.findById(1)
                        .orElseThrow(() -> new IllegalStateException("Phương thức Tiền mặt không tồn tại"));
                break;
            case "transfer":
                phuongThucThanhToan = phuongThucThanhToanRepository.findById(2)
                        .orElseThrow(() -> new IllegalStateException("Phương thức Chuyển khoản không tồn tại"));
                break;
            case "both":
                phuongThucThanhToan = phuongThucThanhToanRepository.findById(3)
                        .orElseThrow(() -> new IllegalStateException("Phương thức Cả hai không tồn tại"));
                break;
            default:
                phuongThucThanhToan = Boolean.TRUE.equals(request.getIsDelivery()) ?
                        phuongThucThanhToanRepository.findById(2)
                                .orElseThrow(() -> new IllegalStateException("Phương thức Chuyển khoản không tồn tại"))
                        : phuongThucThanhToanRepository.findById(3)
                        .orElseThrow(() -> new IllegalStateException("Phương thức Cả hai không tồn tại"));
                break;
        }

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

        if (hoaDon.getEmail() != null && !hoaDon.getEmail().isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a Z, EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
                String formattedDate = dateFormat.format(hoaDon.getNgayThanhToan());
                String formattedTotalAmount = String.format("%,.0f", hoaDon.getTongTienSauGiam());

                StringBuilder productDetails = new StringBuilder();
                for (HoaDonChiTiet hdct : hoaDonChiTiets) {
                    ChiTietSanPham ctsp = hdct.getIdChiTietSanPham();
                    String tenSanPham = ctsp.getIdSanPham().getTenSanPham();
                    String mauSac = ctsp.getIdMauSac().getMauSac() != null ? ctsp.getIdMauSac().getMauSac() : "Không xác định";
                    productDetails.append(String.format("- %s (Màu: %s)<br>", tenSanPham, mauSac));
                }

                emailSendBH.sendPaymentConfirmationEmail(
                        hoaDon.getEmail(),
                        hoaDon.getMa(),
                        formattedTotalAmount,
                        phuongThucThanhToan.getKieuThanhToan(),
                        formattedDate,
                        productDetails.toString()
                );
            } catch (Exception e) {
                System.err.println("Lỗi khi gửi email xác nhận thanh toán: " + e.getMessage());
            }
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
                    itemDTO.setMauSac(hdct.getIdChiTietSanPham().getIdMauSac() != null
                            ? hdct.getIdChiTietSanPham().getIdMauSac().getMauSac()
                            : "N/A");
                    itemDTO.setRam(hdct.getIdChiTietSanPham().getIdRam() != null
                            ? hdct.getIdChiTietSanPham().getIdRam().getDungLuongRam()
                            : "N/A");
                    itemDTO.setBoNhoTrong(hdct.getIdChiTietSanPham().getIdBoNhoTrong() != null
                            ? hdct.getIdChiTietSanPham().getIdBoNhoTrong().getDungLuongBoNhoTrong()
                            : "N/A");
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
        dto.setMauSac(chiTietGioHang.getIdChiTietSanPham().getIdMauSac().getMauSac());
        dto.setBoNhoTrong(chiTietGioHang.getIdChiTietSanPham().getIdBoNhoTrong().getDungLuongBoNhoTrong());
        dto.setRam(chiTietGioHang.getIdChiTietSanPham().getIdRam().getDungLuongRam());
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
    public List<PhieuGiamGiaCaNhan> findByKhachHangId(Integer idKhachHang) {
        return phieuGiamGiaCaNhanRepository.findByIdKhachHangId(idKhachHang);
    }

}