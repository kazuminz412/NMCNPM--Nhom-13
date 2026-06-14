package com.bluemoon.controller;

import com.bluemoon.service.ThongKeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/thong-ke")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('KE_TOAN')") 
public class ThongKeController {

    // Gọi duy nhất tầng Service, tuyệt đối không gọi Repository ở đây nữa
    private final ThongKeService thongKeService;

    @GetMapping("/doanh-thu")
    public ResponseEntity<?> getDoanhThu() {
        return ResponseEntity.ok(thongKeService.getThongKeDoanhThu());
    }

    @GetMapping("/cong-no")
    public ResponseEntity<?> getCongNo() {
        return ResponseEntity.ok(thongKeService.getThongKeCongNo());
    }

    @GetMapping("/dan-cu")
    public ResponseEntity<?> getDanCu() {
        return ResponseEntity.ok(thongKeService.getThongKeDanCu());
    }
}
