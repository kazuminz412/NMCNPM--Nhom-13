package com.bluemoon.service;

import com.bluemoon.model.HoDan;
import com.bluemoon.repository.HoDanRepository;
import com.bluemoon.repository.NhatKyHoatDongRepository;
import com.bluemoon.model.NhatKyHoatDong;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HoDanService {
    
    private final HoDanRepository repo;
    private final NhatKyHoatDongRepository nhatKyRepo;

    public List<HoDan> getAll() { 
        return repo.findAll(); 
    }

    public HoDan create(HoDan ho) {
        if (ho.getMaHoKhau() == null || ho.getMaHoKhau().trim().isEmpty()) {
            ho.setMaHoKhau("HK" + System.currentTimeMillis());
        } else if (repo.existsByMaHoKhau(ho.getMaHoKhau())) {
            throw new RuntimeException("Mã hộ khẩu đã tồn tại!");
        }
        HoDan savedHo = repo.save(ho);
        
        NhatKyHoatDong log = new NhatKyHoatDong();
        log.setNoiDung(String.format("Hộ <b>%s</b> vừa được thêm mới vào hệ thống", savedHo.getTenChuHo() != null ? savedHo.getTenChuHo() : "Trống"));
        log.setMauSac("#2E86C1"); 
        log.setThoiGian(LocalDateTime.now());
        nhatKyRepo.save(log);
        
        return savedHo;
    }

    public HoDan update(Long id, HoDan data) {
        //Tìm hộ dan trong DB
        HoDan existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ gia đình!"));

        //  Nếu mã hộ khẩu bị thay đổi, phải kiểm tra xem mã mới có bị trùng với ai khác không
        if (data.getMaHoKhau() != null && !java.util.Objects.equals(existing.getMaHoKhau(), data.getMaHoKhau()) && repo.existsByMaHoKhau(data.getMaHoKhau())) {
            throw new RuntimeException("Mã hộ khẩu mới này đã tồn tại trong hệ thống!");
        }

        // Cập nhật TOÀN BỘ các trường thông tin (khớp với file Model đã chốt)
        if (data.getMaHoKhau() != null && !data.getMaHoKhau().trim().isEmpty()) {
            if (data.getMaHoKhau() != null && !data.getMaHoKhau().trim().isEmpty()) {
            existing.setMaHoKhau(data.getMaHoKhau());
        }
        }
        existing.setTenChuHo(data.getTenChuHo()); 
        existing.setSoPhong(data.getSoPhong());
        existing.setToaNha(data.getToaNha());
        existing.setDienTichM2(data.getDienTichM2());
        existing.setSoNhanKhau(data.getSoNhanKhau());
        existing.setNgayCapHoKhau(data.getNgayCapHoKhau());
        existing.setSoDienThoai(data.getSoDienThoai());
        existing.setTrangThai(data.getTrangThai());

        return repo.save(existing);
    }

    public void delete(Long id) { 
        HoDan ho = repo.findById(id).orElse(null);
        if (ho != null) {
            repo.deleteById(id);
            
            NhatKyHoatDong log = new NhatKyHoatDong();
            log.setNoiDung(String.format("Hộ <b>%s</b> vừa bị xóa khỏi hệ thống", ho.getTenChuHo() != null ? ho.getTenChuHo() : "Trống"));
            log.setMauSac("#E74C3C"); 
            log.setThoiGian(LocalDateTime.now());
            nhatKyRepo.save(log);
        }
    }

    // Tìm Hộ dân theo ID (Dành cho MeController lấy thông tin nhà)
    public HoDan timTheoId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Hộ gia đình có ID: " + id));
    }
}
