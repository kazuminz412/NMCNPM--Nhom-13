package com.bluemoon.controller;

import com.bluemoon.model.DanhMucPhi;
import com.bluemoon.service.DanhMucPhiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/danh-muc-phi") 
@RequiredArgsConstructor
public class DanhMucPhiController {

    private final DanhMucPhiService service;

    // 1. Thêm mới
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DanhMucPhi danhMucPhi) {
        return ResponseEntity.ok(service.save(danhMucPhi));
    }

    // 2. Lấy danh sách
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // 3. Sửa khoản phí 
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody DanhMucPhi danhMucPhiMoi) {
        // Backend cần viết hàm update() trong DanhMucPhiService
        DanhMucPhi updated = service.update(id, danhMucPhiMoi); 
        return ResponseEntity.ok(updated);
    }

    // 4. Xóa khoản phí (Dành cho nút 🗑 Xóa)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        // Backend cần viết hàm delete() trong DanhMucPhiService
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công!");
    }
}
