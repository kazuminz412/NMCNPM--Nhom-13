package com.bluemoon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "nhan_khau")
@Getter @Setter
public class NhanKhau extends BaseEntity { 
    
    @Column(name = "ho_ten", nullable = false)
    private String hoTen;
    
    @Column(name = "cccd", unique = true)
    private String soCCCD;
    
    @Column(name = "ngay_sinh", nullable = false)
    private LocalDate ngaySinh;
    
    @Column(name = "gioi_tinh", nullable = false)
    private String gioiTinh;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;
    
    @Column(name = "quan_he_chu_ho")
    private String quanHe;
    
    @Column(name = "trang_thai_cu_tru")
    private String trangThai; 
    
    // Đã thêm 2 trường bị thiếu so với DB
    @Column(name = "ngay_chuyen_den")
    private LocalDate ngayChuyenDen;

    @Column(name = "ngay_chuyen_di")
    private LocalDate ngayChuyenDi;
    
    // Đã XÓA trường ghiChu vì DB không có

    // SỬA LỖI 3: Map hoDanId để Frontend dễ lấy dữ liệu (không dùng Transient)
    @Column(name = "ho_dan_id", insertable = false, updatable = false)
    private Long hoDanId;

    @org.hibernate.annotations.Formula("(SELECT hd.ten_chu_ho FROM ho_dan hd WHERE hd.id = ho_dan_id)")
    private String tenChuHo;

    @org.hibernate.annotations.Formula("(SELECT hd.so_phong FROM ho_dan hd WHERE hd.id = ho_dan_id)")
    private String canHo;

    // SỬA LỖI 1: Chặn đứng vòng lặp vô hạn JSON
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ho_dan_id")
    @JsonIgnore 
    private HoDan hoDan;
}
