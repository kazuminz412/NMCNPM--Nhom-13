package com.bluemoon.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        try {
            // 1. Lấy Header Authorization
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 2. Tách lấy chuỗi Token
            String jwt = authHeader.substring(7);
            String username = jwtUtils.extractUsername(jwt);

            // 3. Kiểm tra User chưa đăng nhập trong Context
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // 🔒 CHỐT CHẶN QUAN TRỌNG: Kiểm tra Token có hợp lệ và còn hạn không
                if (jwtUtils.isTokenValid(jwt)) {
                    String role = jwtUtils.extractRole(jwt); 
                    
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 4. Chính thức cấp thẻ vào cửa
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Bắt mọi lỗi liên quan đến Token (Hết hạn, sai chữ ký, chuỗi rác...)
            // Không ném Exception ra ngoài để tránh sập Server, chỉ log lại.
            // Hệ thống sẽ tự động chặn request ở bước tiếp theo vì Context không có Authentication
            System.err.println("Lỗi xác thực JWT: " + e.getMessage());
        }

        // 5. Cho phép request đi tiếp đến Controller (hoặc bị chặn nếu chưa có Authentication)
        filterChain.doFilter(request, response);
    }
}
