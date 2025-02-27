<<<<<<<< HEAD:src/main/java/com/example/graduation_project_group_2_mobileworld/controller/PhieuGiamGia/PhieuGiamGiaController.java
package com.example.graduation_project_group_2_mobileworld.controller.PhieuGiamGia;
========
package com.example.graduation_project_group_2_mobileworld.controller.phieu_giam_gia;
>>>>>>>> 6d62e844a95aff6ea5e022df6ece1d09f0503cd0:src/main/java/com/example/graduation_project_group_2_mobileworld/controller/phieu_giam_gia/PhieuGiamGiaController.java

import com.example.graduation_project_group_2_mobileworld.dto.phieuGiamGiaDTO.PhieuGiamGiaDTO;
import com.example.graduation_project_group_2_mobileworld.entity.KhachHang;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
<<<<<<<< HEAD:src/main/java/com/example/graduation_project_group_2_mobileworld/controller/PhieuGiamGia/PhieuGiamGiaController.java
import com.example.graduation_project_group_2_mobileworld.service.khach_hang_service.KhachHangService;
import com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia.PhieuGiamGiaService;
========
import com.example.graduation_project_group_2_mobileworld.service.khach_hang.KhachHangServices;
import com.example.graduation_project_group_2_mobileworld.service.phieu_giam_gia.PhieuGiamGiaService;
>>>>>>>> 6d62e844a95aff6ea5e022df6ece1d09f0503cd0:src/main/java/com/example/graduation_project_group_2_mobileworld/controller/phieu_giam_gia/PhieuGiamGiaController.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/phieu-giam-gia")
@RestController
@CrossOrigin("*")
public class PhieuGiamGiaController {

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

<<<<<<<< HEAD:src/main/java/com/example/graduation_project_group_2_mobileworld/controller/PhieuGiamGia/PhieuGiamGiaController.java
    @GetMapping("/data-pgg")
========
    @Autowired
    private KhachHangServices khachHangService;

    @GetMapping("/data")
>>>>>>>> 6d62e844a95aff6ea5e022df6ece1d09f0503cd0:src/main/java/com/example/graduation_project_group_2_mobileworld/controller/phieu_giam_gia/PhieuGiamGiaController.java
    public List<PhieuGiamGia> fetchData() {
        List<PhieuGiamGia> listPGG = phieuGiamGiaService.getPGG();
        return listPGG;
    }



}
