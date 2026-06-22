package com.bluemoon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "danh_muc_phi") 
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DanhMucPhi extends BaseEntity {

    // ÄÃ£ sá»­a tÃªn biáº¿n thÃ nh tenPhi Ä‘á»ƒ há»©ng Ä‘Ãºng dá»¯ liá»‡u JSON tá»« Frontend
    @Column(name = "ten_phi", nullable = false)
    private String tenPhi; 

    @Column(name = "loai_phi", nullable = false)
    private String loaiPhi; // bat_buoc / tu_nguyen

    // ÄÆ¡n giÃ¡ dÃ¹ng Long Ä‘á»ƒ trÃ¡nh sai sá»‘ tiá»n tá»‡
    @Min(value = 0, message = "ÄÆ¡n giÃ¡ khÃ´ng Ä‘Æ°á»£c phÃ©p lÃ  sá»‘ Ã¢m!")
    @Column(name = "don_gia", nullable = false)
    private Long donGia; 

    @Column(name = "don_vi_tinh", nullable = false)
    private String donViTinh; // m2, xe, ho, nguoi

    @Column(name = "trang_thai")
    private String trangThai; // dang_ap_dung / tam_ngung

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "chu_ky")
    private String chuKy; // CÃ³ thá»ƒ giá»¯ láº¡i náº¿u nhÃ³m muá»‘n dÃ¹ng
}

