package com.bluemoon.repository;

import com.bluemoon.model.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    // ==========================================
    // NHÓM 1: KHỞI TẠO HÓA ĐƠN ĐỊNH KỲ
    // ==========================================
    
    // Kiểm tra xem tháng này đã chốt sổ chưa để chống tạo trùng
    boolean existsByThang(String thang);

    // Gọi Stored Procedure để tính tiền tự động dưới Database
    @Modifying
    @Transactional
    @Query(value = "CALL sp_TaoHoaDonDinhKy(:thang)", nativeQuery = true)
    void taoHoaDonDinhKy(@Param("thang") String thang);


    // ==========================================
    // NHÓM 2: PHỤC VỤ DASHBOARD THỐNG KÊ
    // ==========================================
    
    // Tính tổng tiền đã thu được trong tháng
    @Query("SELECT COALESCE(SUM(h.tongTien), 0) FROM HoaDon h WHERE h.thang = :thang AND h.trangThai = 'da_thanh_toan'")
    Long sumDoanhThuByThang(@Param("thang") String thang);

    // Đếm số hộ đã hoàn thành nộp phí
    @Query("SELECT COUNT(h) FROM HoaDon h WHERE h.thang = :thang AND h.trangThai = 'da_thanh_toan'")
    Long countHoaDonDaThuByThang(@Param("thang") String thang);

    // Đếm tổng số tờ hóa đơn được phát hành trong tháng
    @Query("SELECT COUNT(h) FROM HoaDon h WHERE h.thang = :thang")
    Long countTotalHoaDonByThang(@Param("thang") String thang);


    // ==========================================
    // NHÓM 3: TRUY VẤN LẤY DANH SÁCH HIỂN THỊ
    // ==========================================

    // Lấy toàn bộ hóa đơn của 1 tháng (Dành cho màn hình Quản lý Hóa đơn)
    List<HoaDon> findByThang(String thang);

    // Lấy danh sách các nhà CÒN NỢ TIỀN (Phục vụ màn hình "Thu Phí" của Kế toán)
    // VD: findByTrangThai("chua_thanh_toan")
    List<HoaDon> findByTrangThai(String trangThai);

    // Lọc chi tiết: Lấy các nhà nợ tiền trong 1 tháng cụ thể
    List<HoaDon> findByThangAndTrangThai(String thang, String trangThai);

    // Lấy toàn bộ hóa đơn của riêng 1 hộ dân (Dành cho "Góc Cư Dân" xem lịch sử nhà mình)
    List<HoaDon> findByHoDanId(Long hoDanId);
}
