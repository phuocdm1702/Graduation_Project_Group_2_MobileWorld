package com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia;

import com.example.graduation_project_group_2_mobileworld.dto.khach_hang.KhachHangDTO;
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



    private final PhieuGiamGiaCaNhanRepository phieuGiamGiaCaNhanRepository;


    public PhieuGiamGiaService(PhieuGiamGiaRepository phieuGiamGiaRepository,
                               PhieuGiamGiaCaNhanRepository phieuGiamGiaCaNhanRepository) {
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.phieuGiamGiaCaNhanRepository = phieuGiamGiaCaNhanRepository;
    }

    public Page<PhieuGiamGia> getPGG(Pageable pageable) {
        Date now = new Date();
        return phieuGiamGiaRepository.findByNgayKetThucGreaterThanEqual(now, pageable);
    }

    @Scheduled(fixedRate = 200000)
    public void updateHanPGG() {
        List<PhieuGiamGia> listPgg = phieuGiamGiaRepository.findAll();
        Date now = new Date();

        for (PhieuGiamGia pgg : listPgg) {
            if (pgg.getNgayBatDau() != null && pgg.getNgayKetThuc() != null) {
                boolean isActive = (pgg.getNgayBatDau().before(now) || pgg.getNgayBatDau().equals(now)) &&
                        (pgg.getNgayKetThuc().after(now) || pgg.getNgayKetThuc().equals(now));
                boolean currentStatus = pgg.getTrangThai() != null && pgg.getTrangThai();

                if (isActive && !currentStatus) {
                    pgg.setTrangThai(true); // Cập nhật thành Hoạt động
                    phieuGiamGiaRepository.save(pgg);
                    System.out.println("Updated PGG " + pgg.getMa() + " to Hoạt động");
                } else if (!isActive && currentStatus) {
                    pgg.setTrangThai(false); // Cập nhật thành Không hoạt động
                    phieuGiamGiaRepository.save(pgg);
                    System.out.println("Updated PGG " + pgg.getMa() + " to Không hoạt động");
                }
            }
        }
    }

    public Page<PhieuGiamGia> searchData(String keyword, Pageable pageable) {
        Date now = new Date();
        if (keyword == null || keyword.trim().isEmpty()) {
            return (Page<PhieuGiamGia>) phieuGiamGiaRepository.findAll(); // Trả về tất cả nếu không có điều kiện lọc
        }
        return phieuGiamGiaRepository.search(keyword, now, pageable);
    }

    public Page<PhieuGiamGia> filterPhieuGiamGia(
            String loaiPhieuGiamGia,
            String trangThai,
            Date ngayBatDau,
            Date ngayKetThuc,
            Double minOrder,
            Double valueFilter,
            Pageable pageable) {

        String loaiPhieu = loaiPhieuGiamGia != null && loaiPhieuGiamGia.trim().isEmpty() ? null : loaiPhieuGiamGia;

        Boolean trangThaiBoolean = null;
        if (trangThai != null && !trangThai.trim().isEmpty()) {
            if ("Hoạt động".equals(trangThai)) {
                trangThaiBoolean = true;
            } else if ("Không hoạt động".equals(trangThai)) {
                trangThaiBoolean = false;
            }
        }

        Date now = new Date();
        if (ngayBatDau != null && ngayKetThuc != null && ngayBatDau.after(ngayKetThuc)) {
            throw new IllegalArgumentException("Ngày bắt đầu không thể lớn hơn ngày kết thúc");
        }
        if (minOrder != null && minOrder < 0) {
            throw new IllegalArgumentException("Hóa đơn tối thiểu không thể nhỏ hơn 0");
        }
        if (valueFilter != null && valueFilter < 0) {
            throw new IllegalArgumentException("Giá trị phiếu không thể nhỏ hơn 0");
        }

        System.out.println("Filter params - loaiPhieu: " + loaiPhieu + ", trangThai: " + trangThaiBoolean +
                ", ngayBatDau: " + ngayBatDau + ", ngayKetThuc: " + ngayKetThuc +
                ", minOrder: " + minOrder + ", valueFilter: " + valueFilter);

        Page<PhieuGiamGia> result = phieuGiamGiaRepository.filterPhieuGiamGia(
                loaiPhieu,
                trangThaiBoolean,
                ngayBatDau,
                ngayKetThuc,
                minOrder,
                valueFilter,
                now,
                pageable
        );

        System.out.println("Filter result size: " + result.getContent().size());
        result.getContent().forEach(voucher ->
                System.out.println("Voucher: " + voucher.getMa() + ", TrangThai: " + (voucher.getTrangThai() ? "Hoạt động" : "Không hoạt động") +
                        ", NgayBatDau: " + voucher.getNgayBatDau() + ", NgayKetThuc: " + voucher.getNgayKetThuc())
        );

        return result;
    }

    public Optional<PhieuGiamGia> getById(Integer id) {
        return phieuGiamGiaRepository.findById(id);
    }

    public PhieuGiamGia addPGG(PhieuGiamGia phieuGiamGia) {
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }

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
        dto.setTrangThai(pgg.getTrangThai()); // boolean từ entity sang DTO
        return dto;
    }

    public PhieuGiamGiaDTO updateTrangthai(Integer id, Boolean trangThai) {
        Optional<PhieuGiamGia> optionalPgg = phieuGiamGiaRepository.findById(id);
        if (!optionalPgg.isPresent()) {
            throw new RuntimeException("Phiếu giảm giá không tồn tại với ID: " + id);
        }

        PhieuGiamGia pgg = optionalPgg.get();
        pgg.setTrangThai(trangThai); // true = Hoạt động, false = Không hoạt động
        PhieuGiamGia updatePGG = phieuGiamGiaRepository.save(pgg);
        System.out.println("Updated PGG " + updatePGG.getMa() + " to TrangThai: " + (trangThai ? "Hoạt động" : "Không hoạt động"));
        return convertToDTO(updatePGG);
    }

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
        pggDTO.setTrangThai(pgg.getTrangThai() ? true : false);
        pggDTO.setRiengTu(pgg.getRiengTu() ? 1 : 0);

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

    public PhieuGiamGia updatePGG(PhieuGiamGia editPGG) {
        return phieuGiamGiaRepository.save(editPGG);
    }

    public List<PhieuGiamGia> getall() {
        return phieuGiamGiaRepository.findAll();
    }
}
