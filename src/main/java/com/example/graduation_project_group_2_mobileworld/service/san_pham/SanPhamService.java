package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.SanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.SanPham;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ChiTietSanPhamRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SanPhamService {

    private final SanPhamRepository sanPhamRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    public SanPhamService(SanPhamRepository sanPhamRepository, ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
    }

    public Page<SanPhamDTO> getAllSanPham(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanPham> sanPhamPage = sanPhamRepository.findByDeletedFalse(pageable);
        return mapToDTOPage(sanPhamPage, pageable);
    }

    public Page<SanPhamDTO> searchSanPham(
            String keyword,
            Integer idNhaSanXuat,
            Integer idHeDieuHanh,
            String heDieuHanh,
            String phienBan,
            Integer idCongNgheManHinh,
            String congNgheManHinh,
            String chuanManHinh,
            Integer idPin,
            String loaiPin,
            String dungLuongPin,
            Integer idCpu,
            Integer idGpu,
            Integer idCumCamera,
            Integer idThietKe,
            Integer idSim,
            Integer idHoTroCongNgheSac,
            Integer idCongNgheMang,
            Boolean inStock,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<SanPham> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), false));

            if (keyword != null && !keyword.isEmpty()) {
                System.out.println("Filtering by keyword: " + keyword);
                predicates.add(cb.like(cb.lower(root.get("tenSanPham")), "%" + keyword.toLowerCase() + "%"));
            }
            if (idNhaSanXuat != null) {
                System.out.println("Filtering by idNhaSanXuat: " + idNhaSanXuat);
                predicates.add(cb.equal(root.get("idNhaSanXuat").get("id"), idNhaSanXuat));
            }
            if (idHeDieuHanh != null) {
                System.out.println("Filtering by idHeDieuHanh: " + idHeDieuHanh);
                predicates.add(cb.equal(root.get("idHeDieuHanh").get("id"), idHeDieuHanh));
            }
            if (heDieuHanh != null && !heDieuHanh.isEmpty()) {
                System.out.println("Filtering by heDieuHanh: " + heDieuHanh);
                predicates.add(cb.equal(root.get("idHeDieuHanh").get("heDieuHanh"), heDieuHanh));
            }
            if (phienBan != null && !phienBan.isEmpty()) {
                System.out.println("Filtering by phienBan: " + phienBan);
                predicates.add(cb.equal(root.get("idHeDieuHanh").get("phienBan"), phienBan));
            }
            if (idCongNgheManHinh != null) {
                System.out.println("Filtering by idCongNgheManHinh: " + idCongNgheManHinh);
                predicates.add(cb.equal(root.get("congNgheManHinh").get("id"), idCongNgheManHinh));
            }
            if (congNgheManHinh != null && !congNgheManHinh.isEmpty()) {
                System.out.println("Filtering by congNgheManHinh: " + congNgheManHinh);
                predicates.add(cb.equal(root.get("congNgheManHinh").get("congNgheManHinh"), congNgheManHinh));
            }
            if (chuanManHinh != null && !chuanManHinh.isEmpty()) {
                System.out.println("Filtering by chuanManHinh: " + chuanManHinh);
                predicates.add(cb.equal(root.get("congNgheManHinh").get("chuanManHinh"), chuanManHinh));
            }
            if (idPin != null) {
                System.out.println("Filtering by idPin: " + idPin);
                predicates.add(cb.equal(root.get("idPin").get("id"), idPin));
            }
            if (loaiPin != null && !loaiPin.isEmpty()) {
                System.out.println("Filtering by loaiPin: " + loaiPin);
                predicates.add(cb.equal(root.get("idPin").get("loaiPin"), loaiPin));
            }
            if (dungLuongPin != null && !dungLuongPin.isEmpty()) {
                System.out.println("Filtering by dungLuongPin: " + dungLuongPin);
                predicates.add(cb.equal(root.get("idPin").get("dungLuongPin"), dungLuongPin));
            }
            if (idCpu != null) {
                System.out.println("Filtering by idCpu: " + idCpu);
                predicates.add(cb.equal(root.get("idCpu").get("id"), idCpu));
            }
            if (idGpu != null) {
                System.out.println("Filtering by idGpu: " + idGpu);
                predicates.add(cb.equal(root.get("idGpu").get("id"), idGpu));
            }
            if (idCumCamera != null) {
                System.out.println("Filtering by idCumCamera: " + idCumCamera);
                predicates.add(cb.equal(root.get("idCumCamera").get("id"), idCumCamera));
            }
            if (idThietKe != null) {
                System.out.println("Filtering by idThietKe: " + idThietKe);
                predicates.add(cb.equal(root.get("idThietKe").get("id"), idThietKe));
            }
            if (idSim != null) {
                System.out.println("Filtering by idSim: " + idSim);
                predicates.add(cb.equal(root.get("idSim").get("id"), idSim));
            }
            if (idHoTroCongNgheSac != null) {
                System.out.println("Filtering by idHoTroCongNgheSac: " + idHoTroCongNgheSac);
                predicates.add(cb.equal(root.get("hoTroCongNgheSac").get("id"), idHoTroCongNgheSac));
            }
            if (idCongNgheMang != null) {
                System.out.println("Filtering by idCongNgheMang: " + idCongNgheMang);
                predicates.add(cb.equal(root.get("idCongNgheMang").get("id"), idCongNgheMang));
            }

            if (inStock != null) {
                System.out.println("Filtering by inStock: " + inStock);
                Subquery<Long> subquery = query.subquery(Long.class);
                var subRoot = subquery.from(ChiTietSanPham.class);
                subquery.select(cb.count(subRoot));
                subquery.where(
                        cb.equal(subRoot.get("idSanPham").get("id"), root.get("id")),
                        cb.equal(subRoot.get("deleted"), false)
                );

                if (inStock) {
                    predicates.add(cb.greaterThan(subquery, 0L));
                } else {
                    predicates.add(cb.equal(subquery, 0L));
                }
            }

            System.out.println("Predicates: " + predicates);
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<SanPham> sanPhamPage = sanPhamRepository.findAll(spec, pageable);
        System.out.println("SanPhamPage content: " + sanPhamPage.getContent());
        return mapToDTOPage(sanPhamPage, pageable);
    }

    private Page<SanPhamDTO> mapToDTOPage(Page<SanPham> sanPhamPage, Pageable pageable) {
        List<SanPhamDTO> dtos = sanPhamPage.getContent().stream().map(this::mapToDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, sanPhamPage.getTotalElements());
    }

    private SanPhamDTO mapToDTO(SanPham sanPham) {
        SanPhamDTO dto = new SanPhamDTO();
        dto.setId(sanPham.getId());
        dto.setMa(sanPham.getMa());
        dto.setTenSanPham(sanPham.getTenSanPham());
        dto.setNhaSanXuat(sanPham.getIdNhaSanXuat() != null ? sanPham.getIdNhaSanXuat().getNhaSanXuat() : "N/A");
        dto.setHeDieuHanh(sanPham.getIdHeDieuHanh() != null ? sanPham.getIdHeDieuHanh().getHeDieuHanh() : "N/A");
        dto.setPhienBan(sanPham.getIdHeDieuHanh() != null ? sanPham.getIdHeDieuHanh().getPhienBan() : "N/A");
        dto.setCongNgheManHinh(sanPham.getCongNgheManHinh() != null ? sanPham.getCongNgheManHinh().getCongNgheManHinh() : "N/A");
        dto.setChuanManHinh(sanPham.getCongNgheManHinh() != null ? sanPham.getCongNgheManHinh().getChuanManHinh() : "N/A");
        dto.setTenCpu(sanPham.getIdCpu() != null ? sanPham.getIdCpu().getTenCpu() : "N/A");
        dto.setCameraTruoc(sanPham.getIdCumCamera() != null ? sanPham.getIdCumCamera().getCameraTruoc() : "N/A");
        dto.setCameraSau(sanPham.getIdCumCamera() != null ? sanPham.getIdCumCamera().getCameraSau() : "N/A");
        dto.setLoaiPin(sanPham.getIdPin() != null ? sanPham.getIdPin().getLoaiPin() : "N/A");
        dto.setDungLuongPin(sanPham.getIdPin() != null ? sanPham.getIdPin().getDungLuongPin() : "N/A");
        dto.setSoLuongSimHoTro(sanPham.getIdSim() != null ? sanPham.getIdSim().getSoLuongSimHoTro() : 0);
        dto.setCacLoaiSimHoTro(sanPham.getIdSim() != null ? sanPham.getIdSim().getCacLoaiSimHoTro() : "N/A");
        dto.setDeleted(sanPham.getDeleted());
        dto.setCreatedAt(sanPham.getCreatedAt());
        dto.setCreatedBy(sanPham.getCreatedBy());
        dto.setUpdatedAt(sanPham.getUpdatedAt());
        dto.setUpdatedBy(sanPham.getUpdatedBy());

        Long imeiCount = chiTietSanPhamRepository.countByIdSanPhamIdAndDeletedFalse(sanPham.getId());
        dto.setImeiCount(imeiCount);

        List<ChiTietSanPham> variants = chiTietSanPhamRepository.findByIdSanPhamIdAndDeletedFalse(sanPham.getId(), false);
        BigDecimal minPrice = variants.stream()
                .map(ChiTietSanPham::getGiaBan)
                .filter(price -> price != null)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        BigDecimal maxPrice = variants.stream()
                .map(ChiTietSanPham::getGiaBan)
                .filter(price -> price != null)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        dto.setMinPrice(minPrice);
        dto.setMaxPrice(maxPrice);

        return dto;
    }

    public Optional<SanPham> getSanPhamById(Integer id) {
        return Optional.ofNullable(sanPhamRepository.findByIdAndDeletedFalse(id));
    }

    @Transactional
    public SanPham updateSanPham(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    public Long countChiTietSanPhamBySanPhamId(Integer sanPhamId) {
        return chiTietSanPhamRepository.countByIdSanPhamIdAndDeletedFalse(sanPhamId);
    }
}