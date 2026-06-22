package com.bluemoon.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hoa_don")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HoaDon extends BaseEntity {

    // 1. Ãnh xáº¡ trá»±c tiáº¿p tá»›i Há»™ DÃ¢n (Thay vÃ¬ dÃ¹ng Long hoDanId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ho_dan_id", nullable = false)
    private HoDan hoDan;

    // 2. Äá»•i tÃªn biáº¿n cho khá»›p Frontend
    @Column(nullable = false)
    private String thang; // VD: "2026-05"

    // 3. Äá»•i Double thÃ nh Long Ä‘á»ƒ trÃ¡nh sai sá»‘ tiá»n tá»‡
    @Column(name = "tong_tien")
    private Long tongTien = 0L; // Máº·c Ä‘á»‹nh hÃ³a Ä‘Æ¡n má»›i táº¡o lÃ  0Ä‘

    @Column(name = "trang_thai")
    private String trangThai; // chua_thanh_toan / da_thanh_toan

    // 4. Bá»” SUNG QUAN TRá»ŒNG: Má»‘i quan há»‡ 1-Nhiá»u vá»›i báº£ng Chi Tiáº¿t
    // CascadeType.ALL giÃºp khi lÆ°u HoaDon thÃ¬ tá»± Ä‘á»™ng lÆ°u luÃ´n cÃ¡c ChiTietHoaDon bÃªn trong
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietHoaDon> chiTietList = new ArrayList<>();
}

