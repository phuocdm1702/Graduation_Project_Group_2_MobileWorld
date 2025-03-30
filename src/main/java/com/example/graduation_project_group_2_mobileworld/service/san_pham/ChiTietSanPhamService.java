package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ChiTietSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private final ManHinhRepository manHinhRepository;
    private final NhaSanXuatRepository nhaSanXuatRepository;
    private final CumCameraRepository cumCameraRepository;
    private final SimRepository simRepository;
    private final ThietKeRepository thietKeRepository;
    private final PinRepository pinRepository;
    private final CpuRepository cpuRepository;
    private final GpuRepository gpuRepository;
    private final CongNgheMangRepository congNgheMangRepository;
    private final CongSacRepository congSacRepository;
    private final HoTroCongNgheSacRepository hoTroCongNgheSacRepository;
    private final ChiSoKhangBuiVaNuocRepository chiSoKhangBuiVaNuocRepository;
    private final TinhTrangRepository tinhTrangRepository;
    private final HoTroBoNhoNgoaiRepository hoTroBoNhoNgoaiRepository;

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
                                 TinhTrangRepository tinhTrangRepository,
                                 MauSacRepository mauSacRepository,
                                 ThietKeRepository thietKeRepository,
                                 GpuRepository gpuRepository,
                                 ImelRepository imelRepository,
                                 CongSacRepository congSacRepository,
                                 SimRepository simRepository,
                                 ManHinhRepository manHinhRepository,
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
        this.tinhTrangRepository = tinhTrangRepository;
        this.mauSacRepository = mauSacRepository;
        this.thietKeRepository = thietKeRepository;
        this.gpuRepository = gpuRepository;
        this.imelRepository = imelRepository;
        this.congSacRepository = congSacRepository;
        this.simRepository = simRepository;
        this.manHinhRepository = manHinhRepository;
        this.nhaSanXuatRepository = nhaSanXuatRepository;
        this.hoTroCongNgheSacRepository = hoTroCongNgheSacRepository;
        this.cumCameraRepository = cumCameraRepository;
    }

    public ChiTietSanPhamResponse createChiTietSanPham(ChiTietSanPhamDTO dto, List<MultipartFile> images) throws IOException {
        // Validate input
        validateInput(dto, images);

        // Create or update product
        SanPham sanPham = createOrUpdateProduct(dto);

        // Upload and save images
        List<AnhSanPham> anhSanPhams = uploadAndSaveImages(images);

        // Create variants
        List<ChiTietSanPham> chiTietSanPhams = createVariants(dto, sanPham, anhSanPhams);

        // Save variants
        List<ChiTietSanPham> savedChiTietSanPhams = chiTietSanPhamRepository.saveAll(chiTietSanPhams);

        return new ChiTietSanPhamResponse(
                sanPham.getId(),
                savedChiTietSanPhams.stream().map(ChiTietSanPham::getId).toList(),
                anhSanPhams.stream().map(AnhSanPham::getId).toList()
        );
    }

    private void validateInput(ChiTietSanPhamDTO dto, List<MultipartFile> images) {
        if (images == null || images.isEmpty() || images.stream().anyMatch(MultipartFile::isEmpty)) {
            throw new IllegalArgumentException("Phải cung cấp ít nhất một ảnh hợp lệ cho chi tiết sản phẩm");
        }
        if (dto.getVariants() == null || dto.getVariants().isEmpty()) {
            throw new IllegalArgumentException("Phải cung cấp ít nhất một biến thể cho chi tiết sản phẩm");
        }
    }

    private SanPham createOrUpdateProduct(ChiTietSanPhamDTO dto) {
        if (dto.getIdSanPham() != null) {
            return sanPhamRepository.findById(dto.getIdSanPham())
                    .orElseThrow(() -> new IllegalArgumentException("Sản phẩm với ID " + dto.getIdSanPham() + " không tồn tại"));
        }

        SanPham sanPham = new SanPham();
        sanPham.setTenSanPham(dto.getTenSanPham());
        sanPham.setMa(UUID.randomUUID().toString());

        // Set required fields
        sanPham.setIdNhaSanXuat(getEntity(nhaSanXuatRepository, dto.getIdNhaSanXuat(), "Nhà sản xuất"));
        sanPham.setIdPin(getEntity(pinRepository, dto.getIdPin(), "Pin"));
        sanPham.setIdManHinh(getEntity(manHinhRepository, dto.getIdManHinh(), "Màn hình"));
        sanPham.setIdCpu(getEntity(cpuRepository, dto.getIdCpu(), "CPU"));
        sanPham.setIdGpu(getEntity(gpuRepository, dto.getIdGpu(), "GPU"));
        sanPham.setIdCumCamera(getEntity(cumCameraRepository, dto.getIdCumCamera(), "Cụm camera"));
        sanPham.setIdHeDieuHanh(getEntity(heDieuHanhRepository, dto.getIdHeDieuHanh(), "Hệ điều hành"));
        sanPham.setIdThietKe(getEntity(thietKeRepository, dto.getIdThietKe(), "Thiết kế"));
        sanPham.setIdSim(getEntity(simRepository, dto.getIdSim(), "Sim"));
        sanPham.setIdCongSac(getEntity(congSacRepository, dto.getIdCongSac(), "Cổng sạc"));
        sanPham.setIdHoTroCongNgheSac(getEntity(hoTroCongNgheSacRepository, dto.getIdHoTroCongNgheSac(), "Hỗ trợ công nghệ sạc"));
        sanPham.setIdCongNgheMang(getEntity(congNgheMangRepository, dto.getIdCongNgheMang(), "Công nghệ mạng"));

        // Set optional fields
        sanPham.setIdHoTroBoNhoNgoai(dto.getIdHoTroBoNhoNgoai() != null ?
                getEntity(hoTroBoNhoNgoaiRepository, dto.getIdHoTroBoNhoNgoai(), "Hỗ trợ bộ nhớ ngoài") : null);
        sanPham.setIdChiSoKhangBuiVaNuoc(dto.getIdChiSoKhangBuiVaNuoc() != null ?
                getEntity(chiSoKhangBuiVaNuocRepository, dto.getIdChiSoKhangBuiVaNuoc(), "Chỉ số kháng bụi nước") : null);

        // Set audit fields
        sanPham.setDeleted(false);
        sanPham.setCreatedAt(new Date());
        sanPham.setCreatedBy(dto.getCreatedBy());
        sanPham.setUpdatedAt(dto.getUpdatedAt());
        sanPham.setUpdatedBy(dto.getUpdatedBy());

        return sanPhamRepository.save(sanPham);
    }

    private <T> T getEntity(JpaRepository<T, Integer> repository, Integer id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(entityName + " với ID " + id + " không tồn tại"));
    }

    private List<AnhSanPham> uploadAndSaveImages(List<MultipartFile> images) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        List<AnhSanPham> anhSanPhams = new ArrayList<>();
        for (MultipartFile image : images) {
            if (image.isEmpty()) {
                continue;
            }

            // Validate image
            validateImage(image);

            // Generate unique filename
            String originalFilename = StringUtils.cleanPath(image.getOriginalFilename());
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Save file
            Path filePath = uploadPath.resolve(uniqueFilename);
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Create image entity
            AnhSanPham anh = new AnhSanPham();
            anh.setMa(UUID.randomUUID().toString());
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

    private List<ChiTietSanPham> createVariants(ChiTietSanPhamDTO dto, SanPham sanPham, List<AnhSanPham> anhSanPhams) {
        List<ChiTietSanPham> chiTietSanPhams = new ArrayList<>();

        for (ChiTietSanPhamDTO.VariantDTO variant : dto.getVariants()) {
            ChiTietSanPham chiTiet = new ChiTietSanPham();
            chiTiet.setIdSanPham(sanPham);
            chiTiet.setIdImel(getEntity(imelRepository, variant.getIdImel(), "IMEL"));
            chiTiet.setIdMauSac(getEntity(mauSacRepository, variant.getIdMauSac(), "Màu sắc"));
            chiTiet.setIdRam(getEntity(ramRepository, variant.getIdRam(), "RAM"));
            chiTiet.setIdBoNhoTrong(getEntity(boNhoTrongRepository, variant.getIdBoNhoTrong(), "Bộ nhớ trong"));
            chiTiet.setIdLoaiTinhTrang(getEntity(tinhTrangRepository, variant.getIdLoaiTinhTrang(), "Tình trạng"));

            // Set common fields
            chiTiet.setMa(UUID.randomUUID().toString());
            chiTiet.setGiaBan(dto.getGiaBan());
            chiTiet.setTienIchDacBiet(dto.getTienIchDacBiet());
            chiTiet.setGhiChu(dto.getGhiChu());

            // Set audit fields
            chiTiet.setDeleted(false);
            chiTiet.setCreatedAt(new Date());
            chiTiet.setCreatedBy(dto.getCreatedBy());
            chiTiet.setUpdatedAt(dto.getUpdatedAt());
            chiTiet.setUpdatedBy(dto.getUpdatedBy());

            // Set image
            Integer imageIndex = variant.getImageIndex();
            if (imageIndex != null && imageIndex >= 0 && imageIndex < anhSanPhams.size()) {
                chiTiet.setIdAnhSanPham(anhSanPhams.get(imageIndex));
            } else {
                chiTiet.setIdAnhSanPham(anhSanPhams.get(0)); // Default to first image
            }

            chiTietSanPhams.add(chiTiet);
        }

        return chiTietSanPhams;
    }



    public List<AnhSanPham> getLastSavedImages(int limit) {
        return anhSanPhamRepository.findTopNByOrderByIdDesc(limit);
    }

    public record ChiTietSanPhamResponse(Integer sanPhamId, List<Integer> chiTietSanPhamIds, List<Integer> anhSanPhamIds) {}
}