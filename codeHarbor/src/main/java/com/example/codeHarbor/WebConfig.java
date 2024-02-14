package com.example.codeHarbor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // cors 오류 해결
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:5173") // 이 부분을 localhost로 처리했을 때 cors 문제 발생하는 경우 있음
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true); // 쿠키나 인증 헤더 정보를 받아올지 결정
    }
}
