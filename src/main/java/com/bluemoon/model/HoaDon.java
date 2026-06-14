package com.bluemoon.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hoa_don")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HoaDon extends BaseEntity {

    // 1. Ánh xạ trực tiếp tới Hộ Dân (Thay vì dùng Long hoDanId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ho_dan_id", nullable = false)
    private HoDan hoDan;

    // 2. Đổi tên biến cho khớp Frontend
    @Column(nullable = false)
    private String thang; // VD: "2026-05"

    // 3. Đổi Double thành Long để tránh sai số tiền tệ
    @Column(name = "tong_tien")
    private Long tongTien = 0L; // Mặc định hóa đơn mới tạo là 0đ

    @Column(name = "trang_thai")
    private String trangThai; // chua_thanh_toan / da_thanh_toan

    // 4. BỔ SUNG QUAN TRỌNG: Mối quan hệ 1-Nhiều với bảng Chi Tiết
    // CascadeType.ALL giúp khi lưu HoaDon thì tự động lưu luôn các ChiTietHoaDon bên trong
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietHoaDon> chiTietList = new ArrayList<>();
}
