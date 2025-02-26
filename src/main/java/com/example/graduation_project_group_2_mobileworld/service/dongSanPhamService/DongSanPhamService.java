package com.example.graduation_project_group_2_mobileworld.service.dongSanPhamService;

import com.example.graduation_project_group_2_mobileworld.dto.dongSanPhamDTO.DongSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import com.example.graduation_project_group_2_mobileworld.repository.dongSanPham.DongSanPhamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DongSanPhamService {

    private final DongSanPhamRepository dongSanPhamRepository;

    public DongSanPhamService(DongSanPhamRepository dongSanPhamRepository) {
        this.dongSanPhamRepository = dongSanPhamRepository;
    }

    // Lấy tất cả dòng sản phẩm chưa bị xóa
    public List<DongSanPhamDTO> getAllDongSanPham() {
        return dongSanPhamRepository.findByDeletedFalse()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy danh sách dòng sản phẩm chưa bị xóa với phân trang
    public Page<DongSanPhamDTO> getAllDongSanPham(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DongSanPham> productPage = dongSanPhamRepository.findByDeletedFalse(pageable);
        return productPage.map(this::convertToDTO);
    }

    // Thêm mới dòng sản phẩm
    public DongSanPhamDTO createDongSanPham(DongSanPhamDTO dto) {
        DongSanPham newDSP = new DongSanPham();
        newDSP.setMa(dto.getMa());
        newDSP.setDongSanPham(dto.getDongSanPham());
        newDSP.setDeleted(false); // Mặc định chưa bị xóa

        DongSanPham savedDSP = dongSanPhamRepository.save(newDSP);
        return convertToDTO(savedDSP);
    }

    // Cập nhật dòng sản phẩm
    public DongSanPhamDTO updateDongSanPham(Integer id, DongSanPhamDTO dto) {
        return dongSanPhamRepository.findById(id)
                .map(dsp -> {
                    dsp.setMa(dto.getMa());
                    dsp.setDongSanPham(dto.getDongSanPham());
                    DongSanPham updatedDSP = dongSanPhamRepository.save(dsp);
                    return convertToDTO(updatedDSP);
                })
                .orElseThrow(() -> new RuntimeException("Dòng sản phẩm không tồn tại!"));
    }

    // Xóa dòng sản phẩm (Chuyển trạng thái deleted = true)
    public boolean deleteDongSanPham(Integer id) {
        return dongSanPhamRepository.findById(id)
                .map(dsp -> {
                    dsp.setDeleted(true);
                    dongSanPhamRepository.save(dsp);
                    return true;
                })
                .orElse(false);
    }

    // Tìm kiếm dòng sản phẩm theo keyword và phân trang
    public Page<DongSanPhamDTO> searchDongSanPham(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DongSanPham> resultPage = dongSanPhamRepository.searchByKeyword(keyword, pageable);
        return resultPage.map(this::convertToDTO);
    }

    // Hàm chuyển đổi từ Entity sang DTO
    private DongSanPhamDTO convertToDTO(DongSanPham dongSanPham) {
        return new DongSanPhamDTO(dongSanPham.getId(), dongSanPham.getMa(), dongSanPham.getDongSanPham());
    }

    // Kiểm tra mã dòng sản phẩm đã tồn tại chưa
    public boolean existsByMa(String ma) {
        return dongSanPhamRepository.existsByMa(ma);
    }

    // Kiểm tra tên dòng sản phẩm đã tồn tại chưa
    public boolean existsByDongSanPham(String dongSanPham) {
        return dongSanPhamRepository.existsByDongSanPham(dongSanPham);
    }
}