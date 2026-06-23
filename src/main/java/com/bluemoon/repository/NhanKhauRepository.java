package com.bluemoon.repository;

import com.bluemoon.model.NhanKhau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhanKhauRepository extends JpaRepository<NhanKhau, Long> {
    
    // 1. Tìm nhân khẩu theo CCCD (Code cũ của bạn - Rất tốt)
    boolean existsBySoCCCD(String soCCCD);
    
    // 2. Lấy danh sách thành viên trong 1 hộ gia đình (Code cũ của bạn - Rất tốt)
    List<NhanKhau> findByHoDanId(Long hoDanId);

    // 3. Tìm kiếm cư dân theo Tên (Tìm gần đúng - Like '%ten%')
    // Dùng cho thanh tìm kiếm (Search Bar) trên giao diện Frontend
    List<NhanKhau> findByHoTenContainingIgnoreCase(String hoTen);

    // 4. Lọc cư dân theo Trạng thái cư trú (VD: Lấy toàn bộ người 'TAM_VANG')
    // Dùng cho bộ lọc (Filter) của Tổ trưởng
    List<NhanKhau> findByTrangThai(String trangThai);
    
    // 5. Kiểm tra xem 1 hộ dân đã có Chủ hộ chưa (Tránh 1 nhà có 2 chủ hộ)
    boolean existsByHoDanIdAndQuanHe(Long hoDanId, String quanHe);
}
