package com.bluemoon.service;

import com.bluemoon.model.PhuongTien;
import com.bluemoon.model.HoDan;
import com.bluemoon.repository.PhuongTienRepository;
import com.bluemoon.repository.HoDanRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Thêm thư viện này

@Service
public class PhuongTienService {

    @Autowired
    private PhuongTienRepository phuongTienRepository;

    @Autowired
    private HoDanRepository hoDanRepository;

    // Thêm Transactional và đổi Exception thành RuntimeException
    @Transactional 
    public PhuongTien dangKyXe(PhuongTien p, Long hoDanId) {
        
        // Kiểm tra biển số trùng
        if (phuongTienRepository.existsByBienSo(p.getBienSo())) {
            throw new IllegalArgumentException("Biển số xe " + p.getBienSo() + " đã được đăng ký!");
        }

        // Tìm HoDan thực tế từ ID
        HoDan hoDan = hoDanRepository.findById(hoDanId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hộ dân với ID: " + hoDanId));

        // Map HoDan vào đối tượng PhuongTien
        p.setHoDan(hoDan); 
        return phuongTienRepository.save(p);
    }
}
