package com.bluemoon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {
    private Long hodan;
    private Long nhankhau;
    private Long xe;
    private Long thuThang;
    private Double tyLeDaThu;
}
