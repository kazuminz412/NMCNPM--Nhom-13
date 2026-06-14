package com.bluemoon.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String username;
    private String hoTen;
    private String role;
    private Long hoDanId; // Bắt buộc phải có để phân biệt nhà các hộ dân
}
