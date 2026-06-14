package com.bluemoon.service;

import com.bluemoon.dto.ActivityResponse;
import com.bluemoon.dto.ChartResponse;
import com.bluemoon.dto.StatsResponse;
import com.bluemoon.model.NhatKyHoatDong;
import com.bluemoon.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final HoDanRepository hoDanRepo;
    private final NhanKhauRepository nhanKhauRepo;
    private final PhuongTienRepository phuongTienRepo;
    private final HoaDonRepository hoaDonRepo;
    private final NhatKyHoatDongRepository nhatKyRepo;

    // 1. Logic lấy Thống Kê Tổng (Đã sửa lỗi trùng biến)
    public StatsResponse getDashboardStats() {
        String thangHienTai = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        long tongHoDan = hoDanRepo.count();
        long tongNhanKhau = nhanKhauRepo.count();
        long tongXe = phuongTienRepo.count();

        // Lấy Tổng thu thực tế từ Database
        long thuThang = hoaDonRepo.sumDoanhThuByThang(thangHienTai);

        // Tính tỷ lệ % hoàn thành
        long soDaThu = hoaDonRepo.countHoaDonDaThuByThang(thangHienTai);
        long tongSoHoaDon = hoaDonRepo.countTotalHoaDonByThang(thangHienTai);

        double tyLe = 0.0;
        if (tongSoHoaDon > 0) {
            tyLe = Math.round((soDaThu * 100.0) / tongSoHoaDon); 
        }

        return StatsResponse.builder()
                .hodan(tongHoDan)
                .nhankhau(tongNhanKhau)
                .xe(tongXe)
                .thuThang(thuThang)
                .tyLeDaThu(tyLe)
                .build();
    }

    // 2. Logic tính toán Biểu Đồ (ĐÃ NÂNG CẤP LÊN SỐ THẬT 100%)
    public ChartResponse getDashboardCharts() {
        List<String> labels = new ArrayList<>();
        List<Double> doanhThu = new ArrayList<>();
        
        // Vòng lặp lấy 6 tháng gần nhất để vẽ biểu đồ Cột
        for (int i = 5; i >= 0; i--) {
            YearMonth ym = YearMonth.now().minusMonths(i);
            String thangStr = ym.format(DateTimeFormatter.ofPattern("yyyy-MM")); // Format "yyyy-MM" để query
            labels.add("T" + ym.getMonthValue());
            
            // Query số thật và chia cho 1 triệu (Vì Frontend đang hiển thị đơn vị "triệu đồng")
            Long tongThuThang = hoaDonRepo.sumDoanhThuByThang(thangStr);
            doanhThu.add(tongThuThang / 1_000_000.0); 
        }

        // Tính số liệu cho biểu đồ Tròn (Tháng hiện tại)
        String thangHienTai = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        long soDaThu = hoaDonRepo.countHoaDonDaThuByThang(thangHienTai);
        long tongSoHoaDon = hoaDonRepo.countTotalHoaDonByThang(thangHienTai);
        double tyLe = 0.0;
        if (tongSoHoaDon > 0) {
            tyLe = Math.round((soDaThu * 100.0) / tongSoHoaDon);
        }

        return ChartResponse.builder()
                .labels(labels)
                .doanhThu(doanhThu)
                .tyLeDaThu(tyLe) 
                .build();
    }

    // 3. Logic chuyển đổi Thời Gian cho Nhật Ký
    public List<ActivityResponse> getRecentActivities() {
        List<NhatKyHoatDong> logs = nhatKyRepo.findTop5ByOrderByThoiGianDesc();
        LocalDateTime now = LocalDateTime.now();

        return logs.stream().map(log -> {
            Duration duration = Duration.between(log.getThoiGian(), now);
            String timeAgo;
            
            if (duration.toMinutes() < 60) {
                timeAgo = duration.toMinutes() + " phút trước";
            } else if (duration.toHours() < 24) {
                timeAgo = duration.toHours() + " giờ trước";
            } else {
                timeAgo = duration.toDays() + " ngày trước";
            }

            return ActivityResponse.builder()
                    .dot(log.getMauSac())
                    .txt(log.getNoiDung())
                    .time(timeAgo)
                    .build();
        }).collect(Collectors.toList());
    }
}
