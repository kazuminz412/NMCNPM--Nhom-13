package com.bluemoon.controller;

import com.bluemoon.dto.ThuPhiDTO;
import com.bluemoon.model.*;
import com.bluemoon.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/thu-phi")
@RequiredArgsConstructor
public class ThuPhiController {

    private final ChiTietHoaDonRepository chiTietHoaDonRepo;
    private final HoaDonRepository hoaDonRepo;
    private final HoDanRepository hoDanRepo;
    private final DanhMucPhiRepository danhMucPhiRepo;
    private final GiaoDichRepository giaoDichRepo;
    private final PhuongTienRepository phuongTienRepo;
    private final NhatKyHoatDongRepository nhatKyRepo;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('KE_TOAN')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<ThuPhiDTO>> getAllThuPhi() {
        List<ChiTietHoaDon> list = chiTietHoaDonRepo.findAll();
        List<ThuPhiDTO> result = new ArrayList<>();
        
        for (ChiTietHoaDon ct : list) {
            DanhMucPhi dmp = ct.getDanhMucPhi();
            String rawTrangThai = ct.getTrangThai();
            String trangThai = (rawTrangThai == null || rawTrangThai.trim().isEmpty() || rawTrangThai.equals("null")) ? "chua_dong" : rawTrangThai;
            
            // Theo yêu cầu: Phần thu phí chưa đóng chỉ hiện những dữ liệu bắt buộc nộp
            if (dmp.getLoaiPhi() != null && !dmp.getLoaiPhi().toLowerCase().contains("bat_buoc") && "chua_dong".equals(trangThai)) {
                continue;
            }
            
            HoDan hd = ct.getHoaDon().getHoDan();
            
            ThuPhiDTO dto = new ThuPhiDTO();
            dto.setId(ct.getId());
            dto.setHoDan(hd.getTenChuHo() + " – " + hd.getSoPhong());
            dto.setKhoanThu(dmp.getTenPhi() + " T" + ct.getHoaDon().getThang().replace("-", "/").substring(5) + "/" + ct.getHoaDon().getThang().substring(0,4));
            dto.setSoTien(ct.getThanhTien());
            
            // Han nop tam tinh la cuoi thang
            String thang = ct.getHoaDon().getThang();
            try {
                LocalDate date = LocalDate.parse(thang + "-01");
                dto.setHanNop(date.plusMonths(1).minusDays(1).toString());
            } catch(Exception e) {
                dto.setHanNop(thang + "-31");
            }
            
            dto.setTrangThai(trangThai);
            dto.setHoDanId(hd.getId());
            
            result.add(dto);
        }
        
        // Sort: chua_dong first
        result.sort((a, b) -> {
            if (a.getTrangThai().equals(b.getTrangThai())) {
                return b.getId().compareTo(a.getId());
            }
            return a.getTrangThai().equals("chua_dong") ? -1 : 1;
        });
        
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('KE_TOAN')")
    @PostMapping("/{id}")
    @Transactional
    public ResponseEntity<?> payFee(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        ChiTietHoaDon ct = chiTietHoaDonRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy khoản phí"));
            
        if ("da_dong".equals(ct.getTrangThai())) {
            return ResponseEntity.badRequest().body("Khoản này đã được thu!");
        }
        
        Long soTienNop = Long.valueOf(body.get("soTienNop").toString());
        String ghiChu = body.getOrDefault("ghiChu", "").toString();
        
        // Update status
        ct.setTrangThai("da_dong");
        chiTietHoaDonRepo.save(ct);
        
        // Create GiaoDich
        GiaoDich gd = new GiaoDich();
        gd.setMaGiaoDich("GD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        gd.setHoaDon(ct.getHoaDon());
        gd.setHoDan(ct.getHoaDon().getHoDan());
        gd.setSoTien(soTienNop);
        gd.setPhuongThuc("Tien Mat"); // Default
        gd.setThoiGian(LocalDateTime.now());
        gd.setGhiChu(ghiChu);
        giaoDichRepo.save(gd);
        
        NhatKyHoatDong log = new NhatKyHoatDong();
        log.setNoiDung(String.format("Hộ <b>%s</b> đã nộp khoản <b>%s</b> (%,d đ)", ct.getHoaDon().getHoDan().getTenChuHo(), ct.getDanhMucPhi().getTenPhi(), soTienNop));
        log.setMauSac("#1E8449"); 
        log.setThoiGian(LocalDateTime.now());
        nhatKyRepo.save(log);
        
        // Check if HoaDon is fully paid
        HoaDon hd = ct.getHoaDon();
        boolean allPaid = true;
        for (ChiTietHoaDon c : hd.getChiTietList()) {
            // Không xét các quỹ tình nguyện/dịch vụ không bắt buộc
            if (c.getDanhMucPhi().getLoaiPhi() != null && !c.getDanhMucPhi().getLoaiPhi().toLowerCase().contains("bat_buoc")) {
                continue;
            }
            if ("chua_dong".equals(c.getTrangThai())) {
                allPaid = false;
                break;
            }
        }
        if (allPaid) {
            hd.setTrangThai("da_thanh_toan");
        } else {
            hd.setTrangThai("mot_phan");
        }
        hoaDonRepo.save(hd);
        
        return ResponseEntity.ok("Ghi nhận thu phí thành công!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteFee(@PathVariable Long id) {
        ChiTietHoaDon ct = chiTietHoaDonRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy khoản phí"));
            
        if ("da_dong".equals(ct.getTrangThai())) {
            return ResponseEntity.badRequest().body("Không thể xóa khoản phí đã đóng!");
        }
        
        HoaDon hd = ct.getHoaDon();
        hd.setTongTien(hd.getTongTien() - ct.getThanhTien());
        hoaDonRepo.save(hd);
        
        chiTietHoaDonRepo.delete(ct);
        return ResponseEntity.ok("Xóa thành công!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/tao-moi")
    @Transactional
    public ResponseEntity<?> createFee(@RequestBody Map<String, Object> payload) {
        Long hoDanId = Long.valueOf(payload.get("hoDanId").toString());
        Long danhMucPhiId = Long.valueOf(payload.get("danhMucPhiId").toString());
        String thang = payload.get("thang").toString();
        
        HoDan hd = hoDanRepo.findById(hoDanId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ dân"));
        DanhMucPhi dmp = danhMucPhiRepo.findById(danhMucPhiId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục phí"));
            
        // Find or create HoaDon
        HoaDon hdObj = hoaDonRepo.findAll().stream()
            .filter(h -> h.getHoDan().getId().equals(hoDanId) && h.getThang().equals(thang))
            .findFirst()
            .orElse(null);
            
        if (hdObj == null) {
            hdObj = new HoaDon();
            hdObj.setHoDan(hd);
            hdObj.setThang(thang);
            hdObj.setTrangThai("chua_thanh_toan");
            hdObj.setTongTien(0L);
            hdObj = hoaDonRepo.save(hdObj);
        }
        
        // Check for duplicates
        boolean exists = hdObj.getChiTietList().stream()
            .anyMatch(c -> c.getDanhMucPhi().getId().equals(danhMucPhiId));
        if (exists) {
            return ResponseEntity.badRequest().body("Khoản phí này đã được thêm vào hóa đơn của tháng!");
        }
        
        ChiTietHoaDon ct = new ChiTietHoaDon();
        ct.setHoaDon(hdObj);
        ct.setDanhMucPhi(dmp);
        
        Long soLuong = 1L;
        if (dmp.getLoaiPhi().equals("o_to") || dmp.getLoaiPhi().equals("xe_may") || dmp.getLoaiPhi().equals("xe_dap_dien")) {
            soLuong = (long) phuongTienRepo.findByHoDanId(hd.getId()).stream().filter(p -> p.getLoaiXe().equals(dmp.getLoaiPhi())).count();
        } else if (dmp.getLoaiPhi().equals("dien_tich")) {
            soLuong = hd.getDienTichM2() != null ? hd.getDienTichM2().longValue() : 1L;
        }
        if (soLuong == 0) soLuong = 1L; // To add at least something if manually added
        
        ct.setSoLuong(soLuong);
        ct.setDonGia(dmp.getDonGia());
        ct.setThanhTien(soLuong * dmp.getDonGia());
        if (dmp.getLoaiPhi().equals("tu_nguyen")) {
            ct.setTrangThai("da_dong");
        } else {
            ct.setTrangThai("chua_dong");
        }
        chiTietHoaDonRepo.save(ct);
        
        hdObj.setTongTien(hdObj.getTongTien() + ct.getThanhTien());
        
        // Check if HoaDon is fully paid
        boolean allPaid = true;
        for (ChiTietHoaDon c : hdObj.getChiTietList()) {
            // Không xét các quỹ tình nguyện/dịch vụ không bắt buộc
            if (c.getDanhMucPhi().getLoaiPhi() != null && !c.getDanhMucPhi().getLoaiPhi().toLowerCase().contains("bat_buoc")) {
                continue;
            }
            if ("chua_dong".equals(c.getTrangThai())) {
                allPaid = false;
                break;
            }
        }
        hdObj.setTrangThai(allPaid ? "da_thanh_toan" : "chua_thanh_toan");
        hoaDonRepo.save(hdObj);
        
        // Nếu là tự nguyện thì tự động sinh giao dịch luôn
        if (dmp.getLoaiPhi().equals("tu_nguyen")) {
            GiaoDich gd = new GiaoDich();
            gd.setMaGiaoDich("GD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            gd.setHoaDon(hdObj);
            gd.setHoDan(hd);
            gd.setSoTien(ct.getThanhTien());
            gd.setPhuongThuc("Tien Mat");
            gd.setThoiGian(LocalDateTime.now());
            gd.setGhiChu("Đóng phí tự nguyện");
            giaoDichRepo.save(gd);
        }
        
        return ResponseEntity.ok("Tạo khoản thu thủ công thành công!");
    }
}
