package com.geoparty.spring_boot.security.config;

import com.geoparty.spring_boot.security.jwt.AuthInterceptor;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(url = "https://dogeoparty.duckdns.org", description = "Default Server URL"),
                @Server(url = "http://localhost:8080", description = "Local Development Server") // 추가된 로컬 서버 URL
        }
)
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**") // 특정 경로에만 적용
                .excludePathPatterns("/api/auth/**", "/api/members/**"); // 제외할 경로

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://dogeoparty.duckdns.org", "https://d2ahug1uc3qjo6.cloudfront.net")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true); // 인증 정보 포함을 허용
    }
}
