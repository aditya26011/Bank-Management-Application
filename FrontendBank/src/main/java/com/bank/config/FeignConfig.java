package com.bank.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            var requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
                HttpSession session = servletRequestAttributes.getRequest().getSession(false);
                if (session != null) {
                    Object tokenObj = session.getAttribute("JWT_TOKEN");
                    if (tokenObj instanceof String token && !token.isBlank()) {
                        template.header("Authorization", "Bearer " + token);
                    }
                }
            }
        };
    }


}
