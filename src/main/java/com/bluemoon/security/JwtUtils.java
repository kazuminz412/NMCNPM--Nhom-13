package com.bluemoon.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    // Chuỗi secret key dùng để ký mã hóa token (tối thiểu 32 ký tự)
    private final String SECRET_KEY = "chuan-bi-mot-chuoi-bi-mat-that-dai-va-an-toan-cho-blue-moon";
    private final long EXPIRATION_TIME = 86400000; // Token có hạn trong 1 ngày (ms)

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // Hàm tạo mã Token (Đã nâng cấp: Đóng dấu Quyền và ID hộ dân vào vé)
    public String generateToken(String username, String role, Long hoDanId) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)       // 👈 Chỗ này cực quan trọng: Nhét quyền vào vé
                .claim("hoDanId", hoDanId) // 👈 Nhét ID hộ dân vào vé
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }


    // Hàm giải mã Token để lấy username (Sửa dứt điểm lỗi parserBuilder)
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Hàm kiểm tra Token hợp lệ
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // --- CÁC HÀM BỔ SUNG THEO YÊU CẦU CỦA ĐỒNG ĐỘI ---

    // 1. Đồng đội gọi hàm extractUsername thay vì getUsernameFromToken
    public String extractUsername(String token) {
        return getUsernameFromToken(token); // Gọi lại hàm cũ cho nhanh
    }

    // 2. Đồng đội gọi hàm isTokenValid thay vì validateToken
    public boolean isTokenValid(String token) {
        return validateToken(token); // Gọi lại hàm cũ cho nhanh
    }

    // 3. Hàm lấy Role (Quyền) từ Token
    public String extractRole(String token) {
        try {
            return io.jsonwebtoken.Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("role", String.class);
        } catch (Exception e) {
            return "CU_DAN"; // Nếu token không có role thì mặc định trả về cư dân để không bị lỗi
        }
    }

    // 4. Hàm lấy ID Hộ dân từ Token
    public Long extractHoDanId(String token) {
        try {
            return io.jsonwebtoken.Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("hoDanId", Long.class);
        } catch (Exception e) {
            return 1L; // Trả về ID tạm để code biên dịch qua cửa
        }
    }
}
