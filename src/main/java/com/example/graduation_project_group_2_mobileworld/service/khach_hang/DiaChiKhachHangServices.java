package com.example.graduation_project_group_2_mobileworld.service.khach_hang;

import com.example.graduation_project_group_2_mobileworld.dto.DiaChi.DiaChiDTO;
import com.example.graduation_project_group_2_mobileworld.dto.khach_hang.KhachHangDTO;
import com.example.graduation_project_group_2_mobileworld.entity.DiaChiKhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.TaiKhoan;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.KhachHangRepository;
import com.example.graduation_project_group_2_mobileworld.repository.khach_hang.diaChiKhachHangRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DiaChiKhachHangServices {
    private final diaChiKhachHangRepo diaChiRepo;
    private final KhachHangRepository khachHangRepository;


    public DiaChiKhachHangServices(diaChiKhachHangRepo diaChiRepo, KhachHangRepository khachHangRepository) {
        this.diaChiRepo = diaChiRepo;
        this.khachHangRepository = khachHangRepository;
    }

    public Optional<DiaChiKhachHang> findByIdKH(Integer id) {
        return diaChiRepo.findById(id);
    }

    private String MaDchi() {
        // Logic tạo mã tự động, ví dụ:
        return "DC" + System.currentTimeMillis();
    }

    public String MaDchi2() {
        Integer maxMa = diaChiRepo.findMaxMa();
        if (maxMa == null) {
            return "DCKH00001";
        }
        int nextNumber = maxMa + 1;
        return String.format("DCKH%05d", nextNumber);
    }

    public DiaChiKhachHang addDiaChi(DiaChiDTO khachHangDTO) {
        // Kiểm tra idKhachHang có tồn tại
        KhachHang khachHang = khachHangRepository.findById(khachHangDTO.getIdKhachHang())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại với ID: " + khachHangDTO.getIdKhachHang()));

        // Tạo đối tượng địa chỉ mới
        DiaChiKhachHang diaChiKhachHang = new DiaChiKhachHang();
        diaChiKhachHang.setIdKhachHang(khachHang); // Gán đối tượng KhachHang
        diaChiKhachHang.setMa(MaDchi2());
        diaChiKhachHang.setThanhPho(khachHangDTO.getThanhPho());
        diaChiKhachHang.setDiaChiCuThe(khachHangDTO.getDiaChiCuThe());
        diaChiKhachHang.setQuan(khachHangDTO.getQuan());
        diaChiKhachHang.setPhuong(khachHangDTO.getPhuong());

        // Lưu vào database
        return diaChiRepo.save(diaChiKhachHang);
    }
    public List<DiaChiKhachHang> getAllAddressesByKhachHangId(Integer idKhachHang) {
        return diaChiRepo.findAllByIdKhachHangId(idKhachHang);
    }

    public void deleteDiaChi(Integer id) throws Exception {
        Optional<DiaChiKhachHang> diaChiOptional = diaChiRepo.findById(id);
        if (diaChiOptional.isPresent()) {
            diaChiRepo.deleteById(id); // Xóa hoàn toàn
        } else {
            throw new Exception("Không tìm thấy địa chỉ với id: " + id);
        }
    }

    public DiaChiDTO updateDiaChi(Integer id, DiaChiDTO updatedDiaChiDTO) throws Exception {

        DiaChiKhachHang existingDchi = diaChiRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại với ID: " + id));

        // Cập nhật các trường từ DTO
        existingDchi.setDiaChiCuThe(updatedDiaChiDTO.getDiaChiCuThe());
        existingDchi.setPhuong(updatedDiaChiDTO.getPhuong());
        existingDchi.setQuan(updatedDiaChiDTO.getQuan());
        existingDchi.setThanhPho(updatedDiaChiDTO.getThanhPho());
        existingDchi.setMacDinh(updatedDiaChiDTO.isMacDinh());

        // Lưu địa chỉ đã cập nhật
        DiaChiKhachHang updatedDchi = diaChiRepo.save(existingDchi);

        // Chuyển đổi về DiaChiDTO để trả về
        DiaChiDTO resultDTO = new DiaChiDTO();
        resultDTO.setIdKhachHang(updatedDchi.getIdKhachHang() != null ? updatedDchi.getIdKhachHang().getId() : null);
        resultDTO.setThanhPho(updatedDchi.getThanhPho());
        resultDTO.setPhuong(updatedDchi.getPhuong());
        resultDTO.setQuan(updatedDchi.getQuan());
        resultDTO.setDiaChiCuThe(updatedDchi.getDiaChiCuThe());
        resultDTO.setMacDinh(updatedDchi.getMacDinh() != null ? updatedDchi.getMacDinh() : false);

        return resultDTO;
    }

    public void setMacDinh(Integer id, Boolean macDinh) {
        DiaChiKhachHang address = diaChiRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại với ID: " + id));

        if (macDinh) {
            // Tìm tất cả địa chỉ của khách hàng dựa trên idKhachHang của địa chỉ hiện tại
            List<DiaChiKhachHang> allAddresses = diaChiRepo.findByIdKhachHang(address.getIdKhachHang());
            for (DiaChiKhachHang addr : allAddresses) {
                if (!addr.getId().equals(id)) { // Không đặt lại cho địa chỉ hiện tại
                    addr.setMacDinh(false);
                }
            }
            diaChiRepo.saveAll(allAddresses); // Lưu thay đổi
        }

        // Cập nhật địa chỉ được chọn thành mặc định
        address.setMacDinh(macDinh);
        diaChiRepo.save(address);
    }

}
