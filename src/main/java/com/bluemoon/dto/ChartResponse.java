package com.bluemoon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartResponse {
    private List<String> labels;
    private List<Double> doanhThu;
    private Double tyLeDaThu;
}
