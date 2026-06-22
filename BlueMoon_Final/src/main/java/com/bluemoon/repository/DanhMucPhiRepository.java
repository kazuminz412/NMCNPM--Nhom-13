package com.bluemoon.repository;

import com.bluemoon.model.DanhMucPhi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhMucPhiRepository extends JpaRepository<DanhMucPhi, Long> {

    // 1. Dùng cho luồng KHỞI TẠO HÓA ĐƠN:
    // Lọc ra các loại phí theo loại (VD: "bat_buoc") và trạng thái (VD: "dang_ap_dung")
    List<DanhMucPhi> findByLoaiPhiAndTrangThai(String loaiPhi, String trangThai);

    // 2. Dùng cho luồng THÊM KHOẢN PHÍ (Validation):
    boolean existsByTenPhi(String tenPhi);
}
