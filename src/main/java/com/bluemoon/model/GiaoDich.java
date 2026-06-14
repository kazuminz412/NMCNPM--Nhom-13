package com.bluemoon.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "giao_dich")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class GiaoDich extends BaseEntity {

    // Thêm mã giao dịch cho Biên lai
    @Column(name = "ma_giao_dich", nullable = false, unique = true)
    private String maGiaoDich;

    // Chuẩn hóa quan hệ JPA thay vì chỉ lưu ID thường
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoa_don_id", nullable = false)
    private HoaDon hoaDon;

    // Map thêm HoDan vào đây để truy vấn lịch sử cho Cư Dân cực nhanh (không cần JOIN vòng)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ho_dan_id", nullable = false)
    private HoDan hoDan;

    // Sửa Double thành Long để tính tiền chuẩn xác
    @Column(name = "so_tien", nullable = false)
    private Long soTien;

    @Column(name = "phuong_thuc", nullable = false)
    private String phuongThuc; 

    @Column(name = "thoi_gian", nullable = false)
    private LocalDateTime thoiGian;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
}
