package com.graduation.common.jwt.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private String secret = "mySecretKey123456789012345678901234567890";
    private Long expiration = 86400L;
    private List<String> includePaths = Collections.singletonList("/api/**");
    private List<String> excludePaths = Arrays.asList(
            "/api/users/login",
            "/api/users/register",
            "/api/users/query",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/error"
    );
}