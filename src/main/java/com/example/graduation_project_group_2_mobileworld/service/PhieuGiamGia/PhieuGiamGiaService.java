package com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.dto.phieuGiamGiaDTO.KhPggDTO;
import com.example.graduation_project_group_2_mobileworld.dto.phieuGiamGiaDTO.PhieuGiamGiaDTO;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaCaNhanRepository;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaRepository;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaService {

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private PhieuGiamGiaCaNhanRepository phieuGiamGiaCaNhanRepository;

    // Lấy danh sách phiếu giảm giá với phân trang
    public Page<PhieuGiamGia> getPGG(Pageable pageable) {
        Date now = new Date();
        return phieuGiamGiaRepository.findByNgayKetThucGreaterThanEqual(now, pageable);
    }

    // Tự động cập nhật trạng thái phiếu giảm giá
    @Scheduled(fixedRate = 200000)
    public void updateHanPGG() {
        List<PhieuGiamGia> listPgg = phieuGiamGiaRepository.findAll();
        Date now = new Date();

        for (PhieuGiamGia pgg : listPgg) {
            if (pgg.getNgayBatDau() != null && pgg.getNgayKetThuc() != null) {
                if ((pgg.getNgayBatDau().before(now) || pgg.getNgayBatDau().equals(now)) &&
                        (pgg.getNgayKetThuc().after(now) || pgg.getNgayKetThuc().equals(now))) {
                    if (pgg.getTrangThai() == null || pgg.getTrangThai()) {
                        pgg.setTrangThai(false); // Hoạt động
                        phieuGiamGiaRepository.save(pgg);
                    }
                } else {
                    if (pgg.getTrangThai() == null || !pgg.getTrangThai()) {
                        pgg.setTrangThai(true); // Không hoạt động
                        phieuGiamGiaRepository.save(pgg);
                    }
                }
            }
        }
    }

    // Tìm kiếm phiếu giảm giá
    public Page<PhieuGiamGia> searchData(String keyword, Pageable pageable) {
        Date now = new Date();
        if (keyword == null || keyword.trim().isEmpty()) {
            return phieuGiamGiaRepository.findAll(pageable);
        }
        return phieuGiamGiaRepository.search(keyword, now, pageable);
    }

    // Lọc phiếu giảm giá
    public Page<PhieuGiamGia> filterPhieuGiamGia(
            String loaiPhieuGiamGia,
            String trangThai,
            Date ngayBatDau,
            Date ngayKetThuc,
            Double minOrder,
            Double valueFilter,
            Pageable pageable) {

        String loaiPhieu = loaiPhieuGiamGia;

        if (loaiPhieu != null && ("Tất cả loại phiếu".equals(loaiPhieu) || loaiPhieu.isEmpty())) {
            loaiPhieu = null;
        }

        Boolean trangThaiBoolean = null;
        if (trangThai != null && !trangThai.isEmpty()) {
            if ("Hoạt động".equals(trangThai)) {
                trangThaiBoolean = false;
            } else if ("Không hoạt động".equals(trangThai)) {
                trangThaiBoolean = true;
            }
        }

        Date now = new Date();

        if (loaiPhieu == null && trangThaiBoolean == null && ngayBatDau == null &&
                ngayKetThuc == null && minOrder == null && valueFilter == null) {
            return phieuGiamGiaRepository.findAll(pageable);
        }

        if (ngayBatDau != null && ngayKetThuc != null && ngayBatDau.after(ngayKetThuc)) {
            throw new IllegalArgumentException("Ngày bắt đầu không thể lớn hơn ngày kết thúc");
        }

        if (minOrder != null && minOrder < 0) {
            throw new IllegalArgumentException("Hóa đơn tối thiểu không thể nhỏ hơn 0");
        }

        if (valueFilter != null && valueFilter < 0) {
            throw new IllegalArgumentException("Giá trị phiếu không thể nhỏ hơn 0");
        }

        return phieuGiamGiaRepository.filterPhieuGiamGia(
                loaiPhieu,
                trangThaiBoolean,
                ngayBatDau,
                ngayKetThuc,
                minOrder,
                valueFilter,
                now,
                pageable
        );
    }

    // Lấy phiếu giảm giá theo ID
    public Optional<PhieuGiamGia> getById(Integer id) {
        return phieuGiamGiaRepository.findById(id);
    }

    // Thêm phiếu giảm giá
    public PhieuGiamGia addPGG(PhieuGiamGia phieuGiamGia) {
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }

    // Cập nhật trạng thái phiếu giảm giá
    public PhieuGiamGiaDTO updateTrangthai(Integer id, Boolean trangThai) {
        Optional<PhieuGiamGia> optionalPgg = phieuGiamGiaRepository.findById(id);
        if (!optionalPgg.isPresent()) {
            throw new RuntimeException("Phiếu giảm giá không tồn tại với ID: " + id);
        }

        PhieuGiamGia pgg = optionalPgg.get();
        pgg.setTrangThai(trangThai);
        phieuGiamGiaRepository.save(pgg);
        return convertToDTO(pgg);
    }

    // Lấy chi tiết phiếu giảm giá
    public PhieuGiamGiaDTO getDetailPGG(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID không hợp lệ!");
        }

        Optional<PhieuGiamGia> detailPGG = phieuGiamGiaRepository.findById(id);
        if (!detailPGG.isPresent()) {
            throw new RuntimeException("Phiếu giảm giá không tồn tại!");
        }

        PhieuGiamGia pgg = detailPGG.get();

        List<PhieuGiamGiaCaNhan> pggCNList = phieuGiamGiaCaNhanRepository.findByIdPhieuGiamGia(pgg);
        List<KhachHang> allCustomers = khachHangRepository.findAll();

        PhieuGiamGiaDTO pggDTO = new PhieuGiamGiaDTO();
        pggDTO.setId(pgg.getId());
        pggDTO.setMa(pgg.getMa());
        pggDTO.setTenPhieuGiamGia(pgg.getTenPhieuGiamGia());
        pggDTO.setLoaiPhieuGiamGia(pgg.getLoaiPhieuGiamGia());
        pggDTO.setPhanTramGiamGia(pgg.getPhanTramGiamGia());
        pggDTO.setSoTienGiamToiDa(pgg.getSoTienGiamToiDa());
        pggDTO.setHoaDonToiThieu(pgg.getHoaDonToiThieu());
        pggDTO.setSoLuongDung(pgg.getSoLuongDung());
        pggDTO.setNgayBatDau(pgg.getNgayBatDau());
        pggDTO.setNgayKetThuc(pgg.getNgayKetThuc());
        pggDTO.setMoTa(pgg.getMoTa());
        pggDTO.setTrangThai(pgg.getTrangThai() != null ? pgg.getTrangThai() : false);
        pggDTO.setRiengTu(pgg.getRiengTu() != null ? pgg.getRiengTu() : false);

        List<KhPggDTO> selectedCustomers = pggCNList.stream()
                .map(pggCN -> {
                    KhachHang kh = pggCN.getIdKhachHang();
                    return new KhPggDTO(kh.getId(), kh.getMa(), kh.getTen(), kh.getNgaySinh());
                })
                .collect(Collectors.toList());
        pggDTO.setSelectedCustomers(selectedCustomers);

        List<Integer> customerIds = pggCNList.stream()
                .map(pggCN -> pggCN.getIdKhachHang().getId())
                .collect(Collectors.toList());
        pggDTO.setCustomerIds(customerIds);

        List<KhPggDTO> allCustomersDTO = allCustomers.stream()
                .map(kh -> new KhPggDTO(kh.getId(), kh.getMa(), kh.getTen(), kh.getNgaySinh()))
                .collect(Collectors.toList());
        pggDTO.setAllCustomers(allCustomersDTO);

        return pggDTO;
    }

    // Cập nhật phiếu giảm giá
    public void updatePGG(PhieuGiamGia editPGG) {
        phieuGiamGiaRepository.save(editPGG);
    }

    // Chuyển đổi từ PhieuGiamGia sang DTO
    private PhieuGiamGiaDTO convertToDTO(PhieuGiamGia pgg) {
        PhieuGiamGiaDTO dto = new PhieuGiamGiaDTO();
        dto.setId(pgg.getId());
        dto.setMa(pgg.getMa());
        dto.setTenPhieuGiamGia(pgg.getTenPhieuGiamGia());
        dto.setLoaiPhieuGiamGia(pgg.getLoaiPhieuGiamGia());
        dto.setPhanTramGiamGia(pgg.getPhanTramGiamGia());
        dto.setSoTienGiamToiDa(pgg.getSoTienGiamToiDa());
        dto.setHoaDonToiThieu(pgg.getHoaDonToiThieu());
        dto.setSoLuongDung(pgg.getSoLuongDung());
        dto.setNgayBatDau(pgg.getNgayBatDau());
        dto.setNgayKetThuc(pgg.getNgayKetThuc());
        dto.setMoTa(pgg.getMoTa());
        dto.setTrangThai(pgg.getTrangThai() != null ? pgg.getTrangThai() : false);
        dto.setRiengTu(pgg.getRiengTu() != null ? pgg.getRiengTu() : false);

        List<PhieuGiamGiaCaNhan> pggCNList = phieuGiamGiaCaNhanRepository.findByIdPhieuGiamGia(pgg);
        List<KhPggDTO> selectedCustomers = pggCNList.stream()
                .map(pggCN -> {
                    KhachHang kh = pggCN.getIdKhachHang();
                    return new KhPggDTO(kh.getId(), kh.getMa(), kh.getTen(), kh.getNgaySinh());
                })
                .collect(Collectors.toList());
        dto.setSelectedCustomers(selectedCustomers);

        List<Integer> customerIds = pggCNList.stream()
                .map(pggCN -> pggCN.getIdKhachHang().getId())
                .collect(Collectors.toList());
        dto.setCustomerIds(customerIds);

        return dto;
    }
}