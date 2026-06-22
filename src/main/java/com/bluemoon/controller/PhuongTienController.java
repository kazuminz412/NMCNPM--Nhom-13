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

    @Autowired
    private com.bluemoon.repository.PhuongTienRepository phuongTienRepository;

    @Autowired
    private com.bluemoon.repository.HoDanRepository hoDanRepository;

    @GetMapping
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(phuongTienRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return phuongTienRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/dang-ky/{hoDanId}")
    public ResponseEntity<?> dangKyXe(@RequestBody PhuongTien phuongTien, @PathVariable Long hoDanId) {
        try {
            PhuongTien xeMoi = phuongTienService.dangKyXe(phuongTien, hoDanId);
            return ResponseEntity.ok(xeMoi);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateXe(@PathVariable Long id, @RequestBody PhuongTien updated) {
        return phuongTienRepository.findById(id).map(xe -> {
            xe.setLoaiXe(updated.getLoaiXe());
            xe.setBienSo(updated.getBienSo());
            xe.setHangXe(updated.getHangXe());
            xe.setMauXe(updated.getMauXe());
            xe.setGhiChu(updated.getGhiChu());
            // Update HoDan if needed
            if (updated.getHoDan() != null && updated.getHoDan().getId() != null) {
                com.bluemoon.model.HoDan hoDan = hoDanRepository.findById(updated.getHoDan().getId()).orElse(null);
                if (hoDan != null) {
                    xe.setHoDan(hoDan);
                }
            }
            phuongTienRepository.save(xe);
            return ResponseEntity.ok(xe);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteXe(@PathVariable Long id) {
        if (!phuongTienRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        phuongTienRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
