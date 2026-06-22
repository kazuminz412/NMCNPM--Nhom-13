package com.bluemoon.repository;

import com.bluemoon.model.ChiTietHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Long> {

    // Lấy toàn bộ chi tiết phí của một Hóa Đơn cụ thể
    List<ChiTietHoaDon> findByHoaDonId(Long hoaDonId);
}
