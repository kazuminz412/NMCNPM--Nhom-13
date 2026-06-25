package com.bluemoon.controller;

import com.bluemoon.security.JwtUtils;
import com.bluemoon.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('CU_DAN')") // Khóa chặt: Chỉ Cư Dân mới được gọi API này
public class MeController {

    private final JwtUtils jwtUtils;
    private final HoDanService hoDanService; // Bổ sung Service này
    private final HoaDonService hoaDonService;
    private final PhuongTienService phuongTienService;
    private final NhanKhauService nhanKhauService;

    // Hàm nội bộ: Rút trích an toàn hoDanId từ Token
    private Long getHoDanId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            Long id = jwtUtils.extractHoDanId(jwt);
            if (id != null) return id;
        }
        throw new IllegalArgumentException("Truy cập bị từ chối: Không tìm thấy mã Hộ dân hợp lệ!");
    }

    // 1. LẤY THÔNG TIN GỐC CỦA HỘ DÂN
    @GetMapping("/ho-dan")
    public ResponseEntity<?> getThongTinNhaToi(HttpServletRequest request) {
        return ResponseEntity.ok(hoDanService.timTheoId(getHoDanId(request)));
    }

    // 2. LẤY DANH SÁCH NHÂN KHẨU
    @GetMapping("/nhan-khau")
    public ResponseEntity<?> getNhanKhauCuaToi(HttpServletRequest request) {
        return ResponseEntity.ok(nhanKhauService.findByHoDanId(getHoDanId(request)));
    }

    // 3. LẤY DANH SÁCH PHƯƠNG TIỆN
    @GetMapping("/phuong-tien")
    public ResponseEntity<?> getPhuongTienCuaToi(HttpServletRequest request) {
        return ResponseEntity.ok(phuongTienService.findByHoDanId(getHoDanId(request)));
    }

    // 4. LẤY LỊCH SỬ HÓA ĐƠN (Hỗ trợ lọc theo tháng)
    @GetMapping("/hoa-don")
    public ResponseEntity<?> getHoaDonCuaToi(
            HttpServletRequest request,
            @RequestParam(required = false) String thang) {
        
        Long hoDanId = getHoDanId(request);
        
        // Nếu Frontend gửi tháng lên thì lọc theo tháng, nếu không thì lấy toàn bộ
        if (thang != null && !thang.isEmpty()) {
            return ResponseEntity.ok(hoaDonService.timTheoHoDanVaThang(hoDanId, thang));
        }
        return ResponseEntity.ok(hoaDonService.findByHoDanId(hoDanId));
    }

    // Tự động tiêm các Repository cần thiết cho chức năng đóng góp tự nguyện
    private final com.bluemoon.repository.HoDanRepository hoDanRepo;
    private final com.bluemoon.repository.DanhMucPhiRepository danhMucPhiRepo;
    private final com.bluemoon.repository.HoaDonRepository hoaDonRepo;
    private final com.bluemoon.repository.ChiTietHoaDonRepository chiTietHoaDonRepo;
    private final com.bluemoon.repository.GiaoDichRepository giaoDichRepo;
    private final com.bluemoon.repository.NhatKyHoatDongRepository nhatKyRepo;

    // 5. ĐÓNG GÓP TỰ NGUYỆN
    @PostMapping("/tu-nguyen")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<?> dongGopTuNguyen(HttpServletRequest request, @RequestBody java.util.Map<String, Object> payload) {
        Long hoDanId = getHoDanId(request);
        Long danhMucPhiId = Long.valueOf(payload.get("danhMucPhiId").toString());
        Long soTienNop = Long.valueOf(payload.get("soTienNop").toString());
        Long soLuong = payload.containsKey("soLuong") ? Long.valueOf(payload.get("soLuong").toString()) : 1L;
        String ghiChu = payload.getOrDefault("ghiChu", "Cư dân đóng góp tự nguyện").toString();

        com.bluemoon.model.HoDan hd = hoDanRepo.findById(hoDanId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ dân"));
        com.bluemoon.model.DanhMucPhi dmp = danhMucPhiRepo.findById(danhMucPhiId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục phí"));

        if (!"tu_nguyen".equals(dmp.getLoaiPhi()) && !"dich_vu".equals(dmp.getLoaiPhi())) {
            return ResponseEntity.badRequest().body("Khoản này không được phép đăng ký/đóng góp tự do!");
        }

        // Lấy tháng hiện tại
        java.time.YearMonth currentMonth = java.time.YearMonth.now();
        String thang = currentMonth.toString(); // format "YYYY-MM"

        // Tìm hoặc tạo HoaDon
        com.bluemoon.model.HoaDon hdObj = hoaDonRepo.findAll().stream()
            .filter(h -> h.getHoDan().getId().equals(hoDanId) && h.getThang().equals(thang))
            .findFirst()
            .orElse(null);

        if (hdObj == null) {
            hdObj = new com.bluemoon.model.HoaDon();
            hdObj.setHoDan(hd);
            hdObj.setThang(thang);
            hdObj.setTrangThai("chua_thanh_toan");
            hdObj.setTongTien(0L);
            hdObj = hoaDonRepo.save(hdObj);
        }

        // Tạo ChiTietHoaDon
        com.bluemoon.model.ChiTietHoaDon ct = new com.bluemoon.model.ChiTietHoaDon();
        ct.setHoaDon(hdObj);
        ct.setDanhMucPhi(dmp);
        ct.setSoLuong(soLuong);
        if (dmp.getDonGia() != null && dmp.getDonGia() > 0) {
            ct.setDonGia(dmp.getDonGia());
        } else {
            ct.setDonGia(soTienNop / soLuong);
        }
        ct.setThanhTien(soTienNop);
        ct.setTrangThai("da_dong");
        chiTietHoaDonRepo.save(ct);
        
        if (hdObj.getChiTietList() == null) {
            hdObj.setChiTietList(new java.util.ArrayList<>());
        }
        hdObj.getChiTietList().add(ct);

        // Cập nhật tổng tiền hóa đơn
        hdObj.setTongTien(hdObj.getTongTien() + soTienNop);
        // Vì đây là khoản đóng góp, có thể hóa đơn vẫn còn khoản khác chưa đóng.
        // Chạy lại logic check xem tất cả đã đóng chưa.
        boolean allPaid = true;
        for (com.bluemoon.model.ChiTietHoaDon c : hdObj.getChiTietList()) {
            if (c.getDanhMucPhi() != null && "bat_buoc".equals(c.getDanhMucPhi().getLoaiPhi()) && "chua_dong".equals(c.getTrangThai())) {
                allPaid = false;
                break;
            }
        }
        hdObj.setTrangThai(allPaid ? "da_thanh_toan" : "mot_phan");
        hoaDonRepo.save(hdObj);

        // Tạo GiaoDich
        com.bluemoon.model.GiaoDich gd = new com.bluemoon.model.GiaoDich();
        gd.setMaGiaoDich("GD-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        gd.setHoaDon(hdObj);
        gd.setHoDan(hd);
        gd.setSoTien(soTienNop);
        gd.setPhuongThuc("VietQR");
        gd.setThoiGian(java.time.LocalDateTime.now());
        gd.setGhiChu(ghiChu);
        giaoDichRepo.save(gd);
        
        com.bluemoon.model.NhatKyHoatDong log = new com.bluemoon.model.NhatKyHoatDong();
        log.setNoiDung(String.format("Hộ <b>%s</b> đã tự nguyện đóng góp <b>%s</b> (%,d đ)", hd.getTenChuHo() != null ? hd.getTenChuHo() : hd.getSoPhong(), dmp.getTenPhi(), soTienNop));
        log.setMauSac("#1ABC9C"); 
        log.setThoiGian(java.time.LocalDateTime.now());
        nhatKyRepo.save(log);

        return ResponseEntity.ok("Ghi nhận đóng góp thành công!");
    }
}
