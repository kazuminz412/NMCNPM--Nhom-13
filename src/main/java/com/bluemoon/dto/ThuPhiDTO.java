package com.bluemoon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThuPhiDTO {
    private Long id;
    private String hoDan; // Ví dụ: "Nguyễn Văn An – A1-204"
    private String khoanThu; // Ví dụ: "Phí vệ sinh T5/2026"
    private Long soTien;
    private String hanNop; // Tháng của hóa đơn (Ví dụ: 2026-05-31)
    private String trangThai; // "da_dong" hoặc "chua_dong"
    private Long hoDanId;
}
