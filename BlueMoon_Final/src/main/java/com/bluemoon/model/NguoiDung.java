package com.bluemoon.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nguoi_dung")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class NguoiDung extends BaseEntity {

    @Column(name = "ten_dang_nhap", nullable = false, unique = true, length = 50)
    private String username;

    // Giữ nguyên annotation xịn sò của bạn
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "mat_khau", nullable = false)
    private String password;

    // [BỔ SUNG 1]: Tên hiển thị trên góc phải màn hình Frontend
    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @Column(name = "vai_tro", nullable = false)
    private String role; 

    // [BỔ SUNG 2]: Để Admin có thể khóa tài khoản
    @Column(name = "trang_thai")
    private Boolean trangThai = true;

    // Giữ nguyên liên kết đến 1 người cụ thể của bạn
    @Column(name = "nhan_khau_id", unique = true)
    private Long nhanKhauId; // Đổi Integer thành Long cho đồng bộ với BaseEntity

    // [BỔ SUNG 3]: Cực kỳ quan trọng để API /api/me/hoa-don lấy được đúng hóa đơn của nhà đó
    @Column(name = "ho_dan_id")
    private Long hoDanId; 
}
