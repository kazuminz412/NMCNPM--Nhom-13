package com.bluemoon.repository;

import com.bluemoon.model.NhatKyHoatDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhatKyHoatDongRepository extends JpaRepository<NhatKyHoatDong, Long> {
    // Câu lệnh "thần thánh" lấy 5 hành động mới nhất
    List<NhatKyHoatDong> findTop5ByOrderByThoiGianDesc();
}
