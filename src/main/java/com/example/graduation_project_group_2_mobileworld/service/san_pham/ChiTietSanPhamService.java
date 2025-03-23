package com.example.graduation_project_group_2_mobileworld.service.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ChiTietSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.AnhSanPhamRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.HoTroBoNhoNgoaiRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.BoNhoTrongRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ChiSoKhangBuiVaNuocRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CpuRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CumCameraRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.GpuRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.HeDieuHanhRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ImelRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ManHinhRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CongNgheMangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.MauSacRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.NhaSanXuatRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.PinRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.RamRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.CongSacRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.HoTroCongNgheSacRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ChiTietSanPhamRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.SanPhamRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.SimRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ThietKeRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.TinhTrangRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    public ChiTietSanPhamService(HeDieuHanhRepository heDieuHanhRepository, SanPhamRepository sanPhamRepository, PinRepository pinRepository, ChiTietSanPhamRepository chiTietSanPhamRepository, AnhSanPhamRepository anhSanPhamRepository, ChiSoKhangBuiVaNuocRepository chiSoKhangBuiVaNuocRepository, RamRepository ramRepository, BoNhoTrongRepository boNhoTrongRepository, CongNgheMangRepository congNgheMangRepository, HoTroBoNhoNgoaiRepository hoTroBoNhoNgoaiRepository, CpuRepository cpuRepository, TinhTrangRepository tinhTrangRepository, MauSacRepository mauSacRepository, ThietKeRepository thietKeRepository, GpuRepository gpuRepository, ImelRepository imelRepository, CongSacRepository congSacRepository, SimRepository simRepository, ManHinhRepository manHinhRepository, NhaSanXuatRepository nhaSanXuatRepository, HoTroCongNgheSacRepository hoTroCongNgheSacRepository, CumCameraRepository cumCameraRepository) {
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

    public void createChiTietSanPham(ChiTietSanPhamDTO dto, List<MultipartFile> images) throws IOException {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("Phải cung cấp ít nhất một ảnh cho chi tiết sản phẩm");
        }

        // Tạo hoặc lấy sản phẩm
        SanPham sanPham = new SanPham();
        if (dto.getIdSanPham() != null) {
            sanPham = sanPhamRepository.findById(dto.getIdSanPham())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        } else {
            sanPham.setTenSanPham(dto.getTenSanPham());
            sanPham.setMa(UUID.randomUUID().toString());
            sanPham.setIdNhaSanXuat(nhaSanXuatRepository.findById(dto.getIdNhaSanXuat())
                    .orElseThrow(() -> new RuntimeException("Nhà sản xuất không tồn tại")));
            sanPham.setIdPin(pinRepository.findById(dto.getIdPin())
                    .orElseThrow(() -> new RuntimeException("Pin không tồn tại")));
            sanPham.setIdManHinh(manHinhRepository.findById(dto.getIdManHinh())
                    .orElseThrow(() -> new RuntimeException("Màn hình không tồn tại")));
            sanPham.setIdCpu(cpuRepository.findById(dto.getIdCpu())
                    .orElseThrow(() -> new RuntimeException("CPU không tồn tại")));
            sanPham.setIdGpu(gpuRepository.findById(dto.getIdGpu())
                    .orElseThrow(() -> new RuntimeException("GPU không tồn tại")));
            sanPham.setIdCumCamera(cumCameraRepository.findById(dto.getIdCumCamera())
                    .orElseThrow(() -> new RuntimeException("Cụm camera không tồn tại")));
            sanPham.setIdHeDieuHanh(heDieuHanhRepository.findById(dto.getIdHeDieuHanh())
                    .orElseThrow(() -> new RuntimeException("Hệ điều hành không tồn tại")));
            sanPham.setIdThietKe(thietKeRepository.findById(dto.getIdThietKe())
                    .orElseThrow(() -> new RuntimeException("Thiết kế không tồn tại")));
            sanPham.setIdSim(simRepository.findById(dto.getIdSim())
                    .orElseThrow(() -> new RuntimeException("Sim không tồn tại")));
            sanPham.setIdCongSac(congSacRepository.findById(dto.getIdCongSac())
                    .orElseThrow(() -> new RuntimeException("Cổng sạc không tồn tại")));
            sanPham.setIdHoTroCongNgheSac(hoTroCongNgheSacRepository.findById(dto.getIdHoTroCongNgheSac())
                    .orElseThrow(() -> new RuntimeException("Hỗ trợ công nghệ sạc không tồn tại")));
            sanPham.setIdCongNgheMang(congNgheMangRepository.findById(dto.getIdCongNgheMang())
                    .orElseThrow(() -> new RuntimeException("Công nghệ mạng không tồn tại")));
            // Các trường không bắt buộc
            sanPham.setIdHoTroBoNhoNgoai(dto.getIdHoTroBoNhoNgoai() != null ?
                    hoTroBoNhoNgoaiRepository.findById(dto.getIdHoTroBoNhoNgoai())
                            .orElseThrow(() -> new RuntimeException("Hỗ trợ bộ nhớ ngoài không tồn tại")) : null);
            sanPham.setIdChiSoKhangBuiVaNuoc(dto.getIdChiSoKhangBuiVaNuoc() != null ?
                    chiSoKhangBuiVaNuocRepository.findById(dto.getIdChiSoKhangBuiVaNuoc())
                            .orElseThrow(() -> new RuntimeException("Chỉ số kháng bụi nước không tồn tại")) : null);
            sanPham.setDeleted(false);
            sanPham.setCreatedAt(new Date());
            sanPham.setCreatedBy(dto.getCreatedBy()); // Thêm trường created_by
            sanPham.setUpdatedAt(dto.getUpdatedAt()); // Thêm trường updated_at
            sanPham.setUpdatedBy(dto.getUpdatedBy()); // Thêm trường updated_by
            sanPham = sanPhamRepository.save(sanPham);
        }

        // Upload và lưu tất cả ảnh mới
        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        List<AnhSanPham> anhSanPhams = new ArrayList<>();
        for (MultipartFile image : images) {
            String originalFileName = image.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFileName;
            File dest = new File(uploadDir + fileName);
            image.transferTo(dest);

            AnhSanPham anh = new AnhSanPham();
            anh.setMa(UUID.randomUUID().toString());
            anh.setTenAnh(originalFileName);
            anh.setDuongDan(fileName);
            anh.setDeleted(false);
            anhSanPhams.add(anh);
        }
        anhSanPhamRepository.saveAll(anhSanPhams); // Lưu để có ID

        // Tạo các biến thể (chi tiết sản phẩm)
        List<ChiTietSanPham> chiTietSanPhams = new ArrayList<>();
        for (ChiTietSanPhamDTO.VariantDTO variant : dto.getVariants()) {
            ChiTietSanPham chiTiet = new ChiTietSanPham();
            chiTiet.setIdSanPham(sanPham);
            chiTiet.setIdImel(imelRepository.findById(variant.getIdImel())
                    .orElseThrow(() -> new RuntimeException("IMEL không tồn tại")));
            chiTiet.setIdMauSac(mauSacRepository.findById(variant.getIdMauSac())
                    .orElseThrow(() -> new RuntimeException("Màu sắc không tồn tại")));
            chiTiet.setIdRam(ramRepository.findById(variant.getIdRam())
                    .orElseThrow(() -> new RuntimeException("RAM không tồn tại")));
            chiTiet.setIdBoNhoTrong(boNhoTrongRepository.findById(variant.getIdBoNhoTrong())
                    .orElseThrow(() -> new RuntimeException("Bộ nhớ trong không tồn tại")));
            chiTiet.setIdLoaiTinhTrang(tinhTrangRepository.findById(variant.getIdLoaiTinhTrang())
                    .orElseThrow(() -> new RuntimeException("Tình trạng không tồn tại")));
            chiTiet.setMa(UUID.randomUUID().toString());
            chiTiet.setGiaBan(dto.getGiaBan());
            chiTiet.setTienIchDacBiet(dto.getTienIchDacBiet());
            chiTiet.setGhiChu(dto.getGhiChu()); // Thêm trường ghi_chu
            chiTiet.setDeleted(false);
            chiTiet.setCreatedAt(new Date());
            chiTiet.setCreatedBy(dto.getCreatedBy()); // Thêm trường created_by
            chiTiet.setUpdatedAt(dto.getUpdatedAt()); // Thêm trường updated_at
            chiTiet.setUpdatedBy(dto.getUpdatedBy()); // Thêm trường updated_by

            // Gắn ảnh được chọn dựa trên imageIndex
            Integer imageIndex = variant.getImageIndex();
            if (imageIndex != null && imageIndex >= 0 && imageIndex < anhSanPhams.size()) {
                chiTiet.setIdAnhSanPham(anhSanPhams.get(imageIndex)); // Gắn ảnh tại index
            } else {
                chiTiet.setIdAnhSanPham(anhSanPhams.get(0)); // Mặc định ảnh đầu tiên
            }

            chiTietSanPhams.add(chiTiet);
        }

        // Lưu chi tiết sản phẩm
        chiTietSanPhamRepository.saveAll(chiTietSanPhams);
    }

    public List<AnhSanPham> getLastSavedImages(int limit) {
        return anhSanPhamRepository.findTopNByOrderByIdDesc(limit);
    }
}