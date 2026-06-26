package com.bluemoon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CanHoDTO {
    private String maCanHo; // e.g. A1-101
    private String toaNha;  // e.g. A1
    private String trangThai; // "da_co_chu" or "chua_co_chu"
    private String tenChuHo; // Name of owner if exists
    private Double dienTich; // M2
}
