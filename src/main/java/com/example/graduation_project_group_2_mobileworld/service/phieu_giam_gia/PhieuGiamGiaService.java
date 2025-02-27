<<<<<<<< HEAD:src/main/java/com/example/graduation_project_group_2_mobileworld/service/PhieuGiamGia/PhieuGiamGiaService.java
package com.example.graduation_project_group_2_mobileworld.service.PhieuGiamGia;
========
package com.example.graduation_project_group_2_mobileworld.service.phieu_giam_gia;
>>>>>>>> 6d62e844a95aff6ea5e022df6ece1d09f0503cd0:src/main/java/com/example/graduation_project_group_2_mobileworld/service/phieu_giam_gia/PhieuGiamGiaService.java

import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGia;
import com.example.graduation_project_group_2_mobileworld.entity.PhieuGiamGiaCaNhan;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaCaNhanRepository;
import com.example.graduation_project_group_2_mobileworld.repository.giam_gia.PhieuGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhieuGiamGiaService {

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;


    public PhieuGiamGiaService(PhieuGiamGiaRepository phieuGiamGiaRepository) {
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
    }

    public List<PhieuGiamGia> getPGG() {
        return phieuGiamGiaRepository.findAll();
    }

    public PhieuGiamGia addPGG(PhieuGiamGia phieuGiamGia) {
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }


}
