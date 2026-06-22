package com.bluemoon.repository;

import com.bluemoon.model.HoDan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoDanRepository extends JpaRepository<HoDan, Long> {
    boolean existsByMaHoKhau(String maHoKhau);
}
