package com.bluemoon.repository;

import com.bluemoon.model.PhuongTien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhuongTienRepository extends JpaRepository<PhuongTien, Long> {
    // Kiểm tra trùng biển số
    boolean existsByBienSo(String bienSo);
    
    // Tìm danh sách xe theo ID hộ dân
    List<PhuongTien> findByHoDanId(Long hoDanId);
}
