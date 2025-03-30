package com.example.graduation_project_group_2_mobileworld.controller.san_pham;

import com.example.graduation_project_group_2_mobileworld.dto.san_pham.ImelDaBanDTO;
import com.example.graduation_project_group_2_mobileworld.service.san_pham.ImelDaBanService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/imel-da-ban")
@CrossOrigin(origins = "http://localhost:3000")
public class ImelDaBanController {

    private final ImelDaBanService service;

    public ImelDaBanController(ImelDaBanService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ImelDaBanDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.getAllImelDaBan(page, size));
    }
}