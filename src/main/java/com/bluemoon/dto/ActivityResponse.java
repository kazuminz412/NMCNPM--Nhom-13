package com.bluemoon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {
    private String dot;  // Mã màu (VD: #1E8449, #C0392B)
    private String txt;  // Nội dung hành động (Cho phép chứa thẻ HTML <b>)
    private String time; // Chuỗi hiển thị (VD: "10 phút trước")
}
