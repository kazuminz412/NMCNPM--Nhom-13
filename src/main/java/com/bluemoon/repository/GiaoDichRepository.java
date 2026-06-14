package com.bluemoon.repository;

import com.bluemoon.model.GiaoDich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiaoDichRepository extends JpaRepository<GiaoDich, Long> {

    // Lấy danh sách giao dịch của 1 hộ dân, sắp xếp mới nhất lên đầu
    List<GiaoDich> findByHoDanIdOrderByThoiGianDesc(Long hoDanId);

    // Tra cứu chi tiết 1 giao dịch dựa vào mã biên lai
    Optional<GiaoDich> findByMaGiaoDich(String maGiaoDich);
}
