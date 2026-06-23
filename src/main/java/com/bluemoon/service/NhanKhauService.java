package com.bluemoon.service;

import com.bluemoon.model.HoDan;
import com.bluemoon.model.NhanKhau;
import com.bluemoon.repository.HoDanRepository;
import com.bluemoon.repository.NhanKhauRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NhanKhauService {
    
    private final NhanKhauRepository nhanKhauRepository;
    private final HoDanRepository hoDanRepository; 

    public List<NhanKhau> findAll() {
        return nhanKhauRepository.findAll();
    }

    // Thêm nhân khẩu mới
    public NhanKhau save(NhanKhau nhanKhau) {
        //  Kiểm tra trùng CCCD (Nếu người dùng có nhập CCCD)
        if (nhanKhau.getSoCCCD() != null && !nhanKhau.getSoCCCD().isEmpty()) {
            if (nhanKhauRepository.existsBySoCCCD(nhanKhau.getSoCCCD())) {
                throw new RuntimeException("Số CCCD này đã tồn tại trong hệ thống!");
            }
        }

        if (nhanKhau.getHoDanId() != null) {
            HoDan hoDan = hoDanRepository.findById(nhanKhau.getHoDanId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Hộ gia đình để thêm vào!"));
            nhanKhau.setHoDan(hoDan); // SỬA: hoDanh -> hoDan, setHoDanh -> setHoDan
        } else {
            throw new RuntimeException("Phải chọn Hộ gia đình cho nhân khẩu này!");
        }

        return nhanKhauRepository.save(nhanKhau);
    }

    public NhanKhau update(Long id, NhanKhau details) {
        NhanKhau existing = nhanKhauRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với ID: " + id));
        
        if (details.getSoCCCD() != null && !details.getSoCCCD().equals(existing.getSoCCCD())) {
            if (nhanKhauRepository.existsBySoCCCD(details.getSoCCCD())) {
                throw new RuntimeException("Số CCCD mới đã bị trùng với người khác!");
            }
        }

        existing.setHoTen(details.getHoTen());
        existing.setNgaySinh(details.getNgaySinh());
        existing.setGioiTinh(details.getGioiTinh());
        existing.setSoCCCD(details.getSoCCCD());
        existing.setSoDienThoai(details.getSoDienThoai());
        existing.setQuanHe(details.getQuanHe());
        existing.setTrangThai(details.getTrangThai());
        // ĐÃ XÓA: existing.setGhiChu() — Model NhanKhau không có trường ghiChu

        // SỬA: HoGiaDinh -> HoDan, hoGiaDinhRepository -> hoDanRepository, setHoGiaDinh -> setHoDan
        if (details.getHoDanId() != null && 
           (existing.getHoDan() == null || !existing.getHoDan().getId().equals(details.getHoDanId()))) {
            HoDan hoDanMoi = hoDanRepository.findById(details.getHoDanId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Hộ gia đình mới!"));
            existing.setHoDan(hoDanMoi);
        }
        
        return nhanKhauRepository.save(existing);
    }

    // Xóa nhân khẩu
    public void delete(Long id) {
        nhanKhauRepository.deleteById(id);
    }

    // Lấy danh sách nhân khẩu theo Hộ dân (Dành cho MeController - Góc Cư Dân)
    public List<NhanKhau> findByHoDanId(Long hoDanId) {
        return nhanKhauRepository.findByHoDanId(hoDanId);
    }
}
