package com.bluemoon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "danh_muc_phi") 
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DanhMucPhi extends BaseEntity {

    // Đã sửa tên biến thành tenPhi để hứng đúng dữ liệu JSON từ Frontend
    @Column(name = "ten_phi", nullable = false)
    private String tenPhi; 

    @Column(name = "loai_phi", nullable = false)
    private String loaiPhi; // bat_buoc / tu_nguyen

    // Đơn giá dùng Long để tránh sai số tiền tệ
    @Min(value = 0, message = "Đơn giá không được phép là số âm!")
    @Column(name = "don_gia", nullable = false)
    private Long donGia; 

    @Column(name = "don_vi_tinh", nullable = false)
    private String donViTinh; // m2, xe, ho, nguoi

    @Column(name = "trang_thai")
    private String trangThai; // dang_ap_dung / tam_ngung

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "chu_ky")
    private String chuKy; // Có thể giữ lại nếu nhóm muốn dùng
}
