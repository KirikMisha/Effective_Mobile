package org.example.effectivemobile.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.example.effectivemobile.models.Users;
import org.example.effectivemobile.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenUtils {

    private final UserService userService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    public JwtTokenUtils(UserService userService) {
        this.userService = userService;
    }

    public String generateToken(Long id) {

        Date issuedDate = new Date();
        Date expirationDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        Users user = userService.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(issuedDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getId(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException | ExpiredJwtException e) {
            return false;
        }
    }
}
