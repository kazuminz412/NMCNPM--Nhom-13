package com.bluemoon.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // SỬA: Thêm import bị thiếu
import jakarta.persistence.*;                       // SỬA: javax -> jakarta (Spring Boot 3)

@Entity
@Table(name = "phuong_tien")
public class PhuongTien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bienSo; // Biển số xe

    private String loaiXe; // Ô tô, Xe máy...
    private String mauXe;

    @Column(name = "hang_xe")
    private String hangXe;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    // Lưu hoDanId để truy vấn nhanh (không cần join bảng)
    @Column(name = "ho_dan_id", insertable = false, updatable = false)
    private Long hoDanId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ho_dan_id", nullable = false)
    private HoDan hoDan;

    // Constructors
    public PhuongTien() {}

    public PhuongTien(String bienSo, String loaiXe, String mauXe, HoDan hoDan) {
        this.bienSo = bienSo;
        this.loaiXe = loaiXe;
        this.mauXe = mauXe;
        this.hoDan = hoDan;
    }

    // Getters and Setters — ĐÃ BỔ SUNG ĐẦY ĐỦ
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBienSo() { return bienSo; }
    public void setBienSo(String bienSo) { this.bienSo = bienSo; }

    public String getLoaiXe() { return loaiXe; }
    public void setLoaiXe(String loaiXe) { this.loaiXe = loaiXe; }

    public String getMauXe() { return mauXe; }
    public void setMauXe(String mauXe) { this.mauXe = mauXe; }

    public String getHangXe() { return hangXe; }
    public void setHangXe(String hangXe) { this.hangXe = hangXe; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public Long getHoDanId() { return hoDanId; }

    public HoDan getHoDan() { return hoDan; }
    public void setHoDan(HoDan hoDan) { this.hoDan = hoDan; }

    @org.hibernate.annotations.Formula("(SELECT hd.ten_chu_ho FROM ho_dan hd WHERE hd.id = ho_dan_id)")
    private String tenChuHo;

    @org.hibernate.annotations.Formula("(SELECT hd.so_phong FROM ho_dan hd WHERE hd.id = ho_dan_id)")
    private String canHo;

    @com.fasterxml.jackson.annotation.JsonProperty("tenChuHo")
    public String getTenChuHo() {
        return tenChuHo;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("canHo")
    public String getCanHo() {
        return canHo;
    }
}
