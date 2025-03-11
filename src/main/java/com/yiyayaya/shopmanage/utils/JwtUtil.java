package com.yiyayaya.shopmanage.utils;

import com.yiyayaya.shopmanage.entity.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret = "yiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiya";

    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24; // 24小时过期

    public String createToken(Users user) {
        return Jwts.builder()
            .claim("data",user.getUserId()+"")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
            .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
            .compact();
    }

    public Integer getUserId(String token) {
        try {
            String userId = Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8)) // 使用原始字符串
                .parseClaimsJws(token).getBody().get("data", String.class);
            return Integer.parseInt(userId);
        } catch (Exception e) {
            return null;
        }
    }
} 