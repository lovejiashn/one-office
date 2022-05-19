package com.jiashn.oneofficesso.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author jiangjs
 * @date 2022-05-19 23:28
 */
@Configuration
public class IgnoringSourceWebConfigurer implements WebMvcConfigurer {
    @Value("${security.release-url}")
    private String releaseUrl;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SpringSecurityIgnoringSourceInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(releaseUrl.split(","));
    }
}
