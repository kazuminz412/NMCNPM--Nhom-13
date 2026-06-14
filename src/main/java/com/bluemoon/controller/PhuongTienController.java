package com.bluemoon.controller;

import com.bluemoon.model.PhuongTien;
import com.bluemoon.service.PhuongTienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phuong-tien")
public class PhuongTienController {

    @Autowired
    private PhuongTienService phuongTienService;

    @PostMapping("/dang-ky/{hoDanId}")
    public ResponseEntity<?> dangKyXe(@RequestBody PhuongTien phuongTien, @PathVariable Long hoDanId) {
        try {
            PhuongTien xeMoi = phuongTienService.dangKyXe(phuongTien, hoDanId);
            return ResponseEntity.ok(xeMoi);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
