package com.graduation.common.jwt.config;


import com.graduation.common.jwt.JwtUtils;
import com.graduation.common.jwt.properties.JwtProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtAuthAutoConfiguration implements WebMvcConfigurer {

    private final JwtProperties jwtProperties;
    private final JwtAuthInterceptor jwtAuthInterceptor;

    public JwtAuthAutoConfiguration(JwtProperties jwtProperties, JwtAuthInterceptor jwtAuthInterceptor) {
        this.jwtProperties = jwtProperties;
        this.jwtAuthInterceptor = jwtAuthInterceptor;
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns(jwtProperties.getIncludePaths())
                .excludePathPatterns(jwtProperties.getExcludePaths());
    }
}