package com.graduation.common.jwt.config;


import com.graduation.common.jwt.JwtUserContext;
import com.graduation.common.jwt.JwtUserContextHolder;
import com.graduation.common.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;

    public JwtAuthInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "缺少 Authorization");
        }

        String token = authorization;
        if (authorization.startsWith("Bearer ")) {
            token = authorization.substring("Bearer ".length()).trim();
        }
        if (token.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization 无效");
        }

        Claims claims;
        try {
            claims = jwtUtils.parseClaims(token);
        } catch (ExpiredJwtException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 已过期", ex);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 无效", ex);
        }

        Long userId = claims.get("userId", Long.class);
        String username = claims.get("username", String.class);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 缺少 userId");
        }

        JwtUserContextHolder.set(JwtUserContext.builder()
                .userId(userId)
                .username(username)
                .build());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        JwtUserContextHolder.clear();
    }
}
