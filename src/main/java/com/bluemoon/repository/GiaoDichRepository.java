package com.bluemoon.repository;

import com.bluemoon.model.GiaoDich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiaoDichRepository extends JpaRepository<GiaoDich, Long> {

    // Lấy danh sách giao dịch của 1 hộ dân, sắp xếp mới nhất lên đầu
    List<GiaoDich> findByHoDanIdOrderByThoiGianDesc(Long hoDanId);
    
    // Lấy giao dịch mới nhất của 1 hóa đơn
    Optional<GiaoDich> findTopByHoaDonIdOrderByThoiGianDesc(Long hoaDonId);

    // Tra cứu chi tiết 1 giao dịch dựa vào mã biên lai
    Optional<GiaoDich> findByMaGiaoDich(String maGiaoDich);

    // Tính tổng doanh thu tháng hiện tại (Dành cho ThongKeService)
    @Query(value = "SELECT COALESCE(SUM(so_tien), 0) FROM giao_dich WHERE DATE_FORMAT(thoi_gian, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m')", nativeQuery = true)
    Long tinhTongDoanhThuThangHienTai();
}
