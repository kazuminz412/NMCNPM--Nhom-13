package com.bluemoon.controller;

import com.bluemoon.model.HoDan;
import com.bluemoon.service.HoDanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ho-dan") 
@RequiredArgsConstructor
public class HoDanController {
    
    private final HoDanService service;

    @GetMapping
    public ResponseEntity<?> getAll() { 
        return ResponseEntity.ok(service.getAll()); 
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody HoDan ho) {
        try {
            return ResponseEntity.ok(service.create(ho));
        } catch (RuntimeException e) {
            // Trả về lỗi 400 (Bad Request) dạng JSON để Frontend dễ đọc
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody HoDan ho) {
        try {
            return ResponseEntity.ok(service.update(id, ho));
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            // Đóng gói thông báo vào JSON để Frontend parse không bị lỗi
            Map<String, String> response = new HashMap<>();
            response.put("message", "Đã xóa hộ gia đình!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            // Xử lý luôn cái nguy cơ xóa nhà đang có hóa đơn mà lúc nãy mình nhắc
            response.put("message", "Không thể xóa hộ dân này (có thể do đang có dữ liệu liên quan)!");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
