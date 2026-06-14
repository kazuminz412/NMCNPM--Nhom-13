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

    // 1. Logic lấy Thống Kê Tổng
    public StatsResponse getDashboardStats() {
        String thangHienTai = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        long tongHoDan = hoDanRepo.count();
        long tongNhanKhau = nhanKhauRepo.count(); // Giả sử đã có bảng NhanKhau
        long tongXe = phuongTienRepo.count();

        // Chú ý: Backend cần tự viết thêm 2 hàm @Query trong HoaDonRepository:
        // - sumDoanhThuByThang(String thang) -> Tổng tiền những hóa đơn "da_thanh_toan"
        // - countHoaDonDaThuByThang(String thang) / countHoaDonByThang(String thang)
        
        // Mock logic (thay bằng hàm gọi Repository thực tế):
        long thuThang = 98500000L; // hoaDonRepo.sumDoanhThuByThang(thangHienTai)
        double tyLe = 76.0;        // (hoaDonRepo.countHoaDonDaThuByThang(thangHienTai) / tongHoHoaDon) * 100

        return StatsResponse.builder()
                .hodan(tongHoDan)
                .nhankhau(tongNhanKhau)
                .xe(tongXe)
                .thuThang(thuThang)
                .tyLeDaThu(tyLe)
                .build();
    }

    // 2. Logic tính toán Biểu Đồ
    public ChartResponse getDashboardCharts() {
        List<String> labels = new ArrayList<>();
        List<Double> doanhThu = new ArrayList<>();
        
        // Vòng lặp lấy 6 tháng gần nhất
        for (int i = 5; i >= 0; i--) {
            YearMonth ym = YearMonth.now().minusMonths(i);
            labels.add("T" + ym.getMonthValue());
            
            // Ở dự án thực tế: doanhThu.add(hoaDonRepo.sumDoanhThuByThang(ym.toString()) / 1_000_000.0);
            // Fix cứng số liệu tạm thời để giao diện chạy:
            doanhThu.add(Math.round(Math.random() * 20 + 70) * 1.0); 
        }

        return ChartResponse.builder()
                .labels(labels)
                .doanhThu(doanhThu)
                .tyLeDaThu(76.0) // Lấy từ getDashboardStats()
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
