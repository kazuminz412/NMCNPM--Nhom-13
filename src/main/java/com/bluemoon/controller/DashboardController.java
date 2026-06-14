package com.bluemoon.controller;

import com.bluemoon.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(service.getDashboardStats());
    }

    @GetMapping("/charts")
    public ResponseEntity<?> getCharts() {
        return ResponseEntity.ok(service.getDashboardCharts());
    }

    @GetMapping("/activity")
    public ResponseEntity<?> getActivity() {
        return ResponseEntity.ok(service.getRecentActivities());
    }
}
