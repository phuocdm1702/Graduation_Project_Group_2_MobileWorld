package com.example.graduation_project_group_2_mobileworld.service.dongSanPhamService;

import com.example.graduation_project_group_2_mobileworld.dto.dongSanPhamDTO.DongSanPhamDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DongSanPham;
import com.example.graduation_project_group_2_mobileworld.repository.dongSanPham.DongSanPhamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DongSanPhamService {

    private final DongSanPhamRepository dongSanPhamRepository;

    public DongSanPhamService(DongSanPhamRepository dongSanPhamRepository) {
        this.dongSanPhamRepository = dongSanPhamRepository;
    }

    // Lấy tất cả dòng sản phẩm
    public List<DongSanPhamDTO> getAllDongSanPham() {
        return dongSanPhamRepository.findByDeletedFalse()
                .stream()
                .map(dsp -> new DongSanPhamDTO(dsp.getId(), dsp.getMa(), dsp.getDongSanPham()))
                .collect(Collectors.toList());
    }

    // Thêm mới dòng sản phẩm
    public DongSanPhamDTO createDongSanPham(DongSanPhamDTO dto) {
        DongSanPham newDSP = new DongSanPham();
        newDSP.setMa(dto.getMa());
        newDSP.setDongSanPham(dto.getDongSanPham());

        DongSanPham savedDSP = dongSanPhamRepository.save(newDSP);
        return new DongSanPhamDTO(savedDSP.getId(), savedDSP.getMa(), savedDSP.getDongSanPham());
    }

    // Cập nhật dòng sản phẩm
    public DongSanPhamDTO updateDongSanPham(Integer id, DongSanPhamDTO dto) {
        return dongSanPhamRepository.findById(id)
                .map(dsp -> {
                    dsp.setMa(dto.getMa());
                    dsp.setDongSanPham(dto.getDongSanPham());
                    DongSanPham updatedDSP = dongSanPhamRepository.save(dsp);
                    return new DongSanPhamDTO(updatedDSP.getId(), updatedDSP.getMa(), updatedDSP.getDongSanPham());
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

    // Tìm kiếm dòng sản phẩm theo keyword
    public List<DongSanPhamDTO> searchDongSanPham(String keyword) {
        return dongSanPhamRepository.searchByKeyword(keyword)
                .stream()
                .map(dsp -> new DongSanPhamDTO(dsp.getId(), dsp.getMa(), dsp.getDongSanPham()))
                .collect(Collectors.toList());
    }
}
