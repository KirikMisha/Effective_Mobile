package org.example.effectivemobile.config;

import org.example.effectivemobile.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JWTRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                if (jwtTokenUtils.isTokenValid(jwt)) {
                    Long id = Long.valueOf(jwtTokenUtils.getId(jwt));
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        UsernamePasswordAuthenticationToken token =
                                new UsernamePasswordAuthenticationToken(id, null, new ArrayList<>());
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                } else {
                    throw new SignatureException("Invalid JWT signature");
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Время жизни токена истекло");
                return;
            } catch (SignatureException e) {
                log.debug("Подпись некорректна");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Некорректная подпись токена");
                return;
            } catch (NumberFormatException e) {
                log.debug("Некорректный формат JWT или ID");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Некорректный формат JWT или ID");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
