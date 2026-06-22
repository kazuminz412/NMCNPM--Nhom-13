package com.bluemoon.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Tạm thời hardcode để chạy đồ án, thực tế đi làm sẽ để trong
    // application.properties
    private final String SECRET = "khoa_bi_mat_rat_dai_cua_he_thong_quan_ly_ho_dan_bluemoon_123456";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 1. CẤP PHÁT TOKEN (Đã bổ sung hoDanId)
    public String generateToken(String username, String role, Long hoDanId) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .claim("hoDanId", hoDanId) // Cực kỳ quan trọng để truy xuất dữ liệu Cư Dân
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Hạn 10 tiếng
                .signWith(key)
                .compact();
    }

    // 2. LẤY THÔNG TIN TỪ TOKEN
    public String extractUsername(String token) {
        return Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build().parseSignedClaims(token).getPayload()
                .getSubject();
    }

    public String extractRole(String token) {
        return Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build().parseSignedClaims(token).getPayload()
                .get("role", String.class);
    }

    // Đã bổ sung: Hàm lấy Mã hộ dân từ Token cho API đuôi /me
    public Long extractHoDanId(String token) {
        Object idObj = Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build().parseSignedClaims(token)
                .getPayload().get("hoDanId");
        if (idObj != null) {
            return Long.parseLong(idObj.toString());
        }
        return null; // Quản trị viên và Kế toán sẽ không có mã này
    }

    // 3. Đã bổ sung: HÀM KIỂM TRA TOKEN HỢP LỆ (Phục vụ cho Filter)
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Nếu token hết hạn, bị sửa đổi chữ ký, hoặc rỗng -> Trả về false ngay
            return false;
        }
    }
}
