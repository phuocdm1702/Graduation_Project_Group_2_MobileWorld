package com.example.graduation_project_group_2_mobileworld.controller.hoa_don;

import com.example.graduation_project_group_2_mobileworld.dto.hoa_don.HoaDonDTO;
import com.example.graduation_project_group_2_mobileworld.entity.HoaDon;
import com.example.graduation_project_group_2_mobileworld.service.hoa_don.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/hoa-don")
public class HoaDonController {

    private HoaDonService hoaDonService;

    public HoaDonController(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }


//    @GetMapping("/home")
//    public ResponseEntity<Page<HoaDonDTO>> getAllHoaDon(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(hoaDonService.getHoaDonWithPagination(page, size));
//    }

    @GetMapping("/home")
    public ResponseEntity<Page<HoaDonDTO>> getAllHoaDon(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String loaiDon,
            @RequestParam(required = false) Long minAmount,
            @RequestParam(required = false) Long maxAmount,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(hoaDonService.getHoaDonWithFilters(page, size, keyword, loaiDon, minAmount, maxAmount, startDate, endDate));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<HoaDonDTO> getFullHoaDonDetail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(hoaDonService.getFullHoaDonDetails(id));
    }

    @GetMapping("/print/{id}")
    public ResponseEntity<byte[]> printHoaDon(@PathVariable("id") Integer id) {
        try {
            byte[] pdfBytes = hoaDonService.generateHoaDonPdf(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "hoa_don_" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            // Convert the error message to byte[]
            String errorMessage = "Không thể tạo PDF: " + e.getMessage();
            byte[] errorBytes = errorMessage.getBytes();

            // Set headers for the error response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN); // Use text/plain for error message
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(errorBytes, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

