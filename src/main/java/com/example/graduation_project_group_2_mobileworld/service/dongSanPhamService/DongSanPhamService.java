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
        List<DongSanPham> entityList = dongSanPhamRepository.findByDeletedFalse();
        return entityList.stream()
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
        Optional<DongSanPham> existingDSP = dongSanPhamRepository.findById(id);

        if (existingDSP.isPresent()) {
            DongSanPham dsp = existingDSP.get();
            dsp.setMa(dto.getMa());
            dsp.setDongSanPham(dto.getDongSanPham());

            DongSanPham updatedDSP = dongSanPhamRepository.save(dsp);
            return new DongSanPhamDTO(updatedDSP.getId(), updatedDSP.getMa(), updatedDSP.getDongSanPham());
        }
        throw new RuntimeException("Dòng sản phẩm không tồn tại!");
    }

    // Xóa dòng sản phẩm
    public void deleteDongSanPham(Integer id) {
        Optional<DongSanPham> existingDSP = dongSanPhamRepository.findById(id);
        if (existingDSP.isPresent()) {
            DongSanPham dsp = existingDSP.get();
            dsp.setDeleted(true); // Đánh dấu là đã xóa
            dongSanPhamRepository.save(dsp);
        } else {
            throw new RuntimeException("Dòng sản phẩm không tồn tại!");
        }
    }

}
