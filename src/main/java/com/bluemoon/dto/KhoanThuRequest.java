package com.bluemoon.dto;

public class KhoanThuRequest {
    private Long danhMucPhiId;
    private String tenKhoanThu;
    private String loai;
    private String cachTinh;
    private Long soTien;
    private String thangApDung;
    private String ghiChu;

    public KhoanThuRequest() {}

    public Long getDanhMucPhiId() { return danhMucPhiId; }
    public void setDanhMucPhiId(Long danhMucPhiId) { this.danhMucPhiId = danhMucPhiId; }
    public String getTenKhoanThu() { return tenKhoanThu; }
    public void setTenKhoanThu(String tenKhoanThu) { this.tenKhoanThu = tenKhoanThu; }
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
    public String getCachTinh() { return cachTinh; }
    public void setCachTinh(String cachTinh) { this.cachTinh = cachTinh; }
    public Long getSoTien() { return soTien; }
    public void setSoTien(Long soTien) { this.soTien = soTien; }
    public String getThangApDung() { return thangApDung; }
    public void setThangApDung(String thangApDung) { this.thangApDung = thangApDung; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
