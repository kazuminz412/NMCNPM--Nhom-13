package com.bluemoon.dto;

import lombok.Data;

@Data
public class ThanhToanRequest {
    private Long hoaDonId;
    private Long soTienKhach;
    private Long soTienThanhToan;
    private Long tienThua;
    private String phuongThuc;
    private String ghiChu;
}
