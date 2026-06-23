package com.bluemoon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "ho_dan")
@Getter @Setter
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HoDan extends BaseEntity {
    
    private String maHoKhau;      
    private String soPhong;
    private String toaNha;
    @Column(name = "dien_tich_m2")
    private Double dienTichM2;
    private String tenChuHo;     
    private LocalDate ngayCapHoKhau;
    private String soDienThoai;
    @org.hibernate.annotations.Formula("(SELECT CASE WHEN count(hd.id) = 0 THEN 'da_dong' WHEN sum(CASE WHEN hd.trang_thai = 'da_thanh_toan' THEN 1 ELSE 0 END) = count(hd.id) THEN 'da_dong' WHEN sum(CASE WHEN hd.trang_thai = 'chua_thanh_toan' THEN 1 ELSE 0 END) = count(hd.id) THEN 'chua_dong' ELSE 'mot_phan' END FROM hoa_don hd WHERE hd.ho_dan_id = id)")
    private String trangThai;  
    
    @org.hibernate.annotations.Formula("(SELECT count(*) FROM nhan_khau nk WHERE nk.ho_dan_id = id)")
    private Integer soNhanKhau;
}

