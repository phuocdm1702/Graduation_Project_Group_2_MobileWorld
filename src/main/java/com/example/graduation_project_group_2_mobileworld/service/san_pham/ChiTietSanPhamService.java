package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ChiTietSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.AnhSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.ChiTietSanPham;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.Imel;
import com.example.graduation_project_group_2_mobileworld.entity.SanPham.SanPham;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChiTietSanPhamService {

    private final SanPhamRepository sanPhamRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final AnhSanPhamRepository anhSanPhamRepository;
    private final RamRepository ramRepository;
    private final BoNhoTrongRepository boNhoTrongRepository;
    private final MauSacRepository mauSacRepository;
    private final ImelRepository imelRepository;
    private final HeDieuHanhRepository heDieuHanhRepository;
    private final CongNgheManHinhRepository congNgheManHinhRepository; // Thay ManHinhRepository
    private final NhaSanXuatRepository nhaSanXuatRepository;
    private final CumCameraRepository cumCameraRepository;
    private final SimRepository simRepository;
    private final ThietKeRepository thietKeRepository;
    private final PinRepository pinRepository;
    private final CpuRepository cpuRepository;
    private final GpuRepository gpuRepository;
    private final CongNgheMangRepository congNgheMangRepository;
    private final HoTroCongNgheSacRepository hoTroCongNgheSacRepository;
    private final ChiSoKhangBuiVaNuocRepository chiSoKhangBuiVaNuocRepository;
    private final HoTroBoNhoNgoaiRepository hoTroBoNhoNgoaiRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ChiTietSanPhamService(HeDieuHanhRepository heDieuHanhRepository,
                                 SanPhamRepository sanPhamRepository,
                                 PinRepository pinRepository,
                                 ChiTietSanPhamRepository chiTietSanPhamRepository,
                                 AnhSanPhamRepository anhSanPhamRepository,
                                 ChiSoKhangBuiVaNuocRepository chiSoKhangBuiVaNuocRepository,
                                 RamRepository ramRepository,
                                 BoNhoTrongRepository boNhoTrongRepository,
                                 CongNgheMangRepository congNgheMangRepository,
                                 HoTroBoNhoNgoaiRepository hoTroBoNhoNgoaiRepository,
                                 CpuRepository cpuRepository,
                                 MauSacRepository mauSacRepository,
                                 ThietKeRepository thietKeRepository,
                                 GpuRepository gpuRepository,
                                 ImelRepository imelRepository,
                                 SimRepository simRepository,
                                 CongNgheManHinhRepository congNgheManHinhRepository, // Thay ManHinhRepository
                                 NhaSanXuatRepository nhaSanXuatRepository,
                                 HoTroCongNgheSacRepository hoTroCongNgheSacRepository,
                                 CumCameraRepository cumCameraRepository) {
        this.heDieuHanhRepository = heDieuHanhRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.pinRepository = pinRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.anhSanPhamRepository = anhSanPhamRepository;
        this.chiSoKhangBuiVaNuocRepository = chiSoKhangBuiVaNuocRepository;
        this.ramRepository = ramRepository;
        this.boNhoTrongRepository = boNhoTrongRepository;
        this.congNgheMangRepository = congNgheMangRepository;
        this.hoTroBoNhoNgoaiRepository = hoTroBoNhoNgoaiRepository;
        this.cpuRepository = cpuRepository;
        this.mauSacRepository = mauSacRepository;
        this.thietKeRepository = thietKeRepository;
        this.gpuRepository = gpuRepository;
        this.imelRepository = imelRepository;
        this.simRepository = simRepository;
        this.congNgheManHinhRepository = congNgheManHinhRepository;
        this.nhaSanXuatRepository = nhaSanXuatRepository;
        this.hoTroCongNgheSacRepository = hoTroCongNgheSacRepository;
        this.cumCameraRepository = cumCameraRepository;
    }

    public ChiTietSanPhamResponse createChiTietSanPham(ChiTietSanPhamDTO dto, List<MultipartFile> images) throws IOException {
        System.out.println("Received DTO: " + dto);
        validateInput(dto, images);

        SanPham sanPham = createOrUpdateProduct(dto);
        System.out.println("Created/Updated SanPham: " + sanPham);
        if (sanPham == null || sanPham.getId() == null) {
            throw new IllegalStateException("Failed to create or update product");
        }

        List<AnhSanPham> anhSanPhams = uploadAndSaveImages(images);
        System.out.println("Uploaded Images: " + anhSanPhams);
        if (anhSanPhams == null || anhSanPhams.isEmpty()) {
            throw new IllegalStateException("No images were saved");
        }

        List<Imel> imels = createAndSaveImels(dto.getVariants());
        System.out.println("Created Imels: " + imels);
        List<ChiTietSanPham> chiTietSanPhams = createVariants(dto, sanPham, anhSanPhams, imels);
        System.out.println("Created Variants: " + chiTietSanPhams);
        List<ChiTietSanPham> savedChiTietSanPhams = chiTietSanPhamRepository.saveAll(chiTietSanPhams);
        if (savedChiTietSanPhams == null || savedChiTietSanPhams.isEmpty()) {
            throw new IllegalStateException("Failed to save product variants");
        }

        return new ChiTietSanPhamResponse(
                sanPham.getId(),
                savedChiTietSanPhams.stream().map(ChiTietSanPham::getId).collect(Collectors.toList()),
                anhSanPhams.stream().map(AnhSanPham::getId).collect(Collectors.toList())
        );
    }

    public void updatePrice(Integer id, BigDecimal newPrice) {
        if (newPrice == null) {
            throw new IllegalArgumentException("Giá không hợp lệ: " + newPrice);
        }

        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với ID: " + id));

        chiTietSanPham.setGiaBan(newPrice);
        chiTietSanPham.setUpdatedAt(new Date());
        chiTietSanPham.setUpdatedBy(1); // Thay bằng thông tin người dùng hiện tại nếu có

        chiTietSanPhamRepository.save(chiTietSanPham);
    }

    private void validateInput(ChiTietSanPhamDTO dto, List<MultipartFile> images) {
        if (images == null || images.isEmpty() || images.stream().anyMatch(MultipartFile::isEmpty)) {
            throw new IllegalArgumentException("Phải cung cấp ít nhất một ảnh hợp lệ cho chi tiết sản phẩm");
        }
        if (dto.getVariants() == null || dto.getVariants().isEmpty()) {
            throw new IllegalArgumentException("Phải cung cấp ít nhất một biến thể cho chi tiết sản phẩm");
        }
        if (dto.getTenSanPham() == null || dto.getTenSanPham().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        }
        if (dto.getIdNhaSanXuat() == null || !nhaSanXuatRepository.existsById(dto.getIdNhaSanXuat())) {
            throw new IllegalArgumentException("Nhà sản xuất không hợp lệ");
        }
        if (dto.getIdPin() == null || !pinRepository.existsById(dto.getIdPin())) {
            throw new IllegalArgumentException("Pin không hợp lệ");
        }
        if (dto.getCongNgheManHinh() == null || !congNgheManHinhRepository.existsById(dto.getCongNgheManHinh())) {
            throw new IllegalArgumentException("Công nghệ màn hình không hợp lệ");
        }
        if (dto.getIdCpu() == null || !cpuRepository.existsById(dto.getIdCpu())) {
            throw new IllegalArgumentException("CPU không hợp lệ");
        }
        if (dto.getIdGpu() == null || !gpuRepository.existsById(dto.getIdGpu())) {
            throw new IllegalArgumentException("GPU không hợp lệ");
        }
        if (dto.getIdCumCamera() == null || !cumCameraRepository.existsById(dto.getIdCumCamera())) {
            throw new IllegalArgumentException("Cụm camera không hợp lệ");
        }
        if (dto.getIdHeDieuHanh() == null || !heDieuHanhRepository.existsById(dto.getIdHeDieuHanh())) {
            throw new IllegalArgumentException("Hệ điều hành không hợp lệ");
        }
        if (dto.getIdThietKe() == null || !thietKeRepository.existsById(dto.getIdThietKe())) {
            throw new IllegalArgumentException("Thiết kế không hợp lệ");
        }
        if (dto.getIdSim() == null || !simRepository.existsById(dto.getIdSim())) {
            throw new IllegalArgumentException("Sim không hợp lệ");
        }
        if (dto.getHoTroCongNgheSac() == null || !hoTroCongNgheSacRepository.existsById(dto.getHoTroCongNgheSac())) {
            throw new IllegalArgumentException("Hỗ trợ công nghệ sạc không hợp lệ");
        }
        if (dto.getIdCongNgheMang() == null || !congNgheMangRepository.existsById(dto.getIdCongNgheMang())) {
            throw new IllegalArgumentException("Công nghệ mạng không hợp lệ");
        }
        for (ChiTietSanPhamDTO.VariantDTO variant : dto.getVariants()) {
            if (variant.getIdMauSac() == null || !mauSacRepository.existsById(variant.getIdMauSac())) {
                throw new IllegalArgumentException("Màu sắc không hợp lệ");
            }
            if (variant.getIdRam() == null || !ramRepository.existsById(variant.getIdRam())) {
                throw new IllegalArgumentException("RAM không hợp lệ");
            }
            if (variant.getIdBoNhoTrong() == null || !boNhoTrongRepository.existsById(variant.getIdBoNhoTrong())) {
                throw new IllegalArgumentException("Bộ nhớ trong không hợp lệ");
            }
            if (variant.getDonGia() == null) {
                throw new IllegalArgumentException("Đơn giá không được để trống");
            }
        }
    }

    private SanPham createOrUpdateProduct(ChiTietSanPhamDTO dto) {
        Optional<SanPham> existingSanPham = sanPhamRepository.findByTenSanPhamAndDeletedFalse(dto.getTenSanPham());

        SanPham sanPham;
        if (existingSanPham.isPresent()) {
            sanPham = existingSanPham.get();
            updateSanPhamFields(sanPham, dto);
        } else {
            sanPham = new SanPham();
            sanPham.setTenSanPham(dto.getTenSanPham());
            sanPham.setMa(null);
            updateSanPhamFields(sanPham, dto);
        }

        return sanPhamRepository.save(sanPham);
    }

    private void updateSanPhamFields(SanPham sanPham, ChiTietSanPhamDTO dto) {
        sanPham.setIdNhaSanXuat(getEntity(nhaSanXuatRepository, dto.getIdNhaSanXuat(), "Nhà sản xuất"));
        sanPham.setIdPin(getEntity(pinRepository, dto.getIdPin(), "Pin"));
        sanPham.setCongNgheManHinh(getEntity(congNgheManHinhRepository, dto.getCongNgheManHinh(), "Công nghệ màn hình")); // Thay idManHinh
        sanPham.setIdCpu(getEntity(cpuRepository, dto.getIdCpu(), "CPU"));
        sanPham.setIdGpu(getEntity(gpuRepository, dto.getIdGpu(), "GPU"));
        sanPham.setIdCumCamera(getEntity(cumCameraRepository, dto.getIdCumCamera(), "Cụm camera"));
        sanPham.setIdHeDieuHanh(getEntity(heDieuHanhRepository, dto.getIdHeDieuHanh(), "Hệ điều hành"));
        sanPham.setIdThietKe(getEntity(thietKeRepository, dto.getIdThietKe(), "Thiết kế"));
        sanPham.setIdSim(getEntity(simRepository, dto.getIdSim(), "Sim"));
        sanPham.setHoTroCongNgheSac(getEntity(hoTroCongNgheSacRepository, dto.getHoTroCongNgheSac(), "Hỗ trợ công nghệ sạc")); // Thay idCongSac
        sanPham.setIdCongNgheMang(getEntity(congNgheMangRepository, dto.getIdCongNgheMang(), "Công nghệ mạng"));

        sanPham.setIdHoTroBoNhoNgoai(dto.getIdHoTroBoNhoNgoai() != null ?
                getEntity(hoTroBoNhoNgoaiRepository, dto.getIdHoTroBoNhoNgoai(), "Hỗ trợ bộ nhớ ngoài") : null);
        sanPham.setIdChiSoKhangBuiVaNuoc(dto.getIdChiSoKhangBuiVaNuoc() != null ?
                getEntity(chiSoKhangBuiVaNuocRepository, dto.getIdChiSoKhangBuiVaNuoc(), "Chỉ số kháng bụi nước") : null);

        sanPham.setDeleted(false);
        sanPham.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : new Date());
        sanPham.setCreatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : 1);
        sanPham.setUpdatedAt(new Date());
        sanPham.setUpdatedBy(dto.getUpdatedBy() != null ? dto.getUpdatedBy() : 1);
    }

    private <T> T getEntity(JpaRepository<T, Integer> repository, Integer id, String entityName) {
        if (id == null) {
            throw new IllegalArgumentException(entityName + " ID không được null");
        }
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(entityName + " với ID " + id + " không tồn tại"));
    }

    private List<AnhSanPham> uploadAndSaveImages(List<MultipartFile> images) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        List<AnhSanPham> anhSanPhams = new ArrayList<>();
        for (MultipartFile image : images) {
            if (image.isEmpty()) {
                continue;
            }

            validateImage(image);

            String originalFilename = StringUtils.cleanPath(image.getOriginalFilename());
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            Path filePath = uploadPath.resolve(uniqueFilename);
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            AnhSanPham anh = new AnhSanPham();
            anh.setMa(null);
            anh.setTenAnh(originalFilename);
            anh.setDuongDan(filePath.toString());
            anh.setDeleted(false);
            anhSanPhams.add(anh);
        }

        return anhSanPhamRepository.saveAll(anhSanPhams);
    }

    private void validateImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new IllegalArgumentException("Chỉ chấp nhận file ảnh JPEG hoặc PNG");
        }
        if (file.getSize() > 10 * 1024 * 1024) { // 10MB
            throw new IllegalArgumentException("File không được vượt quá 10MB");
        }
    }

    private List<Imel> createAndSaveImels(List<ChiTietSanPhamDTO.VariantDTO> variants) {
        List<Imel> imels = new ArrayList<>();

        for (ChiTietSanPhamDTO.VariantDTO variant : variants) {
            if (variant.getImeiList() != null && !variant.getImeiList().isEmpty()) {
                for (String imei : variant.getImeiList()) {
                    Optional<Imel> existingImel = imelRepository.findByImel(imei);

                    if (existingImel.isPresent()) {
                        imels.add(existingImel.get());
                    } else {
                        Imel imel = new Imel();
                        imel.setMa(null);
                        imel.setImel(imei);
                        imel.setDeleted(false);
                        imels.add(imel);
                    }
                }
            }
        }

        List<Imel> newImels = imels.stream()
                .filter(imel -> imel.getId() == null)
                .collect(Collectors.toList());

        if (!newImels.isEmpty()) {
            imelRepository.saveAll(newImels);
        }

        return imels;
    }

    private List<ChiTietSanPham> createVariants(ChiTietSanPhamDTO dto, SanPham sanPham,
                                                List<AnhSanPham> anhSanPhams, List<Imel> imels) {
        List<ChiTietSanPham> chiTietSanPhams = new ArrayList<>();
        int imelIndex = 0;

        for (ChiTietSanPhamDTO.VariantDTO variant : dto.getVariants()) {
            if (variant.getImeiList() != null && !variant.getImeiList().isEmpty()) {
                for (String imei : variant.getImeiList()) {
                    ChiTietSanPham chiTiet = new ChiTietSanPham();
                    chiTiet.setIdSanPham(sanPham);

                    Imel imelToAssign;
                    if (imelIndex < imels.size()) {
                        imelToAssign = imels.get(imelIndex);
                        imelIndex++;
                    } else {
                        imelToAssign = new Imel();
                        imelToAssign.setMa("DEFAULT-" + UUID.randomUUID().toString());
                        imelToAssign.setImel("N/A");
                        imelToAssign.setDeleted(false);
                        imelToAssign = imelRepository.save(imelToAssign);
                    }
                    chiTiet.setIdImel(imelToAssign);

                    chiTiet.setIdMauSac(getEntity(mauSacRepository, variant.getIdMauSac(), "Màu sắc"));
                    chiTiet.setIdRam(getEntity(ramRepository, variant.getIdRam(), "RAM"));
                    chiTiet.setIdBoNhoTrong(getEntity(boNhoTrongRepository, variant.getIdBoNhoTrong(), "Bộ nhớ trong"));

                    chiTiet.setMa(null);
                    chiTiet.setGiaBan(variant.getDonGia() != null ? variant.getDonGia() : dto.getGiaBan());
                    chiTiet.setGhiChu(dto.getGhiChu());

                    chiTiet.setDeleted(false);
                    chiTiet.setCreatedAt(new Date());
                    chiTiet.setCreatedBy(1);
                    chiTiet.setUpdatedAt(new Date());
                    chiTiet.setUpdatedBy(1);

                    Integer imageIndex = variant.getImageIndex();
                    if (imageIndex != null && imageIndex >= 0 && imageIndex < anhSanPhams.size()) {
                        chiTiet.setIdAnhSanPham(anhSanPhams.get(imageIndex));
                    } else {
                        chiTiet.setIdAnhSanPham(anhSanPhams.get(0));
                    }

                    chiTietSanPhams.add(chiTiet);
                }
            } else {
                ChiTietSanPham chiTiet = new ChiTietSanPham();
                chiTiet.setIdSanPham(sanPham);

                Imel imelToAssign = new Imel();
                imelToAssign.setMa(null);
                imelToAssign.setImel("N/A");
                imelToAssign.setDeleted(false);
                imelToAssign = imelRepository.save(imelToAssign);
                chiTiet.setIdImel(imelToAssign);

                chiTiet.setIdMauSac(getEntity(mauSacRepository, variant.getIdMauSac(), "Màu sắc"));
                chiTiet.setIdRam(getEntity(ramRepository, variant.getIdRam(), "RAM"));
                chiTiet.setIdBoNhoTrong(getEntity(boNhoTrongRepository, variant.getIdBoNhoTrong(), "Bộ nhớ trong"));

                chiTiet.setMa(null);
                chiTiet.setGiaBan(variant.getDonGia() != null ? variant.getDonGia() : dto.getGiaBan());
                chiTiet.setGhiChu(dto.getGhiChu());

                chiTiet.setDeleted(false);
                chiTiet.setCreatedAt(new Date());
                chiTiet.setCreatedBy(1);
                chiTiet.setUpdatedAt(new Date());
                chiTiet.setUpdatedBy(1);

                Integer imageIndex = variant.getImageIndex();
                if (imageIndex != null && imageIndex >= 0 && imageIndex < anhSanPhams.size()) {
                    chiTiet.setIdAnhSanPham(anhSanPhams.get(imageIndex));
                } else {
                    chiTiet.setIdAnhSanPham(anhSanPhams.get(0));
                }

                chiTietSanPhams.add(chiTiet);
            }
        }

        return chiTietSanPhams;
    }

    public List<ChiTietSanPhamDTO> getChiTietSanPhamBySanPhamId(Integer sanPhamId) {
        List<ChiTietSanPham> chiTietSanPhams = chiTietSanPhamRepository.findByIdSanPhamIdAndDeletedFalse(sanPhamId, false);
        return mapToDTOList(chiTietSanPhams);
    }

    public Page<ChiTietSanPhamDTO> getChiTietSanPhamDetails(Integer sanPhamId, String keyword, String status, Integer idMauSac, Integer idBoNhoTrong, Integer idRam, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<ChiTietSanPham> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("idSanPham").get("id"), sanPhamId));
            predicates.add(cb.equal(root.get("deleted"), status == null || "active".equals(status) ? false : true));
            if (keyword != null && !keyword.isEmpty()) {
                Predicate maPredicate = cb.like(cb.lower(root.get("ma")), "%" + keyword.toLowerCase() + "%");
                Predicate imeiPredicate = cb.like(cb.lower(root.get("idImel").get("imel")), "%" + keyword.toLowerCase() + "%");
                predicates.add(cb.or(maPredicate, imeiPredicate));
            }
            if (idMauSac != null) {
                predicates.add(cb.equal(root.get("idMauSac").get("id"), idMauSac));
            }
            if (idBoNhoTrong != null) {
                predicates.add(cb.equal(root.get("idBoNhoTrong").get("id"), idBoNhoTrong));
            }
            if (idRam != null) {
                predicates.add(cb.equal(root.get("idRam").get("id"), idRam));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("giaBan"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("giaBan"), maxPrice));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<ChiTietSanPham> chiTietSanPhamPage = chiTietSanPhamRepository.findAll(spec, pageable);
        List<ChiTietSanPhamDTO> dtos = mapToDTOList(chiTietSanPhamPage.getContent());
        return new PageImpl<>(dtos, pageable, chiTietSanPhamPage.getTotalElements());
    }

    public Map<String, BigDecimal> getPriceRange(Integer sanPhamId) {
        List<ChiTietSanPham> chiTietSanPhams = chiTietSanPhamRepository.findByIdSanPhamIdAndDeletedFalse(sanPhamId, false);

        if (chiTietSanPhams.isEmpty()) {
            return Map.of(
                    "minPrice", BigDecimal.ZERO,
                    "maxPrice", BigDecimal.valueOf(10000000) // Giá mặc định nếu không có sản phẩm
            );
        }

        BigDecimal minPrice = chiTietSanPhams.stream()
                .map(ChiTietSanPham::getGiaBan)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal maxPrice = chiTietSanPhams.stream()
                .map(ChiTietSanPham::getGiaBan)
                .filter(Objects::nonNull)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.valueOf(10000000));

        return Map.of(
                "minPrice", minPrice,
                "maxPrice", maxPrice
        );
    }

    private List<ChiTietSanPhamDTO> mapToDTOList(List<ChiTietSanPham> chiTietSanPhams) {
        return chiTietSanPhams.stream().map(chiTiet -> {
            ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO();
            dto.setId(chiTiet.getId());
            SanPham sanPham = chiTiet.getIdSanPham();
            dto.setIdSanPham(sanPham.getId());
            dto.setIdNhaSanXuat(sanPham.getIdNhaSanXuat().getId());
            dto.setIdPin(sanPham.getIdPin().getId());
            dto.setCongNgheManHinh(sanPham.getCongNgheManHinh().getId()); // Thay idManHinh
            dto.setIdCpu(sanPham.getIdCpu().getId());
            dto.setIdGpu(sanPham.getIdGpu().getId());
            dto.setIdCumCamera(sanPham.getIdCumCamera().getId());
            dto.setIdHeDieuHanh(sanPham.getIdHeDieuHanh().getId());
            dto.setIdThietKe(sanPham.getIdThietKe().getId());
            dto.setIdSim(sanPham.getIdSim().getId());
            dto.setHoTroCongNgheSac(sanPham.getHoTroCongNgheSac().getId()); // Thay idCongSac
            dto.setIdCongNgheMang(sanPham.getIdCongNgheMang().getId());
            dto.setTenSanPham(sanPham.getTenSanPham());
            dto.setMa(chiTiet.getMa());
            dto.setIdHoTroBoNhoNgoai(sanPham.getIdHoTroBoNhoNgoai() != null ? sanPham.getIdHoTroBoNhoNgoai().getId() : null);
            dto.setIdChiSoKhangBuiVaNuoc(sanPham.getIdChiSoKhangBuiVaNuoc() != null ? sanPham.getIdChiSoKhangBuiVaNuoc().getId() : null);
            dto.setGhiChu(chiTiet.getGhiChu());
            dto.setGiaBan(chiTiet.getGiaBan());
            dto.setCreatedAt(chiTiet.getCreatedAt());
            dto.setCreatedBy(chiTiet.getCreatedBy());
            dto.setUpdatedAt(chiTiet.getUpdatedAt());
            dto.setUpdatedBy(chiTiet.getUpdatedBy());

            ChiTietSanPhamDTO.VariantDTO variantDTO = new ChiTietSanPhamDTO.VariantDTO();
            variantDTO.setIdImel(chiTiet.getIdImel());
            variantDTO.setMauSac(chiTiet.getIdMauSac() != null ? chiTiet.getIdMauSac().getMauSac() : null);
            variantDTO.setDungLuongRam(chiTiet.getIdRam() != null ? chiTiet.getIdRam().getDungLuongRam() : null);
            variantDTO.setDungLuongBoNhoTrong(chiTiet.getIdBoNhoTrong() != null ? chiTiet.getIdBoNhoTrong().getDungLuongBoNhoTrong() : null);
            variantDTO.setImageIndex(chiTiet.getIdAnhSanPham() != null ? anhSanPhamRepository.findAll().indexOf(chiTiet.getIdAnhSanPham()) : 0);
            variantDTO.setDonGia(chiTiet.getGiaBan());

            dto.setVariants(List.of(variantDTO));
            return dto;
        }).collect(Collectors.toList());
    }

    public record ChiTietSanPhamResponse(Integer sanPhamId, List<Integer> chiTietSanPhamIds, List<Integer> anhSanPhamIds) {}
}