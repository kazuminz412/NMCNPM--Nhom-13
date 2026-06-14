package com.bluemoon.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chi_tiet_hoa_don")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ChiTietHoaDon extends BaseEntity {

    // 1. Ánh xạ khóa ngoại tới bảng Hóa Đơn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoa_don_id", nullable = false)
    private HoaDon hoaDon;

    // 2. Ánh xạ khóa ngoại tới bảng Danh Mục Phí (Đã đổi tên từ KhoanThu)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "danh_muc_phi_id", nullable = false)
    private DanhMucPhi danhMucPhi;

    // 3. Số lượng (VD: 65 m2, 2 xe máy, 4 người)
    @Column(name = "so_luong", nullable = false)
    private Long soLuong;
    
    // 4. Lưu lại giá tiền TẠI THỜI ĐIỂM XUẤT HÓA ĐƠN để chống sai lệch báo cáo sau này
    @Column(name = "don_gia", nullable = false)
    private Long donGia;

    // 5. Đổi Double thành Long
    @Column(name = "thanh_tien", nullable = false)
    private Long thanhTien;
}
