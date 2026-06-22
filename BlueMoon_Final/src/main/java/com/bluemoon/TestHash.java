package com.bluemoon;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class TestHash {
    public static void main(String[] args) {
        String SECRET = "khoa_bi_mat_rat_dai_cua_he_thong_quan_ly_ho_dan_bluemoon_123456";
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJjdWRhbjAxIiwicm9sZSI6IkNVX0RBTiIsImhvRGFuSWQiOjEsImlhdCI6MTc4MjA5OTA3NiwiZXhwIjoxNzgyMTM1MDc2fQ.-ZKlgO7kY4FEzHva_H2Bac8nVW6pJY6yejlpKPhazv98IskThYjF2UTc_qrtKZoE";

        try {
            Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build().parseSignedClaims(token);
            System.out.println("Valid!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
