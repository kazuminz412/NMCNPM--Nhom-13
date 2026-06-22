package com.bluemoon.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nhat_ky_hoat_dong")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class NhatKyHoatDong {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "noi_dung", nullable = false)
    private String noiDung;

    @Column(name = "mau_sac")
    private String mauSac; // Lưu mã HEX, mặc định nên để Xanh lá hoặc Xanh dương

    @Column(name = "thoi_gian", nullable = false)
    private LocalDateTime thoiGian;
}
