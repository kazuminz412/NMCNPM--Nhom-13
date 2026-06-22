package com.bluemoon.dto;

public class KhoanThuDTO {
    private Long danhMucPhiId;
    private String tenKhoanThu;
    private String loai;
    private Long soTien;
    private String thangApDung;
    private Long soHo;

    public KhoanThuDTO() {}

    public KhoanThuDTO(Long danhMucPhiId, String tenKhoanThu, String loai, Long soTien, String thangApDung, Long soHo) {
        this.danhMucPhiId = danhMucPhiId;
        this.tenKhoanThu = tenKhoanThu;
        this.loai = loai;
        this.soTien = soTien;
        this.thangApDung = thangApDung;
        this.soHo = soHo;
    }

    public Long getDanhMucPhiId() { return danhMucPhiId; }
    public void setDanhMucPhiId(Long danhMucPhiId) { this.danhMucPhiId = danhMucPhiId; }
    public String getTenKhoanThu() { return tenKhoanThu; }
    public void setTenKhoanThu(String tenKhoanThu) { this.tenKhoanThu = tenKhoanThu; }
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
    public Long getSoTien() { return soTien; }
    public void setSoTien(Long soTien) { this.soTien = soTien; }
    public String getThangApDung() { return thangApDung; }
    public void setThangApDung(String thangApDung) { this.thangApDung = thangApDung; }
    public Long getSoHo() { return soHo; }
    public void setSoHo(Long soHo) { this.soHo = soHo; }
}
