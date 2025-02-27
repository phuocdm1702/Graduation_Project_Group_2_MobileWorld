package com.example.graduation_project_group_2_mobileworld.controller.nhan_vien;

import com.example.graduation_project_group_2_mobileworld.entity.NhanVien;
import com.example.graduation_project_group_2_mobileworld.service.nhan_vien.NhanVienServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:8080"})
@RequestMapping("/nhan-vien")
public class NhanVienController {
    private final NhanVienServices nhanVienServices;


    public NhanVienController(NhanVienServices nhanVienServices) {
        this.nhanVienServices = nhanVienServices;
    }

    //Lay tat ca du lieu
    @GetMapping("/home")
    public List<NhanVien> getAllnv() {
        List<NhanVien> nv = nhanVienServices.getall();
        System.out.println("Danh sách nv: " + nv);
        return nv;
    }
    @PostMapping("/add")
    public NhanVien addNhanVien(@RequestBody NhanVien nhanVien){
        return nhanVienServices.add(nhanVien);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id){
       return nhanVienServices.delete(id);
    }
}
