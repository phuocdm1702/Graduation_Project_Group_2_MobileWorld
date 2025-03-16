package com.example.graduation_project_group_2_mobileworld.service.san_pham.chi_tiet_san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.chi_tiet_san_pham.ChiTietSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.dto.san_pham.chi_tiet_san_pham.ChiTietSanPhamFilterDTO;
import com.example.graduation_project_group_2_mobileworld.entity.*;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.bo_nho_trong.BoNhoTrongRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.chi_tiet_san_pham.ChiTietSanPhamRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.cpu.CpuRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.cum_camera.CumCameraRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.dong_san_pham.DongSanPhamRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.gpu.GpuRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.he_dieu_hanh.HeDieuHanhRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.man_hinh.ManHinhRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.mang.CongNgheMangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.mau_sac.MauSacRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.nha_san_xuat.NhaSanXuatRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.pin.PinRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.ram.RamRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.sac.CongSacRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.sac.HoTroCongNgheSacRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.sim.SimRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.thiet_ke.ThietKeRepository;
import com.example.graduation_project_group_2_mobileworld.repository.san_pham.tinh_trang.TinhTrangRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChiTietSanPhamService {

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    private NhaSanXuatRepository nhaSanXuatRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private ManHinhRepository manHinhRepository;

    @Autowired
    private RamRepository ramRepository;

    @Autowired
    private BoNhoTrongRepository boNhoTrongRepository;

    @Autowired
    private CpuRepository cpuRepository;

    @Autowired
    private GpuRepository gpuRepository;

    @Autowired
    private CumCameraRepository cumCameraRepository;

    @Autowired
    private HeDieuHanhRepository heDieuHanhRepository;

    @Autowired
    private ThietKeRepository thietKeRepository;

    @Autowired
    private SimRepository simRepository;

    @Autowired
    private CongSacRepository congSacRepository;

    @Autowired
    private HoTroCongNgheSacRepository hoTroCongNgheSacRepository;

    @Autowired
    private DongSanPhamRepository dongSanPhamRepository;

    @Autowired
    private CongNgheMangRepository congNgheMangRepository;

    @Autowired
    private TinhTrangRepository tinhTrangRepository;

    // Load danh sách sản phẩm chi tiết với phân trang
    public Page<ChiTietSanPhamDTO> getAllChiTietSanPham(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChiTietSanPham> chiTietSanPhams = chiTietSanPhamRepository.findAllActive(pageable);
        return chiTietSanPhams.map(this::convertToDTO);
    }

    // Tìm kiếm và lọc
    public Page<ChiTietSanPhamDTO> searchAndFilterChiTietSanPham(ChiTietSanPhamFilterDTO filterDTO) {
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize());
        Page<ChiTietSanPham> chiTietSanPhams = chiTietSanPhamRepository.searchAndFilter(
                filterDTO.getKeyword(),
                filterDTO.getIdNhaSanXuat(),
                filterDTO.getIdMauSac(),
                filterDTO.getIdPin(),
                filterDTO.getIdManHinh(),
                filterDTO.getIdRam(),
                filterDTO.getIdBoNhoTrong(),
                filterDTO.getIdCpu(),
                filterDTO.getIdGpu(),
                filterDTO.getIdCumCamera(),
                filterDTO.getIdHeDieuHanh(),
                filterDTO.getIdThietKe(),
                filterDTO.getIdSim(),
                filterDTO.getIdCongSac(),
                filterDTO.getIdHoTroCongNgheSac(),
                filterDTO.getIdCongNgheMang(),
                filterDTO.getIdLoaiTinhTrang(),
                pageable
        );
        return chiTietSanPhams.map(this::convertToDTO);
    }

    // Lấy chi tiết sản phẩm theo ID
    public ChiTietSanPhamDTO getChiTietSanPhamById(Integer id) {
        Optional<ChiTietSanPham> chiTietSanPham = chiTietSanPhamRepository.findById(id);
        return chiTietSanPham.map(this::convertToDTO).orElse(null);
    }

    // Thêm mới sản phẩm chi tiết
    public ChiTietSanPhamDTO createChiTietSanPham(ChiTietSanPhamDTO dto) {
        ChiTietSanPham chiTietSanPham = new ChiTietSanPham();
        mapDTOToEntity(dto, chiTietSanPham);
        chiTietSanPham.setCreatedAt(new Date());
        chiTietSanPham.setDeleted(false);
        ChiTietSanPham saved = chiTietSanPhamRepository.save(chiTietSanPham);
        return convertToDTO(saved);
    }

    // Cập nhật sản phẩm chi tiết
    public ChiTietSanPhamDTO updateChiTietSanPham(Integer id, ChiTietSanPhamDTO dto) {
        Optional<ChiTietSanPham> optional = chiTietSanPhamRepository.findById(id);
        if (optional.isPresent()) {
            ChiTietSanPham chiTietSanPham = optional.get();
            mapDTOToEntity(dto, chiTietSanPham);
            chiTietSanPham.setUpdatedAt(new Date());
            ChiTietSanPham updated = chiTietSanPhamRepository.save(chiTietSanPham);
            return convertToDTO(updated);
        }
        return null;
    }

    // Xóa mềm sản phẩm chi tiết
    public boolean deleteChiTietSanPham(Integer id) {
        Optional<ChiTietSanPham> optional = chiTietSanPhamRepository.findById(id);
        if (optional.isPresent()) {
            ChiTietSanPham chiTietSanPham = optional.get();
            chiTietSanPham.setDeleted(true);
            chiTietSanPham.setUpdatedAt(new Date());
            chiTietSanPhamRepository.save(chiTietSanPham);
            return true;
        }
        return false;
    }

    public void deleteBulkChiTietSanPham(List<Integer> ids) {
        ids.forEach(id -> {
            Optional<ChiTietSanPham> optional = chiTietSanPhamRepository.findById(id);
            optional.ifPresent(chiTietSanPham -> {
                chiTietSanPham.setDeleted(true);
                chiTietSanPham.setUpdatedAt(new Date());
                chiTietSanPhamRepository.save(chiTietSanPham);
            });
        });
    }

    // Lấy danh sách thuộc tính để load lên combobox
    public List<NhaSanXuat> getAllNhaSanXuat() {
        return nhaSanXuatRepository.findAll();
    }

    public List<DongSanPham> getAllDongSanPham() {
        return dongSanPhamRepository.findAll();
    }

    public List<MauSac> getAllMauSac() {
        return mauSacRepository.findAll();
    }

    public List<Pin> getAllPin() {
        return pinRepository.findAll();
    }

    public List<ManHinh> getAllManHinh() {
        return manHinhRepository.findAll();
    }

    public List<Ram> getAllRam() {
        return ramRepository.findAll();
    }

    public List<BoNhoTrong> getAllBoNhoTrong() {
        return boNhoTrongRepository.findAll();
    }

    public List<Cpu> getAllCpu() {
        return cpuRepository.findAll();
    }

    public List<Gpu> getAllGpu() {
        return gpuRepository.findAll();
    }

    public List<CumCamera> getAllCumCamera() {
        return cumCameraRepository.findAll();
    }

    public List<HeDieuHanh> getAllHeDieuHanh() {
        return heDieuHanhRepository.findAll();
    }

    public List<ThietKe> getAllThietKe() {
        return thietKeRepository.findAll();
    }

    public List<Sim> getAllSim() {
        return simRepository.findAll();
    }

    public List<CongSac> getAllCongSac() {
        return congSacRepository.findAll();
    }

    public List<HoTroCongNgheSac> getAllHoTroCongNgheSac() {
        return hoTroCongNgheSacRepository.findAll();
    }

    public List<CongNgheMang> getAllCongNgheMang() {
        return congNgheMangRepository.findAll();
    }

    public List<TinhTrang> getAllTinhTrang() {
        return tinhTrangRepository.findAll();
    }

    // Chuyển đổi từ Entity sang DTO
    private ChiTietSanPhamDTO convertToDTO(ChiTietSanPham entity) {
        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO();
        BeanUtils.copyProperties(entity, dto);

        // Gán các trường tên thuộc tính
        dto.setTenNhaSanXuat(entity.getIdNhaSanXuat().getNhaSanXuat());
        dto.setDongSanPham(entity.getIdDongSanPham().getDongSanPham());
        dto.setTenMauSac(entity.getIdMauSac().getMauSac());
        dto.setDungLuongPin(entity.getIdPin().getDungLuongPin());
        dto.setKichThuocManHinh(entity.getIdManHinh().getKichThuoc());
        dto.setDungLuongRam(entity.getIdRam().getDungLuongRam());
        dto.setDungLuongBoNhoTrong(entity.getIdBoNhoTrong().getDungLuongBoNhoTrong());
        dto.setTenCpu(entity.getIdCpu().getTenCpu());
        dto.setTenGpu(entity.getIdGpu().getTenGpu());
        dto.setTenCumCamera(entity.getIdCumCamera().getMa());
        dto.setTenHeDieuHanh(entity.getIdHeDieuHanh().getHeDieuHanh());
        if (entity.getIdChiSoKhangBuiVaNuoc() != null) {
            dto.setMaChiSoKhangBuiVaNuoc(entity.getIdChiSoKhangBuiVaNuoc().getTenChiSo());
        }
        dto.setTenThietKe(entity.getIdThietKe().getChatLieuKhung());
        dto.setLoaiSim(entity.getIdSim().getCacLoaiSimHoTro());
        dto.setTenCongSac(entity.getIdCongSac().getCongSac());
        dto.setTenHoTroCongNgheSac(entity.getIdHoTroCongNgheSac().getMa());
        dto.setTenCongNgheMang(entity.getIdCongNgheMang().getTenCongNgheMang());
        dto.setTenTinhTrang(entity.getIdLoaiTinhTrang().getLoaiTinhTrang());

        // Gán ID của các thuộc tính
        dto.setIdNhaSanXuat(entity.getIdNhaSanXuat().getId());
        dto.setIdDongSanPham(entity.getIdDongSanPham().getId());
        dto.setIdMauSac(entity.getIdMauSac().getId());
        dto.setIdPin(entity.getIdPin().getId());
        dto.setIdManHinh(entity.getIdManHinh().getId());
        dto.setIdRam(entity.getIdRam().getId());
        dto.setIdBoNhoTrong(entity.getIdBoNhoTrong().getId());
        dto.setIdCpu(entity.getIdCpu().getId());
        dto.setIdGpu(entity.getIdGpu().getId());
        dto.setIdCumCamera(entity.getIdCumCamera().getId());
        dto.setIdHeDieuHanh(entity.getIdHeDieuHanh().getId());
        if (entity.getIdChiSoKhangBuiVaNuoc() != null) {
            dto.setIdChiSoKhangBuiVaNuoc(entity.getIdChiSoKhangBuiVaNuoc().getId());
        }
        dto.setIdThietKe(entity.getIdThietKe().getId());
        dto.setIdSim(entity.getIdSim().getId());
        dto.setIdCongSac(entity.getIdCongSac().getId());
        dto.setIdHoTroCongNgheSac(entity.getIdHoTroCongNgheSac().getId());
        dto.setIdCongNgheMang(entity.getIdCongNgheMang().getId());
        dto.setIdLoaiTinhTrang(entity.getIdLoaiTinhTrang().getId());

        return dto;
    }

    // Map DTO sang Entity
    private void mapDTOToEntity(ChiTietSanPhamDTO dto, ChiTietSanPham entity) {
        entity.setId(dto.getId());
        entity.setTienIchDacBiet(dto.getTienIchDacBiet());
        entity.setGiaBan(dto.getGiaBan());
        entity.setDeleted(dto.getDeleted());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setUpdatedBy(dto.getUpdatedBy());

        // Gán các thực thể liên quan
        entity.setIdNhaSanXuat(nhaSanXuatRepository.findById(dto.getIdNhaSanXuat()).orElse(null));
        entity.setIdDongSanPham(dongSanPhamRepository.findById(dto.getIdDongSanPham()).orElse(null));
        entity.setIdMauSac(mauSacRepository.findById(dto.getIdMauSac()).orElse(null));
        entity.setIdPin(pinRepository.findById(dto.getIdPin()).orElse(null));
        entity.setIdManHinh(manHinhRepository.findById(dto.getIdManHinh()).orElse(null));
        entity.setIdRam(ramRepository.findById(dto.getIdRam()).orElse(null));
        entity.setIdBoNhoTrong(boNhoTrongRepository.findById(dto.getIdBoNhoTrong()).orElse(null));
        entity.setIdCpu(cpuRepository.findById(dto.getIdCpu()).orElse(null));
        entity.setIdGpu(gpuRepository.findById(dto.getIdGpu()).orElse(null));
        entity.setIdCumCamera(cumCameraRepository.findById(dto.getIdCumCamera()).orElse(null));
        entity.setIdHeDieuHanh(heDieuHanhRepository.findById(dto.getIdHeDieuHanh()).orElse(null));
        entity.setIdThietKe(thietKeRepository.findById(dto.getIdThietKe()).orElse(null));
        entity.setIdSim(simRepository.findById(dto.getIdSim()).orElse(null));
        entity.setIdCongSac(congSacRepository.findById(dto.getIdCongSac()).orElse(null));
        entity.setIdHoTroCongNgheSac(hoTroCongNgheSacRepository.findById(dto.getIdHoTroCongNgheSac()).orElse(null));
        entity.setIdCongNgheMang(congNgheMangRepository.findById(dto.getIdCongNgheMang()).orElse(null));
        entity.setIdLoaiTinhTrang(tinhTrangRepository.findById(dto.getIdLoaiTinhTrang()).orElse(null));
    }
}