package com.bluemoon.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tai_khoan")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TaiKhoan extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password; // Bắt buộc phải lưu chuỗi đã mã hóa (BCrypt)

    @Column(name = "ho_ten", nullable = false)
    private String hoTen; // Hiển thị trên góc phải màn hình Frontend

    @Column(nullable = false, length = 20)
    private String role; // Chỉ nhận 3 giá trị: ADMIN, KE_TOAN, CU_DAN

    @Column(name = "trang_thai")
    private Boolean trangThai = true; // true: Hoạt động, false: Bị khóa

    // Cột Khóa Ngoại: Cho phép NULL vì ADMIN và KE_TOAN thì không thuộc hộ dân nào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ho_dan_id")
    private HoDan hoDan; 
}
