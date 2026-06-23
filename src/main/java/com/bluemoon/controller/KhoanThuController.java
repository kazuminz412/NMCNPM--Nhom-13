package com.bluemoon.controller;

import com.bluemoon.dto.KhoanThuDTO;
import com.bluemoon.dto.KhoanThuRequest;
import com.bluemoon.model.DanhMucPhi;
import com.bluemoon.model.HoaDon;
import com.bluemoon.model.ChiTietHoaDon;
import com.bluemoon.model.HoDan;
import com.bluemoon.repository.DanhMucPhiRepository;
import com.bluemoon.repository.HoaDonRepository;
import com.bluemoon.repository.ChiTietHoaDonRepository;
import com.bluemoon.repository.HoDanRepository;
import com.bluemoon.repository.PhuongTienRepository;
import com.bluemoon.model.PhuongTien;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/khoan-thu")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KhoanThuController {

    private final JdbcTemplate jdbcTemplate;
    private final DanhMucPhiRepository danhMucPhiRepo;
    private final HoaDonRepository hoaDonRepo;
    private final ChiTietHoaDonRepository chiTietHoaDonRepo;
    private final HoDanRepository hoDanRepo;
    private final PhuongTienRepository phuongTienRepo;

    @GetMapping
    public ResponseEntity<?> getAllKhoanThu() {
        String sql = "SELECT " +
                     "  d.id as danhMucPhiId, " +
                     "  d.ten_phi as tenKhoanThu, " +
                     "  d.loai_phi as loai, " +
                     "  d.don_gia as soTien, " +
                     "  h.thang as thangApDung, " +
                     "  COUNT(DISTINCT h.ho_dan_id) as soHo " +
                     "FROM danh_muc_phi d " +
                     "JOIN chi_tiet_hoa_don ct ON d.id = ct.danh_muc_phi_id " +
                     "JOIN hoa_don h ON ct.hoa_don_id = h.id " +
                     "GROUP BY d.id, d.ten_phi, d.loai_phi, d.don_gia, h.thang " +
                     "ORDER BY h.thang DESC, d.ten_phi ASC";

        List<KhoanThuDTO> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            KhoanThuDTO dto = new KhoanThuDTO();
            dto.setDanhMucPhiId(rs.getLong("danhMucPhiId"));
            dto.setTenKhoanThu(rs.getString("tenKhoanThu"));
            dto.setLoai(rs.getString("loai"));
            dto.setSoTien(rs.getLong("soTien"));
            dto.setThangApDung(rs.getString("thangApDung"));
            dto.setSoHo(rs.getLong("soHo"));
            return dto;
        });
        
        return ResponseEntity.ok(list);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createKhoanThu(@RequestBody KhoanThuRequest request) {
        DanhMucPhi phi = danhMucPhiRepo.findById(request.getDanhMucPhiId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoản phí!"));

        List<HoDan> tatCaHoDan = hoDanRepo.findAll();
        
        for (HoDan hoDan : tatCaHoDan) {
            // Calculate soLuong FIRST
            Long soLuong = 0L;
            if ("m2".equals(phi.getDonViTinh())) {
                soLuong = (long) Math.round(hoDan.getDienTichM2() != null ? hoDan.getDienTichM2() : 0);
            } else if ("nguoi".equals(phi.getDonViTinh())) {
                soLuong = (long) (hoDan.getSoNhanKhau() != null ? hoDan.getSoNhanKhau() : 0);
            } else if ("o_to".equals(phi.getDonViTinh()) || "xe_may".equals(phi.getDonViTinh()) || "xe_dap_dien".equals(phi.getDonViTinh()) || "xe".equals(phi.getDonViTinh())) {
                long xeCount = 0;
                List<PhuongTien> phuongTiens = phuongTienRepo.findByHoDanId(hoDan.getId());
                for (PhuongTien pt : phuongTiens) {
                    String loai = pt.getLoaiXe() != null ? pt.getLoaiXe().toLowerCase() : "";
                    if ("o_to".equals(phi.getDonViTinh()) && (loai.equals("o_to") || loai.equals("oto"))) {
                        xeCount++;
                    } else if ("xe_may".equals(phi.getDonViTinh()) && (loai.equals("xe_may") || loai.equals("may"))) {
                        xeCount++;
                    } else if ("xe_dap_dien".equals(phi.getDonViTinh()) && loai.equals("xe_dap_dien")) {
                        xeCount++;
                    } else if ("xe".equals(phi.getDonViTinh())) {
                        xeCount++;
                    }
                }
                soLuong = xeCount;
            } else if ("ho_dan".equals(phi.getDonViTinh())) {
                soLuong = 1L; // Cố định 1 cho mỗi hộ dân
            } else {
                soLuong = 1L; // Fallback cho các loại phí khác
            }

            // DO NOT CREATE HoaDon/ChiTietHoaDon if soLuong is 0
            if (soLuong == 0) {
                continue;
            }

            // Find or create HoaDon for the specified month
            HoaDon hoaDon = hoaDonRepo.findByHoDanIdAndThang(hoDan.getId(), request.getThangApDung()).stream().findFirst().orElse(null);
            if (hoaDon == null) {
                hoaDon = new HoaDon();
                hoaDon.setHoDan(hoDan);
                hoaDon.setThang(request.getThangApDung());
                hoaDon.setTrangThai("chua_thanh_toan");
                hoaDon.setTongTien(0L);
                hoaDon = hoaDonRepo.save(hoaDon);
            }

            // Check if ChiTietHoaDon already exists for this fee
            final Long hdId = hoaDon.getId();
            boolean alreadyExists = chiTietHoaDonRepo.findByHoaDonId(hdId).stream()
                    .anyMatch(ct -> ct.getDanhMucPhi().getId().equals(phi.getId()));

            if (!alreadyExists) {
                ChiTietHoaDon chiTiet = new ChiTietHoaDon();
                chiTiet.setHoaDon(hoaDon);
                chiTiet.setDanhMucPhi(phi);
                
                chiTiet.setSoLuong(soLuong);
                chiTiet.setDonGia(phi.getDonGia());
                chiTiet.setThanhTien(soLuong * phi.getDonGia());
                
                chiTietHoaDonRepo.save(chiTiet);
                
                // Cập nhật tổng tiền hóa đơn
                hoaDon.setTongTien(hoaDon.getTongTien() + chiTiet.getThanhTien());
                hoaDonRepo.save(hoaDon);
            }
        }

        return ResponseEntity.ok("Tạo khoản thu thành công!");
    }

    @DeleteMapping("/{danhMucPhiId}/{thang}")
    @Transactional
    public ResponseEntity<?> deleteKhoanThu(@PathVariable Long danhMucPhiId, @PathVariable String thang) {
        String sql = "SELECT c.id FROM chi_tiet_hoa_don c JOIN hoa_don h ON c.hoa_don_id = h.id WHERE c.danh_muc_phi_id = ? AND h.thang = ?";
        List<Long> ctIds = jdbcTemplate.queryForList(sql, Long.class, danhMucPhiId, thang);
        
        for (Long ctId : ctIds) {
            ChiTietHoaDon ct = chiTietHoaDonRepo.findById(ctId).orElse(null);
            if (ct != null) {
                HoaDon hd = ct.getHoaDon();
                hd.setTongTien(hd.getTongTien() - ct.getThanhTien());
                chiTietHoaDonRepo.delete(ct);
                // Xóa cả hóa đơn nếu trống
                if (hd.getTongTien() <= 0 && chiTietHoaDonRepo.findByHoaDonId(hd.getId()).size() <= 1) {
                    hoaDonRepo.delete(hd);
                } else {
                    hoaDonRepo.save(hd);
                }
            }
        }
        return ResponseEntity.ok("Đã xóa khoản thu thành công!");
    }
}

